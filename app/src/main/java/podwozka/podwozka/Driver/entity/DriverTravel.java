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

    import podwozka.podwozka.Driver.DriverMainActivity;
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
    private String travelId;


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
    public DriverTravel(String travelId, String login, String startPlace, String endPlace, String startDatetime, String maxPassengers) {
        this.travelId = travelId;
        this.login = login;
        this.maxPassengers = maxPassengers;
        this.startDatetime = startDatetime;
        this.startPlace = startPlace;
        this.endPlace = endPlace;
        }

	public DriverTravel(Parcel in) {
        this.travelId = in.readString();
        this.login = in.readString();
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.passengersCount = in.readString();
        this.maxPassengers = in.readString();
        this.startDatetime = in.readString();
        this.startPlace = in.readString();
        this.endPlace = in.readString();
    }

    public DriverTravel(){}

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.travelId);
        dest.writeString(this.login);
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.passengersCount);
        dest.writeString(this.maxPassengers);
        dest.writeString(this.startDatetime);
        dest.writeString(this.startPlace);
        dest.writeString(this.endPlace);
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

    public String getTravelId () { return travelId; }

    public void setTravelId (String travelId) { this.travelId = travelId; }

    public int describeContents() {
        return 0;
    }
	
    public String getAllUserTravlesFromServer(final String login, final String idToken) {
        HttpCommands httpCommand = new HttpCommands();

        httpCommand.getAllUserTravles();
        return httpCommand.getResponse();
    }

    public int postNewTravel (DriverTravel newTravel, String userIdToken)
    {
        int httpResponse = -1;
        HttpCommands httpCommand = new HttpCommands();
        org.json.JSONObject jsonObject = new org.json.JSONObject();

        // Convert POST data into JSON format
        try {
            // Automate this
            jsonObject.put("id", jsonObject.NULL);
            jsonObject.put("login", newTravel.getLogin());
            jsonObject.put("startPlace", newTravel.getStartPlace());
            jsonObject.put("endPlace", newTravel.getEndPlace());
            jsonObject.put("pickUpDatetime", newTravel.getStartDatetime());
            jsonObject.put("passengersCount", newTravel.getMaxPassengers());

            httpResponse = httpCommand.postNewTravel(jsonObject.toString());
        } catch (Exception e){
            e.printStackTrace();
        }
        return httpResponse;
    }


    public int editTravelInfo(DriverTravel editedTravel){
        HttpCommands httpCommand = new HttpCommands();
        org.json.JSONObject jsonObject = new org.json.JSONObject();

        // Convert PUT data into JSON format
        try {
            // Automate this
            jsonObject.put("id", editedTravel.getTravelId());
            jsonObject.put("login", editedTravel.getLogin());
            jsonObject.put("startPlace", editedTravel.getStartPlace());
            jsonObject.put("endPlace", editedTravel.getEndPlace());
            jsonObject.put("pickUpDatetime", editedTravel.getStartDatetime());
            jsonObject.put("passengersCount", editedTravel.getMaxPassengers());

             httpCommand.editTravelInfo(jsonObject.toString());
        } catch (Exception e){
            e.printStackTrace();
        }
        return httpCommand.getHttpResponseCode();
    }

    public int deleteTravel(String travelId){
        int httpResponse;
        List<NameValuePair> travel = new ArrayList<>(1);
        HttpCommands httpCommand = new HttpCommands();

        httpResponse = httpCommand.deleteTravel(travelId);
        return httpResponse;
    }

    public ArrayList<DriverTravel> getTravelsJSONParser(String travelsJSON) {
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

    public String convcerTravelsForRecyclerFormat(ArrayList<DriverTravel> travels ){
        int x = DriverMainActivity.driverTravels.size();
        DriverTravel travel;
        travel = travels.get(0);
        String travelsConverted =
                "{\n" +
                        "  \"_embedded\" : {\n" +
                        "    \"travels\" : [ {\n" +
                        "      \"login\" : \"bartek\",\n" +
                        "      \"firstName\" : \"Maciej\",\n" +
                        "      \"lastName\" : \"Topola\",\n" +
                        "      \"passengersCount\" : \"2\",\n" +
                        "      \"maxPassengers\" : \""+travel.getMaxPassengers()+"\",\n" +
                        "      \"startDatetime\" : \""+travel.getStartDatetime()+"\",\n" +
                        "      \"startPlace\" : \""+travel.getStartPlace()+"\",\n" +
                        "      \"endPlace\" : \""+travel.getEndPlace()+"\"\n" +
                        "    },\n";
        x-=1;
        int y = 1;
        while (x > 0){
            travel = travels.get(y);
            travelsConverted +=
                    "\t{\n" +
                            "      \"login\" : \"bartek\",\n" +
                            "      \"firstName\" : \"Maciej\",\n" +
                            "      \"lastName\" : \"Topola\",\n" +
                            "      \"passengersCount\" : \"2\",\n" +
                            "      \"maxPassengers\" : \""+travel.getMaxPassengers()+"\",\n" +
                            "      \"startDatetime\" : \""+travel.getStartDatetime()+"\",\n" +
                            "      \"startPlace\" : \""+travel.getStartPlace()+"\",\n" +
                            "      \"endPlace\" : \""+travel.getEndPlace()+"\"\n" +
                            "    },\n";
            x--;
            y++;
        }
        travelsConverted+=
                "    ]\n" +
                "}\n" +
                "}";

        return travelsConverted;
    }
}

