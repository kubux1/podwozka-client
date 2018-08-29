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

import settings.ConnectionSettings;



public class RegisterActivity extends AppCompatActivity {

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
                //Starting a new Intent
                Intent nextScreen = new Intent(getApplicationContext(), RegisterActivity.class);

                EditText login = (EditText) findViewById(R.id.loginField);
                String loginMessage = login.getText().toString();

                EditText password = (EditText) findViewById(R.id.passwordField);
                String passwordMessage = password.getText().toString();

                EditText emailAddress = (EditText) findViewById(R.id.emailAddressField);
                String emailAddressMessage = emailAddress.getText().toString();
                boolean emailValidation = isValidEmail(emailAddressMessage);

                if(emailValidation == true) {
                    sendRegisterData(loginMessage, passwordMessage, emailAddressMessage);
                    startActivity(nextScreen);
                }
                else {
                PopUpWindows alertWindow = new PopUpWindows();
                alertWindow.showAlertWindow(RegisterActivity.this, null, "Proszę podać poprawny adres email");
                }
            }
        });

    }

    private void sendRegisterData(final String login, final String password, final String emailAddress) {
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
