package com.tlabs.android.evanova.app.characters.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tlabs.android.evanova.R;

import com.tlabs.android.evanova.app.characters.CharacterFragment;
import com.tlabs.android.evanova.app.characters.CharacterDetailsView;
import com.tlabs.android.jeeves.model.EveCharacter;
import com.tlabs.android.jeeves.views.character.CharacterAttributesWidget;
import com.tlabs.android.jeeves.views.character.CharacterClonesWidget;
import com.tlabs.android.jeeves.views.character.CharacterSummaryWidget;
import com.tlabs.android.jeeves.views.character.CharacterWidget;
import com.tlabs.android.jeeves.views.ui.pager.TabPager;
import com.tlabs.android.jeeves.views.ui.pager.ViewPagerAdapter;

public class CharacterDetailsFragment extends CharacterFragment implements CharacterDetailsView {

    private static class CharacterDetailsPager extends TabPager {

        private ViewPagerAdapter adapter;

        public CharacterDetailsPager(Context context) {
            super(context);
            init();
        }

        private void init() {
            setId(R.id.pagerCharacterSummary);

            this.adapter = new ViewPagerAdapter(getContext());
            this.adapter.addView(new CharacterSummaryWidget(getContext()), R.string.character_pager_summary);
            this.adapter.addView(new CharacterAttributesWidget(getContext()), R.string.character_pager_attributes);
            this.adapter.addView(new CharacterClonesWidget(getContext()), R.string.character_pager_clones);

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
    public View onCreateFragmentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.pager = new CharacterDetailsPager(getContext());
        return this.pager;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}
