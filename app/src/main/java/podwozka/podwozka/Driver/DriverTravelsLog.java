package podwozka.podwozka.Driver;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import podwozka.podwozka.Driver.entity.DriverTravel;
import podwozka.podwozka.R;

public class DriverTravelsLog extends AppCompatActivity {
    private List<DriverTravel> travelList = new ArrayList<>();
    private RecyclerView recyclerView;
    private DriverBrowseTravelsAdapter mAdapter;
    private final static String COMING = "coming";
    private final static String PAST = "past";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travels_log);

        final String travelsFound;
        recyclerView = findViewById(R.id.recycler_view);
        final Button comingTravels = findViewById(R.id.comingTravels);
        final Button pastTravels = findViewById(R.id.pastTravels);

        recyclerView.addOnItemTouchListener(
                new DriverRecyclerItemClickListener(DriverTravelsLog.this, recyclerView ,new DriverRecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Intent nextScreen = new Intent(DriverTravelsLog.this, DriverTravelEditor.class);
                        DriverTravel travel = mAdapter.returnTravel(position);
                        nextScreen.putExtra("TRAVEL", (Parcelable)travel);
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
        prepareTravelData(travelsFound, "coming");

        comingTravels.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                comingTravels.setBackgroundColor(Color.GRAY);
                pastTravels.setBackgroundColor(0);
                prepareTravelData(travelsFound, COMING);
            }
        });

        pastTravels.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                pastTravels.setBackgroundColor(Color.GRAY);
                comingTravels.setBackgroundColor(0);
                prepareTravelData(travelsFound, PAST);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent nextScreen = new Intent(DriverTravelsLog.this, DriverMain.class);
        startActivity(nextScreen);
        finish();
    }

    private void prepareTravelData (String travelsJSON, String time) {
        JSONParser parser = new JSONParser();
        Date currentDate = new Date(), selectedDate;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

        // Clear previous data
        travelList.clear();
        mAdapter.notifyDataSetChanged();

        try {
            JSONArray travelsObjects = (JSONArray)parser.parse(travelsJSON);
            for (Object obj : travelsObjects) {
                JSONObject jsonObj = (JSONObject) obj;
                selectedDate = dateFormat.parse((String) jsonObj.get("pickUpDatetime"));
                if (time.equals(COMING) & currentDate.before(selectedDate)) {
                    travelList.add(new DriverTravel(
                            (Long) jsonObj.get("id"),
                            (String) jsonObj.get("driverLogin"),
                            (String) jsonObj.get("pickUpDatetime"),
                            (String) jsonObj.get("startPlace"),
                            (String) jsonObj.get("endPlace"),
                            String.valueOf(jsonObj.get("passengersCount")),
                            String.valueOf(jsonObj.get("maxPassenger"))
                    ));
                }
                else if (time.equals(PAST) & currentDate.after(selectedDate)) {
                    travelList.add(new DriverTravel(
                            (Long) jsonObj.get("id"),
                            (String) jsonObj.get("driverLogin"),
                            (String) jsonObj.get("pickUpDatetime"),
                            (String) jsonObj.get("startPlace"),
                            (String) jsonObj.get("endPlace"),
                            String.valueOf(jsonObj.get("passengersCount")),
                            String.valueOf(jsonObj.get("maxPassenger"))
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        mAdapter.notifyDataSetChanged();
    }
}