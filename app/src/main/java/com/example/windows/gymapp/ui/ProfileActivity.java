package com.example.windows.gymapp.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.windows.gymapp.R;
import com.example.windows.gymapp.data.StorageSP;
import com.example.windows.gymapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {

    private AppCompatImageView ic_back;
    private TextView tv_full_name, tv_email, tv_professional;
    private EditText et_old_pass, et_new_pass;
    private Button btn_change_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ic_back = findViewById(R.id.ic_back);
        tv_full_name = findViewById(R.id.tv_full_name);
        tv_email = findViewById(R.id.tv_email);
        tv_professional = findViewById(R.id.tv_professional);
        et_old_pass = findViewById(R.id.et_old_pass);
        et_new_pass = findViewById(R.id.et_new_pass);
        btn_change_pass = findViewById(R.id.btn_change_pass);

        final User storageUser = new StorageSP(this).getUser();
        tv_full_name.setText(storageUser.getFullName());
        tv_email.setText(storageUser.getEmail());
        if(storageUser.getIsExpert()){
            tv_professional.setText(getString(R.string.yes));
        }else{
            tv_professional.setText(getString(R.string.no));
        }

        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btn_change_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String oldPassword = et_old_pass.getText().toString();
                final String newPassword = et_new_pass.getText().toString();

                if(oldPassword.isEmpty() || oldPassword==null){
                    et_old_pass.setError(getString(R.string.required));
                }else if(newPassword.isEmpty() || newPassword==null){
                    et_new_pass.setError(getString(R.string.required));
                }else{
                    AuthCredential credential = EmailAuthProvider.getCredential(storageUser.getEmail(),oldPassword);
                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(getApplicationContext(), getString(R.string.password_update_succeded), Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                                            new StorageSP(getApplicationContext()).deleteStorage();
                                            FirebaseAuth.getInstance().signOut();
                                            startActivity(intent);
                                            finishAffinity();
                                        }else {
                                            try{
                                                throw task.getException();
                                            }catch (FirebaseAuthWeakPasswordException e){
                                                et_new_pass.setError(getString(R.string.weak_pass));
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }

                                    }
                                });
                            }else {
                                try{
                                    throw task.getException();
                                }catch (FirebaseAuthInvalidCredentialsException e){
                                    et_old_pass.setError(getString(R.string.wrong_password));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }
            }
        });
    }
}
