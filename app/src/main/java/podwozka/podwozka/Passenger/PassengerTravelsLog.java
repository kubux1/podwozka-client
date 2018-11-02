package podwozka.podwozka.Passenger;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.view.View;
import android.widget.TextView;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import podwozka.podwozka.Driver.DriverBrowseTravelsAdapter;
import podwozka.podwozka.Driver.DriverRecyclerItemClickListener;
import podwozka.podwozka.Driver.entity.DriverTravel;
import podwozka.podwozka.PopUpWindows;
import podwozka.podwozka.R;

public class PassengerTravelsLog extends AppCompatActivity {
    private List<DriverTravel> travelList = new ArrayList<>();
    private RecyclerView recyclerView;
    private DriverBrowseTravelsAdapter mAdapter;
    private final static String COMING = "coming";
    private final static String PAST = "past";
    private TextView noComingTravels;
    private TextView noPastTravels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travels_log);

        recyclerView = findViewById(R.id.recycler_view);
        final Button comingTravels = findViewById(R.id.comingTravels);
        final Button pastTravels = findViewById(R.id.pastTravels);
        noComingTravels = findViewById(R.id.noComingTravels);
        noPastTravels = findViewById(R.id.noPastTravels);

        mAdapter = new DriverBrowseTravelsAdapter(travelList, getApplicationContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(
                new DriverRecyclerItemClickListener(PassengerTravelsLog.this, recyclerView, new DriverRecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent nextScreen = new Intent(PassengerTravelsLog.this, PassengerTravelDriverAndCar.class);
                        String driverLogin = mAdapter.returnTravel(position).getDriverLogin();
                        nextScreen.putExtra("DRIVER_LOGIN", driverLogin);
                        startActivity(nextScreen);
                        finish();
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

        final String travelsFound = new DriverTravel().getAllUserTravles();
        if (travelsFound == null) {
            new PopUpWindows().showAlertWindow(PassengerTravelsLog.this, null, getResources().getString(R.string.server_down));
        } else {

            comingTravels.setOnClickListener(new View.OnClickListener() {

                public void onClick(View arg0) {
                    comingTravels.setBackgroundColor(Color.GRAY);
                    pastTravels.setBackgroundColor(0);
                    noPastTravels.setVisibility(View.INVISIBLE);
                    boolean isEmpty = new DriverTravel().prepareTravelData(travelsFound, COMING, travelList, mAdapter);
                    if (isEmpty) {
                        noComingTravels.setVisibility(View.VISIBLE);
                    }
                }
            });
            pastTravels.setOnClickListener(new View.OnClickListener() {

                public void onClick(View arg0) {
                    pastTravels.setBackgroundColor(Color.GRAY);
                    comingTravels.setBackgroundColor(0);
                    noComingTravels.setVisibility(View.INVISIBLE);
                    boolean isEmpty = new DriverTravel().prepareTravelData(travelsFound, PAST, travelList, mAdapter);
                    if (isEmpty) {
                        noPastTravels.setVisibility(View.VISIBLE);
                    }
                }
            });
            comingTravels.callOnClick();
        }
    }
}
