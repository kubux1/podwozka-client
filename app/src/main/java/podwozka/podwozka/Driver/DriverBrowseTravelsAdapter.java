package podwozka.podwozka.Driver;

import android.content.Context;
import android.support.v7.widget.RecyclerView;;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Iterator;
import java.util.List;

import podwozka.podwozka.Driver.entity.DriverTravel;
import podwozka.podwozka.R;


public class DriverBrowseTravelsAdapter extends RecyclerView.Adapter<DriverBrowseTravelsAdapter.MyViewHolder> {

    private List<DriverTravel> travelsList;
    public Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, date, freeSpace;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.name);
            date = (TextView) view.findViewById(R.id.startTime);
            freeSpace = (TextView) view.findViewById(R.id.freeSpace);
        }
    }


    public DriverBrowseTravelsAdapter(List<DriverTravel> travelsList, Context context) {
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
        DriverTravel travel = travelsList.get(position);
        String title = travel.getStartPlace() + " - " + travel.getEndPlace();
        holder.title.setText(title);
        String startDatetime = this.context.getResources().getString(R.string.start_place)+ ": " + travel.getStartDatetime();
        holder.date.setText(startDatetime);
        int freeSpace = 0;
        try{
            freeSpace = Integer.parseInt(travel.getMaxPassengers()) -
                    Integer.parseInt(travel.getPassengersCount());
        } catch (NumberFormatException e)
        {
            e.printStackTrace();
        }



        String freeSpaceMessage = this.context.getResources().getString(R.string.free_seats) + ": " + freeSpace;
        holder.freeSpace.setText(freeSpaceMessage);
    }

    @Override
    public int getItemCount() {
        return travelsList.size();
    }

    public DriverTravel returnTravel (int position){
        return travelsList.get(position);
    }

    public void deleteTravel (int travelId){
        int x = 0;
        for (Iterator<DriverTravel> iter = travelsList.listIterator(); iter.hasNext(); ) {
            DriverTravel travel = iter.next();
            if (x == travelId) {
                iter.remove();
            }
            x++;
        }
    }

}
