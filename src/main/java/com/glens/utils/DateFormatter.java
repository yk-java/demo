package com.glens.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Title:
 * Description:
 *
 * @author yk.
 * @version Version 1.0
 * Date: 2018/1/17 17:31
 */
public class DateFormatter {
    public static Date parser(String str){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Date date= null;
        try {
            date = sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    public static String stringDate(Date date){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }
    public static Date sqlDate(Date date){
        Date time = null;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = sdf.format(date);
        try {
            time = new java.sql.Date(sdf.parse(str).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }
}
