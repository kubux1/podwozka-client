package podwozka.podwozka;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.text.TextUtils;

import java.net.HttpURLConnection;

import podwozka.podwozka.Libs.AppStatus;
import podwozka.podwozka.entity.User;

public class RegisterActivity extends AppCompatActivity {
    public static User user;
    public static final int PASSWORD_MIN_LENGTH = 4;
    public static final int PASSWORD_MAX_LENGTH = 100;

    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button registerButton = (Button) findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (AppStatus.getInstance(RegisterActivity.this).isOnline()) {
                    int httpResponseCode;
                    PopUpWindows alertWindow = new PopUpWindows();
                    //Starting a new Intent
                    Intent nextScreen = new Intent(getApplicationContext(), MainActivity.class);

                    EditText name = findViewById(R.id.nameField);
                    String nameMessage = name.getText().toString();

                    EditText surname = findViewById(R.id.surnameField);
                    String surnameMessage = surname.getText().toString();

                    EditText login = findViewById(R.id.loginField);
                    String loginMessage = login.getText().toString();

                    EditText password = findViewById(R.id.passwordField);
                    String passwordMessage = password.getText().toString();

                    EditText passwordRetype = findViewById(R.id.passwordRetypeField);
                    String passwordRetypeMessage = passwordRetype.getText().toString();

                    EditText emailAddress = findViewById(R.id.emailAddressField);
                    String emailAddressMessage = emailAddress.getText().toString();

                    if (nameMessage.isEmpty()) {
                        alertWindow.showAlertWindow(RegisterActivity.this, null, getResources().getString(R.string.name_empty));
                    } else if (surnameMessage.isEmpty()) {
                        alertWindow.showAlertWindow(RegisterActivity.this, null, getResources().getString(R.string.surname_empty));
                    } else if (emailAddressMessage.isEmpty()) {
                        alertWindow.showAlertWindow(RegisterActivity.this, null, getResources().getString(R.string.email_empty));
                    } else if (loginMessage.isEmpty()) {
                        alertWindow.showAlertWindow(RegisterActivity.this, null, getResources().getString(R.string.login_empty));
                    } else if (passwordMessage.isEmpty()) {
                        alertWindow.showAlertWindow(RegisterActivity.this, null, getResources().getString(R.string.password_empty));
                    } else if (passwordRetypeMessage.isEmpty()) {
                        alertWindow.showAlertWindow(RegisterActivity.this, null, getResources().getString(R.string.password_retype_empty));
                    } else if (passwordMessage.length() < PASSWORD_MIN_LENGTH) {
                        alertWindow.showAlertWindow(RegisterActivity.this, null, getResources().getString(R.string.password_too_short));
                    } else if (passwordMessage.length() > PASSWORD_MAX_LENGTH) {
                        alertWindow.showAlertWindow(RegisterActivity.this, null, getResources().getString(R.string.password_too_long));
                    } else if (!passwordMessage.equals(passwordRetypeMessage)) {
                        alertWindow.showAlertWindow(RegisterActivity.this, null, getResources().getString(R.string.passwords_diffrent));
                    } else {
                        boolean emailValidation = isValidEmail(emailAddressMessage.trim());

                        if (emailValidation == true) {
                            user = new User();
                            httpResponseCode = user.registerUser(nameMessage.trim(),
                                    surnameMessage.trim(),
                                    loginMessage.trim(),
                                    passwordMessage.trim(),
                                    emailAddressMessage.trim());
                            if (httpResponseCode == HttpURLConnection.HTTP_CREATED) {
                                nextScreen.putExtra("MESSAGE", getResources().getString(R.string.registration_successful));
                                startActivity(nextScreen);

                            } else if (httpResponseCode == HttpURLConnection.HTTP_BAD_REQUEST) {
                                alertWindow.windowTimeout(3).showAlertWindow(RegisterActivity.this, null, getResources().getString(R.string.email_user_exists));
                            } else if (httpResponseCode == HttpURLConnection.HTTP_UNAVAILABLE) {
                                alertWindow.showAlertWindow(RegisterActivity.this, null, getResources().getString(R.string.server_down));
                            } else {
                                alertWindow.showAlertWindow(RegisterActivity.this, null, getResources().getString(R.string.unknown_error));
                            }
                        } else {
                            alertWindow.showAlertWindow(RegisterActivity.this, null, getResources().getString(R.string.email_not_valid));
                        }
                    }
                } else {
                    new PopUpWindows().showAlertWindow(RegisterActivity.this, null, getResources().getString(R.string.no_internet_connection));
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent nextScreen = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(nextScreen);
        finish();
    }
}
