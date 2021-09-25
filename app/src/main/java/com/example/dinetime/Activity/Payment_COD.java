package com.example.dinetime.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dinetime.Models.Checkout;
import com.example.dinetime.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Payment_COD extends AppCompatActivity {

    EditText name,mobile,address;
    AppCompatButton edit_view,btn_checkout,update_view;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    FirebaseUser user;
    String userID,totalprice;
    DocumentReference documentReference;
    DatabaseReference dbRef;
    String paymenthod;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_cod);

        SetUpUI();
        GetDataIntent();
        btnClicks();
        getUserDetails();



    }

    private void GetDataIntent(){

        Intent i = getIntent();
        paymenthod = i.getStringExtra("paymethod");
        totalprice = i.getStringExtra("total");
        Log.e("checkpaymethod",""+paymenthod);
        Log.e("Checktotal",""+totalprice);
    }

    private void SetUpUI(){

        name = findViewById(R.id.name);
        mobile = findViewById(R.id.mobile);
        address = findViewById(R.id.address);
        edit_view = findViewById(R.id.edit_view);
        update_view = findViewById(R.id.update_view);
        update_view.setVisibility(View.INVISIBLE);
        btn_checkout = findViewById(R.id.btn_checkout);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();


        userID = firebaseAuth.getCurrentUser().getUid();
        user = firebaseAuth.getCurrentUser();
        documentReference = firestore.collection("users").document(userID);
        dbRef = FirebaseDatabase.getInstance().getReference().child("Payments");

    }

    private void btnClicks(){

        edit_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                update_view.setVisibility(View.VISIBLE);
                name.setEnabled(true);
                mobile.setEnabled(true);
            }
        });

        update_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUserDetails();
                Log.e("UpdatebtnClicked","");


            }
        });
        btn_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddCheckout();
            }
        });

    }

    private void getUserDetails(){

        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                name.setText(value.getString("Name"));
                try {
                    mobile.setText(value.get("Mobile").toString());
                }
                catch (Exception e){
                    Log.e("CheckmobileInt",""+e);
                }
            }
        });
    }

    private void updateUserDetails(){

        // updating the firestore profile details
        final DocumentReference documentReference1 = FirebaseFirestore.getInstance().collection("users").document(userID);
        Map<String, Object> map = new HashMap<>();
        map.put("Name", name.getText().toString());
        map.put("Mobile", mobile.getText().toString());

        documentReference1.update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(Payment_COD.this, "UserDetails updated", Toast.LENGTH_SHORT).show();
                update_view.setVisibility(View.GONE);
                name.setEnabled(false);
                mobile.setEnabled(false);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Payment_COD.this, "Something Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void AddCheckout(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd_HH:mm:ss", Locale.getDefault());
        String uname = name.getText().toString();
        String umobile = mobile.getText().toString();
        String uAddress= address.getText().toString();
        String date = sdf.format(new Date());
        String orderid = "OID"+UUID.randomUUID().toString();
        String tot = totalprice;
        Log.e("Checktotlprice",""+totalprice);


        if(uname.isEmpty() || umobile.isEmpty() || uAddress.isEmpty()){
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        }
        else {
            Checkout payments = new Checkout(orderid,date,tot,uname,umobile,uAddress,paymenthod);
            dbRef.push().setValue(payments).addOnCompleteListener(new OnCompleteListener<Void>() {
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



    private void CompleteAlert(){

        SweetAlertDialog pdialog = new SweetAlertDialog(Payment_COD.this, SweetAlertDialog.SUCCESS_TYPE);
        pdialog.setTitleText("Complete");
        pdialog.setContentText("Order Confirmed Successful");
        pdialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                DeleteCart();
                Intent i = new Intent(Payment_COD.this, Activity_Bottom_navigation.class);
                startActivity(i);
            }
        });
        pdialog.setCanceledOnTouchOutside(false);
        pdialog.show();
    }

    private void faileAlert(){
        SweetAlertDialog pdialog = new SweetAlertDialog(Payment_COD.this, SweetAlertDialog.ERROR_TYPE);
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

    private void DeleteCart(){
        databaseReference = FirebaseDatabase.getInstance().getReference("CartItems");
        databaseReference.removeValue();
    }
}