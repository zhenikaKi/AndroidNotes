package ru.kirea.androidnotes.views.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import ru.kirea.androidnotes.R;

public class ItemTitleViewHolder extends RecyclerView.ViewHolder {
    private TextView title;

    public ItemTitleViewHolder(final View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.title_id);
    }

    public TextView getTitle() {
        return title;
    }
}