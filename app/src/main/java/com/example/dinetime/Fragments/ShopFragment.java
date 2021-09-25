package com.example.dinetime.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dinetime.Activity.Activity_Bottom_navigation;
import com.example.dinetime.Activity.Admin_ShopView;
import com.example.dinetime.Adapters.Admin_Pview_adapter;
import com.example.dinetime.Adapters.ShopView_adapter;
import com.example.dinetime.Models.Product;
import com.example.dinetime.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class ShopFragment extends Fragment {

    TextView title;
    ShopView_adapter adapter;
    RecyclerView shopview_recycler;
    private String shoptype;
    DatabaseReference databaseReference;
    ArrayList<Product> products;
    ArrayList<String> keylist;
    String key;
    ProgressBar progress_circular;
    private FirebaseStorage storage;
    private StorageReference storageReference;



    public ShopFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_shop, container, false);

        View v = inflater.inflate(R.layout.fragment_shop, container, false);

        SetUpUI(v);
        Back();
        loadData();

        return v;
    }

    private void SetUpUI(View v){

        title = v.findViewById(R.id.title);
        shopview_recycler = v.findViewById(R.id.shopview_recycler);
        progress_circular = v.findViewById(R.id.progress_circular);
        progress_circular.setVisibility(View.VISIBLE);
        products = new ArrayList<>();
        keylist = new ArrayList<>();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        HomeFragment homeFragment = new HomeFragment();
        shoptype = homeFragment.shoptype;

        if (shoptype.equals("bakery")){
            title.setText("Bakery Shop");
        }
        else if(shoptype.equals("coffee")){
            title.setText("Coffee Shop");
        }

    }

    private void Back(){
        OnBackPressedCallback callback = new OnBackPressedCallback(true ) {
            @Override
            public void handleOnBackPressed() {

                Intent i = new Intent(getContext(), Activity_Bottom_navigation.class);
                startActivity(i);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    private void loadData(){

        if(shoptype.equals("bakery")){
            databaseReference = FirebaseDatabase.getInstance().getReference("BakeryProducts");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        progress_circular.setVisibility(View.GONE);
                        Product product = data.getValue(Product.class);
                        products.add(product); // user details added to user list
                        Log.e("namecheck", "" + product.getPname());
                        Log.e("checkkey", "" + data.getKey());
                        Log.e("checkkey", "" + data.getRef());
                        keylist.add(data.getKey()); //adding keys to different list - path key
                    }

                    putDataIntoBottomRecyclerView(getContext(), products);
                    Log.e("listcheck", "" + products);
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "Something Error Try again later", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else if(shoptype.equals("coffee")){
            databaseReference = FirebaseDatabase.getInstance().getReference("CoffeeProducts");
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

                    putDataIntoBottomRecyclerView(getContext(), products);
                    Log.e("listcheck", "" + products);
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "Something Error Try again later", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            Toast.makeText(getContext(), "Something Error", Toast.LENGTH_SHORT).show();
        }
    }

    private void putDataIntoBottomRecyclerView(Context context, ArrayList<Product> itemsList) {

        Log.e("Listsize_check", "" + itemsList.size());
        if (itemsList.size() == 0){
            Toast.makeText(context, "No Data to Display", Toast.LENGTH_SHORT).show();
        }
        else {
            progress_circular.setVisibility(View.GONE);
            adapter = new ShopView_adapter(getContext(), itemsList);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            shopview_recycler.setLayoutManager(layoutManager);
            shopview_recycler.setAdapter(adapter);

            adapter.setOnItemClickListner(new ShopView_adapter.onItemClickListner() {
                @Override
                public void onCardClick(int position) {
                    Product data = itemsList.get(position);
                    String key = keylist.get(position);
                    //Log.e("checkpostion",""+dta.getPname());
                    Log.e("CheckKey",""+key);
                    Bundle b = new Bundle();
                    b.putSerializable("productItem", data);
                    // itemDetailsIntent.putExtras(b); //pass bundle to your intent

                    ProductDiscription_Fragment product = new ProductDiscription_Fragment();
                    product.setArguments(b);



                    getActivity().getSupportFragmentManager()
                            .beginTransaction().
                            replace(R.id.fragmentContainer,product)
                            .addToBackStack(null)
                            .commit();
                }
            });


        }
    }
}