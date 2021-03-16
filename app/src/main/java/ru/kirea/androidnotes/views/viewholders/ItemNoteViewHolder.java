package ru.kirea.androidnotes.views.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import ru.kirea.androidnotes.R;

public class ItemNoteViewHolder extends RecyclerView.ViewHolder {
    private TextView title;
    private TextView description;
    private TextView crete;
    private TextView update;
    private ConstraintLayout item;

    public ItemNoteViewHolder(View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.note_title_id);
        description = itemView.findViewById(R.id.note_description_id);
        crete = itemView.findViewById(R.id.note_create_id);
        update = itemView.findViewById(R.id.note_update_id);
        item = itemView.findViewById(R.id.note_item_id);
    }

    public TextView getTitle() {
        return title;
    }

    public TextView getDescription() {
        return description;
    }

    public TextView getCrete() {
        return crete;
    }

    public TextView getUpdate() {
        return update;
    }

    public ConstraintLayout getItem() {
        return item;
    }
}