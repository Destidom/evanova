package com.tlabs.android.jeeves.views.character;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tlabs.android.jeeves.model.EveCharacter;
import com.tlabs.android.jeeves.model.EveTraining;
import com.tlabs.android.jeeves.views.EveFormat;
import com.tlabs.android.jeeves.views.R;
import com.tlabs.eve.api.character.SkillInTraining;

public class CharacterTrainingDetailsWidget extends FrameLayout implements CharacterWidget {

    public interface Listener {
        void onTrainingDetailsSelected();
    }

    private TextView trainingSkillText;
    private TextView trainingSkillTimeText;
    private TextView trainingRemainingText;
    private TextView trainingEndText;
    private TextView trainingPlanningText;

    private ProgressBar trainingProgress;
    private View trainingSelectionBox;
    private View trainingDetailsBox;

    private Listener listener;

    public CharacterTrainingDetailsWidget(Context context) {
        super(context);
        init();
    }

    public CharacterTrainingDetailsWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CharacterTrainingDetailsWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setMoreDetails(boolean visible) {
        this.trainingDetailsBox.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setCharacter(EveCharacter character) {
        setTraining(character.getTraining());
    }

    public void setTraining(final EveTraining training) {
        if (null == training || training.isEmpty()) {
            renderEmpty();
            return;
        }
        final SkillInTraining first = training.getTraining(0);
        if (first.getTrainingType() == SkillInTraining.TYPE_QUEUE) {
            renderQueue(training);
        }
        else {
            renderEmpty();
        }
        renderPlanning(training);
    }

    private void init() {
        inflate(getContext(), R.layout.jeeves_view_character_training_details, this);

        this.trainingSkillText = (TextView)findViewById(R.id.j_character_training_DetailSkillText);
        this.trainingSkillTimeText = (TextView)findViewById(R.id.j_character_training_DetailSkillTimeText);
        this.trainingRemainingText = (TextView)findViewById(R.id.j_character_training_DetailRemainingText);
        this.trainingEndText = (TextView)findViewById(R.id.j_character_training_DetailEndText);
        this.trainingPlanningText = (TextView)findViewById(R.id.j_character_training_DetailPlanningText);
        this.trainingProgress = (ProgressBar)findViewById(R.id.j_character_training_DetailProgress);
        this.trainingSelectionBox = findViewById(R.id.j_character_training_DetailSelectionBox);
        this.trainingDetailsBox = findViewById(R.id.j_character_training_DetailsBox);

        this.trainingProgress.setProgress(0);
        this.trainingProgress.setMax(100);

        this.trainingSelectionBox.setOnClickListener(v -> {
            if (null != listener) {
                listener.onTrainingDetailsSelected();
            }
        });
    }

    private void renderEmpty() {
        this.trainingSkillText.setText("");
        this.trainingSkillTimeText.setText("");
        this.trainingProgress.setProgress(0);

        this.trainingRemainingText.setText(R.string.jeeves_characters_training_none);
        this.trainingRemainingText.setTextColor(Color.GRAY);

        this.trainingEndText.setText("");
        this.trainingEndText.setTextColor(Color.GRAY);

        this.trainingPlanningText.setText(R.string.jeeves_characters_planning_none);
    }

    private void renderQueue(final EveTraining training) {
        final SkillInTraining active = training.getActiveTraining();
        if (null == active) {
            renderEmpty();
            return;
        }
        this.trainingSkillText.setText(active.getSkillName() + " " + active.getSkillLevel());
        this.trainingSkillTimeText.setText(EveFormat.Duration.MEDIUM(active.getEndTime() - System.currentTimeMillis()));

        final long end = training.getEndTime(SkillInTraining.TYPE_QUEUE);
        final long duration = Math.max(0, end - System.currentTimeMillis());
        final int durationColor = EveFormat.getDurationColor(duration);
        final int queueCount = training.getCount(SkillInTraining.TYPE_QUEUE);

        this.trainingRemainingText.setText(EveFormat.Duration.MEDIUM(duration));
        this.trainingRemainingText.setTextColor(durationColor);

        this.trainingEndText.setTextColor(durationColor);
        if (queueCount == 1) {
            this.trainingEndText.setText(r(
                    this.trainingEndText,
                    R.string.jeeves_characters_training_one,
                    EveFormat.DateTime.SHORT(end)));
        }
        else {
            this.trainingEndText.setText(r(
                    this.trainingEndText,
                    R.string.jeeves_characters_training_many,
                    queueCount,
                    EveFormat.DateTime.SHORT(end)));
        }
        final int progress = (int) (duration * 100L / (24L * 3600L * 1000L));
        this.trainingProgress.setProgress(Math.min(progress, 100));
    }

    private void renderPlanning(final EveTraining training) {
        final int plannedCount = training.getCount(SkillInTraining.TYPE_PLAN);
        switch (plannedCount) {
            case 0: {
                this.trainingPlanningText.setText(R.string.jeeves_characters_planning_none);
                this.trainingPlanningText.setTextColor(Color.GRAY);
                break;
            }
            case 1: {
                this.trainingPlanningText.setTextColor(Color.WHITE);
                this.trainingPlanningText.setText(r(
                        this.trainingPlanningText,
                        R.string.jeeves_characters_planning_one,
                        EveFormat.DateTime.SHORT(training.getEndTime())));
                break;
            }
            default: {
                this.trainingPlanningText.setTextColor(Color.WHITE);
                this.trainingPlanningText.setText(r(
                        this.trainingPlanningText,
                        R.string.jeeves_characters_planning_many,
                        plannedCount,
                        EveFormat.DateTime.SHORT(training.getEndTime())));
                break;
            }
        }
    }

    private static String r(final View view, final int id, final Object... format) {
        String s = view.getResources().getString(id);
        if (null == format || format.length == 0) {
            return s;
        }
        return String.format(s, format);
    }
}
