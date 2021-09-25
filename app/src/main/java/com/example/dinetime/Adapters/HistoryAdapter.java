package com.example.dinetime.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dinetime.Models.Checkout;
import com.example.dinetime.Models.Product;
import com.example.dinetime.R;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Checkout> historylist;
    private onItemClickListner mlistner;

    public HistoryAdapter(Context mContext, ArrayList<Checkout> historylist) {
        this.mContext = mContext;
        this.historylist = historylist;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_orderhistory, parent, false);

        return new HistoryAdapter.ViewHolder(v, mlistner);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.ViewHolder holder, int position) {

        Checkout hisory = historylist.get(position);

        holder.oid.setText("OrderID: "+hisory.getOrderid());
        holder.odate.setText("Date: "+hisory.getDate());
        holder.order_name.setText("Pay.Method: "+hisory.getPaymentmethod());
        holder.order_qty.setText("Address: "+hisory.getAddress());
       // holder.order_price.setText("Total: "+hisory.getTotalprice());
        holder.order_price.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return historylist.size();
    }
    public void updateData(ArrayList<Checkout> productArrayList){
        historylist.clear();
        historylist.addAll(productArrayList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView order_delete;
        TextView oid,order_name,odate,order_qty,order_price;


        public ViewHolder(@NonNull View itemView,onItemClickListner clickListner) {
            super(itemView);

            order_delete = itemView.findViewById(R.id.order_delete);
            oid = itemView.findViewById(R.id.oid);
            odate = itemView.findViewById(R.id.odate);
            order_name = itemView.findViewById(R.id.order_name);
            order_qty = itemView.findViewById(R.id.order_qty);
            order_price = itemView.findViewById(R.id.order_price);
            order_qty = itemView.findViewById(R.id.order_qty);

            order_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (mlistner != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mlistner.onDelete(position);

                        }
                    }
                }
            });
        }
    }

    public interface onItemClickListner {
        void onDelete(int position);
    }

    public void setOnItemClickListner(onItemClickListner listner) {
        mlistner = listner;

    }
}
