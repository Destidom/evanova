package com.tlabs.android.evanova.app.characters.main;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.tlabs.android.evanova.R;
import com.tlabs.android.evanova.ui.ButtonMenuWidget;
import com.tlabs.android.jeeves.model.EveCharacter;
import com.tlabs.android.jeeves.views.character.CharacterInfoWidget;
import com.tlabs.android.jeeves.views.character.CharacterTrainingDetailsWidget;
import com.tlabs.android.jeeves.views.character.CharacterWidget;

import butterknife.BindView;
import butterknife.ButterKnife;

class CharacterMainWidget extends FrameLayout implements CharacterWidget {

    public interface Listener {

        void onCharacterDetailsSelected();

        void onCharacterTrainingSelected();

        void onCharacterMenuSelected(int buttonId);
    }

    @BindView(R.id.f_character_CharacterInfoWidget)
    CharacterInfoWidget wInfo;

    @BindView(R.id.f_character_CharacterTrainingDetailsWidget)
    CharacterTrainingDetailsWidget wTraining;

    @BindView(R.id.f_character_ButtonMenuWidget)
    ButtonMenuWidget wMenu;

    private Listener listener;

    public CharacterMainWidget(Context context) {
        super(context);
        init();
    }

    public CharacterMainWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CharacterMainWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public void setCharacter(EveCharacter character) {
        this.wMenu.setCharacter(character);
        this.wInfo.setCharacter(character);
        this.wTraining.setCharacter(character);
    }

    private void init() {
        inflate(getContext(), R.layout.f_character_view, this);
        ButterKnife.bind(this);

        this.wMenu.setListener(buttonId -> {if (null != listener) listener.onCharacterMenuSelected(buttonId);});
        this.wInfo.setListener(() -> {if (null != listener) listener.onCharacterDetailsSelected();});
        this.wTraining.setListener(() -> {if (null != listener) listener.onCharacterTrainingSelected();});
    }
}
