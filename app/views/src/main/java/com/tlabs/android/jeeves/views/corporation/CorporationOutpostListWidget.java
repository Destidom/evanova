package com.tlabs.android.jeeves.views.corporation;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tlabs.android.jeeves.views.EveFormat;
import com.tlabs.android.jeeves.views.R;
import com.tlabs.android.jeeves.views.ui.list.AbstractListRecyclerView;
import com.tlabs.android.jeeves.views.ui.list.AbstractListRowHolder;
import com.tlabs.android.jeeves.views.ui.list.ListRecyclerViewAdapter;
import com.tlabs.eve.api.corporation.Outpost;

public class CorporationOutpostListWidget extends AbstractListRecyclerView<Outpost>  {

    private static class OutpostHolder extends AbstractListRowHolder<Outpost> {

        private final TextView stationName;
        private final TextView locationName;
        private final TextView ownerName;
        private final TextView standingOwnerName;
        private final TextView typeName;
        private final TextView reprocessingText;

        public OutpostHolder(final View view) {
            super(view);
            this.stationName = findView(R.id.rowOutpostStation);
            this.locationName = findView(R.id.rowOutpostSolarSystem);
            this.ownerName = findView(R.id.rowOutpostOwner);
            this.standingOwnerName = findView(R.id.rowOutpostStanding);
            this.typeName = findView(R.id.rowOutpostType);
            this.reprocessingText = findView(R.id.rowOutpostReprocessingText);
        }

        @Override
        public void render(final Outpost outpost) {
            this.stationName.setText(outpost.getStationName());
            this.locationName.setText(outpost.getSolarSystemName());
            this.ownerName.setText(outpost.getOwnerName());
            this.standingOwnerName.setText(outpost.getStandingOwnerName());
            this.typeName.setText(outpost.getStationTypeName());
            //I18N
            final float efficiency = outpost.getReprocessingEfficiency() * 100f;
            final float takes = outpost.getReprocessingStationTake() * 100f;
            this.reprocessingText.setText(
                    "Efficiency " + EveFormat.Number.FLOAT(efficiency) + "% - Takes " + EveFormat.Number.FLOAT(takes) + "%");
        }
    }

    public CorporationOutpostListWidget(Context context) {
        super(context);
    }

    public CorporationOutpostListWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CorporationOutpostListWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected ListRecyclerViewAdapter.ViewHolder<Outpost> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new OutpostHolder(LayoutInflater.from(getContext()).inflate(R.layout.row_corp_outpost, parent, false));
    }

    @Override
    protected long getItemId(Outpost item) {
        return item.getStationID();
    }
}
