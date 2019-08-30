package com.example.windows.gymapp.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.windows.gymapp.R;
import com.example.windows.gymapp.adpter.ActivityAdapter;
import com.example.windows.gymapp.data.FirebaseDb;
import com.example.windows.gymapp.data.StorageSP;
import com.example.windows.gymapp.model.Activity;
import com.example.windows.gymapp.util.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class FavouriteFragment extends Fragment {

    private List<Activity> activityList = new ArrayList<>();
    private RecyclerView rv_favourite;
    private ActivityAdapter activityAdapter;
    private View rootView;
    private EditText et_search_favourite;
    private DatabaseReference favouriteRef;
    private DatabaseReference activityRef;
    private List<String> favouriteList = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_favourite,container,false);
        rv_favourite = rootView.findViewById(R.id.rv_favourite);
        et_search_favourite = rootView.findViewById(R.id.et_search_favourite);
        FirebaseDatabase myDb = FirebaseDatabase.getInstance();
        favouriteRef = myDb.getReference(Constants.USERS)
                .child(new FirebaseDb(getContext()).getUserId()).child(Constants.FAVOURITES);
        activityRef = myDb.getReference(Constants.ACTIVITIES);


        updateListFavourites();
        buildRecyclerView();

        et_search_favourite.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });

        return rootView;
    }



    private void filter(String text) {
        List<Activity> filteredList = new ArrayList<>();

        for(Activity activity: activityList){
            if(activity.getName().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(activity);
                activityAdapter.filterList(filteredList);
            }
        }
    }

    private void buildRecyclerView() {

        rv_favourite.setHasFixedSize(true);
        rv_favourite.setLayoutManager(new LinearLayoutManager(getContext()));
        activityAdapter = new ActivityAdapter(activityList,getContext());
        rv_favourite.setAdapter(activityAdapter);
    }

    private void updateListFavourites() {
        favouriteRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                activityList.clear();
                favouriteList = (List<String>) dataSnapshot.getValue();
                if(favouriteList!=null){
                    for(String id: favouriteList){
                        filterActivities(id);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void filterActivities(String id) {
        activityRef.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                activityList.add(dataSnapshot.getValue(Activity.class));
                activityAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
