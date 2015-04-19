package org.sparksample.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by achumakov on 4/16/2015.
 */
public class Order implements Serializable {

    private String userId;

    private String orderId;

    private String date;

    private BigDecimal orderSum;

    private Integer orderItemsCount;

    public Order(String userId, String orderId) {
        this.userId = userId;
        this.orderId = orderId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public BigDecimal getOrderSum() {
        return orderSum;
    }

    public void setOrderSum(BigDecimal orderSum) {
        this.orderSum = orderSum;
    }

    public Integer getOrderItemsCount() {
        return orderItemsCount;
    }

    public void setOrderItemsCount(Integer orderItemsCount) {
        this.orderItemsCount = orderItemsCount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (!orderId.equals(order.orderId)) return false;
        if (!userId.equals(order.userId)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userId.hashCode();
        result = 31 * result + orderId.hashCode();
        return result;
    }
}
