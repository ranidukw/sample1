package com.example.dinetime.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.dinetime.R;

public class AdminHome extends AppCompatActivity {

    CardView coffee_shop,bakery_shop;
    public static String shoptype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        SetupUI();
        ButtonClicks();
    }

    private void SetupUI(){

        coffee_shop = findViewById(R.id.coffee_shop);
        bakery_shop = findViewById(R.id.bakery_shop);
    }

    private void ButtonClicks(){

        coffee_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shoptype ="coffee";
                Intent intent = new Intent(AdminHome.this,Admin_Shop.class);
                startActivity(intent);
            }
        });

        bakery_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shoptype="bakery";
                Intent intent = new Intent(AdminHome.this,Admin_Shop.class);
                startActivity(intent);
            }
        });
    }
}