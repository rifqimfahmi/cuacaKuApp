package net.renotekno.rifqi.cuacaku.Model;

import net.renotekno.rifqi.cuacaku.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Current {
    private String mSummary;
    private String mIconId;
    private double mHumidity;
    private double mPrecipChance;
    private int mTemperature;
    private String mTimeZone;
    private String timeString;

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }

    public int getIconId() {
        return Forecast.getIconId(mIconId);
    }

    public void setIconId(String iconNames) {
        mIconId = iconNames;
    }

    public double getHumidity() {
        return mHumidity;
    }

    public void setHumidity(double humidity) {
        mHumidity = humidity;
    }

    public int getPrecipChance() {
        return (int) mPrecipChance;
    }

    public void setPrecipChance(double precipChance) {
        mPrecipChance = precipChance * 100;
    }

    public int getTemperature() {
        return mTemperature;
    }

    public void setTemperature(double temperature) {
        int intFahrenheit = (int) Math.round(temperature);
        double doubleCelcius = (intFahrenheit -32)*5/9;
        int intCelcius = (int) Math.round(doubleCelcius);
        this.mTemperature = intCelcius;
    }

    public String getTimeZone() {
        return mTimeZone;
    }

    public void setTimeZone(String timeZone) {
        mTimeZone = timeZone;
    }

    public String getTime() {
        return timeString;
    }

    public void setTime(long time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a");
        dateFormat.setTimeZone(TimeZone.getTimeZone(getTimeZone()));
        Date date = new Date(time * 1000);
        timeString = dateFormat.format(date);
    }

    public String getTimeString() {
        return timeString;
    }
}
