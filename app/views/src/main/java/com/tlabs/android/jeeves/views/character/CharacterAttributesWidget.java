package com.tlabs.android.jeeves.views.character;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.tlabs.android.jeeves.model.EveCharacter;

public class CharacterAttributesWidget extends FrameLayout implements CharacterWidget {

    public CharacterAttributesWidget(Context context) {
        super(context);
    }

    public CharacterAttributesWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CharacterAttributesWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setCharacter(EveCharacter character) {

    }
}
