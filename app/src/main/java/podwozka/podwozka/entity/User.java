package podwozka.podwozka.entity;

import org.json.JSONObject;

public class User {
    private String login;
    private String idToken;
    private String loginOption;

    public String driver = "Driver";
    public String passenger = "Passenger";

    public User(String login, String loginOption){
        this.login = login;
        this.loginOption = loginOption;
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

    public String getLoginOption(){
        return this.loginOption;
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

    public int registerUser(String name, String surname, String login, String password, String email)
    {
        JSONObject jsonObject = new JSONObject();
        int httpResponseCode = 0;
        HttpCommands httpCommand = new HttpCommands();

        // Convert register data into Json
        try {
            jsonObject.put("id", jsonObject.NULL);
            jsonObject.put("firstName", name);
            jsonObject.put("lastName", surname);
            jsonObject.put("login", login);
            jsonObject.put("password", password);
            jsonObject.put("email", email);
            httpResponseCode =  httpCommand.sendRegisterData(jsonObject.toString());
        } catch (Exception e){
            e.printStackTrace();
        }
        return httpResponseCode;
    }
}
