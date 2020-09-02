package com.example.momflavortw.ui.cart;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class Cart2ViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public Cart2ViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is cart2 fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
