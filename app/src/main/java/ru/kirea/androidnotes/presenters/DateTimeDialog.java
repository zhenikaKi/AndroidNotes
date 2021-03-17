package ru.kirea.androidnotes.presenters;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;

//класс по работе с окном выбора даты, времени
public class DateTimeDialog {
    private Context context;

    //отдельно день, месяц, год, часы, минуты, секунды
    private Calendar calendar;
    private int dateDD, dateMM, dateYYYY;
    private int timeHH, timeMM;

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
        calendar = Calendar.getInstance();
        if (dateTime != 0) {
            calendar.setTimeInMillis(dateTime);
        }
        dateDD = calendar.get(Calendar.DAY_OF_MONTH);
        dateMM = calendar.get(Calendar.MONTH);
        dateYYYY = calendar.get(Calendar.YEAR);
        timeHH = calendar.get(Calendar.HOUR_OF_DAY);
        timeMM = calendar.get(Calendar.MINUTE);
    }

    //Показать окно выбора даты
    public void showDate() {
        new DatePickerDialog(context, dateSetListener, dateYYYY, dateMM, dateDD).show();
    }

    //Показзать окно выбора времени
    public void showTime() {
        new TimePickerDialog(context, timeSetListener, timeHH, timeMM, true).show();
    }

    //Обработка выбора даты
    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            if (dateTimeListener != null)
                dateTimeListener.selectedDateTime(calendar.getTimeInMillis());
        }
    };

    //Обработка выбора времени
    private TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);

            if (dateTimeListener != null)
                dateTimeListener.selectedDateTime(calendar.getTimeInMillis());
        }
    };

    public interface DateTimeListener {
        void selectedDateTime(long millis);
    }
}
