package com.tlabs.android.jeeves.views.fittings;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.tlabs.android.jeeves.views.R;
import com.tlabs.eve.dogma.Fitter;

public class FittingModulesWidget extends FrameLayout implements FittingWidget {

    private RecyclerView listView;
    private View statsView;

    public FittingModulesWidget(Context context) {
        super(context);
        init();
    }

    public FittingModulesWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FittingModulesWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public void setFitting(Fitter fitted) {
        this.listView.setAdapter(new FittingModulesAdapter(fitted));
        FittingFormat.format(fitted, this.statsView);
    }

    //@Override
    public void setLoading(boolean loading) {
        //FIXME
    }

    private void init() {
        final View view = inflate(getContext(), R.layout.jeeves_view_fitting_modules, null);
        addView(view);

        this.statsView = view.findViewById(R.id.j_shipModulesStatistics);

        this.listView = (RecyclerView)view.findViewById(R.id.j_shipModulesRecycler);
        this.listView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

}
