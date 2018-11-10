package podwozka.podwozka.Driver;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.text.ParseException;
import java.util.Date;

import podwozka.podwozka.Constants;
import podwozka.podwozka.R;
import podwozka.podwozka.Rest.APIClient;
import podwozka.podwozka.Rest.TravelService;
import podwozka.podwozka.entity.TravelDTO;

import podwozka.podwozka.entity.TravelUser;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static podwozka.podwozka.LoginActivity.user;

public class DriverBrowseWaitingPassengers extends AppCompatActivity {

    private static final String TAG = DriverBrowseWaitingPassengers.class.getName();
    private TravelService travelService = APIClient.getTravelService();
    private List<TravelUser> travelList = new ArrayList<>();
    private RecyclerView recyclerView;
    private TextView route, date;
    private DriverBrowseComingPassengersAdapter driverBrowseComingPassengersAdapter;
    private DriverBrowsePastPassengersAdapter driverBrowsePastPassengersAdapter;
    private static TravelDTO travelDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_browse_waiting_passengers_list);
        Intent i = getIntent();

        travelDTO = i.getParcelableExtra(Constants.TRAVELDTO);
        recyclerView = findViewById(R.id.recycler_view);
        date = findViewById(R.id.date);
        route = findViewById(R.id.route);

        driverBrowseComingPassengersAdapter = new DriverBrowseComingPassengersAdapter(travelList, getApplicationContext());
        driverBrowsePastPassengersAdapter = new DriverBrowsePastPassengersAdapter(travelList, getApplicationContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(
                getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        if (isFromFuture(travelDTO))
            recyclerView.setAdapter(driverBrowseComingPassengersAdapter);
        else
            recyclerView.setAdapter(driverBrowsePastPassengersAdapter);

        Call<List<TravelUser>> call = travelService.getSignedUpPassenger(
                user.getLogin(), travelDTO.getId(), user.getBearerToken());
        call.enqueue(getFetchCallback());

        String routeString = travelDTO.getStartPlace().getName() + " - " +
                travelDTO.getEndPlace().getName();
        route.setText(routeString);

        String dateString = changeDateFormat(travelDTO.getPickUpDatetime());
        date.setText(dateString);
    }

    @Override
    public void onBackPressed() {
        Intent nextScreen = new Intent(DriverBrowseWaitingPassengers.this,
                DriverTravelEditor.class);
        nextScreen.putExtra(Constants.TRAVELDTO, travelDTO);
        startActivity(nextScreen);
        finish();
    }

    private boolean isFromFuture(TravelDTO travelDTO) {
        Date travelDate = null;
        Date now = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            travelDate = dateFormat.parse(travelDTO.getPickUpDatetime());
            now = new Date();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return !now.after(travelDate);
    }


    private String changeDateFormat(String strDate){
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date date = dateFormat.parse(strDate);

            dateFormat = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
            return dateFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return strDate;
    }

    private Callback<List<TravelUser>> getFetchCallback() {
        return new Callback<List<TravelUser>>() {
            @Override
            public void onResponse(Call<List<TravelUser>> call, Response<List<TravelUser>> response) {
                Log.i(TAG, response.message());
                if(response.isSuccessful()) {
                    driverBrowseComingPassengersAdapter.update(response.body());
                    driverBrowsePastPassengersAdapter.update(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<TravelUser>> call, Throwable t) {
                Log.e(TAG, t.getMessage(), t);
            }
        };
    }
}
