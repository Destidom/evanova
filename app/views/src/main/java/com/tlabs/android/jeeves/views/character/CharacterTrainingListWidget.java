package com.tlabs.android.jeeves.views.character;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tlabs.android.jeeves.model.EveCharacter;
import com.tlabs.android.jeeves.model.EveTraining;
import com.tlabs.android.jeeves.views.EveFormat;
import com.tlabs.android.jeeves.views.R;
import com.tlabs.android.jeeves.views.skills.SkillLevelWidget;
import com.tlabs.android.jeeves.views.ui.list.AbstractListRecyclerView;
import com.tlabs.android.jeeves.views.ui.list.AbstractListRowHolder;
import com.tlabs.android.jeeves.views.ui.list.ListRecyclerViewAdapter;
import com.tlabs.eve.api.character.SkillInTraining;

public class CharacterTrainingListWidget extends AbstractListRecyclerView<SkillInTraining> implements CharacterWidget {

    private static class SkillTrainingHolder extends AbstractListRowHolder<SkillInTraining> {

        private TextView skillNameView;
        private TextView remainingView;
        private TextView endsOnView;
        private ProgressBar timeProgress;
        private SkillLevelWidget trainingLevelView;

        private EveTraining training;

        public SkillTrainingHolder(View itemView, EveTraining training) {
            super(itemView);

            this.skillNameView = (TextView)itemView.findViewById(R.id.j_character_training_RowNameText);
            this.remainingView = (TextView)itemView.findViewById(R.id.j_character_training_RowRemainingText);
            this.endsOnView = (TextView)itemView.findViewById(R.id.j_character_training_RowEndsText);
            this.timeProgress = (ProgressBar)itemView.findViewById(R.id.j_character_training_RowTimeProgress);
            this.trainingLevelView = (SkillLevelWidget)itemView.findViewById(R.id.j_character_training_RowSkillLevelView);
            this.training = training;
        }

        @Override
        public void render(SkillInTraining skill) {
            this.skillNameView.setText(skill.getSkillName());
            this.endsOnView.setText("Ends " + EveFormat.DateTime.MEDIUM(skill.getEndTime()));//I18N

            if (skill.getTrainingType() == SkillInTraining.TYPE_QUEUE) {
                boolean first = training.getActiveTraining() == skill;
                this.trainingLevelView.setTrainDrawable(R.drawable.s_blue);
                this.trainingLevelView.setSkillLevel(training.getSkillLevel(skill.getSkillID()), skill.getSkillLevel(), first);
                if (first) {
                    this.timeProgress.setVisibility(View.VISIBLE);
                    final double duration = skill.getEndTime() - skill.getStartTime();
                    final double left = skill.getEndTime() - System.currentTimeMillis();
                    final double progress = left / duration * 100d;
                    this.remainingView.setText(EveFormat.Duration.MEDIUM(Math.max(0, Math.round(left))));
                    timeProgress.setProgress(Math.max(0, 100 - (int)progress));
                }
                else {
                    final long remaining = skill.getEndTime() - skill.getStartTime();
                    this.remainingView.setText(EveFormat.Duration.MEDIUM(remaining));
                    this.remainingView.setTextColor(Color.LTGRAY);
                    this.timeProgress.setVisibility(View.GONE);
                }
            }
            else {
                final long remaining = skill.getEndTime() - skill.getStartTime();
                this.remainingView.setText(EveFormat.Duration.MEDIUM(remaining));
                this.remainingView.setTextColor(Color.LTGRAY);
                this.timeProgress.setVisibility(View.GONE);
                this.endsOnView.setTextColor(Color.LTGRAY);

                this.trainingLevelView.setTrainDrawable(R.drawable.s_orange);
                this.trainingLevelView.setSkillLevel(training.getSkillLevel(skill.getSkillID()), skill.getSkillLevel());
            }
        }
    }

    private EveTraining training;

    public CharacterTrainingListWidget(Context context) {
        super(context);
        init();
    }

    public CharacterTrainingListWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CharacterTrainingListWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected ListRecyclerViewAdapter.ViewHolder<SkillInTraining> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SkillTrainingHolder(
                LayoutInflater.from(getContext()).inflate(R.layout.jeeves_row_character_training, parent, false),
                this.training);
    }

    @Override
    protected long getItemId(SkillInTraining item) {

        return (item.getSkillID() << 64) + item.getSkillLevel();
    }

    @Override
    public void setCharacter(EveCharacter character) {
        setTraining(character.getTraining());
    }

    public void setTraining(final EveTraining training) {
        this.training = training;
        setItems(training.getAll());
    }

    private void init() {}

}
