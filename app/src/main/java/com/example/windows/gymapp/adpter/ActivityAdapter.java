package com.example.windows.gymapp.adpter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.windows.gymapp.R;
import com.example.windows.gymapp.model.Activity;
import com.example.windows.gymapp.ui.ExplanationActivity;
import com.example.windows.gymapp.util.Constants;

import java.util.List;

/**
 * Created by windows on 06/08/2019.
 */

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ActivityHolder> {

    private List<Activity> activityList;
    private Context mContext;

    public ActivityAdapter(List<Activity> activityList, Context mContext) {
        this.activityList = activityList;
        this.mContext = mContext;
    }

    @Override
    public ActivityHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_activity,parent,false);
        ActivityHolder activityHolder = new ActivityHolder(view);
        return activityHolder;
    }

    @Override
    public void onBindViewHolder(ActivityHolder holder, int position) {

        if(position%2 == 0){
            holder.ll_activity.setBackgroundColor(mContext.getResources().getColor(R.color.colorRow1));
        }else{
            holder.ll_activity.setBackgroundColor(mContext.getResources().getColor(R.color.colorRow2));
        }

        final Activity activity = activityList.get(position);
        holder.tv_creator_name.setText(activity.getName_creator());
        holder.tv_activity_name.setText(activity.getName());
        Double mean = activity.returnMeanRating();
        String meanString;
        if(mean == 0.0){
            meanString = "-,-";
        }else{
            meanString = String.format("%.2f",mean);
        }
        holder.tv_activity_rating.setText(meanString);

        holder.ll_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext.getApplicationContext(),ExplanationActivity.class);
                intent.putExtra(Constants.ACTIVITIES,activity);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return activityList.size();
    }

    public void filterList(List<Activity> filteredList) {
        if(filteredList.size()!=0){
            activityList = filteredList;
            notifyDataSetChanged();
        }
    }

    public class ActivityHolder extends RecyclerView.ViewHolder {

        TextView tv_activity_name;
        TextView tv_creator_name;
        TextView tv_activity_rating;
        LinearLayout ll_activity;

        public ActivityHolder(View itemView) {
            super(itemView);

            tv_activity_name = itemView.findViewById(R.id.tv_activity_name);
            tv_creator_name = itemView.findViewById(R.id.tv_creator_name);
            tv_activity_rating = itemView.findViewById(R.id.tv_activity_rating);
            ll_activity = itemView.findViewById(R.id.ll_activity);
        }
    }
}
