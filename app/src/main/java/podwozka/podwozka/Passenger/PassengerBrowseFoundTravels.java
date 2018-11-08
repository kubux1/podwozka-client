package podwozka.podwozka.Passenger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import podwozka.podwozka.Constants;
import podwozka.podwozka.Driver.DriverBrowseTravelsAdapter;
import podwozka.podwozka.PopUpWindows;
import podwozka.podwozka.R;
import podwozka.podwozka.Rest.APIClient;
import podwozka.podwozka.Rest.TravelService;
import podwozka.podwozka.entity.PlaceDTO;
import podwozka.podwozka.entity.TravelDTO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.TextView;

import com.google.android.gms.common.util.CollectionUtils;

import static podwozka.podwozka.LoginActivity.user;

public class PassengerBrowseFoundTravels extends AppCompatActivity {
    private List<TravelDTO> travelList = new ArrayList<>();
    private RecyclerView recyclerView;
    private DriverBrowseTravelsAdapter mAdapter;
    private static final String TAG = PassengerBrowseFoundTravels.class.getName();
    protected TravelService travelService = APIClient.getTravelService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_travels_list);

        travelList = getIntent().getParcelableArrayListExtra(Constants.TRAVELDTOS);
        if(CollectionUtils.isEmpty(travelList)) {
            TextView noTravels = findViewById(R.id.noTravelsFound);
            noTravels.setVisibility(View.VISIBLE);
        } else {
            recyclerView = findViewById(R.id.recycler_view);

            mAdapter = new DriverBrowseTravelsAdapter(travelList, getApplicationContext());
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);

            recyclerView.addOnItemTouchListener(getPassengerRecyclerListener());
        }
    }

    @Override
    public void onBackPressed() {
        Intent nextScreen = new Intent(PassengerBrowseFoundTravels.this, PassengerFindTravels.class);

        PlaceDTO startPlace = getIntent().getParcelableExtra(Constants.START_PLACE_SAVED);
        if(startPlace != null) {
            nextScreen.putExtra(Constants.START_PLACE_SAVED, startPlace);
        }
        PlaceDTO endPlace = getIntent().getParcelableExtra(Constants.END_PLACE_SAVED);
        if(startPlace != null) {
            nextScreen.putExtra(Constants.END_PLACE_SAVED, endPlace);
        }
        String date = getIntent().getStringExtra(Constants.DATE_SAVED);
        if(date != null) {
            nextScreen.putExtra(Constants.DATE_SAVED, date);
        }
        String time = getIntent().getStringExtra(Constants.TIME_SAVED);
        if(time != null) {
            nextScreen.putExtra(Constants.TIME_SAVED, time);
        }

        startActivity(nextScreen);
        finish();
    }

    private PassangerRecyclerItemClickListener getPassengerRecyclerListener() {
        return new PassangerRecyclerItemClickListener(PassengerBrowseFoundTravels.this,
                recyclerView, new PassangerRecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                final TravelDTO travel = mAdapter.returnTravel(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(PassengerBrowseFoundTravels.this);

                builder.setMessage(getResources()
                        .getString(R.string.sign_up_for_trip_confirmation));

                builder.setNegativeButton(getResources().getString(R.string.no),
                        new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });

                builder.setPositiveButton(getResources().getString(R.string.yes),
                        new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        Call<Void> call = travelService.signUpForTravel(
                                user.getLogin(), travel.getId(), user.getBearerToken());
                        call.enqueue(getFetchCallback());

                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }

            @Override
            public void onLongItemClick(View view, int position) {
                // For future use
            }
        });
    }

    private Callback<Void>  getFetchCallback() {
        return new Callback<Void> () {
            @Override
            public void onResponse(Call<Void>  call, Response<Void>  response) {
                Log.i(TAG, response.message());
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    new PopUpWindows().showAlertWindow(
                            PassengerBrowseFoundTravels.this, null,
                            getResources().getString(R.string.sing_up_success));
                }
                else if (response.code() == HttpURLConnection.HTTP_FORBIDDEN) {
                    new PopUpWindows().showAlertWindow(
                            PassengerBrowseFoundTravels.this, null,
                            getResources().getString(R.string.already_signed_up));
                }
                else if (response.code() == HttpURLConnection.HTTP_NOT_FOUND) {
                    new PopUpWindows().showAlertWindow(
                            PassengerBrowseFoundTravels.this, null,
                            getResources().getString(R.string.trip_stopped_existing));
                }
                else if (response.code() == HttpURLConnection.HTTP_UNAVAILABLE) {
                    new PopUpWindows().showAlertWindow(
                            PassengerBrowseFoundTravels.this, null,
                            getResources().getString(R.string.server_down));
                }
                else {
                    new PopUpWindows().showAlertWindow(
                            PassengerBrowseFoundTravels.this, null,
                            getResources().getString(R.string.unknown_error));
                }
            }

            @Override
            public void onFailure(Call<Void>  call, Throwable t) {
                Log.e(TAG, t.getMessage(), t);
            }
        };
    }
}



