package com.tlabs.android.evanova.app.fittings.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.ShareActionProvider;
import android.util.AttributeSet;
import android.view.View;

import com.tlabs.android.evanova.R;
import com.tlabs.android.jeeves.model.data.fitting.FittingFacade;
import com.tlabs.android.jeeves.views.fittings.FittingAttributeListWidget;
import com.tlabs.android.jeeves.views.fittings.FittingEffectListWidget;
import com.tlabs.android.jeeves.views.fittings.FittingModulesWidget;
import com.tlabs.android.jeeves.views.fittings.FittingStatisticsWidget;
import com.tlabs.android.jeeves.views.fittings.FittingWidget;
import com.tlabs.android.jeeves.views.ui.pager.ViewPager;
import com.tlabs.android.jeeves.views.ui.pager.ViewPagerAdapter;
import com.tlabs.eve.dogma.Fitter;
import com.tlabs.eve.dogma.Fitting;
import com.tlabs.eve.dogma.extra.format.FittingFormat;

import javax.inject.Inject;

public class FittingPager extends ViewPager {

    public static class FittingPagerAdapter extends ViewPagerAdapter {

        public FittingPagerAdapter(Context context) {
            super(context);

            addView(new FittingModulesWidget(context), R.string.fit_modules);
            addView(new FittingStatisticsWidget(context), R.string.fit_statistics);

            addView(new FittingAttributeListWidget(context), R.string.fit_attributes);
            addView(new FittingEffectListWidget(context), R.string.fit_effects);
        }

        public void fit(final Fitter ship) {
            for (View v: getViews()) {
                if (v instanceof FittingWidget) {
                    ((FittingWidget)v).setFitting(ship);
                }
            }
        }
/*
        public List<Fitter.Module> removeSelectedModules() {
            final FittingModulesFragment fModules = getFragment(0);
            final List<Fitter.Module> removed = fModules.removeSelectedModules();
            if (!removed.isEmpty()) {
                final FittingStatisticsFragment fStats = getFragment(1);
                fStats.setFitter(fStats.getFitted());
            }
            return removed;
        }

        public Fitter.Module addModule(final long item) {
            final FittingModulesFragment fModules = getFragment(0);
            final Fitter.Module added = fModules.addModule(item);
            if (null == added) {
                return null;
            }
            final FittingStatisticsFragment fStats = getFragment(1);
            fStats.setFitter(fStats.getFitted());
            return added;
        }

        public boolean onBackPressed(final int fragment) {
            final FittingFragment f = (FittingFragment)getFragments().get(fragment);
            return f.onBackPressed();
        }*/
    }

    @Inject
    FittingFacade fittingFacade;

    private Fitter fitter = null;

    private ShareActionProvider shareProvider;

    public FittingPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FittingPager(Context context) {
        super(context);
    }
    /*
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.shareProvider = new ShareActionProvider(getContext());
    }

    @Override
    protected FittingPagerAdapter createAdapter(Bundle savedInstanceState) {
        final FittingPagerAdapter adapter = new FittingPagerAdapter(getContext());
        return adapter;
    }

    @Override
    protected final int getPagerID() {
        return R.id.pagerFitting;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fittings, menu);
        Activities.enableMenuItem(R.id.menu_fitting_import, menu, false, false);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        final boolean enabled = (null != this.fitter);
        Activities.enableMenuItem(R.id.menu_fitting_add, menu, false, false);
        Activities.enableMenuItem(R.id.menu_fitting_delete, menu, false, false);
        Activities.enableMenuItem(R.id.menu_fitting_rename, menu, enabled, true);
        Activities.enableMenuItem(R.id.menu_fitting_share, menu, enabled, true);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (null == this.fitter) {
            return false;
        }

        switch (item.getItemId()) {
            case R.id.menu_fitting_rename:
                onFittingRename();
                return true;
            case R.id.menu_fitting_share:
                onFittingShare();
                return true;
            case R.id.menu_fitting_delete:
              //  onDeleteSelected();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/
/*
    @Override
    public void setTitle(ActivityTitle title) {
        if (null == this.fitter) {
            return;
        }
        title.setText(this.fitter.getName());
        title.setDescription(this.fitter.getTypeName());
        title.setImage(EveImages.getItemIconURL(getContext(), this.fitter.getTypeID()));
    }*/

    /*public boolean onBackPressed() {
        return getPagerAdapter().onBackPressed(getDisplayedChild());
    }
*/
    /*
    private int onDeleteSelected() {
        final int removed = getPagerAdapter().removeSelectedModules().size();
        if (removed == 0) {
            return 0;
        }
        onFittingSave();
        //I18N
        snackbar("Removed " + removed + " module(s)");
        return removed;
    }

    public Item addModule(final long item) {
        final Fitter.Module added = getPagerAdapter().addModule(item);
        if (null == added) {
            snackbar("Could not fit this item");
            return null;
        }
        onFittingSave();
        snackbar("Added " + added.getItem().getItemName());
        return added.getItem();
    }*/
/*
    public void setFitter(final Fitter fitter) {
        this.fitter = fitter;
        getPagerAdapter().fit(fitter);
        Activities.invalidateTitle(this, true);
    }

    private void onFittingRename() {
        Dialogs.showRenameFitting(
                getActivity(),
                fitter,
                (dialog, action) -> {
                    onFittingSave();
                });
    }

    private void onFittingSave() {
        final Fitting saved = this.fitter.toFitting();
        if (fittingFacade.save(saved)) {
            Activities.invalidateTitle(this, false);
        }
        else {
        }
    }
*/
    private void onFittingShare() {
        final Fitting fitting = fitter.toFitting();

        final StringBuilder body = new StringBuilder();
        body.append(FittingFormat.toDNA(fitting));
        body.append("\n----------------------------\n");
        body.append(FittingFormat.toXML(fitting));

        final Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, body.toString());
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, fitting.getName());

        shareProvider.setShareIntent(shareIntent);
    }
}
