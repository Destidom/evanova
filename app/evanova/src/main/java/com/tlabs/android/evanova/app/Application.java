package com.tlabs.android.evanova.app;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.os.StrictMode.VmPolicy.Builder;
import android.support.multidex.MultiDex;
import android.widget.ImageView;

import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.squareup.picasso.Picasso;
import com.tlabs.android.evanova.content.ContentModule;
import com.tlabs.android.evanova.preferences.PreferencesModule;
import com.tlabs.android.jeeves.JeevesModule;
import com.tlabs.android.jeeves.model.EveAccount;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rx.Observable;
import rx.schedulers.Schedulers;

public class Application extends android.app.Application {
    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    private static ApplicationComponent appComponent;
    private static UserComponent userComponent;

	@Override
	public void onCreate() {
        super.onCreate();

//        LeakCanary.install(this);

        StrictMode.setThreadPolicy(new ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build());

        StrictMode.setVmPolicy(new Builder()
                .detectAll()
                .penaltyLog()
                .build());

        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            private final Picasso p = Picasso.with(getApplicationContext());
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                p.load(uri).placeholder(placeholder).into(imageView);
            }

            @Override
            public void cancel(ImageView imageView) {
                p.cancelRequest(imageView);
            }
        });

        appComponent = DaggerApplicationComponent
                .builder()
                .preferencesModule(new PreferencesModule(getApplicationContext()))
                .contentModule(new ContentModule())
                .jeevesModule(new JeevesModule(getApplicationContext()))
                .preferencesModule(new PreferencesModule(getApplicationContext()))
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static void runWith(final Context context, EveAccount user) {
        Observable.defer(() -> Observable.just(updateGraph(context.getApplicationContext(), user)))
        .observeOn(Schedulers.io())
        .subscribeOn(Schedulers.io())
        .subscribe();
    }

    public static ApplicationComponent getAppComponent() {
        return appComponent;
    }

    public static UserComponent getEveComponent() {
        return userComponent;
    }

    private static Void updateGraph(final Context context, final EveAccount user) {
        userComponent = DaggerUserComponent
                .builder()
                .applicationComponent(appComponent)
                .userModule(new UserModule(context, user))
                .build();
        return null;
    }
}
