package podwozka.podwozka.entity;

import java.util.Objects;

public class AddressDTO {

    private Long id;

    private Long buildingNumber;

    private String street;

    private String postcode;

    private String locality;

    private String country;

    public AddressDTO() {
        // Empty constructor needed for Jackson.
    }

    public AddressDTO(String address) {
        // TODO: parse String
        this.buildingNumber = 1L;
        this.street = "Długa";
        this.postcode = "77-304";
        this.locality = "Gdańsk";
        this.country = "Polska";
    }

    public AddressDTO(AddressDTO addressDTO) {
        this.id = addressDTO.getId();
        this.buildingNumber = addressDTO.getBuildingNumber();
        this.street = addressDTO.getStreet();
        this.postcode = addressDTO.getPostcode();
        this.locality = addressDTO.getLocality();
        this.country = addressDTO.getCountry();
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
        if (!(o instanceof AddressDTO)) return false;
        AddressDTO addressDTO = (AddressDTO) o;
        return Objects.equals(id, addressDTO.id) &&
                Objects.equals(buildingNumber, addressDTO.buildingNumber) &&
                Objects.equals(street, addressDTO.street) &&
                Objects.equals(postcode, addressDTO.postcode) &&
                Objects.equals(locality, addressDTO.locality) &&
                Objects.equals(country, addressDTO.country);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, buildingNumber, street, postcode, locality, country);
    }
}
