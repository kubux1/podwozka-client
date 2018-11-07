package podwozka.podwozka.Passenger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.List;

import podwozka.podwozka.Constants;
import podwozka.podwozka.Driver.DriverBrowseTravelsAdapter;
import podwozka.podwozka.Driver.DriverRecyclerItemClickListener;
import podwozka.podwozka.Driver.DriverTravelsLog;
import podwozka.podwozka.PopUpWindows;
import podwozka.podwozka.R;
import podwozka.podwozka.entity.TravelDTO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PassengerTravelsLog extends DriverTravelsLog {
    private static final String TAG = PassengerTravelsLog.class.getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travels_log);

        recyclerView = findViewById(R.id.recycler_view);
        comingTravels = findViewById(R.id.comingTravels);
        pastTravels = findViewById(R.id.pastTravels);

        mAdapter = new DriverBrowseTravelsAdapter(travelList, getApplicationContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(
                getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

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
    protected Callback<List<TravelDTO>> getFetchCallback() {
        return new Callback<List<TravelDTO>>() {
            @Override
            public void onResponse(Call<List<TravelDTO>> call, Response<List<TravelDTO>> response) {
                Log.i(TAG, response.message());
                if(response.isSuccessful()) {
                    mAdapter.update(response.body());
                    if (mAdapter.getItemCount() > 0) noTravels.setVisibility(View.INVISIBLE);
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
                Intent nextScreen = new Intent(PassengerTravelsLog.this,
                        PassengerTravelDriverAndCar.class);
                String driverLogin = mAdapter.returnTravel(position).getDriverLogin();
                nextScreen.putExtra(Constants.DRIVER_LOGIN, driverLogin);
                startActivity(nextScreen);
                finish();
            }

            @Override public void onLongItemClick(View view, int position) {
                // pass
            }
        });
    }
}
