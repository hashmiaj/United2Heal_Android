package com.u2h.user.united2healandroid;

public class ExcelDataPoint {
    private String itemName;
    private double quantity;

    public ExcelDataPoint(String itemName, double quantity) {
        this.itemName = itemName;
        this.quantity = quantity;
    }
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
