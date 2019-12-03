package com.example.windows.gymapp.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.example.windows.gymapp.model.Activity;
import com.example.windows.gymapp.model.Section;
import com.example.windows.gymapp.model.User;
import com.example.windows.gymapp.util.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by windows on 26/11/2018.
 */

public class FirebaseDb {

    private FirebaseDatabase myDB;
    private Context mContext;
    private String user_id;

    public FirebaseDb(Context mContext){
        myDB = FirebaseDatabase.getInstance();
        this.mContext = mContext;
        user_id = FirebaseAuth.getInstance().getUid();
    }

    public void postUser (User user){
        DatabaseReference userReference = myDB.getReference(Constants.USERS);
        user_id = user.getId();
        userReference.child(user.getId()).setValue(user);
    }


    public void postSectionTraining (Section section){

        DatabaseReference trainingReference = myDB.getReference(Constants.TRAININGS);
        String id = trainingReference.push().getKey();
        section.setId(id);
        section.setId_creator(user_id);
        trainingReference.child(id).setValue(section).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(mContext,"Sección Añadida", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(mContext,"Ha habido un error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void postSectionExercise(Section section){

        DatabaseReference exerciseReference = myDB.getReference(Constants.EXERCISES);
        String id = exerciseReference.push().getKey();
        section.setId(id);
        section.setId_creator(user_id);
        exerciseReference.child(id).setValue(section).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(mContext,"Sección añadida correctamente", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(mContext,"Ha habido un error", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void postActivityInSection(final Activity activity, boolean update){
        DatabaseReference sectionReference;
        activity.setId_creator(user_id);
        if(activity.getFromExercises()){
            sectionReference = myDB.getReference(Constants.EXERCISES).child(activity.getId_section()).child(Constants.ACTIVITIES);
        }else{
            sectionReference = myDB.getReference(Constants.TRAININGS).child(activity.getId_section()).child(Constants.ACTIVITIES);
        }
        if(!update){
            String id = sectionReference.push().getKey();
            activity.setId(id);
        }
        postActivity(activity);
        sectionReference.child(activity.getId()).setValue(activity).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(mContext,"Actividad creada correctamente", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(mContext,"Ha habido un error", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void postActivity(Activity activity){
        DatabaseReference activityReference = myDB.getReference(Constants.ACTIVITIES);
        activityReference.child(activity.getId()).setValue(activity).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(mContext,"Actividad añadida correctamente!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(mContext,"Ha habido un error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateFavourites (List<String> favouriteList){
        DatabaseReference favouritesReference = myDB.getReference(Constants.USERS)
                .child(user_id).child(Constants.FAVOURITES);

        favouritesReference.setValue(favouriteList).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(mContext,"Lista de favoritos actualizada!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(mContext,"Ha habido un error!", Toast.LENGTH_SHORT).show();
            }
        });
    }



    public String getUserId(){
        return this.user_id;
    }



}
