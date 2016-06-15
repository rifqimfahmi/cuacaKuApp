package net.renotekno.rifqi.cuacaku.UI;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import net.renotekno.rifqi.cuacaku.Adapter.HourAdapter;
import net.renotekno.rifqi.cuacaku.Model.Hour;
import net.renotekno.rifqi.cuacaku.R;

import java.util.Arrays;

public class HourlyActivity extends AppCompatActivity {

    private Hour[] mHours;
    private RecyclerView mRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hourly);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        Intent intent = getIntent();
        Parcelable[] parcelables = intent.getParcelableArrayExtra(MainActivity.HOURLY_FORECAST);
        mHours = Arrays.copyOf(parcelables, parcelables.length, Hour[].class);
        HourAdapter hourAdapter = new HourAdapter(this, mHours);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);



        mRecyclerView.setAdapter(hourAdapter);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setHasFixedSize(true);

    }
}
