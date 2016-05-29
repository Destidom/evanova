package com.tlabs.android.evanova.app.corporations.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tlabs.android.evanova.mvp.Presenter;
import com.tlabs.android.jeeves.views.corporation.CorporationInformationWidget;
import com.tlabs.android.jeeves.views.corporation.CorporationMemberListWidget;
import com.tlabs.android.jeeves.views.mails.KillMailListWidget;
import com.tlabs.android.jeeves.views.ui.pager.TabPager;
import com.tlabs.android.jeeves.views.ui.pager.ViewPagerAdapter;

import javax.inject.Inject;

public class CorporationInfoFragment extends CorporationFragment {

    public static CorporationInfoFragment newInstance(final long corpID) {
        Bundle bundle = new Bundle();
        bundle.putLong(CorporationPresenter.EXTRA_OWNER_ID, corpID);

        final CorporationInfoFragment f = new CorporationInfoFragment();
        f.setArguments(bundle);
        return f;
    }

    private static class CorporationInfoAdapter extends ViewPagerAdapter {
        public CorporationInfoAdapter(Context context) {
            super(context);
            addView(new CorporationInformationWidget(context), 0);
            addView(new CorporationMemberListWidget(context), 0);
            addView(new KillMailListWidget(context), 0);
        }
    }

    @Inject
    @Presenter
    CorporationPresenter presenter;

    private TabPager pager;

    @Nullable
    @Override
    public View onCreateFragmentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.pager = new TabPager(getContext());
        this.pager.setAdapter(new CorporationInfoAdapter(getContext()));
        return this.pager;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        this.presenter.setCorporation(
                getArguments().getLong(CorporationPresenter.EXTRA_OWNER_ID, -1l));
    }

}
