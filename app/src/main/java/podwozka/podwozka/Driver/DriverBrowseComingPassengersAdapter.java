package podwozka.podwozka.Driver;

import android.content.Context;
import android.support.v7.widget.RecyclerView;;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

import podwozka.podwozka.R;
import podwozka.podwozka.Rest.APIClient;
import podwozka.podwozka.Rest.TravelService;
import podwozka.podwozka.entity.TravelUser;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static podwozka.podwozka.LoginActivity.user;

public class DriverBrowseComingPassengersAdapter extends
        RecyclerView.Adapter<DriverBrowseComingPassengersAdapter.MyViewHolder> {

    private static final String TAG = DriverBrowseComingPassengersAdapter.class.getName();
    private List<TravelUser> travelsList;
    public Context context;

    private TravelService travelService = APIClient.getTravelService();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public Switch acceptTravelSwitch;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            acceptTravelSwitch = view.findViewById(R.id.acceptTravelSwitch);
            acceptTravelSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
                @Override
                public void onCheckedChanged(CompoundButton cb, boolean on){
                    TravelUser currentTravel = null;
                    for (TravelUser travel : travelsList) {
                        if (travel.getUserLogin().equals(name.getText().toString()))
                            currentTravel = travel;
                    }
                    if (currentTravel != null)
                    {
                        if(on)
                        {
                            currentTravel.setUserAccepted(true);
                            Call<TravelUser> call = travelService.acceptTravel(
                                    currentTravel, user.getBearerToken());
                            call.enqueue(getFetchCallback());
                        }
                        else
                        {
                            currentTravel.setUserAccepted(false);
                            Call<TravelUser> call = travelService.acceptTravel(
                                    currentTravel, user.getBearerToken());
                            call.enqueue(getFetchCallback());
                        }
                    }
                }
            });
        }
    }

    public DriverBrowseComingPassengersAdapter(List<TravelUser> travelsList,
                                               Context context) {
        this.travelsList = travelsList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_browse_coming_travels_signed_up_person_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TravelUser travel = travelsList.get(position);
        String name = travel.getUserLogin();
        holder.name.setText(name);
        holder.acceptTravelSwitch.setChecked(travel.isUserAccepted());
    }

    @Override
    public int getItemCount() {
        return travelsList.size();
    }

    public void update(List<TravelUser> travelsList) {
        this.travelsList = travelsList;
        notifyDataSetChanged();
    }

    private Callback<TravelUser> getFetchCallback() {
        return new Callback<TravelUser>() {
            @Override
            public void onResponse(Call<TravelUser> call, Response<TravelUser> response) {
                Log.i(TAG, response.message());
                if(response.isSuccessful()) {

                }
            }

            @Override
            public void onFailure(Call<TravelUser> call, Throwable t) {
                Log.e(TAG, t.getMessage(), t);
            }
        };
    }
}
