package podwozka.podwozka.Driver;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import java.net.HttpURLConnection;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import podwozka.podwozka.Libs.AppStatus;
import podwozka.podwozka.PopUpWindows;
import podwozka.podwozka.R;
import podwozka.podwozka.entity.Car;
import podwozka.podwozka.entity.HttpCommands;
import podwozka.podwozka.Libs.ScreenActivityLib;

public class DriverAddCar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_info);
    }

    public void saveCarInfo(View v){
        LinearLayout myLinearLayout = findViewById( R.id.carInfoLayout );
        int httpResponseCode;
        HttpCommands httpCommand = new HttpCommands();
        if (AppStatus.getInstance(this).isOnline()) {
            ArrayList<EditText> EditTextList = new ScreenActivityLib().getAllEditTextInActivity(myLinearLayout);
            Map<String, String> carFields = new ScreenActivityLib().EditTextValuesToMap(EditTextList, false);
            // Check if any field was empty
            if (carFields.get("EmptyFieldName") != null) {
                new PopUpWindows().showAlertWindow(DriverAddCar.this, null,
                        getResources().getString(R.string.field_empty) + " " + carFields.get("EmptyFieldName"));
            } else if (validateProductionYear(carFields.get("productionYear")) == true &
                    validateOtherFields(carFields) == true) {
                Car carToSave = new Car(carFields);
                String carInJson = carToSave.newCarToJSON(carToSave);
                httpResponseCode = httpCommand.addNewCar(carInJson);
                if (httpResponseCode == HttpURLConnection.HTTP_CREATED) {
                    Intent nextScreen = new Intent(DriverAddCar.this, DriverMain.class);
                    nextScreen.putExtra("MESSAGE", getResources().getString(R.string.car_added));
                    startActivity(nextScreen);
                } else if (httpResponseCode == HttpURLConnection.HTTP_FORBIDDEN | httpResponseCode == HttpURLConnection.HTTP_BAD_REQUEST) {
                    new PopUpWindows().showAlertWindow(DriverAddCar.this, null, getResources().getString(R.string.car_exists));
                } else if (httpResponseCode == HttpURLConnection.HTTP_UNAVAILABLE) {
                    new PopUpWindows().showAlertWindow(DriverAddCar.this, null, getResources().getString(R.string.server_down));
                } else {
                    new PopUpWindows().showAlertWindow(DriverAddCar.this, null, getResources().getString(R.string.unknown_error));
                }
            }
        } else {
            new PopUpWindows().showAlertWindow(DriverAddCar.this, null, getResources().getString(R.string.no_internet_connection));
        }
    }

    public boolean validateProductionYear(String productionYear){
        boolean isValidationSuccessful;

        int prodYear;
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        // Check if productionYear is integer only
        try{
            prodYear = Integer.parseInt(productionYear);
        }catch (Exception e){
            new PopUpWindows().showAlertWindow(DriverAddCar.this, null, getResources().getString(R.string.production_year_not_integer));
            isValidationSuccessful = false;
            return isValidationSuccessful;
        }
        if((currentYear - prodYear) > new Car().getMaxCarAge()){
            new PopUpWindows().showAlertWindow(DriverAddCar.this, null, getResources().getString(R.string.car_too_old));
            isValidationSuccessful = false;
        }
        else if (prodYear > currentYear){
            new PopUpWindows().showAlertWindow(DriverAddCar.this, null, getResources().getString(R.string.car_from_future));
            isValidationSuccessful = false;
        }
        else {
            isValidationSuccessful = true;
        }

        return isValidationSuccessful;
    }

    public boolean validateOtherFields(Map<String, String> carFields){
        boolean isValidationSuccessful;

        // Check if brand name has only letters
        if(!carFields.get("brand").matches("[a-zA-Z]+")){
            new PopUpWindows().showAlertWindow(DriverAddCar.this, null, getResources().getString(R.string.brand_not_only_letters));
            isValidationSuccessful = false;
        }
        // Check if color name has only letters
        else if(!carFields.get("color").matches("[a-zA-Z]+")){
            new PopUpWindows().showAlertWindow(DriverAddCar.this, null, getResources().getString(R.string.color_not_only_letters));
            isValidationSuccessful = false;
        }
        else if(!carFields.get("maxPassengersCapacity").matches("[0-9]+")){
            new PopUpWindows().showAlertWindow(DriverAddCar.this, null, getResources().getString(R.string.max_passengers_only_numbers));
            isValidationSuccessful = false;
        }
        else {
            isValidationSuccessful = true;
        }

        return isValidationSuccessful;
    }

    @Override
    public void onBackPressed() {
        Intent nextScreen = new Intent(DriverAddCar.this, DriverMain.class);
        startActivity(nextScreen);
        finish();
    }
}
