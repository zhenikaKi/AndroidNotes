package ru.kirea.androidnotes.presenters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ru.kirea.androidnotes.R;
import ru.kirea.androidnotes.db.models.ItemType;
import ru.kirea.androidnotes.db.models.Note;
import ru.kirea.androidnotes.db.models.Title;
import ru.kirea.androidnotes.helpers.DateHelper;
import ru.kirea.androidnotes.models.ItemAdapterClickable;
import ru.kirea.androidnotes.models.NoteClickable;
import ru.kirea.androidnotes.views.viewholders.ItemNoteViewHolder;
import ru.kirea.androidnotes.views.viewholders.ItemTitleViewHolder;

public class ListNotesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
	public static final int ITEM_TITLE = 1; //элемент заголовка
	public static final int ITEM_NOTE = 2; //элемент заметки

	private List<ItemType> items;
	private NoteClickable noteClickable;

	public ListNotesAdapter(List<ItemType> items) {
		this.items = items;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
		LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
		if (viewType == ITEM_TITLE) {
			View v = layoutInflater.inflate(R.layout.item_title, viewGroup, false);
			return new ItemTitleViewHolder(v);
		} else {
			View v = layoutInflater.inflate(R.layout.item_note, viewGroup, false);
			return new ItemNoteViewHolder(v);
		}
	}

	@Override
	public int getItemViewType(int position) {
		return items.get(position).getType();
	}

	@Override
	public int getItemCount() {
		return items.size();
	}

	@Override
	public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
		if (getItemViewType(position) == ITEM_TITLE) {
			showItemTitle((ItemTitleViewHolder) holder, position);
		} else {
			showItemNote((ItemNoteViewHolder) holder, position);
		}
	}

	public void setNoteClickable(NoteClickable noteClickable) {
		this.noteClickable = noteClickable;
	}

	//показать элемент в виде заголовка
	private void showItemTitle(ItemTitleViewHolder holder, int position) {
		Title titleItem = (Title) items.get(position);
		holder.getTitle().setText(titleItem.getText());
	}

	//показать элемент в виде заметки
	private void showItemNote(ItemNoteViewHolder noteViewHolder, int position) {
		//получаем заметку и показываем ее
		Note note = (Note) items.get(position);
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
					noteClickable.noteClick((Note) items.get(position));
				}

				@Override
				public void itemMenuClick(View view, int position) {
					noteClickable.noteMenuClick(view, (Note) items.get(position));
				}
			});
		}
	}
}
