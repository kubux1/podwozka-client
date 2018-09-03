package podwozka.podwozka.entity;

import org.apache.http.NameValuePair;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class HttpCommands {

    public HttpCommands(){};

    public InputStream getAllUserTravles(String login){
        Thread thread = new Thread();
        InputStream allUserTravels = null;

        Connection connection = new Connection();
        CountDownLatch latch = new CountDownLatch(1);
        connection.sendGetCommand("api/travels?login="+login, null, latch);
        try {
            latch.await();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        int code = connection.getHttpResponseCode();
        if (code == 200) {
            allUserTravels = connection.getInputStream();
        }

        return allUserTravels;
    }

    public String findMatchingTravels(List<NameValuePair> object){
        String travelsFound = null;
        Connection connection = new Connection();
        CountDownLatch latch = new CountDownLatch(1);

        //travelsFound = connection.sendPostCommand("/api/travels", object, latch);
        try {
            latch.await();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return travelsFound;
    }

    public int postNewTravel(List<NameValuePair> object){
        int httpResponse;
        Connection connection = new Connection();
        CountDownLatch latch = new CountDownLatch(1);

        //connection.sendPostCommand("api/travels", object, latch);
        try {
            latch.await();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        httpResponse = connection.getHttpResponseCode();
        return httpResponse;
    }

    public int sendLogInData(List<NameValuePair> object){
        int httpResponse;
        Connection connection = new Connection();
        CountDownLatch latch = new CountDownLatch(1);
        String[] params = new String[3];
        params[0] = "api/authenticate";
        params[1]= object.toString();
        //connection.sendPostCommand("api/authenticate", object, latch);
        //connection.doInBackground(params);
        try {
            latch.await();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        httpResponse = connection.getHttpResponseCode();
        return httpResponse;
    }

    public int sendRegisterData(List<NameValuePair> object){
        int httpResponse;
        Connection connection = new Connection();
        CountDownLatch latch = new CountDownLatch(1);

        //connection.sendPostCommand("api/users", object, latch);
        try {
            latch.await();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        httpResponse = connection.getHttpResponseCode();
        return httpResponse;
    }

    public int editTravelInfo(List<NameValuePair> object){
        int httpResponse;
        Thread thread = new Thread();
        Connection connection = new Connection();
        CountDownLatch latch = new CountDownLatch(1);

        connection.sendGetCommand("api/travels", object, latch);
        try {
            latch.await();
        } catch (Exception e)
        {

        }

        httpResponse = connection.getHttpResponseCode();
        return httpResponse;
    }

    public int deleteTravel(List<NameValuePair> object){
        int httpResponse;
        Thread thread = new Thread();
        Connection connection = new Connection();
        CountDownLatch latch = new CountDownLatch(1);

        int travelId =  Integer.parseInt(object.get(0).getValue());
        connection.sendGetCommand("api/travels/delete/"+travelId, null, latch);
        try {
            latch.await();
        } catch (Exception e)
        {

        }

        httpResponse = connection.getHttpResponseCode();
        return httpResponse;
    }

}
