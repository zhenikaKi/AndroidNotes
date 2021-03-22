package ru.kirea.androidnotes.models;

import android.text.format.DateUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import ru.kirea.androidnotes.db.models.Note;

//локальное хранилище заметок
public class LocalNotesServiceImpl implements NotesService {
    public static long incrementId = 1; //для автоматического заполнения id'шника заметки
    private static List<Note> notes = new ArrayList<>();

    @Override
    public void init() {
        if (notes.isEmpty()) {
            generateNotes();
        }
    }

    @Override
    public List<Note> getNotes() {
        //выдаем отсортированный список заметок по дате создания
        List<Note> result = new ArrayList<>(notes);
        Collections.sort(result, new Comparator<Note>() {
            @Override
            public int compare(Note o1, Note o2) {
                Long d1 = o1.getCreateDate();
                Long d2 = o2.getCreateDate();
                return d2.compareTo(d1);
            }
        });
        return result;
    }

    @Override
    public Note findNote(long id) {
        for (Note note: notes) {
            if (id == note.getId()) {
                return note;
            }
        }
        return null;
    }

    @Override
    public void saveNote(Note note) {
        if (note == null) {
            return;
        }

        //проверяем, есть ли заметка в базе
        Integer position = getPosition(note.getId());
        if (position == null) {
            notes.add(note); //заметки нет, добавляем
        } else {
            notes.set(position, note); //заметка есть, обновляем ее
        }
    }

    @Override
    public void deleteNote(Note note) {
        Integer position = getPosition(note.getId());
        if (position != null) {
            notes.remove(position.intValue());
        }
    }

    //сформировать список заметок
    private void generateNotes() {
        final int COUNT = 10;

        Random random = new Random();
        for (int ind = 1; ind <= COUNT; ind++) {
            String title = String.format("Заметка #%s", ind);
            String description = String.format("Описание заметки. Ее номер %s из %s", ind, COUNT);
            Note note = new Note(title, description);

            //сделаем разброс даты создания заметки
            long crete = System.currentTimeMillis() - random.nextInt(1440) * DateUtils.MINUTE_IN_MILLIS;
            note.setCreateDate(crete);
            notes.add(note);
        }
    }

    //найти позицию заметки по ее id - вспомогательный метод, т.к. заметки храняться в массиве
    private Integer getPosition(long id) {
        for (int ind = 0; ind < notes.size(); ind++) {
            if (id == notes.get(ind).getId()) {
                return ind;
            }
        }
        return null;
    }
}
