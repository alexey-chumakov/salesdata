package org.sparksample;

import org.apache.commons.lang3.StringUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import scala.Tuple2;

import java.util.Iterator;

/**
 * Created by achumakov on 4/10/2015.
 */
public class SparkSample {

    public static void main(String... args) {
        JavaRDD<String> logData = loadData();

        JavaRDD<String> ordersWithId = logData.filter(order -> (order.split("\t").length >= 8)).cache();

        JavaRDD<OrderItem> orders = convertToOrderItems(ordersWithId);

        // Just count
        long ordersCount = orders.count();

        System.out.println("Orders: " + ordersCount);

        JavaPairRDD<String, OrderItem> mappedOrders = orders.mapToPair(order -> (new Tuple2<>(order.getUserId(), order)));
        JavaPairRDD<String, Iterable<OrderItem>> grouped = mappedOrders.groupByKey();
        System.out.println(grouped.count());

        grouped.foreach(order -> {

            int i = 0;
            Iterator<OrderItem> iterator = order._2().iterator();
            while (iterator.hasNext()) {
                i++; iterator.next();
            }

            System.out.println("ID: " + order._1() + " : " + i);

        });

    }

    private static JavaRDD<OrderItem> convertToOrderItems(JavaRDD<String> ordersWithId) {
        return ordersWithId.map(s -> {
                    String[] order = s.split("\t");
                    return new OrderItem(order[6], order[0], order[8], order[9]);
            }).cache();
    }

    private static JavaRDD<String> loadData() {
        String logFile = "E:/Projects/Oxagile/trades.csv"; // Should be some file on your system
        SparkConf conf = new SparkConf().setAppName("Simple Application");
        conf.setMaster("local[*]");
        JavaSparkContext sc = new JavaSparkContext(conf);
        return sc.textFile(logFile).cache();
    }

}
