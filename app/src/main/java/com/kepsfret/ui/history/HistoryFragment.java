package com.kepsfret.ui.history;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.kepsfret.Adaptor.ItemAdaptor;
import com.kepsfret.Adaptor.OrderAdaptor;
import com.kepsfret.Models.ItemsModel;
import com.kepsfret.Models.PdfModel;
import com.kepsfret.Preview;
import com.kepsfret.R;
import com.kepsfret.AddItems;
import com.kepsfret.databinding.FragmentHistoryBinding;
import com.kepsfret.form_generated;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HistoryFragment extends Fragment {

    private FragmentHistoryBinding binding;
    FirebaseFirestore database;
    FirebaseAuth auth;
    RecyclerView recyclerView;
    TextView textView;
    OrderAdaptor orderAdaptor;
    ImageView imageView;
    List<PdfModel> pdfModelList;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        imageView = root.findViewById(R.id.order_img);
        textView = root.findViewById(R.id.order_txt);
        recyclerView = root.findViewById(R.id.recycle_history);
        database = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        pdfModelList = new ArrayList<>();
        orderAdaptor = new OrderAdaptor(getContext(),pdfModelList);
        recyclerView.setAdapter(orderAdaptor);
        database.collection("CurrentUserItems").document(auth.getCurrentUser().getUid()).collection("History")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult()).getDocuments()) {
                        PdfModel pdfModel = documentSnapshot.toObject(PdfModel.class);
                        pdfModelList.add(pdfModel);
                        orderAdaptor.notifyDataSetChanged();
                        if(pdfModel != null){
                            imageView.setVisibility(View.GONE);
                            textView.setVisibility(View.GONE);
                        }
                    }
                } else {
                    Toast.makeText(getContext(), "Error"+task.getException(), Toast.LENGTH_SHORT).show();
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