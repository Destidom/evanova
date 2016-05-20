package com.tlabs.android.evanova.app.character.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tlabs.android.evanova.R;
import com.tlabs.android.jeeves.model.EveCharacter;
import com.tlabs.android.jeeves.views.character.CharacterAttributesWidget;
import com.tlabs.android.jeeves.views.character.CharacterClonesWidget;
import com.tlabs.android.jeeves.views.character.CharacterSummaryWidget;
import com.tlabs.android.jeeves.views.character.CharacterWidget;
import com.tlabs.android.jeeves.views.ui.pager.ViewPager;
import com.tlabs.android.jeeves.views.ui.pager.ViewPagerAdapter;

public class CharacterDetailsFragment extends CharacterFragment {

    private static class CharacterDetailsPager extends ViewPager {

        private ViewPagerAdapter adapter;

        public CharacterDetailsPager(Context context) {
            super(context);
            init();
        }

        private void init() {
            setId(R.id.pagerCharacterSummary);

            this.adapter = new ViewPagerAdapter(getContext());
            this.adapter.addView(new CharacterSummaryWidget(getContext()), R.string.character_pager_summary);
            this.adapter.addView(new CharacterAttributesWidget(getContext()), R.string.character_pager_summary);
            this.adapter.addView(new CharacterClonesWidget(getContext()), R.string.character_pager_summary);

            setAdapter(this.adapter);
        }

        public void setCharacter(final EveCharacter character) {
            for (View v: this.adapter.getViews()) {
                ((CharacterWidget)v).setCharacter(character);
            }
        }
    }

    private CharacterDetailsPager pager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.pager = new CharacterDetailsPager(getContext());
        return this.pager;
    }

    @Override
    protected void onCharacterChanged(EveCharacter character) {
        this.pager.setCharacter(character);
    }
}
