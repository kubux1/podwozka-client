package podwozka.podwozka.entity;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static podwozka.podwozka.LoginActivity.user;

public class HttpCommands {
    private int httpResponseCode;
    private String response;

    private int serverTimeoutInSeconds = 5;
    private TimeUnit timeUnit = TimeUnit.SECONDS;

    public HttpCommands() {
    }

    public int getHttpResponseCode() {
        return this.httpResponseCode;
    }

    private void setHttpResponseCode(int httpResponseCode) {
        this.httpResponseCode = httpResponseCode;
    }

    public String getResponse() {
        return this.response;
    }

    private void setResponse(String response) {
        this.response = response;
    }

    public int sendLogInData(String dataToSend) {
        CountDownLatch latch = new CountDownLatch(1);
        Connection connection = new Connection();
        int httpResponseCode;

        connection.sendCommand("api/authenticate", "POST", dataToSend, null, latch);
        // Wait for a sendCommand task to end
        try {
            latch.await(serverTimeoutInSeconds, timeUnit);
        } catch (Exception e) {
            e.printStackTrace();
        }
        httpResponseCode = connection.getHttpResponseCode();

        if (httpResponseCode == HttpURLConnection.HTTP_OK) {
            try {
                // Extract user id token from server response
                JSONObject jsonObj = new JSONObject(connection.getResponse());
                setResponse(jsonObj.getString("id_token"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if(httpResponseCode == 0){
            httpResponseCode = HttpURLConnection.HTTP_UNAVAILABLE;
        }

        return httpResponseCode;
    }

    public int sendRegisterData(String data) {
        Connection connection = new Connection();
        CountDownLatch latch = new CountDownLatch(1);

        connection.sendCommand("api/register", "POST", data, null, latch);
        // Wait for a sendCommand task to end
        try {
            latch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection.getHttpResponseCode();
    }

    public int postNewTravel(String dataToSend) {
        Connection connection = new Connection();
        CountDownLatch latch = new CountDownLatch(1);

        connection.sendCommand("api/travels", "POST", dataToSend, user.getIdToken(), latch);
        // Wait for a sendCommand task to end
        try {
            latch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection.getHttpResponseCode();
    }

    public int editTravelInfo(String dataToSend) {
        Connection connection = new Connection();
        CountDownLatch latch = new CountDownLatch(1);

        connection.sendCommand("api/travels", "PUT", dataToSend, user.getIdToken(), latch);
        // Wait for a sendCommand task to end
        try {
            latch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection.getHttpResponseCode();
    }

    public int deleteTravel(Long travelId) {
        Connection connection = new Connection();
        CountDownLatch latch = new CountDownLatch(1);

        connection.sendCommand("api/travels/delete/" + travelId, "DELETE", null, user.getIdToken(), latch);
        // Wait for a sendCommand task to end
        try {
            latch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection.getHttpResponseCode();
    }

    public int getAllUserTravles() {
        Connection connection = new Connection();
        CountDownLatch latch = new CountDownLatch(1);

        if(user.getLoginOption().equals(user.driver)) {
            connection.sendCommand("api/travels?login=" + user.getLogin(), "GET", null, user.getIdToken(), latch);
        } else if(user.getLoginOption().equals(user.passenger)){
            connection.sendCommand("api/travels/passenger?login=" + user.getLogin(), "GET", null, user.getIdToken(), latch);
        }
        // Wait for a sendCommand task to end
        try {
            latch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setResponse(connection.getResponse());

        return connection.getHttpResponseCode();
    }

    public int findTravels(String dataToSend) {
        Connection connection = new Connection();
        CountDownLatch latch = new CountDownLatch(1);

        connection.sendCommand("api/travels/findMatching", "POST", dataToSend, user.getIdToken(), latch);
        // Wait for a sendCommand task to end
        try {
            latch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setResponse(connection.getResponse());

        return connection.getHttpResponseCode();
    }

    public int findMatchingPassengerTravels(Long driverTravelId) {
        Connection connection = new Connection();
        CountDownLatch latch = new CountDownLatch(1);

        connection.sendCommand("api/passenger/travels?login=" + user.getLogin() + "&driverTravelId=" + driverTravelId, "GET", null, user.getIdToken(), latch);
        // Wait for a sendCommand task to end
        try {
            latch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setResponse(connection.getResponse());

        return connection.getHttpResponseCode();
    }

    public int signUpForTravel(Long travelId) {
        Connection connection = new Connection();
        CountDownLatch latch = new CountDownLatch(1);

        connection.sendCommand("api/travels/signUp?login=" + user.getLogin() +
                        "&travelId=" + travelId, "POST",
                null, user.getIdToken(), latch);
        // Wait for a sendCommand task to end
        try {
            latch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setResponse(connection.getResponse());

        return connection.getHttpResponseCode();
    }

    public int getUserName(String login) {
        Connection connection = new Connection();
        CountDownLatch latch = new CountDownLatch(1);

        connection.sendCommand("api/users/name?login="+login, "GET",
                null, user.getIdToken(), latch);
        // Wait for a sendCommand task to end
        try {
            latch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setResponse(connection.getResponse());

        return connection.getHttpResponseCode();
    }

    public int addNewCar(String dataToSend) {
        Connection connection = new Connection();
        CountDownLatch latch = new CountDownLatch(1);

        connection.sendCommand("api/cars", "POST",
                dataToSend, user.getIdToken(), latch);
        // Wait for a sendCommand task to end
        try {
            latch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection.getHttpResponseCode();
    }

    public int getCar() {
        Connection connection = new Connection();
        CountDownLatch latch = new CountDownLatch(1);

        connection.sendCommand("api/cars?login=" + user.getLogin(), "GET",
                null, user.getIdToken(), latch);
        // Wait for a sendCommand task to end
        try {
            latch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setResponse(connection.getResponse());

        return connection.getHttpResponseCode();
    }

    public int getCarLimited(String driverLogin) {
        Connection connection = new Connection();
        CountDownLatch latch = new CountDownLatch(1);

        connection.sendCommand("api/cars/restricted?login=" + driverLogin, "GET",
                null, user.getIdToken(), latch);
        // Wait for a sendCommand task to end
        try {
            latch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setResponse(connection.getResponse());

        return connection.getHttpResponseCode();
    }
}
