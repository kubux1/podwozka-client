package podwozka.podwozka.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.location.places.Place;

import java.util.Objects;
import java.util.Optional;

public class PlaceDTO implements Parcelable {

    private Long id;

    private double latitude;

    private double longitude;

    private String name;

    private AddressDTO address;

    public PlaceDTO(Place place) {
        this.latitude = place.getLatLng().latitude;
        this.longitude = place.getLatLng().longitude;
        setNameFromCharSequence(place.getName());
        this.address = new AddressDTO(Optional.ofNullable(
                place.getAddress()).orElse("")
                .toString());
    }

    public PlaceDTO() {
        // Empty constructor needed for Jackson.
    }

    protected PlaceDTO(Parcel in) {
        id = in.readLong();
        latitude = in.readDouble();
        longitude = in.readDouble();
        name = in.readString();
        address = in.readParcelable(AddressDTO.class.getClassLoader());
    }

    public static final Creator<PlaceDTO> CREATOR = new Creator<PlaceDTO>() {
        @Override
        public PlaceDTO createFromParcel(Parcel in) {
            return new PlaceDTO(in);
        }

        @Override
        public PlaceDTO[] newArray(int size) {
            return new PlaceDTO[size];
        }
    };

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AddressDTO getAddress() {
        return address;
    }

    public void setAddress(AddressDTO addressDTO) {
        this.address = addressDTO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlaceDTO)) return false;
        PlaceDTO placeDTO = (PlaceDTO) o;
        return Double.compare(placeDTO.latitude, latitude) == 0 &&
                Double.compare(placeDTO.longitude, longitude) == 0 &&
                Objects.equals(id, placeDTO.id) &&
                Objects.equals(name, placeDTO.name) &&
                Objects.equals(address, placeDTO.address);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, latitude, longitude, name, address);
    }

    private void setNameFromCharSequence(CharSequence name) {
        String tmp = new StringBuilder(name).toString();
        tmp = tmp.replace("\'", " ");
        tmp= tmp.replace("\"", " ");
        this.name = tmp.replace("°", " ");
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeLong(id);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
        parcel.writeString(name);
        parcel.writeParcelable(address, flags);
    }
}
