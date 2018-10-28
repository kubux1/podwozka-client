package podwozka.podwozka.Driver;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import podwozka.podwozka.Driver.entity.DriverTravel;
import podwozka.podwozka.R;
import java.net.HttpURLConnection;
import podwozka.podwozka.Libs.DateFunctions;

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

        String date = new DateFunctions().serverDateTimeToDate(editedTravel.getStartDatetime());
        final EditText pickUpDate = findViewById(R.id.pickUpDate);
        pickUpDate.setText(date);

        String time = new DateFunctions().serverDateTimeToTime(editedTravel.getStartDatetime());
        final EditText pickUpTime = findViewById(R.id.pickUpTime);
        pickUpTime.setText(time);

        Button saveChanges = findViewById(R.id.saveChanges);
        // TODO: Change startDateTime to values from fields
        saveChanges.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                editedTravel = new DriverTravel(
                        editedTravel.getTravelId(),
                        editedTravel.getDriverLogin(),
                        startPlace.getText().toString(),
                        endPlace.getText().toString(),
                        "2018-10-28T17:24",
                        editedTravel.getPassengersCount());

                int httpResponse = editedTravel.editTravelInfo(editedTravel);
                Intent nextScreen = new Intent(DriverEditTravelInfo.this, DriverTravelsLog.class);
                String message = null;
                if(httpResponse == HttpURLConnection.HTTP_OK){
                    nextScreen.putExtra("TRAVEL", editedTravel);
                    message = getResources().getString(R.string.travel_edit_success);
                } else if(httpResponse == HttpURLConnection.HTTP_NOT_FOUND){
                    message = getResources().getString(R.string.travel_not_found);
                } else {
                    message = getResources().getString(R.string.unknown_error);
                }
                nextScreen.putExtra("MESSAGE", message);
                startActivity(nextScreen);
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
