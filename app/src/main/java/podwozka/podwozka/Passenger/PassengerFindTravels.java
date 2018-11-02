package podwozka.podwozka.Passenger;

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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.net.HttpURLConnection;
import java.util.Calendar;

import podwozka.podwozka.Driver.DriverAddTravel;
import podwozka.podwozka.PopUpWindows;
import podwozka.podwozka.R;
import podwozka.podwozka.Passenger.entity.PassangerTravel;
import podwozka.podwozka.entity.PdPlace;
import podwozka.podwozka.entity.PdTravel;

import static podwozka.podwozka.LoginActivity.user;


public class PassengerFindTravels extends AppCompatActivity {

    private static final String TAG = PassengerFindTravels.class.getName();

    private static final int START_PLACE_REQUEST = 1000;

    private static final int END_PLACE_REQUEST = 1001;

    private static TextView pickedTime = null;
    private static TextView pickedDate = null;
    private static String date;
    private static String startTravelPlaceMessage;
    private static String endTravelPlaceMessage;

    // Maps
    private PlacePicker.IntentBuilder builder;

    private TextView startPlaceView;

    private TextView endPlaceView;

    private PdPlace startPlace = null;

    private PdPlace endPlace = null;


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
            String monthInString = Integer.toString(month + 1);
            String dayInString = Integer.toString(day);

            // Make sure there will be alawys two letters for month and day
            if((month + 1) < 10)
                monthInString = "0" + monthInString;
            if (day < 10)
                dayInString = "0" + dayInString;
            // Date in YYYY-MM-DD format only accepted by server
            date = Integer.toString(year) + "-" + monthInString + "-" + dayInString;

            // Date in DD-MM-YYYY format which is more convenient for a user
            pickedDate.setText(new StringBuilder().append(day).append("-")
                    .append(month + 1).append("-").append(year));
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
            String timeInString = String.format("%02d:%02d", hourOfDay, minute);
            pickedTime.setText(timeInString);

        }
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passanger_new_travel);

        Button btnNextScreen = findViewById(R.id.submitNewTravelButton);
        Button reversePlacesButton = findViewById(R.id.reverseTravelPlaces);
        Button pickStartPlaceButton = findViewById(R.id.pickStartPlace);
        Button pickEndPlaceButton = findViewById(R.id.pickEndPlace);

        startPlaceView = findViewById(R.id.startPlaceView);
        endPlaceView = findViewById(R.id.endPlaceView);

        pickedDate =  findViewById(R.id.pickedDate);
        pickedTime = findViewById(R.id.pickedTime);
        final String maxPassengersCapacity = Long.toString(getIntent().getLongExtra("CAPACITY",0));

        builder = new PlacePicker.IntentBuilder();
        pickStartPlaceButton.setOnClickListener(getPlaceListener(START_PLACE_REQUEST));
        pickEndPlaceButton.setOnClickListener(getPlaceListener(END_PLACE_REQUEST));

        btnNextScreen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                PopUpWindows alertWindow = new PopUpWindows();

                String mistake = findMistake();
                if(mistake == null) {
                    PdTravel travel = new PdTravel();
                    travel.setDriverLogin(user.getLogin());
                    travel.setStartPlace(startPlace);
                    travel.setEndPlace(endPlace);
                    travel.setPassengersCount(Long.parseLong(maxPassengersCapacity));
                    travel.setPickUpDatetime(date+"T"+pickedTime.getText().toString());

                    String travelsFound = travel.findMatchingTravels();
                    if(travelsFound == null)
                    {
                        new PopUpWindows().showAlertWindow(PassengerFindTravels.this, null, getResources().getString(R.string.server_down));
                    }
                    Intent nextScreen = new Intent(PassengerFindTravels.this,
                            PassengerBrowseFoundTravels.class);
                    nextScreen.putExtra("TRAVELS", travelsFound);
                    startActivity(nextScreen);
                } else {
                    alertWindow.showAlertWindow(PassengerFindTravels.this,
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

    @Override
    public void onBackPressed() {
        Intent nextScreen = new Intent(PassengerFindTravels.this, PassangerMain.class);
        startActivity(nextScreen);
        finish();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("startTravelPlace", startTravelPlaceMessage);
        savedInstanceState.putString("endTravelPlace", endTravelPlaceMessage);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        EditText startTravelPlace = findViewById(R.id.startTravelPlace);
        EditText endTravelPlace = findViewById(R.id.endTravelPlace);

        startTravelPlaceMessage = savedInstanceState.getString("startTravelPlace");
        startTravelPlace.setText(startTravelPlaceMessage);
        endTravelPlaceMessage = savedInstanceState.getString("endTravelPlace");
        endTravelPlace.setText(endTravelPlaceMessage);
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

    protected View.OnClickListener getPlaceListener(final int requestCode) {
        return new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    startActivityForResult(builder.build(PassengerFindTravels.this),
                            requestCode);
                } catch (GooglePlayServicesNotAvailableException
                        | GooglePlayServicesRepairableException ex) {
                    Log.e(TAG, "PdPlace Picker cannot use GooglePlayServices", ex);
                }
            }
        };
    }

    private void setPlace(Place place, int requestCode) {
        switch(requestCode) {
            case START_PLACE_REQUEST:
                startPlaceView.setText(String.format("%s: %s", place.getName(),
                        place.getAddress()));
                startPlace = new PdPlace(place);
                break;
            case END_PLACE_REQUEST:
                endPlaceView.setText(String.format("%s: %s", place.getName(),
                        place.getAddress()));
                endPlace = new PdPlace(place);
                break;
        }
        String toastMsg = String.format("%s: %s", R.string.toast_place, place.getName());
        Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
    }

    private String findMistake() {
        if (startPlace == null)
        {
            return getResources().getString(R.string.start_place_empty);
        } else if (endPlace == null) {
            return getResources().getString(R.string.end_place_empty);
        } else if (pickedDate == null) {
            return getResources().getString(R.string.end_place_empty);
        } else if (pickedTime == null) {
            return getResources().getString(R.string.end_place_empty);
        }

        return null;
    }
}
