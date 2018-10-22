package podwozka.podwozka.Passenger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.net.HttpURLConnection;

import podwozka.podwozka.R;
import podwozka.podwozka.entity.Car;
import podwozka.podwozka.entity.HttpCommands;
import podwozka.podwozka.entity.User;

public class PassengerTravelDriverAndCar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_driver_car_info);

        HttpCommands httpCommand = new HttpCommands();
        int httpResponseCode;
        Intent i = getIntent();
        String driverLogin = i.getParcelableExtra("DRIVER_LOGIN");

        TextView driverName = findViewById(R.id.driverName);
        TextView driverCarBrand = findViewById(R.id.driverCarBrand);
        TextView driverCarModel = findViewById(R.id.driverCarModel);
        TextView driverCarColor = findViewById(R.id.driverCarColor);

        //TODO: Add error responses handling from server
        httpResponseCode = httpCommand.getUserName(driverLogin);
        if (httpResponseCode == HttpURLConnection.HTTP_OK) {
            driverName.setText(httpCommand.getResponse());
        }

        httpResponseCode = httpCommand.getCar(driverLogin);
        if (httpResponseCode == HttpURLConnection.HTTP_OK) {
            Car driverCar = new Car().JSONToCar(httpCommand.getResponse());
            driverCarBrand.setText(driverCar.getBrand());
            driverCarModel.setText(driverCar.getModel());
            driverCarColor.setText(driverCar.getColor());
        }
    }

    @Override
    public void onBackPressed() {
        Intent nextScreen = new Intent(PassengerTravelDriverAndCar.this, PassengerTravelsLog.class);
        startActivity(nextScreen);
        finish();
    }
}
