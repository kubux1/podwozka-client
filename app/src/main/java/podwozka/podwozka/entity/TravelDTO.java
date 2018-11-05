package podwozka.podwozka.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Objects;

import podwozka.podwozka.Constants;

public class TravelDTO implements Parcelable {
    private Long id;

    private String driverLogin;

    private PlaceDTO startPlace;

    private PlaceDTO endPlace;

    private String firstName;

    private String lastName;

    private Long passengersCount;

    private String pickUpDatetime;

    public TravelDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDriverLogin() {
        return driverLogin;
    }

    public void setDriverLogin(String driverLogin) {
        this.driverLogin = driverLogin;
    }

    public PlaceDTO getStartPlace() {
        return startPlace;
    }

    public void setStartPlace(PlaceDTO startPlace) {
        this.startPlace = startPlace;
    }

    public PlaceDTO getEndPlace() {
        return endPlace;
    }

    public void setEndPlace(PlaceDTO endPlace) {
        this.endPlace = endPlace;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Long getPassengersCount() {
        return passengersCount;
    }

    public void setPassengersCount(Long passengersCount) {
        this.passengersCount = passengersCount;
    }

    public String getPickUpDatetime() {
        return pickUpDatetime;
    }

    public void setPickUpDatetime(String pickUpDatetime) {
        this.pickUpDatetime = pickUpDatetime;
    }

    public String toJson() throws JsonProcessingException {
        ObjectMapper mapper = Constants.getDefaultObjectMapper();
        return mapper.writeValueAsString(this);
    }

    public int post() {
        int httpResponse = -1;
        HttpCommands httpCommand = new HttpCommands();

        try {
            httpResponse = httpCommand.postNewTravel(toJson());
        } catch (JsonProcessingException e){
            e.printStackTrace();
        }
        return httpResponse;
    }

    public String findMatchingTravels(){
        HttpCommands httpCommand = new HttpCommands();

        try {
            httpCommand.findTravels(toJson());
        } catch (JsonProcessingException e){
            e.printStackTrace();
        }
        return httpCommand.getResponse();
    }

    public static String getAllUserTravles() {
        HttpCommands httpCommand = new HttpCommands();

        httpCommand.getAllUserTravles();
        return httpCommand.getResponse();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TravelDTO)) return false;
        TravelDTO travelDTO = (TravelDTO) o;
        return Objects.equals(id, travelDTO.id) &&
                Objects.equals(driverLogin, travelDTO.driverLogin) &&
                Objects.equals(startPlace, travelDTO.startPlace) &&
                Objects.equals(endPlace, travelDTO.endPlace) &&
                Objects.equals(firstName, travelDTO.firstName) &&
                Objects.equals(lastName, travelDTO.lastName) &&
                Objects.equals(passengersCount, travelDTO.passengersCount) &&
                Objects.equals(pickUpDatetime, travelDTO.pickUpDatetime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, driverLogin, startPlace, endPlace, firstName, lastName,
                passengersCount, pickUpDatetime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.driverLogin);
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeLong(this.passengersCount);
        dest.writeString(this.pickUpDatetime);
        dest.writeString(this.startPlace.getName());
        dest.writeString(this.endPlace.getName());
    }
}
