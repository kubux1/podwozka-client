package podwozka.podwozka.entity;

import java.util.Objects;

public class Address {

    private Long id;

    private Long buildingNumber;

    private String street;

    private String postcode;

    private String locality;

    private String country;

    public Address() {
        // Empty constructor needed for Jackson.
    }

    public Address(String address) {
        // TODO: parse String
        this.buildingNumber = 1L;
        this.street = "Długa";
        this.postcode = "77-304";
        this.locality = "Gdańsk";
        this.country = "Polska";
    }

    public Address(Address address) {
        this.id = address.getId();
        this.buildingNumber = address.getBuildingNumber();
        this.street = address.getStreet();
        this.postcode = address.getPostcode();
        this.locality = address.getLocality();
        this.country = address.getCountry();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBuildingNumber() {
        return buildingNumber;
    }

    public void setBuildingNumber(Long buildingNumber) {
        this.buildingNumber = buildingNumber;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address)) return false;
        Address address = (Address) o;
        return Objects.equals(id, address.id) &&
                Objects.equals(buildingNumber, address.buildingNumber) &&
                Objects.equals(street, address.street) &&
                Objects.equals(postcode, address.postcode) &&
                Objects.equals(locality, address.locality) &&
                Objects.equals(country, address.country);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, buildingNumber, street, postcode, locality, country);
    }
}
