package com.example.dinetime.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dinetime.Models.Product;
import com.example.dinetime.R;

import java.util.ArrayList;

public class ShopView_adapter extends RecyclerView.Adapter<ShopView_adapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Product> hitsList;
    private onItemClickListner mlistner;

    public ShopView_adapter(Context mContext, ArrayList<Product> hitsList) {
        this.mContext = mContext;
        this.hitsList = hitsList;
    }

    @Override
    public ShopView_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_productview_only, parent, false);

        return new ShopView_adapter.ViewHolder(v, mlistner);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopView_adapter.ViewHolder holder, int position) {

        Product product = hitsList.get(position);

        holder.product_name.setText(product.getPname());
        holder.product_price.setText(product.getPprice());

        Glide.with(mContext)
                .load(product.getImgeurl())
                .centerCrop()
                .into(holder.product_image);
    }

    @Override
    public int getItemCount() {

        return hitsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView product_name, product_price;
        ImageView order_delete, product_image;
        CardView cardview;

        public ViewHolder(@NonNull  View itemView, onItemClickListner clickListener) {
            super(itemView);

            product_name = itemView.findViewById(R.id.product_name);
            product_price = itemView.findViewById(R.id.product_price);
            order_delete = itemView.findViewById(R.id.order_delete);
            product_image = itemView.findViewById(R.id.product_image);
            cardview = itemView.findViewById(R.id.cardview);

            cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (mlistner != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mlistner.onCardClick(position);

                        }
                    }
                }
            });
        }
    }

    public interface onItemClickListner {
        void onCardClick(int position);
    }

    public void setOnItemClickListner(onItemClickListner listner) {
        mlistner = listner;

    }
}
