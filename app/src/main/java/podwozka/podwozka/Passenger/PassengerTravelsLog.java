package podwozka.podwozka.Passenger;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.view.View;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import podwozka.podwozka.Passenger.entity.PassangerTravel;
import podwozka.podwozka.R;
import java.util.Calendar;

public class PassengerTravelsLog extends AppCompatActivity {
    private List<PassangerTravel> travelList = new ArrayList<>();
    private RecyclerView recyclerView;
    private PassengerBrowseTravelsAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travels_log);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new PassengerBrowseTravelsAdapter(travelList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        // TODO: Implement after server side is ready
        //travelsFound = new PassangerTravel().getAllUserTravles();

        final String travelsFound =
                "{\n" +
                        "  \"_embedded\" : {\n" +
                        "    \"travels\" : [ {\n" +
                        "      \"login\" : \"bartek\",\n" +
                        "      \"firstName\" : \"Maciej\",\n" +
                        "      \"lastName\" : \"Topola\",\n" +
                        "      \"passengersCount\" : \"2\",\n" +
                        "      \"maxPassengers\" : \"3\",\n" +
                        "      \"startDatetime\" : \"2016-03-16 12:56\",\n" +
                        "      \"startPlace\" : \"Gdynia, 10 Lutego\",\n" +
                        "      \"endPlace\" : \"Gdańsk, Wrzeszcz\"\n" +
                        "    },\n" +
                        "\t{\n" +
                        "      \"login\" : \"bartek\",\n" +
                        "      \"firstName\" : \"Maciej\",\n" +
                        "      \"lastName\" : \"Topola\",\n" +
                        "      \"passengersCount\" : \"2\",\n" +
                        "      \"maxPassengers\" : \"3\",\n" +
                        "      \"startDatetime\" : \"2016-03-16 12:56\",\n" +
                        "      \"startPlace\" : \"Gdynia, 10 Lutego\",\n" +
                        "      \"endPlace\" : \"Gdańsk, Matarnia\"\n" +
                        "    } ]\n" +
                        "}\n" +
                        "}";

        prepareTravelData(travelsFound, "past");

        Button currentTravelsOption = (Button) findViewById(R.id.currentTravels);
        currentTravelsOption.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                prepareTravelData(travelsFound, "current");
            }
        });

        Button pastTravelsOption = (Button) findViewById(R.id.pastTravels);
        pastTravelsOption.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                prepareTravelData(travelsFound, "past");
            }
        });
    }
    private void prepareTravelData (String travelsJSON, String order) {
        JSONParser parser = new JSONParser();
        Date currentTime = Calendar.getInstance().getTime();
        try {
            Object travelsObjects = parser.parse(travelsJSON);
            JSONObject jsonObject = (JSONObject) travelsObjects;
            JSONArray entityList = (JSONArray)((JSONObject) jsonObject.get("_embedded")).get("travels");

            for (Object obj : entityList) {
                JSONObject jsonObj = (JSONObject) obj;
                if (order == "past" && ((Date) jsonObj.get("startDatetime")).before(currentTime)) {
                    travelList.add(new PassangerTravel(
                            (String) jsonObj.get("login"),
                            (String) jsonObj.get("firstName"),
                            (String) jsonObj.get("lastName"),
                            (String) jsonObj.get("passengersCount"),
                            (String) jsonObj.get("maxPassengers"),
                            (String) jsonObj.get("startDatetime"),
                            (String) jsonObj.get("startPlace"),
                            (String) jsonObj.get("endPlace")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        mAdapter.notifyDataSetChanged();
    }
}
