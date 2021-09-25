package com.example.dinetime.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.dinetime.Models.Cart;
import com.example.dinetime.R;

public class PaymentSelect extends AppCompatActivity {

    RadioButton radio1,radio2; //radio1-cashondelivery,radio2-pickup
    String paymenthod;
    AppCompatButton btn_checkout;
    String CartTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_select);

        radio1 = findViewById(R.id.radio1);
        radio2 = findViewById(R.id.radio2);
        btn_checkout = findViewById(R.id.btn_checkout);

        SetLocation();
        GetIntent();
    }

    private void GetIntent(){

        Intent i = getIntent();
        CartTotal = i.getStringExtra("totalprice");
    }


    private void SetLocation(){

        btn_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(radio1.isChecked()){
                    paymenthod = "CashOnDelivery";
                    Intent i = new Intent(PaymentSelect.this, Payment_COD.class);
                    i.putExtra("paymethod",paymenthod);
                    i.putExtra("total",CartTotal);
                    Log.e("Checkpaymethod",""+paymenthod);
                    startActivity(i);
                }
                else if(radio2.isChecked()){
                    paymenthod = "PickUp";
                    Intent i = new Intent(PaymentSelect.this, Payment_Pickup.class);
                    i.putExtra("paymethod",paymenthod);
                    i.putExtra("total",CartTotal);
                    Log.e("Checkpaymethod",""+paymenthod);
                    startActivity(i);
                }
                else{

                }
            }
        });
    }
}