package com.example.date_values.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateUtil {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final SimpleDateFormat monthNow = new SimpleDateFormat("MM");
    private static final SimpleDateFormat yearNow = new SimpleDateFormat("yy");

    public static Long getDate(Long dateTime) {
        return dateTime / 1000000;
    }

    public static Long getCurrenDate() {
        return Long.parseLong(dateFormat.format(new Date()));
    }

    public static Long getCurrenDateTime() {
        return Long.parseLong(dateTimeFormat.format(new Date()));
    }

    public static String getMonthNow(){
        return monthNow.format(new Date());
    }

    public static String getYearNow(){
        return yearNow.format(new Date());
    }

    public static Long sum(Long date, int add) {
        // Chuyển đổi Long sang Date
        Date dateObj;
        try {
            dateObj = dateFormat.parse(date.toString());
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format", e);
        }

        // Sử dụng Calendar để cộng thêm số ngày
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateObj);
        calendar.add(Calendar.DATE, add);

        // Chuyển đổi lại Date sang Long với định dạng yyyyMMdd
        return Long.parseLong(dateFormat.format(calendar.getTime()));
    }

    public static Long subtract(Long date, int subtractDays) {
        // Chuyển đổi Long sang Date
        Date dateObj;
        try {
            dateObj = dateFormat.parse(date.toString());
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format", e);
        }

        // Sử dụng Calendar để trừ số ngày
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateObj);
        calendar.add(Calendar.DATE, -subtractDays); // Trừ số ngày

        // Chuyển đổi lại Date sang Long với định dạng yyyyMMdd
        return Long.parseLong(dateFormat.format(calendar.getTime()));
    }

    public static long calculateDaysBetween(Long date1, Long date2) {
        // Chuyển đổi Long sang Date
        Date dateObj1;
        Date dateObj2;
        try {
            dateObj1 = dateFormat.parse(date1.toString());
            dateObj2 = dateFormat.parse(date2.toString());
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format", e);
        }

        // Tính số ngày giữa hai ngày
        long diffInMillies = Math.abs(dateObj2.getTime() - dateObj1.getTime());
        return TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    public static String dateLongToString(Long date){
        // Chuyển Long thành chuỗi
        String dateString = date.toString();

        // Lấy từng phần ngày, tháng, năm từ chuỗi
        String day = dateString.substring(6, 8);
        String month = dateString.substring(4, 6);
        String year = dateString.substring(0, 4);

        // Kết hợp lại thành định dạng mong muốn
        String result = day + "-" + month + "-" + year;
        return result;
    }

    public static Long StringToDateLong(String date){

        // Tách ngày, tháng, năm từ chuỗi
        String[] dateParts = date.split("-");
        String day = dateParts[0];
        String month = dateParts[1];
        String year = dateParts[2];

        // Ghép lại thành chuỗi định dạng yyyyMMdd
        String longDateString = year + month + day;

        // Chuyển đổi thành Long
        Long result = Long.parseLong(longDateString);
        return result;
    }

    public static LocalDate convertLongToLocalDate(Long yyyymmdd) throws DateTimeParseException {
        String dateStr = yyyymmdd.toString();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return LocalDate.parse(dateStr, formatter);
    }
}
