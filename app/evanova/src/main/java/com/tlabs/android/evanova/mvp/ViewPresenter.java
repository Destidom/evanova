package com.tlabs.android.evanova.mvp;

import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func0;

public class ViewPresenter<T> {

    private T view;

    public void setView(final T view) {
        this.view = view;
    }

    public void destroyView() {
        this.view = null;
    }

    public final T getView() {
        return this.view;
    }

    protected static <T> Subscription subscribe(Func0<T> f) {
        return RX.subscribe(f);
    }

    protected static <T> Subscription subscribe(Func0<T> f, Action1<T> action1) {
        return RX.subscribe(f, action1);
    }
}
