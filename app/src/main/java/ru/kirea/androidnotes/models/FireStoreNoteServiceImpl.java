package ru.kirea.androidnotes.models;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import ru.kirea.androidnotes.db.DBConsts;
import ru.kirea.androidnotes.db.models.Note;

//облачное хранилище заметок
public class FireStoreNoteServiceImpl implements NotesService {
    private final String COLLECTION_NAME = "notes";
    private CollectionReference collection;

    @Override
    public void init() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        collection = firestore.collection(COLLECTION_NAME);
    }

    @Override
    public void getNotes(final Callback<List<Note>> callback) {
        collection.orderBy(DBConsts.NOTE_CREATE_DATE, Query.Direction.DESCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    // При удачном считывании данных загрузим список карточек
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<Note> notes = new ArrayList<>();
                        if (task.isSuccessful() && task.getResult() != null) {
                            for (DocumentSnapshot documentSnapshot: task.getResult().getDocuments()) {
                                notes.add(documentSnapshotToNote(documentSnapshot));
                            }
                        }

                        callback.onResult(notes);
                    }
                });
    }

    @Override
    public void findNote(String id, final Callback<Note> callback) {
        collection.document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                callback.onResult(documentSnapshotToNote(task.getResult()));
            }
        });
    }

    @Override
    public void saveNote(final Note note, final Callback<Note> callback) {
        if (note == null) {
            return;
        }

        HashMap<String, Object> data = noteToDocument(note);
        if (note.getId() == null) {
            collection.add(data).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    if (task.getResult() != null) {
                        note.setId(task.getResult().getId());
                        callback.onResult(note);
                    }
                }
            });
        } else {
            collection.document(note.getId()).set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    callback.onResult(note);
                }
            });
        }
    }

    @Override
    public void deleteNote(Note note) {
        collection.document(note.getId()).delete();
    }

    //преобразование документ из облака в объект заметки
    private Note documentSnapshotToNote(DocumentSnapshot documentSnapshot) {
        if (documentSnapshot == null) {
            return null;
        }

        String id = documentSnapshot.getId();
        String title = documentSnapshot.getString(DBConsts.NOTE_TITLE);
        String description = documentSnapshot.getString(DBConsts.NOTE_DESCRIPTION);
        Date date = documentSnapshot.getDate(DBConsts.NOTE_CREATE_DATE);
        long createDate = date != null ? date.getTime() : 0;
        date = documentSnapshot.getDate(DBConsts.NOTE_UPDATE_DATE);
        Long updateDate = date != null ? date.getTime() : null;

        Note note = new Note(id, title, description, createDate);
        note.setUpdateDate(updateDate);

        return note;
    }

    //преобразование заметки в объект для облака
    private HashMap<String, Object> noteToDocument(Note note) {
        HashMap<String, Object> data = new HashMap<>();
        data.put(DBConsts.NOTE_TITLE, note.getTitle());
        data.put(DBConsts.NOTE_DESCRIPTION, note.getDescription());
        data.put(DBConsts.NOTE_CREATE_DATE, new Date(note.getCreateDate()));
        data.put(DBConsts.NOTE_UPDATE_DATE, note.getUpdateDate() == null ? null : new Date(note.getUpdateDate()));
        return data;
    }
}
