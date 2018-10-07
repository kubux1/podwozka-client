package podwozka.podwozka.Passenger.entity;

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

import podwozka.podwozka.Driver.entity.DriverTravel;
import podwozka.podwozka.entity.HttpCommands;
import podwozka.podwozka.LoginActivity;

public class PassangerTravel implements Parcelable {
    private String login;
    private String firstName;
    private String lastName;
    private String maxPassengers;
    private String passengersCount;
    private String startDatetime;
    private String startPlace;
    private String endPlace;
    private int travelId;

    public PassangerTravel(String login, String startPlace, String endPlace, String startDatetime, String passengersCount) {
        if (login == null) {
            this.login = LoginActivity.user.getLogin();
        }
        else {
            this.login = login;
        }
        this.passengersCount = passengersCount;
        this.startDatetime = startDatetime;
        this.startPlace = startPlace;
        this.endPlace = endPlace;
    }

    public PassangerTravel(String login, String firstName, String lastName, String startPlace, String endPlace, String startDatetime, String passengersCount) {
        if (login == null) {
            this.login = LoginActivity.user.getLogin();
        }
        else {
            this.login = login;
        }
        this.firstName = firstName;
        this.lastName = lastName;
        this.passengersCount = passengersCount;
        this.startDatetime = startDatetime;
        this.startPlace = startPlace;
        this.endPlace = endPlace;
    }

	public PassangerTravel(Parcel in) {
        String[] data = new String[7];

        in.readStringArray(data);

        this.login = data[0];
        this.firstName = data[1];
        this.lastName = data[2];
        this.passengersCount = data[3];
        this.startDatetime = data[4];
        this.startPlace = data[5];
        this.endPlace = data[6];
    }

    public PassangerTravel(){}
	
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

    public int getTravelId () { return travelId; }

    public void setTravelId (int travelId) { this.travelId = travelId; }
	
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{
                this.firstName,
                this.startPlace,
                this.endPlace
        });
    }

    public static final Creator CREATOR = new Creator() {
        public PassangerTravel createFromParcel(Parcel in) {
            return new PassangerTravel(in);
        }

        public PassangerTravel[] newArray(int size) {
            return new PassangerTravel[size];
        }
    };
	
    public int getAllUserTravles() {
        HttpCommands httpCommand = new HttpCommands();

        httpCommand.getAllUserTravles();
        return httpCommand.getHttpResponseCode();
    }

    public String findMatchingTravels(PassangerTravel passengerTravel){
        HttpCommands httpCommand = new HttpCommands();
        org.json.JSONObject jsonObject = new org.json.JSONObject();

        // Convert POST data into JSON format
        try {
            // Automate this
            jsonObject.put("id", jsonObject.NULL);
            jsonObject.put("login", passengerTravel.getLogin());
            jsonObject.put("startPlace", passengerTravel.getStartPlace());
            jsonObject.put("endPlace", passengerTravel.getEndPlace());
            jsonObject.put("pickUpDatetime", passengerTravel.getStartDatetime());
            jsonObject.put("passengersCount", passengerTravel.getPassengersCount());

            httpCommand.findTravels(jsonObject.toString());
        } catch (Exception e){
            e.printStackTrace();
        }
        return httpCommand.getResponse();
    }

    private ArrayList<PassangerTravel> getTravelsJSONParser(String travelsJSON) {
        ArrayList<PassangerTravel> travels = new ArrayList<PassangerTravel>();
        JSONParser parser = new JSONParser();

        try {
            Object travelsObjects = parser.parse(travelsJSON);
            JSONObject jsonObject = (JSONObject) travelsObjects;
            JSONArray entityList = (JSONArray) jsonObject.get("entity");

            for (Object obj : entityList) {
                JSONObject jsonObj = (JSONObject) obj;
                travels.add(new PassangerTravel(
                        (String)jsonObj.get("login"),
                        (String)jsonObj.get("firstName"),
                        (String)jsonObj.get("lastName"),
                        (String)jsonObj.get("passengersCount"),
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

