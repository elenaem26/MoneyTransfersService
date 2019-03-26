package revolut.bank.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtils {

    private static final String DATE_TIME_FORMAT = "yyyy.MM.dd.HH.mm.ss";
    private static final String DATE_FORMAT_INPUT = "yyyy-MM-dd";

    public static String map(Timestamp timestamp) {
        return new SimpleDateFormat(DATE_TIME_FORMAT).format(timestamp);
    }

    public static Timestamp convertDate(String strDate, boolean floor) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_INPUT);
        LocalDate date = LocalDate.parse(strDate, formatter);
        LocalTime time = floor ? LocalTime.MIN : LocalTime.MAX;
        return java.sql.Timestamp.valueOf( LocalDateTime.of(date, time));
    }
}
