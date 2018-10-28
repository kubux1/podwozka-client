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
        final NumberPicker np = findViewById(R.id.maxPassangersCapacity);

        ArrayList<EditText> EditTextList = new ScreenActivityLib().getAllEditTextInActivity(myLinearLayout);
        Map<String, String> carFields = new ScreenActivityLib().EditTextValuesToMap(EditTextList, false);
        // Add numberPicker field
        carFields.put(np.getTag().toString(),Integer.toString(np.getValue()));
        // Check if any field was empty
        if(carFields.get("EmptyFieldName") != null){
            new PopUpWindows().showAlertWindow(DriverAddCar.this, null,
                    getResources().getString(R.string.field_empty) + " " + carFields.get("EmptyFieldName"));
        }
        else if(validateProductionYear(carFields.get("productionYear")) == true &
                validateBrandAndColorField(carFields.get("brand"), carFields.get("color")) == true){
            Car carToSave = new Car(carFields);
            String carInJson = carToSave.newCarToJSON(carToSave);
            httpResponseCode = httpCommand.addNewCar(carInJson);
            if (httpResponseCode == HttpURLConnection.HTTP_CREATED){
                Intent nextScreen = new Intent(DriverAddCar.this, DriverMain.class);
                nextScreen.putExtra("MESSAGE",  getResources().getString(R.string.car_added));
                startActivity(nextScreen);
            } else if (httpResponseCode == HttpURLConnection.HTTP_FORBIDDEN | httpResponseCode == HttpURLConnection.HTTP_BAD_REQUEST) {
                new PopUpWindows().showAlertWindow(DriverAddCar.this, null, getResources().getString(R.string.car_exists));
            } else {
                new PopUpWindows().showAlertWindow(DriverAddCar.this, null, getResources().getString(R.string.unknown_error));
            }
        }
    }

    public boolean validateProductionYear(String productionYear){
        boolean isValidationSuccessful = false;

        int prodYear;
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        // Check if productionYear is integer only
        try{
            prodYear = Integer.parseInt(productionYear);
        }catch (Exception e){
            new PopUpWindows().showAlertWindow(DriverAddCar.this, null, getResources().getString(R.string.production_year_not_integer));
            return isValidationSuccessful;
        }
        if((currentYear - prodYear) > new Car().getMaxCarAge()){
            new PopUpWindows().showAlertWindow(DriverAddCar.this, null, getResources().getString(R.string.car_too_old));
        }
        else if (prodYear > currentYear){
            new PopUpWindows().showAlertWindow(DriverAddCar.this, null, getResources().getString(R.string.car_from_future));
        }

        return isValidationSuccessful;
    }

    public boolean validateBrandAndColorField(String brand, String color){
        boolean isValidationSuccessful = false;

        // Check if brand name has only letters
        if(!brand.matches("[a-zA-Z]+")){
            new PopUpWindows().showAlertWindow(DriverAddCar.this, null, getResources().getString(R.string.brand_not_only_letters));
        }
        // Check if color name has only letters
        else if(!color.matches("[a-zA-Z]+")){
            new PopUpWindows().showAlertWindow(DriverAddCar.this, null, getResources().getString(R.string.color_not_only_letters));
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
