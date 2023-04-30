package com.example.myproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {

    Button loginb, signInb;
    EditText usernametxt, passwordtxt, emailtxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        loginb = findViewById(R.id.sloginbttn);
        signInb = findViewById(R.id.signinbttn);
        usernametxt = findViewById(R.id.susername);
        passwordtxt = findViewById(R.id.spassword);
        emailtxt = findViewById(R.id.email);

        loginb.setOnClickListener((v) -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        signInb.setOnClickListener((v)->createAccount() );
    }


    void createAccount() {
        String email = emailtxt.getText().toString();
        String password = passwordtxt.getText().toString();
        String username = usernametxt.getText().toString();

        boolean isValidated = validateData(email, password, username);
        if (!isValidated) {
            return;
        }

        createAccountInFirebase(email, password);
    }

    void createAccountInFirebase(String email,String password){

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(SignupActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            //creating acc is done
                            Toast.makeText(SignupActivity.this, "Successfully created account. Check email to verify.", Toast.LENGTH_LONG).show();
                            firebaseAuth.getCurrentUser().sendEmailVerification();
                            firebaseAuth.signOut();
                            finish();
                        }else{
                            //failure
                            Toast.makeText(SignupActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();

                        }
                    }
                }
        );

    }
    boolean validateData(String email,String password,String username){
        //validate the data that are input by user.

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailtxt.setError("Email is invalid");
            return false;
        }
        if(password.length()<6){
            passwordtxt.setError("Password length is invalid");
            return false;
        }
        if(username.length()<3){
            usernametxt.setError("Username is invalid");
            return false;
        }
        return true;
    }

}