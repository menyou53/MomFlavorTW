package com.example.momflavortw.ui.notifications.history;

class UserInfo {
    private String name;
    private String email;
    private String phone;
    private String saveInfo;
    private String payment;
    private String status;
    private String account;
    private String paydate;
    private int payed;
    private int changeable;
    private String ship;
    private int shipping;
    public UserInfo(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSaveInfo() {
        return saveInfo;
    }

    public void setSaveInfo(String saveInfo) {
        this.saveInfo = saveInfo;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getPayment() {
        return payment;
    }

    public int getPayed() {
        return payed;
    }

    public void setPayed(int payed) {
        this.payed = payed;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPaydate() {
        return paydate;
    }

    public void setPaydate(String paydate) {
        this.paydate = paydate;
    }

    public void setChangeable(int changeable) {
        this.changeable = changeable;
    }

    public int getChangeable() {
        return changeable;
    }

    public String getShip() {
        return ship;
    }

    public void setShip(String ship) {
        this.ship = ship;
    }

    public int getShipping() {
        return shipping;
    }

    public void setShipping(int shipping) {
        this.shipping = shipping;
    }
}
