package com.example.windows.gymapp.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.windows.gymapp.R;
import com.example.windows.gymapp.data.StorageSP;
import com.example.windows.gymapp.model.User;
import com.example.windows.gymapp.util.Constants;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;
    private AppCompatImageView ic_menu;
    private AppCompatImageView ic_add;
    private NavigationView menu_view;
    private User user;
    private TextView tv_user_name;
    private TextView tv_user_surname;
    private LinearLayout ll_profile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StorageSP storage = new StorageSP(this);
        user = storage.getUser();
        if(user==null){
            storage.deleteStorage();
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this,LoginActivity.class));
        }

        menu_view = findViewById(R.id.menu_view);
        ic_menu = findViewById(R.id.ic_menu);
        ic_add = findViewById(R.id.ic_add);
        bottomNav = findViewById(R.id.bottom_navigation);
        View headerView = menu_view.getHeaderView(0);
        tv_user_name = headerView.findViewById(R.id.tv_user_name);
        tv_user_surname = headerView.findViewById(R.id.tv_user_surname);
        ll_profile = headerView.findViewById(R.id.ll_profile);

        tv_user_name.setText(user.getName());
        tv_user_surname.setText(user.getSurname());
        

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.tab_fragment_container,new ExerciseFragment()).commit();


        handleMenu();
        updateView(true);
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
                updateView(selectedFragment);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.tab_fragment_container,selectedFragment).commit();
                return true;
            }
        });
        
        ll_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ProfileActivity.class);
                startActivity(intent);
                closeMenu();
            }
        });

        ic_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.tab_fragment_container);
                Boolean fromExercise;
                if(currentFragment instanceof ExerciseFragment){
                    fromExercise= true;
                }else{
                    fromExercise=false;
                }
                Intent intent = new Intent(getApplicationContext(),NewSectionActivity.class);
                intent.putExtra(Constants.FROM_EXERCISE,fromExercise);
                startActivity(intent);
            }
        });



    }

    private void handleMenu() {
        ic_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(menu_view.getVisibility()==View.GONE){
                    openMenu();
                }else{
                    closeMenu();
                }
            }
        });

        menu_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home:
                        closeMenu();
                        break;
                    case R.id.nav_login:
                        new StorageSP(getApplicationContext()).deleteStorage();
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                        finishAffinity();
                        break;
                    case R.id.nav_gallery:
                        startActivity(new Intent(getApplicationContext(),GalleryActivity.class));
                        closeMenu();
                        break;
                }
                return true;
            }
        });
    }

    private void closeMenu() {
        ic_menu.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu));
        menu_view.setVisibility(View.GONE);
    }

    private void openMenu() {
        ic_menu.setImageDrawable(getResources().getDrawable(R.drawable.ic_back));
        menu_view.setVisibility(View.VISIBLE);
    }

    private void updateView(boolean b) {
        if(user.getIsExpert()) {
            ic_add.setVisibility(View.VISIBLE);
        }
    }

    private void updateView(Fragment currentFragment) {

        if(currentFragment instanceof FavouriteFragment){
            ic_add.setVisibility(View.GONE);
        }else if(user.getIsExpert()){
            ic_add.setVisibility(View.VISIBLE);
        }else{
            ic_add.setVisibility(View.GONE);
        }


    }

    @Override
    public void onBackPressed() {
        if(menu_view.getVisibility()==View.VISIBLE){
            menu_view.setVisibility(View.GONE);
            ic_menu.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu));
        }else{
            super.onBackPressed();
        }
    }
}
