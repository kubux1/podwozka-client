package podwozka.podwozka.Driver;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import podwozka.podwozka.R;
import podwozka.podwozka.entity.TravelDTO;


public class DriverBrowseTravelsAdapter extends
        RecyclerView.Adapter<DriverBrowseTravelsAdapter.MyViewHolder> {

    private List<TravelDTO> travelsList;
    public Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, date, freeSpace;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.name);
            date = view.findViewById(R.id.startTime);
            freeSpace = view.findViewById(R.id.freeSpace);
        }
    }

    public DriverBrowseTravelsAdapter(List<TravelDTO> travelsList, Context context) {
        this.travelsList = travelsList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_browse_travels_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TravelDTO travel = travelsList.get(position);
        String title = travel.getStartPlace().getName() + " - " + travel.getEndPlace().getName();
        holder.title.setText(title);
        String startDatetime = this.context.getResources().getString(R.string.start_time)+ ": " + travel.getPickUpDatetime();
        holder.date.setText(startDatetime);
        Long freeSpace = travel.getPassengersCount();

        String freeSpaceMessage = this.context.getResources().getString(R.string.free_seats) + ": " + freeSpace;
        holder.freeSpace.setText(freeSpaceMessage);
    }

    @Override
    public int getItemCount() {
        return travelsList.size();
    }

    public void update(List<TravelDTO> travelsList) {
        this.travelsList = travelsList;
        notifyDataSetChanged();
    }

    public TravelDTO returnTravel (int position){
        return travelsList.get(position);
    }
}
