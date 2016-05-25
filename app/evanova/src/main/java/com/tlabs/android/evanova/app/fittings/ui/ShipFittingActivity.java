package com.tlabs.android.evanova.app.fittings.ui;

import android.content.Intent;
import android.view.MenuItem;

import com.tlabs.android.evanova.R;
import com.tlabs.android.evanova.mvp.BaseActivity;

public class ShipFittingActivity extends BaseActivity{

    public static final String EXTRA_ID = ShipFittingActivity.class.getName() + ".fittingID";//long

    private static final int RESULT_ADD_MODULE = 5;

   /* @Inject
    FittingFacade fittingFacade;

    @Inject
    UserPreferences userPreferences;*/

   /* @Override
    protected FittingPager newFragment(Bundle savedInstanceState) {
        return new FittingPager();
    }

    @Override
    protected void inject(ApplicationComponent appComponent) {
     //   //appComponent.inject(this);
    }*/

  /*  @Override
    protected void onCreate(FittingPager fragment, Bundle savedInstanceState) {
        super.onCreate(fragment, savedInstanceState);
        final long fittingID = getIntent().getLongExtra(EXTRA_ID, -1L);

        if (fittingID == -1) {
            return;
        }
    //    setFitting(fittingFacade.load(fittingID));
    }
*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (super.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.menu_fitting_add:
                final Intent intent = new Intent(this, ModuleSearchActivity.class);
                startActivityForResult(intent, RESULT_ADD_MODULE);
                return true;
            default:
                return false;
        }
    }

   /* private void setFitting(final Fitting fitting) {
        if (null == fitting) {
            return;
        }
        setRefreshProgressVisible(true);
        final Observable<Fitter> observable =
                Observable.defer(() -> Observable.just(fittingFacade.fit(fitting)))
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread());

        observable.subscribe((fitter) -> {
            getFragment().setFitter(fitter);
            userPreferences.setBackgroundItem(this, fitting.getTypeID());
            setRefreshProgressVisible(false);
            Activities.invalidateTitle(this, true);
        });
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null == data) {
            return;
        }
        switch (requestCode) {
            case RESULT_ADD_MODULE: {
             //   getFragment().addModule(data.getLongExtra(ModuleSearchActivity.EXTRA_ITEM, -1));
            }
            break;
        }
    }

}
