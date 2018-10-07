package podwozka.podwozka.Driver;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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

import podwozka.podwozka.Driver.entity.DriverTravel;
import podwozka.podwozka.R;

public class DriverBrowseTravels extends AppCompatActivity {
    private List<DriverTravel> travelList = new ArrayList<>();
    private RecyclerView recyclerView;
    private DriverBrowseTravelsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_travels_list);

        String travelsFound;
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        recyclerView.addOnItemTouchListener(
                new DriverRecyclerItemClickListener(DriverBrowseTravels.this, recyclerView ,new DriverRecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Intent nextScreen = new Intent(DriverBrowseTravels.this, DriverTravelEditorActivity.class);
                        DriverTravel travel = mAdapter.returnTravel(position);
                        nextScreen.putExtra("TRAVEL", (Parcelable)travel);
                        startActivity(nextScreen);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

        mAdapter = new DriverBrowseTravelsAdapter(travelList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        travelsFound = new DriverTravel().getAllUserTravles();
        prepareTravelData(travelsFound);
    }

    @Override
    public void onBackPressed() {
        Intent nextScreen = new Intent(DriverBrowseTravels.this, DriverMainActivity.class);
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
                        (String)jsonObj.get("id"),
                        (String)jsonObj.get("login"),
                        (String)jsonObj.get("firstName"),
                        (String)jsonObj.get("lastName"),
                        (String)jsonObj.get("startDatetime"),
                        (String)jsonObj.get("startPlace"),
                        (String)jsonObj.get("endPlace"),
                        (String)jsonObj.get("passengersCount")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        mAdapter.notifyDataSetChanged();
    }
}
