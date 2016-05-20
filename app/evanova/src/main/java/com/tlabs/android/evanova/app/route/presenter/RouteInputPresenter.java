package com.tlabs.android.evanova.app.route.presenter;

import android.content.Context;
import android.content.Intent;

import com.tlabs.android.evanova.R;
import com.tlabs.android.evanova.app.EvanovaActivityPresenter;
import com.tlabs.android.evanova.app.route.RouteInputView;
import com.tlabs.android.evanova.app.route.RouteUseCase;
import com.tlabs.android.evanova.app.route.ui.RouteDisplayActivity;
import com.tlabs.android.evanova.mvp.ActivityPresenter;

import org.devfleet.dotlan.DotlanJumpOptions;
import org.devfleet.dotlan.DotlanOptions;

import java.util.List;

import javax.inject.Inject;

public class RouteInputPresenter extends EvanovaActivityPresenter<RouteInputView> {

    private final RouteUseCase useCase;

    @Inject
    public RouteInputPresenter(Context context, RouteUseCase useCase) {
        super(context);
        this.useCase = useCase;
    }

    @Override
    public void setView(RouteInputView view) {
        super.setView(view);
        setBackground(userPreferences().getBackgroundDefault());

        view.setTitle(R.string.activity_routes_title);
        view.setTitleDescription(R.string.activity_routes_description);
        loadOptions();
    }

    public void loadOptions() {
        subscribe(() -> this.useCase.loadOptions(), options -> getView().displayOptions(options));
    }

    public void onRouteSelected(final DotlanOptions options) {
        startActivity(newRouteDisplayIntent(getContext(), options));
    }

    private static Intent newRouteDisplayIntent(final Context context, final DotlanOptions options) {
        final Intent intent = new Intent(context, RouteDisplayActivity.class);

        final List<String> waypoints = options.getWaypoints();

        intent.putExtra(RouteDisplayActivity.EXTRA_WAYPOINTS, waypoints.toArray(new String[waypoints.size()]));

        if (options instanceof DotlanJumpOptions) {
            final DotlanJumpOptions jumpOptions = (DotlanJumpOptions)options;
            final StringBuilder b = new StringBuilder()
                    .append(jumpOptions.getJumpShip())
                    .append(":")
                    .append(jumpOptions.getAvoidIncursions())
                    .append(":")
                    .append(jumpOptions.getPreferSolarSystems())
                    .append(":")
                    .append(jumpOptions.getJumpDriveCalibrationSkill())
                    .append(":")
                    .append(jumpOptions.getJumpFuelConservationSkill())
                    .append(":")
                    .append(jumpOptions.getJumpFreighterSkill());
            intent.putExtra(RouteDisplayActivity.EXTRA_JUMP, b.toString());
        }
        return intent;
    }

}
