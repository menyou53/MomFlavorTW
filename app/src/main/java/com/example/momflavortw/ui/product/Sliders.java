package com.example.momflavortw.ui.product;

public class Sliders {
    private String mName;
    private String mSlider1;
    private String mSlider2;
    private String mSlider3;
    private String mImageUrl;
    private String mSlider;
    private int mPrice;
    private String mVideoUrl="";
    private String choice;
    private boolean isChecked;
    private String mCapacity ="";
    private String narrative;
    private String markupName;
    private int markupPrice;
    private int markupNum;



    public Sliders(String name, String slider1, String slider2, String slider3, int price, String slider){
        if(name.trim().equals("")){
            name = "no Name";
        }
        mName = name;
        mSlider1 = slider1;
        mSlider2 = slider2;
        mSlider3 = slider3;
        mPrice = price;
        mSlider = slider;
    }

    public Sliders(String choice, boolean isChecked) {
        this.choice = choice;
        this.isChecked = isChecked;
    }

    public Sliders(){

}

    public String getName(){
        return mName;
    }
    public void setName(String name){
        mName = name;
    }

    public String getSlider1(){
        return mSlider1;
    }
    public void setSlider1(String slider1){
        mSlider1 = slider1;
    }

    public String getSlider2(){
        return mSlider2;
    }
    public void setSlider2(String slider2){
        mSlider2 = slider2;
    }

    public String getSlider3(){
        return mSlider3;
    }
    public void setSlider3(String slider3){
        mSlider3 = slider3;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String ImageUrl) {
        mImageUrl = ImageUrl;
    }

    public int getPrice() {
        return mPrice;
    }

    public void setPrice(int Price) {
        mPrice = Price;
    }

    public String getSlider() {
        return mSlider;
    }

    public void setSlider(String Slider) {
        mSlider = Slider;
    }

    public String getVideoUrl() {
        return mVideoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        mVideoUrl = videoUrl;
    }

    public String getChoice() {
        return choice;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }
    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getCapacity() {
        return mCapacity;
    }

    public void setCapacity(String capacity) {
        this.mCapacity = capacity;
    }

    public String getNarrative() {
        return narrative;
    }

    public void setNarrative(String narrative) {
        this.narrative = narrative;
    }

    public String getMarkupName() {
        return markupName;
    }

    public void setMarkupName(String markupName) {
        this.markupName = markupName;
    }

    public int getMarkupPrice() {
        return markupPrice;
    }

    public void setMarkupPrice(int markupPrice) {
        this.markupPrice = markupPrice;
    }

    public int getMarkupNum() {
        return markupNum;
    }

    public void setMarkupNum(int markupNum) {
        this.markupNum = markupNum;
    }
}
