package com.core2web.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Format {

    // ₹725 for whole amounts, ₹725.50 otherwise
    public static String price(double price) {
        return price == Math.floor(price)
                ? String.format("₹%,.0f", price)
                : String.format("₹%,.2f", price);
    }

    public static String dateTime(long millis) {
        return new SimpleDateFormat("dd MMM yyyy, hh:mm a").format(new Date(millis));
    }
}
