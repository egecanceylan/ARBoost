package com.oneandonly.arboost.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.oneandonly.arboost.R;
import com.oneandonly.arboost.models.TransactionModel;
import com.oneandonly.arboost.view.ArActivity;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.Holder> {

    private ArrayList<TransactionModel> models;
    private ArActivity arActivity;

    public RecyclerAdapter(ArrayList<TransactionModel> models, ArActivity arActivity) {
        this.models = models;
        this.arActivity = arActivity;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.sample_layout, null);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        TransactionModel model = models.get(position);
        holder.textViewStore.setText(model.getStore());
        holder.textViewAmount.setText(String.valueOf(model.getTotalAmount()));
        String Sector = model.getSector();
        switch (Sector) {
            case "Coffee":
                holder.imageView.setImageResource(R.drawable.coffee_icon);
                break;
            case "Clothing":
                holder.imageView.setImageResource(R.drawable.clothing_icon);
                break;
            case "Gas":
                holder.imageView.setImageResource(R.drawable.gasstation);
                break;
            case "Company":
                holder.imageView.setImageResource(R.drawable.organization_icon);
                break;
            case "Personal":
                holder.imageView.setImageResource(R.drawable.person);
                break;
            case "Communication":
                holder.imageView.setImageResource(R.drawable.phone_icon);
                break;
            case "Food":
                holder.imageView.setImageResource(R.drawable.restaurant_icon);
                break;
            case "Bills":
                holder.imageView.setImageResource(R.drawable.bill_icon);
                break;
            case "Accessories":
                holder.imageView.setImageResource(R.drawable.shop_icon);
                break;
            case "Market":
                holder.imageView.setImageResource(R.drawable.shopping_icon);
                break;
            default:
                holder.imageView.setImageResource(R.drawable.person);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        TextView textViewStore;
        TextView textViewAmount;
        ImageView imageView;

        public Holder(@NonNull View itemView) {
            super(itemView);

            textViewStore = itemView.findViewById(R.id.sample_layout_store);
            textViewAmount = itemView.findViewById(R.id.sample_layout_amount);
            imageView = itemView.findViewById(R.id.card_icon);;
        }
    }
}
