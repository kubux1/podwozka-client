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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.net.HttpURLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import podwozka.podwozka.Constants;
import podwozka.podwozka.Libs.AppStatus;
import podwozka.podwozka.PopUpWindows;
import podwozka.podwozka.R;
import podwozka.podwozka.Rest.APIClient;
import podwozka.podwozka.Rest.TravelService;
import podwozka.podwozka.entity.PlaceDTO;
import podwozka.podwozka.entity.TravelDTO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static podwozka.podwozka.LoginActivity.user;


public class DriverAddTravel extends AppCompatActivity {

    private static final String TAG = DriverAddTravel.class.getName();

    protected static final int START_PLACE_REQUEST = 1000;

    protected static final int END_PLACE_REQUEST = 1001;

    // values
    protected static TextView pickedTime;

    protected static TextView pickedDate;

    protected static String date;

    // Maps
    protected PlacePicker.IntentBuilder builder;

    protected TextView startPlaceView;

    protected TextView endPlaceView;

    protected PlaceDTO startPlace;

    protected PlaceDTO endPlace;

    protected TravelService travelService = APIClient.getTravelService();

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
            DatePickerDialog datePicker = new DatePickerDialog(getActivity(), this,
                    year, month, day);
            datePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1);
            return datePicker;
        }


        public void onDateSet(DatePicker view, int year, int month, int day) {
            month++;
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
            TimePickerDialog timePicker =  new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
            return timePicker;
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            pickedTime.setText(new StringBuilder()
                    .append(String.format(Locale.ENGLISH,"%02d", hourOfDay))
                    .append(":")
                    .append(String.format(Locale.ENGLISH,"%02d", minute)));

        }
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    // Check if there was is any message from previous activity
    public void checkForMessages(){
        Intent i = getIntent();
        String message = i.getStringExtra(Constants.MESSAGE);
        if(message !=  null){
            new PopUpWindows().showAlertWindow(DriverAddTravel.this, null, message);
        }
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
        final String maxPassengersCapacity = Long.toString(getIntent().getLongExtra("CAPACITY",0));

        builder = new PlacePicker.IntentBuilder();
        pickStartPlaceButton.setOnClickListener(getPlaceListener(START_PLACE_REQUEST));
        pickEndPlaceButton.setOnClickListener(getPlaceListener(END_PLACE_REQUEST));

        btnNextScreen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                PopUpWindows alertWindow = new PopUpWindows();
                if (AppStatus.getInstance(DriverAddTravel.this).isOnline()) {
                    String mistake = findMistake();
                    if (mistake == null) {
                        TravelDTO travel = new TravelDTO();
                        travel.setDriverLogin(user.getLogin());
                        travel.setStartPlace(startPlace);
                        travel.setEndPlace(endPlace);
                        travel.setPassengersCount(Long.parseLong(maxPassengersCapacity));
                        travel.setPickUpDatetime(date + "T" + pickedTime.getText().toString());

                        Call<TravelDTO> call = travelService.createTravel(travel,
                                user.getBearerToken());
                        call.enqueue(getFetchCallback());
                    } else {
                        alertWindow.showAlertWindow(DriverAddTravel.this,
                                null, mistake);
                    }
                } else {
                    alertWindow.showAlertWindow(DriverAddTravel.this,
                            null, getResources().getString(R.string.no_internet_connection));
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

        checkForMessages();
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
        Intent nextScreen = new Intent(DriverAddTravel.this, DriverMain.class);
        startActivity(nextScreen);
        finish();
    }

    protected void setPlace(Place place, int requestCode) {
        switch(requestCode) {
            case START_PLACE_REQUEST:
                startPlaceView.setText(String.format("%s: %s", place.getName(),
                        place.getAddress()));
                startPlace = new PlaceDTO(place);
                break;
            case END_PLACE_REQUEST:
                endPlaceView.setText(String.format("%s: %s", place.getName(),
                        place.getAddress()));
                endPlace = new PlaceDTO(place);
                break;
        }
        String toastMsg = String.format("%s: %s", R.string.toast_place, place.getName());
        Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
    }

    protected String findMistake() {
        if (startPlace == null) {
            return getResources().getString(R.string.start_place_empty);
        } else if (endPlace == null) {
            return getResources().getString(R.string.end_place_empty);
        } else if (pickedDate == null) {
            return getResources().getString(R.string.end_place_empty);
        } else if (pickedTime == null) {
            return getResources().getString(R.string.end_place_empty);
        }
        // Checking if choosed time is not before current time, it is checked only if current day
        // was choosed
        else if(pickedTime != null) {
            Date currDate = null;
            Date choosedDate = null;
            Date currTime = null;
            Date choosedTime = null;
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String currentDate = sdf.format(Calendar.getInstance().getTime());
                currDate = sdf.parse(currentDate);
                choosedDate = sdf.parse(date);

                sdf = new SimpleDateFormat("HH:mm");
                String currentTime = sdf.format(Calendar.getInstance().getTime());
                currTime = sdf.parse(currentTime);
                choosedTime = sdf.parse(pickedTime.getText().toString());
            } catch (ParseException exception) {
                exception.printStackTrace();
            }
            if((currDate.compareTo(choosedDate) == 0) & (currTime.compareTo(choosedTime) >= 0)) {
                return getResources().getString(R.string.non_valid_time);
            }
        }

        return null;
    }

    private Callback<TravelDTO> getFetchCallback() {
        return new Callback<TravelDTO>() {
            @Override
            public void onResponse(Call<TravelDTO> call, Response<TravelDTO> response) {
                Log.i(TAG, response.message());
                if(response.isSuccessful()) {
                    Intent nextScreen = new Intent(getApplicationContext(),
                            DriverAddTravel.class);
                    nextScreen.putExtra(Constants.MESSAGE,
                            getResources().getString(R.string.trip_added));
                    startActivity(nextScreen);
                    finish();
                } else if (response.code()  == HttpURLConnection.HTTP_UNAVAILABLE) {
                    new PopUpWindows().showAlertWindow(DriverAddTravel.this,
                            null, getResources().getString(R.string.server_down));
                } else {
                    Toast.makeText(DriverAddTravel.this,
                            R.string.err_create_fail, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<TravelDTO> call, Throwable t) {
                Log.e(TAG, t.getMessage(), t);
                Toast.makeText(DriverAddTravel.this,
                        R.string.err_create_fail, Toast.LENGTH_LONG).show();
            }
        };
    }

    private View.OnClickListener getPlaceListener(final int requestCode) {
        return new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    startActivityForResult(builder.build(DriverAddTravel.this),
                            requestCode);
                } catch (GooglePlayServicesNotAvailableException
                        | GooglePlayServicesRepairableException ex) {
                    Log.e(TAG, "PlaceDTO Picker cannot use GooglePlayServices", ex);
                }
            }
        };
    }
}
