package podwozka.podwozka.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

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

    protected TravelDTO(Parcel in) {
        id = in.readLong();
        driverLogin = in.readString();
        startPlace = in.readParcelable(PlaceDTO.class.getClassLoader());
        endPlace = in.readParcelable(PlaceDTO.class.getClassLoader());
        firstName = in.readString();
        lastName = in.readString();
        passengersCount = in.readLong();
        pickUpDatetime = in.readString();
    }

    public static final Creator<TravelDTO> CREATOR = new Creator<TravelDTO>() {
        @Override
        public TravelDTO createFromParcel(Parcel in) {
            return new TravelDTO(in);
        }

        @Override
        public TravelDTO[] newArray(int size) {
            return new TravelDTO[size];
        }
    };

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
        dest.writeParcelable(this.startPlace, flags);
        dest.writeParcelable(this.endPlace, flags);
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeLong(this.passengersCount);
        dest.writeString(this.pickUpDatetime);
    }
}
