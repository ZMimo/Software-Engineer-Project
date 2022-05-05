package com.example.ajz;

public class ItemModel {

    private String barcode;
    private String itemName;
    private float itemCost;

    public ItemModel(String barcode, String itemName, float itemCost) {
        this.barcode = barcode;
        this.itemName = itemName;
        this.itemCost = itemCost;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getItemName() {
        return itemName;
    }

    public float getItemCost() {
        return itemCost;
    }
}