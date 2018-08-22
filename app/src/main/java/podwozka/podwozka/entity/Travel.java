package podwozka.podwozka.entity;

import podwozka.podwozka.entity.HttpCommands;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Travel implements Parcelable {
    private String login;
    private String firstName;
    private String lastName;
    private String passengersCount;
    private String maxPassengers;
    private String startDatetime;
    private String startPlace;
    private String endPlace;


    public Travel(String login, String firstName, String lastName, String passengersCount,
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

	public Travel(Parcel in) {
        String[] data = new String[3];

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

    public Travel(){}
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{
                this.firstName,
                this.startPlace,
                this.endPlace
        });
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Travel createFromParcel(Parcel in) {
            return new Travel(in);
        }

        public Travel[] newArray(int size) {
            return new Travel[size];
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
	
    public ArrayList getAllUserTravlesFromServer() {
        ArrayList userTravlesArrayList = null;
        InputStream userTravelsStream = null;
        HttpCommands httpCommand = new HttpCommands();

        userTravelsStream = httpCommand.getAllUserTravles(this.login);
        Scanner scanner = new Scanner(userTravelsStream);
        scanner.useDelimiter("\\A");
        boolean hasInput = scanner.hasNext();
        if (hasInput) {
            String content = scanner.next();
            userTravlesArrayList = getTravelsJSONParser(content);
        }
        return userTravlesArrayList;
    }

    private ArrayList<Travel> getTravelsJSONParser(String travelsJSON) {
        ArrayList<Travel> travels = new ArrayList<Travel>();
        JSONParser parser = new JSONParser();
        try {
            Object travelsObjects = parser.parse(travelsJSON);
            JSONObject jsonObject = (JSONObject) travelsObjects;
            JSONArray entityList = (JSONArray) jsonObject.get("entity");

            for (Object obj : entityList) {
                JSONObject jsonObj = (JSONObject) obj;
                travels.add(new Travel(
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

