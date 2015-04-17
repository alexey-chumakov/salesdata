package org.sparksample;

import java.io.Serializable;

/**
 * Created by achumakov on 4/16/2015.
 */
public class OrderItem implements Serializable {

    private String userId;

    private String orderId;

    private String subCategory;

    private String department;

    public OrderItem(String userId, String orderId, String subCategory, String department) {
        this.userId = userId;
        this.orderId = orderId;
        this.subCategory = subCategory;
        this.department = department;
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

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
