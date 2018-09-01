package podwozka.podwozka.Driver.entity;

import android.os.Parcel;
import android.os.Parcelable;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import podwozka.podwozka.entity.HttpCommands;

public class DriverTravel implements Parcelable {
    private String login;
    private String firstName;
    private String lastName;
    private String passengersCount;
    private String maxPassengers;
    private String startDatetime;
    private String startPlace;
    private String endPlace;


    public DriverTravel(String login, String firstName, String lastName, String passengersCount,
                        String maxPassengers, String startDatetime, String startPlace, String endPlace) {
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.passengersCount = passengersCount;
        this.maxPassengers = maxPassengers;
        this.startDatetime = startDatetime;
        this.startPlace = startPlace;
        this.endPlace = endPlace;
    }

    public DriverTravel(String login, String startPlace, String endPlace, String startDatetime, String maxPassengers) {
        this.login = login;
        this.maxPassengers = maxPassengers;
        this.startDatetime = startDatetime;
        this.startPlace = startPlace;
        this.endPlace = endPlace;
    }

	public DriverTravel(Parcel in) {
        String[] data = new String[7];

        in.readStringArray(data);

        this.login = data[0];
        this.firstName = data[1];
        this.lastName = data[2];
        this.passengersCount = data[3];
        this.maxPassengers = data[4];
        this.startDatetime = data[5];
        this.startPlace = data[6];
        this.endPlace = data[7];
    }

    public DriverTravel(){}
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{
                this.firstName,
                this.startPlace,
                this.endPlace
        });
    }

    public static final Creator CREATOR = new Creator() {
        public DriverTravel createFromParcel(Parcel in) {
            return new DriverTravel(in);
        }

        public DriverTravel[] newArray(int size) {
            return new DriverTravel[size];
        }
    };
	
    public String getLogin() {
        return login;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassengersCount() {
        return passengersCount;
    }

    public void setPassengersCount(String passengersCount) { this.passengersCount = passengersCount; }

    public String getStartDatetime() {
        return startDatetime;
    }

    public void setStartDatetime(String startDatetime) {
        this.startDatetime = startDatetime;
    }

    public String getStartPlace() {
        return startPlace;
    }

    public void setStartPlace(String startPlace) {
        this.startPlace = startPlace;
    }

    public String getEndPlace() { return endPlace; }

    public void setEndPlace(String endPlace) {
        this.endPlace = endPlace;
    }

    public String getMaxPassengers() {
        return maxPassengers;
    }

    public void setMaxPassengers(String maxPassengers) {
        this.maxPassengers = maxPassengers;
    }
	
	    public int describeContents() {
        return 0;
    }
	
    public String getAllUserTravlesFromServer() {
        String userTravles;
        HttpCommands httpCommand = new HttpCommands();

        userTravles = httpCommand.getAllUserTravles(this.login).toString();
        return userTravles;
    }

    public int postNewTravel (DriverTravel newTravel)
    {
        int httpResponse;
        List<NameValuePair> travel = new ArrayList<>(1);
        HttpCommands httpCommand = new HttpCommands();
        try {
            // Automate this
            travel.add(new BasicNameValuePair("login", newTravel.getLogin()));
            travel.add(new BasicNameValuePair("startPlace", newTravel.getStartPlace()));
            travel.add(new BasicNameValuePair("endPlace", newTravel.getEndPlace()));
            travel.add(new BasicNameValuePair("startDatetime", newTravel.getStartDatetime()));
            travel.add(new BasicNameValuePair("maxPassengers", newTravel.getPassengersCount()));
        } catch (Exception e){
            e.printStackTrace();
        }
        httpResponse = httpCommand.postNewTravel(travel);
        return httpResponse;
    }

    private ArrayList<DriverTravel> getTravelsJSONParser(String travelsJSON) {
        ArrayList<DriverTravel> travels = new ArrayList<DriverTravel>();
        JSONParser parser = new JSONParser();

        try {
            Object travelsObjects = parser.parse(travelsJSON);
            JSONObject jsonObject = (JSONObject) travelsObjects;
            JSONArray entityList = (JSONArray) jsonObject.get("entity");

            for (Object obj : entityList) {
                JSONObject jsonObj = (JSONObject) obj;
                travels.add(new DriverTravel(
                        (String)jsonObj.get("login"),
                        (String)jsonObj.get("firstName"),
                        (String)jsonObj.get("lastName"),
                        (String)jsonObj.get("passengersCount"),
                        (String)jsonObj.get("maxPassengers"),
                        (String)jsonObj.get("startDatetime"),
                        (String)jsonObj.get("startPlace"),
                        (String)jsonObj.get("endPlace")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return travels;
    }
}

