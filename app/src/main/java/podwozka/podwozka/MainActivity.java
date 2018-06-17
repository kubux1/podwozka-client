package podwozka.podwozka;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnNextScreen = (Button) findViewById(R.id.btnNewTravel);
        btnNextScreen.setText("Zaplanuj podróż");

        btnNextScreen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                //Starting a new Intent
                Intent nextScreen = new Intent(getApplicationContext(), NewTravelActivity.class);

                startActivity(nextScreen);

            }
        });
    }

    protected void testMessage(View view) {
        System.out.print("abc");
    }
}
