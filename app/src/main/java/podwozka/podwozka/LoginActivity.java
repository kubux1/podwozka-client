package podwozka.podwozka;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
                //Starting a new Intent
                Intent nextScreen = new Intent(getApplicationContext(), NewTravelActivity.class);

                EditText login = (EditText) findViewById(R.id.loginField);
                String loginMessage = login.getText().toString();

                EditText password = (EditText) findViewById(R.id.passwordField);
                String passwordMessage = password.getText().toString();

                sendLoginData(loginMessage, passwordMessage);
                startActivity(nextScreen);
            }
        });
    }

    private void sendLoginData(final String login, final String password) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ConnectionSettings connectionSettings = new ConnectionSettings();
                    URL url = new URL(connectionSettings.getHostIP() + ":"
                            + connectionSettings.getHostPort()
                            + "/travel_data/?login=" + login
                            +"&password=" + password);

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
