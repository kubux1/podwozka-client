package podwozka.podwozka.Passenger.entity;

import android.os.Parcel;
import android.os.Parcelable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.util.ArrayList;
import podwozka.podwozka.entity.HttpCommands;
import podwozka.podwozka.LoginActivity;

public class PassangerTravel implements Parcelable {
    private Long travelId;
    private String login;
    private String startPlace;
    private String endPlace;
    private String firstName;
    private String lastName;
    private String passengersCount;
    private String startDateTime;
    private Long driverTravelId;

    public PassangerTravel(Long travelId, String login, String startPlace, String endPlace, String startDateTime, String passengersCount, Long driverTravelId) {
        this.travelId = travelId;
        if (login == null) {
            this.login = LoginActivity.user.getLogin();
        }
        else {
            this.login = login;
        }
        this.passengersCount = passengersCount;
        this.startDateTime = startDateTime;
        this.startPlace = startPlace;
        this.endPlace = endPlace;
        this.driverTravelId = driverTravelId;
    }

    public PassangerTravel(Long travelId, String login, String firstName, String lastName, String startPlace, String endPlace, String startDateTime, String passengersCount, Long driverTravelId) {
        this.travelId = travelId;
        if (login == null) {
            this.login = LoginActivity.user.getLogin();
        }
        else {
            this.login = login;
        }
        this.firstName = firstName;
        this.lastName = lastName;
        this.passengersCount = passengersCount;
        this.startDateTime = startDateTime;
        this.startPlace = startPlace;
        this.endPlace = endPlace;
        this.driverTravelId = driverTravelId;
    }

	public PassangerTravel(Parcel in) {
        String[] data = new String[7];

        in.readStringArray(data);

        this.login = data[0];
        this.firstName = data[1];
        this.lastName = data[2];
        this.passengersCount = data[3];
        this.startDateTime = data[4];
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

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDatetime(String startDateTime) {
        this.startDateTime = startDateTime;
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

    public Long getTravelId () { return travelId; }

    public void setTravelId (Long travelId) { this.travelId = travelId; }
	
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
	
    public String getAllUserTravles() {
        HttpCommands httpCommand = new HttpCommands();

        httpCommand.getAllUserTravles();
        return httpCommand.getResponse();
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
            jsonObject.put("pickUpDatetime", passengerTravel.getStartDateTime());
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
                        (Long)jsonObj.get("id"),
                        (String)jsonObj.get("login"),
                        (String)jsonObj.get("firstName"),
                        (String)jsonObj.get("lastName"),
                        (String)jsonObj.get("startPlace"),
                        (String)jsonObj.get("endPlace"),
                        (String)jsonObj.get("startDateTime"),
                        (String)jsonObj.get("passengersCount"),
                        (Long)jsonObj.get("driverId")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return travels;
    }
}

