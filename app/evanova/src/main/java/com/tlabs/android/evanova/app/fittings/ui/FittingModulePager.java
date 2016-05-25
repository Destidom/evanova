package com.tlabs.android.evanova.app.fittings.ui;

import android.content.Context;
import android.util.AttributeSet;

import com.tlabs.android.jeeves.views.ui.pager.ViewPager;
import com.tlabs.android.jeeves.views.ui.pager.ViewPagerAdapter;

public class FittingModulePager extends ViewPager {

    public interface Listener {

        void onMarketGroupSelected(final long groupID);
        void onMarketItemSelected(final long itemID);
    }

    public static class FittingModulePagerAdapter extends ViewPagerAdapter {

        public FittingModulePagerAdapter(final Context context) {
            super(context);

        }

        protected void onMarketGroupSelected(final long groupID) {}

        protected void onMarketItemSelected(final long itemID) {}
/*
        protected void setRecentItems() {
            final FittingModuleItemFragment fRecent = getFragment(1);
            fRecent.setRecentItems();
        }
*/
    }

    private Listener listener;
/*
    @Override
    protected FittingModulePagerAdapter createAdapter(Bundle savedInstanceState) {
        return new FittingModulePagerAdapter(getContext()) {
            @Override
            protected void onMarketGroupSelected(long groupID) {
                if (null != listener) {
                    listener.onMarketGroupSelected(groupID);
                }
            }

            @Override
            protected void onMarketItemSelected(long itemID) {
                if (null != listener) {
                    listener.onMarketItemSelected(itemID);
                }
            }
        };
    }*/

    public void setListener(Listener listener) {
        this.listener = listener;
    }
/*
    @Override
    protected int getPagerID() {
        return R.id.pagerFittingModule;
    }*/

    public FittingModulePager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FittingModulePager(Context context) {
        super(context);
    }
}
