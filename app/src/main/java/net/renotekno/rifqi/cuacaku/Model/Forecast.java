package net.renotekno.rifqi.cuacaku.Model;

import net.renotekno.rifqi.cuacaku.R;

public class Forecast {
    Current mCurrent;
    Day[] mDays;
    Hour[] mHours;
    public static String mLocality;
    public static String mCity;

    public Current getCurrent() {
        return mCurrent;
    }

    public void setCurrent(Current current) {
        mCurrent = current;
    }

    public Day[] getDays() {
        return mDays;
    }

    public void setDays(Day[] days) {
        mDays = days;
    }

    public Hour[] getHours() {
        return mHours;
    }

    public void setHours(Hour[] hours) {
        mHours = hours;
    }

    public static String getLocationFormat(){
        String locationFormat = mLocality + ", " + mCity;
        return locationFormat;
    }

    public static int getIconId(String iconNames){

        int mIconId = R.drawable.clear_day;

        if (iconNames.equals("clear-day")) {
            mIconId = R.drawable.clear_day;
        }
        else if (iconNames.equals("clear-night")) {
            mIconId = R.drawable.clear_night;
        }
        else if (iconNames.equals("rain")) {
            mIconId = R.drawable.rain;
        }
        else if (iconNames.equals("snow")) {
            mIconId = R.drawable.snow;
        }
        else if (iconNames.equals("sleet")) {
            mIconId = R.drawable.sleet;
        }
        else if (iconNames.equals("wind")) {
            mIconId = R.drawable.wind;
        }
        else if (iconNames.equals("fog")) {
            mIconId = R.drawable.fog;
        }
        else if (iconNames.equals("cloudy")) {
            mIconId = R.drawable.cloudy;
        }
        else if (iconNames.equals("partly-cloudy-day")) {
            mIconId = R.drawable.partly_cloudy;
        }
        else if (iconNames.equals("partly-cloudy-night")) {
            mIconId = R.drawable.cloudy_night;
        }

        return mIconId;
    }
}
