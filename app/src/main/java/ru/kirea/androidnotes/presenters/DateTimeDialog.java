package ru.kirea.androidnotes.presenters;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import ru.kirea.androidnotes.helpers.DateHelper;

//класс по работе с окном выбора даты, времени
public class DateTimeDialog {
    private Context context;

    //отдельно день, месяц, год, часы, минуты, секунды
    private GregorianCalendar gregorianCalendar;
    private int dateDD, dateMM, dateYYYY;
    private int timeHH, timeMM, timeSS;

    private DateTimeListener dateTimeListener;

    public DateTimeDialog(Context context, long dateTime) {
        this.context = context;
        setDate(dateTime);
    }

    //задать слушатель
    public void setDateTimeListener(DateTimeListener dateTimeListener) {
        this.dateTimeListener = dateTimeListener;
    }

    //Установить дату
    private void setDate(long dateTime) {
        if (dateTime == 0L) {
            gregorianCalendar = new GregorianCalendar(TimeZone.getDefault());
            dateDD = gregorianCalendar.get(Calendar.DAY_OF_MONTH);
            dateMM = gregorianCalendar.get(Calendar.MONTH);
            dateYYYY = gregorianCalendar.get(Calendar.YEAR);
            timeHH = gregorianCalendar.get(Calendar.HOUR_OF_DAY);
            timeMM = gregorianCalendar.get(Calendar.MINUTE);
            timeSS = gregorianCalendar.get(Calendar.SECOND);
        }
        else {
            //получаем дату
            String curDate = DateHelper.timestampToString(dateTime, DateHelper.DateFormat.DDMMYYYY_HHMMSS);
            dateDD = Integer.valueOf(curDate.substring(0, 2));
            dateMM = Integer.valueOf(curDate.substring(3, 5)) - 1;
            dateYYYY = Integer.valueOf(curDate.substring(6, 10));
            timeHH = Integer.valueOf(curDate.substring(11, 13));
            timeMM = Integer.valueOf(curDate.substring(14, 16));
            timeSS = Integer.valueOf(curDate.substring(17, 19));
            //задаем дату
            gregorianCalendar = new GregorianCalendar(dateYYYY,
                    dateMM,
                    dateDD,
                    timeHH,
                    timeMM,
                    timeSS);
        }
    }

    //Показать окно выбора даты
    public void showDate() {
        new DatePickerDialog(context, dateSetListener,
                gregorianCalendar.get(Calendar.YEAR),
                gregorianCalendar.get(Calendar.MONTH),
                gregorianCalendar.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    //Показзать окно выбора времени
    public void showTime() {
        new TimePickerDialog(context, timeSetListener,
                gregorianCalendar.get(Calendar.HOUR_OF_DAY),
                gregorianCalendar.get(Calendar.MINUTE),
                true).show();
    }

    //Обработка выбора даты
    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            gregorianCalendar.set(Calendar.YEAR, year);
            gregorianCalendar.set(Calendar.MONTH, monthOfYear);
            gregorianCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            gregorianCalendar.set(Calendar.HOUR_OF_DAY, timeHH);
            gregorianCalendar.set(Calendar.MINUTE, timeMM);
            gregorianCalendar.set(Calendar.SECOND, timeSS);

            if (dateTimeListener != null)
                dateTimeListener.selectedDateTime(gregorianCalendar.getTimeInMillis());
        }
    };

    //Обработка выбора времени
    private TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            gregorianCalendar.set(Calendar.YEAR, dateYYYY);
            gregorianCalendar.set(Calendar.MONTH, dateMM);
            gregorianCalendar.set(Calendar.DAY_OF_MONTH, dateDD);
            gregorianCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            gregorianCalendar.set(Calendar.MINUTE, minute);
            gregorianCalendar.set(Calendar.SECOND, timeSS);

            if (dateTimeListener != null)
                dateTimeListener.selectedDateTime(gregorianCalendar.getTimeInMillis());
        }
    };

    public interface DateTimeListener {
        void selectedDateTime(long millis);
    }
}
