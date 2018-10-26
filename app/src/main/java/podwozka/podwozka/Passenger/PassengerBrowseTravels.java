package podwozka.podwozka.Passenger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.List;

import podwozka.podwozka.Driver.DriverBrowseTravelsAdapter;
import podwozka.podwozka.Driver.entity.DriverTravel;
import podwozka.podwozka.R;

import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;

public class PassengerBrowseTravels extends AppCompatActivity {
    private List<DriverTravel> travelList = new ArrayList<>();
    private RecyclerView recyclerView;
    private DriverBrowseTravelsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String travelsFound;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_travels_list);

        recyclerView = findViewById(R.id.recycler_view);

        mAdapter = new DriverBrowseTravelsAdapter(travelList, getApplicationContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        travelsFound = getIntent().getStringExtra("TRAVELS");
        prepareTravelData(travelsFound);

        recyclerView.addOnItemTouchListener(
                new PassangerRecyclerItemClickListener(PassengerBrowseTravels.this, recyclerView ,new PassangerRecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        final DriverTravel travel = mAdapter.returnTravel(position);
                        AlertDialog.Builder builder = new AlertDialog.Builder(PassengerBrowseTravels.this);

                        builder.setMessage(getResources().getString(R.string.sign_up_for_trip_confirmation));

                        builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();
                            }
                        });

                        builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                int httpResponse = travel.signUpForTravel(travel.getTravelId());
                                if(httpResponse == 200){
                                }
                                dialog.dismiss();
                            }
                        });

                        AlertDialog alert = builder.create();
                        alert.show();
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // For future use
                    }
                })
        );
    }

    @Override
    public void onBackPressed() {
        Intent nextScreen = new Intent(PassengerBrowseTravels.this, PassangerMain.class);
        startActivity(nextScreen);
        finish();
    }

    private void prepareTravelData (String travelsJSON) {
        JSONParser parser = new JSONParser();
        try {
            JSONArray travelsObjects = (JSONArray)parser.parse(travelsJSON);

            for (Object obj : travelsObjects) {
                JSONObject jsonObj = (JSONObject) obj;
                travelList.add(new DriverTravel(
                        (Long)jsonObj.get("id"),
                        (String)jsonObj.get("driverLogin"),
                        (String)jsonObj.get("firstName"),
                        (String)jsonObj.get("lastName"),
                        (String)jsonObj.get("startDatetime"),
                        (String)jsonObj.get("startPlace"),
                        (String)jsonObj.get("endPlace"),
                        (Long)jsonObj.get("passengersCount")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        mAdapter.notifyDataSetChanged();
    }
}



