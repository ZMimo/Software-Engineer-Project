package com.example.ajz;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Profit_Activity extends AppCompatActivity {

    // Initialize Data Field
    RecyclerView ProfitDataView;
    ArrayList<ProfitModel> profitModels = new ArrayList<>();

    DBHelper databaseHelper;

    TextView TotalProfitView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profit_activity);

        ProfitDataView = findViewById(R.id.ProfitView);
        TotalProfitView = findViewById(R.id.TotalProfitView);

        databaseHelper = new DBHelper(Profit_Activity.this);

        setUpProfitModels();

        Profit_RecyclerViewAdapter adapter = new Profit_RecyclerViewAdapter(this, profitModels);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        ProfitDataView.setAdapter(adapter);
        ProfitDataView.setLayoutManager(layoutManager);

        TotalProfitView.setText(String.format("$%.2f", getTotalProfit()));
    }

    @SuppressLint("NewApi")
    private void setUpProfitModels() {
        profitModels.clear();
        Cursor cursor = databaseHelper.getProfitData();
        if (cursor.getCount() == 0) {
            Toast.makeText(Profit_Activity.this, "No Entry Exists", Toast.LENGTH_SHORT).show();
            return;
        }
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss");
        while (cursor.moveToNext()) {
            LocalDateTime DateTime = LocalDateTime.parse(cursor.getString(0),format);
            String ItemName = cursor.getString(2);
            int Quantity = cursor.getInt(3);
            float Profit = cursor.getFloat(4);
            profitModels.add(new ProfitModel(DateTime, ItemName, Quantity, Profit));
        }
    }

    private float getTotalProfit() {
        float totalProfit = 0;

        Cursor cursor = databaseHelper.getProfitData();
        if (cursor.getCount() == 0) {
            return totalProfit;
        }

        while (cursor.moveToNext()) {
            totalProfit += cursor.getFloat(4);
        }
        return totalProfit;
    }
}
