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

        Button accountInfo = findViewById(R.id.accountInformationButton);
        accountInfo.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                //TODO: Implement functionality
            }
        });

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

        builder.setMessage("Czy napewno chcesz sie wylogowac?");

        builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                Intent nextScreen = new Intent(PassangerMain.this, MainActivity.class);
                startActivity(nextScreen);
                finish();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Nie", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }
}
