package com.example.rucafeandroid.model;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.HashSet;
import java.util.LinkedHashSet;

public class StoreOrdersViewModel extends ViewModel {

    private final MutableLiveData<LinkedHashSet<Order>> storeOrdersLiveData = new MutableLiveData<>();

    public MutableLiveData<LinkedHashSet<Order>> getStoreOrdersLiveData() {
        return storeOrdersLiveData;
    }

    public void setStoreOrders(LinkedHashSet<Order> storeOrders) {
        storeOrdersLiveData.setValue(storeOrders);
    }
}
