package com.tlabs.android.evanova.app.fittings.ui;

import com.tlabs.android.evanova.mvp.BaseActivity;

public class ShipSearchActivity extends BaseActivity {

    public static final String EXTRA_ITEM = ShipSearchActivity.class.getName() + ".itemID";
    /*
    @Override
    protected final void onDatabaseItemSelected(long itemId) {
        final Intent intent = new Intent();
        intent.putExtra(EXTRA_ITEM, itemId);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onCreate(ItemGroupFragment f, Bundle savedInstanceState) {
        super.onCreate(f, savedInstanceState);
        f.setShowAdd(true);
        f.setFilter((group -> {
            if (group.getParentGroupID() == -1) {
                return (group.getMarketGroupID() == 4L);
            }
            return true;
        }));
    }*/
}
