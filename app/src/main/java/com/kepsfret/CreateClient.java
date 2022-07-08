package com.kepsfret;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.kepsfret.Models.Usermodels;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Objects;

public class CreateClient extends AppCompatActivity {

    EditText nom1,address1,code1,ville1,pays1,number1,nom2,address2,code2,ville2,pays2,number2;
    Button button;
    FirebaseFirestore database;
    FirebaseAuth auth;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        database = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        database.collection("CurrentUserItems").document(auth.getCurrentUser().getUid())
                .collection("addtoCart").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                for(DocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())){
                    documentSnapshot.getReference().delete();
                }
            }
        });
        Intent intent = new Intent(CreateClient.this,MainActivity2.class);
        startActivity(intent);
        finish();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        database = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        database.collection("CurrentUserItems").document(auth.getCurrentUser().getUid())
                .collection("addtoCart").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                for(DocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())){
                    documentSnapshot.getReference().delete();
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_create_client);


        nom1 = findViewById(R.id.nom);
        address1 = findViewById(R.id.adresse);
        code1 = findViewById(R.id.Codepostal);
        ville1 = findViewById(R.id.Ville);
        pays1 = findViewById(R.id.Pays);
        number1 = findViewById(R.id.Tel);
        nom2 = findViewById(R.id.dnom);
        address2 = findViewById(R.id.d_adresse);
        code2 = findViewById(R.id.d_Codepostal);
        ville2 = findViewById(R.id.d_Ville);
        pays2 = findViewById(R.id.d_Pays);
        number2 = findViewById(R.id.d_Tel);
        button = findViewById(R.id.submit);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nom1.getText().toString().isEmpty() || nom2.getText().toString().isEmpty() || address1.getText().toString().isEmpty() || address2.getText().toString().isEmpty() || code1.getText().toString().isEmpty() || code2.getText().toString().isEmpty() || ville1.getText().toString().isEmpty() || ville2.getText().toString().isEmpty() || pays1.getText().toString().isEmpty() || pays2.getText().toString().isEmpty() || number1.getText().toString().isEmpty() || number2.getText().toString().isEmpty()){
                    Toast.makeText(CreateClient.this, "Please Fill All Details!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent(CreateClient.this, AddItems.class);
                    form_generated.nom =  nom1.getText().toString();
                    form_generated.address =  address1.getText().toString();
                    form_generated.code =  code1.getText().toString();
                    form_generated.ville =  ville1.getText().toString();
                    form_generated.pays =  pays1.getText().toString();
                    form_generated.number =  number1.getText().toString();
                    form_generated.dnom =  nom2.getText().toString();
                    form_generated.daddress =  address2.getText().toString();
                    form_generated.dcode =  code2.getText().toString();
                    form_generated.dville =  ville2.getText().toString();
                    form_generated.dpays =  pays2.getText().toString();
                    form_generated.dnumber =  number2.getText().toString();
                    startActivity(intent);
                    finish();
                }
            }
        });

    }
}