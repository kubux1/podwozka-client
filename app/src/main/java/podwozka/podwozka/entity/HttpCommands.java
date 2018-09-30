package podwozka.podwozka.entity;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static podwozka.podwozka.LoginActivity.user;

public class HttpCommands {
    private int httpResponseCode;
    private String response;

    public HttpCommands(){};

    public int getHttpResponseCode() {
        return this.httpResponseCode;
    }

    private void setHttpResponseCode(int httpResponseCode){
        this.httpResponseCode = httpResponseCode;
    }

    public String getResponse() {
        return this.response;
    }

    private void setResponse(String response) {
        this.response = response;
    }

    public int sendLogInData(String dataToSend){
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
            JSONObject jsonObj = new JSONObject(connection.getResponse());
            setResponse(jsonObj.getString("id_token"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return connection.getHttpResponseCode();
    }

    public int sendRegisterData(String data){
        Connection connection = new Connection();
        CountDownLatch latch = new CountDownLatch(1);

        connection.sendCommand("api/register", "POST", data,null, latch);
        // Wait for a sendCommand task to end
        try {
            latch.await();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return connection.getHttpResponseCode();
    }

    public int postNewTravel(String dataToSend){
        Connection connection = new Connection();
        CountDownLatch latch = new CountDownLatch(1);

        //connection.sendPostCommand("api/travels", dataToSend, user.getIdToken(), latch);
        connection.sendCommand("api/travels", "POST", dataToSend, user.getIdToken(), latch);
        // Wait for a sendCommand task to end
        try {
            latch.await();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return connection.getHttpResponseCode();
    }

    public int editTravelInfo(String dataToSend){
        Connection connection = new Connection();
        CountDownLatch latch = new CountDownLatch(1);

        connection.sendCommand("api/travels", "PUT", dataToSend, user.getIdToken(), latch);
        // Wait for a sendCommand task to end
        try {
            latch.await();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return connection.getHttpResponseCode();
    }

    public int deleteTravel(String travelId){
        Connection connection = new Connection();
        CountDownLatch latch = new CountDownLatch(1);

        //connection.sendDeleteCommand("api/travels/delete/" + travelId, user.getIdToken(), latch);
        connection.sendCommand("api/travels/delete/" + travelId, "DELETE", null, user.getIdToken(), latch);
        // Wait for a sendCommand task to end
        try {
            latch.await();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return connection.getHttpResponseCode();
    }

    public int getAllUserTravles(){
        Connection connection = new Connection();
        CountDownLatch latch = new CountDownLatch(1);

        connection.sendCommand("api/travels?login=" + user.getLogin(), "GET",null, user.getIdToken(), latch);
        // Wait for a sendCommand task to end
        try {
            latch.await();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        setResponse(connection.getResponse());

        return connection.getHttpResponseCode();
    }

    public String findMatchingTravels(String dataToSend){
        Connection connection = new Connection();
        CountDownLatch latch = new CountDownLatch(1);

        //TODO: Waiting for a server side to have functionality to return matching travels for a passanger travel
        // connection.someCommandWhichWillReturnMatchingTravels
        // Wait for a task to end
        try {
            latch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return connection.getResponse();
    }
}
