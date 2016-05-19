package com.tlabs.android.jeeves.notifications;

import android.content.Context;

import com.tlabs.eve.api.character.CharacterSheet;

import java.util.Collections;
import java.util.List;

abstract class CharacterInspector extends Inspector {

    private final CharacterSheet sheet;

    public CharacterInspector(Context base, final CharacterSheet sheet) {
        super(base);
        this.sheet = sheet;
    }

    abstract Notification inspect(final CharacterSheet sheet);

    @Override
    List<Notification> inspect() {
        final Notification notification = inspect(this.sheet);
        if (null == notification) {
            return Collections.emptyList();
        }
        return Collections.singletonList(notification);
    }
}
