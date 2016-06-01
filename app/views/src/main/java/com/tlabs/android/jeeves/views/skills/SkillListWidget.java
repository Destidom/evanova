package com.tlabs.android.jeeves.views.skills;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

import com.tlabs.android.jeeves.model.EveCharacter;
import com.tlabs.android.jeeves.views.ui.list.SingleListGroupDisplayAdapter;
import com.tlabs.eve.api.Skill;
import com.tlabs.eve.api.SkillTree;

public class SkillListWidget extends ExpandableListView {

    public static final int VIEW_ALL = SkillTreeAdapter.SHOW_ALL;
    public static final int VIEW_CURRENT = SkillTreeAdapter.SHOW_CURRENT;
    public static final int VIEW_TRAINABLE = SkillTreeAdapter.SHOW_TRAINABLE;

    public interface Listener {

        void onSkillSelected(final Skill skill, final boolean selected);

    }

    private SkillListWidget.Listener listener;

    public SkillListWidget(Context context) {
        super(context);
        init();
    }

    public SkillListWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SkillListWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setSkills(final SkillTree tree) {
        setAdapter(new SkillTreeAdapter(tree));
    }

    public void setViewType(int type) {
        final SkillTreeAdapter adapter = (SkillTreeAdapter)getExpandableListAdapter();
        adapter.setShow(type);
    }

    public void setCharacter(final EveCharacter character) {
        final SkillTreeAdapter adapter = (SkillTreeAdapter)getExpandableListAdapter();
        adapter.setCharacter(character);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    private void init() {
        setOnItemLongClickListener((parent, view, position, id) -> {
            if (null == listener) {
                return false;
            }
            final Object item = parent.getItemAtPosition(position);
            if (item instanceof Skill) {
                listener.onSkillSelected((Skill)item, true);
                return true;
            }
            return false;
        });

        setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            if (null == listener) {
                return false;
            }
            listener.onSkillSelected(
                    (Skill)parent.getExpandableListAdapter().getChild(groupPosition, childPosition), true);
            return true;
        });

        setOnGroupExpandListener(new SingleListGroupDisplayAdapter(this));
        setAdapter(new SkillTreeAdapter(new SkillTree()));
    }
}
