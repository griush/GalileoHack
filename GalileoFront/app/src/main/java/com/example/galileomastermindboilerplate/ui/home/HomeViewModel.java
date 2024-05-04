package com.example.galileomastermindboilerplate.ui.home;

import android.widget.Switch;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    public final MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Live satellite data: ");
    }

    public LiveData<String> getText() {
        return mText;
    }
}