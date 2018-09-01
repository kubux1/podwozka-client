package podwozka.podwozka.Driver;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.List;

import podwozka.podwozka.Driver.DriverBrowseTravelsActivityAdapter;
import podwozka.podwozka.Driver.entity.DriverTravel;
import podwozka.podwozka.R;

public class DriverBrowseTravels extends AppCompatActivity {
    private List<DriverTravel> travelList = new ArrayList<>();
    private RecyclerView recyclerView;
    private DriverBrowseTravelsActivityAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String travelsFound;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_travels_list);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new DriverBrowseTravelsActivityAdapter(travelList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        travelsFound = new DriverTravel().getAllUserTravlesFromServer();
        prepareTravelData(travelsFound);
    }


    private void prepareTravelData(String travelsJSON) {
        JSONParser parser = new JSONParser();
        try {
            Object travelsObjects = parser.parse(travelsJSON);
            JSONObject jsonObject = (JSONObject) travelsObjects;
            JSONArray entityList = (JSONArray) ((JSONObject) jsonObject.get("_embedded")).get("travels");

            for (Object obj : entityList) {
                JSONObject jsonObj = (JSONObject) obj;
                travelList.add(new DriverTravel(
                        (String) jsonObj.get("login"),
                        (String) jsonObj.get("firstName"),
                        (String) jsonObj.get("lastName"),
                        (String) jsonObj.get("passengersCount"),
                        (String) jsonObj.get("maxPassengers"),
                        (String) jsonObj.get("startDatetime"),
                        (String) jsonObj.get("startPlace"),
                        (String) jsonObj.get("endPlace")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        mAdapter.notifyDataSetChanged();
    }
}
