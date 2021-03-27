package ru.kirea.androidnotes.presenters;

import android.view.View;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.Calendar;

import androidx.fragment.app.FragmentManager;
import ru.kirea.androidnotes.R;

//класс по работе с окном выбора даты, времени
public class DateTimeDialog {
    //отдельно минуты, секунды
    private Calendar calendar;
    private long selectedDate;
    private int timeHH, timeMM;

    private DateTimeListener dateTimeListener;
    private FragmentManager fragmentManager;

    public DateTimeDialog(FragmentManager fragmentManager, long dateTime) {
        this.fragmentManager = fragmentManager;
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
            selectedDate = dateTime;
        } else {
            selectedDate = System.currentTimeMillis();
        }
        timeHH = calendar.get(Calendar.HOUR_OF_DAY);
        timeMM = calendar.get(Calendar.MINUTE);
    }

    //Показать окно выбора даты
    public void showDate() {
        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setSelection(selectedDate);
        builder.setTitleText(R.string.date_dialog_title);

        MaterialDatePicker<Long> datePicker = builder.build();
        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                if (dateTimeListener != null) {
                    dateTimeListener.selectedDateTime(selection);
                }
            }
        });

        datePicker.show(fragmentManager, null);

    }

    //Показзать окно выбора времени
    public void showTime() {
        final MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(timeHH)
                .setMinute(timeMM)
                .setTitleText(R.string.time_dialog_title)
                .build();

        timePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                calendar.set(Calendar.MINUTE, timePicker.getMinute());

                if (dateTimeListener != null) {
                    dateTimeListener.selectedDateTime(calendar.getTimeInMillis());
                }
            }
        });

        timePicker.show(fragmentManager, null);
    }

    public interface DateTimeListener {
        void selectedDateTime(long millis);
    }
}
