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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddItems extends AppCompatActivity{

    Button add,button;
    EditText code,quantity,poids,volume,designation,prixunit,remise,acompte;
    EditText total;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    static String signature_img;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AddItems.this,CreateClient.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_additems);
        add = findViewById(R.id.add_item);
        acompte = findViewById(R.id.acompte);
        button = findViewById(R.id.Proceed);
        code = findViewById(R.id.code);
        quantity = findViewById(R.id.quantity);
        poids = findViewById(R.id.Poids);
        volume = findViewById(R.id.volume);
        designation = findViewById(R.id.Designation);
        prixunit = findViewById(R.id.prix_uti);
        remise = findViewById(R.id.Remise);
        total = findViewById(R.id.total);
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        File dr = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/kepsCache/sign.jpg");
        if(!dr.exists()) {
            DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            Uri downloadUri = Uri.parse(signature_img);
            DownloadManager.Request request = new DownloadManager.Request(downloadUri);
            request.setAllowedNetworkTypes(
                    DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false)
                    .setTitle("Downloading")
                    .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/kepsCache/sign.jpg");
            manager.enqueue(request);
        }
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
                userData.put("acompte",acompte.getText().toString());
                userData.put("total",total.getText().toString());

                firestore.collection("CurrentUserItems").document(auth.getCurrentUser().getUid())
                        .collection("addtoCart").add(userData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(AddItems.this, "User Data is Stored Successfully", Toast.LENGTH_LONG).show();
                            code.getText().clear();
                            quantity.getText().clear();
                            poids.getText().clear();
                            volume.getText().clear();
                            designation.getText().clear();
                            prixunit.getText().clear();
                            remise.getText().clear();
                            acompte.getText().clear();
                            total.getText().clear();
                    }
                });
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddItems.this,Preview.class);
                startActivity(intent);
                finish();
            }
        });

    }
}

