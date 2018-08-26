package podwozka.podwozka.Passenger;

import podwozka.podwozka.PopUpWindows;
import podwozka.podwozka.R;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class PassangerMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passanger_main);

        Button newTravelButton = findViewById(R.id.newTravelButton);
        newTravelButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                //Starting a new Intent
                Intent nextScreen = new Intent(getApplicationContext(), PassengerNewTravelActivity.class);
                startActivity(nextScreen);
            }
        });

        Button travelsHistory = findViewById(R.id.travelsHistoryButton);
        travelsHistory.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                //Starting a new Intent
                Intent nextScreen = new Intent(getApplicationContext(), BrowseTravelsActivity.class);
                startActivity(nextScreen);
            }
        });
    }

    @Override
    public void onBackPressed() {
        String response;
        AlertDialog.Builder builder = new AlertDialog.Builder(PassangerMainActivity.this);

        builder.setMessage("Czy napewno chcesz sie wylogowac?");

        builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
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
