package podwozka.podwozka.Driver;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import podwozka.podwozka.Driver.entity.DriverTravel;
import podwozka.podwozka.PopUpWindows;
import podwozka.podwozka.R;

public class DriverTravelsLog extends AppCompatActivity {
    private List<DriverTravel> travelList = new ArrayList<>();
    private RecyclerView recyclerView;
    private DriverBrowseTravelsAdapter mAdapter;
    final String COMING = "coming";
    final String PAST = "past";
    private TextView noComingTravels;
    private TextView noPastTravels;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travels_log);

        final String travelsFound;
        recyclerView = findViewById(R.id.recycler_view);
        final Button comingTravels = findViewById(R.id.comingTravels);
        final Button pastTravels = findViewById(R.id.pastTravels);
        noComingTravels = findViewById(R.id.noComingTravels);
        noPastTravels = findViewById(R.id.noPastTravels);

        recyclerView.addOnItemTouchListener(
                new DriverRecyclerItemClickListener(DriverTravelsLog.this, recyclerView ,new DriverRecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Intent nextScreen = new Intent(DriverTravelsLog.this, DriverTravelEditor.class);
                        DriverTravel travel = mAdapter.returnTravel(position);
                        nextScreen.putExtra("TRAVEL", travel);
                        startActivity(nextScreen);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

        mAdapter = new DriverBrowseTravelsAdapter(travelList, getApplicationContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        travelsFound = new DriverTravel().getAllUserTravles();
        new DriverTravel().prepareTravelData(travelsFound, COMING, travelList, mAdapter);

        comingTravels.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                comingTravels.setBackgroundColor(Color.GRAY);
                pastTravels.setBackgroundColor(0);
                noPastTravels.setVisibility(View.INVISIBLE);
                boolean isEmpty = new DriverTravel().prepareTravelData(travelsFound, COMING, travelList, mAdapter);
                if(isEmpty){
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
                if(isEmpty){
                    noPastTravels.setVisibility(View.VISIBLE);
                }
            }
        });
        comingTravels.callOnClick();
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
}
