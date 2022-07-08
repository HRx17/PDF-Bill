package com.kepsfret;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.kepsfret.Adaptor.ItemAdaptor;
import com.kepsfret.Models.ItemsModel;
import com.kepsfret.Models.Usermodels;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Preview extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView imageView;
    TextView textView;
    Button button;
    FirebaseFirestore database;
    List<ItemsModel> itemsModelList;
    ItemAdaptor itemAdaptor;
    FirebaseAuth auth;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Preview.this,AddItems.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_preview);

        button = findViewById(R.id.preview_confirm);
        textView = findViewById(R.id.text);
        imageView = findViewById(R.id.empty);
        button.setVisibility(View.GONE);
        recyclerView = findViewById(R.id.recycle_preview);

        database = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();


        recyclerView.setLayoutManager(new LinearLayoutManager(Preview.this,RecyclerView.VERTICAL,false));
        itemsModelList = new ArrayList<>();
        itemAdaptor = new ItemAdaptor(Preview.this,itemsModelList);
        recyclerView.setAdapter(itemAdaptor);

        database.collection("CurrentUserItems").document(auth.getCurrentUser().getUid()).collection("addtoCart")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                        String documentId = documentSnapshot.getId();
                        ItemsModel itemsModel = documentSnapshot.toObject(ItemsModel.class);
                        itemsModel.setDocumentId(documentId);
                        itemsModelList.add(itemsModel);
                        itemAdaptor.notifyDataSetChanged();
                        if(itemsModel != null){
                            imageView.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            button.setVisibility(View.VISIBLE);
                            textView.setVisibility(View.GONE);
                        }
                        else{
                            imageView.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                            button.setVisibility(View.GONE);
                            textView.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    Toast.makeText(Preview.this, "Error"+task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.collection("CurrentUserItems").document(auth.getCurrentUser().getUid()).collection("addtoCart")
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                                        ItemsModel itemsModel = documentSnapshot.toObject(ItemsModel.class);
                                        if (itemsModel == null) {
                                            Toast.makeText(Preview.this, "No Item in Cart", Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            Intent intent = new Intent(Preview.this, form_generated.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                }
                            }
                        });
            }
        });
    }
}