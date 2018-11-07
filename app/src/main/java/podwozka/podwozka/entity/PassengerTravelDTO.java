package podwozka.podwozka.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

public class PassengerTravelDTO implements Parcelable {

    private Long id;

    private String login;

    private String startPlace;

    private String endPlace;

    private String firstName;

    private String lastName;

    private Long driverId;

    private String pickUpDatetime;

    public PassengerTravelDTO() {
        // Empty constructor needed for Jackson.
    }

    protected PassengerTravelDTO(Parcel in) {
        id = in.readLong();
        login = in.readString();
        startPlace = in.readString();
        endPlace = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        driverId = in.readLong();
        pickUpDatetime = in.readString();
    }

    public static final Creator<PassengerTravelDTO> CREATOR = new Creator<PassengerTravelDTO>() {
        @Override
        public PassengerTravelDTO createFromParcel(Parcel in) {
            return new PassengerTravelDTO(in);
        }

        @Override
        public PassengerTravelDTO[] newArray(int size) {
            return new PassengerTravelDTO[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PassengerTravelDTO)) return false;
        PassengerTravelDTO that = (PassengerTravelDTO) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(login, that.login) &&
                Objects.equals(startPlace, that.startPlace) &&
                Objects.equals(endPlace, that.endPlace) &&
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(driverId, that.driverId) &&
                Objects.equals(pickUpDatetime, that.pickUpDatetime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, startPlace, endPlace, firstName, lastName, driverId,
                pickUpDatetime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeLong(this.id);
        parcel.writeString(this.login);
        parcel.writeString(this.startPlace);
        parcel.writeString(this.endPlace);
        parcel.writeString(this.firstName);
        parcel.writeString(this.lastName);
        parcel.writeLong(this.driverId);
        parcel.writeString(this.pickUpDatetime);
    }
}
