package com.tlabs.android.jeeves.views.routes;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tlabs.android.jeeves.views.ui.AutoCompleteAdapter;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;
import org.devfleet.dotlan.DotlanJumpOptions;
import org.devfleet.dotlan.DotlanOptions;

import java.util.Collections;
import java.util.List;

public class RouteInputWidget extends FrameLayout {


    public interface RouteAutoComplete {

        List<String> searchLocations(final String search, final float maxSecurityStatus);

        List<String> searchJumpShips(final String search);

    }

    public interface Listener {
        void onRouteSelected(final DotlanOptions options);
    }

    private AutoCompleteTextView fromAutoComplete;

    private AutoCompleteTextView toAutoComplete;

    private AutoCompleteTextView freighterAutoComplete;

    private CheckBox jumpCheck;

    private View jumpContainerView;

    private CheckBox incursionCheck;

    private CheckBox solarSystemsCheck;

    private TextView freighterSkillView;

    private DiscreteSeekBar freighterSkillSeek;

    private TextView calibrationSkillView;

    private DiscreteSeekBar calibrationSkillSeek;

    private TextView conservationSkillView;

    private DiscreteSeekBar conservationSkillSeek;

    private ImageButton submitButton;

    private RouteInputWidget.Listener listener;
    private RouteAutoComplete autoComplete;

    public RouteInputWidget(Context context) {
        super(context);
        init();
    }

    public RouteInputWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setRoute(final DotlanOptions options) {
        this.fromAutoComplete.setText(options.getFrom());
        this.toAutoComplete.setText(options.getTo());

        if (options instanceof DotlanJumpOptions) {
            this.jumpCheck.setChecked(true);
            this.jumpContainerView.setVisibility(VISIBLE);
            setJumpRoute((DotlanJumpOptions) options);
        } else {
            this.jumpCheck.setChecked(false);
            this.jumpContainerView.setVisibility(GONE);
        }
    }

    private void setJumpRoute(final DotlanJumpOptions options) {
        this.freighterAutoComplete.setText(options.getJumpShip());
        this.incursionCheck.setChecked(options.getAvoidIncursions());
        this.solarSystemsCheck.setChecked(options.getPreferSolarSystems());

        this.freighterSkillSeek.setProgress(options.getJumpFreighterSkill());
        this.calibrationSkillSeek.setProgress(options.getJumpDriveCalibrationSkill());
        this.conservationSkillSeek.setProgress(options.getJumpFuelConservationSkill());
    }

    private DotlanOptions createOptions() {
        if (!this.jumpCheck.isChecked()) {
            return new DotlanOptions()
                    .addWaypoint(this.fromAutoComplete.getText().toString())
                    .addWaypoint(this.toAutoComplete.getText().toString());
        }

        return new DotlanJumpOptions()
                .setJumpShip(this.freighterAutoComplete.getText().toString())
                .setJumpFreighterSkill(this.freighterSkillSeek.getProgress())
                .setJumpDriveCalibrationSkill(this.calibrationSkillSeek.getProgress())
                .setJumpFuelConservationSkill(this.conservationSkillSeek.getProgress())
                .setAvoidIncursions(this.incursionCheck.isChecked())
                .setPreferSolarSystems(this.solarSystemsCheck.isChecked())
                .addWaypoint(this.fromAutoComplete.getText().toString())
                .addWaypoint(this.toAutoComplete.getText().toString());
    }

    public void setAutoComplete(RouteAutoComplete autoComplete) {
        this.autoComplete = autoComplete;
    }

    private void init() {
        final View view = inflate(getContext(), R.layout.jeeves_view_route_select, this);
        bind(view);

        this.autoComplete = new RouteAutoComplete() {
            @Override
            public List<String> searchLocations(String search, float maxSecurityStatus) {
                return Collections.emptyList();
            }

            @Override
            public List<String> searchJumpShips(String search) {
                return Collections.emptyList();
            }
        };

        setupAutoComplete(
                this.fromAutoComplete,
                (constraint -> autoComplete.searchLocations(constraint, this.jumpCheck.isChecked() ? 0.5f : 1.0f)));

        setupAutoComplete(
                this.toAutoComplete,
                (constraint -> autoComplete.searchLocations(
                        constraint,
                        this.jumpCheck.isChecked() ? 0.5f : 1.0f)));

        setupAutoComplete(
                this.freighterAutoComplete,
                (constraint -> autoComplete.searchJumpShips(constraint)));
    }

    private void setupAutoComplete(final AutoCompleteTextView view, AutoCompleteAdapter.AutoComplete<String> autoComplete) {
        view.setAdapter(new AutoCompleteAdapter(autoComplete));
        view.setThreshold(3);
        view.setOnItemClickListener(
                (ac, v, position, id) -> view.setText((String) ac.getItemAtPosition(position)));
    }

    private void bind(final View view) {
        this.submitButton = (ImageButton) view.findViewById(R.id.j_routeSubmitButton);

        this.fromAutoComplete = (AutoCompleteTextView)view.findViewById(R.id.j_routeFromAutoComplete);
        this.toAutoComplete = (AutoCompleteTextView)view.findViewById(R.id.j_routeToAutoComplete);
        this.freighterAutoComplete = (AutoCompleteTextView)view.findViewById(R.id.j_routeFreighterAutoComplete);


        this.jumpContainerView = view.findViewById(R.id.j_routeJumpContainer);
        this.jumpCheck = (CheckBox)view.findViewById(R.id.j_routeJumpCheck);
        this.incursionCheck = (CheckBox)view.findViewById(R.id.j_routeJumpIncursionCheck);
        this.solarSystemsCheck = (CheckBox)view.findViewById(R.id.j_routeJumpSystemsCheck);

        this.freighterSkillSeek = (DiscreteSeekBar)view.findViewById(R.id.j_routeJumpFreighterSkillSeek);
        this.calibrationSkillSeek = (DiscreteSeekBar)view.findViewById(R.id.j_routeJumpCalibrationSkillSeek);
        this.conservationSkillSeek = (DiscreteSeekBar)view.findViewById(R.id.j_routeJumpConservationSkillSeek);

        this.freighterSkillView = (TextView)view.findViewById(R.id.j_routeJumpFreighterSkillText);
        this.calibrationSkillView = (TextView)view.findViewById(R.id.j_routeJumpCalibrationSkillText);
        this.conservationSkillView = (TextView)view.findViewById(R.id.j_routeJumpConservationSkillText);

        this.submitButton.setOnClickListener(v -> {
            if (null == listener) {
                return;
            }
            listener.onRouteSelected(createOptions());
        });
        this.jumpCheck.setOnClickListener(v -> jumpContainerView.setVisibility(((CheckBox)v).isChecked() ? VISIBLE : GONE));
    }
}
