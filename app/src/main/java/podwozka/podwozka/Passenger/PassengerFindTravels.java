package podwozka.podwozka.Passenger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import podwozka.podwozka.PopUpWindows;
import podwozka.podwozka.R;
import podwozka.podwozka.Passenger.entity.PassangerTravel;
import settings.ConnectionSettings;


public class PassengerFindTravels extends AppCompatActivity {
    String travelsFound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passanger_new_travel);

        Button btnNextScreen = (Button) findViewById(R.id.submitNewTravelButton);
        Button reversePlacesButton = (Button) findViewById(R.id.reverseTravelPlaces);

        btnNextScreen.setText("Zatwierdź");

        btnNextScreen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                boolean noErrors = true;
                PopUpWindows alertWindow = new PopUpWindows();
                Intent nextScreen = new Intent(getApplicationContext(), PassengerBrowseTravels.class);

                EditText startTravelPlace = (EditText) findViewById(R.id.startTravelPlace);
                String startTravelPlaceMessage = startTravelPlace.getText().toString();

                EditText endTravelPlace = (EditText) findViewById(R.id.endTravelPlace);
                String endTravelPlaceMessage = endTravelPlace.getText().toString();

                EditText pickUpTime = (EditText) findViewById(R.id.pickUpTime);
                String pickUpTimeMessage = pickUpTime.getText().toString();

                EditText howManyPeopleToPickUp = (EditText) findViewById(R.id.howManyPeopleToPickUp);
                String howManyPeopleToPickUpMessage = howManyPeopleToPickUp.getText().toString();

                if (startTravelPlaceMessage.isEmpty()) {
                    alertWindow.showAlertWindow(PassengerFindTravels.this, null, getResources().getString(R.string.start_place_empty));
                    noErrors = false;
                } else if (endTravelPlaceMessage.isEmpty()) {
                    alertWindow.showAlertWindow(PassengerFindTravels.this, null, getResources().getString(R.string.end_place_empty));
                    noErrors = false;
                } else if (pickUpTimeMessage.isEmpty()) {
                    alertWindow.showAlertWindow(PassengerFindTravels.this, null, "Proszę podać planowaną godzinę odebrania");
                    noErrors = false;
                } else if (howManyPeopleToPickUpMessage.isEmpty()) {
                    alertWindow.showAlertWindow(PassengerFindTravels.this, null, "Proszę podać dodatkową liczbę pasażerów jaka będzie z Tobą");
                    noErrors = false;
                } else if (!pickUpTimeMessage.matches("[0-9]+")) {
                    alertWindow.showAlertWindow(PassengerFindTravels.this, null, "Proszę podać tylko liczby w polu Godzina odjazdu");
                    noErrors = false;
                } else if (!howManyPeopleToPickUpMessage.matches("[0-9]+")) {
                    alertWindow.showAlertWindow(PassengerFindTravels.this, null, "Proszę podać tylko liczby w polu Liczba Dodatkowych Pasażerów");
                    noErrors = false;
                }
                if (noErrors == true) {
                    PassangerTravel passengerTravel = new PassangerTravel(null, startTravelPlaceMessage, endTravelPlaceMessage, "2016-03-16T13:00", howManyPeopleToPickUpMessage);
                    travelsFound = passengerTravel.findMatchingTravels(passengerTravel);

                    nextScreen.putExtra("TRAVELS", travelsFound);
                    startActivity(nextScreen);
                }
            }
        });

        reversePlacesButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                EditText startTravelPlace = (EditText) findViewById(R.id.startTravelPlace);
                String startTravelPlaceMessage = startTravelPlace.getText().toString();

                EditText endTravelPlace = (EditText) findViewById(R.id.endTravelPlace);
                String endTravelPlaceMessage = endTravelPlace.getText().toString();

                startTravelPlace.setText(endTravelPlaceMessage);
                endTravelPlace.setText(startTravelPlaceMessage);

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent nextScreen = new Intent(PassengerFindTravels.this, PassangerMain.class);
        startActivity(nextScreen);
        finish();
    }
}
