package podwozka.podwozka.entity;

import org.apache.http.NameValuePair;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class HttpCommands {
    private int HttpResponseCode;

    public HttpCommands(){};

    public InputStream getAllUserTravles(String login){
        Thread thread = new Thread();
        InputStream allUserTravels = null;

        Connection connection = new Connection();
        CountDownLatch latch = new CountDownLatch(1);
        connection.sendGetCommand("travels?login="+login, latch);
        try {
            latch.await();
        } catch (Exception e)
        {

        }
        int code = connection.getHttpResponseCode();
        if (code == 200) {
            allUserTravels = connection.getInputStream();
        }

        return allUserTravels;
    }

    public String findMatchingTravels(List<NameValuePair> object){
        String travelsFound;
        Connection connection = new Connection();

        travelsFound = connection.sendPostCommand("/api/travels", object);

        return travelsFound;
    }

    public int postNewTravel(List<NameValuePair> object){
        int httpResponse;
        Connection connection = new Connection();

        connection.sendPostCommand("travels", object);
        httpResponse = connection.getHttpResponseCode();

        return httpResponse;
    }

    public int sendLogInData(List<NameValuePair> object){
        int httpResponse;
        Connection connection = new Connection();

        connection.sendPostCommand("users", object);
        httpResponse = connection.getHttpResponseCode();

        return httpResponse;
    }

    public int sendRegisterData(List<NameValuePair> object){
        int httpResponse;
        Connection connection = new Connection();

        connection.sendPostCommand("users", object);
        httpResponse = connection.getHttpResponseCode();

        return httpResponse;
    }
}
