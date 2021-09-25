package com.example.dinetime.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.dinetime.Adapters.CartAdapter;
import com.example.dinetime.Adapters.HistoryAdapter;
import com.example.dinetime.Models.Cart;
import com.example.dinetime.Models.Checkout;
import com.example.dinetime.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class HistoryFragment extends Fragment {

    RecyclerView history_recycler;
    ProgressBar progressBar;
    HistoryAdapter adapter;
    DatabaseReference databaseReference;
    ArrayList<Checkout> historyArrayList;
    ArrayList<String> keylist;
    String key;
    public HistoryFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_history, container, false);

        View v = inflater.inflate(R.layout.fragment_history, container, false);
        SetupUI(v);
        HistoryDisplay();
        return v;
    }
    private void SetupUI(View v){
        history_recycler = v.findViewById(R.id.history_recycler);
        progressBar =v.findViewById(R.id.progress_bar);

        historyArrayList = new ArrayList<>();
        keylist = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("Payments");
    }

    private void HistoryDisplay(){

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot data : snapshot.getChildren()) {
                    progressBar.setVisibility(View.GONE);
                    Checkout product = data.getValue(Checkout.class);
                    historyArrayList.add(product); // user details added to user list
                    Log.e("namecheck", "" + product.getOrderid());
                    Log.e("checkkey", "" + data.getKey());
                    Log.e("checkkey", "" + data.getRef());
                    keylist.add(data.getKey()); //adding keys to different list - path key


                }

                putDataIntoRecyclerView(getContext(), historyArrayList);
                Log.e("listcheck", "" + historyArrayList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void putDataIntoRecyclerView(Context context, ArrayList<Checkout> historylist) {
        Log.e("Listsize_check", "" + historylist.size());
        if (historylist.size() == 0) {
            Toast.makeText(context, "No Data to Display", Toast.LENGTH_SHORT).show();
        } else {
            adapter = new HistoryAdapter(getActivity(), historylist);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            history_recycler.setLayoutManager(layoutManager);
            history_recycler.setAdapter(adapter);

            adapter.setOnItemClickListner(new HistoryAdapter.onItemClickListner() {
                @Override
                public void onDelete(int position) {
                    Checkout dta = historylist.get(position);
                    String key = keylist.get(position);
                    //Log.e("checkpostion",""+dta.getPname());
                    Log.e("CheckKey", "" + key);
                    databaseReference = FirebaseDatabase.getInstance().getReference("Payments").child(key);
                    databaseReference.removeValue();
                    DeleteComplete();
                    adapter.updateData(historylist);
                }
            });


        }
    }
    private void DeleteComplete() {
        SweetAlertDialog pdialog = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE);
        pdialog.setTitleText("Completed");
        pdialog.setContentText("Order History Item Deleted Successfully");
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