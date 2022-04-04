package com.example.ajz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;

 class Admin_Activity extends AppCompatActivity {

    Button AddItem,DeleteItem,CheckItem,CheckoutItem;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity);
        AddItem = findViewById(R.id.AddItem);
        DeleteItem = findViewById(R.id.RemoveItem);
        CheckItem = findViewById(R.id.CheckItem);
        CheckoutItem = findViewById(R.id.CheckoutItem);

    }

   public void AddItemStatus(View v){

   }

   public void DeleteItemStatus(View v){

   }

   public void ViewItemStatus(View v){

   }

   public void CheckOutItemStatus(View v){

   }



}
