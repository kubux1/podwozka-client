package podwozka.podwozka.Passenger;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import podwozka.podwozka.Constants;
import podwozka.podwozka.Driver.DriverBrowseTravelsAdapter;
import podwozka.podwozka.Driver.DriverRecyclerItemClickListener;
import podwozka.podwozka.Driver.DriverTravelsLog;
import podwozka.podwozka.Libs.AppStatus;
import podwozka.podwozka.PopUpWindows;
import podwozka.podwozka.R;
import podwozka.podwozka.entity.TravelDTO;
import podwozka.podwozka.entity.TravelUser;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static podwozka.podwozka.LoginActivity.user;

public class PassengerTravelsLog extends DriverTravelsLog {
    private static final String TAG = PassengerTravelsLog.class.getName();
    protected PassengerBrowseTravelsAdapter passengerBrowseTravelsAdapter;
    protected List<TravelUser> acceptance = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travels_log);

        recyclerView = findViewById(R.id.recycler_view);
        comingTravels = findViewById(R.id.comingTravels);
        pastTravels = findViewById(R.id.pastTravels);

        passengerBrowseTravelsAdapter = new PassengerBrowseTravelsAdapter(travelList, acceptance,
                getApplicationContext());

        Call<List<TravelUser>> call = travelService.getSignedUpTravels(
                user.getLogin(), user.getLogin(), user.getBearerToken());
        call.enqueue(getAcceptanceFetchCallback());

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(
                getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(passengerBrowseTravelsAdapter);

        recyclerView.addOnItemTouchListener(getDriverRecyclerItemClickListener());
        comingTravels.setOnClickListener(getComingTravelsListener());
        pastTravels.setOnClickListener(getPastTravelsListener());
        comingTravels.callOnClick();
    }

    @Override
    public void onBackPressed() {
        Intent nextScreen = new Intent(PassengerTravelsLog.this, PassangerMain.class);
        startActivity(nextScreen);
        finish();
    }

    @Override
    public void checkForMessages(){
        Intent i = getIntent();
        String message = i.getStringExtra(Constants.MESSAGE);
        if(message !=  null){
            new PopUpWindows().showAlertWindow(PassengerTravelsLog.this, null, message);
        }
    }

    @Override
    protected void getComingTravels() {
        if (AppStatus.getInstance(PassengerTravelsLog.this).isOnline()) {
            Call<List<TravelDTO>> call = travelService.getAllUserComingTravelsForPassenger(
                    user.getLogin(), 0, user.getBearerToken());
            call.enqueue(getFetchCallback());
            comingTravels.setBackgroundColor(Color.GRAY);
            pastTravels.setBackgroundColor(0);
        } else {
            new PopUpWindows().showAlertWindow(
                    PassengerTravelsLog.this, null,
                    getResources().getString(R.string.no_internet_connection));
        }
    }

    @Override
    protected void getPastTravels() {
        if (AppStatus.getInstance(PassengerTravelsLog.this).isOnline()) {
            Call<List<TravelDTO>> call = travelService.getAllUserPastTravelsForPassenger(
                    user.getLogin(), 0, user.getBearerToken());
            call.enqueue(getFetchCallback());
            pastTravels.setBackgroundColor(Color.GRAY);
            comingTravels.setBackgroundColor(0);
        } else {
            new PopUpWindows().showAlertWindow(
                    PassengerTravelsLog.this, null,
                    getResources().getString(R.string.no_internet_connection));
        }
    }

    @Override
    protected Callback<List<TravelDTO>> getFetchCallback() {
        return new Callback<List<TravelDTO>>() {
            @Override
            public void onResponse(Call<List<TravelDTO>> call, Response<List<TravelDTO>> response) {
                Log.i(TAG, response.message());
                if(response.isSuccessful()) {
                    passengerBrowseTravelsAdapter.update(response.body());
                    if (passengerBrowseTravelsAdapter.getItemCount() > 0) noTravels.
                            setVisibility(View.INVISIBLE);
                    else noTravels.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<TravelDTO>> call, Throwable t) {
                Log.e(TAG, t.getMessage(), t);
            }
        };
    }

    private DriverRecyclerItemClickListener getDriverRecyclerItemClickListener() {
        return new DriverRecyclerItemClickListener(PassengerTravelsLog.this,
                recyclerView, new DriverRecyclerItemClickListener.OnItemClickListener() {

            @Override public void onItemClick(View view, int position) {
                if (AppStatus.getInstance(PassengerTravelsLog.this).isOnline()) {
                    Intent nextScreen = new Intent(PassengerTravelsLog.this,
                            PassengerTravelDriverAndCar.class);
                    String driverLogin = passengerBrowseTravelsAdapter.returnTravel(position).
                            getDriverLogin();
                    nextScreen.putExtra(Constants.DRIVER_LOGIN, driverLogin);
                    startActivity(nextScreen);
                    finish();
                } else {
                    new PopUpWindows().showAlertWindow(
                            PassengerTravelsLog.this, null,
                            getResources().getString(R.string.no_internet_connection));
                }
            }

            @Override public void onLongItemClick(View view, int position) {
                // pass
            }
        });
    }

    private Callback<List<TravelUser>> getAcceptanceFetchCallback() {
        return new Callback<List<TravelUser>>() {
            @Override
            public void onResponse(Call<List<TravelUser>> call, Response<List<TravelUser>> response) {
                Log.i(TAG, response.message());
                if(response.isSuccessful()) {
                    passengerBrowseTravelsAdapter.updateAcceptance(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<TravelUser>> call, Throwable t) {
                Log.e(TAG, t.getMessage(), t);
            }
        };
    }
}
