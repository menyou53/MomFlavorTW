package com.example.momflavortw.ui.notifications.history;

class Purchased {
    private String mName;
    private String mDate;
    private String mImageUrl;
    private int mNum;
    private int mPrice;
    private int mTotal;
    private String product;
    private boolean cancelProposed;
    public Purchased(){

    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setPrice(int price) {
        mPrice = price;
    }

    public int getPrice() {
        return mPrice;
    }

    public void setNum(int num) {
        mNum = num;
    }

    public int getNum() {
        return mNum;
    }

    public int getTotal() {
        return mTotal;
    }

    public void setTotal(int total) {
        mTotal = total;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public boolean isCancelProposed() {
        return cancelProposed;
    }

    public void setCancelProposed(boolean cancelProposed) {
        this.cancelProposed = cancelProposed;
    }
}

