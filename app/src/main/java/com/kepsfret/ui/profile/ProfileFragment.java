package com.kepsfret.ui.profile;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kepsfret.MainActivity;
import com.kepsfret.MainActivity2;
import com.kepsfret.Models.Usermodels;
import com.kepsfret.R;
import com.kepsfret.databinding.FragmentProfileBinding;
import com.kepsfret.form_generated;
import com.kepsfret.sign;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.DOWNLOAD_SERVICE;

public class ProfileFragment extends Fragment {

    public static Uri KEY_User_Document1;
    public static int CHECK=0;
    ProgressBar progressBar;
    ImageView IDProf,profilepic;
    Button Upload_Btn;
    EditText email,name;
    FirebaseStorage firestore;
    FirebaseFirestore db;
    FirebaseAuth auth;
    FirebaseDatabase database;

    String Document_img1="";
    private FragmentProfileBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        progressBar = root.findViewById(R.id.wait);
        email = (EditText) root.findViewById(R.id.email);
        name = (EditText)root.findViewById(R.id.name);
        profilepic = root.findViewById(R.id.profile_image);
        IDProf=root.findViewById(R.id.IdProf);
        Upload_Btn=root.findViewById(R.id.update_profile_button);
        firestore = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        db = FirebaseFirestore.getInstance();
        progressBar.setVisibility(View.VISIBLE);

        if(CHECK == 1){
            progressBar.setVisibility(View.GONE);
        }
        if(!(KEY_User_Document1 == null)) {
            IDProf.setImageURI(KEY_User_Document1);
        }

        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 33);
            }
        });

        IDProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), sign.class);
                startActivity(intent);

            }
        });

        Upload_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(CHECK == 0){
                    Toast.makeText(getContext(), "Please Add Signature and wait", Toast.LENGTH_SHORT).show();
                }
                else {
                    progressBar.setVisibility(View.GONE);
                    if (name.getText().toString().isEmpty() || email.getText().toString().isEmpty()) {
                        Toast.makeText(getContext(), "Please Enter All Details!", Toast.LENGTH_SHORT).show();
                    } else {
                        Map<String, Object> update = new HashMap<>();
                        update.put("Name", name.getText().toString());
                        update.put("Photo", Document_img1);
                        update.put("Signature", KEY_User_Document1.toString());
                        update.put("email", email.getText().toString());
                        update.put("numbers",1);
                        db.collection("CurrentUserItems").document(auth.getCurrentUser().getUid()).collection("Profile").document(
                                "0hdD4CBfHuOjGtVGb6wA").set(update).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getContext(), "Updated!", Toast.LENGTH_SHORT).show();
                                email.getText().clear();
                                name.getText().clear();
                                SharedPreferences sharedPreferencess = getActivity().getApplicationContext().getSharedPreferences("sign",0);
                                SharedPreferences.Editor editorr = sharedPreferencess.edit();
                                editorr.putString("sign", "1");
                                editorr.apply();
                                File dr = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/kepsCache/sign.jpg");
                                if(dr.exists()) {
                                    dr.delete();
                                    DownloadManager manager = (DownloadManager) getActivity().getSystemService(DOWNLOAD_SERVICE);
                                    DownloadManager.Request request = new DownloadManager.Request(KEY_User_Document1);
                                    request.setAllowedNetworkTypes(
                                            DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                                            .setAllowedOverRoaming(false)
                                            .setTitle("Downloading")
                                            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/kepsCache/sign.jpg");
                                    manager.enqueue(request);
                                }
                            }
                        });
                    }
                }
            }
        });
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data.getData() != null) {
            Uri profileUri = data.getData();
            profilepic.setImageURI(profileUri);

            final StorageReference reference = firestore.getReference().child("profile_picture")
                    .child(FirebaseAuth.getInstance().getUid());
            reference.putFile(profileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getContext(), "Uploaded!", Toast.LENGTH_SHORT).show();

                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Document_img1 = uri.toString();
                            database.getReference().child("Admin").child(FirebaseAuth.getInstance().getUid())
                                    .child("profile_img").setValue(uri.toString());
                            Toast.makeText(getContext(), "Profile Picture Updated!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}