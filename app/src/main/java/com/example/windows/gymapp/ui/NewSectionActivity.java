package com.example.windows.gymapp.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.example.windows.gymapp.R;
import com.example.windows.gymapp.data.FirebaseDb;
import com.example.windows.gymapp.model.Section;
import com.example.windows.gymapp.util.Constants;

public class NewSectionActivity extends AppCompatActivity {

    AppCompatImageView ic_back;
    EditText et_name;
    RadioGroup rg_section_kind;
    Button btn_add_section;
    FirebaseDb db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_section);

        Bundle extras = getIntent().getExtras();
        Boolean fromExercise = extras.getBoolean(Constants.FROM_EXERCISE);

        db = new FirebaseDb(this);
        et_name = findViewById(R.id.et_name);
        rg_section_kind = findViewById(R.id.rg_section_kind);
        btn_add_section = findViewById(R.id.btn_add_section);
        ic_back = findViewById(R.id.ic_back);

        if(fromExercise){
            rg_section_kind.check(R.id.rb_exercises);
        }else{
            rg_section_kind.check(R.id.rb_trainings);
        }

        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btn_add_section.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sectionName = et_name.getText().toString();
                if(sectionName==null||sectionName.isEmpty()){
                    et_name.setError(getString(R.string.required));
                }else{
                    Section section = new Section();
                    section.setName(sectionName);

                    switch (rg_section_kind.getCheckedRadioButtonId()){
                        case R.id.rb_exercises:
                            db.postSectionExercise(section);
                            break;
                        case R.id.rb_trainings:
                            db.postSectionTraining(section);
                            break;
                    }
                }

                onBackPressed();

            }
        });



    }
}
