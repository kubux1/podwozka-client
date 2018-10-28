    package podwozka.podwozka.Driver.entity;

    import android.os.Parcel;
    import android.os.Parcelable;
    import org.apache.http.NameValuePair;
    import org.json.simple.JSONArray;
    import org.json.simple.JSONObject;
    import org.json.simple.parser.JSONParser;

    import java.text.DateFormat;
    import java.text.SimpleDateFormat;
    import java.util.ArrayList;
    import java.util.Date;
    import java.util.List;

    import podwozka.podwozka.Driver.DriverBrowseTravelsAdapter;
    import podwozka.podwozka.Driver.DriverMain;
    import podwozka.podwozka.entity.HttpCommands;

    public class DriverTravel implements Parcelable {
        private Long travelId;
        private String login;
        private String startPlace;
        private String endPlace;
        private String firstName;
        private String lastName;
        private String passengersCount;
        private String maxPassengers;
        private String startDatetime;


    public DriverTravel(Long travelId, String login, String startDatetime, String startPlace,
                        String endPlace, String passengersCount, String maxPassengers) {
        this.travelId = travelId;
        this.login = login;
        this.passengersCount = passengersCount;
        this.maxPassengers = maxPassengers;
        this.startDatetime = startDatetime;
        this.startPlace = startPlace;
        this.endPlace = endPlace;
    }

    public DriverTravel(Long travelId, String login, String firstName, String lastName, String startDatetime, String startPlace, String endPlace, Long passengersCount) {
        this.travelId = travelId;
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.passengersCount = passengersCount.toString();
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

    public DriverTravel(Long travelId, String login, String startPlace, String endPlace, String startDatetime, String maxPassengers) {
        this.travelId = travelId;
        this.login = login;
        this.maxPassengers = maxPassengers;
        this.startDatetime = startDatetime;
        this.startPlace = startPlace;
        this.endPlace = endPlace;
        }

	public DriverTravel(Parcel in) {
        this.travelId = in.readLong();
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

    public String getDriverLogin() {
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

    public Long getTravelId () { return travelId; }

    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.travelId);
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
	
    public String getAllUserTravles() {
        HttpCommands httpCommand = new HttpCommands();

        httpCommand.getAllUserTravles();
        return httpCommand.getResponse();
    }

    public int postNewTravel (DriverTravel newTravel)
    {
        int httpResponse = -1;
        HttpCommands httpCommand = new HttpCommands();
        org.json.JSONObject jsonObject = new org.json.JSONObject();

        // Convert POST data into JSON format
        try {
            // Automate this
            jsonObject.put("id", jsonObject.NULL);
            jsonObject.put("driverLogin", newTravel.getDriverLogin());
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
            jsonObject.put("driverLogin", editedTravel.getDriverLogin());
            jsonObject.put("startPlace", editedTravel.getStartPlace());
            jsonObject.put("endPlace", editedTravel.getEndPlace());
            jsonObject.put("pickUpDatetime", editedTravel.getStartDatetime());
            jsonObject.put("passengersCount", 1);

             httpCommand.editTravelInfo(jsonObject.toString());
        } catch (Exception e){
            e.printStackTrace();
        }
        return httpCommand.getHttpResponseCode();
    }

    public int deleteTravel(Long travelId){
        int httpResponse;
        HttpCommands httpCommand = new HttpCommands();

        httpResponse = httpCommand.deleteTravel(travelId);
        return httpResponse;
    }

    public int signUpForTravel(Long travelId) {
        HttpCommands httpCommand = new HttpCommands();

        return httpCommand.signUpForTravel(travelId);
    }

    public String findMatchingPassengerTravels(DriverTravel driverTravel)
    {
        HttpCommands httpCommand = new HttpCommands();

        httpCommand.findMatchingPassengerTravels(driverTravel.getTravelId());

        return httpCommand.getResponse();
    }

    public boolean prepareTravelData (String travelsJSON, String time, List<DriverTravel> travelList, DriverBrowseTravelsAdapter mAdapter) {
        JSONParser parser = new JSONParser();
        Date currentDate = new Date(), selectedDate;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        final String COMING = "coming";
        final String PAST = "past";
        boolean isEmpty = true;

        // Clear previous data
        travelList.clear();
        mAdapter.notifyDataSetChanged();

        try {
            JSONArray travelsObjects = (JSONArray)parser.parse(travelsJSON);
            for (Object obj : travelsObjects) {
                JSONObject jsonObj = (JSONObject) obj;
                selectedDate = dateFormat.parse((String) jsonObj.get("pickUpDatetime"));
                if (time.equals(COMING) & currentDate.before(selectedDate)) {
                    travelList.add(new DriverTravel(
                            (Long) jsonObj.get("id"),
                            (String) jsonObj.get("driverLogin"),
                            (String) jsonObj.get("pickUpDatetime"),
                            (String) jsonObj.get("startPlace"),
                            (String) jsonObj.get("endPlace"),
                            String.valueOf(jsonObj.get("passengersCount")),
                            String.valueOf(jsonObj.get("maxPassenger"))
                    ));
                    isEmpty = false;
                }
                else if (time.equals(PAST) & currentDate.after(selectedDate)) {
                    travelList.add(new DriverTravel(
                            (Long) jsonObj.get("id"),
                            (String) jsonObj.get("driverLogin"),
                            (String) jsonObj.get("pickUpDatetime"),
                            (String) jsonObj.get("startPlace"),
                            (String) jsonObj.get("endPlace"),
                            String.valueOf(jsonObj.get("passengersCount")),
                            String.valueOf(jsonObj.get("maxPassenger"))
                    ));
                    isEmpty = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        mAdapter.notifyDataSetChanged();
        return isEmpty;
    }
}
