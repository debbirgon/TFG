package com.example.windows.gymapp.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.windows.gymapp.R;
import com.example.windows.gymapp.adpter.ActivityAdapter;
import com.example.windows.gymapp.data.StorageSP;
import com.example.windows.gymapp.model.Activity;
import com.example.windows.gymapp.model.Section;
import com.example.windows.gymapp.model.User;
import com.example.windows.gymapp.util.Constants;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class PhysicalActivityActivity extends AppCompatActivity {

    private List<Activity> activityList = new ArrayList<>();
    private RecyclerView rv_phac;
    private AppCompatImageView ic_back;
    private ActivityAdapter activityAdapter;
    private EditText et_search_activity;
    private AppCompatImageView add_activity;
    private TextView tv_section_name;
    private DatabaseReference activityRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_physical_activity);

        Bundle extras = getIntent().getExtras();
        final Boolean fromExercise = extras.getBoolean(Constants.FROM_EXERCISE);
        final Section mySection = (Section) extras.getSerializable(Constants.SECTION);
        StorageSP storage = new StorageSP(this);
        User user = storage.getUser();



        FirebaseDatabase myDb = FirebaseDatabase.getInstance();
        if(fromExercise){
            activityRef = myDb.getReference(Constants.EXERCISES).child(mySection.getId()).child(Constants.ACTIVITIES);
        }else{
            activityRef = myDb.getReference(Constants.TRAININGS).child(mySection.getId()).child(Constants.ACTIVITIES);
        }
        
        rv_phac = findViewById(R.id.rv_phac);
        et_search_activity = findViewById(R.id.et_search_activity);
        add_activity = findViewById(R.id.add_activity);
        tv_section_name = findViewById(R.id.tv_section_name);
        ic_back = findViewById(R.id.ic_back);


        updateList();
        buildRecyclerView();

        if(user.getIsExpert()){
            add_activity.setVisibility(View.VISIBLE);
        }

        tv_section_name.setText(mySection.getName());

        et_search_activity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });

        add_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),NewPhacActivity.class);
                intent.putExtra(Constants.FROM_EXERCISE,fromExercise);
                intent.putExtra(Constants.SECTION, mySection);
                startActivity(intent);
            }
        });

        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void filter(String text) {
        List<Activity> filteredList = new ArrayList<>();

        for(Activity activity: activityList){
            if(activity.getName().toLowerCase().contains(text.toLowerCase()) ||
                    activity.getName_creator().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(activity);
                activityAdapter.filterList(filteredList);
            }
        }
    }

    private void buildRecyclerView() {

        rv_phac.setHasFixedSize(true);
        rv_phac.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        activityAdapter = new ActivityAdapter(activityList,getApplicationContext());
        rv_phac.setAdapter(activityAdapter);
    }

    private void updateList() {
        activityRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                activityList.add(dataSnapshot.getValue(Activity.class));
                activityAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Activity changedActivity = dataSnapshot.getValue(Activity.class);
                for(int i=0;i<activityList.size();i++){
                    if(activityList.get(i).getId().equals(changedActivity.getId())){
                        activityList.set(i,changedActivity);
                    }
                }
                activityAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                String idDelete = dataSnapshot.getValue(Section.class).getId();
                for(int i =0; i<activityList.size(); i++){
                    if(activityList.get(i).getId().equals(idDelete)) {
                        activityList.remove(i);
                    }
                }
                activityAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
