package com.kepsfret.Adaptor;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kepsfret.Models.ItemsModel;
import com.kepsfret.Models.PdfModel;
import com.kepsfret.R;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

public class OrderAdaptor extends RecyclerView.Adapter<OrderAdaptor.ViewHolder> {

    FirebaseAuth auth;
    FirebaseFirestore db;
    Context context;
    List<PdfModel> pdfModelList;

    public OrderAdaptor(Context context, List<PdfModel> pdfModelList) {
        this.context = context;
        this.pdfModelList = pdfModelList;
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.history_items,parent,false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.number.setText("Order Number: "+pdfModelList.get(position).getNum());

        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(pdfModelList.get(position).getPdf());
                String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
                String fileName = pdfModelList.get(position).getNum()+"bill.pdf";
                DownloadManager downloadManager = (DownloadManager) context
                        .getSystemService(Context.DOWNLOAD_SERVICE);
                DownloadManager.Request request= new DownloadManager.Request(uri);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalFilesDir(context, pdfPath, fileName);
                downloadManager.enqueue(request);
                Toast.makeText(context, "Bill Download!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public int getItemCount() {
        return pdfModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView number,download;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            number = itemView.findViewById(R.id.order_num);
            download = itemView.findViewById(R.id.download);

        }
    }
}
