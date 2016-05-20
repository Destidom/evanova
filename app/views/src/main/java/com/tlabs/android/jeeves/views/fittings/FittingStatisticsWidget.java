package com.tlabs.android.jeeves.views.fittings;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.tlabs.android.jeeves.views.R;
import com.tlabs.eve.dogma.Fitter;

public class FittingStatisticsWidget extends FrameLayout implements FittingWidget {

    private View statsView;

    public FittingStatisticsWidget(Context context) {
        super(context);
        init();
    }

    public FittingStatisticsWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FittingStatisticsWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public void setFitting(Fitter fitted) {
        FittingFormat.format(fitted, this.statsView);
    }

    private void init() {
        final View view = inflate(getContext(), R.layout.jeeves_view_fitting_stats, null);
        addView(view);
        this.statsView = view;
    }

}
