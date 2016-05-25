package com.tlabs.android.jeeves.views.items;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

import com.tlabs.eve.api.Item;

public class ShipTraitsWidget extends ExpandableListView implements ItemWidget {

    public ShipTraitsWidget(Context context) {
        super(context);
        init();
    }

    public ShipTraitsWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ShipTraitsWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public void setItem(Item item) {

    }

    private void init() {
        setAdapter(new ShipTraitsAdapter());
    }
}
