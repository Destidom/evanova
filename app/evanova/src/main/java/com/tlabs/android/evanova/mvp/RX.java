package com.tlabs.android.evanova.mvp;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

public class RX {

    private RX() {}

    public static <T> Observable<T> defer(Func0<T> f) {
        final Observable<T> observable =
                Observable.defer(() -> Observable.fromCallable(f));
        return observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> Subscription subscribe(Func0<T> f) {
        final Observable<T> observable = defer(f);
        return observable.subscribe();
    }

    public static <T> Subscription subscribe(Func0<T> f, Action1<T> action1) {
        return defer(f).subscribe(action1::call);
    }
}
