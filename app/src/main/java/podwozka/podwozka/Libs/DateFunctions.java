package podwozka.podwozka.Libs;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateFunctions {

    public String changeDateFormat(String strDate){
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date date = dateFormat.parse(strDate);

            dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            return dateFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return strDate;
    }

    public String serverDateTimeToDate (String dateTime){
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date date = dateFormat.parse(dateTime);
            Calendar cal = Calendar.getInstance();

            cal.setTime(date);
            int year = cal.get(Calendar.YEAR);
            int month = (cal.get(Calendar.MONTH) + 1);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            String dateInString = Integer.toString(year) + "-" + Integer.toString(month) + "-" + Integer.toString(day);
            return dateInString;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTime;
    }

    public String serverDateTimeToTime (String dateTime){
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date date = dateFormat.parse(dateTime);
            Calendar cal = Calendar.getInstance();

            cal.setTime(date);
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int minutes = cal.get(Calendar.MINUTE);
            String timeInString = String.format("%02d:%02d", hour, minutes);

            return timeInString;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTime;
    }
}
