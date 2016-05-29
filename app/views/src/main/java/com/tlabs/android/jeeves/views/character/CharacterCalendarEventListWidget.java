package com.tlabs.android.jeeves.views.character;

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
import com.tlabs.eve.api.character.CharacterCalendar;

import org.apache.commons.lang3.StringUtils;

public class CharacterCalendarEventListWidget extends AbstractListRecyclerView<CharacterCalendar.Entry> {

    public static class EventHolder extends AbstractListRowHolder<CharacterCalendar.Entry> {
        private final TextView titleView;
        private final TextView ownerView;
        private final TextView timeView;
        private final TextView remainingView;
        private final ImageView iconView;

        public EventHolder(View itemView) {
            super(itemView);
            this.titleView = findView(R.id.rowCalendarTitle);
            this.ownerView = findView(R.id.rowCalendarOwner);
            this.timeView = findView(R.id.rowCalendarTime);
            this.remainingView = findView(R.id.rowCalendarRemaining);
            this.iconView = findView(R.id.rowCalendarIcon);
        }

        @Override
        public void render(final CharacterCalendar.Entry entry) {
            this.ownerView.setText(entry.getOwnerName());

            if (StringUtils.isBlank(entry.getEventTitle())) {
                titleView.setText("");
            }
            else {
                titleView.setText(entry.getEventTitle());
            }

            if (entry.getDuration() > 1) {
                timeView.setText(EveFormat.DateTime.PLAIN(entry.getEventDate()));
            }
            else {
                timeView.setText(EveFormat.DateTime.PLAIN(entry.getEventDate()));
            }

            long remaining = entry.getEventDate() - System.currentTimeMillis();
            if (remaining > 0) {
                remainingView.setText(EveFormat.Duration.MEDIUM(remaining) + " from now");
                remainingView.setTextColor(EveFormat.getDurationColor(remaining));
            }
            else {
                remainingView.setText("");
            }

            switch (entry.getOwnerType()) {
                case ALLIANCE:
                    EveImages.loadAllianceIcon(entry.getOwnerID(), iconView);
                    break;
                case CHARACTER:
                    EveImages.loadCharacterIcon(entry.getOwnerID(), iconView);
                    break;
                case CORPORATION:
                    EveImages.loadCorporationIcon(entry.getOwnerID(), iconView);
                    break;
                case CCP:
                    EveImages.loadCharacterIcon(entry.getOwnerID(), iconView);
                    break;
                default:
                    break;
            }
        }
    }

    public CharacterCalendarEventListWidget(Context context) {
        super(context);
    }

    public CharacterCalendarEventListWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CharacterCalendarEventListWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected ListRecyclerViewAdapter.ViewHolder<CharacterCalendar.Entry> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EventHolder(LayoutInflater.from(getContext()).inflate(R.layout.row_calendar, parent, false));
    }

    @Override
    protected long getItemId(CharacterCalendar.Entry item) {
        return item.getEventID();
    }
}
