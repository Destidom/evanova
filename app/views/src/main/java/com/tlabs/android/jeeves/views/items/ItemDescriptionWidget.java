package com.tlabs.android.jeeves.views.items;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.tlabs.eve.api.Item;

public class ItemDescriptionWidget extends TextView implements ItemWidget {

    public ItemDescriptionWidget(Context context) {
        super(context);
    }

    public ItemDescriptionWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ItemDescriptionWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setItem(Item item) {
        this.setText(item.getDescription());
    }
}
