package ru.kirea.androidnotes.models;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import ru.kirea.androidnotes.db.models.Note;

public class NoteViewModel extends ViewModel {
    private NotesService notesService;

    private final MutableLiveData<List<Note>> notesLiveData = new MutableLiveData<>();

    public NoteViewModel() {
        //notesService = new LocalNotesServiceImpl(); //подключаемся к локальному хранилищу заметок
        notesService = new BDNoteServiceImpl(); //подключаемся к хранилищу заметок в базе
        notesService.init();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    public LiveData<List<Note>> getNotesLiveData() {
        return notesLiveData;
    }

    //получить список заметок в отдельном потоке
    public void getNotes() {
        try {
            //ExecutorService executorService = Executors.newFixedThreadPool(10);
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            Future<List<Note>> future = executorService.submit(new Callable<List<Note>>(){
                public List<Note> call() {
                    return notesService.getNotes();
                }
            });
            executorService.shutdown();
            notesLiveData.postValue(future.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public NotesService getNotesService() {
        return notesService;
    }
}
