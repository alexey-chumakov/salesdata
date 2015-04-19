package org.sparksample;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.sparksample.model.Customer;
import org.sparksample.model.Order;
import org.sparksample.model.OrderItem;
import scala.Tuple2;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by achumakov on 4/10/2015.
 */
public class SparkCustomerStats {

    public static void main(String... args) {
        JavaRDD<String> logData = loadData();

        // Filter and convert to Java objects
        JavaRDD<String> ordersWithId = logData.filter(order -> (order.split("\t").length >= 8)).cache();
        JavaRDD<OrderItem> orders = convertToOrderItems(ordersWithId);
        System.out.println("Orders: " + orders.count());

        // Get orders mapped by userID
        JavaPairRDD<String, Order> ordersByUsers = getUserOrders(orders);

        // Get customers
        JavaRDD<Customer> customers = getCustomers(ordersByUsers);
        listCustomers(customers);

        // Get some simple stats
        JavaPairRDD<String, Long> classStats = customers
                .mapToPair(customer -> new Tuple2<>(customer.getFrequencyClass() + customer.getAmountClass(), 1L))
                .reduceByKey((a, b) -> a + b)
                .sortByKey(true);
        long customerCount = customers.count();
        System.out.println(customerCount);
        classStats.foreach(stats -> System.out.println("Class: " + stats._1() + " Amount: " + stats._2() + " Percentage: " + stats._2() * 100 / customerCount + "%"));
    }

    private static JavaRDD<String> loadData() {
        String logFile = "E:/Projects/Oxagile/trades.csv"; // Should be some file on your system
        SparkConf conf = new SparkConf().setAppName("Simple Application");
        conf.setMaster("local[*]");
        JavaSparkContext sc = new JavaSparkContext(conf);
        return sc.textFile(logFile).cache();
    }

    private static JavaRDD<OrderItem> convertToOrderItems(JavaRDD<String> ordersWithId) {
        return ordersWithId.map(s -> {
            String[] order = s.split("\t");
            BigDecimal amount = new BigDecimal(order[3]);
            return new OrderItem(order[6], order[0], order[8], order[9], order[1], amount);
        });
    }

    private static JavaPairRDD<String, Order> getUserOrders(JavaRDD<OrderItem> orders) {
        JavaPairRDD<Order, OrderItem> mappedOrderItems = orders.mapToPair(orderItem -> (new Tuple2<>(new Order(orderItem.getUserId(), orderItem.getOrderId()), orderItem)));
        JavaPairRDD<Order, Iterable<OrderItem>> groupedByOrder = mappedOrderItems.groupByKey();
        JavaPairRDD<String, Tuple2<Order, Iterable<OrderItem>>> userGroupedOrders = groupedByOrder.mapToPair(order -> (new Tuple2<>(order._1().getUserId(), order)));

        return userGroupedOrders.mapToPair(userOrder -> {
            Order order = userOrder._2()._1();
            Iterable<OrderItem> orderItems = userOrder._2()._2();

            BigDecimal amount = BigDecimal.ZERO;
            Integer orderItemsCount = 0;
            String orderDate = null;

            for (OrderItem item : orderItems) {
                amount = amount.add(item.getOrderAmount());
                orderItemsCount++;
                orderDate = item.getDate();
            }

            order.setOrderItemsCount(orderItemsCount);
            order.setOrderSum(amount);
            order.setDate(orderDate);

            return new Tuple2<>(userOrder._1(), order);
        });
    }

    private static JavaRDD<Customer> getCustomers(JavaPairRDD<String, Order> ordersByUsers) {
        JavaPairRDD<String, Iterable<Order>> groupedByUser = ordersByUsers.groupByKey();
        return groupedByUser.map(items -> {
            Iterable<Order> customerOrders = items._2();
            BigDecimal amount = BigDecimal.ZERO;
            int ordersCount = 0;
            for (Order order : customerOrders) {
                amount = amount.add(order.getOrderSum());
                ordersCount++;
            }
            return new Customer(items._1(), ordersCount, amount);
        });
    }

    private static void listCustomers(JavaRDD<Customer> customers) {
        String lineFormat = "%s\t%s\t%s\t%s\t%s";
        String title = String.format(lineFormat, "User ID", "Amount", "Orders", "Frequency Class", "Amount Class");
        List<Customer> list = customers.collect();
        try (final PrintWriter writer = new PrintWriter("E:/Projects/Oxagile/customers.csv", "UTF-8")) {
            writer.println(title);
            list.stream().forEach(customer ->
                    writer.println(String.format(lineFormat, customer.getId(), customer.getOrderAmount(),
                            customer.getOrdersCount(), customer.getFrequencyClass(), customer.getAmountClass())));
        } catch (Exception e) {
            e.printStackTrace();
        }
/*
        JavaRDD<String> customersText = customers.map(customer -> "User ID: " + customer.getId()
                + " Amount: " + customer.getOrderAmount() + " Orders: " + customer.getOrdersCount()
                + " Frequency Class: " + customer.getFrequencyClass() + " Amount Class: " + customer.getAmountClass());
        customersText.foreach(customer -> System.out.println(customer));
*/
    }

}
