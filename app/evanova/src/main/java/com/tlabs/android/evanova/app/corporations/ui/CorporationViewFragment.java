package com.tlabs.android.evanova.app.corporations.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tlabs.android.evanova.R;
import com.tlabs.android.evanova.ui.ButtonMenuWidget;
import com.tlabs.android.jeeves.model.EveCorporation;

public class CorporationViewFragment extends CorporationFragment {


    private ButtonMenuWidget wMenu;

    @Nullable
    @Override
    public View onCreateFragmentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.f_corporation_view, null);

        this.wMenu = (ButtonMenuWidget)view.findViewById(R.id.f_corporation_ButtonMenuWidget);
        this.wMenu.setListener(buttonId -> presenter.onCorporationMenuSelected(buttonId));
        return view;
    }

    @Override
    protected void onCorporationChanged(EveCorporation corporation) {
        wMenu.setCorporation(corporation);
    }
}
