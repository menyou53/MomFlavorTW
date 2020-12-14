package com.example.momflavortw.data;

import com.example.momflavortw.ui.product.Sliders;

public class Common {
    public static Sliders currentItem = null;
    public static String choiceItem = "";
    public static boolean msgAlet;
    public static boolean newMsg = false;


    public String getChoiceItem() {
        return choiceItem;
    }

    public void setChoiceItem(String choiceItem) {
        this.choiceItem = choiceItem;
    }

    public boolean isMsgAlet() {
        return msgAlet;
    }

    public  void setMsgAlet(boolean msgAlet) {
        Common.msgAlet = msgAlet;
    }

    public boolean isNewMsg() {
        return newMsg;
    }

    public void setNewMsg(boolean newMsg) {
        Common.newMsg = newMsg;
    }
}
