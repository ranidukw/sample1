package com.example.dinetime.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dinetime.Activity.Admin_Shop;
import com.example.dinetime.Models.Cart;
import com.example.dinetime.Models.Product;
import com.example.dinetime.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class ProductDiscription_Fragment extends Fragment {

    ImageView imageview;
    ProgressBar progress_circular;
    TextView p_name,p_price,tprice,txtItemQty;
    ImageButton imgBtnQtyMinus,imgBtnQtyPlus;
    AppCompatButton add;
    private int userOrderdQty=0;
    private float eachprice;
    float total,totalprice;
    DatabaseReference dbRef;
    float pprice;
    private String imgUrl;
    public ProductDiscription_Fragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_product_discription_, container, false);

        View v = inflater.inflate(R.layout.fragment_product_discription_, container, false);
        SetUpUI(v);
        dataSet();
        QtyChange();
        AddtoCart();
        return v;
    }

    private void SetUpUI(View v){

        imageview = v.findViewById(R.id.imageview);
        p_name = v.findViewById(R.id.p_name);
        p_price = v.findViewById(R.id.p_price);
        tprice = v.findViewById(R.id.tprice);
        txtItemQty = v.findViewById(R.id.txtItemQty);
        imgBtnQtyMinus = v.findViewById(R.id.imgBtnQtyMinus);
        imgBtnQtyPlus = v.findViewById(R.id.imgBtnQtyPlus);
        add = v.findViewById(R.id.add);
        progress_circular = v.findViewById(R.id.progress_circular);
        txtItemQty.setText("1");
        tprice.setText("");
        dbRef = FirebaseDatabase.getInstance().getReference().child("CartItems");
    }

    private void dataSet(){

        Bundle bundle = getArguments();
        Product item = (Product) bundle.getSerializable("productItem");
        String cat = item.getCategory();
        eachprice = item.getPrice();
        imgUrl = item.getImgeurl();
        progress_circular.setVisibility(View.GONE);

        p_name.setText(item.getPname());
        p_price.setText(item.getPprice());
        tprice.setText("LKR "+eachprice);

        Glide.with(getContext())
                .load(item.getImgeurl())
                .placeholder(R.drawable.progress_bar)
                .into(imageview);
    }

    private void QtyChange(){

        imgBtnQtyPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ++userOrderdQty;
                txtItemQty.setText(String.valueOf(userOrderdQty));
                Log.e("Clicked +","");
                 total = eachprice * userOrderdQty;
                tprice.setText("LKR"+total);
                Log.e("Check Totalprice",""+total);
            }
        });

        imgBtnQtyMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userOrderdQty > 0) {
                    --userOrderdQty;
                    txtItemQty.setText(String.valueOf(userOrderdQty));
                    Log.e("Clicked -","");
                     total = eachprice * userOrderdQty;
                    tprice.setText("LKR"+total);
                    Log.e("Check Totalprice",""+total);
                }
            }
        });



    }

    private void AddtoCart(){

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String productname = p_name.getText().toString();
                String product_price = p_price.getText().toString();
                try {
                    pprice = Float.parseFloat(p_price.getText().toString());
                }
                catch (Exception e){

                }
                if(productname.isEmpty() || product_price.isEmpty()){

                    Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
                else {
                    Cart cart = new Cart(productname,product_price,eachprice,imgUrl,total,"LKR "+total);
                    dbRef.push().setValue(cart).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            CompleteAlert();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            faileAlert();
                        }
                    });
                }
            }
        });
    }

    private void CompleteAlert(){

        SweetAlertDialog pdialog = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE);
        pdialog.setTitleText("Complete");
        pdialog.setContentText("Item Added to cart Successfully");
        pdialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {

                sweetAlertDialog.dismissWithAnimation();
                Fragment fragment = new CartFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer,fragment)
                        .commit();
            }
        });
        pdialog.setCanceledOnTouchOutside(false);
        pdialog.show();
    }

    private void faileAlert(){
        SweetAlertDialog pdialog = new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE);
        pdialog.setTitleText("Incomplete");
        pdialog.setContentText("Something went Wrong!");
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