package podwozka.podwozka.Driver;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import podwozka.podwozka.PopUpWindows;
import podwozka.podwozka.R;
import podwozka.podwozka.Rest.APIClient;
import podwozka.podwozka.Rest.TravelService;
import podwozka.podwozka.entity.TravelDTO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static podwozka.podwozka.LoginActivity.user;

public class DriverTravelsLog extends AppCompatActivity {
    private static final String TAG = DriverTravelsLog.class.getName();
    private List<TravelDTO> travelList = new ArrayList<>();
    private RecyclerView recyclerView;
    private DriverBrowseTravelsAdapter mAdapter;
    private TextView noTravels;
    private Button comingTravels;
    private Button pastTravels;
    private TravelService travelService = APIClient.getTravelService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travels_log);

        recyclerView = findViewById(R.id.recycler_view);
        comingTravels = findViewById(R.id.comingTravels);
        pastTravels = findViewById(R.id.pastTravels);
        noTravels = findViewById(R.id.noTravels);

        recyclerView.addOnItemTouchListener(getRecyclerListener());

        mAdapter = new DriverBrowseTravelsAdapter(travelList, getApplicationContext(), 1);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        comingTravels.setOnClickListener(getComingTravelsListener());
        comingTravels.callOnClick();
        pastTravels.setOnClickListener(getPastTravelsListener());

        checkForMessages();
    }

    @Override
    public void onBackPressed() {
        Intent nextScreen = new Intent(DriverTravelsLog.this, DriverMain.class);
        startActivity(nextScreen);
        finish();
    }

    // Check if there was is any message from previous activity
    public void checkForMessages(){
        Intent i = getIntent();
        String message = i.getStringExtra("MESSAGE");
        if(message !=  null){
            new PopUpWindows().showAlertWindow(DriverTravelsLog.this, null, message);
        }
    }

    private Callback<List<TravelDTO>> getFetchCallback() {
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

    private void getComingTravels() {
        Call<List<TravelDTO>> call = travelService.getAllUserComingTravels(
                user.getLogin(), 0, user.getBearerToken());
        call.enqueue(getFetchCallback());
        comingTravels.setBackgroundColor(Color.GRAY);
        pastTravels.setBackgroundColor(0);
    }

    private View.OnClickListener getComingTravelsListener() {
        return new View.OnClickListener() {
            public void onClick(View arg0) {
                getComingTravels();
            }
        };
    }

    private void getPastTravels() {
        Call<List<TravelDTO>> call = travelService.getAllUserPastTravels(
                user.getLogin(), 0, user.getBearerToken());
        call.enqueue(getFetchCallback());
        pastTravels.setBackgroundColor(Color.GRAY);
        comingTravels.setBackgroundColor(0);
    }

    private View.OnClickListener getPastTravelsListener() {
        return new View.OnClickListener() {
            public void onClick(View arg0) {
                getPastTravels();
            }
        };
    }

    private DriverRecyclerItemClickListener getRecyclerListener() {
        return new DriverRecyclerItemClickListener(
                DriverTravelsLog.this, recyclerView,
                new DriverRecyclerItemClickListener.OnItemClickListener() {
            @Override public void onItemClick(View view, int position) {
                Intent nextScreen = new Intent(DriverTravelsLog.this, DriverTravelEditor.class);
                TravelDTO travel = mAdapter.returnTravel(position, 1);
                nextScreen.putExtra("TRAVEL", travel);
                startActivity(nextScreen);
            }

            @Override public void onLongItemClick(View view, int position) {
                // do whatever
            }
        });
    }
}
