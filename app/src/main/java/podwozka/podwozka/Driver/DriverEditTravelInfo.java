package podwozka.podwozka.Driver;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import podwozka.podwozka.Driver.entity.DriverTravel;
import podwozka.podwozka.R;

public class DriverEditTravelInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_edit_travel_info);

        Intent i = getIntent();
        final DriverTravel travel = i.getParcelableExtra("TRAVEL");


        final EditText startPlace = findViewById(R.id.startTravelPlace);
        startPlace.setText(travel.getStartPlace());

        final EditText endPlace = findViewById(R.id.endTravelPlace);
        endPlace.setText(travel.getEndPlace());

        final EditText pickUpTime = findViewById(R.id.pickUpTime);
        pickUpTime.setText(travel.getStartDatetime());

        final EditText maxPassengers = findViewById(R.id.maxPassengers);
        maxPassengers.setText(travel.getMaxPassengers());

        Button saveChanges = findViewById(R.id.saveChanges);
        saveChanges.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                int httpResponse = travel.editTravelInfo(new DriverTravel(
                        travel.getTravelId(),
                        travel.getLogin(),
                        startPlace.getText().toString(),
                        endPlace.getText().toString(),
                        pickUpTime.getText().toString(),
                        maxPassengers.getText().toString()));
                if(httpResponse == 200){
                    Intent nextScreen = new Intent(DriverEditTravelInfo.this, DriverBrowseTravels.class);
                    startActivity(nextScreen);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent nextScreen = new Intent(DriverEditTravelInfo.this, DriverTravelEditor.class);
        startActivity(nextScreen);
        finish();
    }
}
