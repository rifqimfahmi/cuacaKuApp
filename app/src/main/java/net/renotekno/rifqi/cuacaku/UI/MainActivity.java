package net.renotekno.rifqi.cuacaku.UI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.renotekno.rifqi.cuacaku.Model.Current;
import net.renotekno.rifqi.cuacaku.Model.Day;
import net.renotekno.rifqi.cuacaku.Model.Forecast;
import net.renotekno.rifqi.cuacaku.Model.Hour;
import net.renotekno.rifqi.cuacaku.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String DAILY_FORECAST = "DAILY_FORECAST";
    public static final String HOURLY_FORECAST = "HOURLY_FORECAST";
    public static final String LOCATION_FORMAT = "LOCATION_FORMAT";
    private static final String PREF_FILE = "net.renotekno.cuacaku.sharedpreferences";
    private static final String SAVED_TEMPERATURE = "saved_temperature";
    private static final String SAVED_TIME = "saved_time";
    private static final String SAVED_HUMIDITY = "saved_humidity";
    private static final String SAVED_PRECIP = "saved_precip";
    private static final String SAVED_SUMMARY = "saved_summary";
    private static final String SAVED_IMAGE = "saved_image";
    private String apiKey;
    private String connection;
    private double latitude;
    private double longitude;
    private Forecast mForecast;
    private Current mCurrent;
    private TextView mTemperatureValue;
    private TextView mTimeValue;
    private TextView mHumidityValue;
    private TextView mPrecipValue;
    private TextView mSummaryLabel;
    private ImageView mIcon;
    private ImageView mRefreshButton;
    private ProgressBar mProgressBar;
    private Button mDayButton;
    private Button mHourButton;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    /* private TextView mAdress; */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSharedPreferences = getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        Long savedLongHumidity = mSharedPreferences.getLong(SAVED_HUMIDITY, 0);
        double savedHumidityValue = Double.longBitsToDouble(savedLongHumidity);


        mTemperatureValue = (TextView) findViewById(R.id.temperaturValue);
        mTimeValue = (TextView)findViewById(R.id.timeLabel);
        mHumidityValue = (TextView)findViewById(R.id.humidityValue);
        mPrecipValue = (TextView)findViewById(R.id.precipValue);
        mSummaryLabel = (TextView) findViewById(R.id.summaryLabel);
        mIcon = (ImageView)findViewById(R.id.iconLabel);
        mRefreshButton = (ImageView)findViewById(R.id.refreshButton);
        mProgressBar = (ProgressBar)findViewById(R.id.progressBar);
        mDayButton = (Button) findViewById(R.id.dayButton);
        mHourButton = (Button) findViewById(R.id.hourButton);
        /* mAdress = (TextView) findViewById(R.id.addressValue); */


        // LATITUDE dan LONGITUDE
        apiKey = "9189d3986a8dc736ebbc9882036d49c2";
        latitude = -6.2087634;
        longitude = 106.845599;
        connection = "https://api.forecast.io/forecast/" + apiKey + "/" +
                latitude +"," + longitude;

        // Display saved data
        mHumidityValue.setText(savedHumidityValue +"");
        mTemperatureValue.setText(mSharedPreferences.getInt(SAVED_TEMPERATURE, 0) + "");
        mTimeValue.setText(mSharedPreferences.getString(SAVED_TIME, "Getting time..."));
        mPrecipValue.setText(mSharedPreferences.getInt(SAVED_PRECIP, 0) + "%");
        mSummaryLabel.setText(mSharedPreferences.getString(SAVED_SUMMARY, "Getting weather.."));
        mIcon.setImageResource(mSharedPreferences.getInt(SAVED_IMAGE, R.drawable.clear_day));

        // Update display for the first time0
        mProgressBar.setVisibility(View.INVISIBLE);
        updateData();

        mRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });

        mDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DailyActivity.class);
                Bundle extras = new Bundle();
                extras.putParcelableArray(DAILY_FORECAST, mForecast.getDays());
                /* extras.putString(LOCATION_FORMAT, Forecast.getLocationFormat()); */
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
        mHourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HourlyActivity.class);
                intent.addFlags(intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra(HOURLY_FORECAST, mForecast.getHours());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mForecast != null) {
            mCurrent = mForecast.getCurrent();
            mEditor.putInt(SAVED_TEMPERATURE, mCurrent.getTemperature());
            mEditor.putString(SAVED_TIME, mCurrent.getTimeString());
            mEditor.putLong(SAVED_HUMIDITY, Double.doubleToRawLongBits(mCurrent.getHumidity()));
            mEditor.putInt(SAVED_PRECIP, mCurrent.getPrecipChance());
            mEditor.putString(SAVED_SUMMARY, mCurrent.getSummary());
            mEditor.putInt(SAVED_IMAGE, mCurrent.getIconId());
            mEditor.apply();
        }
    }

    private Forecast setJsonData(String jsonData) throws JSONException {
        Forecast forecast = new Forecast();

        forecast.setCurrent(setWeatherDetail(jsonData));
        forecast.setDays(setDaysDetail(jsonData));
        forecast.setHours(setHoursDetail(jsonData));

        return forecast;
    }

    private Hour[] setHoursDetail(String jsonData) throws JSONException {
        JSONObject json = new JSONObject(jsonData);
        JSONObject hourly = json.getJSONObject("hourly");
        JSONArray data = hourly.getJSONArray("data");

        Hour[] mHours = new Hour[data.length()];

        for(int i = 0; i < data.length(); i++){
            Hour hour = new Hour();
            JSONObject singleHour = data.getJSONObject(i);

            hour.setIcon(singleHour.getString("icon"));
            hour.setTimeZone(json.getString("timezone"));
            hour.setTime(singleHour.getLong("time"));
            hour.setTemperature(singleHour.getDouble("temperature"));
            hour.setSummary(singleHour.getString("summary"));

            mHours[i] = hour;
        }

        return mHours;
    }

    private Day[] setDaysDetail(String jsonData) throws JSONException {
        JSONObject json = new JSONObject(jsonData);
        JSONObject daily = json.getJSONObject("daily");
        JSONArray data = daily.getJSONArray("data");

        Day[] mDays = new Day[data.length()];

        for(int i = 0; i < data.length(); i++){
            Day day = new Day();
            JSONObject singleDay = data.getJSONObject(i);

            day.setIcon(singleDay.getString("icon"));
            day.setTimeZone(json.getString("timezone"));
            day.setTime(singleDay.getLong("time"));
            day.setTemperatureMax(singleDay.getDouble("temperatureMax"));

            mDays[i] = day;
        }

        return mDays;
    }

    private void updateCurrentDisplay() {
        Current current = mForecast.getCurrent();
        mTemperatureValue.setText(current.getTemperature() + "");
        mHumidityValue.setText(current.getHumidity() +"");
        mPrecipValue.setText(current.getPrecipChance() + "%");
        mSummaryLabel.setText(current.getSummary());
        mTimeValue.setText(current.getTime());
        // mAdress.setText(Forecast.getLocationFormat());

        Drawable drawable = getResources().getDrawable(current.getIconId());
        mIcon.setImageDrawable(drawable);
    }



    private Current setWeatherDetail(String jsonData) throws JSONException {
        JSONObject data = new JSONObject(jsonData);
        JSONObject currently = data.getJSONObject("currently");
        mCurrent = new Current();

        mCurrent.setTimeZone(data.getString("timezone"));
        mCurrent.setTemperature(currently.getDouble("temperature"));
        mCurrent.setHumidity(currently.getDouble("humidity"));
        mCurrent.setPrecipChance(currently.getDouble("precipProbability"));
        mCurrent.setSummary(currently.getString("summary"));
        mCurrent.setTime(currently.getLong("time"));
        mCurrent.setIconId(currently.getString("icon"));

        return mCurrent;

    }


    private void updateData() {
        if(isNetworkAvailable()) {
            toggleRefreshButton();
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(connection).build();
            Call call = client.newCall(request);

            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    alertUserAboutError("There was something error !");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        try {
                            String jsonData = response.body().string();
                            mForecast = setJsonData(jsonData);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateCurrentDisplay();
                                    toggleRefreshButton();
                                }
                            });


                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e){
                            e.printStackTrace();
                        }
                    } else {
                        alertUserAboutError("There was somethin error !");
                    }
                }
            });
        } else {
            alertUserAboutError("Network Unavailable");
        }
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager manager =(ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isNetworkAvailable = false;
        if(networkInfo != null && networkInfo.isConnected()){
            isNetworkAvailable = true;
        }
        return isNetworkAvailable;
    }

    private void alertUserAboutError(String errorMessage) {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.setErrorMessage(errorMessage);
        dialog.show(getFragmentManager(), "error_tag");
    }


    private void toggleRefreshButton() {
        if(mRefreshButton.getVisibility() == View.VISIBLE){
            mRefreshButton.setVisibility(View.INVISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mRefreshButton.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }




    /*
    private void getLocationName() {
        Geocoder geoCoder = new Geocoder(MainActivity.this, Locale.getDefault());
        StringBuilder builder = new StringBuilder();
        try {
            List<Address> address = geoCoder.getFromLocation(latitude, longitude, 1);
            mForecast.mLocality = address.get(0).getLocality();
            mForecast.mCity = address.get(0).getSubAdminArea();


        } catch (IOException e) {
            e.printStackTrace();
            alertUserAboutError("Service is not availalbe. Please restart your device");
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
    */

 /*  private void getLocationAndUpdate() {
        toggleRefreshButton();
        final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.v("MAIN_ACTTIVITY", location.getLatitude() +" &  " + location.getLongitude());
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                connection = "https://api.forecast.io/forecast/" + apiKey + "/" +
                        latitude +"," + longitude;

                getLocationName();
               updateData();
                toggleRefreshButton();
               locationManager.removeUpdates(this);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
            }

        };
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        } else {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                    1);
        }
    } */
}


