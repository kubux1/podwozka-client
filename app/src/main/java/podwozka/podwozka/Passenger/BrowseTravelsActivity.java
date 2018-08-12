package podwozka.podwozka.Passenger;

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

import podwozka.podwozka.R;
import podwozka.podwozka.entity.Travel;


public class BrowseTravelsActivity extends AppCompatActivity {
    private List<Travel> travelList = new ArrayList<>();
    private RecyclerView recyclerView;
    private BrowseTravelsActivityAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_travels_list);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new BrowseTravelsActivityAdapter(travelList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        String travelsJSON = getIntent().getStringExtra("TRAVELS");

        prepareTravelData(travelsJSON);
    }


    private void prepareTravelData (String travelsJSON) {
        JSONParser parser = new JSONParser();
        try {
            Object travelsObjects = parser.parse(travelsJSON);
            JSONObject jsonObject = (JSONObject) travelsObjects;
            JSONArray entityList = (JSONArray)((JSONObject) jsonObject.get("_embedded")).get("travels");

            for (Object obj : entityList) {
                JSONObject jsonObj = (JSONObject) obj;
                travelList.add(new Travel(
                        (String)jsonObj.get("login"),
                        (String)jsonObj.get("firstName"),
                        (String)jsonObj.get("lastName"),
                        (String)jsonObj.get("passengersCount"),
                        (String)jsonObj.get("maxPassengers"),
                        (String)jsonObj.get("startDatetime"),
                        (String)jsonObj.get("startPlace"),
                        (String)jsonObj.get("endPlace")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        mAdapter.notifyDataSetChanged();
    }
}



