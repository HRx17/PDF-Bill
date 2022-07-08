package com.kepsfret;

import android.Manifest;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.kepsfret.Models.Usermodels;
import com.kepsfret.databinding.ActivityMain2Binding;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.provider.Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION;

public class MainActivity2 extends AppCompatActivity {

    final int APP_STORAGE_ACCESS_REQUEST_CODE = 501;
    private final int STORAGE_PERMISSION_CODE = 1;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMain2Binding binding;
    FirebaseFirestore database;
    FirebaseAuth auth;

    @Override
    public void onBackPressed() {
        return;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();
        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_profile, R.id.nav_register, R.id.nav_history,R.id.nav_settings,R.id.nav_signout)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        if(ContextCompat.checkSelfPermission(MainActivity2.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

        } else {
            requestStoragePermission();
        }

        View headerView = navigationView.getHeaderView(0);
        TextView name = headerView.findViewById(R.id.nav_name);
        TextView email = headerView.findViewById(R.id.nav_email);
        CircleImageView img = headerView.findViewById(R.id.nav_img);

        database.collection("CurrentUserItems").document(auth.getCurrentUser().getUid())
                .collection("Profile").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    if (document.exists()) {
                        Usermodels usermodels = document.toObject(Usermodels.class);
                        AddItems.signature_img = Objects.requireNonNull(usermodels).getSignature();
                    }
                }
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("tokn",0);
        String tmp = sharedPreferences.getString("tokn","0");

        if(tmp.equals("0")) {
            try {
                Intent intent = new Intent(ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, Uri.parse("package:" + BuildConfig.APPLICATION_ID));
                startActivityForResult(intent, APP_STORAGE_ACCESS_REQUEST_CODE);
                SharedPreferences sharedPreferencess = getSharedPreferences("tokn",0);
                SharedPreferences.Editor editorr = sharedPreferencess.edit();
                editorr.putString("tokn", String.valueOf(1));
                editorr.apply();
            }catch(ActivityNotFoundException e){
                e.printStackTrace();
            }
        }

        database.collection("CurrentUserItems").document(auth.getCurrentUser().getUid()).collection("Profile")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Usermodels usermodels = document.toObject(Usermodels.class);
                        name.setText(usermodels.getName());
                        email.setText(usermodels.getEmail());
                        Glide.with(MainActivity2.this).load(usermodels.getPhoto()).into(img);
                    }
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == APP_STORAGE_ACCESS_REQUEST_CODE)
        {
            if (Environment.isExternalStorageManager())
            {
                SharedPreferences sharedPreferencess = getSharedPreferences("tokn",0);
                SharedPreferences.Editor editorr = sharedPreferencess.edit();
                editorr.putString("tokn", String.valueOf(1));
                editorr.apply();
                // Permission granted. Now resume your workflow.
            }

        }
    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            SharedPreferences sharedPreferences = getSharedPreferences("token",0);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("token", String.valueOf(1));
            editor.apply();

            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is to download invoice")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity2.this,
                                    new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity2, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}