package podwozka.podwozka.Passenger;

import podwozka.podwozka.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class PassangerMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passanger_main);

        Button newTravelButton = findViewById(R.id.newTravelButton);
        newTravelButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                //Starting a new Intent
                Intent nextScreen = new Intent(getApplicationContext(), PassengerNewTravelActivity.class);
                startActivity(nextScreen);
            }
        });
    }
}
