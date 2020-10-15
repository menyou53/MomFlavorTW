package com.example.momflavortw.ui.image;

public class Upload {
    private String mName;
    private String mImageUrl;
    private String mCapacity = "";
    private String mProduct;
    private String videoUrl = "";
    private int stock;
    private int price;
    private int markup = 0;

    public Upload(){

    }
    public Upload(String name,String imageUrl){
        if(name.trim().equals("")) {
            name = "no Name";
        }
        mName = name;
        mImageUrl = imageUrl;
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

    public String getCapacity() {
        return mCapacity;
    }

    public void setCapacity(String capacity) {
        this.mCapacity = capacity;
    }

    public String getProduct() {
        return mProduct;
    }

    public void setProduct(String product) {
        this.mProduct = product;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public int getMarkup() {
        return markup;
    }

    public void setMarkup(int markup) {
        this.markup = markup;
    }


}


