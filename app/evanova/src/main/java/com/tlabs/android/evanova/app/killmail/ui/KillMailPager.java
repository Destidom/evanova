package com.tlabs.android.evanova.app.killmail.ui;

import android.content.Context;
import android.util.AttributeSet;

import com.tlabs.android.evanova.R;
import com.tlabs.android.jeeves.views.killmail.KillMailAttackersWidget;
import com.tlabs.android.jeeves.views.killmail.KillMailDetailsWidget;
import com.tlabs.android.jeeves.views.killmail.KillMailItemsWidget;
import com.tlabs.android.jeeves.views.ui.pager.ViewPager;
import com.tlabs.android.jeeves.views.ui.pager.ViewPagerAdapter;
import com.tlabs.eve.zkb.ZKillMail;

class KillMailPager extends ViewPager {

    private KillMailAttackersWidget wAttackers;
    private KillMailItemsWidget wItems;
    private KillMailDetailsWidget wDetails;

    public KillMailPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public KillMailPager(Context context) {
        super(context);
        init();
    }

    public void setKillMail(final ZKillMail killMail) {
        this.wDetails.setKillMail(killMail);
        this.wAttackers.setItems(killMail.getAttackers());
        this.wItems.setItems(killMail.getItems());
    }

    private void init() {
        setId(R.id.pagerKillMail);

        final ViewPagerAdapter adapter = new ViewPagerAdapter(getContext());

        this.wDetails = new KillMailDetailsWidget(getContext());
        adapter.addView(wDetails, R.string.jeeves_killmail_title_details);

        this.wAttackers = new KillMailAttackersWidget(getContext());
        adapter.addView(wAttackers, R.string.jeeves_killmail_title_attackers);

        this.wItems = new KillMailItemsWidget(getContext());
        adapter.addView(wItems, R.string.jeeves_killmail_title_items);

        setAdapter(adapter);
    }
}
