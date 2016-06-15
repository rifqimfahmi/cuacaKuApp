package net.renotekno.rifqi.cuacaku.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Hour implements Parcelable{
    private long mTime;
    private double mTemperature;
    private String mTimeZone;
    private String mIcon;
    private String mSummary;

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public int getTemperature() {
        return (int) mTemperature;
    }

    public void setTemperature(double temperature) {
        int intFahrenheit = (int) Math.round(temperature);
        double doubleCelcius = (intFahrenheit -32)*5/9;
        int intCelcius = (int) Math.round(doubleCelcius);
        mTemperature = intCelcius;
    }

    public String getTimeZone() {
        return mTimeZone;
    }

    public void setTimeZone(String timeZone) {
        mTimeZone = timeZone;
    }

    public int getIconId() {
        return Forecast.getIconId(mIcon);
    }
    public String getHourly(){
        SimpleDateFormat formatter = new SimpleDateFormat("h a");
        formatter.setTimeZone(TimeZone.getTimeZone(getTimeZone()));
        Date date = new Date(mTime * 1000);
        return formatter.format(date);
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mTime);
        dest.writeDouble(mTemperature);
        dest.writeString(mTimeZone);
        dest.writeString(mIcon);
        dest.writeString(mSummary);
    }

    public Hour(){}
    public Hour(Parcel in){
        mTime = in.readLong();
        mTemperature = in.readDouble();
        mTimeZone = in.readString();
        mIcon = in.readString();
        mSummary = in.readString();
    }
    public static Creator<Hour> CREATOR = new Creator<Hour>() {
        @Override
        public Hour createFromParcel(Parcel source) {
            return new Hour(source);
        }

        @Override
        public Hour[] newArray(int size) {
            return new Hour[size];
        }
    };
}
