package com.tlabs.android.evanova.app.corporations.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tlabs.android.evanova.R;
import com.tlabs.android.evanova.mvp.Presenter;
import com.tlabs.android.evanova.ui.ButtonMenuWidget;
import com.tlabs.android.jeeves.model.EveCorporation;
import com.tlabs.android.jeeves.views.corporation.CorporationMainWidget;

import javax.inject.Inject;

import butterknife.BindView;

public class CorporationViewFragment extends CorporationFragment {

    public static CorporationViewFragment newInstance(final long corpID) {
        Bundle bundle = new Bundle();
        bundle.putLong(CorporationPresenter.EXTRA_OWNER_ID, corpID);

        final CorporationViewFragment f = new CorporationViewFragment();
        f.setArguments(bundle);
        return f;
    }

    @Inject
    @Presenter
    CorporationPresenter presenter;

    @BindView(R.id.f_corporation_ButtonMenuWidget)
    ButtonMenuWidget wMenu;

    @BindView(R.id.f_corporation_MainWidget)
    CorporationMainWidget wMain;

    @Nullable
    @Override
    public View onCreateFragmentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.f_corporation_view, null);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.wMenu = (ButtonMenuWidget)view.findViewById(R.id.f_corporation_ButtonMenuWidget);
        this.wMenu.setListener(buttonId -> presenter.onCorporationMenuSelected(buttonId));
    }

    @Override
    public void onStart() {
        super.onStart();
        this.presenter.setCorporation(
                getArguments().getLong(CorporationPresenter.EXTRA_OWNER_ID, -1l));
    }

    @Override
    public void setCorporation(EveCorporation corporation) {
        wMain.setCorporation(corporation);
        wMenu.setCorporation(corporation);
    }

    @Override
    public void showCorporationInformation() {
        //FIXME bleh
        ((CorporationViewActivity)getActivity()).showCorporationInformation();
    }
}
