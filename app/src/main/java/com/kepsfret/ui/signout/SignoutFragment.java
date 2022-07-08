package com.kepsfret.ui.signout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.kepsfret.Login;
import com.kepsfret.MainActivity;
import com.kepsfret.R;
import com.kepsfret.databinding.FragmentSignoutBinding;

public class SignoutFragment extends Fragment {
    private FragmentSignoutBinding binding;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSignoutBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Intent intent = new Intent(getContext(), Login.class);
        startActivity(intent);
        FirebaseAuth.getInstance().signOut();
        getActivity().finish();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
