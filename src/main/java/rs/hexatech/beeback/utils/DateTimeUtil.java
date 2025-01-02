package rs.hexatech.beeback.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class DateTimeUtil {

    public static final String DATE_FORMAT = "dd.MM.yyyy";
    public static final String DEFAULT_ZONE = "UTC";

    public static Instant now() {
        Date date = new Date();
        @SuppressWarnings("deprecation")
        Integer zoneOffset = date.getTimezoneOffset();
        Instant instant = date.toInstant();
        return instant.minus(zoneOffset, ChronoUnit.MINUTES);
    }

    public static Instant dateFromString(String stringDate) throws ParseException {
        return dateFromString(stringDate, DATE_FORMAT);
    }

    public static Instant dateFromString(String stringDate, String dateFormat) throws ParseException {
        Date date = new SimpleDateFormat(dateFormat).parse(stringDate);
        @SuppressWarnings("deprecation")
        Integer zoneOffset = date.getTimezoneOffset();
        Instant instant = date.toInstant();
        return instant.minus(zoneOffset, ChronoUnit.MINUTES);
    }

    public static LocalDateTime toLocalDateTime(Instant date) {
        return LocalDateTime.ofInstant(dateStartOfDay(date), ZoneId.of(DEFAULT_ZONE));
    }

    public static String dateToString(Instant date) throws ParseException {
        return dateToString(date, DATE_FORMAT);
    }

    public static String dateToString(Instant date, String dateFormat) throws ParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat).withZone(ZoneId.of(DEFAULT_ZONE));
        return formatter.format(date);
    }

    public static Instant dateStartOfDay(Instant date) {
        return date.atZone(ZoneId.of("UTC")).withHour(0).withMinute(0).withSecond(0).toInstant();
    }

    public static Instant dateEndOfDay(Instant date) {
        return date.atZone(ZoneId.of("UTC")).withHour(23).withMinute(59).withSecond(59).toInstant();

    }

    public static Instant dateMinusDays(Instant date, Integer daysDifference) {
        return date.minus(daysDifference, ChronoUnit.DAYS);
    }

    public static Instant datePlusDays(Instant date, Integer daysDifference) {
        return date.plus(daysDifference, ChronoUnit.DAYS);
    }

    public static Long daysBetween(Instant startDate, Instant endDate) {
        return ChronoUnit.DAYS.between(toLocalDateTime(dateStartOfDay(startDate)), toLocalDateTime(dateStartOfDay(endDate)));
    }
}
