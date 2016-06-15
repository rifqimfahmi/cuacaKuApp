package net.renotekno.rifqi.cuacaku.Adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.renotekno.rifqi.cuacaku.Model.Day;
import net.renotekno.rifqi.cuacaku.R;

import org.w3c.dom.Text;

public class DayAdapter extends BaseAdapter {
    private Context mContext;
    private Day[] mDays;

    public DayAdapter(Context context, Day[] days){
        mContext = context;
        mDays = days;
    }
    @Override
    public int getCount() {
        return mDays.length;
    }

    @Override
    public Object getItem(int position) {
        return mDays[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.custom_day_list, parent, false);
            holder = new ViewHolder();
            holder.mDayLabel = (TextView) convertView.findViewById(R.id.dayLabel);
            holder.mIconImageView = (ImageView) convertView.findViewById(R.id.iconImageView);
            holder.mTemperatureLabel = (TextView) convertView.findViewById(R.id.temperatureValue);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Day day = mDays[position];
        if(position == 0){
            holder.mDayLabel.setText("Today");
        } else {
            holder.mDayLabel.setText(day.getDayOfTheWeek());
        }
        holder.mIconImageView.setImageResource(day.getIconId());
        holder.mTemperatureLabel.setText(day.getTemperatureMax() + "");

        return convertView;
    }

    private static class ViewHolder{
        ImageView mIconImageView;
        TextView mDayLabel;
        TextView mTemperatureLabel;
    }
}
