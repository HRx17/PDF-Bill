package com.kepsfret.Adaptor;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kepsfret.Models.ItemsModel;
import com.kepsfret.Preview;
import com.kepsfret.R;
import com.kepsfret.editActivity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemAdaptor extends RecyclerView.Adapter<ItemAdaptor.ViewHolder> {

    FirebaseAuth auth;
    FirebaseFirestore db;
    Context context;
    List<ItemsModel> itemsModelList;

    public ItemAdaptor(Context context, List<ItemsModel> itemsModelList) {
        this.context = context;
        this.itemsModelList = itemsModelList;
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.quantity.setText("Quantity: "+itemsModelList.get(position).getQuantity());
        holder.code.setText("Item Code : "+itemsModelList.get(position).getItemCode());
        holder.poids.setText("Poids : "+itemsModelList.get(position).getPoids());
        holder.volume.setText("Volume : "+itemsModelList.get(position).getVolume());
        holder.designation.setText("Designation : "+itemsModelList.get(position).getDesignation());
        holder.prixunit.setText("PrixUnit : "+itemsModelList.get(position).getPrixunit());
        holder.remise.setText("Remise : "+itemsModelList.get(position).getRemise());
        holder.total.setText("Total : "+itemsModelList.get(position).getTotal());

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("CurrentUserItems").document(auth.getCurrentUser().getUid())
                        .collection("addtoCart").document(itemsModelList.get(position).getDocumentId())
                        .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if(task.isSuccessful()){
                            itemsModelList.remove(itemsModelList.get(position));
                            notifyDataSetChanged();
                            Toast.makeText(context, "Item Deleted!", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(context, "Error!"+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, editActivity.class);
                intent.putExtra("details", itemsModelList.get(position).getDocumentId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemsModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView code,quantity,poids,volume,designation,prixunit,remise,total,delete,edit;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            code = itemView.findViewById(R.id.pop_code);
            quantity = itemView.findViewById(R.id.pop_quantity);
            poids = itemView.findViewById(R.id.pop_poids);
            volume = itemView.findViewById(R.id.pop_volume);
            designation = itemView.findViewById(R.id.pop_designation);
            prixunit = itemView.findViewById(R.id.pop_prixunit);
            remise = itemView.findViewById(R.id.pop_remise);
            total = itemView.findViewById(R.id.pop_total);
            delete = itemView.findViewById(R.id.delete);
            edit = itemView.findViewById(R.id.edit);

        }
    }
}
