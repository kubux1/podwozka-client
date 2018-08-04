package podwozka.podwozka;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import podwozka.podwozka.entity.Travel;

import static podwozka.podwozka.R.layout.activity_browse_travels_list;

public class BrowseTravelsActivity extends AppCompatActivity {
 /*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_browse_travels_list);
        RecyclerView mRecyclerView;
        mRecyclerView = (RecyclerView) findViewById(R.id.browse_travels_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)

        Bundle bundle = getIntent().getExtras();


        List<Travel> travels = bundle.getParcelableArrayList("TRAVEL_LIST");

        RecyclerView.Adapter mAdapter = new MyAdapter(travels);
        mRecyclerView.setAdapter(mAdapter);
    }*/

}


