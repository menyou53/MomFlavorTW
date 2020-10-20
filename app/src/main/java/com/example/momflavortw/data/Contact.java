package com.example.momflavortw.data;

public class Contact {
    private String mAddress;
    private int shipping;
    private String remittance;


    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public int getShipping() {
        return shipping;
    }

    public void setShipping(int shipping) {
        this.shipping = shipping;
    }

    public String getRemittance() {
        return remittance;
    }

    public void setRemittance(String remittance) {
        this.remittance = remittance;
    }
}
