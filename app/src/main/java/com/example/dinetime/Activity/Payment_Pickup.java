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
import java.util.Locale;
import java.util.UUID;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Payment_Pickup extends AppCompatActivity {

    EditText name,mobile,address;
    AppCompatButton btn_checkout;

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
        setContentView(R.layout.activity_payment_pickup);

        SetUpUI();
        GetDataIntent();
        getUserDetails();
        AddCheckout();
    }

    private void SetUpUI(){

        name = findViewById(R.id.name);
        mobile = findViewById(R.id.mobile);
        address = findViewById(R.id.address);
        btn_checkout = findViewById(R.id.btn_checkout);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        Intent i = getIntent();
        totalprice = i.getStringExtra("total");

        userID = firebaseAuth.getCurrentUser().getUid();
        user = firebaseAuth.getCurrentUser();
        documentReference = firestore.collection("users").document(userID);
        dbRef = FirebaseDatabase.getInstance().getReference().child("Payments");

    }

    private void GetDataIntent(){

        Intent i = getIntent();
        paymenthod = i.getStringExtra("paymethod");
        totalprice = i.getStringExtra("total");
        Log.e("checkpaymethod",""+paymenthod);
        Log.e("checkpaymethod",""+totalprice);
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

    private void AddCheckout(){

        btn_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd_HH:mm:ss", Locale.getDefault());
                String uname = name.getText().toString();
                String umobile = mobile.getText().toString();
                String uAddress= address.getText().toString();
                String date = sdf.format(new Date());
                String orderid = "OID"+ UUID.randomUUID().toString();
                String tot = totalprice;
                Log.e("Checktotlprice",""+totalprice);


                if(uname.isEmpty() || umobile.isEmpty() || uAddress.isEmpty()){
                    Toast.makeText(Payment_Pickup.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
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
                            failedAlert();
                        }
                    });
                }
            }
        });



    }

    private void CompleteAlert(){

        SweetAlertDialog pdialog = new SweetAlertDialog(Payment_Pickup.this, SweetAlertDialog.SUCCESS_TYPE);
        pdialog.setTitleText("Complete");
        pdialog.setContentText("Order Confirmed Successful");
        pdialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                DeleteCart();
                Intent i = new Intent(Payment_Pickup.this, Activity_Bottom_navigation.class);
                startActivity(i);
            }
        });
        pdialog.setCanceledOnTouchOutside(false);
        pdialog.show();
    }

    private void failedAlert(){
        SweetAlertDialog pdialog = new SweetAlertDialog(Payment_Pickup.this, SweetAlertDialog.ERROR_TYPE);
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