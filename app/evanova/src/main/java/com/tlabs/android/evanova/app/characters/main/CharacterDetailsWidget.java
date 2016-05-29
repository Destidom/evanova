package com.tlabs.android.evanova.app.characters.main;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.tlabs.android.evanova.R;
import com.tlabs.android.jeeves.model.EveCharacter;
import com.tlabs.android.jeeves.views.character.CharacterAttributesWidget;
import com.tlabs.android.jeeves.views.character.CharacterClonesWidget;
import com.tlabs.android.jeeves.views.character.CharacterSummaryWidget;
import com.tlabs.android.jeeves.views.character.CharacterWidget;
import com.tlabs.android.jeeves.views.ui.pager.TabPager;
import com.tlabs.android.jeeves.views.ui.pager.ViewPagerAdapter;

class CharacterDetailsWidget extends TabPager implements CharacterWidget {

    public CharacterDetailsWidget(Context context) {
        super(context);
        init();
    }

    public CharacterDetailsWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CharacterDetailsWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public void setCharacter(EveCharacter character) {
        final ViewPagerAdapter adapter = getAdapter();
        for (View v: adapter.getViews()) {
            ((CharacterWidget)v).setCharacter(character);
        }
    }

    private void init() {
        final ViewPagerAdapter adapter = new ViewPagerAdapter(getContext());
        adapter.addView(new CharacterSummaryWidget(getContext()), R.string.character_pager_summary);
        adapter.addView(new CharacterAttributesWidget(getContext()), R.string.character_pager_attributes);
        adapter.addView(new CharacterClonesWidget(getContext()), R.string.character_pager_clones);

        setAdapter(adapter);
    }
}
