package com.example.windows.gymapp.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.windows.gymapp.R;
import com.example.windows.gymapp.data.FirebaseDb;
import com.example.windows.gymapp.data.StorageSP;
import com.example.windows.gymapp.model.Activity;
import com.example.windows.gymapp.model.Section;
import com.example.windows.gymapp.model.User;
import com.example.windows.gymapp.util.Constants;

public class NewPhacActivity extends AppCompatActivity {

    AppCompatImageView ic_back;
    EditText et_activity_name;
    EditText et_activity_url;
    EditText et_activity_comments;
    Button btn_add_activity;
    FirebaseDb db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_phac);

        Bundle extras = getIntent().getExtras();
        final Boolean fromExercise = extras.getBoolean(Constants.FROM_EXERCISE);
        final Section mySection = (Section) extras.getSerializable(Constants.SECTION);

        db = new FirebaseDb(this);
        et_activity_name = findViewById(R.id.et_activity_name);
        et_activity_url = findViewById(R.id.et_activity_url);
        et_activity_comments = findViewById(R.id.et_activity_comments);
        btn_add_activity = findViewById(R.id.btn_add_activity);
        ic_back = findViewById(R.id.ic_back);


        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btn_add_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String activityName = et_activity_name.getText().toString();
                String activityUrl = et_activity_url.getText().toString();
                String comments = et_activity_comments.getText().toString();

                if(activityName==null||activityName.isEmpty()){
                    et_activity_name.setError(getString(R.string.required));
                }else if(activityUrl==null||activityUrl.isEmpty()){
                    et_activity_url.setError(getString(R.string.required));
                }else if(comments==null||comments.isEmpty()){
                    et_activity_comments.setError(getString(R.string.required));
                }else if(!activityUrl.contains(Constants.YOUTUBE_URL)&& !activityUrl.contains(Constants.YOUTUBE_URL2)) {
                    et_activity_url.setError(getString(R.string.must_youtube));
                } else if (activityUrl.contains(Constants.YOUTUBE_PLAYLIST)) {
                    et_activity_url.setError(getString(R.string.not_playlist));
                }else{
                    String realUrl="";
                    if(activityUrl.contains("=")){
                        realUrl = activityUrl.split("=")[1];
                    }else if(activityUrl.contains(".be/")){
                        realUrl = activityUrl.split("/")[3];
                    }
                    User user = new StorageSP(getApplicationContext()).getUser();
                    Activity activity = new Activity(activityName,realUrl,comments,user.getFullName());

                    activity.addRating("init",0);
                    activity.setFromExercises(fromExercise);
                    activity.setId_section(mySection.getId());
                    db.postActivityInSection(activity, false);

                    onBackPressed();
                }

            }
        });



    }
}
