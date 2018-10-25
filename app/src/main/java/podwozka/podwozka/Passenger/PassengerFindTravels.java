package podwozka.podwozka.Passenger;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import java.util.Calendar;
import podwozka.podwozka.PopUpWindows;
import podwozka.podwozka.R;
import podwozka.podwozka.Passenger.entity.PassangerTravel;
import static podwozka.podwozka.LoginActivity.user;


public class PassengerFindTravels extends AppCompatActivity {
    private static TextView pickedTime;
    private static TextView pickedDate;
    private static String date;

    // Date dialog
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }


        public void onDateSet(DatePicker view, int year, int month, int day) {
            String monthInString = Integer.toString(month);
            String dayInString = Integer.toString(day);

            // Make sure there will be alawys two letters for month and day
            if(month < 10)
                monthInString = "0" + monthInString;
            if (day < 10)
                dayInString = "0" + dayInString;
            // Date in YYYY-MM-DD format only accepted by server
            date = Integer.toString(year) + "-" + monthInString + "-" + dayInString;

            // Date in DD-MM-YYYY format which is more convenient for a user
            pickedDate.setText(new StringBuilder().append(day).append("-")
                    .append(month).append("-").append(year));
        }
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }


    // Time dialog
    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            pickedTime.setText(new StringBuilder().append(hourOfDay).append(":")
                    .append(minute));

        }
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    String travelsFound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passanger_new_travel);
        Button btnNextScreen = findViewById(R.id.submitNewTravelButton);
        Button reversePlacesButton = findViewById(R.id.reverseTravelPlaces);
        pickedDate = findViewById(R.id.pickedDate);
        pickedTime = findViewById(R.id.pickedTime);


        btnNextScreen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                PopUpWindows alertWindow = new PopUpWindows();
                boolean noErrors = true;

                EditText startTravelPlace = findViewById(R.id.startTravelPlace);
                String startTravelPlaceMessage = startTravelPlace.getText().toString();

                EditText endTravelPlace = findViewById(R.id.endTravelPlace);
                String endTravelPlaceMessage = endTravelPlace.getText().toString();

                if (startTravelPlaceMessage.isEmpty())
                {
                    alertWindow.showAlertWindow(PassengerFindTravels.this, null, getResources().getString(R.string.start_place_empty));
                    noErrors = false;
                }
                else if (endTravelPlaceMessage.isEmpty())
                {
                    alertWindow.showAlertWindow(PassengerFindTravels.this, null, getResources().getString(R.string.end_place_empty));
                    noErrors = false;
                }

                if(noErrors == true) {
                    PassangerTravel passengerTravel = new PassangerTravel(null, user.getLogin(),
                            startTravelPlaceMessage,
                            endTravelPlaceMessage,
                            (date+"T"+pickedTime.getText().toString()),
                            null,
                            null);
                    travelsFound = passengerTravel.findMatchingTravels(passengerTravel);
                    Intent nextScreen = new Intent(getApplicationContext(), PassengerBrowseTravels.class);
                    nextScreen.putExtra("TRAVELS", travelsFound);
                    startActivity(nextScreen);
                }

            }
        });

        reversePlacesButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                //Starting a new Inten
                EditText startTravelPlace = findViewById(R.id.startTravelPlace);
                String startTravelPlaceMessage = startTravelPlace.getText().toString();

                EditText endTravelPlace = findViewById(R.id.endTravelPlace);
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
