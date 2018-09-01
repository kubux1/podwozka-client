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

import podwozka.podwozka.entity.HttpCommands;
import podwozka.podwozka.entity.User;
import settings.ConnectionSettings;

public class LoginActivity extends AppCompatActivity {
    public static User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginButton = (Button) findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            PopUpWindows alertWindow = new PopUpWindows();

            public void onClick(View arg0) {
                String loginOptionMessage = "";
                Intent nextScreen = null;
                int errorsCount = 0;
                int httpResponseCode;

                EditText login = (EditText) findViewById(R.id.loginField);
                String loginMessage = login.getText().toString();

                EditText password = (EditText) findViewById(R.id.passwordField);
                String passwordMessage = password.getText().toString();

                RadioButton driverRadioButton = (RadioButton) findViewById(R.id.driverRadioButton);
                RadioButton passangerRadioButton = (RadioButton) findViewById(R.id.passangerRadioButton);

                if (loginMessage.isEmpty()) {
                    alertWindow.showAlertWindow(LoginActivity.this, null, "Proszę podać login");
                    errorsCount += 1;
                } else if (passwordMessage.isEmpty()) {
                    alertWindow.showAlertWindow(LoginActivity.this, null, "Proszę podać hasło");
                    errorsCount += 1;
                } else {
                    if (driverRadioButton.isChecked()) {
                        loginOptionMessage = "Driver";
                        nextScreen = new Intent(getApplicationContext(), DriverMainActivity.class);
                    } else if (passangerRadioButton.isChecked()) {
                        loginOptionMessage = "Passanger";
                        nextScreen = new Intent(getApplicationContext(), PassangerMainActivity.class);
                    } else {
                        alertWindow.showAlertWindow(LoginActivity.this, null, "Proszę wybrać profil logowania");
                        errorsCount += 1;
                    }
                }
                if (errorsCount == 0) {
                    user = new User(loginMessage);
                    //httpResponseCode = user.logInUser(loginMessage, passwordMessage);
                    httpResponseCode = 201;
                    if (httpResponseCode == 201) {
                        nextScreen.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(nextScreen);
                        finish(); // call this to finish the current activity
                    } else {
                        alertWindow.showAlertWindow(LoginActivity.this, null, "Nie udało się zalogować");
                    }
                }
            }
        });
    }
}
