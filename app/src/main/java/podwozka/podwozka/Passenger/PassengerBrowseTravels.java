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

import podwozka.podwozka.Passenger.entity.PassangerTravel;
import podwozka.podwozka.Passenger.PassangerRecyclerItemClickListener;
import podwozka.podwozka.R;
import podwozka.podwozka.PopUpWindows;
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;

public class PassengerBrowseTravels extends AppCompatActivity {
    private List<PassangerTravel> travelList = new ArrayList<>();
    private RecyclerView recyclerView;
    private PassengerBrowseTravelsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String travelsFound;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_travels_list);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new PassengerBrowseTravelsAdapter(travelList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        travelsFound = getIntent().getStringExtra("TRAVELS");
        travelsFound =
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
        prepareTravelData(travelsFound);

        recyclerView.addOnItemTouchListener(
                new PassangerRecyclerItemClickListener(PassengerBrowseTravels.this, recyclerView ,new PassangerRecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        final PassangerTravel travel = mAdapter.returnTravel(position);
                        AlertDialog.Builder builder = new AlertDialog.Builder(PassengerBrowseTravels.this);

                        builder.setMessage("Czy napewno chcesz zapisać się na tę podróż?");

                        builder.setNegativeButton("Nie", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();
                            }
                        });

                        builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {

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
            Object travelsObjects = parser.parse(travelsJSON);
            JSONObject jsonObject = (JSONObject) travelsObjects;
            JSONArray entityList = (JSONArray)((JSONObject) jsonObject.get("_embedded")).get("travels");

            for (Object obj : entityList) {
                JSONObject jsonObj = (JSONObject) obj;
                travelList.add(new PassangerTravel(
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



