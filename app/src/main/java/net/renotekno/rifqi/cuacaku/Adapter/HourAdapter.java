package net.renotekno.rifqi.cuacaku.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.renotekno.rifqi.cuacaku.Model.Hour;
import net.renotekno.rifqi.cuacaku.R;

public class HourAdapter extends RecyclerView.Adapter<HourAdapter.HourViewHolder> {
    private Hour[] mHours;
    private Context mContext;

    private int lastPosition = -1;
    public HourAdapter(Context context ,Hour[] hours){
        mHours = hours;
        mContext = context;
    }
    @Override
    public HourViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_hour_list, null);
        HourViewHolder hourViewHolder = new HourViewHolder(view);
        return hourViewHolder;
    }

    @Override
    public void onBindViewHolder(HourViewHolder holder, int position) {
        setAnimate(holder.mRelativeLayout, position);
        holder.bindHour(mHours[position]);
    }

    private void setAnimate(View viewToAnimate, int position){
        if(position > lastPosition){
            Animation animate = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
            viewToAnimate.setAnimation(animate);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return mHours.length;
    }

    public class HourViewHolder extends RecyclerView.ViewHolder {
        private TextView mTimeValue;
        private TextView mTemperatureValue;
        private TextView mSummaryValue;
        private ImageView mIconValue;
        private RelativeLayout mRelativeLayout;

        public HourViewHolder(View itemView) {
            super(itemView);
            mTimeValue = (TextView) itemView.findViewById(R.id.timeValue);
            mTemperatureValue = (TextView) itemView.findViewById(R.id.temperatureValue);
            mSummaryValue = (TextView) itemView.findViewById(R.id.summaryValue);
            mIconValue = (ImageView) itemView.findViewById(R.id.iconValue);
            mRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.container);
        }

        public void bindHour(Hour hour){
            mTimeValue.setText(hour.getHourly());
            mTemperatureValue.setText(hour.getTemperature() + "");
            mSummaryValue.setText(hour.getSummary());
            mIconValue.setImageResource(hour.getIconId());
        }
    }
}
