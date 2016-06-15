package net.renotekno.rifqi.cuacaku.UI;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import net.renotekno.rifqi.cuacaku.Adapter.DayAdapter;
import net.renotekno.rifqi.cuacaku.Model.Day;
import net.renotekno.rifqi.cuacaku.R;

import java.util.Arrays;

public class DailyActivity extends ListActivity {

    private Day[] mDays;
    private TextView mAddressValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily);
        mAddressValue = (TextView) findViewById(R.id.addressValue);

        Bundle extras = getIntent().getExtras();
        Parcelable[] parcelable = extras.getParcelableArray(MainActivity.DAILY_FORECAST);
        // String locationFormat = extras.getString(MainActivity.LOCATION_FORMAT);
        // mAddressValue.setText(locationFormat);

        mDays = Arrays.copyOf(parcelable, parcelable.length, Day[].class);
        DayAdapter dayAdapter = new DayAdapter(this, mDays);
        setListAdapter(dayAdapter);


    }
}
