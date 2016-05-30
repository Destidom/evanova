package com.tlabs.android.jeeves.views.items;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tlabs.android.jeeves.views.EveImages;
import com.tlabs.android.jeeves.views.R;
import com.tlabs.android.jeeves.views.ui.list.AbstractListRecyclerView;
import com.tlabs.android.jeeves.views.ui.list.AbstractListRowHolder;
import com.tlabs.android.jeeves.views.ui.list.ListRecyclerViewAdapter;
import com.tlabs.eve.api.Item;

public class ItemListWidget extends AbstractListRecyclerView<Item> {

    private static final class ItemHolder extends AbstractListRowHolder<Item> {
        private final ImageView icon;
        private final ImageView addIcon;
        private final TextView name;

        public ItemHolder(View itemView) {
            super(itemView);
            this.icon = findView(R.id.jeeves_itemCategoryIcon);
            this.addIcon = findView(R.id.jeeves_itemCategoryPlusIcon);
            this.name = findView(R.id.jeeves_itemCategoryName);
        }

        public void render(final Item item) {
            this.addIcon.setVisibility(View.GONE);
            this.name.setText(item.getName());
            EveImages.loadItemIcon(item.getItemID(), this.icon);
        }
    }

    public ItemListWidget(Context context) {
        super(context);
    }

    public ItemListWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ItemListWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected ListRecyclerViewAdapter.ViewHolder<Item> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(getContext()).inflate(R.layout.jeeves_row_item, parent, false));
    }

    @Override
    protected long getItemId(Item item) {
        return item.getItemID();
    }
}
