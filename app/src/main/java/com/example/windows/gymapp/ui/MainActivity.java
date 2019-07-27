package com.example.windows.gymapp.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.windows.gymapp.R;
import com.example.windows.gymapp.firebase.Database;
import com.example.windows.gymapp.model.User;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements Database.OnGetUser{

    TextView name;
    Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = new Database(this);
        name = findViewById(R.id.tv_name);

        database.getUser(FirebaseAuth.getInstance().getUid());


    }

    @Override
    public void onBackPressed() {
        FirebaseAuth.getInstance().signOut();
        super.onBackPressed();
    }

    @Override
    public void onGetUser(User user) {
        String nameS = user.getName() + " " + user.getSurname();
        name.setText(nameS);
    }
}
