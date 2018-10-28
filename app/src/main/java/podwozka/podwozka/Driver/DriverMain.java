package podwozka.podwozka.Driver;

import podwozka.podwozka.Driver.entity.DriverTravel;
import podwozka.podwozka.MainActivity;
import podwozka.podwozka.PopUpWindows;
import podwozka.podwozka.R;
import podwozka.podwozka.entity.HttpCommands;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.content.DialogInterface;

import java.net.HttpURLConnection;
import java.util.ArrayList;

public class DriverMain extends AppCompatActivity {
    public static ArrayList<DriverTravel> driverTravels = new ArrayList<DriverTravel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_main);

        Button newTravelButton = findViewById(R.id.newTravelButton);
        newTravelButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                if(checkIfDriverAddedCar() == true) {
                    //Starting a new Intent
                    Intent nextScreen = new Intent(getApplicationContext(), DriverAddTravel.class);
                    startActivity(nextScreen);
                }
            }
        });

        Button travelsHistory = findViewById(R.id.travelsHistoryButton);
        travelsHistory.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                //Starting a new Intent
                Intent nextScreen = new Intent(getApplicationContext(), DriverTravelsLog.class);
                startActivity(nextScreen);
            }
        });

        Button carInfo = findViewById(R.id.carInformationButton);
        carInfo.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                //Starting a new Intent
                Intent nextScreen = new Intent(getApplicationContext(), DriverAddCar.class);
                startActivity(nextScreen);
            }
        });

        final Button logOut = findViewById(R.id.logOutButton);
        logOut.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                logOut();
            }
        });

        checkForMessages();
    }

    @Override
    public void onBackPressed() {
        logOut();
    }

    public boolean checkIfDriverAddedCar(){
        boolean hasCar = false;
        HttpCommands httpCommand = new HttpCommands();

        int httpResponseCode = httpCommand.getCar();
        if(httpResponseCode == HttpURLConnection.HTTP_OK){
            hasCar = true;
        } else if(httpResponseCode == HttpURLConnection.HTTP_NOT_FOUND){
            new PopUpWindows().showAlertWindow(DriverMain.this, null, getResources().getString(R.string.car_not_added));
        } else {
            new PopUpWindows().showAlertWindow(DriverMain.this, null, getResources().getString(R.string.unknown_error));
        }
        return hasCar;
    }

    public void logOut(){
        AlertDialog.Builder builder = new AlertDialog.Builder(DriverMain.this);

        builder.setMessage(getResources().getString(R.string.log_out_confirmation));

        builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                Intent nextScreen = new Intent(DriverMain.this, MainActivity.class);
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

    // Check if there was is any message from previous activity
    public void checkForMessages(){
        Intent i = getIntent();
        String message = i.getStringExtra("MESSAGE");
        if(message !=  null){
            new PopUpWindows().showAlertWindow(DriverMain.this, null, message);
        }
    }
}
