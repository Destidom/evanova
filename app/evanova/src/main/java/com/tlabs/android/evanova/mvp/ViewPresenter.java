package com.tlabs.android.evanova.mvp;

import android.content.Intent;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

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

    public void pauseView() {}

    public void resumeView() {}

    public void startView(final Intent intent) {}

    protected static <T> Observable<T> defer(Func0<T> f) {
        final Observable<T> observable =
                Observable.defer(() -> Observable.fromCallable(f));
        return observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    protected static <T> Subscription subscribe(Func0<T> f) {
        final Observable<T> observable = defer(f);
        return observable.subscribe();
    }

    protected static <T> Subscription subscribe(Func0<T> f, Action1<T> action1) {
        return defer(f).subscribe(action1::call);
    }
}
