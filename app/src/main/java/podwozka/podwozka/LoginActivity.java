package podwozka.podwozka;

import podwozka.podwozka.Driver.DriverMainActivity;
import podwozka.podwozka.Passenger.PassangerMainActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import settings.ConnectionSettings;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginButton = (Button) findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                String loginOptionMessage = "";
                Intent nextScreen = null;
                int errorsCount = 0;

                EditText login = (EditText) findViewById(R.id.loginField);
                String loginMessage = login.getText().toString();

                EditText password = (EditText) findViewById(R.id.passwordField);
                String passwordMessage = password.getText().toString();

                RadioButton driverRadioButton = (RadioButton) findViewById(R.id.driverRadioButton);
                RadioButton passangerRadioButton = (RadioButton) findViewById(R.id.passangerRadioButton);
                if (driverRadioButton.isChecked()){
                    loginOptionMessage = "Driver";
                    nextScreen = new Intent(getApplicationContext(), DriverMainActivity.class);
                }
                else if (passangerRadioButton.isChecked()){
                    loginOptionMessage = "Passanger";
                    nextScreen = new Intent(getApplicationContext(), PassangerMainActivity.class);
                }
                else {
                    PopUpWindows alertWindow = new PopUpWindows();
                    alertWindow.showAlert(LoginActivity.this, null, "Proszę wybrać profil logowania");
                    errorsCount +=1 ;
                }

                if (errorsCount == 0){
                    sendLoginData(loginMessage, passwordMessage, loginOptionMessage);
                    startActivity(nextScreen);
                }
            }
        });
    }

    private void sendLoginData(final String login, final String password, final String loginOption) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ConnectionSettings connectionSettings = new ConnectionSettings();
                    URL url = new URL(connectionSettings.getHostIP() + ":"
                            + connectionSettings.getHostPort()
                            + "/travel_data/?login=" + login
                            + "&password=" + password
                            + "&option=" + loginOption);

                    HttpURLConnection connection = (HttpURLConnection)url.openConnection();

                    connection.setRequestMethod("GET");

                    connection.connect();
                    int code = connection.getResponseCode();
                    if (code == 200) {
                        InputStream in = connection.getInputStream();

                        Scanner scanner = new Scanner(in);
                        scanner.useDelimiter("\\A");

                        boolean hasInput = scanner.hasNext();
                        if (hasInput) {
                            String content = scanner.next();
                        }
                    }

                    connection.disconnect();
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        thread.start();
    }
}
