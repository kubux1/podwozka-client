package podwozka.podwozka.entity;

import com.google.android.gms.location.places.Place;

import java.util.Objects;
import java.util.Optional;

public class PdPlace {

    private Long id;

    private double latitude;

    private double longitude;

    private String name;

    private Address address;

    public PdPlace(Place place) {
        this.latitude = place.getLatLng().latitude;
        this.longitude = place.getLatLng().longitude;
        this.name = place.getName().toString();
        this.address = new Address(Optional.ofNullable(
                place.getAddress()).orElse("")
                .toString());
    }

    public PdPlace(PdPlace pdPlace) {
        this.id = pdPlace.getId();
        this.latitude = pdPlace.getLatitude();
        this.longitude = pdPlace.getLongitude();
        this.name = pdPlace.getName();
        this.address = new Address(pdPlace.getAddress());
    }

    public PdPlace() {
        // Empty constructor needed for Jackson.
    }

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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PdPlace)) return false;
        PdPlace pdPlace = (PdPlace) o;
        return Double.compare(pdPlace.latitude, latitude) == 0 &&
                Double.compare(pdPlace.longitude, longitude) == 0 &&
                Objects.equals(id, pdPlace.id) &&
                Objects.equals(name, pdPlace.name) &&
                Objects.equals(address, pdPlace.address);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, latitude, longitude, name, address);
    }
}
