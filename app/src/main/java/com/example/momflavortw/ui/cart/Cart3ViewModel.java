package com.example.momflavortw.ui.cart;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class Cart3ViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public Cart3ViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is cart3 fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
