package com.u2h.user.united2healandroid;

public class BoxStatsDataPoint {
    private String itemName;
    private int quantity;
    private  String expirationDate;
    public BoxStatsDataPoint(String itemName, int quantity, String expirationDate) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.expirationDate=expirationDate;
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

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }
}
