package com.tlabs.android.jeeves.views.character;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tlabs.android.jeeves.model.EveCharacter;

import com.tlabs.android.jeeves.views.EveImages;
import com.tlabs.android.jeeves.views.EveFormat;
import com.tlabs.android.jeeves.views.R;
import com.tlabs.android.jeeves.views.Strings;
import com.tlabs.android.jeeves.views.ui.list.AbstractListRecyclerView;
import com.tlabs.android.jeeves.views.ui.list.AbstractListRowHolder;
import com.tlabs.android.jeeves.views.ui.list.ListRecyclerViewAdapter;
import com.tlabs.eve.api.character.SkillInTraining;

public class CharacterListWidget extends AbstractListRecyclerView<EveCharacter>
        implements AbstractListRecyclerView.Draggable, AbstractListRecyclerView.Timed {

    public static final int VIEW_TRAINING = 0;
    public static final int VIEW_LOCATION = 1;

    public static class Listener implements AbstractListRecyclerView.Listener<EveCharacter> {
        @Override
        public void onItemClicked(EveCharacter character) {

        }

        @Override
        public void onItemSelected(EveCharacter character, boolean selected) {

        }

        @Override
        public void onItemMoved(EveCharacter character, int from, int to) {

        }
    }

    private static class CharacterHolder extends AbstractListRowHolder<EveCharacter> {

        protected int viewType;

        public CharacterHolder(View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;
        }

        @Override
        public void render(EveCharacter character) {
            progress.setVisibility(updating ? View.VISIBLE : View.GONE);
            EveImages.loadCharacterIcon(character.getID(), portraitImage);

            text1.setText(character.getName());
            text2.setText(character.getCorporationName());

            text4.setVisibility(View.VISIBLE);
            text5.setVisibility(View.VISIBLE);

            text3.setText(EveFormat.Currency.LONG(character.getBalance()));
            text3.setTextColor(Color.GRAY);

            switch (this.viewType) {
                case VIEW_LOCATION:
                    renderLocation(character);
                    break;
                case VIEW_TRAINING:
                default:
                    renderTraining(character);
                    break;
            }

            boolean hasSSO = character.hasCrest();
            boolean hasApi = character.hasApiKey();
            if (hasSSO && hasApi) {
                this.crestImage.setImageResource(R.drawable.ic_crest_enabled);
                this.crestImage.setVisibility(VISIBLE);
            }
            else if (hasSSO) {
                this.crestImage.setImageResource(R.drawable.ic_crest_disabled);
                this.crestImage.setVisibility(VISIBLE);
            }
            else {
                this.crestImage.setVisibility(INVISIBLE);
            }

        }

        private void renderLocation(EveCharacter character) {
            text5.setTextColor(Color.WHITE);
            text5.setText(character.getLocation().getLocationName());

            text4.setTextColor(Color.LTGRAY);
            Strings.r(
                    text4,
                    R.string.jeeves_characters_ship,
                    character.getShipName(),
                    character.getShipTypeName());

            final SkillInTraining t = character.getTraining().getActiveTraining();
            final long trainingEndTime = character.getTraining().getEndTime();
            if (null == t || trainingEndTime <= 0) {
                text1.setTextColor(Color.WHITE);
                return;
            }
            final long remainingQueueTime = trainingEndTime - System.currentTimeMillis();
            text1.setTextColor(EveFormat.getDurationColor(remainingQueueTime));
        }

        private void renderTraining(EveCharacter character) {
            final SkillInTraining t = character.getTraining().getActiveTraining();
            if (null == t) {
                text1.setTextColor(Color.WHITE);
                text5.setTextColor(Color.GRAY);
                text5.setText(R.string.jeeves_characters_training_inactive);
                text4.setText("");
                return;
            }

            final long remainingQueueTime = character.getTraining().getEndTime(SkillInTraining.TYPE_QUEUE) - System.currentTimeMillis();
            text1.setTextColor(EveFormat.getDurationColor(remainingQueueTime));
            text5.setTextColor(Color.WHITE);
            Strings.r(
                    text5,
                R.string.jeeves_characters_skill,
                t.getSkillName(),
                t.getSkillLevel());

            final long remainingTrainingTime = t.getEndTime() - System.currentTimeMillis();
            text4.setTextColor(EveFormat.getDurationColor(remainingQueueTime));
            text4.setText(EveFormat.Duration.MEDIUM(remainingTrainingTime));
        }
    }

    private int viewType = VIEW_TRAINING;

    public CharacterListWidget(Context context) {
        super(context);
    }

    public CharacterListWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CharacterListWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setViewType(final int viewType) {
        this.viewType = viewType;
        notifyDataSetChanged();
    }

    @Override
    public boolean canMove(int from, int to) {
        return false;
    }

    @Override
    public int getTimerInterval() {
        return 0;
    }

    @Override
    protected ListRecyclerViewAdapter.ViewHolder<EveCharacter> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CharacterHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.jeeves_row_character, parent, false),
                this.viewType);
    }

    @Override
    protected long getItemId(EveCharacter item) {
        return item.getID();
    }
}
