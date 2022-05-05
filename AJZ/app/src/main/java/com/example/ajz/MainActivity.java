package com.example.ajz;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements AccessDialog.AccessDialogListener {

    // Initialize Data Field
    DBHelper databaseHelper;

    Button clearButton, loginButton, registerButton, viewButton;
    EditText usernameEdit, passwordEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Login Page");

        // Define Data Field
        registerButton = findViewById(R.id.RegisterButton);
        loginButton = findViewById(R.id.LoginButton);
        clearButton = findViewById(R.id.ClearButton);
        viewButton = findViewById(R.id.ViewButton);
        usernameEdit = findViewById(R.id.UsernameEdit);
        passwordEdit = findViewById(R.id.PasswordEdit);
        databaseHelper = new DBHelper(MainActivity.this);


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEdit.getText().toString();
                String password = passwordEdit.getText().toString();

                if (username.equals("") || password.equals("")) {
                    Toast.makeText(MainActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (databaseHelper.insertLoginData(username, password))
                    Toast.makeText(MainActivity.this, "New Entry Inserted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(MainActivity.this, "New Entry Not Inserted", Toast.LENGTH_SHORT).show();
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEdit.getText().toString();
                String password = passwordEdit.getText().toString();

                if (username.equals("") || password.equals("")) {
                    Toast.makeText(MainActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!databaseHelper.isValidLogin(username, password)) {
                    Toast.makeText(MainActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(MainActivity.this, "Sign in successful", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MainActivity.this, Admin_Activity.class);
                startActivity(intent);
            }
        });
        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogAdminAccess();
            }
        });
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usernameEdit.setText("");
                passwordEdit.setText("");
            }
        });
    }

    @Override
    public void verifyAdminAccess(String accessCode) {
        if (!accessCode.equals("admin123")) {
            Toast.makeText(MainActivity.this, "Access Denied", Toast.LENGTH_SHORT).show();
            return;
        }
        displayLoginInfo();
        Toast.makeText(MainActivity.this, "Access Verified", Toast.LENGTH_SHORT).show();
    }

    public void displayLoginInfo() {
        Cursor res = databaseHelper.getLoginData();
        if (res.getCount() == 0) {
            Toast.makeText(MainActivity.this, "No Entry Exists", Toast.LENGTH_SHORT).show();
            return;
        }

        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
            buffer.append("Username:\t" + res.getString(0) + "\n");
            buffer.append("Password:\t" + res.getString(1) + "\n");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(true);
        builder.setTitle("User Entries");
        builder.setMessage(buffer.toString());
        builder.show();

        Log.d(String.valueOf(buffer), "onClick: ");
    }

    // helps with interacting with the admin verification alert dialog
    public void openDialogAdminAccess() {
        AccessDialog accessDialog = new AccessDialog();
        accessDialog.show(getSupportFragmentManager(), null);
    }
}