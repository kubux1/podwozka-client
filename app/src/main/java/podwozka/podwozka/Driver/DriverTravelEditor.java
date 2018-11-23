package podwozka.podwozka.Driver;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.net.HttpURLConnection;

import podwozka.podwozka.Constants;
import podwozka.podwozka.Libs.AppStatus;
import podwozka.podwozka.PopUpWindows;
import podwozka.podwozka.R;
import podwozka.podwozka.Rest.APIClient;
import podwozka.podwozka.Rest.TravelService;
import podwozka.podwozka.entity.TravelDTO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static podwozka.podwozka.LoginActivity.user;

public class DriverTravelEditor extends AppCompatActivity {

    private static final String TAG = DriverTravelEditor.class.getName();

    private Button cancelTravel;

    private Button waitingPassengersButton;

    private TravelDTO travelDTO;

    private String message;

    private TravelService travelService = APIClient.getTravelService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_travel_editor);
        Intent i = getIntent();
        travelDTO = i.getParcelableExtra(Constants.TRAVELDTO);

        /* Uncomment when fixed bug that after editing travel, passengers loses association to signed up travels
        Button editTravel = findViewById(R.id.editTravel);
        editTravel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                //Starting a new Intent
                Intent nextScreen = new Intent(DriverTravelEditor.this, DriverEditTravelInfo.class);
                nextScreen.putExtra("TRAVEL", travel);
                startActivity(nextScreen);
            }
        });
        */
        waitingPassengersButton = findViewById(R.id.passangersInfo);
        waitingPassengersButton.setOnClickListener(getWaitingPassengersListener());

        cancelTravel = findViewById(R.id.cancelTravel);
        cancelTravel.setOnClickListener(getCancelTravelListener());

        checkForMessages();
    }

    @Override
    public void onBackPressed() {
        Intent nextScreen = new Intent(DriverTravelEditor.this,
                DriverTravelsLog.class);
        startActivity(nextScreen);
        finish();
    }

    // Check if there was is any message from previous activity
    public void checkForMessages(){
        Intent i = getIntent();
        String message = i.getStringExtra(Constants.MESSAGE);
        if(message !=  null){
            new PopUpWindows().showAlertWindow(DriverTravelEditor.this, null, message);
        }
    }

    private Callback<Void> getFetchCallback() {
        return new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.i(TAG, response.message());
                if(response.isSuccessful()) {
                    message = getString(R.string.travel_canceled);
                } else if (response.code() == HttpURLConnection.HTTP_UNAVAILABLE) {
                    new PopUpWindows().showAlertWindow(DriverTravelEditor.this,
                            null, getResources().getString(R.string.server_down));
                } else {
                    message = getString(R.string.unknown_error);
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, t.getMessage(), t);
                message = getString(R.string.unknown_error);
            }
        };
    }

    private View.OnClickListener getWaitingPassengersListener() {
        return new View.OnClickListener() {
            public void onClick(View arg0) {
                if(AppStatus.getInstance(DriverTravelEditor.this).isOnline()) {
                    Intent nextScreen = new Intent(DriverTravelEditor.this,
                            DriverBrowseWaitingPassengers.class);
                    nextScreen.putExtra(Constants.TRAVELDTO, travelDTO);
                    startActivity(nextScreen);
                } else {
                    new PopUpWindows().showAlertWindow(DriverTravelEditor.this,
                            null, getResources().getString(R.string.no_internet_connection));
                }
            }
        };
    }

    private View.OnClickListener getCancelTravelListener() {
        return new View.OnClickListener() {
            public void onClick(View arg0) {
                if (AppStatus.getInstance(DriverTravelEditor.this).isOnline()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            DriverTravelEditor.this);

                    builder.setMessage(getResources().getString(R.string.cancel_trip_confirmation));
                    builder.setPositiveButton(getResources().getString(R.string.yes),
                            new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int which) {
                                    Call<Void> call = travelService.deleteTravel(travelDTO.getId(),
                                            user.getBearerToken());
                                    call.enqueue(getFetchCallback());

                                    Intent nextScreen = new Intent(DriverTravelEditor.this,
                                            DriverTravelsLog.class);
                                    nextScreen.putExtra(Constants.MESSAGE, message);
                                    startActivity(nextScreen);
                                    finish();
                                    dialog.dismiss();
                                }
                            });

                    builder.setNegativeButton(getResources().getString(R.string.no),
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    new PopUpWindows().showAlertWindow(DriverTravelEditor.this,
                            null, getResources().getString(R.string.no_internet_connection));
                }
            }
        };
    }
}
