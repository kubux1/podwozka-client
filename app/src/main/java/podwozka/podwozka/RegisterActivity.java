package podwozka.podwozka;

import android.content.Intent;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.text.TextUtils;
import android.widget.RadioButton;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import podwozka.podwozka.entity.User;
import settings.ConnectionSettings;



public class RegisterActivity extends AppCompatActivity {
    public static User user;
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
                int httpResponseCode;
                PopUpWindows alertWindow = new PopUpWindows();
                //Starting a new Intent
                Intent nextScreen = new Intent(getApplicationContext(), MainActivity.class);

                EditText login = (EditText) findViewById(R.id.loginField);
                String loginMessage = login.getText().toString();

                EditText password = (EditText) findViewById(R.id.passwordField);
                String passwordMessage = password.getText().toString();

                EditText emailAddress = (EditText) findViewById(R.id.emailAddressField);
                String emailAddressMessage = emailAddress.getText().toString();

                if (emailAddressMessage.isEmpty())
                {
                    alertWindow.showAlertWindow(RegisterActivity.this, null, getResources().getString(R.string.email_empty));
                }
                else if (loginMessage.isEmpty())
                {
                    alertWindow.showAlertWindow(RegisterActivity.this, null, getResources().getString(R.string.login_empty));
                }
                else if (passwordMessage.isEmpty())
                {
                    alertWindow.showAlertWindow(RegisterActivity.this, null, getResources().getString(R.string.password_empty));
                }
                else
                {
                    boolean emailValidation = isValidEmail(emailAddressMessage);

                    if (emailValidation == true)
                    {
                        user = new User();
                        httpResponseCode = user.registerUser(loginMessage, passwordMessage, emailAddressMessage);
                        if(httpResponseCode == 201) {
                            alertWindow.showAlertWindow(RegisterActivity.this, null, getResources().getString(R.string.registration_successful));
                            startActivity(nextScreen);
                        } else if (httpResponseCode == 400){
                            alertWindow.showAlertWindow(RegisterActivity.this, null, getResources().getString(R.string.email_already_exists));
                        }
                    } else
                    {
                        alertWindow.showAlertWindow(RegisterActivity.this, null, getResources().getString(R.string.email_not_valid));
                    }
                }
            }
        });

    }
}
