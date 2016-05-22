package com.tlabs.android.jeeves.views.items;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tlabs.android.jeeves.model.EveMarketGroup;
import com.tlabs.android.jeeves.views.EveImages;
import com.tlabs.android.jeeves.views.R;
import com.tlabs.android.jeeves.views.ui.list.AbstractListRecyclerView;
import com.tlabs.android.jeeves.views.ui.list.AbstractListRowHolder;
import com.tlabs.android.jeeves.views.ui.list.ListRecyclerViewAdapter;

public class MarketGroupListView extends AbstractListRecyclerView<EveMarketGroup> {

    private static final class MarketGroupHolder extends AbstractListRowHolder<EveMarketGroup> {
        private final ImageView icon;
        private final ImageView addIcon;
        private final TextView name;

        public MarketGroupHolder(View itemView) {
            super(itemView);
            this.icon = (ImageView)itemView.findViewById(R.id.jeeves_itemCategoryIcon);
            this.addIcon = (ImageView)itemView.findViewById(R.id.jeeves_itemCategoryPlusIcon);
            this.name = (TextView)itemView.findViewById(R.id.jeeves_itemCategoryName);
        }

        public void render(final EveMarketGroup group) {
            this.name.setText(group.getMarketGroupName());
            EveImages.loadItemIcon(group.getIconID(), this.icon);
            //this.addIcon.setVisibility(showAdd ? View.VISIBLE : View.GONE);
            this.addIcon.setVisibility(View.GONE);
        }
    }

    public MarketGroupListView(Context context) {
        super(context);
    }

    public MarketGroupListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MarketGroupListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected ListRecyclerViewAdapter.ViewHolder<EveMarketGroup> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MarketGroupHolder(LayoutInflater.from(getContext()).inflate(R.layout.jeeves_row_item, parent, false));
    }

    @Override
    protected long getItemId(EveMarketGroup item) {
        return item.getMarketGroupID();
    }
}
