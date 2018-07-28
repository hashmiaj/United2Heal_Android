package com.u2h.user.united2healandroid;

public class BoxStatsDataPoint {
    private String itemName;
    private int quantity;

    public BoxStatsDataPoint(String itemName, int quantity) {
        this.itemName = itemName;
        this.quantity = quantity;
    }
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
