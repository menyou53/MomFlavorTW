package com.example.momflavortw.ui.product;

public class Sliders {
    private String mName;
    private String mSlider1;
    private String mSlider2;
    private String mSlider3;

    public Sliders(String name, String slider1, String slider2, String slider3){
        if(name.trim().equals("")){
            name = "no Name";
        }
        mName = name;
        mSlider1 = slider1;
        mSlider2 = slider2;
        mSlider3 = slider3;
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


}
