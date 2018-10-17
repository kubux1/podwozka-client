package podwozka.podwozka.Driver;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.net.HttpURLConnection;
import java.util.Calendar;

import podwozka.podwozka.Driver.entity.DriverTravel;
import podwozka.podwozka.PopUpWindows;
import podwozka.podwozka.R;

import static podwozka.podwozka.LoginActivity.user;


public class DriverPostNewTravel extends AppCompatActivity {

    private static final String TAG = DriverPostNewTravel.class.getName();

    private static final int START_PLACE_REQUEST = 1000;

    private static final int END_PLACE_REQUEST = 1001;

    // values
    private static TextView pickedTime;

    private static TextView pickedDate;

    private static String date;

    // Maps
    private PlacePicker.IntentBuilder builder;

    TextView startPlaceView;

    TextView endPlaceView;

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

            if(month < 10)
                monthInString = "0" + monthInString;
            if (day < 10)
                dayInString = "0" + dayInString;

            // Date in DD-MM-YYYY format which is more convenient for a user
            pickedDate.setText(new StringBuilder().append(dayInString).append("-")
                    .append(monthInString).append("-").append(Integer.toString(year)));

            // Date in YYYY-MM-DD format only accepted by server
            date = Integer.toString(year) + "-" + monthInString + "-" + dayInString;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_new_travel);

        Button btnNextScreen = findViewById(R.id.submitNewTravelButton);
        Button reversePlacesButton = findViewById(R.id.reverseTravelPlaces);
        Button pickStartPlaceButton = findViewById(R.id.pickStartPlace);
        Button pickEndPlaceButton = findViewById(R.id.pickEndPlace);

        startPlaceView = findViewById(R.id.startPlaceView);
        endPlaceView = findViewById(R.id.endPlaceView);

        pickedDate =  findViewById(R.id.pickedDate);
        pickedTime = findViewById(R.id.pickedTime);
        final NumberPicker np = findViewById(R.id.passengerCount);

        builder = new PlacePicker.IntentBuilder();
        pickStartPlaceButton.setOnClickListener(getPlaceListener(START_PLACE_REQUEST));
        pickEndPlaceButton.setOnClickListener(getPlaceListener(END_PLACE_REQUEST));

        np.setMinValue(0);
        np.setMaxValue(10);

        btnNextScreen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                PopUpWindows alertWindow = new PopUpWindows();
                int httpResponse;

                String startTravelPlaceMessage = startPlaceView.getText().toString();
                String endTravelPlaceMessage = endPlaceView.getText().toString();

                String mistake = findMistake();
                if(mistake == null) {
                DriverTravel newTravel = new DriverTravel(user.getLogin(),
                        startTravelPlaceMessage,
                        endTravelPlaceMessage,
                        (date+"T"+pickedTime.getText().toString()),
                        Integer.toString(np.getValue()));

                    httpResponse = newTravel.postNewTravel(newTravel);
                    if(httpResponse == HttpURLConnection.HTTP_CREATED) {
                        Intent nextScreen = new Intent(getApplicationContext(),
                                DriverPostNewTravel.class);
                        startActivity(nextScreen);
                    } else {
                        Toast.makeText(DriverPostNewTravel.this,
                                R.string.err_create_fail, Toast.LENGTH_LONG).show();
                    }
                } else {
                    alertWindow.showAlertWindow(DriverPostNewTravel.this,
                            null, mistake);
                }

            }
        });

        reversePlacesButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                String startText = startPlaceView.getText().toString();
                String endText = endPlaceView.getText().toString();

                startPlaceView.setText(endText);
                endPlaceView.setText(startText);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == START_PLACE_REQUEST
                || requestCode == END_PLACE_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, intent);
                setPlace(place, requestCode);
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent nextScreen = new Intent(DriverPostNewTravel.this, DriverMain.class);
        startActivity(nextScreen);
        finish();
    }

    protected View.OnClickListener getPlaceListener(final int requestCode) {
        return new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    startActivityForResult(builder.build(DriverPostNewTravel.this),
                            requestCode);
                } catch (GooglePlayServicesNotAvailableException
                        | GooglePlayServicesRepairableException ex) {
                    Log.e(TAG, "Place Picker cannot use GooglePlayServices", ex);
                }
            }
        };
    }

    private void setPlace(Place place, int requestCode) {
        switch(requestCode) {
            case START_PLACE_REQUEST:
                startPlaceView.setText(String.format("%s: %s", place.getName(),
                        place.getAddress()));
                break;
            case END_PLACE_REQUEST:
                endPlaceView.setText(String.format("%s: %s", place.getName(),
                        place.getAddress()));
                break;
        }
        String toastMsg = String.format("%s: %s", R.string.toast_place, place.getName());
        Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
    }

    private String findMistake() {
        if (startPlaceView.getText().toString().isEmpty())
        {
            return getResources().getString(R.string.start_place_empty);
        }
        else if (endPlaceView.getText().toString().isEmpty())
        {
            return getResources().getString(R.string.end_place_empty);
        }

        return null;
    }
}
