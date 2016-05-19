package com.tlabs.android.jeeves.views.contracts;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tlabs.android.jeeves.views.R;
import com.tlabs.android.jeeves.views.ui.list.AbstractListRecyclerView;
import com.tlabs.android.jeeves.views.ui.list.ListRecyclerViewAdapter;
import com.tlabs.eve.api.Contract;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

public class ContractListWidget extends AbstractListRecyclerView<Contract> {

    public static final int VIEW_ALL = 0;
    public static final int VIEW_CURRENT = 2;
    public static final int VIEW_OUTSTANDING = 1;

    private int filterView = VIEW_ALL;
    private int filterOrder = 1;

    public ContractListWidget(Context context) {
        super(context);
    }

    public ContractListWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ContractListWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected ListRecyclerViewAdapter.ViewHolder<Contract> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContractListHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.jeeves_row_contract, parent, false));
    }

    @Override
    protected long getItemId(Contract item) {
        return item.getContractID();
    }

    @Override
    public void setItems(List<Contract> items) {
        subscribe(() -> sortContracts(items), contracts -> super.setItems(contracts));
    }

    public void setViewType(final int viewType) {
        this.filterView = viewType;
    }

    private List<Contract> sortContracts(List<Contract> contracts) {
        final List<Contract> sorted = new ArrayList<>(contracts.size());
        sorted.addAll(contracts);

        switch (this.filterView) {
            case VIEW_ALL:
                Collections.sort(sorted, (c1, c2) -> filterOrder * Long.compare(c1.getDateIssued(), c2.getDateIssued()));
                break;
            case VIEW_CURRENT:

                Collections.sort(sorted, (c1, c2) -> {
                    final long dueDate1 = c1.getDateAccepted() + c1.getNumDays() * 24L * 3600L * 1000L;
                    final long dueDate2 = c2.getDateAccepted() + c2.getNumDays() * 24L * 3600L * 1000L;
                    return filterOrder * Long.compare(dueDate1, dueDate2);
                });
                break;
            case VIEW_OUTSTANDING:
                Collections.sort(sorted, (c1, c2) -> filterOrder * Long.compare(c1.getDateExpired(), c2.getDateExpired()));
                break;
        }
        return sorted;
    }

    private static <T> Observable<T> defer(Func0<T> f) {
        final Observable<T> observable =
                Observable.defer(() -> Observable.just(f.call()));
        return observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private static <T> Subscription subscribe(Func0<T> f, Action1<T> action1) {
        final Observable<T> observable = defer(f);
        return observable.subscribe(action1::call);
    }
}
