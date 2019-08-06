package com.example.windows.gymapp.adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.windows.gymapp.R;

import java.util.List;

/**
 * Created by windows on 05/08/2019.
 */

public class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.SectionHolder> {

    private List<String> sectionList;
    private Context mContext;

    public SectionAdapter(List<String> sectionList, Context mContext){
        this.sectionList = sectionList;
        this.mContext = mContext;
    }

    @Override
    public SectionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_section,parent,false);
        SectionHolder sectionHolder = new SectionHolder(view);
        return sectionHolder;
    }

    @Override
    public void onBindViewHolder(SectionHolder holder, final int position) {

        if(position%2 == 0){
            holder.ll_section.setBackgroundColor(mContext.getResources().getColor(R.color.colorRow1));
        }else{
            holder.ll_section.setBackgroundColor(mContext.getResources().getColor(R.color.colorRow2));
        }
        holder.tv_section_name.setText(sectionList.get(position));
        holder.ll_section.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext,"Click en posición: "+position,Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return sectionList.size();
    }

    public void filterList(List<String> filteredList) {
        if(filteredList.size()!=0){
            sectionList = filteredList;
            notifyDataSetChanged();
        }
    }

    public class SectionHolder extends RecyclerView.ViewHolder{

        LinearLayout ll_section;
        TextView tv_section_name;

        public SectionHolder(View itemView) {
            super(itemView);

            ll_section = itemView.findViewById(R.id.ll_section);
            tv_section_name = itemView.findViewById(R.id.tv_section_name);

        }
    }

}
