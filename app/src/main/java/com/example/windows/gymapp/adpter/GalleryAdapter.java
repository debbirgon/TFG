package com.example.windows.gymapp.adpter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.windows.gymapp.R;
import com.example.windows.gymapp.model.Image;
import com.example.windows.gymapp.ui.ViewImageActivity;
import com.example.windows.gymapp.util.Constants;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by windows on 28/08/2019.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryHolder>{

    private List<Image> imageList;
    private Context mContext;

    public GalleryAdapter(List<Image> imageList, Context mContext) {
        this.imageList = imageList;
        this.mContext = mContext;
    }

    @Override
    public GalleryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_gallery,parent,false);
        GalleryHolder galleryHolder = new GalleryHolder(view);
        return galleryHolder;
    }

    @Override
    public void onBindViewHolder(GalleryHolder holder, int position) {
        final Image currentImage = imageList.get(position);

        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyy");
        Date date = new Date(Long.valueOf(currentImage.getName()));
        holder.tv_date.setText(formatter.format(date));
        Picasso.get().load(currentImage.getUrl()).into(holder.iv_photo);

        holder.iv_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ViewImageActivity.class);
                intent.putExtra(Constants.IMAGE_URL,currentImage.getUrl());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class GalleryHolder extends RecyclerView.ViewHolder{

        TextView tv_date;
        AppCompatImageView iv_photo;

        public GalleryHolder(View itemView) {
            super(itemView);

            tv_date = itemView.findViewById(R.id.tv_date);
            iv_photo = itemView.findViewById(R.id.iv_image);
        }
    }
}
