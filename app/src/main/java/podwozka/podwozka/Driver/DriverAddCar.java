package podwozka.podwozka.Driver;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Map;

import podwozka.podwozka.PopUpWindows;
import podwozka.podwozka.R;
import podwozka.podwozka.entity.Car;
import podwozka.podwozka.entity.HttpCommands;
import podwozka.podwozka.entity.MyLib;

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

        ArrayList<EditText> EditTextList = new MyLib().getAllEditTextInActivity(myLinearLayout);
        Map<String, String> carFields = new MyLib().EditTextValuesToMap(EditTextList, false);
        // Add numberPicker field
        carFields.put(np.getTag().toString(),Integer.toString(np.getValue()));
        // Check if any field was empty
        if(carFields.get("EmptyFieldName") != null){
            new PopUpWindows().showAlertWindow(DriverAddCar.this, null,
                    getResources().getString(R.string.field_empty) + " " + carFields.get("EmptyFieldName"));
        } else {
            Car carToSave = new Car(carFields);
            String carInJson = carToSave.newCarToJSON(carToSave);
            httpResponseCode = httpCommand.addNewCar(carInJson);
            if (httpResponseCode == HttpURLConnection.HTTP_CREATED){
                Intent nextScreen = new Intent(DriverAddCar.this, DriverMain.class);
                nextScreen.putExtra("MESSAGE",  getResources().getString(R.string.car_added));
                startActivity(nextScreen);
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent nextScreen = new Intent(DriverAddCar.this, DriverMain.class);
        startActivity(nextScreen);
        finish();
    }
}
