package com.example.rucafeandroid.model;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.MutableLiveData;

/**
 * @author Aryan Patel
 */
public class OrderViewModel extends ViewModel {

    private final MutableLiveData<Order> orderLiveData = new MutableLiveData<>();

    /**
     *
     * @return
     */
    public MutableLiveData<Order> getOrderLiveData() {
        return orderLiveData;
    }

    /**
     *
     * @param order
     */
    public void setOrder(Order order) {
        orderLiveData.setValue(order);
    }
}
