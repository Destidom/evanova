package com.tlabs.android.evanova.app.fitting.ui;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import com.tlabs.android.evanova.R;
import com.tlabs.android.evanova.app.fitting.presenter.FittingListPresenter;
import com.tlabs.android.evanova.mvp.ActivityView;
import com.tlabs.android.evanova.mvp.BaseActivity;
import com.tlabs.eve.dogma.Fitting;
import com.tlabs.eve.dogma.extra.format.FittingFormat;

import java.util.List;

import javax.inject.Inject;

public class ShipFittingListActivity extends BaseActivity implements ActivityView {

    private static final int RESULT_ADD = 5;
    private static final int RESULT_IMPORT = 6;

/*    @Inject
    FittingFacade fittingFacade;

    private ShareActionProvider shareProvider;*/

    @Inject
    FittingListPresenter presenter;
/*
    @Override
    protected FittingListFragment newFragment(Bundle savedInstanceState) {
        return new FittingListFragment();
    }

    @Override
    protected void onCreate(FittingListFragment fragment, Bundle savedInstanceState) {
        super.onCreate(fragment, savedInstanceState);

        fragment.setListener(new FittingListFragment.Listener() {
            @Override
            public boolean onFittingClicked(Fitting fitting) {
                final Intent intent = new Intent(ShipFittingListActivity.this, ShipFittingActivity.class);
                intent.putExtra(ShipFittingActivity.EXTRA_ID, fitting.getId());
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onFittingSelected(Fitting fitting, boolean selected) {
                supportInvalidateOptionsMenu();
                return true;
            }
        });
     //   this.shareProvider = new ShareActionProvider(this);
    }
*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.fittings, menu);
        return true;
    }
/*
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (null == getFragment()) {
            return false;
        }
       final boolean hasSelection = !getFragment().getSelected().isEmpty();
        Activities.enableMenuItem(R.id.menu_fitting_add, menu, true, true);
        Activities.enableMenuItem(R.id.menu_fitting_delete, menu, hasSelection, true);
        Activities.enableMenuItem(R.id.menu_fitting_import, menu, true, true);
        Activities.enableMenuItem(R.id.menu_fitting_rename, menu, false, false);
        return true;
    }
*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_fitting_add: {
                final Intent intent = new Intent(this, ShipSearchActivity.class);
                startActivityForResult(intent, RESULT_ADD);
                break;
            }
            case R.id.menu_fitting_import: {
                final Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.select)), RESULT_IMPORT);
                break;
            }

           /* case R.id.menu_fitting_delete: {
                final List<Long> selected = getFragment().getSelected();
                if (!selected.isEmpty()) {
                    for (Long id: selected) {
                        fittingFacade.delete(id);
                        getFragment().removeFitting(id);
                    }
                    getFragment().setSelected(Collections.emptyList());
                    supportInvalidateOptionsMenu();
                }
                break;
            }
            case R.id.menu_fitting_share:
                final Intent shareIntent = newShareIntent(getFragment().getFittings());
                shareProvider.setShareIntent(shareIntent);
                return super.onOptionsItemSelected(item);*/
            default:
                break;
        }
        return true;
    }

   /* @Override
    public void onBackPressed() {
        if (getFragment().getSelected().isEmpty()) {
            super.onBackPressed();
        }
        getFragment().setSelected(Collections.emptyList());
        supportInvalidateOptionsMenu();
    }*/
/*
    @Override
    public void setTitle(ActivityTitle title) {
        title.setText(R.string.fit_fittings);
        title.setDescription("");
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null == data) {
            return;
        }
        switch (requestCode) {
            case RESULT_ADD: {
              /*  final long selectedItem = data.getLongExtra(ShipSearchActivity.EXTRA_ITEM, -1);
                if (selectedItem == -1) {
                    break;
                }
                final Fitting added = addFitting(selectedItem);
                if (null == added) {
                    break;
                }
*/
               // snackbar(added.getTypeName() + " added", Snackbar.LENGTH_SHORT);
                supportInvalidateOptionsMenu();
                break;
            }
            case RESULT_IMPORT: {
                if (null == data.getData()) {
                    return;
                }
             /*   addFittings(data.getData());
                getFragment().setSelected(Collections.emptyList());
                supportInvalidateOptionsMenu();*/
                break;
            }
            default: {
                break;
            }
        }
    }

    /*private void addFittings(final Uri uri) {
        final List<Fitting> loaded = fittingFacade.load(uri);
        if (loaded.isEmpty()) {
            return;
        }

        for (Fitting f: loaded) {
            fittingFacade.save(f);
            getFragment().addFitting(f);
        }
    }*/

  /*  private Fitting addFitting(final long itemId) {
        final Fitting fitting = fittingFacade.build(itemId);
        if (null == fitting) {
            return null;
        }

        fittingFacade.save(fitting);
        getFragment().addFitting(fitting);
        return fitting;
    }*/

    private Intent newShareIntent(final List<Fitting> fittings) {
        final Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");

        final StringBuilder text = new StringBuilder();
        text.append(FittingFormat.toDNA(fittings));
        text.append("\n\n");
        text.append(FittingFormat.toXML(fittings));

        shareIntent.putExtra(Intent.EXTRA_TEXT, text.toString());
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Evanova ship fittings");

        return shareIntent;
    }

}
