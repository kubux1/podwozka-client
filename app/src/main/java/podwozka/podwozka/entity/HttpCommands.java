package podwozka.podwozka.entity;

import java.io.InputStream;

public class HttpCommands {
    private int HttpResponseCode;

    public HttpCommands(){};

    public InputStream getAllUserTravles(String login){
        InputStream allUserTravels = null;

        Connection connection = new Connection();
            int code = connection.sendCommand("travels/?login="+login, "GET");
            if (code == 200) {
                allUserTravels = connection.getInputStream();
        }

        return allUserTravels;
    }
}
