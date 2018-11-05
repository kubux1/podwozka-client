package podwozka.podwozka;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    Locale myLocale;
    String currentLanguage = "en", currentLang;
    Spinner spinner;
    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button loginButton = findViewById(R.id.loginButton);
        Button registerButton = findViewById(R.id.registerButton);
        List<String> langList = new ArrayList<String>();

        loginButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                //Starting a new Intent
                Intent nextScreen = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(nextScreen);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                //Starting a new Intent
                Intent nextScreen = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(nextScreen);
            }
        });

        // Language choose option
        currentLanguage = getIntent().getStringExtra(currentLang);
        spinner = (Spinner) findViewById(R.id.spinner);

        langList.add("Select language");
        langList.add("English");
        langList.add("Polski");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, langList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        setLocale("en");
                        break;
                    case 2:
                        setLocale("pl");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        checkForMessages();
        checkLocationPermission();
    }

    @Override
    public void onBackPressed() {
    }

    public void setLocale(String localeName) {
        if (!localeName.equals(currentLanguage)) {
            myLocale = new Locale(localeName);
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
            Intent refresh = new Intent(this, MainActivity.class);
            refresh.putExtra(currentLang, localeName);
            startActivity(refresh);
        } else {
            Toast.makeText(MainActivity.this, "Language already selected!", Toast.LENGTH_SHORT).show();
        }
    }

    // Check if there was is any message from previous activity
    public void checkForMessages(){
        Intent i = getIntent();
        String message = i.getStringExtra("MESSAGE");
        if(message !=  null){
            new PopUpWindows().showAlertWindow(MainActivity.this, null, message);
        }
    }

    public void checkLocationPermission(){
        // Check if permission for location is already granted
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, ask for permission
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_FINE_LOCATION);

        }
    }

    //For future use
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                // Check if permission was granted.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Enable the functionality that depends on this permission.
                    int x = 2;
                } else {
                    // Disable the functionality that depends on this permission.
                    int y = 2;
                }
                return;
            }
        }
    }
}
