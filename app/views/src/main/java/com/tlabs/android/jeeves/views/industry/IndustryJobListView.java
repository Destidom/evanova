package com.tlabs.android.jeeves.views.industry;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tlabs.android.jeeves.views.EveFormat;
import com.tlabs.android.jeeves.views.ui.list.AbstractListRecyclerView;
import com.tlabs.android.jeeves.views.ui.list.AbstractListRowHolder;
import com.tlabs.android.jeeves.views.ui.list.ListRecyclerViewAdapter;
import com.tlabs.eve.api.IndustryJob;

public class IndustryJobListView extends AbstractListRecyclerView<IndustryJob> {

    private static class IndustryJobHolder extends AbstractListRowHolder<IndustryJob> {
        private final TextView titleText;
        private final TextView nameText;
        private final TextView ownerText;
        private final TextView text2;
        private final TextView text3;

        private IndustryJobHolder(final View view) {
            super(view);
            this.titleText = (TextView)view.findViewById(R.id.rowIndustryTitle);
            this.nameText = (TextView)view.findViewById(R.id.rowIndustryName);
            this.ownerText = (TextView)view.findViewById(R.id.rowIndustryOwner);
            this.text2 = (TextView)view.findViewById(R.id.rowIndustryText2);
            this.text3 = (TextView)view.findViewById(R.id.rowIndustryText3);

        }

        //I18N
        public void render(final IndustryJob job) {
            IndustryJob.Type jobType = job.getType();
            this.titleText.setText(jobType.getText() /*+  " - " + job.getRuns() + " run(s)"*/);
            this.nameText.setText(job.getBlueprintTypeName());
            this.ownerText.setText(job.getInstallerName());

            //I18N
            switch (job.getStatus()) {
                case FAILED:
                case ABORTED:
                case ABORTED_GM:
                case DESTROYED:
                case INFLIGHT:
                    this.titleText.setTextColor(Color.RED);
                    this.text2.setText("Completed " + EveFormat.DateTime.MEDIUM(job.getEndDate()));
                    this.text3.setText(job.getStatus().getText());
                    break;
                case DELIVERED:
                    this.titleText.setTextColor(Color.GRAY);
                    this.text2.setText("Completed " + EveFormat.DateTime.MEDIUM(job.getEndDate()));
                    this.text3.setText("Delivered at " + job.getOutputLocationName());
                    break;
                case WAITING:
                    this.titleText.setTextColor(Color.GREEN);
                    this.text2.setText("Completed " + EveFormat.DateTime.MEDIUM(job.getEndDate()));
                    this.text3.setText("Waiting for delivery at " + job.getOutputLocationName());
                    break;
                case QUEUED:
                    this.titleText.setTextColor(Color.WHITE);
                    this.text2.setText("Starting on " + EveFormat.DateTime.MEDIUM(job.getStartDate()));
                    this.text3.setText("Queued");
                    break;
                case RUNNING:
                    this.titleText.setTextColor(Color.YELLOW);
                    this.text2.setText("Started " + EveFormat.DateTime.MEDIUM(job.getStartDate()));
                    this.text3.setText("Ends " + EveFormat.DateTime.MEDIUM(job.getEndDate()));
                    break;
                case NONE:
                default:
                    this.titleText.setTextColor(Color.GRAY);
                    this.text2.setText("Unknown");
                    this.text3.setText("");
                    break;
            }
        }
    }

    public IndustryJobListView(Context context) {
        super(context);
    }

    public IndustryJobListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IndustryJobListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected ListRecyclerViewAdapter.ViewHolder<IndustryJob> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new IndustryJobHolder(LayoutInflater.from(getContext()).inflate(R.layout.row_industry_job, parent, false));
    }

    @Override
    protected long getItemId(IndustryJob item) {
        return item.getJobID();
    }
}
