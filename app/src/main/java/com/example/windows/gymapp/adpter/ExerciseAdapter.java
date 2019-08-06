package com.example.windows.gymapp.adpter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by windows on 06/08/2019.
 */

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseHolder> {


    @Override
    public ExerciseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ExerciseHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ExerciseHolder extends RecyclerView.ViewHolder {
        public ExerciseHolder(View itemView) {
            super(itemView);
        }
    }
}
