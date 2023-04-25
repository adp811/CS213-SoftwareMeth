package com.example.rucafeandroid.model;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.LinkedHashSet;

/**
 * @author Aryan Patel
 */
public class StoreOrdersViewModel extends ViewModel {

    private final MutableLiveData<LinkedHashSet<Order>> storeOrdersLiveData = new MutableLiveData<>();

    /**
     *
     * @return
     */
    public MutableLiveData<LinkedHashSet<Order>> getStoreOrdersLiveData() {
        return storeOrdersLiveData;
    }

    /**
     *
     * @param storeOrders
     */
    public void setStoreOrders(LinkedHashSet<Order> storeOrders) {
        storeOrdersLiveData.setValue(storeOrders);
    }
}
