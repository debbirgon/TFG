package com.example.windows.gymapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.windows.gymapp.R;
import com.example.windows.gymapp.adpter.SectionAdapter;
import com.example.windows.gymapp.data.StorageSP;
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


public class TrainingFragment extends Fragment {

    private List<Section> sectionList = new ArrayList<>();
    private RecyclerView rv_training;
    private SectionAdapter sectionAdapter;
    private View rootView;
    private EditText et_search_training;
    private DatabaseReference trainingRef;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_training,container,false);
        rv_training = rootView.findViewById(R.id.rv_training);
        et_search_training = rootView.findViewById(R.id.et_search_training);
        FirebaseDatabase myDb = FirebaseDatabase.getInstance();
        trainingRef = myDb.getReference(Constants.TRAININGS);

        updateList();
        buildRecyclerView();

        et_search_training.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });


        return rootView;
    }

    private void filter(String text) {
        List<Section> filteredList = new ArrayList<>();

        for(Section section: sectionList){
            if(section.getName().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(section);
                sectionAdapter.filterList(filteredList);
            }
        }
    }

    private void buildRecyclerView() {

        rv_training.setHasFixedSize(true);
        rv_training.setLayoutManager(new LinearLayoutManager(getContext()));
        sectionAdapter = new SectionAdapter(sectionList,getContext(), false);
        rv_training.setAdapter(sectionAdapter);
    }

    private void updateList() {
        trainingRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                sectionList.add(dataSnapshot.getValue(Section.class));
                sectionAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                String idDelete = dataSnapshot.getValue(Section.class).getId();
                for(int i =0; i<sectionList.size(); i++){
                    if(sectionList.get(i).getId().equals(idDelete)) {
                        sectionList.remove(i);
                    }
                }
                sectionAdapter.notifyDataSetChanged();

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
