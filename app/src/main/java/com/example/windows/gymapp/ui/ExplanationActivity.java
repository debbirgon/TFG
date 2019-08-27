package com.example.windows.gymapp.ui;

import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.windows.gymapp.R;
import com.example.windows.gymapp.data.FirebaseDb;
import com.example.windows.gymapp.model.Activity;
import com.example.windows.gymapp.util.Constants;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ExplanationActivity extends YouTubeBaseActivity {

    private Button btn_rate;
    private YouTubePlayerView yv_video;
    private CheckBox cb_isFavourite;
    private AppCompatImageView ic_back;
    private TextView tv_explanation;
    private TextView tv_activity_name;
    private RatingBar rb_rating;
    private List<String> favouriteList;
    private Activity activity;
    private String user_id;
    private FirebaseDatabase myDb;
    private FirebaseDb firebaseDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explanation);

        myDb = FirebaseDatabase.getInstance();
        firebaseDb = new FirebaseDb(this);
        activity = (Activity) getIntent().getExtras().getSerializable(Constants.ACTIVITIES);

        initializeView();
        analyzeIfFavourite();
        tv_activity_name.setText(activity.getName());
        tv_explanation.setText(activity.getExplanation());
        rb_rating.setRating(activity.returnRatingById(user_id).floatValue());
        initializeVideo(activity.getUrl());

        btn_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer rating = Double.valueOf(rb_rating.getRating()).intValue();
                Toast.makeText(ExplanationActivity.this, "Rating: "+rating, Toast.LENGTH_SHORT).show();
                activity.addRating(user_id,rating);
                firebaseDb.postActivityInSection(activity,true);

            }
        });

        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        cb_isFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked = cb_isFavourite.isChecked();
                if(isChecked){
                    favouriteList.add(activity.getId());
                    firebaseDb.updateFavourites(favouriteList);

                }else{
                    favouriteList.remove(activity.getId());
                    firebaseDb.updateFavourites(favouriteList);

                }

            }
        });
        cb_isFavourite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

            }
        });
    }

    private void initializeView() {
        btn_rate = findViewById(R.id.btn_rate);
        yv_video = findViewById(R.id.yv_video);
        cb_isFavourite = findViewById(R.id.cb_isFavourite);
        ic_back = findViewById(R.id.ic_back);
        tv_explanation = findViewById(R.id.tv_explanation);
        tv_activity_name = findViewById(R.id.tv_activity_name);
        rb_rating = findViewById(R.id.rb_rating);
    }

    private void analyzeIfFavourite() {
        user_id = firebaseDb.getUserId();

        DatabaseReference favouritesRef = myDb.getReference(Constants.USERS)
                .child(user_id).child(Constants.FAVOURITES);

        favouritesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                favouriteList = (List<String>) dataSnapshot.getValue();
                if(favouriteList!=null){

                    if(favouriteList.contains(activity.getId())){
                        cb_isFavourite.setChecked(true);
                    }else{
                        cb_isFavourite.setChecked(false);
                    }
                }else{
                    favouriteList = new ArrayList<>();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void initializeVideo(final String id_video) {
        yv_video.initialize(Constants.YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.cueVideo(id_video);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });

    }
}
