package com.tlabs.android.jeeves.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;
import android.widget.RemoteViews;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

public final class EveImages {
    private static final Logger LOG = LoggerFactory.getLogger(EveImages.class);

    private static final String IMAGE_CHARACTER = "%s/Character/%s_512.jpg";
    private static final String ICON_CHARACTER = "%s/Character/%s_128.jpg";

    private static final String IMAGE_CORPORATION = "%s/Corporation/%s_256.png";
    private static final String ICON_CORPORATION = "%s/Corporation/%s_128.png";

    private static final String IMAGE_ALLIANCE = "%s/Alliance/%s_256.png";
    private static final String ICON_ALLIANCE = "%s/Alliance/%s_128.png";

    private static final String IMAGE_ITEM = "%s/Render/%s_512.png";
    private static final String ICON_ITEM = "%s/Type/%s_64.png";

    /*
     * Copyright 2014 Julian Shen
     *
     * Licensed under the Apache License, Version 2.0 (the "License");
     * you may not use this file except in compliance with the License.
     * You may obtain a copy of the License at
     *
     *     http://www.apache.org/licenses/LICENSE-2.0
     *
     * Unless required by applicable law or agreed to in writing, software
     * distributed under the License is distributed on an "AS IS" BASIS,
     * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     * See the License for the specific language governing permissions and
     * limitations under the License.
     */
    /**
     * Created by julian on 13/6/21.
     */
    private static class CircleTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size/2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }

    private static final Transformation circularTransform = new CircleTransform();

    public static void loadItemIcon(final long typeID, final ImageView intoView) {
        p(intoView.getContext()).load(url(ICON_ITEM, typeID)).transform(circularTransform).into(intoView);
    }

    public static void loadItemImage(final long typeID, final ImageView intoView) {
        p(intoView.getContext()).load(url(IMAGE_ITEM, typeID)).into(intoView);
    }

    public static void loadItemImage(final long typeID, final Activity intoView) {
        load(url(IMAGE_ITEM, typeID), intoView);
    }

    public static void loadCharacterImage(final long ownerId, final ImageView intoView) {
        p(intoView.getContext()).load(url(IMAGE_CHARACTER, ownerId)).into(intoView);
    }

    public static void loadCharacterImage(final long ownerId, final Activity intoView) {
        load(url(IMAGE_CHARACTER, ownerId), intoView);
    }

    public static void loadCharacterIcon(final long ownerId, final ImageView intoView) {
        p(intoView.getContext()).load(url(ICON_CHARACTER, ownerId)).transform(circularTransform).into(intoView);
    }

    public static void loadCorporationIcon(final long ownerId, final ImageView intoView) {
        p(intoView.getContext()).load(url(ICON_CORPORATION, ownerId)).transform(circularTransform).into(intoView);

    }

    public static void loadCorporationImage(final long ownerId, final ImageView intoView) {
        p(intoView.getContext()).load(url(IMAGE_CORPORATION, ownerId)).into(intoView);
    }

    public static void loadCorporationImage(final long ownerId, final Activity intoView) {
        load(url(IMAGE_CORPORATION, ownerId), intoView);
    }

    public static void loadAllianceIcon(final long ownerId, final ImageView intoView) {
        p(intoView.getContext()).load(url(ICON_ALLIANCE, ownerId)).transform(circularTransform).into(intoView);
    }

    public static void loadAllianceImage(final long ownerId, final ImageView intoView) {
        p(intoView.getContext()).load(url(IMAGE_ALLIANCE, ownerId)).into(intoView);
    }

    public static String getItemImageURL(final Context context, final long charID) {
        return formatUrl(context, IMAGE_ITEM, charID);
    }

    public static String getItemIconURL(final Context context, final long charID) {
        return formatUrl(context, ICON_ITEM, charID);
    }

    public static String getCharacterImageURL(final Context context, final long charID) {
        return formatUrl(context, IMAGE_CHARACTER, charID);
    }

    public static String getCharacterIconURL(final Context context, final long charID) {
        return formatUrl(context, ICON_CHARACTER, charID);
    }

    public static String getCorporationImageURL(final Context context, final long corpID) {
        return formatUrl(context, IMAGE_CORPORATION, corpID);
    }

    public static String getCorporationIconURL(final Context context, final long corpID) {
        return formatUrl(context, ICON_CORPORATION, corpID);
    }

    public static String getAllianceIconURL(final Context context, final long allianceID) {
        return formatUrl(context, ICON_ALLIANCE, allianceID);
    }

    private static String formatUrl(final Context context, final String urlConstant, final long typeID) {
        p(context);
        return String.format(url(urlConstant, typeID));
    }

    public static void load(final String uri, final Context context, final RemoteViews views, int target, int[] widgetIds) {
        p(context).load(uri).into(views, target, widgetIds);
    }

    public static void load(final String uri, final ImageView intoView) {
        p(intoView.getContext()).load(uri).into(intoView);
    }

    public static void load(final String uri, final Activity activity) {

        final Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);

        subscribe(() -> {
                    try {
                        return p(activity).load(uri).resize(rect.width(), rect.height()).centerCrop().get();
                    } catch (IOException e) {
                        return null;
                    }
                },
                bitmap -> {
                    if (null != bitmap) {
                        activity.getWindow().setBackgroundDrawable(new BitmapDrawable(activity.getResources(), bitmap));
                    }
                });
    }

    public static Bitmap getNotificationImage(final Context context, final String url) {
        try {
            return p(context).load(url).resize(128, 128).transform(new CropCircleTransformation()).get();
        }
        catch (IOException e) {
            return null;
        }
    }

    public static Bitmap getTitleImage(final Context context, final String url) {
        try {
            return p(context).load(url).resize(256, 256).transform(circularTransform).get();
        }
        catch (IOException e) {
            return null;
        }
    }

    public static Bitmap getImage(final Context context, final String url) {
        try {
            return p(context).load(url).get();
        }
        catch (IOException e) {
            return null;
        }
    }

    private static Picasso p;
    private static String rootURL = "https://image.eveonline.com";

    private static Picasso p(final Context context) {
        if (null == p) {
            p = Picasso.with(context.getApplicationContext());
            if (LOG.isDebugEnabled()) {
                p.setIndicatorsEnabled(true);
                p.setLoggingEnabled(true);
            }
        }
        return p;
    }

    private static String url(final String urlConstant, final long typeID) {
        return String.format(urlConstant, rootURL, Long.toString(typeID));
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
