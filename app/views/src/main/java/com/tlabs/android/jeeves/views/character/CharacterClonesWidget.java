package com.tlabs.android.jeeves.views.character;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tlabs.android.jeeves.model.EveCharacter;
import com.tlabs.android.jeeves.views.EveFormat;
import com.tlabs.eve.api.character.CharacterSheet;

public class CharacterClonesWidget extends FrameLayout implements CharacterWidget {

    public interface Listener {
        void onImplantSelected(CharacterSheet.Implant implant);
    }


    private ExpandableListView listView;

    private TextView jumpLastText;
    private TextView jumpAgoText;

    private Listener listener;

    public CharacterClonesWidget(Context context) {
        super(context);
        init();
    }

    public CharacterClonesWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CharacterClonesWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public void setCharacter(EveCharacter character) {
        updateView(character);
    }

    private void init() {
        inflate(getContext(), R.layout.jeeves_view_character_clones, this);

        this.listView = (ExpandableListView)findViewById(R.id.j_character_CloneList);
        this.listView.setAdapter(new CharacterClonesAdapter());

        this.listView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            if (null == listener) {
                return false;
            }

            final CharacterSheet.Implant implant = (CharacterSheet.Implant)parent.getExpandableListAdapter().getChild(groupPosition, childPosition);
            listener.onImplantSelected(implant);
            return true;
        });

        this.jumpLastText = (TextView)findViewById(R.id.j_character_CloneLastJump);
        this.jumpAgoText = (TextView)findViewById(R.id.j_character_CloneJumpAgo);
    }

    private void updateView(final EveCharacter character) {
        if (null == character) {
            return;
        }
        if (null != this.listView) {
            final CharacterClonesAdapter adapter = (CharacterClonesAdapter) this.listView.getExpandableListAdapter();
            adapter.setJumpClones(character.getJumpClones());
        }
        if (null != this.jumpLastText) {
            if (character.getCloneJumpDate() == 0) {
                this.jumpLastText.setText("Last Jump unknown");//I18N
            }
            else {
                this.jumpLastText.setText("Last Jump on " + EveFormat.DateTime.PLAIN(character.getCloneJumpDate()));//I18N
            }
        }
        if (null != this.jumpAgoText) {
            if (character.getCloneJumpDate() == 0) {
                this.jumpAgoText.setText("");
            }
            else {
                final long since = System.currentTimeMillis() - character.getCloneJumpDate();
                this.jumpAgoText.setText(EveFormat.Duration.MEDIUM(since) + " ago.");
            }
        }
    }

}
