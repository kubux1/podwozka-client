package podwozka.podwozka.Passenger;


import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

import podwozka.podwozka.R;
import podwozka.podwozka.entity.TravelDTO;
import podwozka.podwozka.entity.TravelUser;

public class PassengerBrowseTravelsAdapter extends RecyclerView.Adapter<PassengerBrowseTravelsAdapter.MyViewHolder> {

    private List<TravelDTO> travelsList;
    private List<TravelUser> acceptance;
    public Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, date, freeSpace, travelId;
        public Switch acceptTravelSwitch;
        public RelativeLayout layout;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.name);
            date = view.findViewById(R.id.startTime);
            freeSpace = view.findViewById(R.id.freeSpace);
            acceptTravelSwitch = view.findViewById(R.id.acceptTravelSwitch);
            layout = view.findViewById(R.id.background);
        }
    }

    public PassengerBrowseTravelsAdapter(List<TravelDTO> travelsList, List<TravelUser> acceptance, Context context) {
        this.acceptance = acceptance;
        this.travelsList = travelsList;
        this.context = context;
    }

    @Override
    public PassengerBrowseTravelsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_browse_travels_in_passengers_item, parent, false);

        return new PassengerBrowseTravelsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PassengerBrowseTravelsAdapter.MyViewHolder holder, int position) {
        TravelDTO travel = travelsList.get(position);
        String title = travel.getStartPlace().getName() + " - " + travel.getEndPlace().getName();
        holder.title.setText(title);
        String startDatetime = this.context.getResources().getString(R.string.start_time)+ ": " + travel.getPickUpDatetime();
        holder.date.setText(startDatetime);
        Long freeSpace = travel.getPassengersCount();

        String freeSpaceMessage = this.context.getResources().getString(R.string.free_seats) + ": " + freeSpace;
        holder.freeSpace.setText(freeSpaceMessage);

        holder.layout.setBackgroundColor(Color.parseColor("#ffeaea"));
        holder.acceptTravelSwitch.setChecked(false);
        holder.acceptTravelSwitch.setClickable(false);

        for (TravelUser userTravel : acceptance) {
            if (userTravel.getTravelId().equals(travel.getId()))
            {
                holder.acceptTravelSwitch.setChecked(userTravel.isUserAccepted());
                if (userTravel.isUserAccepted())
                    holder.layout.getRootView().setBackgroundColor(Color.parseColor("#eaffea"));
            }
        }

    }

    @Override
    public int getItemCount() {
        return travelsList.size();
    }

    public void update(List<TravelDTO> travelsList) {
        this.travelsList = travelsList;
        notifyDataSetChanged();
    }

    public void updateAcceptance(List<TravelUser> acceptanceList) {
        this.acceptance = acceptanceList;
        notifyDataSetChanged();
    }

    public TravelDTO returnTravel (int position){
        return travelsList.get(position);
    }
}
