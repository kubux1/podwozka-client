package podwozka.podwozka.Driver;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import podwozka.podwozka.Driver.entity.DriverTravel;
import podwozka.podwozka.PopUpWindows;
import podwozka.podwozka.R;

public class DriverTravelEditorActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_travel_editor);

        Intent i = getIntent();
        final DriverTravel travel = (DriverTravel) i.getParcelableExtra("TRAVEL");
        final int travelId = i.getIntExtra("TRAVEL_ID", -1);

        Button editTravel = (Button) findViewById(R.id.editTravel);
        editTravel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                //Starting a new Intent
                Intent nextScreen = new Intent(DriverTravelEditorActivity.this, DriverEditTravelInfoActivity.class);
                nextScreen.putExtra("TRAVEL", (Parcelable)travel);
                startActivity(nextScreen);
            }
        });

        Button cancelTravel = (Button) findViewById(R.id.cancelTravel);
        cancelTravel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DriverTravelEditorActivity.this);

                builder.setMessage("Czy napewno chcesz anulować podróż?");

                builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        //int httpResponse = travel.deleteTravel(travelId);
                        //--------- START MOCK ---------
                        int httpResponse = 200;
                        //--------- END MOCK ---------
                        if(httpResponse == 200){
                            PopUpWindows successWindow = new PopUpWindows();
                            successWindow.showAlertWindow(DriverTravelEditorActivity.this, null, "Podróż usunięta");
                            Intent nextScreen = new Intent(DriverTravelEditorActivity.this, DriverBrowseTravels.class);
                            //--------- START MOCK ---------
                            nextScreen.putExtra("TRAVEL_DELETE",travelId);
                            DriverMainActivity.driverTravels.remove(travelId);
                            //--------- END MOCK ---------
                            startActivity(nextScreen);
                            //finish();
                        }
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
        });
    }

    @Override
    public void onBackPressed() {
        Intent nextScreen = new Intent(DriverTravelEditorActivity.this, DriverBrowseTravels.class);
        startActivity(nextScreen);
        finish();
    }
}
