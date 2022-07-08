package com.kepsfret;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kepsfret.Models.ItemsModel;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class editActivity extends AppCompatActivity {

    Button add,button;
    EditText code,quantity,poids,volume,designation,prixunit,remise;
    EditText total;
    FirebaseFirestore firestore;
    FirebaseAuth auth;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(editActivity.this,Preview.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_edit);

        String id = String.valueOf(getIntent().getSerializableExtra("details"));
        add = findViewById(R.id.update_item);
        code = findViewById(R.id.ed_code);
        quantity = findViewById(R.id.ed_quantity);
        poids = findViewById(R.id.ed_Poids);
        volume = findViewById(R.id.ed_volume);
        designation = findViewById(R.id.ed_Designation);
        prixunit = findViewById(R.id.ed_prix_uti);
        remise = findViewById(R.id.ed_Remise);
        total = findViewById(R.id.ed_total);
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        firestore.collection("CurrentUserItems").document(auth.getCurrentUser().getUid())
                .collection("addtoCart").document(id)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    ItemsModel itemsModel = Objects.requireNonNull(documentSnapshot).toObject(ItemsModel.class);
                    code.setText(Objects.requireNonNull(itemsModel).getItemCode());
                    quantity.setText(itemsModel.getQuantity());
                    poids.setText(itemsModel.getPoids());
                    volume.setText(itemsModel.getVolume());
                    designation.setText(itemsModel.getDesignation());
                    prixunit.setText(itemsModel.getPrixunit());
                    remise.setText(itemsModel.getRemise());
                    total.setText(itemsModel.getTotal());
                }
            }
        });

        quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(prixunit.getText().toString().isEmpty() || quantity.getText().toString().isEmpty() ) {
                }
                else {
                    if (remise.getText().toString().isEmpty()) {
                        final String prix = prixunit.getText().toString();
                        final String quanti = quantity.getText().toString();
                        int unit = Integer.parseInt(prix);
                        int quant = Integer.parseInt(quanti);
                        int totall = (unit * quant);
                        total.setText(String.valueOf(totall));
                    } else {
                        final String prix = prixunit.getText().toString();
                        final String remis = remise.getText().toString();
                        final String quanti = quantity.getText().toString();

                        int unit = Integer.parseInt(prix);
                        int rem = Integer.parseInt(remis);
                        int quant = Integer.parseInt(quanti);
                        int totall = ((unit - rem) * quant);

                        total.setText(String.valueOf(totall));
                    }
                }
            }
        });

        prixunit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(prixunit.getText().toString().isEmpty() || quantity.getText().toString().isEmpty() ) {
                }
                else{
                    if (remise.getText().toString().isEmpty()) {
                        final String prix = prixunit.getText().toString();
                        final String quanti = quantity.getText().toString();
                        int unit = Integer.parseInt(prix);
                        int quant = Integer.parseInt(quanti);
                        int totall = (unit * quant);
                        total.setText(String.valueOf(totall));
                    } else {
                        final String prix = prixunit.getText().toString();
                        final String remis = remise.getText().toString();
                        final String quanti = quantity.getText().toString();

                        int unit = Integer.parseInt(prix);
                        int rem = Integer.parseInt(remis);
                        int quant = Integer.parseInt(quanti);
                        int totall = ((unit - rem) * quant);

                        total.setText(String.valueOf(totall));
                    }
                }
            }
        });

        remise.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(prixunit.getText().toString().isEmpty() || quantity.getText().toString().isEmpty() ) {
                }
                else{
                    if (remise.getText().toString().isEmpty()) {
                        final String prix = prixunit.getText().toString();
                        final String quanti = quantity.getText().toString();
                        int unit = Integer.parseInt(prix);
                        int quant = Integer.parseInt(quanti);
                        int totall = (unit * quant);
                        total.setText(String.valueOf(totall));
                    } else {
                        final String prix = prixunit.getText().toString();
                        final String remis = remise.getText().toString();
                        final String quanti = quantity.getText().toString();

                        int unit = Integer.parseInt(prix);
                        int rem = Integer.parseInt(remis);
                        int quant = Integer.parseInt(quanti);
                        int totall = ((unit - rem) * quant);

                        total.setText(String.valueOf(totall));
                    }
                }
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> userData = new HashMap<>();
                userData.put("itemCode",code.getText().toString());
                userData.put("quantity",quantity.getText().toString());
                userData.put("poids",poids.getText().toString());
                userData.put("volume",volume.getText().toString());
                userData.put("designation",designation.getText().toString());
                userData.put("prixunit",prixunit.getText().toString());
                userData.put("remise",remise.getText().toString());
                userData.put("total",total.getText().toString());

                firestore.collection("CurrentUserItems").document(auth.getCurrentUser().getUid())
                        .collection("addtoCart").document(id).update(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        Toast.makeText(editActivity.this, "Updated!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(editActivity.this,Preview.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });
    }
}

