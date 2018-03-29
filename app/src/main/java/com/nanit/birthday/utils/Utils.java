package com.nanit.birthday.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hadas on 3/29/18.
 */

public class Utils {

    private final static String DATE_PATTERN = "dd/MM/yyyy";

    public static String serializeDate(Date date) {
        return new SimpleDateFormat(DATE_PATTERN).format(date);
    }

    public static Date deserializeDate(String stringDate) {
        SimpleDateFormat format = new SimpleDateFormat(DATE_PATTERN);
        try {
            return format.parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
