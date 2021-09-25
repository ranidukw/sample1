package com.example.dinetime.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.dinetime.Activity.Login;
import com.example.dinetime.R;
import com.google.firebase.auth.FirebaseAuth;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class HomeFragment extends Fragment {

    AppCompatButton logout;
    AlertDialog.Builder builder;
    CardView coffee_shop,bakery_shop;
    public static String shoptype;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_home, container, false);

        View v  = inflater.inflate(R.layout.fragment_home, container, false);
        SetupUI(v);
        ButtonClicks();

        return v;
    }

    private void SetupUI(View v){

        logout = v.findViewById(R.id.logout);
        coffee_shop = v.findViewById(R.id.coffee_shop);
        bakery_shop = v.findViewById(R.id.bakery_shop);
        builder = new AlertDialog.Builder(getContext());
    }

    private void ButtonClicks(){

        coffee_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shoptype = "coffee";
                Fragment fragment = new ShopFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer,fragment)
                        .commit();
            }
        });

        bakery_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shoptype = "bakery";
                Fragment fragment = new ShopFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer,fragment)
                        .commit();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askLogout();
            }

            private void askLogout() {
                SweetAlertDialog pdialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE);
                pdialog.setTitleText("Logout");
                pdialog.setContentText("Are you sure");
                pdialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(getActivity(), Login.class);
                        startActivity(intent);
                        sweetAlertDialog.dismissWithAnimation();

                    }
                });
                pdialog.setCancelButton("No", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                        pdialog.dismissWithAnimation();
                    }
                });
                pdialog.setCanceledOnTouchOutside(false);
                pdialog.show();
            }

        });
    }
}