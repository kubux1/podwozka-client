package podwozka.podwozka.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class TravelUser implements Parcelable {
    private Long id;

    private Long travelId;

    private String userLogin;

    private boolean userAccepted;

    public TravelUser (Long id,Long travelId, String userLogin, boolean userAccepted) {
        this.id = id;
        this.travelId = travelId;
        this.userLogin = userLogin;
        this.userAccepted = userAccepted;
    }

    public TravelUser () {

    }

    protected TravelUser(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        if (in.readByte() == 0) {
            travelId = null;
        } else {
            travelId = in.readLong();
        }
        userLogin = in.readString();
        userAccepted = in.readByte() != 0;
    }

    public static final Creator<TravelUser> CREATOR = new Creator<TravelUser>() {
        @Override
        public TravelUser createFromParcel(Parcel in) {
            return new TravelUser(in);
        }

        @Override
        public TravelUser[] newArray(int size) {
            return new TravelUser[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        if (travelId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(travelId);
        }
        dest.writeString(userLogin);
        dest.writeByte((byte) (userAccepted ? 1 : 0));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTravelId() {
        return travelId;
    }

    public void setTravelId(Long travelId) {
        this.travelId = travelId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public boolean isUserAccepted() {
        return userAccepted;
    }

    public void setUserAccepted(boolean userAccepted) {
        this.userAccepted = userAccepted;
    }
}
