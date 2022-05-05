package com.example.ajz;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;

import com.google.zxing.Result;

public class Camera extends AppCompatActivity {

    // Initialize Data Field
    private CodeScanner mCodeScanner;
    private static final int MY_CAMERA_REQUEST_CODE = 100;

    DBHelper databaseHelper;

    TextView BarcodeView;
    EditText QuantityEdit;
    Button AddButton, DeleteButton, CheckoutButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_main);

        // Define Data Field
        BarcodeView = findViewById(R.id.ScannedBarcodeText);
        QuantityEdit = findViewById(R.id.QuantityEdit);
        AddButton = findViewById(R.id.AddButton);
        DeleteButton = findViewById(R.id.DeleteButton);
        CheckoutButton = findViewById(R.id.CheckoutButton);
        databaseHelper = new DBHelper(Camera.this);

        if (ContextCompat.checkSelfPermission(Camera.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Camera.this, new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
        }
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String barcode = result.getText();
                        Toast.makeText(Camera.this, barcode, Toast.LENGTH_SHORT).show();
                        BarcodeView.setText(barcode);

                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });

        AddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkValidCameraInput())
                    return;

                int quantity = Integer.parseInt(QuantityEdit.getText().toString());

                boolean checkinsertdata = databaseHelper.addItem(BarcodeView.getText().toString(), quantity);
                if (checkinsertdata)
                    Toast.makeText(Camera.this, "New Entry Inserted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(Camera.this, "New Entry Not Inserted", Toast.LENGTH_SHORT).show();
            }
        });
        DeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkValidCameraInput())
                    return;

                int quantity = Integer.parseInt(QuantityEdit.getText().toString());

                boolean checkdeletedata = databaseHelper.removeItem(BarcodeView.getText().toString(), quantity);
                if (checkdeletedata)
                    Toast.makeText(Camera.this, "New Entry Deleted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(Camera.this, "New Entry Not Deleted", Toast.LENGTH_SHORT).show();

            }
        });
        CheckoutButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                if (!checkValidCameraInput())
                    return;

                int quantity = Integer.parseInt(QuantityEdit.getText().toString());

                boolean checkdeletedata = databaseHelper.removeItem(BarcodeView.getText().toString(), quantity);
                boolean checkrecordtransaction = databaseHelper.insertTransactionData("Checkout", BarcodeView.getText().toString(), quantity);
                if (checkdeletedata && checkrecordtransaction)
                    Toast.makeText(Camera.this, "New Entry Checked Out", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(Camera.this, "New Entry Not Checked Out", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();

    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    private boolean checkValidCameraInput() {
        if (!databaseHelper.isBarcodeMatch(BarcodeView.getText().toString())) {
            Toast.makeText(Camera.this, "Invalid Barcode", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (QuantityEdit.getText().toString().equals("") || Integer.parseInt(QuantityEdit.getText().toString()) <= 0) {
            Toast.makeText(Camera.this, "Please enter a quantity greater than 0", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}