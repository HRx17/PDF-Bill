package com.kepsfret;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.kepsfret.Models.ItemsModel;
import com.kepsfret.Models.PdfModel;
import com.kepsfret.Models.Usermodels;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class form_generated extends AppCompatActivity {

    private String signature_img;
    String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    private int TOTAL=0;
    private int REMISE=0;
    private int SOLDE=0;
    private int ACOMPTE=0;

    @Override
    public void onBackPressed() {
        return;
    }

    TextView textView;
    ImageView imageView;

    FirebaseStorage firestore;
    FirebaseFirestore database;
    FirebaseAuth auth;
    private static int billno=1;

    private final int STORAGE_PERMISSION_CODE = 1;
    public static String nom,address,code,ville,pays,number,dnom,daddress,dcode,dville,dpays,dnumber;
    File mydir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/kepsCache");

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_form_generated);
        textView = findViewById(R.id.text);
        imageView = findViewById(R.id.closepdf);
        firestore = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        database.collection("CurrentUserItems").document(auth.getCurrentUser().getUid())
                .collection("Profile").document("0hdD4CBfHuOjGtVGb6wA")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = Objects.requireNonNull(task.getResult());
                if (document.exists()) {
                    Usermodels usermodels = document.toObject(Usermodels.class);
                    form_generated.billno = Objects.requireNonNull(usermodels).getNumbers();
                }
            }
        });


        if (!mydir.exists()) {
            mydir.mkdirs();
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(form_generated.this,MainActivity2.class);
                startActivity(intent);
                finish();
            }
        });

        if (ContextCompat.checkSelfPermission(form_generated.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

        } else {
            requestStoragePermission();
        }
        try {
            createPDF();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {

            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(form_generated.this,
                                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
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
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
        finish();
        startActivity(getIntent());
    }

    private void createPDF() throws FileNotFoundException {
        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File file = new File(pdfPath,"Invoice"+form_generated.billno+".pdf");
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        OutputStream outputStream = new FileOutputStream(file);
        PdfWriter pdfWriter = new PdfWriter(String.valueOf(file));
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        Document document = new Document(pdfDocument);

        @SuppressLint("UseCompatLoadingForDrawables") Drawable d1 = getDrawable(R.drawable.head);
        Bitmap bitmap1 = ((BitmapDrawable)d1).getBitmap();
        ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
        bitmap1.compress(Bitmap.CompressFormat.JPEG,100,stream1);
        byte[] bitmapdata1 = stream1.toByteArray();
        ImageData imageData1 = ImageDataFactory.create(bitmapdata1);
        Image image1 = new Image(imageData1);
        image1.setHeight(100);

        database.collection("CurrentUserItems").document(auth.getCurrentUser().getUid())
                .collection("Profile").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    if (document.exists()) {
                        Usermodels usermodels = document.toObject(Usermodels.class);
                        signature_img = Objects.requireNonNull(usermodels).getSignature();
                    }
                }
            }
        });

        database.collection("CurrentUserItems").document(auth.getCurrentUser().getUid())
                .collection("addtoCart")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
        float col[] = {30,600};
        Table table = new Table(col);
        table.addCell(new Cell().add(new Paragraph("\n")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(image1).setBorder(Border.NO_BORDER));
        document.add(table);

        float colummm[] = {200,300,200};
        Table tablee = new Table(colummm);
        tablee.addCell(new Cell().add(new Paragraph("Facture No. : 00"+billno).setBold().setTextAlignment(TextAlignment.LEFT)).setBorder(Border.NO_BORDER));
        tablee.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        tablee.addCell(new Cell().add(new Paragraph("Date : "+date).setBold().setTextAlignment(TextAlignment.CENTER)).setBorder(Border.NO_BORDER));

        tablee.addCell(new Cell().add(new Paragraph("\n")).setBorder(Border.NO_BORDER));
        tablee.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        tablee.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));

        tablee.addCell(new Cell().add(new Paragraph("EXPEDITEUR").setUnderline().setBold().setTextAlignment(TextAlignment.CENTER)).setBorder(Border.NO_BORDER));
        tablee.addCell(new Cell().add(new Paragraph("").setTextAlignment(TextAlignment.RIGHT)).setBorder(Border.NO_BORDER));
        tablee.addCell(new Cell().add(new Paragraph("DESTINATAIRE").setUnderline().setBold().setTextAlignment(TextAlignment.LEFT)).setBorder(Border.NO_BORDER));
        document.add(tablee);

        float columnwidth[] = {50,90,270,100,90,130};
        Table table1 = new Table(columnwidth);

        table1.addCell(new Cell().add("").setBorder(Border.NO_BORDER));
        table1.addCell(new Cell().add(new Paragraph("Nom : ").setTextAlignment(TextAlignment.LEFT).setFontSize(10)).setBorder(Border.NO_BORDER));
        table1.addCell(new Cell().add(new Paragraph(""+nom).setTextAlignment(TextAlignment.LEFT).setFontSize(10)).setBorder(Border.NO_BORDER));
        table1.addCell(new Cell().add("").setBorder(Border.NO_BORDER));
        table1.addCell(new Cell().add(new Paragraph("Nom : ").setTextAlignment(TextAlignment.LEFT).setFontSize(10)).setBorder(Border.NO_BORDER));
        table1.addCell(new Cell().add(new Paragraph(""+dnom).setTextAlignment(TextAlignment.LEFT).setFontSize(10)).setBorder(Border.NO_BORDER));

        table1.addCell(new Cell().add("").setBorder(Border.NO_BORDER));
        table1.addCell(new Cell().add(new Paragraph("Adresse :").setTextAlignment(TextAlignment.LEFT).setFontSize(10)).setBorder(Border.NO_BORDER));
        table1.addCell(new Cell().add(new Paragraph(""+address).setTextAlignment(TextAlignment.LEFT).setFontSize(10)).setBorder(Border.NO_BORDER));
        table1.addCell(new Cell().add("").setBorder(Border.NO_BORDER));
        table1.addCell(new Cell().add(new Paragraph("Adresse :").setTextAlignment(TextAlignment.LEFT).setFontSize(10)).setBorder(Border.NO_BORDER));
        table1.addCell(new Cell().add(new Paragraph(""+daddress).setTextAlignment(TextAlignment.LEFT).setFontSize(10)).setBorder(Border.NO_BORDER));

        table1.addCell(new Cell().add("").setBorder(Border.NO_BORDER));
        table1.addCell(new Cell().add(new Paragraph("Code postal :").setTextAlignment(TextAlignment.LEFT).setFontSize(10)).setBorder(Border.NO_BORDER));
        table1.addCell(new Cell().add(new Paragraph(""+code).setTextAlignment(TextAlignment.LEFT).setFontSize(10)).setBorder(Border.NO_BORDER));
        table1.addCell(new Cell().add("").setBorder(Border.NO_BORDER));
        table1.addCell(new Cell().add(new Paragraph("Code postal :").setTextAlignment(TextAlignment.LEFT).setFontSize(10)).setBorder(Border.NO_BORDER));
        table1.addCell(new Cell().add(new Paragraph(""+dcode).setTextAlignment(TextAlignment.LEFT).setFontSize(10)).setBorder(Border.NO_BORDER));

        table1.addCell(new Cell().add("").setBorder(Border.NO_BORDER));
        table1.addCell(new Cell().add(new Paragraph("Ville :").setTextAlignment(TextAlignment.LEFT).setFontSize(10)).setBorder(Border.NO_BORDER));
        table1.addCell(new Cell().add(new Paragraph(""+ville).setTextAlignment(TextAlignment.LEFT).setFontSize(10)).setBorder(Border.NO_BORDER));
        table1.addCell(new Cell().add("").setBorder(Border.NO_BORDER));
        table1.addCell(new Cell().add(new Paragraph("Ville :").setTextAlignment(TextAlignment.LEFT).setFontSize(10)).setBorder(Border.NO_BORDER));
        table1.addCell(new Cell().add(new Paragraph(""+dville).setTextAlignment(TextAlignment.LEFT).setFontSize(10)).setBorder(Border.NO_BORDER));

        table1.addCell(new Cell().add("").setBorder(Border.NO_BORDER));
        table1.addCell(new Cell().add(new Paragraph("Pays : ").setTextAlignment(TextAlignment.LEFT).setFontSize(10)).setBorder(Border.NO_BORDER));
        table1.addCell(new Cell().add(new Paragraph(""+pays).setTextAlignment(TextAlignment.LEFT).setFontSize(10)).setBorder(Border.NO_BORDER));
        table1.addCell(new Cell().add("").setBorder(Border.NO_BORDER));
        table1.addCell(new Cell().add(new Paragraph("Pays : ").setTextAlignment(TextAlignment.LEFT).setFontSize(10)).setBorder(Border.NO_BORDER));
        table1.addCell(new Cell().add(new Paragraph(""+dpays).setTextAlignment(TextAlignment.LEFT).setFontSize(10)).setBorder(Border.NO_BORDER));

        table1.addCell(new Cell().add("").setBorder(Border.NO_BORDER));
        table1.addCell(new Cell().add(new Paragraph("Tel : ").setTextAlignment(TextAlignment.LEFT).setFontSize(10)).setBorder(Border.NO_BORDER));
        table1.addCell(new Cell().add(new Paragraph(""+number).setTextAlignment(TextAlignment.LEFT).setFontSize(10)).setBorder(Border.NO_BORDER));
        table1.addCell(new Cell().add("").setBorder(Border.NO_BORDER));
        table1.addCell(new Cell().add(new Paragraph("Tel : ").setTextAlignment(TextAlignment.LEFT).setFontSize(10)).setBorder(Border.NO_BORDER));
        table1.addCell(new Cell().add(new Paragraph(""+dnumber).setTextAlignment(TextAlignment.LEFT).setFontSize(10)).setBorder(Border.NO_BORDER));

        table1.addCell(new Cell().add("").setBorder(Border.NO_BORDER));
        table1.addCell(new Cell().add(new Paragraph("\n").setTextAlignment(TextAlignment.CENTER)).setBorder(Border.NO_BORDER));
        table1.addCell(new Cell().add(new Paragraph("").setTextAlignment(TextAlignment.LEFT)).setBorder(Border.NO_BORDER));
        table1.addCell(new Cell().add(new Paragraph("").setTextAlignment(TextAlignment.CENTER)).setBorder(Border.NO_BORDER));
        table1.addCell(new Cell().add(new Paragraph("").setTextAlignment(TextAlignment.LEFT)).setBorder(Border.NO_BORDER));
        document.add(table1);

        float columns[] = {50,50,50,150,90,50,90};
        Table table2 = new Table(columns);

        table2.addCell(new Cell().add(new Paragraph("Quantité").setFontSize(10)));
        table2.addCell(new Cell().add(new Paragraph("Poids").setFontSize(10)));
        table2.addCell(new Cell().add(new Paragraph("Volume").setFontSize(10)));
        table2.addCell(new Cell().add(new Paragraph("Designation").setTextAlignment(TextAlignment.CENTER).setFontSize(10)));
        table2.addCell(new Cell().add(new Paragraph("Prix Unitaire").setFontSize(10)));
        table2.addCell(new Cell().add(new Paragraph("Remise").setFontSize(10)));
        table2.addCell(new Cell().add(new Paragraph("Prix Total").setFontSize(10)));

        List<ItemsModel> itemsModelList = new ArrayList<>();
        for (DocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
            if (document.exists()) {
                ItemsModel itemsModel = document.toObject(ItemsModel.class);
                table2.addCell(new Cell().add(new Paragraph("" + itemsModel.getQuantity()).setFontSize(10)));
                table2.addCell(new Cell().add(new Paragraph("" + itemsModel.getPoids()).setFontSize(10)));
                table2.addCell(new Cell().add(new Paragraph("" + itemsModel.getVolume()).setFontSize(10)));
                table2.addCell(new Cell().add(new Paragraph("" + itemsModel.getDesignation()).setFontSize(10)));
                table2.addCell(new Cell().add(new Paragraph("" + itemsModel.getPrixunit()).setFontSize(10)));
                table2.addCell(new Cell().add(new Paragraph("" + itemsModel.getRemise()).setFontSize(10)));
                table2.addCell(new Cell().add(new Paragraph("" + itemsModel.getTotal()).setFontSize(10)));
                TOTAL = TOTAL + Integer.parseInt(itemsModel.getTotal());
                ACOMPTE = ACOMPTE + Integer.parseInt(itemsModel.getAcompte());
                REMISE = REMISE + ((Integer.parseInt(itemsModel.getQuantity())*Integer.parseInt(itemsModel.getPrixunit()))-Integer.parseInt(itemsModel.getTotal()));
                itemsModelList.add(itemsModel);

            }
        }

        table2.addCell(new Cell().add(new Paragraph("Total").setBold().setFontSize(10)).setBorderRight(Border.NO_BORDER));
        table2.addCell(new Cell().add(new Paragraph("")).setBorderRight(Border.NO_BORDER).setBorderLeft(Border.NO_BORDER));
        table2.addCell(new Cell().add(new Paragraph("")).setBorderRight(Border.NO_BORDER).setBorderLeft(Border.NO_BORDER));
        table2.addCell(new Cell().add(new Paragraph("")).setBorderRight(Border.NO_BORDER).setBorderLeft(Border.NO_BORDER));
        table2.addCell(new Cell().add(new Paragraph("")).setBorderLeft(Border.NO_BORDER));
        table2.addCell(new Cell().add(new Paragraph(""+REMISE).setBold().setFontSize(10)));
        table2.addCell(new Cell().add(new Paragraph(""+TOTAL).setBold().setFontSize(10)));

        table2.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table2.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table2.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table2.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table2.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table2.addCell(new Cell().add(new Paragraph("Acompte").setFontSize(10)));
        table2.addCell(new Cell().add(new Paragraph(""+ACOMPTE).setFontSize(10)));

        SOLDE = TOTAL - ACOMPTE;
        table2.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table2.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table2.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table2.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table2.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table2.addCell(new Cell().add(new Paragraph("Solde").setBold().setFontSize(10)));
        table2.addCell(new Cell().add(new Paragraph(""+SOLDE).setFontSize(10)));

        table2.addCell(new Cell().add(new Paragraph("\n")).setBorder(Border.NO_BORDER));
        table2.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table2.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table2.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table2.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table2.addCell(new Cell().add(new Paragraph("").setFontSize(10)).setBorder(Border.NO_BORDER));
        table2.addCell(new Cell().add(new Paragraph("").setFontSize(10)).setBorder(Border.NO_BORDER));
        document.add(table2);


        float column[] = {600};
        Table table3 = new Table(column);
        table3.addCell(new Cell().add(new Paragraph("Le Delai de paiement apres envoi de colis de colis est de 2 semaines").setFontSize(9)).setBorder(Border.NO_BORDER));
        table3.addCell(new Cell().add(new Paragraph("NB: Vous avez une semaine maximum pour retirer votre colis a l'arrivee, passer ce delai nous n,en serons plus responsables.").setFontSize(9)).setBorder(Border.NO_BORDER));
        table3.addCell(new Cell().add(new Paragraph("Boissons alcoolisees, pates alimentaires et huiles sont interdites !").setBold().setFontSize(9)).setBorder(Border.NO_BORDER));

        table3.addCell(new Cell().add("\n").setBorder(Border.NO_BORDER));
        table3.addCell(new Cell().add("").setBorder(Border.NO_BORDER));
        table3.addCell(new Cell().add("").setBorder(Border.NO_BORDER));
        document.add(table3);


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


        Drawable d3 = Drawable.createFromPath(mydir.getAbsolutePath() + File.separator + "sign.jpg");
        Bitmap bitmap3 = ((BitmapDrawable) d3).getBitmap();
        float colu[] = {180,400,180};

        Table table6 = new Table(colu);
        table6.addCell(new Cell().add(new Paragraph("Signature Client").setFontSize(11).setTextAlignment(TextAlignment.CENTER)).setBorder(Border.NO_BORDER));
        table6.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table6.addCell(new Cell().add(new Paragraph("Signature Agent").setFontSize(11).setTextAlignment(TextAlignment.LEFT)).setBorder(Border.NO_BORDER));
        document.add(table6);

        Table table4 = new Table(colu);
        ByteArrayOutputStream stream3 = new ByteArrayOutputStream();
        bitmap3.compress(Bitmap.CompressFormat.JPEG, 100, stream3);
        byte[] bitmapdata3 = stream3.toByteArray();
        ImageData imageData3 = ImageDataFactory.create(bitmapdata3);
        Image image3 = new Image(imageData3);
        image3.setHeight(50);

        table4.addCell(new Cell().add("").setBorder(Border.NO_BORDER));
        table4.addCell(new Cell().add("").setBorder(Border.NO_BORDER));
        table4.addCell(new Cell().add(image3).setBorder(Border.NO_BORDER));
        document.add(table4);

        float colum[] = {600,100,500};
        Table table5 = new Table(colum);

        table5.addCell(new Cell().add(new Paragraph("Adresse : 37 les hauts de Marcouville 95300 Pontoise")).setTextAlignment(TextAlignment.LEFT).setFontSize(9).setBorder(Border.NO_BORDER));
        table5.addCell(new Cell().add("").setBorder(Border.NO_BORDER));
        table5.addCell(new Cell().add(new Paragraph("Siren : 884 331 992 R.C.S Pontoise")).setTextAlignment(TextAlignment.CENTER).setFontSize(9).setBorder(Border.NO_BORDER));

        table5.addCell(new Cell().add(new Paragraph("Tel/WhatsApp : 0648620556                    www.kepsfret.com").setFontColor(Color.BLUE)).setFontSize(9).setBorder(Border.NO_BORDER));
        table5.addCell(new Cell().add("").setBorder(Border.NO_BORDER));
        table5.addCell(new Cell().add(new Paragraph("Email : kepsfret@yahoo.com").setFontColor(Color.BLUE).setTextAlignment(TextAlignment.CENTER).setFontSize(9)).setBorder(Border.NO_BORDER));
        document.add(table5);
        document.close();
                }
                else {
                    Toast.makeText(form_generated.this, "Error"+task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        Toast.makeText(this, "Pdf Created", Toast.LENGTH_SHORT).show();
        textView.setText("PDF Generated and Downloaded!");

        Intent install = new Intent(Intent.ACTION_VIEW);

        Uri uri = FileProvider.getUriForFile(getApplicationContext(),getApplicationContext().getPackageName() + ".provider", file);
        install.setDataAndType(uri, "application/pdf");
        install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        getApplicationContext().startActivity(install);

        final StorageReference reference = firestore.getReference().child("order_history").child(auth.getCurrentUser().getUid()+String.valueOf(form_generated.billno));

        reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(form_generated.this, "Uploaded!", Toast.LENGTH_SHORT).show();

                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        PdfModel pdfModel = new PdfModel();
                        pdfModel.setPdf(uri.toString());
                        pdfModel.setNum(String.valueOf(billno));
                        database.collection("CurrentUserItems").document(auth.getCurrentUser().getUid())
                                .collection("History").add(pdfModel).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference ) {

                            }
                        });
                        billno= billno +1;
                        database.collection("CurrentUserItems").document(auth.getCurrentUser().getUid())
                                .collection("Profile").document("0hdD4CBfHuOjGtVGb6wA")
                                .update("numbers",billno);
                    }
                });
            }
        });

       database.collection("CurrentUserItems").document(auth.getCurrentUser().getUid())
                .collection("addtoCart").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                for(DocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())){
                    documentSnapshot.getReference().delete();
                }
            }
        });

    }
}