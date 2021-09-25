package com.example.dinetime.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
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

import com.example.dinetime.Activity.PaymentSelect;
import com.example.dinetime.Activity.Payment_COD;
import com.example.dinetime.Activity.Payment_Pickup;
import com.example.dinetime.Adapters.CartAdapter;
import com.example.dinetime.Models.Cart;
import com.example.dinetime.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class CartFragment extends Fragment {

    ProgressBar progress_circular;
    RecyclerView cart_recycler;
    TextView cart_tprice;
    AppCompatButton checkout;
    CartAdapter adapter;
    DatabaseReference databaseReference;
    ArrayList<Cart> cartArrayList;
    ArrayList<String> keylist;
    String key;
    int CartTotal=0;
    private FirebaseStorage storage;
    private StorageReference storageReference;


    public CartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_cart, container, false);

        View v = inflater.inflate(R.layout.fragment_cart,container,false);
        SetUpUI(v);
        DataDisplay();
        Checkout();

        return v;
    }

    private void SetUpUI(View v){

        checkout = v.findViewById(R.id.checkout);
        cart_recycler = v.findViewById(R.id.cart_recycler);
        progress_circular = v.findViewById(R.id.progress_circular);
        cart_tprice = v.findViewById(R.id.cart_tprice);

        cartArrayList = new ArrayList<>();
        keylist = new ArrayList<>();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("CartItems");
    }

    private void DataDisplay(){

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    progress_circular.setVisibility(View.GONE);
                    Cart product = data.getValue(Cart.class);
                    cartArrayList.add(product); // user details added to user list
                    Log.e("namecheck", "" + product.getImageurl());
                    Log.e("checkkey", "" + data.getKey());
                    Log.e("checkkey", "" + data.getRef());
                    keylist.add(data.getKey()); //adding keys to different list - path key


                }

                putDataIntoBottomRecyclerView(getContext(), cartArrayList);

                Log.e("listcheck", "" + cartArrayList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Something Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void Checkout(){

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), PaymentSelect.class);
                i.putExtra("totalprice",CartTotal);
                startActivity(i);
            }
        });
    }

    private void SetTotal(ArrayList<Cart> cartArrayList1){
        for (int i=0; i< cartArrayList1.size(); i++){
            Log.e("checkeachtotal",""+cartArrayList1.get(i).getTotalPrice());
            float temp = cartArrayList1.get(i).getTotalPrice();
            Log.e("checktemp",""+temp);
            CartTotal = (int) (CartTotal+temp);
            Log.e("CheckTotal",""+CartTotal);
        }

        cart_tprice.setText("LKR"+" "+CartTotal+"."+"00");
    }

    private void putDataIntoBottomRecyclerView(Context context, ArrayList<Cart> itemsList) {

        Log.e("Listsize_check", "" + itemsList.size());
        if (itemsList.size() == 0){
            Toast.makeText(context, "No Data to Display", Toast.LENGTH_SHORT).show();
        }
        else {
            adapter = new CartAdapter(getActivity(), itemsList);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            cart_recycler.setLayoutManager(layoutManager);
            cart_recycler.setAdapter(adapter);
            SetTotal(itemsList);
            adapter.setOnItemClickListner(new CartAdapter.onItemClickListner() {
                @Override
                public void onDelete(int position) {
                    Cart dta = itemsList.get(position);
                    String key = keylist.get(position);
                    //Log.e("checkpostion",""+dta.getPname());
                    Log.e("CheckKey",""+key);
                    databaseReference = FirebaseDatabase.getInstance().getReference("CartItems").child(key);
                    databaseReference.removeValue();
                    DeleteComplete();
                    adapter.updateData(itemsList);
                    UpdateTotal(itemsList);
                }
            });



        }
    }
    private void UpdateTotal(ArrayList<Cart> cartArrayList1){
        CartTotal=0;
        for (int i=0; i< cartArrayList1.size(); i++){
            Log.e("checkeachtotal",""+cartArrayList1.get(i).getTotalPrice());
            float temp = cartArrayList1.get(i).getTotalPrice();
            Log.e("checktemp",""+temp);
            CartTotal = (int) (CartTotal+temp);
            Log.e("CheckTotal",""+CartTotal);
        }

        cart_tprice.setText("LKR"+" "+CartTotal+"."+"00");
    }

    private void DeleteComplete() {
        SweetAlertDialog pdialog = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE);
        pdialog.setTitleText("Completed");
        pdialog.setContentText("Cart Item Deleted Successfully");
        pdialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog)
            {
                sweetAlertDialog.dismissWithAnimation();
            }
        });
        pdialog.setCanceledOnTouchOutside(false);
        pdialog.show();
    }
}