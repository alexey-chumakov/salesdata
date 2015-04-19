package org.sparksample.classifiers;

import org.sparksample.model.Customer;

/**
 * Created by achumakov on 4/19/2015.
 */
public class FrequencyClassifier {

    public static String getCustomerClass(Customer customer) {
        int ordersCount = customer.getOrdersCount();
        if (ordersCount > 50) {
            return "A";
        } else if (ordersCount < 10) {
            return "C";
        } else {
            return "B";
        }
    }

}
