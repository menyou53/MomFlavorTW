package com.example.momflavortw.ui.home;

class Upload {
    private String mName;
    private String mImageUrl;
    private String mProduct;

    public Upload(){

    }
    public Upload(String name, String imageUrl){
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

    public String getProduct() {
        return mProduct;
    }

    public void setProduct(String product) {
        this.mProduct = product;
    }
}


