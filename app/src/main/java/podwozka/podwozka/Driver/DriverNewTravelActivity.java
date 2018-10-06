package podwozka.podwozka.Driver;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Scanner;

import podwozka.podwozka.Driver.entity.DriverTravel;
import podwozka.podwozka.PopUpWindows;
import podwozka.podwozka.R;
import settings.ConnectionSettings;

import static podwozka.podwozka.LoginActivity.user;


public class DriverNewTravelActivity extends AppCompatActivity {

    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView, timeView;
    private int year, month, day;
    private String format = "";
    private TimePicker timePicker1;

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "ca",
                Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2+1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        dateView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }

    public void setTime(View view) {
        int hour = timePicker1.getCurrentHour();
        int min = timePicker1.getCurrentMinute();
        showTime(hour, min);
    }

    public void showTime(int hour, int min) {
        if (hour == 0) {
            hour += 12;
            format = "AM";
        } else if (hour == 12) {
            format = "PM";
        } else if (hour > 12) {
            hour -= 12;
            format = "PM";
        } else {
            format = "AM";
        }

        timeView.setText(new StringBuilder().append(hour).append(" : ").append(min)
                .append(" ").append(format));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_new_travel);
/*
        dateView = (TextView) findViewById(R.id.textView3);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);


        timePicker1 = (TimePicker) findViewById(R.id.timePicker1);
        timeView = (TextView) findViewById(R.id.textView10);
        calendar = Calendar.getInstance();

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        showTime(hour, min);
*/
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
