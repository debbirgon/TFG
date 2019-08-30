package com.example.windows.gymapp.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.windows.gymapp.R;
import com.example.windows.gymapp.adpter.GalleryAdapter;
import com.example.windows.gymapp.data.StorageSP;
import com.example.windows.gymapp.model.Image;
import com.example.windows.gymapp.model.User;
import com.example.windows.gymapp.util.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends AppCompatActivity {

    private AppCompatImageView ic_menu, ic_add_photo;
    private NavigationView menu_view;
    private User user;
    private TextView tv_user_name;
    private TextView tv_user_surname;
    private LinearLayout ll_profile;
    private RecyclerView rv_gallery;
    private GalleryAdapter myAdapter;
    private DatabaseReference imageRef;
    private List<Image> imageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        menu_view = findViewById(R.id.menu_view);
        ic_menu = findViewById(R.id.ic_menu);
        View headerView = menu_view.getHeaderView(0);
        tv_user_name = headerView.findViewById(R.id.tv_user_name);
        tv_user_surname = headerView.findViewById(R.id.tv_user_surname);
        ll_profile = headerView.findViewById(R.id.ll_profile);
        ic_add_photo = findViewById(R.id.ic_add_photo);
        rv_gallery = findViewById(R.id.rv_gallery);

        StorageSP storage = new StorageSP(this);
        user = storage.getUser();

        imageList = new ArrayList<>();
        imageRef = FirebaseDatabase.getInstance().getReference(Constants.IMAGES).child(user.getId());

        tv_user_name.setText(user.getName());
        tv_user_surname.setText(user.getSurname());

        handleMenu();
        updateList();
        buildRecyclerView();

        ll_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ProfileActivity.class);
                startActivity(intent);
                closeMenu();
            }
        });

        ic_add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddPhotoActivity.class);
                startActivity(intent);
            }
        });
    }

    private void updateList() {
        imageRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                imageList.add(dataSnapshot.getValue(Image.class));
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                String idDelete = dataSnapshot.getValue(Image.class).getName();
                for(int i =0; i<imageList.size(); i++){
                    if(imageList.get(i).getName().equals(idDelete)) {
                        imageList.remove(i);
                    }
                }
                myAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void buildRecyclerView() {

        rv_gallery.setHasFixedSize(true);
        rv_gallery.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        myAdapter = new GalleryAdapter(imageList,getApplicationContext());
        rv_gallery.setAdapter(myAdapter);
    }

    private void handleMenu() {
        ic_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (menu_view.getVisibility() == View.GONE) {
                    openMenu();
                } else {
                    closeMenu();
                }
            }
        });

        menu_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        finish();
                        break;
                    case R.id.nav_login:
                        new StorageSP(getApplicationContext()).deleteStorage();
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finishAffinity();
                        break;
                    case R.id.nav_gallery:
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
