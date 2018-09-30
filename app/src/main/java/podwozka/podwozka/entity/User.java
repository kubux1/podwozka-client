package podwozka.podwozka.entity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import podwozka.podwozka.entity.HttpCommands;

public class User {
    private String login;
    private String idToken;

    public User(String login){
        this.login = login;
    }

    public User(){}

    public String getLogin() {
        return login;
    }

    private void setIdToken(String idToken){
        this.idToken = idToken;
    }

    public String getIdToken(){
        return this.idToken;
    }

    public int logInUser(String login, String password)
    {
        int httpResponseCode = -1;
        HttpCommands httpCommand = new HttpCommands();
        JSONObject jsonObject = new JSONObject();

        // Convert login data into Json
        try {
            // Automate this
            jsonObject.put("username", login);
            jsonObject.put("password", password);

            httpResponseCode =  httpCommand.sendLogInData(jsonObject.toString());
            setIdToken(httpCommand.getResponse());
        } catch (Exception e){
            e.printStackTrace();
        }
        return httpResponseCode;
    }

    public int registerUser(String login, String password, String email)
    {
        JSONObject jsonObject = new JSONObject();
        int httpResponseCode = 0;
        HttpCommands httpCommand = new HttpCommands();

        // Convert register data into Json
        try {
            jsonObject.put("id", jsonObject.NULL);
            jsonObject.put("login", login);
            jsonObject.put("password", password);
            jsonObject.put("email", email);

            // TODO: Implement
            jsonObject.put("firstName", "testName");
            jsonObject.put("lastName", "testSurname");

            httpResponseCode =  httpCommand.sendRegisterData(jsonObject.toString());
        } catch (Exception e){
            e.printStackTrace();
        }
        return httpResponseCode;
    }
}
