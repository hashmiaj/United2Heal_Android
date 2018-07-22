package com.u2h.user.united2healandroid;

public class Item {
    private String itemCategory;
    private int itemID;
    private String itemName;
    private int itemQuantity;

    public Item(String itemCategory, int itemID, String itemName, int itemQuantity, String itemBox) {
        this.itemCategory = itemCategory;
        this.itemID = itemID;
        this.itemName = itemName;
        this.itemQuantity = itemQuantity;
        this.itemBox = itemBox;
    }

    private String itemBox;

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(int itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public String getItemBox() {
        return itemBox;
    }

    public void setItemBox(String itemBox) {
        this.itemBox = itemBox;
    }
}
