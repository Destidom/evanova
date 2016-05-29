package com.tlabs.android.jeeves.views.server;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tlabs.android.jeeves.views.EveFormat;
import com.tlabs.android.jeeves.views.Strings;
import com.tlabs.android.jeeves.views.ui.list.AbstractListRowHolder;
import com.tlabs.android.jeeves.views.ui.list.ListRecyclerViewAdapter;
import com.tlabs.eve.ccp.EveRSSEntry;

import org.devfleet.crest.model.CrestServerStatus;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class ServerStatusWidget extends FrameLayout {

    static final class RSSHolder extends AbstractListRowHolder<EveRSSEntry> {

        TextView dateView;

        TextView titleView;

        private EveRSSEntry entry;

        public RSSHolder(final View itemView) {
            super(itemView);
            this.dateView = (TextView)itemView.findViewById(R.id.j_rowNewsDate);
            this.titleView = (TextView)itemView.findViewById(R.id.j_rowNewsTitle);

            itemView.setOnClickListener(v -> {
                if (null == entry) {
                    return;
                }

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(entry.getLink()));
                itemView.getContext().startActivity(intent);
            });
        }

        @Override
        public void render(final EveRSSEntry entry) {
            this.entry = entry;
            this.titleView.setText(entry.getTitle());
            this.dateView.setText(EveFormat.DateTime.MEDIUM(entry.getDateUpdated()));
        }
    }

    private TextView serverStatusView;

    private TextView serverStatusTime;

    private TextView serverCountView;

    private TextView serverUpdateView;

    private ListRecyclerViewAdapter<EveRSSEntry> rssAdapter;

    private Subscription subscription;

    public ServerStatusWidget(Context context) {
        super(context);
        init();
    }

    public ServerStatusWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ServerStatusWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setServerNews(final List<EveRSSEntry> rssNews) {
        this.rssAdapter.setItems(rssNews);
    }

    public void setServerStatus(CrestServerStatus status) {
        if (null == status) {
            setServerStatus(2, 0);
        }
        else {
            setServerStatus(status.getEveOnline()? 0 : 1, status.getEveCount());
        }
    }

    public void setServerStatus(final int status, final int playerCount) {
        if (null == serverStatusView) {
            return;
        }
        switch (status) {
            case 0:
                Strings.r(serverStatusView, R.string.jeeves_server_status_online);
                serverStatusView.setTextColor(Color.GREEN);
                Strings.r(serverCountView, R.string.jeeves_server_status_players, playerCount);
                break;
            case 1:
                Strings.r(serverStatusView, R.string.jeeves_server_status_offline);
                serverStatusView.setTextColor(Color.RED);
                serverCountView.setText("");
                break;
            default:
                Strings.r(serverStatusView, R.string.jeeves_server_status_unknown);
                serverStatusView.setTextColor(Color.YELLOW);
                serverCountView.setText("");
                break;
        }
        updateClocks();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (null == subscription) {
            subscription = Observable.
                    interval(1L, TimeUnit.SECONDS).
                    observeOn(AndroidSchedulers.mainThread()).
                    subscribe(t -> updateClocks());
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        if (null != subscription) {
            subscription.unsubscribe();
            subscription = null;
        }
        super.onDetachedFromWindow();
    }

    private void init() {
        final View view = inflate(getContext(), R.layout.jeeves_view_server_status, this);

        this.serverStatusView = (TextView)view.findViewById(R.id.j_serverStatusText);
        this.serverStatusTime = (TextView)view.findViewById(R.id.j_serverStatusTime);
        this.serverCountView = (TextView)view.findViewById(R.id.j_serverStatusCount);
        this.serverUpdateView = (TextView)view.findViewById(R.id.j_serverStatusUpdate);

        this.rssAdapter = new ListRecyclerViewAdapter<EveRSSEntry>() {
            @Override
            public ViewHolder<EveRSSEntry> onCreateViewHolder(ViewGroup parent, int viewType) {
                return new RSSHolder(LayoutInflater.from(getContext()).inflate(R.layout.jeeves_row_news, parent, false));
            }

            @Override
            public long getItemId(int position) {
                return position;
            }
        };

        RecyclerView rssView = (RecyclerView)view.findViewById(R.id.j_serverRSSRecyclerView);
        rssView.setLayoutManager(new LinearLayoutManager(getContext()));
        rssView.setAdapter(this.rssAdapter);

        this.serverStatusView.setText(R.string.jeeves_server_status_unknown);
        this.serverStatusView.setTextColor(Color.YELLOW);
        this.serverUpdateView.setText("");
        this.serverCountView.setText("");
    }

    private void updateClocks() {
		long now = System.currentTimeMillis();
		Calendar here = Calendar.getInstance();
		long offset = here.getTimeZone().getOffset(now);
        Strings.r(serverStatusTime, R.string.jeeves_server_status_time, EveFormat.DateTime.LONG(now - offset, true));
	}
}
