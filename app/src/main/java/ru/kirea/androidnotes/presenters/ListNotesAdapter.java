package ru.kirea.androidnotes.presenters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ru.kirea.androidnotes.R;
import ru.kirea.androidnotes.helpers.DateHelper;
import ru.kirea.androidnotes.models.ItemAdapterClickable;
import ru.kirea.androidnotes.models.Note;
import ru.kirea.androidnotes.models.NoteClickable;
import ru.kirea.androidnotes.views.viewholders.ItemNoteViewHolder;

public class ListNotesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private List<Note> items;
	private NoteClickable noteClickable;

	public ListNotesAdapter(List<Note> items) {
		this.items = items;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
		LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
		View v = layoutInflater.inflate(R.layout.item_note, viewGroup, false);
		return new ItemNoteViewHolder(v);
	}

	@Override
	public int getItemCount() {
		return items.size();
	}

	@Override
	public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
		ItemNoteViewHolder noteViewHolder = (ItemNoteViewHolder) holder;

		//получаем заметку и показываем ее
		Note note = items.get(position);
		noteViewHolder.getTitle().setText(note.getTitle());
		noteViewHolder.getDescription().setText(note.getDescription());
		String date = DateHelper.timestampToString(note.getCreateDate(), DateHelper.DateFormat.DDMMYYYY_HHMM);
		noteViewHolder.getCrete().setText(date);
		date = note.getUpdateDate() == null ? "" : DateHelper.timestampToString(note.getUpdateDate(), DateHelper.DateFormat.DDMMYYYY_HHMM);
		noteViewHolder.getUpdate().setText(date);

		//обработчик нажатия по элементу
		if (noteClickable != null) {
			noteViewHolder.setItemAdapterClickable(new ItemAdapterClickable() {
				@Override
				public void itemClick(int position) {
					noteClickable.noteClick(items.get(position));
				}
			});
		}
	}

	public void setNoteClickable(NoteClickable noteClickable) {
		this.noteClickable = noteClickable;
	}
}
