package com.example.windows.gymapp.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.windows.gymapp.R;
import com.example.windows.gymapp.data.StorageSP;
import com.example.windows.gymapp.model.User;
import com.example.windows.gymapp.util.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText et_email;
    private EditText et_password;
    private Button btn_login;
    private TextView tv_register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Check current auth state
        mAuth = FirebaseAuth.getInstance();
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);
        tv_register = findViewById(R.id.tv_register);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = et_email.getText().toString();
                String pass = et_password.getText().toString();

                if(TextUtils.isEmpty(email)){
                    et_email.setError(getString(R.string.required));
                }else if(TextUtils.isEmpty(pass)){
                    et_password.setError(getString(R.string.required));
                }else{
                    mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(LoginActivity.this,
                            new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        updateUI(user, true);
                                    }else{
                                        Toast.makeText(getApplicationContext(),
                                                "Error", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        //Check current auth state
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            updateUI(currentUser,false);
        }else{
            updateUI(null,false);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        et_email.getText().clear();
        et_password.getText().clear();
    }

    private void updateUI(FirebaseUser currentUser, Boolean onLogin) {
        if(currentUser != null){
            if(currentUser.isEmailVerified()){
                FirebaseDatabase myDb = FirebaseDatabase.getInstance();
                DatabaseReference userRef = myDb.getReference(Constants.USERS);

                userRef.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        new StorageSP(getApplicationContext()).saveUser(user);

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }else if (onLogin){
                et_email.setError(getString(R.string.email_not_verified));
            }
        }
    }
}
