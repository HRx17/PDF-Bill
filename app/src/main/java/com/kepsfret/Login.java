package com.kepsfret;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.annotations.NotNull;

public class Login extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        return;
    }

    Button button;
    EditText email, pass;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.log_email);
        pass = findViewById(R.id.log_pass);
        button = findViewById(R.id.log_in);
        auth = FirebaseAuth.getInstance();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailid, password;
                emailid = email.getText().toString();
                password = pass.getText().toString();
                if(email.getText().toString().isEmpty() || pass.getText().toString().isEmpty()) {
                    Toast.makeText(Login.this, "Please Fill Details!", Toast.LENGTH_SHORT).show();
                }
                else{
                    auth.signInWithEmailAndPassword(emailid,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(Login.this, "Sign-Up Successful", Toast.LENGTH_SHORT).show();
                                Intent logMain = new Intent(Login.this, MainActivity2.class);
                                startActivity(logMain);
                            }
                            else{
                                Toast.makeText(Login.this, "Error:"+ task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });
    }
}