package com.example.ajz;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MasterData_Activity extends AppCompatActivity {

    // Initialize Data Field
    RecyclerView MasterDataView;
    ArrayList<ItemModel> itemModelList = new ArrayList<>();

    DBHelper databaseHelper;

    EditText EditBarcode, EditItemName, EditItemCost;
    Button RegisterItemButton, DeleteItemButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.masterdata_activity);

        databaseHelper = new DBHelper(MasterData_Activity.this);
        EditBarcode = findViewById(R.id.EditBarcode);
        EditItemName = findViewById(R.id.EditItemName);
        EditItemCost = findViewById(R.id.EditItemCost);
        RegisterItemButton = findViewById(R.id.RegisterItemButton);
        DeleteItemButton = findViewById(R.id.DeleteItemButton);

        MasterDataView = findViewById(R.id.MasterDataView);

        setUpItemModelList();

        MasterData_RecyclerViewAdapter adapter = new MasterData_RecyclerViewAdapter(this, itemModelList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        MasterDataView.setAdapter(adapter);
        MasterDataView.setLayoutManager(layoutManager);

        RegisterItemButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (EditBarcode.getText().toString().equals("") ||
                        EditItemName.getText().toString().equals("") ||
                        EditItemCost.getText().toString().equals("")) {
                    Toast.makeText(MasterData_Activity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                String BarcodeID = EditBarcode.getText().toString();
                String ItemName = EditItemName.getText().toString();
                float ItemCost = Float.parseFloat(EditItemCost.getText().toString());

                boolean checkinsertdata = databaseHelper.insertItemData(BarcodeID, ItemName, ItemCost);
                if (checkinsertdata)
                    Toast.makeText(MasterData_Activity.this, "New Entry Inserted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(MasterData_Activity.this, "New Entry Not Inserted", Toast.LENGTH_SHORT).show();

                updateRecyclerView(adapter);
            }
        }));

        DeleteItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String BarcodeID = EditBarcode.getText().toString();

                if (BarcodeID.equals("")) {
                    Toast.makeText(MasterData_Activity.this, "Please Enter Barcode ID", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean checkinsertdata = databaseHelper.deleteItemData(BarcodeID);
                if (checkinsertdata)
                    Toast.makeText(MasterData_Activity.this, "New Entry Deleted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(MasterData_Activity.this, "New Entry Not Deleted", Toast.LENGTH_SHORT).show();

                updateRecyclerView(adapter);
            }
        });
    }

    private void setUpItemModelList() {
        itemModelList.clear();
        Cursor cursor = databaseHelper.getItemData();
        if (cursor.getCount() == 0) {
            Toast.makeText(MasterData_Activity.this, "No Entry Exists", Toast.LENGTH_SHORT).show();
            return;
        }
        while (cursor.moveToNext()) {
            String Barcode = cursor.getString(0);
            String ItemName = cursor.getString(1);
            float ItemCost = cursor.getFloat(2);
            itemModelList.add(new ItemModel(Barcode, ItemName, ItemCost));
        }
    }

    private void updateRecyclerView(MasterData_RecyclerViewAdapter adapter) {
        setUpItemModelList();

        adapter.notifyDataSetChanged();
    }
}