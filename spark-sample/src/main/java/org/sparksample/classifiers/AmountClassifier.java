package org.sparksample.classifiers;

import org.sparksample.model.Customer;

/**
 * Created by achumakov on 4/19/2015.
 */
public class AmountClassifier {

    public static String getCustomerClass(Customer customer) {
        long amount = customer.getOrderAmount().longValue();
        if (amount > 100000L) {
            return "A";
        } else if (amount < 30000L) {
            return "C";
        } else {
            return "B";
        }
    }

}
