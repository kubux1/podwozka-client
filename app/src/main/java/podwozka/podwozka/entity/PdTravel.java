package podwozka.podwozka.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Objects;

import podwozka.podwozka.Constants;

public class PdTravel {
    private Long travelId;
    private String driverLogin;
    private PdPlace startPlace;
    private PdPlace endPlace;
    private String firstName;
    private String lastName;
    private Long passengersCount;
    private String pickUpDatetime;

    public PdTravel() {
    }

    public PdTravel(Long travelId, String driverLogin, PdPlace startPlace, PdPlace endPlace,
                    String firstName, String lastName, Long passengersCount,
                    String pickUpDatetime) {
        this.travelId = travelId;
        this.driverLogin = driverLogin;
        this.startPlace = startPlace;
        this.endPlace = endPlace;
        this.firstName = firstName;
        this.lastName = lastName;
        this.passengersCount = passengersCount;
        this.pickUpDatetime = pickUpDatetime;
    }

    public String toJson() throws JsonProcessingException {
        ObjectMapper mapper = Constants.getDefaultObjectMapper();
        return mapper.writeValueAsString(this);
    }

    public Long getTravelId() {
        return travelId;
    }

    public void setTravelId(Long travelId) {
        this.travelId = travelId;
    }

    public String getDriverLogin() {
        return driverLogin;
    }

    public void setDriverLogin(String driverLogin) {
        this.driverLogin = driverLogin;
    }

    public PdPlace getStartPlace() {
        return startPlace;
    }

    public void setStartPlace(PdPlace startPlace) {
        this.startPlace = startPlace;
    }

    public PdPlace getEndPlace() {
        return endPlace;
    }

    public void setEndPlace(PdPlace endPlace) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PdTravel)) return false;
        PdTravel pdTravel = (PdTravel) o;
        return Objects.equals(travelId, pdTravel.travelId) &&
                Objects.equals(driverLogin, pdTravel.driverLogin) &&
                Objects.equals(startPlace, pdTravel.startPlace) &&
                Objects.equals(endPlace, pdTravel.endPlace) &&
                Objects.equals(firstName, pdTravel.firstName) &&
                Objects.equals(lastName, pdTravel.lastName) &&
                Objects.equals(passengersCount, pdTravel.passengersCount) &&
                Objects.equals(pickUpDatetime, pdTravel.pickUpDatetime);
    }

    @Override
    public int hashCode() {

        return Objects.hash(travelId, driverLogin, startPlace, endPlace, firstName, lastName,
                passengersCount, pickUpDatetime);
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
}
