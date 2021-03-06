package podwozka.podwozka.Passenger;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.ArrayList;
import java.util.List;

import podwozka.podwozka.Constants;
import podwozka.podwozka.Driver.DriverAddTravel;
import podwozka.podwozka.PopUpWindows;
import podwozka.podwozka.R;
import podwozka.podwozka.entity.PlaceDTO;
import podwozka.podwozka.entity.TravelDTO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static podwozka.podwozka.LoginActivity.user;


public class PassengerFindTravels extends DriverAddTravel {

    private static final String TAG = PassengerFindTravels.class.getName();

    private static PlaceDTO startPlaceSaved;

    private static PlaceDTO endPlaceSaved;

    protected static String dateSaved;

    protected static String timeSaved;

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
                    TravelDTO travel = new TravelDTO();
                    travel.setDriverLogin(user.getLogin());
                    travel.setStartPlace(startPlace);
                    travel.setEndPlace(endPlace);
                    travel.setPassengersCount(Long.parseLong(maxPassengersCapacity));
                    travel.setPickUpDatetime(date+"T"+pickedTime.getText().toString());

                    startPlaceSaved = startPlace;
                    endPlaceSaved = endPlace;
                    dateSaved = date;
                    timeSaved = pickedTime.getText().toString();

                    Call<List<TravelDTO>> call = travelService.finMatching(0,
                            travel,
                            user.getBearerToken());
                    call.enqueue(getFetchCallback());

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
        findAndsetSavedValues();
    }

    @Override
    public void onBackPressed() {
        Intent nextScreen = new Intent(PassengerFindTravels.this, PassangerMain.class);
        startActivity(nextScreen);
        finish();
    }

    public void findAndsetSavedValues(){
        Intent i = getIntent();

        startPlaceSaved = i.getParcelableExtra(Constants.START_PLACE_SAVED);
        if(startPlaceSaved != null){
            startPlaceView.setText(String.format("%s: %s", startPlaceSaved.getName(),""));
            startPlace = startPlaceSaved;
        }

        endPlaceSaved = i.getParcelableExtra(Constants.END_PLACE_SAVED);
        if(endPlaceSaved != null){
            endPlaceView.setText(String.format("%s: %s", endPlaceSaved.getName(),""));
            endPlace = endPlaceSaved;
        }

        dateSaved = i.getStringExtra(Constants.DATE_SAVED);
        if(dateSaved != null){
            pickedDate.setText(dateSaved);
            date = dateSaved;
        }

        timeSaved = i.getStringExtra(Constants.TIME_SAVED);
        if(timeSaved != null){
            pickedTime.setText(timeSaved);
        }
    }

    public void checkForMessages(){
        Intent i = getIntent();
        String message = i.getStringExtra(Constants.MESSAGE);
        if(message !=  null){
            new PopUpWindows().showAlertWindow(PassengerFindTravels.this, null, message);
        }
    }

    private Callback<List<TravelDTO>> getFetchCallback() {
        return new Callback<List<TravelDTO>>() {
            @Override
            public void onResponse(Call<List<TravelDTO>> call, Response<List<TravelDTO>> response) {
                Log.i(TAG, response.message());
                if(response.isSuccessful()) {
                    Intent nextScreen = new Intent(PassengerFindTravels.this,
                            PassengerBrowseFoundTravels.class);
                    nextScreen.putExtra(Constants.TRAVELDTOS, (ArrayList) response.body());
                    nextScreen.putExtra(Constants.START_PLACE_SAVED, startPlaceSaved);
                    nextScreen.putExtra(Constants.END_PLACE_SAVED, endPlaceSaved);
                    nextScreen.putExtra(Constants.DATE_SAVED, dateSaved);
                    nextScreen.putExtra(Constants.TIME_SAVED, timeSaved);
                    startActivity(nextScreen);
                }
            }

            @Override
            public void onFailure(Call<List<TravelDTO>> call, Throwable t) {
                Log.e(TAG, t.getMessage(), t);
            }
        };
    }

    private View.OnClickListener getPlaceListener(final int requestCode) {
        return new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    startActivityForResult(builder.build(PassengerFindTravels.this),
                            requestCode);
                } catch (GooglePlayServicesNotAvailableException
                        | GooglePlayServicesRepairableException ex) {
                    Log.e(TAG, "PlaceDTO Picker cannot use GooglePlayServices", ex);
                }
            }
        };
    }
}
