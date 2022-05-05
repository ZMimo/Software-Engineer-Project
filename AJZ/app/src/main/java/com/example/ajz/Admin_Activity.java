package com.example.ajz;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class Admin_Activity extends AppCompatActivity implements AccessDialog.AccessDialogListener {

    // Initialize Data Field
    boolean accessGranted;

    DBHelper databaseHelper;

    Button Profit, ScanItem, ViewInventory, ViewMasterData;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity);

        // Define Data Field
        Profit = findViewById(R.id.ViewProfitButton);
        ScanItem = findViewById(R.id.ScanItemButton);
        ViewInventory = findViewById(R.id.ViewInventoryButton);
        ViewMasterData = findViewById(R.id.ViewMasterDataButton);
        databaseHelper = new DBHelper(Admin_Activity.this);

        Profit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Admin_Activity.this, Profit_Activity.class);
                startActivity(intent);
            }
        });
        ScanItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Admin_Activity.this, Camera.class);
                startActivity(intent);
            }
        });
        ViewMasterData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogAdminAccess();
            }
        });
        ViewInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor res = databaseHelper.getItemData();
                if (res.getCount() == 0) {
                    Toast.makeText(Admin_Activity.this, "No Entry Exists", Toast.LENGTH_SHORT).show();
                    return;
                }
                StringBuffer buffer = new StringBuffer();
                AlertDialog.Builder builder = new AlertDialog.Builder(Admin_Activity.this);
                while (res.moveToNext()) {
                    buffer.append("Barcode:\t\t" + res.getString(0) + "\n");
                    buffer.append("Quantity:\t\t" + res.getInt(3) + "\n");
                }
                builder.setCancelable(true);
                builder.setTitle("Inventory");
                builder.setMessage(buffer.toString());
                builder.show();
                Log.d(String.valueOf(buffer), "onClick: ");
            }
        });
    }

    @Override
    public void verifyAdminAccess(String accessCode) {
        if (accessCode.equals("admin123")) {
            accessGranted = true;
            Intent intent = new Intent(Admin_Activity.this, MasterData_Activity.class);
            startActivity(intent);

            Toast.makeText(Admin_Activity.this, "Access Verified", Toast.LENGTH_SHORT).show();
        } else {
            accessGranted = false;
            Toast.makeText(Admin_Activity.this, "Access Denied", Toast.LENGTH_SHORT).show();
        }
    }

    public void openDialogAdminAccess() {
        AccessDialog accessDialog = new AccessDialog();
        accessDialog.show(getSupportFragmentManager(), null);
    }

}
