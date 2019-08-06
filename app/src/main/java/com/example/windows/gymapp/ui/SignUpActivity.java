package com.example.windows.gymapp.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.windows.gymapp.R;
import com.example.windows.gymapp.firebase.Database;
import com.example.windows.gymapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class SignUpActivity extends AppCompatActivity {

    private Button btn_register;
    private FirebaseAuth mAuth;
    private EditText et_email;
    private EditText et_password;
    private EditText et_name;
    private EditText et_surname;
    private User user;
    private CheckBox cb_pro;
    private Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        user = new User();
        mAuth = FirebaseAuth.getInstance();
        database = new Database();
        btn_register = findViewById(R.id.btn_register);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        et_name = findViewById(R.id.et_name);
        et_surname = findViewById(R.id.et_surname);
        cb_pro = findViewById(R.id.cb_pro);

        user.setIsExpert(false);

        cb_pro.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                user.setIsExpert(isChecked);
            }
        });

        ImageView trampa = findViewById(R.id.trampa);
        trampa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent fakeIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(fakeIntent);
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = et_email.getText().toString();
                String pass = et_password.getText().toString();
                String name = et_name.getText().toString();
                String surname = et_surname.getText().toString();

                if(TextUtils.isEmpty(email)){
                    et_email.setError(getString(R.string.required));
                }else if(TextUtils.isEmpty(pass)){
                    et_password.setError(getString(R.string.required));
                }else if(TextUtils.isEmpty(name)){
                    et_name.setError(getString(R.string.required));
                }else if(TextUtils.isEmpty(surname)){
                    et_surname.setError(getString(R.string.required));
                }else{
                    user.setEmail(email);
                    user.setName(name);
                    user.setSurname(surname);
                    mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(SignUpActivity.this,
                            new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        mAuth.getCurrentUser().sendEmailVerification();
                                        user.setId(mAuth.getCurrentUser().getUid());
                                        Toast.makeText(getApplicationContext(),
                                                "Debe verificar la dirección email, le enviaremos un correo",
                                                Toast.LENGTH_SHORT).show();
                                        database.postUser(user);
                                        onBackPressed();
                                    }else{
                                        try{
                                            throw task.getException();
                                        }catch (FirebaseAuthWeakPasswordException e){
                                            et_password.setError(getString(R.string.weak_pass));
                                        }catch (FirebaseAuthInvalidCredentialsException e){
                                            et_email.setError(getString(R.string.invalid_email));
                                        }catch(FirebaseAuthUserCollisionException e) {
                                            et_email.setError(getString(R.string.error_user_exists));
                                        }catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                    );

                }
            }
        });
    }
}
