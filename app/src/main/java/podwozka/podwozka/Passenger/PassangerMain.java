package podwozka.podwozka.Passenger;

import podwozka.podwozka.MainActivity;
import podwozka.podwozka.PopUpWindows;
import podwozka.podwozka.R;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class PassangerMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passanger_main);

        Button newTravelButton = findViewById(R.id.newTravelButton);
        newTravelButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                //Starting a new Intent
                Intent nextScreen = new Intent(getApplicationContext(), PassengerFindTravels.class);
                startActivity(nextScreen);
            }
        });

        Button travelsHistory = findViewById(R.id.travelsHistoryButton);
        travelsHistory.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                //Starting a new Intent
                Intent nextScreen = new Intent(getApplicationContext(), PassengerTravelsLog.class);
                startActivity(nextScreen);
            }
        });

        /* Commented for future development
        Button accountInfo = findViewById(R.id.accountInformationButton);
        accountInfo.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                //TODO: Implement functionality
            }
        });
        */

        final Button logOut = findViewById(R.id.logOutButton);
        logOut.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                logOut();
            }
        });
    }

    @Override
    public void onBackPressed() {
        logOut();
    }

    public void logOut(){
        AlertDialog.Builder builder = new AlertDialog.Builder(PassangerMain.this);

        builder.setMessage(getResources().getString(R.string.log_out_confirmation));

        builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                Intent nextScreen = new Intent(PassangerMain.this, MainActivity.class);
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
            new PopUpWindows().showAlertWindow(PassangerMain.this, null, message);
        }
    }
}
