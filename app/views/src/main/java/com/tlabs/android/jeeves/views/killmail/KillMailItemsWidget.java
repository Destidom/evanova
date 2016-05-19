package com.tlabs.android.jeeves.views.killmail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tlabs.android.jeeves.views.EveImages;
import com.tlabs.android.jeeves.views.EveFormat;
import com.tlabs.android.jeeves.views.R;
import com.tlabs.android.jeeves.views.ui.list.AbstractListRecyclerView;
import com.tlabs.android.jeeves.views.ui.list.AbstractListRowHolder;
import com.tlabs.android.jeeves.views.ui.list.ListRecyclerViewAdapter;
import com.tlabs.eve.api.mail.KillMailItem;

public class KillMailItemsWidget extends AbstractListRecyclerView<KillMailItem> {

    private final class ItemHolder extends AbstractListRowHolder<KillMailItem> {
        private ImageView imageView;
        private TextView itemNameView;
        private TextView droppedView;
        private TextView destroyedView;

        private ItemHolder(View view) {
            super(view);
            this.itemNameView = (TextView)itemView.findViewById(R.id.j_killmail_item_ItemName);
            this.droppedView = (TextView)itemView.findViewById(R.id.j_killmail_item_ItemDropped);
            this.destroyedView = (TextView)itemView.findViewById(R.id.j_killmail_item_ItemDestroyed);
            this.imageView = (ImageView)itemView.findViewById(R.id.j_killmail_item_ItemIcon);
        }

        @Override
        public void render(final KillMailItem item) {
            EveImages.loadItemIcon(item.getTypeID(), imageView);
            itemNameView.setText(item.getTypeName());
            droppedView.setText(EveFormat.Number.LONG(item.getDropped()));
            destroyedView.setText(EveFormat.Number.LONG(item.getDestroyed()));
        }
    }

    public KillMailItemsWidget(Context context) {
        super(context);
    }

    public KillMailItemsWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public KillMailItemsWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected ListRecyclerViewAdapter.ViewHolder<KillMailItem> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.jeeves_row_killmail_item, parent, false));
    }

    @Override
    protected long getItemId(KillMailItem item) {
        return item.getTypeID();
    }
}
