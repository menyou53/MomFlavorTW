package com.example.momflavortw.ui.cart;

class Cart {
    private String mName;
    private String mImageUrl;
    private int mNum;
    private int mPrice;
    private int mPriceCount;
    private int mTotal;
    private String mCapacity = "";
    private String mChoice = "";
    private String mProduct;
    public Cart(){

    }


    public Cart(String name,String imageUrl ,int num,int price){
        if(name.trim().equals("")){
            name = "no Name";
        }

        mName = name;
        mImageUrl = imageUrl;
        mNum = num;
        mPrice = price;
    }

    public String getName(){
        return mName;
    }
    public void setName(String name){
        mName = name;
    }
    public String getImageUrl(){
        return mImageUrl;
    }
    public void setImageUrl(String imageUrl){
        mImageUrl = imageUrl;
    }

    public int getNum() {
        return mNum;
    }

    public void setNum(int num) {
        mNum = num;
    }

    public void setPrice(int price) {
        mPrice = price;
    }

    public int getPrice() {
        return mPrice;
    }

    public int getPriceCount() {
        return mPriceCount;
    }

    public void setmPriceCount(int PriceCount) {
        mPriceCount = PriceCount;
    }

    public int getTotal() {
        return mTotal;
    }

    public void setTotal(int Total) {
        mTotal = Total;
    }

    public String getCapacity() {
        return mCapacity;
    }

    public void setCapacity(String capacity) {
        this.mCapacity = capacity;
    }

    public String getChoice() {
        return mChoice;
    }

    public void setChoice(String choice) {
        this.mChoice = choice;
    }

    public String getProduct() {
        return mProduct;
    }

    public void setProduct(String product) {
        this.mProduct = product;
    }
}

