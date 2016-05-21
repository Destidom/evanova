package com.tlabs.android.jeeves.views.fittings;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tlabs.android.jeeves.views.ui.TextProgressBar;
import com.tlabs.eve.dogma.Fitter;
import com.tlabs.eve.dogma.extra.format.AttributeFormat;

import org.apache.commons.lang.StringUtils;

final class FittingFormat {

    private FittingFormat() {
    }


    public static void format(final Fitter fitter, final View view) {
        if (null == view) {
            return;
        }

        if (null == fitter) {
            return;//TODO  change to blank
        }
        final String tag = view.getTag() instanceof String ? (String) view.getTag() : null;

        if (view instanceof TextView) {
            setText(fitter, tag, (TextView) view);
            return;
        }

        if (view instanceof ProgressBar) {
            setProgress(fitter, tag, (ProgressBar) view);
            return;
        }

        if (view instanceof TextProgressBar) {
            setProgress(fitter, tag, (TextProgressBar) view);
            return;
        }

        if (view instanceof ViewGroup) {
            final ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                format(fitter, group.getChildAt(i));
            }
            return;
        }
    }

    private static void setText(final Fitter fitter, final String expression, final TextView view) {
        if (StringUtils.isBlank(expression)) {
            return;
        }
        final Object value = AttributeFormat.evaluate(fitter, expression);
        if (null == value) {
            view.setText(expression);
        }
        else {
            view.setText(value.toString());
        }
    }


    private static void setProgress(final Fitter fitter, final String expression, final ProgressBar view) {
        if (StringUtils.isBlank(expression)) {
            return;
        }

        final Float value = AttributeFormat.evaluate(fitter, expression);
        if (null == value) {
            view.setProgress(0);
        }
        else {
            view.setProgress(value.intValue());
        }
    }

    private static void setProgress(final Fitter fitter, final String expression, final TextProgressBar view) {
        if (StringUtils.isBlank(expression)) {
            return;
        }

        final Number value = AttributeFormat.evaluate(fitter, expression);
        if (null == value) {
            view.getProgressView().setProgress(0);
            view.getTextView().setText("0%");
        }
        else {
            view.getProgressView().setProgress(value.intValue());
            view.getTextView().setText(value.intValue() + " %");
        }
    }
}
