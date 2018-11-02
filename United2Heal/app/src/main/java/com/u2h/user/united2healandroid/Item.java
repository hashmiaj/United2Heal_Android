package com.u2h.user.united2healandroid;

public class Item {
    private int itemID;
    private String itemName;
    private int itemQuantity;

    public Item(String itemCategory, int itemID, String itemName, int itemQuantity, String itemBox) {
        this.itemID = itemID;
        this.itemName = itemName;
        this.itemQuantity = itemQuantity;
        this.itemBox = itemBox;
    }
    public Item(int itemID,String itemName)
    {
        this.itemID=itemID;
        this.itemName=itemName;
    }

    private String itemBox;


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
