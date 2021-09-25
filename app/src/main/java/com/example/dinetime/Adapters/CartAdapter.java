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
import com.example.dinetime.Models.Cart;
import com.example.dinetime.Models.Product;
import com.example.dinetime.R;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Cart> cartlist;
    private onItemClickListner mlistner;

    public CartAdapter(Context mContext, ArrayList<Cart> cartlist) {
        this.mContext = mContext;
        this.cartlist = cartlist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_productview_edit, parent, false);

        return new CartAdapter.ViewHolder(v, mlistner);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {
        Cart cart = cartlist.get(position);

        holder.product_name.setText(cart.getProductname());
        holder.product_price.setText(cart.getDispalyTotal());

        Glide.with(mContext)
                .load(cart.getImageurl())
                .centerCrop()
                .placeholder(R.drawable.progress_bar)
                .into(holder.product_image);
    }

    @Override
    public int getItemCount() {
        return cartlist.size();
    }

    public void updateData(ArrayList<Cart> cartArrayList){
        cartlist.clear();
        cartlist.addAll(cartArrayList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView product_name, product_price;
        ImageView order_delete, product_image;
        CardView cardview;

        public ViewHolder(@NonNull View itemView,onItemClickListner clickListener) {
            super(itemView);
            product_name = itemView.findViewById(R.id.product_name);
            product_price = itemView.findViewById(R.id.product_price);
            order_delete = itemView.findViewById(R.id.order_delete);
            product_image = itemView.findViewById(R.id.product_image);
            cardview = itemView.findViewById(R.id.cardview);

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
