package podwozka.podwozka.Driver;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import podwozka.podwozka.Driver.entity.DriverTravel;
import podwozka.podwozka.PopUpWindows;
import podwozka.podwozka.R;
import settings.ConnectionSettings;

import static podwozka.podwozka.LoginActivity.user;


public class DriverNewTravelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_new_travel);

        Button btnNextScreen = (Button) findViewById(R.id.submitNewTravelButton);
        Button reversePlacesButton = (Button) findViewById(R.id.reverseTravelPlaces);

        btnNextScreen.setText("Zatwierdź");

        btnNextScreen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                PopUpWindows alertWindow = new PopUpWindows();
                boolean noErrors = true;
                int httpResponse;

                //Starting a new Intent
                Intent nextScreen = new Intent(getApplicationContext(), DriverNewTravelActivity.class);

                EditText startTravelPlace = (EditText) findViewById(R.id.startTravelPlace);
                String startTravelPlaceMessage = startTravelPlace.getText().toString();

                EditText endTravelPlace = (EditText) findViewById(R.id.endTravelPlace);
                String endTravelPlaceMessage = endTravelPlace.getText().toString();

                EditText pickUpTime = (EditText) findViewById(R.id.pickUpTime);
                String pickUpTimeMessage = pickUpTime.getText().toString();

                EditText pickUpDate = (EditText) findViewById(R.id.pickUpDate);
                String pickUpDateMessage = pickUpDate.getText().toString();

                EditText maxPassengers = (EditText) findViewById(R.id.maxPassengers);
                String maxPassengersMessage = maxPassengers.getText().toString();

                if (startTravelPlaceMessage.isEmpty())
                {
                    alertWindow.showAlertWindow(DriverNewTravelActivity.this, null, "Proszę podać adres początkowy");
                    noErrors = false;
                }
                else if (endTravelPlaceMessage.isEmpty())
                {
                    alertWindow.showAlertWindow(DriverNewTravelActivity.this, null, "Proszę podać adres końcowy");
                    noErrors = false;
                }
                else if (pickUpTimeMessage.isEmpty())
                {
                    alertWindow.showAlertWindow(DriverNewTravelActivity.this, null, "Proszę podać godzinę wyjazdu");
                    noErrors = false;
                }
                else if (maxPassengersMessage.isEmpty())
                {
                    alertWindow.showAlertWindow(DriverNewTravelActivity.this, null, "Proszę podać maksymalną liczbę pasażerów jaką możesz zabrać");
                    noErrors = false;
                }
                else if (pickUpDateMessage.isEmpty())
                {
                    alertWindow.showAlertWindow(DriverNewTravelActivity.this, null, "Proszę podać datę wyjazdu");
                    noErrors = false;
                }
                else if(!maxPassengersMessage.matches("[0-9]+")) {
                    alertWindow.showAlertWindow(DriverNewTravelActivity.this, null, "Proszę podać tylko liczby w polu Maksymalna Liczba Pasażerów");
                    noErrors = false;
                }
                if(noErrors == true) {
                DriverTravel newTravel = new DriverTravel("user", startTravelPlaceMessage, endTravelPlaceMessage, (pickUpDateMessage+"T"+pickUpTimeMessage), maxPassengersMessage);
                    //alertWindow.showAlertWindow(DriverNewTravelActivity.this, null, "Podróż dodana");
                    httpResponse = newTravel.postNewTravel(newTravel, user.getIdToken());
                    if(httpResponse == 201) {
                        startActivity(nextScreen);
                    }
                }

            }
        });

        reversePlacesButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                //Starting a new Inten
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
        Intent nextScreen = new Intent(DriverNewTravelActivity.this, DriverMainActivity.class);
        startActivity(nextScreen);
        finish();
    }
}
