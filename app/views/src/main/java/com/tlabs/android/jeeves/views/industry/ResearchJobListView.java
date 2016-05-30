package com.tlabs.android.jeeves.views.industry;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tlabs.android.jeeves.views.EveFormat;
import com.tlabs.android.jeeves.views.EveImages;
import com.tlabs.android.jeeves.views.R;
import com.tlabs.android.jeeves.views.ui.list.AbstractListRecyclerView;
import com.tlabs.android.jeeves.views.ui.list.AbstractListRowHolder;
import com.tlabs.android.jeeves.views.ui.list.ListRecyclerViewAdapter;
import com.tlabs.eve.api.character.ResearchJob;

public class ResearchJobListView extends AbstractListRecyclerView<ResearchJob> {

    static class ResearchHolder extends AbstractListRowHolder<ResearchJob> {

        private final TextView agentView;
        private final TextView researchView;
        private final TextView pointsRemainingView;
        private final TextView pointsPerDayView;
        private final TextView startDateView;
        private final TextView locationView;
        private final ImageView iconView;

        public ResearchHolder(final View view) {
            super(view);
            this.agentView = (TextView)view.findViewById(R.id.rowResearchAgent);
            this.researchView = (TextView)view.findViewById(R.id.rowResearchTitle);
            this.pointsRemainingView = (TextView)view.findViewById(R.id.rowResearchRemaining);
            this.pointsPerDayView = (TextView)view.findViewById(R.id.rowResearchPerDay);
            this.startDateView = (TextView)view.findViewById(R.id.rowResearchStartDate);
            this.locationView = (TextView)view.findViewById(R.id.rowResearchLocation);
            this.iconView = (ImageView)view.findViewById(R.id.rowResearchAgentIcon);
        }

        //I18N and some views
        public void render(final ResearchJob r) {
            this.startDateView.setText("Started on " + EveFormat.DateTime.YEAR(r.getStartDate()));
            this.pointsPerDayView.setText(EveFormat.Number.FLOAT(r.getPointsDaily()) + " points/day");
            this.pointsRemainingView.setText(EveFormat.Number.FLOAT(r.getCurrentPoints()) + " points");
            this.agentView.setText("Agent " + r.getAgentName());
            this.locationView.setText(r.getLocationName());
            this.researchView.setText(r.getSkillTypeName() + " " + r.getAgentLevel());

            EveImages.loadCharacterIcon(r.getAgentID(), this.iconView);
        }

    }
    public ResearchJobListView(Context context) {
        super(context);
    }

    public ResearchJobListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ResearchJobListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected ListRecyclerViewAdapter.ViewHolder<ResearchJob> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ResearchHolder(LayoutInflater.from(getContext()).inflate(R.layout.row_research_job, parent, false));
    }

    @Override
    protected long getItemId(ResearchJob item) {
        return item.getAgentID();
    }
}
