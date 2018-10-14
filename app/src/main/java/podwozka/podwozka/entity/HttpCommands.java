package podwozka.podwozka.entity;

import org.json.JSONObject;
import java.util.concurrent.CountDownLatch;
import static podwozka.podwozka.LoginActivity.user;

public class HttpCommands {
    private int httpResponseCode;
    private String response;

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

        connection.sendCommand("api/authenticate", "POST", dataToSend, null, latch);
        // Wait for a sendCommand task to end
        try {
            latch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            // Extract user id token from server response
            if (connection.getHttpResponseCode() == 200) {
                JSONObject jsonObj = new JSONObject(connection.getResponse());
                setResponse(jsonObj.getString("id_token"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return connection.getHttpResponseCode();
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

    public int deleteTravel(String travelId) {
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

        connection.sendCommand("api/travels/find", "POST", dataToSend, user.getIdToken(), latch);
        // Wait for a sendCommand task to end
        try {
            latch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setResponse(connection.getResponse());

        return connection.getHttpResponseCode();
    }

    public int signUpForTravel(String travelId) {
        Connection connection = new Connection();
        CountDownLatch latch = new CountDownLatch(1);
        // TODO: Implement for server signing up for travel
        connection.sendCommand("api/travels/signUp?login=" + user.getLogin() +
                        "&travelId=" + Long.parseLong(travelId) , "POST",
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
