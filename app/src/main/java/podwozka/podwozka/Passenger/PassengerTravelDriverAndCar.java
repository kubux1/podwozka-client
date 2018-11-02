package podwozka.podwozka.Passenger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.net.HttpURLConnection;

import podwozka.podwozka.PopUpWindows;
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
        boolean errorOccured = false;
        Intent i = getIntent();
        String driverLogin = i.getStringExtra("DRIVER_LOGIN");

        TextView driverName = findViewById(R.id.driverNameField);
        TextView driverCarBrand = findViewById(R.id.driverCarBrandField);
        TextView driverCarModel = findViewById(R.id.driverCarModelField);
        TextView driverCarColor = findViewById(R.id.driverCarColorField);

        try {
            httpResponseCode = httpCommand.getUserName(driverLogin);
            if (httpResponseCode == HttpURLConnection.HTTP_OK) {
                String name = new User().JSONToUser(httpCommand.getResponse());
                driverName.setText(name);
            } else {
                errorOccured = true;
                driverName.setText(getResources().getString(R.string.error));
            }

            httpResponseCode = httpCommand.getCarLimited(driverLogin);
            if (httpResponseCode == HttpURLConnection.HTTP_OK) {
                Car driverCar = new Car().JSONToCarRestricted(httpCommand.getResponse());
                driverCarBrand.setText(driverCar.getBrand());
                driverCarModel.setText(driverCar.getModel());
                driverCarColor.setText(driverCar.getColor());
            } else if (httpResponseCode == HttpURLConnection.HTTP_UNAVAILABLE){
                new PopUpWindows().showAlertWindow(PassengerTravelDriverAndCar.this, null, getResources().getString(R.string.server_down));
            }else {
                errorOccured = true;
                driverCarBrand.setText(getResources().getString(R.string.error));
                driverCarModel.setText(getResources().getString(R.string.error));
                driverCarColor.setText(getResources().getString(R.string.error));
            }
        } catch (Exception e)
        {
            new PopUpWindows().windowTimeout(3).showAlertWindow(PassengerTravelDriverAndCar.this, null, getResources().getString(R.string.unknown_error));
        }
        if(errorOccured == true){
            new PopUpWindows().windowTimeout(3).showAlertWindow(PassengerTravelDriverAndCar.this, null, getResources().getString(R.string.unknown_error));
        }
    }

    @Override
    public void onBackPressed() {
        Intent nextScreen = new Intent(PassengerTravelDriverAndCar.this, PassengerTravelsLog.class);
        startActivity(nextScreen);
        finish();
    }
}
