package com.tlabs.android.jeeves.views.skills;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;
import android.widget.ViewFlipper;

import com.tlabs.eve.api.Skill;

import java.util.ArrayList;
import java.util.List;

public class SkillRequiredWidget extends ViewFlipper implements SkillWidget {

    public interface Listener {
        void onItemSelected(final long itemId);
    }

    private List<SkillRequiredAdapter> adapters;
    private SkillRequiredWidget.Listener listener;

    public SkillRequiredWidget(Context context) {
        super(context);
        init();
    }

    public SkillRequiredWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public void setSkill(Skill skill) {

    }

    private void init() {
        this.adapters = new ArrayList<>(5);
        for (int i = 0; i < 5; i++) {
            this.adapters.add(new SkillRequiredAdapter());
        }
        for (int i = 0; i < 5; i++) {
            addSkillView(i);
        }
    }

    private void addSkillView(final int index) {
        final ExpandableListView listView = new ExpandableListView(getContext());
        listView.setOnItemClickListener((parent, view, position, id) -> {
            if (null != listener) {
                listener.onItemSelected(id);
            }
        });
        listView.setAdapter(this.adapters.get(index));
        addView(listView);
    }

}
