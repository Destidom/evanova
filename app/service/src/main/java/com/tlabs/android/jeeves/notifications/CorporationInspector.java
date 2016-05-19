package com.tlabs.android.jeeves.notifications;

import android.content.Context;

import com.tlabs.eve.api.corporation.CorporationSheet;

import java.util.Collections;
import java.util.List;

abstract class CorporationInspector extends Inspector {

    private final CorporationSheet sheet;

    public CorporationInspector(Context base, final CorporationSheet sheet) {
        super(base);
        this.sheet = sheet;
    }

    abstract Notification inspect(final CorporationSheet sheet);

    @Override
    List<Notification> inspect() {
        final Notification notification = inspect(this.sheet);
        if (null == notification) {
            return Collections.emptyList();
        }
        return Collections.singletonList(notification);
    }

}
