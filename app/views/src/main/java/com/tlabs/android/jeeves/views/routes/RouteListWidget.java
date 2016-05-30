package com.tlabs.android.jeeves.views.routes;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tlabs.android.jeeves.views.R;
import com.tlabs.android.jeeves.views.ui.list.ItemTouchCallbackAdapter;
import com.tlabs.android.jeeves.views.ui.list.ListRecyclerViewAdapter;

import org.devfleet.dotlan.DotlanOptions;

import java.util.List;


public class RouteListWidget extends FrameLayout {

    public interface Listener {

        void onRouteSelected(final DotlanOptions options);

        void onRouteChanged(final List<DotlanOptions> routes);
    }

    static class RouteHolder extends ListRecyclerViewAdapter.ViewHolder<DotlanOptions> {
        TextView fromView;
        TextView toView;


        public RouteHolder(View itemView) {
            super(itemView);
            this.fromView = (TextView)itemView.findViewById(R.id.j_rowRecentJumpFrom);
            this.toView = (TextView)itemView.findViewById(R.id.j_rowRecentJumpTo);
        }

        @Override
        public void render(DotlanOptions options) {
            fromView.setText(options.getFrom());
            toView.setText(options.getTo());
        }
    }

    static class RouteListAdapter extends ListRecyclerViewAdapter<DotlanOptions> {

        @Override
        public ViewHolder<DotlanOptions> onCreateViewHolder(ViewGroup parent, int viewType) {
            final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.jeeves_row_recent_route, parent, false);
            final RouteHolder h = new RouteHolder(view);
            view.setOnClickListener(v -> onRouteSelected(getItemAt(h.getAdapterPosition())));
            return h;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        protected void onRouteSelected(final DotlanOptions options) {}
    }

    private RouteListAdapter adapter;
    private Listener listener;

    public RouteListWidget(Context context) {
        super(context);
        init();
    }

    public RouteListWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RouteListWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setRoutes(final List<DotlanOptions> routes) {
        this.adapter.setItems(routes);
    }

    public final List<DotlanOptions> getRoutes() {
        return this.adapter.getItems();
    }

    private void init() {
        this.adapter = new RouteListAdapter() {
            @Override
            protected void onRouteSelected(DotlanOptions options) {
                if (null != listener) {
                    listener.onRouteSelected(options);
                }
            }
        };

        final RecyclerView view = new RecyclerView(getContext());
        view.setLayoutManager(new LinearLayoutManager(getContext()));
        view.setAdapter(this.adapter);

        final ItemTouchHelper touch = new ItemTouchHelper(new ItemTouchCallbackAdapter() {

            @Override
            public boolean canRemove(int position) {
                return true;
            }

            @Override
            public void doRemove(int position) {
                adapter.removeItem(position);
                if (null != listener) {
                    listener.onRouteChanged(adapter.getItems());
                }
            }

            @Override
            public boolean canMove(int from, int to) {
                return true;
            }

            @Override
            public void doMove(int from, int to) {
                adapter.swapItems(from, to);
                if (null != listener) {
                    listener.onRouteChanged(adapter.getItems());
                }
            }
        });

        touch.attachToRecyclerView(view);
        addView(view);
    }
}
