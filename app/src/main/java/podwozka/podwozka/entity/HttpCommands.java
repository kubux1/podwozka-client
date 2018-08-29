package podwozka.podwozka.entity;

import org.apache.http.NameValuePair;

import java.io.InputStream;
import java.util.List;

public class HttpCommands {
    private int HttpResponseCode;

    public HttpCommands(){};

    public InputStream getAllUserTravles(String login){
        InputStream allUserTravels = null;

        Connection connection = new Connection();
        int code = connection.sendGetCommand("/api/travels/?login="+login);
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
}
