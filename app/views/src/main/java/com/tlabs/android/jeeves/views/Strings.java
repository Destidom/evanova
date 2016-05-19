package com.tlabs.android.jeeves.views;

import android.content.Context;
import android.widget.TextView;

public final class Strings {

    private Strings() {}

    public static final String q(Context context, final int id, final int quantity) {
        return context.getResources().getQuantityString(id, quantity, quantity);
    }

    public static void q(final TextView textView, int id, int quantity) {
        textView.setText(q(textView.getContext(), id, quantity));
    }

    public static String r(Context context, int id) {
        return context.getResources().getString(id);
    }

    public static String r(Context context, int id, Object... format) {
        String s = context.getResources().getString(id);
        if (null == format || format.length == 0) {
            return s;
        }
        return String.format(s, format);
    }

    public static void r(final TextView textView, int id) {
        textView.setText(r(textView.getContext(), id));
    }

    public static void r(final TextView textView, int id, Object... format) {
        textView.setText(r(textView.getContext(), id, format));
    }
}
