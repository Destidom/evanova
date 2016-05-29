package com.tlabs.android.evanova.app.contracts.main;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ViewFlipper;

import com.tlabs.android.evanova.R;
import com.tlabs.android.evanova.app.Application;
import com.tlabs.android.evanova.app.contracts.ContractModule;
import com.tlabs.android.evanova.app.contracts.DaggerContractComponent;
import com.tlabs.android.evanova.mvp.BaseActivity;
import com.tlabs.android.evanova.mvp.Presenter;
import com.tlabs.android.jeeves.views.contracts.ContractListWidget;
import com.tlabs.android.jeeves.views.ui.list.AbstractListRecyclerView;
import com.tlabs.android.jeeves.views.ui.pager.TabPager;
import com.tlabs.android.jeeves.views.ui.pager.ViewPagerAdapter;
import com.tlabs.eve.api.Contract;

import java.util.List;

import javax.inject.Inject;

public class ContractActivity extends BaseActivity implements ContractView {

    private static final int FLIPPER_ALL = 0;
    private static final int FLIPPER_CONTRACT = 1;

    private static class ContractListPagerAdapter extends ViewPagerAdapter {
        public ContractListPagerAdapter(Context context) {
            super(context);
            addContractListView(context, ContractListWidget.VIEW_ALL, R.string.jeeves_contract_page_all);
            addContractListView(context, ContractListWidget.VIEW_OUTSTANDING, R.string.jeeves_contract_page_outstanding);
            addContractListView(context, ContractListWidget.VIEW_CURRENT, R.string.jeeves_contract_page_current);
        }

        public void setContracts(List<Contract> contracts) {
            for (View v: getViews()) {
                ((ContractListWidget)v).setItems(contracts);
            }
        }

        protected void onContractSelected(final Contract contract) {}

        private void addContractListView(final Context context, final int view, final int rTitle) {
            final ContractListWidget widget = new ContractListWidget(context);
            widget.setViewType(view);
            widget.setListener(new AbstractListRecyclerView.Listener<Contract>() {
                @Override
                public void onItemClicked(Contract contract) {
                    onContractSelected(contract);
                }

                @Override
                public void onItemSelected(Contract contract, boolean selected) {
                    if (selected) {
                        onContractSelected(contract);
                    }
                }

                @Override
                public void onItemMoved(Contract contract, int from, int to) {

                }
            });
            addView(widget, rTitle);
        }
    }

    private static class ContractPagerAdapter extends ViewPagerAdapter {
        public ContractPagerAdapter(Context context) {
            super(context);
        }

        public void setContract(Contract contract) {

        }
    }

    @Inject
    @Presenter
    ContractPresenter presenter;

    private TabPager listPager;
    private TabPager contractPager;
    private ViewFlipper flipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerContractComponent.builder()
                .applicationComponent(Application.getAppComponent())
                .contractModule(new ContractModule())
                .build()
                .inject(this);


        this.listPager = new TabPager(this);
        this.listPager.setAdapter(new ContractListPagerAdapter(this) {
            @Override
            protected void onContractSelected(Contract contract) {
                presenter.onContractSelected(contract);
            }
        });

        this.contractPager = new TabPager(this);
        this.contractPager.setAdapter(new ContractPagerAdapter(this));

        this.flipper = new ViewFlipper(this);
        this.flipper.addView(this.listPager);
        this.flipper.addView(this.contractPager);

        setView(this.flipper);
    }

    @Override
    public void onBackPressed() {
        if (this.flipper.getDisplayedChild() == FLIPPER_ALL) {
            super.onBackPressed();
            return;
        }
        this.flipper.setDisplayedChild(FLIPPER_ALL);
    }

    @Override
    public void setContracts(List<Contract> contracts) {
        final ContractListPagerAdapter adapter = this.listPager.getAdapter();
        adapter.setContracts(contracts);
    }

    @Override
    public void showContract(Contract contract) {
        final ContractPagerAdapter adapter = this.contractPager.getAdapter();
        adapter.setContract(contract);
        this.flipper.setDisplayedChild(FLIPPER_CONTRACT);

    }
}
