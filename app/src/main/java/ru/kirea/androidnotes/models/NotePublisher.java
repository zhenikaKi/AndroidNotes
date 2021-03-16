package ru.kirea.androidnotes.models;

import java.util.ArrayList;
import java.util.List;

import ru.kirea.androidnotes.presenters.NoteObserver;

public class NotePublisher {
    private static NotePublisher instance;

    private List<NoteObserver> observers;

    private NotePublisher() {
        observers = new ArrayList<>();
    }

    public static NotePublisher getInstance() {
        if (instance == null) {
            instance = new NotePublisher();
        }
        return instance;
    }

    public void add(NoteObserver observer) {
        observers.add(observer);
    }

    public void sendChange() {
        for (NoteObserver observer: observers) {
            observer.updateNotes();
        }
    }
}
