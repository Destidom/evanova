package com.tlabs.android.evanova.app.fittings.ui;



import com.tlabs.android.evanova.mvp.BaseActivity;

public class ModuleSearchActivity extends BaseActivity {
    public static final String EXTRA_ITEM = ModuleSearchActivity.class.getName() + ".itemID";
/*
    @Override
    protected FittingModulePager newFragment(Bundle savedInstanceState) {
        final FittingModulePager f = new FittingModulePager();
        f.setListener(new FittingModulePager.Listener() {
            @Override
            public void onMarketGroupSelected(long groupID) {

            }

            @Override
            public void onMarketItemSelected(long itemID) {
                onSearchResult(Long.toString(itemID));
            }
        });
        return f;
    }

    @Override
    protected final void onSearchResult(final String data) {
        final Intent intent = new Intent();
        FittingPreferences preferences = new FittingPreferences(this);
        try {
            final Long itemId = Long.parseLong(data);
            intent.putExtra(EXTRA_ITEM, Long.parseLong(data));
            setResult(RESULT_OK, intent);
            preferences.addRecentItem(itemId);
        }
        catch (NumberFormatException e) {
            setResult(RESULT_CANCELED, intent);
        }
        finish();
    }
*/
}
