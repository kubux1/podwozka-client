package podwozka.podwozka;

import podwozka.podwozka.Driver.DriverMain;
import podwozka.podwozka.Passenger.PassangerMain;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import podwozka.podwozka.entity.User;

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

                EditText login = findViewById(R.id.loginField);
                String loginMessage = login.getText().toString();

                EditText password = findViewById(R.id.passwordField);
                String passwordMessage = password.getText().toString();

                RadioButton driverRadioButton = findViewById(R.id.driverRadioButton);
                RadioButton passangerRadioButton = findViewById(R.id.passangerRadioButton);

                if (loginMessage.isEmpty()) {
                    alertWindow.showAlertWindow(LoginActivity.this, null, getResources().getString(R.string.login_empty));
                    errorsCount += 1;
                } else if (passwordMessage.isEmpty()) {
                    alertWindow.showAlertWindow(LoginActivity.this, null, getResources().getString(R.string.password_empty));
                    errorsCount += 1;
                } else {
                    if (driverRadioButton.isChecked()) {
                        loginOptionMessage = "Driver";
                        nextScreen = new Intent(getApplicationContext(), DriverMain.class);
                    } else if (passangerRadioButton.isChecked()) {
                        loginOptionMessage = "Passenger";
                        nextScreen = new Intent(getApplicationContext(), PassangerMain.class);
                    } else {
                        alertWindow.showAlertWindow(LoginActivity.this, null, getResources().getString(R.string.account_type_empty));
                        errorsCount += 1;
                    }
                }
                if (errorsCount == 0) {
                    user = new User(loginMessage, loginOptionMessage);
                    httpResponseCode = user.logInUser(loginMessage, passwordMessage);
                    if (httpResponseCode == 200) {
                        nextScreen.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(nextScreen);
                        finish(); // call this to finish the current activity
                    } else {
                        alertWindow.showAlertWindow(LoginActivity.this, null, getResources().getString(R.string.account_not_existing));
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent nextScreen = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(nextScreen);
        finish();
    }
}
