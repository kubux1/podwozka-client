package podwozka.podwozka;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import podwozka.podwozka.entity.Travel;
import settings.ConnectionSettings;


public class NewTravelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_travel);


        Button btnNextScreen = (Button) findViewById(R.id.submitNewTravelButton);
        btnNextScreen.setText("Zatwierdź");

        btnNextScreen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                //Starting a new Intent
                Intent nextScreen = new Intent(getApplicationContext(), NewTravelActivity.class);

                EditText startTravelPlace = (EditText) findViewById(R.id.startTravelPlace);
                String startTravelPlaceMessage = startTravelPlace.getText().toString();

                EditText endTravelPlace = (EditText) findViewById(R.id.endTravelPlace);
                String endTravelPlaceMessage = endTravelPlace.getText().toString();

                sendTravelData(startTravelPlaceMessage, endTravelPlaceMessage);

                startActivity(nextScreen);

            }
        });
    }

    private void sendTravelData(final String startPlace, final String endPlace) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ConnectionSettings connectionSettings = new ConnectionSettings();
                    URL url = new URL(connectionSettings.getHostIP() + ":" + connectionSettings.getHostPort() + "/travel_data/?startPlace=" + startPlace +"&endPlace=" + endPlace);
                    HttpURLConnection connection = (HttpURLConnection)url.openConnection();

                    connection.setRequestMethod("GET");

                    connection.connect();
                    int code = connection.getResponseCode();
                    if (code == 200) {
                        InputStream in = connection.getInputStream();

                        Scanner scanner = new Scanner(in);
                        scanner.useDelimiter("\\A");

                        boolean hasInput = scanner.hasNext();
                        if (hasInput) {
                            String content = scanner.next();
                            List<Travel> travelList = getTravelsJSONParser(content);

                            Intent intent = new Intent(getApplicationContext(), BrowseTravelsActivity.class);
                            intent.putExtra("TRAVEL_LIST", (Serializable) travelList);
                            startActivity(intent);
                        }
                    }

                    connection.disconnect();
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        thread.start();
    }


    private List<Travel> getTravelsJSONParser(String travelsJSON) {
        List<Travel> travels = new ArrayList<Travel>();
        JSONParser parser = new JSONParser();
        try {
            Object travelsObjects = parser.parse(travelsJSON);
            JSONObject jsonObject = (JSONObject) travelsObjects;
            JSONArray entityList = (JSONArray) jsonObject.get("entity");

            for (Object obj : entityList) {
                JSONObject jsonObj = (JSONObject) obj;
                travels.add(new Travel((String)jsonObj.get("name"),(String)jsonObj.get("start"),(String)jsonObj.get("end")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return travels;
    }
}
