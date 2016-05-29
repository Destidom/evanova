package com.tlabs.android.jeeves.views.fittings;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.tlabs.android.jeeves.views.EveImages;
import com.tlabs.android.jeeves.views.ui.list.AbstractListRecyclerView;
import com.tlabs.android.jeeves.views.ui.list.AbstractListRowHolder;
import com.tlabs.android.jeeves.views.ui.list.ListRecyclerViewAdapter;
import com.tlabs.eve.dogma.Fitting;

public class FittingListWidget extends AbstractListRecyclerView<Fitting> {

    private static class FittingHolder extends AbstractListRowHolder<Fitting> {
        public FittingHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void render(Fitting fitting) {
            EveImages.loadItemIcon(fitting.getTypeID(), this.portraitImage);
            this.text1.setText(fitting.getName());
            this.text2.setText(fitting.getTypeName());
            this.text3.setText(fitting.getDescription());
        }
    }
    public FittingListWidget(Context context) {
        super(context);
    }

    public FittingListWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FittingListWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected ListRecyclerViewAdapter.ViewHolder<Fitting> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FittingHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.jeeves_row_character, parent, false));
    }

    @Override
    protected long getItemId(Fitting item) {
        return item.getId();
    }

}
