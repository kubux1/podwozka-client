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


public class PassengerNewTravelActivity extends AppCompatActivity {
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
                //Starting a new Intent
                Intent nextScreen = new Intent(getApplicationContext(), BrowseTravelsActivity.class);

                EditText startTravelPlace = (EditText) findViewById(R.id.startTravelPlace);
                String startTravelPlaceMessage = startTravelPlace.getText().toString();

                EditText endTravelPlace = (EditText) findViewById(R.id.endTravelPlace);
                String endTravelPlaceMessage = endTravelPlace.getText().toString();

                EditText pickUpTime = (EditText) findViewById(R.id.pickUpTime);
                String pickUpTimeMessage = pickUpTime.getText().toString();

                EditText howManyPeopleToPickUp = (EditText) findViewById(R.id.howManyPeopleToPickUp);
                String howManyPeopleToPickUpMessage = howManyPeopleToPickUp.getText().toString();

                if (startTravelPlaceMessage.isEmpty()) {
                    alertWindow.showAlertWindow(PassengerNewTravelActivity.this, null, "Proszę podać adres początkowy");
                    noErrors = false;
                } else if (endTravelPlaceMessage.isEmpty()) {
                    alertWindow.showAlertWindow(PassengerNewTravelActivity.this, null, "Proszę podać adres końcowy");
                    noErrors = false;
                } else if (pickUpTimeMessage.isEmpty()) {
                    alertWindow.showAlertWindow(PassengerNewTravelActivity.this, null, "Proszę podać planowaną godzinę odebrania");
                    noErrors = false;
                } else if (howManyPeopleToPickUpMessage.isEmpty()) {
                    alertWindow.showAlertWindow(PassengerNewTravelActivity.this, null, "Proszę podać dodatkową liczbę pasażerów jaka będzie z Tobą");
                    noErrors = false;
                } else if (!pickUpTimeMessage.matches("[0-9]+")) {
                    alertWindow.showAlertWindow(PassengerNewTravelActivity.this, null, "Proszę podać tylko liczby w polu Godzina odjazdu");
                    noErrors = false;
                } else if (!howManyPeopleToPickUpMessage.matches("[0-9]+")) {
                    alertWindow.showAlertWindow(PassengerNewTravelActivity.this, null, "Proszę podać tylko liczby w polu Liczba Dodatkowych Pasażerów");
                    noErrors = false;
                }
                if (noErrors == true) {
                    PassangerTravel passengerTravel = new PassangerTravel(null, startTravelPlaceMessage, endTravelPlaceMessage, pickUpTimeMessage, howManyPeopleToPickUpMessage);
                    // Not working yet, waiting for testing with server
                    // TODO: Uncomment when App will be integrated with a Server
                    //travelsFound = passengerTravel.findMatchingTravels(passengerTravel);

                    // Expected response
                    travelsFound =
                            "{\n" +
                                    "  \"_embedded\" : {\n" +
                                    "    \"travels\" : [ {\n" +
                                    "      \"login\" : \"bartek\",\n" +
                                    "      \"firstName\" : \"Maciej\",\n" +
                                    "      \"lastName\" : \"Topola\",\n" +
                                    "      \"passengersCount\" : \"2\",\n" +
                                    "      \"maxPassengers\" : \"3\",\n" +
                                    "      \"startDatetime\" : \"2016-03-16 12:56\",\n" +
                                    "      \"startPlace\" : \"Gdynia, 10 Lutego\",\n" +
                                    "      \"endPlace\" : \"Gdańsk, Wrzeszcz\"\n" +
                                    "    },\n" +
                                    "\t{\n" +
                                    "      \"login\" : \"bartek\",\n" +
                                    "      \"firstName\" : \"Maciej\",\n" +
                                    "      \"lastName\" : \"Topola\",\n" +
                                    "      \"passengersCount\" : \"2\",\n" +
                                    "      \"maxPassengers\" : \"3\",\n" +
                                    "      \"startDatetime\" : \"2016-03-16 12:56\",\n" +
                                    "      \"startPlace\" : \"Gdynia, 10 Lutego\",\n" +
                                    "      \"endPlace\" : \"Gdańsk, Matarnia\"\n" +
                                    "    } ]\n" +
                                    "}\n" +
                                    "}";

                    nextScreen.putExtra("TRAVELS", travelsFound);
                    startActivity(nextScreen);

                }
            }
        });

        reversePlacesButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                //Starting a new Intent

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
        Intent nextScreen = new Intent(PassengerNewTravelActivity.this, PassangerMainActivity.class);
        startActivity(nextScreen);
        finish();
    }
}
