package com.tlabs.android.jeeves.views.fittings;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tlabs.android.jeeves.views.EveImages;
import com.tlabs.android.jeeves.views.ui.list.AbstractListRecyclerView;
import com.tlabs.android.jeeves.views.ui.list.ListRecyclerViewAdapter;
import com.tlabs.eve.dogma.model.Item;

public class FittingItemListWidget extends AbstractListRecyclerView<Item> {

    private static class ItemHolder extends ListRecyclerViewAdapter.ViewHolder<Item> {
        //@BindView(R.id.itemCategoryIcon)
        ImageView icon;

        //@BindView(R.id.itemCategoryName)
        TextView name;

        public ItemHolder(View itemView) {
            super(itemView);
        }

        public void render(final com.tlabs.eve.api.Item item) {
            this.name.setText(item.getName());
            EveImages.loadItemIcon(item.getItemID(), this.icon);
        }
    }

    public FittingItemListWidget(Context context) {
        super(context);
    }

    public FittingItemListWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FittingItemListWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected ListRecyclerViewAdapter.ViewHolder<Item> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.jeeves_row_item, parent, false));    }

    @Override
    protected long getItemId(Item item) {
        return item.getItemID();
    }
}
