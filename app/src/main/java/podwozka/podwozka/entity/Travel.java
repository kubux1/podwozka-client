package podwozka.podwozka.entity;

public class Travel {
    private String login;
    private String firstName;
    private String lastName;
    private String passengersCount;
    private String maxPassengers;
    private String startDatetime;
    private String startPlace;
    private String endPlace;


    public Travel(String login, String firstName, String lastName, String passengersCount,
                  String maxPassengers, String startDatetime, String startPlace, String endPlace) {
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.passengersCount = passengersCount;
        this.maxPassengers = maxPassengers;
        this.startDatetime = startDatetime;
        this.startPlace = startPlace;
        this.endPlace = endPlace;
    }

    public String getLogin() {
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

    public void setPassengersCount(String passengersCount) {
        this.passengersCount = passengersCount;
    }

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

    public String getEndPlace() {
        return endPlace;
    }

    public void setEndPlace(String endPlace) {
        this.endPlace = endPlace;
    }

    public String getMaxPassengers() {
        return maxPassengers;
    }

    public void setMaxPassengers(String maxPassengers) {
        this.maxPassengers = maxPassengers;
    }
}

