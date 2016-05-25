package com.tlabs.android.jeeves.views.items;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

import com.tlabs.eve.api.Item;

public class ItemAttributesWidget extends ExpandableListView implements ItemWidget {

    private ItemAttributesAdapter adapter;

    public ItemAttributesWidget(Context context) {
        super(context);
        init();
    }

    public ItemAttributesWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ItemAttributesWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public void setItem(Item item) {

    }

    private void init() {
        setAdapter(new ItemAttributesAdapter());
    }
}
