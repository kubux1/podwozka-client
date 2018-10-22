package podwozka.podwozka.entity;

import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.Map;

import static podwozka.podwozka.LoginActivity.user;

public class Car {

    private long id;
    private String driverLogin;
    private String model;
    private String brand;
    private String color;
    private long productionYear;
    private long registrationNumber;
    private long maxPassengersCapacity;


    private final static String MODEL = "model";
    private final static String BRAND = "brand";
    private final static String COLOR = "color";
    private final static String PRODUCTION_YEAR = "productionYear";
    private final static String REGISTRATION_NUMBER = "registrationNumber";
    private final static String MAX_PASSENGERS_CAP= "maxPassangersCapacity";

    public Car (){}

    public Car(long id, String driverLogin, String model, String brand, String color, long productionYear, long registrationNumber, long maxPassengersCapacity) {
        this.id = id;
        this.driverLogin = driverLogin;
        this.model = model;
        this.brand = brand;
        this.color = color;
        this.productionYear = productionYear;
        this.registrationNumber = registrationNumber;
        this.maxPassengersCapacity = maxPassengersCapacity;
    }

    public Car(Map<String, String> car){
        this.driverLogin = user.getLogin();
        this.model = car.get(MODEL);
        this.brand = car.get(BRAND);
        this.color = car.get(COLOR);
        this.productionYear = Integer.parseInt(car.get(PRODUCTION_YEAR));
        this.registrationNumber = Integer.parseInt(car.get(REGISTRATION_NUMBER));
        this.maxPassengersCapacity = Integer.parseInt(car.get(MAX_PASSENGERS_CAP));
    }

    public Long getId() {
        return id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public long getProductionYear() {
        return productionYear;
    }

    public void setProductionYear(int productionYear) {
        this.productionYear = productionYear;
    }

    public long getRegistrationNumber() {
        return registrationNumber;
    }

    public String getDriverLogin() {
        return driverLogin;
    }

    public void setRegistrationNumber(int registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public long getMaxPassengersCapacity() {
        return maxPassengersCapacity;
    }

    public void setMaxPassengersCapacity(int maxPassengersCapacity) {
        this.maxPassengersCapacity = maxPassengersCapacity;
    }

    public String newCarToJSON (Car car){
        JSONObject carInJsonObject = new JSONObject();

        try {
            carInJsonObject.put("id", carInJsonObject.NULL);
            carInJsonObject.put("driverLogin", car.getDriverLogin());
            carInJsonObject.put(MODEL, car.getModel());
            carInJsonObject.put(BRAND, car.getBrand());
            carInJsonObject.put(COLOR, car.getColor());
            carInJsonObject.put(PRODUCTION_YEAR, car.getProductionYear());
            carInJsonObject.put(REGISTRATION_NUMBER, car.getRegistrationNumber());
            carInJsonObject.put(MAX_PASSENGERS_CAP, car.getMaxPassengersCapacity());
        } catch (Exception e){
            e.printStackTrace();
        }
        return carInJsonObject.toString();
    }

    public Car JSONToCar (String carInJSON){
        JSONParser parser = new JSONParser();
        Car car = null;

        try {
            JSONObject carInJsonObj = (JSONObject)parser.parse(carInJSON);
            car = new Car(
                    (Long)carInJsonObj.get("id"),
                    (String)carInJsonObj.get("driverLogin"),
                    (String)carInJsonObj.get(MODEL),
                    (String)carInJsonObj.get(BRAND),
                    (String)carInJsonObj.get(COLOR),
                    (Long)carInJsonObj.get(PRODUCTION_YEAR),
                    (Long)carInJsonObj.get(REGISTRATION_NUMBER),
                    (Long)carInJsonObj.get(MAX_PASSENGERS_CAP)
            );
        } catch (Exception e){
            e.printStackTrace();
        }
        return car;
    }
}
