package com.example.windows.gymapp.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.windows.gymapp.R;
import com.example.windows.gymapp.data.StorageSP;
import com.example.windows.gymapp.model.User;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StorageSP storage = new StorageSP(this);
        User user = storage.getUser();
        if(user==null){
            storage.deleteStorage();
            startActivity(new Intent(this,LoginActivity.class));
        }


        bottomNav = findViewById(R.id.bottom_navigation);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.tab_fragment_container,new ExerciseFragment()).commit();

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()){
                    case R.id.nav_exercise:
                        selectedFragment = new ExerciseFragment();
                        break;
                    case R.id.navi_training:
                        selectedFragment = new TrainingFragment();
                        break;
                    case R.id.nav_favourite:
                        selectedFragment = new FavouriteFragment();
                        break;

                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.tab_fragment_container,selectedFragment).commit();
                return true;
            }
        });


    }

    @Override
    public void onBackPressed() {
        FirebaseAuth.getInstance().signOut();
        super.onBackPressed();
    }
}
