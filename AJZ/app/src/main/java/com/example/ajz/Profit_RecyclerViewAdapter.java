package com.example.ajz;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Profit_RecyclerViewAdapter extends RecyclerView.Adapter<Profit_RecyclerViewAdapter.MyViewHolder> {

    Context context;
    ArrayList<ProfitModel> profitModels;

    public Profit_RecyclerViewAdapter(Context context, ArrayList<ProfitModel> profitModels) {
        this.context = context;
        this.profitModels = profitModels;
    }

    @NonNull
    @Override
    public Profit_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // This is where you inflate the layout and give a look to your rows
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.profitdata_row, parent, false);

        return new Profit_RecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Profit_RecyclerViewAdapter.MyViewHolder holder, int position) {
        // Assigning values to the views created in the master data row layout file
        // This will be based on the position of the recycler view
        holder.DateTimeView.setText(profitModels.get(position).getDateTime().toString());
        holder.ItemNameView.setText(profitModels.get(position).getItemName());
        holder.QuantityView.setText(String.valueOf(profitModels.get(position).getQuantity()));
        holder.ProfitView.setText(String.format("$%.2f", profitModels.get(position).getProfit()));
    }

    @Override
    public int getItemCount() {
        // Recycler view wants to know how many items you want displayed
        return profitModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // This will be used to grab the views from master data row layout file

        TextView DateTimeView, ItemNameView, QuantityView, ProfitView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            DateTimeView = itemView.findViewById(R.id.DateTimeView);
            ItemNameView = itemView.findViewById(R.id.ItemNameView);
            QuantityView = itemView.findViewById(R.id.QuantityView);
            ProfitView = itemView.findViewById(R.id.TransactionProfitView);
        }
    }

}
