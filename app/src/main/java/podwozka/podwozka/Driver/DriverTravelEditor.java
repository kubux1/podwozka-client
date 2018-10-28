package podwozka.podwozka.Driver;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.net.HttpURLConnection;

import podwozka.podwozka.Driver.entity.DriverTravel;
import podwozka.podwozka.PopUpWindows;
import podwozka.podwozka.R;

public class DriverTravelEditor extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_travel_editor);

        Intent i = getIntent();
        final DriverTravel travel = i.getParcelableExtra("TRAVEL");

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
        Button waitingPassengersButton = (Button) findViewById(R.id.passangersInfo);
        waitingPassengersButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                //Starting a new Intent
                Intent nextScreen = new Intent(DriverTravelEditor.this, DriverBrowseWaitingPassengers.class);
                nextScreen.putExtra("TRAVEL", travel);
                startActivity(nextScreen);
            }
        });

        Button cancelTravel = findViewById(R.id.cancelTravel);
        cancelTravel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DriverTravelEditor.this);

                builder.setMessage(getResources().getString(R.string.cancel_trip_confirmation));

                builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        int httpResponse = travel.deleteTravel(travel.getTravelId());
                        Intent nextScreen = new Intent(DriverTravelEditor.this, DriverTravelsLog.class);
                        String message;
                        if(httpResponse == HttpURLConnection.HTTP_OK){
                            message = getString(R.string.travel_canceled);
                        }
                        else {
                            message = getString(R.string.unknown_error);
                        }
                        nextScreen.putExtra("MESSAGE", message);
                        startActivity(nextScreen);
                        finish();
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        checkForMessages();
    }

    @Override
    public void onBackPressed() {
        Intent nextScreen = new Intent(DriverTravelEditor.this, DriverTravelsLog.class);
        startActivity(nextScreen);
        finish();
    }

    // Check if there was is any message from previous activity
    public void checkForMessages(){
        Intent i = getIntent();
        String message = i.getStringExtra("MESSAGE");
        if(message !=  null){
            new PopUpWindows().showAlertWindow(DriverTravelEditor.this, null, message);
        }
    }
}
