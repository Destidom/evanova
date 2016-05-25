package com.tlabs.android.evanova.app.items.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tlabs.android.evanova.mvp.BaseFragment;
import com.tlabs.android.jeeves.views.items.ItemAttributesWidget;
import com.tlabs.android.jeeves.views.items.ItemDescriptionWidget;
import com.tlabs.android.jeeves.views.items.ItemRequirementsWidget;
import com.tlabs.android.jeeves.views.items.ItemWidget;
import com.tlabs.android.jeeves.views.items.ShipTraitsWidget;
import com.tlabs.android.jeeves.views.ui.pager.TabPager;
import com.tlabs.android.jeeves.views.ui.pager.ViewPagerAdapter;
import com.tlabs.eve.api.Item;

import org.apache.commons.lang3.StringUtils;

public class ItemFragment extends BaseFragment {

    private static class ItemAdapter extends ViewPagerAdapter {
        public ItemAdapter(Context context) {
            super(context);
        }

        public void setItem(final Item item) {
            removeAllViews();
            if (null == item) {
                return;
            }

            if (Item.isShip(item)) {
                addView(new ShipTraitsWidget(getContext()), 0);
                addView(new ItemAttributesWidget(getContext()), 0);
                addView(new ItemRequirementsWidget(getContext()), 0);
                addView(new ItemDescriptionWidget(getContext()), 0);
            }
            else if (StringUtils.endsWithIgnoreCase(item.getName(), "blueprint")) {
                addView(new ItemDescriptionWidget(getContext()), 0);
            }
            else {
                addView(new ItemAttributesWidget(getContext()), 0);
                addView(new ItemRequirementsWidget(getContext()), 0);
                addView(new ItemDescriptionWidget(getContext()), 0);
            }
            notifyDataSetChanged();
            for (View v: getViews()) {
                ((ItemWidget)v).setItem(item);
            }
        }
    }

    private Item item;
    private ItemAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.adapter = new ItemAdapter(getContext());
    }

    @Nullable
    @Override
    public View onCreateFragmentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TabPager pager = new TabPager(getContext());
        pager.setAdapter(this.adapter);
        this.adapter.setItem(item);
        return pager;
    }

    public void setItem(final Item item) {
        this.item = item;
        if (null != this.adapter) {
            this.adapter.setItem(item);
        }
    }
}
