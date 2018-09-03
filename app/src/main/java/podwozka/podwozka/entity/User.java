package podwozka.podwozka.entity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import podwozka.podwozka.entity.HttpCommands;

public class User {
    private String login;

    public User(String login){
        this.login = login;
    }

    public User(){}

    public String getLogin() {
        return login;
    }


    public int logInUser(String login, String password)
    {
        int httpResponseCode;
        HttpCommands httpCommand = new HttpCommands();
        List<NameValuePair> logInData = new ArrayList<>(1);

        // Convert login data into Json
        try {
            // Automate this
            logInData.add(new BasicNameValuePair("username", login));
            logInData.add(new BasicNameValuePair("password", password));
        } catch (Exception e){
            e.printStackTrace();
        }

        httpResponseCode =  httpCommand.sendLogInData(logInData);
        return httpResponseCode;
    }

    public int registerUser(String login, String password, String email)
    {
        int httpResponseCode;
        HttpCommands httpCommand = new HttpCommands();
        List<NameValuePair> registerData = new ArrayList<>(1);

        // Convert login data into Json
        try {
            // Automate this
            registerData.add(new BasicNameValuePair("id", null));
            registerData.add(new BasicNameValuePair("login", login));
            registerData.add(new BasicNameValuePair("password", password));
            registerData.add(new BasicNameValuePair("email", email));

            // TODO: Implement
            registerData.add(new BasicNameValuePair("firstName", null));
            registerData.add(new BasicNameValuePair("lastName", null));
            registerData.add(new BasicNameValuePair("activated", null));
            registerData.add(new BasicNameValuePair("langKey", null));
            registerData.add(new BasicNameValuePair("authorities", null));
            registerData.add(new BasicNameValuePair("authorities", null));
        } catch (Exception e){
            e.printStackTrace();
        }

        httpResponseCode =  httpCommand.sendRegisterData(registerData);
        return httpResponseCode;
    }
}
