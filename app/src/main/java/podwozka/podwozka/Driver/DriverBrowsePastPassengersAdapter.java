package podwozka.podwozka.Driver;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import podwozka.podwozka.R;
import podwozka.podwozka.entity.TravelUser;

;

public class DriverBrowsePastPassengersAdapter extends
        RecyclerView.Adapter<DriverBrowsePastPassengersAdapter.MyViewHolder> {

    private List<TravelUser> travelsList;
    public Context context;
    public RatingBar ratingBar;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            ratingBar = view.findViewById(R.id.ratingBar);
            ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating,
                                            boolean fromUser) {
                    Toast.makeText(context,
                            String.valueOf(ratingBar.getRating()), Toast.LENGTH_LONG).show();

                    //TODO: Passenger rating should be sent to service and saved in database
                }
            });
        }
    }


    public DriverBrowsePastPassengersAdapter(List<TravelUser> travelsList,
                                             Context context) {
        this.travelsList = travelsList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_browse_past_travels_signed_up_person_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TravelUser travel = travelsList.get(position);
        String name = travel.getUserLogin();
        holder.name.setText(name);
    }

    @Override
    public int getItemCount() {
        return travelsList.size();
    }

    public void update(List<TravelUser> travelsList) {
        this.travelsList = travelsList;
        notifyDataSetChanged();
    }
}
