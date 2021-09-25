package com.example.dinetime.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.dinetime.Adapters.Admin_Pview_adapter;
import com.example.dinetime.Models.Product;
import com.example.dinetime.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Admin_ShopView extends AppCompatActivity {

    ProgressBar progress_circular;
    RecyclerView shopview_recycler;
    Admin_Pview_adapter adapter;
    DatabaseReference databaseReference;
    ArrayList<Product> products;
    ArrayList<String> keylist;
    String key;
    private FirebaseStorage storage;
    String shoptype;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_shop_view);

        SetupUI();
        loadData();
    }


    private void SetupUI(){

        progress_circular = findViewById(R.id.progress_circular);
        shopview_recycler = findViewById(R.id.shopview_recycler);
        products = new ArrayList<>();
        keylist = new ArrayList<>();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        AdminHome home = new AdminHome();
        shoptype = home.shoptype;
    }

    private void loadData(){

        if(shoptype.equals("bakery")){
            databaseReference = FirebaseDatabase.getInstance().getReference("BakeryProducts");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot data : snapshot.getChildren()) {

                        Product product = data.getValue(Product.class);
                        products.add(product); // user details added to user list
                        Log.e("namecheck", "" + product.getPname());
                        Log.e("checkkey", "" + data.getKey());
                        Log.e("checkkey", "" + data.getRef());
                        keylist.add(data.getKey()); //adding keys to different list - path key
                    }

                    putDataIntoBottomRecyclerView(Admin_ShopView.this, products);
                    Log.e("listcheck", "" + products);
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(Admin_ShopView.this, "Something Error Try again later", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else if(shoptype.equals("coffee")){
            databaseReference = FirebaseDatabase.getInstance().getReference("CoffeeProducts");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        progress_circular.setVisibility(View.VISIBLE);
                        Product product = data.getValue(Product.class);
                        products.add(product); // user details added to user list
                        Log.e("namecheck", "" + product.getPname());
                        Log.e("checkkey", "" + data.getKey());
                        Log.e("checkkey", "" + data.getRef());
                        keylist.add(data.getKey()); //adding keys to different list - path key
                    }

                     putDataIntoBottomRecyclerView(Admin_ShopView.this, products);
                    Log.e("listcheck", "" + products);
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(Admin_ShopView.this, "Something Error Try again later", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            Toast.makeText(this, "Something Error", Toast.LENGTH_SHORT).show();
        }
    }

    private void putDataIntoBottomRecyclerView(Context context, ArrayList<Product> itemsList) {

        Log.e("Listsize_check", "" + itemsList.size());
        if (itemsList.size() == 0){
            Toast.makeText(context, "No Data to Display", Toast.LENGTH_SHORT).show();
        }
        else {
            progress_circular.setVisibility(View.GONE);
            adapter = new Admin_Pview_adapter(this, itemsList);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            shopview_recycler.setLayoutManager(layoutManager);
            shopview_recycler.setAdapter(adapter);

            adapter.setOnItemClickListner(new Admin_Pview_adapter.onItemClickListner() {
                @Override
                public void onDelete(int position) {
                    Product dta = itemsList.get(position);
                    String key = keylist.get(position);
                    //Log.e("checkpostion",""+dta.getPname());
                    Log.e("CheckKey",""+key);
                    if(shoptype.equals("coffee")){

                        databaseReference = FirebaseDatabase.getInstance().getReference("CoffeeProducts").child(key);
                        databaseReference.removeValue();
                        DeleteSuccess();
                        adapter.updateData(itemsList);

                    }
                    else if(shoptype.equals("bakery")){
                        databaseReference = FirebaseDatabase.getInstance().getReference("BakeryProducts").child(key);
                        databaseReference.removeValue();
                        DeleteSuccess();
                        adapter.updateData(itemsList);

                    }


                }
            });

        }
    }
    private void DeleteSuccess() {
        SweetAlertDialog pdialog = new SweetAlertDialog(Admin_ShopView.this, SweetAlertDialog.SUCCESS_TYPE);
        pdialog.setTitleText("Completed");
        pdialog.setContentText("Product Deleted Successfully");
        pdialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismissWithAnimation();
            }
        });
        pdialog.setCanceledOnTouchOutside(false);
        pdialog.show();
    }
}