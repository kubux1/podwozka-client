package podwozka.podwozka.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Travel implements Parcelable {
    private String name;
    private String start;
    private String end;

    public Travel(String name, String start, String end) {
        this.name = name;
        this.start = start;
        this.end = end;
    }

    public Travel()  {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getName() {
        return name;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public Travel(Parcel in){
        String[] data = new String[3];

        in.readStringArray(data);

        this.name = data[0];
        this.start = data[1];
        this.end = data[2];
    }

    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {this.name,
                this.start,
                this.end});
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Travel createFromParcel(Parcel in) {
            return new Travel(in);
        }

        public Travel[] newArray(int size) {
            return new Travel[size];
        }
    };
}

