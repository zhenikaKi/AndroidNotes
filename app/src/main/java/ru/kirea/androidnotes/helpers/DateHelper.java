package ru.kirea.androidnotes.helpers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateHelper {
    public enum DateFormat {DDMMYYYY, DDMMYYYY_HHMM, DDMMYYYY_HHMMSS}

    //перевести timestamp в читаемую дату
    public static String timestampToString(Long millis, DateFormat dateFormat) {
        if (millis == null) {
            return null;
        }

        String patter;
        if (dateFormat == DateFormat.DDMMYYYY_HHMM) {
            patter = "dd.MM.yyyy HH:mm";
        } else if (dateFormat == DateFormat.DDMMYYYY_HHMMSS) {
            patter = "dd.MM.yyyy HH:mm:ss";
        } else {
            patter = "dd.MM.yyyy";
        }

        SimpleDateFormat simpleFormat = new SimpleDateFormat(patter, Locale.getDefault());
        return simpleFormat.format(new Date(millis));
    }
}
