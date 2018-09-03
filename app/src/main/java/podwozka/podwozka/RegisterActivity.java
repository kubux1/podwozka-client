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
                    alertWindow.showAlertWindow(RegisterActivity.this, null, "Proszę podać adres email");
                }
                else if (loginMessage.isEmpty())
                {
                    alertWindow.showAlertWindow(RegisterActivity.this, null, "Proszę podać login");
                }
                else if (passwordMessage.isEmpty())
                {
                    alertWindow.showAlertWindow(RegisterActivity.this, null, "Proszę podać hasło");
                }
                else
                {
                    boolean emailValidation = isValidEmail(emailAddressMessage);

                    if (emailValidation == true)
                    {
                        user = new User();
                        //httpResponseCode = user.registerUser(loginMessage, passwordMessage, emailAddressMessage);
                        //--------- START MOCK ---------
                        httpResponseCode = 201;
                        //--------- END MOCK ---------
                        if(httpResponseCode == 201) {
                            alertWindow.showAlertWindow(RegisterActivity.this, null, "Udało zarejestrować się konto");
                            startActivity(nextScreen);
                        } else if (httpResponseCode == 400){
                            alertWindow.showAlertWindow(RegisterActivity.this, null, "Konto z podanym adresem email lub login już istnieje");
                        }
                    } else
                    {
                        alertWindow.showAlertWindow(RegisterActivity.this, null, "Proszę podać istniejący adres email");
                    }
                }
            }
        });

    }
}
