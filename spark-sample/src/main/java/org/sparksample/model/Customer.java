package org.sparksample.model;

import org.sparksample.classifiers.AmountClassifier;
import org.sparksample.classifiers.FrequencyClassifier;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by achumakov on 4/19/2015.
 */
public class Customer implements Serializable {

    private String id;

    private Integer ordersCount;

    private BigDecimal orderAmount;

    public Customer(String id, Integer ordersCount, BigDecimal orderAmount) {
        this.id = id;
        this.ordersCount = ordersCount;
        this.orderAmount = orderAmount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getOrdersCount() {
        return ordersCount;
    }

    public void setOrdersCount(Integer ordersCount) {
        this.ordersCount = ordersCount;
    }

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getFrequencyClass() {
        return FrequencyClassifier.getCustomerClass(this);
    }

    public String getAmountClass() {
        return AmountClassifier.getCustomerClass(this);
    }

}
