package com.example.ajz;

import static java.lang.String.format;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MasterData_RecyclerViewAdapter extends RecyclerView.Adapter<MasterData_RecyclerViewAdapter.MyViewHolder> {

    Context context;
    ArrayList<ItemModel> itemModelList;

    public MasterData_RecyclerViewAdapter(Context context, ArrayList<ItemModel> itemModelList) {
        this.context = context;
        this.itemModelList = itemModelList;
    }

    @NonNull
    @Override
    public MasterData_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // This is where you inflate the layout and give a look to your rows
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.masterdata_row, parent, false);

        return new MasterData_RecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MasterData_RecyclerViewAdapter.MyViewHolder holder, int position) {
        // Assigning values to the views created in the master data row layout file
        // This will be based on the position of the recycler view
        holder.BarcodeView.setText(itemModelList.get(position).getBarcode());
        holder.ItemNameView.setText(itemModelList.get(position).getItemName());
        holder.ItemCostView.setText(format("$%.2f",itemModelList.get(position).getItemCost()));

    }

    @Override
    public int getItemCount() {
        // Recycler view wants to know how many items you want displayed
        return itemModelList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // This will be used to grab the views from master data row layout file

        TextView BarcodeView, ItemNameView, ItemCostView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            BarcodeView = itemView.findViewById(R.id.BarcodeView);
            ItemNameView = itemView.findViewById(R.id.ItemNameView);
            ItemCostView = itemView.findViewById(R.id.ItemCostView);
        }
    }
}
