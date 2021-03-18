package ru.kirea.androidnotes.views.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import ru.kirea.androidnotes.R;
import ru.kirea.androidnotes.models.ItemAdapterClickable;

public class ItemNoteViewHolder extends RecyclerView.ViewHolder {
    private TextView title;
    private TextView description;
    private TextView crete;
    private TextView update;

    private ItemAdapterClickable itemAdapterClickable;

    public ItemNoteViewHolder(View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.note_title_id);
        description = itemView.findViewById(R.id.note_description_id);
        crete = itemView.findViewById(R.id.note_create_id);
        update = itemView.findViewById(R.id.note_update_id);

        //обработка нажатия по элементу списка
        itemView.findViewById(R.id.note_item_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemAdapterClickable != null) {
                    itemAdapterClickable.itemClick(getAdapterPosition());
                }
            }
        });
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

    public void setItemAdapterClickable(ItemAdapterClickable itemAdapterClickable) {
        this.itemAdapterClickable = itemAdapterClickable;
    }
}