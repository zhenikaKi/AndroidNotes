package ru.kirea.androidnotes.models;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import ru.kirea.androidnotes.db.models.ItemType;
import ru.kirea.androidnotes.db.models.Note;
import ru.kirea.androidnotes.db.models.Title;
import ru.kirea.androidnotes.helpers.DateHelper;

public class NoteViewModel extends ViewModel {
    private NotesService notesService;

    private final MutableLiveData<List<ItemType>> notesLiveData = new MutableLiveData<>();

    public NoteViewModel() {
        //notesService = new LocalNotesServiceImpl(); //подключаемся к локальному хранилищу заметок
        notesService = new BDNoteServiceImpl(); //подключаемся к хранилищу заметок в базе
        notesService.init();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    public LiveData<List<ItemType>> getNotesLiveData() {
        return notesLiveData;
    }

    //получить список заметок в отдельном потоке
    public void getNotes() {
        try {
            //ExecutorService executorService = Executors.newFixedThreadPool(10);
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    notesService.getNotes(new Callback<List<Note>>() {
                        @Override
                        public void onResult(List<Note> notes) {
                            List<ItemType> result = new ArrayList<>();

                            //проставим заголовки с датами
                            String oldTitle = "*";
                            for (Note note: notes) {
                                String title = DateHelper.timestampToString(note.getCreateDate(), DateHelper.DateFormat.DDMMYYYY);
                                if (title != null && !oldTitle.equals(title)) {
                                    result.add(new Title(title));
                                    oldTitle = title;
                                }
                                result.add(note);
                            }

                            notesLiveData.postValue(result);
                        }
                    });
                }
            });
            executorService.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public NotesService getNotesService() {
        return notesService;
    }
}
