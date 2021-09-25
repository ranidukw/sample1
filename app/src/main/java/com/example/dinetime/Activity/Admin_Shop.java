package com.example.dinetime.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dinetime.Models.Product;
import com.example.dinetime.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;
public class Admin_Shop extends AppCompatActivity {

    ProgressBar progress_bar;
    ImageView imageview;
    EditText p_name,p_price;
    AppCompatButton Add,view;
    public Uri imageurl;
    private FirebaseStorage storage;
    private String shoptype;
    public String ImgUrl;
    private float price;
    DatabaseReference dbRef;
    TextView title;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_shop);

        SetupUI();
        ButtonClicks();
        SetData();
    }
    private void SetupUI(){

        imageview = findViewById(R.id.imageview);
        p_name = findViewById(R.id.p_name);
        p_price = findViewById(R.id.p_price);
        Add = findViewById(R.id.addproduct);
        view = findViewById(R.id.viewproduct);
        progress_bar = findViewById(R.id.progress_bar);
        title = findViewById(R.id.title);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        AdminHome adminHome = new AdminHome();
        shoptype = adminHome.shoptype;

    }

    private void SetData(){

        if (shoptype.equals("bakery")){
            title.setText("Bakery Shop");
        }
        else if(shoptype.equals("coffee")){
            title.setText("Coffee Shop");
        }
        else {
            title.setText("Shop");
        }

    }

    private void ButtonClicks(){

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadPicture();
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Admin_Shop.this, Admin_ShopView.class);
                startActivity(i);
            }
        });

        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SelectImage();
            }
        });
    }

    private void SelectImage(){

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    private void UploadPicture(){
        progress_bar.setVisibility(View.VISIBLE);
        if(shoptype.equals("coffee")) {

            if (imageview.getDrawable() == null) {
                Toast.makeText(this, "Please Select image", Toast.LENGTH_SHORT).show();
            } else {
                final String randomkey = UUID.randomUUID().toString();
                StorageReference mountainsRef = storageReference.child("Coffee/" + randomkey);
                mountainsRef.putFile(imageurl)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                mountainsRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        ImgUrl = uri.toString();
                                        Log.e("checkURI",""+ImgUrl);
                                        imageview.setImageURI(null);
                                        //Toast.makeText(Admin_Shop.this, "File Uploaded", Toast.LENGTH_SHORT).show();
                                        ProductAdd();
                                    }
                                });

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(Admin_Shop.this, "Something Error", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }
        else if(shoptype.equals("bakery")){
            if (imageview.getDrawable() == null) {
                Toast.makeText(this, "Please Select image", Toast.LENGTH_SHORT).show();
            } else {
                final String randomkey = UUID.randomUUID().toString();
                StorageReference mountainsRef = storageReference.child("Bakery/" + randomkey);
                mountainsRef.putFile(imageurl)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                mountainsRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        ImgUrl = uri.toString();
                                        Log.e("checkURI",""+ImgUrl);
                                        imageview.setImageURI(null);
                                        //Toast.makeText(Admin_Shop.this, "File Uploaded", Toast.LENGTH_SHORT).show();
                                        ProductAdd();
                                    }
                                });

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Admin_Shop.this, "Something Error", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }
        else {
            Log.e("NoShopTypeSelected","");
        }
    }

    private void ProductAdd(){

        String productname = p_name.getText().toString();
        String product_price = p_price.getText().toString();
        try {
             price = Float.valueOf(p_price.getText().toString());
        }catch (Exception e){
            Log.e("Float catch",""+e);
        }


        if(shoptype.equals("coffee")){
            dbRef = FirebaseDatabase.getInstance().getReference().child("CoffeeProducts");

            if(productname.isEmpty() || product_price.isEmpty()){

                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
            else{
                Product product = new Product(productname,"LKR"+product_price,price,ImgUrl,"Coffee");
                dbRef.push().setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(Admin_Shop.this, "Product Added Successfully", Toast.LENGTH_SHORT).show();
                        p_name.setText("");
                        p_price.setText("");
                        progress_bar.setVisibility(View.GONE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Admin_Shop.this, "Data Adding Failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }


        }
        else if(shoptype.equals("bakery")){
            dbRef = FirebaseDatabase.getInstance().getReference().child("BakeryProducts");
            if(productname.isEmpty() || product_price.isEmpty()){

                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
            else{
                Product product = new Product(productname,"LKR"+product_price,price,ImgUrl,"Bakery");
                dbRef.push().setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(Admin_Shop.this, "Product Added Successfully", Toast.LENGTH_SHORT).show();
                        p_name.setText("");
                        p_price.setText("");
                        progress_bar.setVisibility(View.GONE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Admin_Shop.this, "Data Adding Failed", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }
        else {
            Log.e("NoShopTypeSelected","");
        }

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){

            imageurl = data.getData();
            imageview.setImageURI(imageurl);

        }
    }


}

