package com.example.ajz;


import java.time.LocalDateTime;


public class ProfitModel {

    private LocalDateTime dateTime;
    private String itemName;
    private int quantity;
    private float profit;

    public ProfitModel(LocalDateTime dateTime, String itemName, int quantity, float profit) {
        this.dateTime = dateTime;
        this.itemName = itemName;
        this.quantity = quantity;
        this.profit = profit;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getItemName() {
        return itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public float getProfit() { return profit; }

    public void setProfit(float profit) {
        this.profit = profit;
    }

}
