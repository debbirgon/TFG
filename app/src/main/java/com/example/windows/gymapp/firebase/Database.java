package com.example.windows.gymapp.firebase;

import com.example.windows.gymapp.model.User;
import com.example.windows.gymapp.util.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by windows on 26/11/2018.
 */

public class Database {

    private User user;
    private FirebaseDatabase myDB;
    private OnGetUser myOnGetUser;

    public Database(OnGetUser myOnGetUser){
        user = new User();
        myDB = FirebaseDatabase.getInstance();
        this.myOnGetUser = myOnGetUser;
    }
    public Database(){
        user = new User();
        myDB = FirebaseDatabase.getInstance();
    }

    public void postUser (User user){
        DatabaseReference userReference = myDB.getReference(Constants.USERS);

        userReference.child(user.getId()).setValue(user);
    }

    public void getUser (final String id){

        FirebaseDatabase myDB = FirebaseDatabase.getInstance();
        DatabaseReference userReference = myDB.getReference(Constants.USERS);

        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myOnGetUser.onGetUser(dataSnapshot.child(id).getValue(User.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                databaseError.getCode();
            }
        });
    }

    public interface OnGetUser{
        void onGetUser(User user);
    }
}
