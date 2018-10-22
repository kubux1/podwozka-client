package podwozka.podwozka.Driver;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import podwozka.podwozka.Driver.entity.DriverTravel;
import podwozka.podwozka.R;
import java.net.HttpURLConnection;

public class DriverEditTravelInfo extends AppCompatActivity {

    private static DriverTravel editedTravel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_edit_travel_info);

        Intent i = getIntent();
        editedTravel = i.getParcelableExtra("TRAVEL");

        final EditText startPlace = findViewById(R.id.startTravelPlace);
        startPlace.setText(editedTravel.getStartPlace());

        final EditText endPlace = findViewById(R.id.endTravelPlace);
        endPlace.setText(editedTravel.getEndPlace());

        final EditText pickUpTime = findViewById(R.id.pickUpTime);
        pickUpTime.setText(editedTravel.getStartDatetime());

        final EditText maxPassengers = findViewById(R.id.maxPassengers);
        maxPassengers.setText(editedTravel.getMaxPassengers());

        Button saveChanges = findViewById(R.id.saveChanges);
        saveChanges.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                editedTravel = new DriverTravel(
                        editedTravel.getTravelId(),
                        editedTravel.getDriverLogin(),
                        startPlace.getText().toString(),
                        endPlace.getText().toString(),
                        pickUpTime.getText().toString(),
                        maxPassengers.getText().toString());

                int httpResponse = editedTravel.editTravelInfo(editedTravel);
                if(httpResponse == HttpURLConnection.HTTP_OK){
                    Intent nextScreen = new Intent(DriverEditTravelInfo.this, DriverTravelsLog.class);
                    nextScreen.putExtra("TRAVEL", editedTravel);
                    startActivity(nextScreen);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent nextScreen = new Intent(DriverEditTravelInfo.this, DriverTravelEditor.class);
        nextScreen.putExtra("TRAVEL", editedTravel);
        startActivity(nextScreen);
        finish();
    }
}
