package com.example.rucafeandroid.model;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.MutableLiveData;

public class OrderViewModel extends ViewModel {

    private final MutableLiveData<Order> orderLiveData = new MutableLiveData<>();

    public MutableLiveData<Order> getOrderLiveData() {
        return orderLiveData;
    }

    public void setOrder(Order order) {
        orderLiveData.setValue(order);
    }
}
