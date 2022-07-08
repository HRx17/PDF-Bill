package com.kepsfret.ui.register;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.kepsfret.CreateClient;
import com.kepsfret.R;
import com.kepsfret.databinding.FragmentRegisterBinding;

import org.jetbrains.annotations.NotNull;

public class RegisterFragment extends Fragment {

    Button button;
    private FragmentRegisterBinding binding;
    FirebaseFirestore database;
    FirebaseAuth auth;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        button = root.findViewById(R.id.register);
        database = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("sign", 0);
        String check = sharedPreferences.getString("sign","0");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             /*   database.collection("CurrentUserItems").document(auth.getCurrentUser().getUid())
                        .collection("Profile").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            QuerySnapshot result = task.getResult();
                            if(result.isEmpty()) {
                                Toast.makeText(getContext(), "Please Add Signature,\n Go to Profile to Add Signature!", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Intent intent = new Intent(getContext(), CreateClient.class);
                                startActivity(intent);
                            }
                        }
                    }
                });*/
                if(!check.equals("0")){
                    Intent intent = new Intent(getContext(), CreateClient.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getContext(), "Please Add Signature,\n Go to Profile to Add Signature!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}