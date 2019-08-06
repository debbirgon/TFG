package com.example.windows.gymapp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.srx.widget.PullToLoadView;

import java.util.ArrayList;
import java.util.List;


public class ExerciseFragment extends Fragment {

    private List<String> sectionList = new ArrayList<>();
    private PullToLoadView mPullToLoad;
    private SectionAdapter sectionAdapter;
    private View rootView;
    private EditText et_search_exercise;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_exercise,container,false);
        mPullToLoad = rootView.findViewById(R.id.pullToLoadExercise);
        et_search_exercise = rootView.findViewById(R.id.et_search_exercise);

        updateList();
        buildRecyclerView();

        et_search_exercise.addTextChangedListener(new TextWatcher() {
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
        List<String> filteredList = new ArrayList<>();

        for(String section: sectionList){
            if(section.toLowerCase().contains(text.toLowerCase())){
                filteredList.add(section);
                sectionAdapter.filterList(filteredList);
            }
        }
    }

    private void buildRecyclerView() {

        RecyclerView mRecycler = mPullToLoad.getRecyclerView();
        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        sectionAdapter = new SectionAdapter(sectionList,getContext());
        mRecycler.setAdapter(sectionAdapter);
    }

    private void updateList() {
        sectionList.add("Abdomen");
        sectionList.add("Piernas");
        sectionList.add("Espalda");
        sectionList.add("Brazo");
        sectionList.add("*Sección añadida por un profesional*");
        sectionList.add("Otra sección");

    }
}
