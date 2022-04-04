package com.example.ajz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    Button Clear,Login1;
    EditText Username1, Password1;


    String Password = "123";
    String User = "admin";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Login1 = findViewById(R.id.Login1);
        Clear = findViewById(R.id.Clear);
        Username1 = findViewById(R.id.Username1);
        Password1 = findViewById(R.id.Password1);

    }




    public void HandleOnClick(View view){
        Intent intent = new Intent(MainActivity.this,Admin_Activity.class);

        if (Username1.getText().toString() == User & Password1.getText().toString() == Password) {

            startActivity(intent);
        }
        else{
            Log.d("Tag", String.valueOf(Username1));
            Log.d("Tag", String.valueOf(User));
            Toast.makeText(this, "Incorrect Login!", Toast.LENGTH_SHORT).show();

        }

    }



}


