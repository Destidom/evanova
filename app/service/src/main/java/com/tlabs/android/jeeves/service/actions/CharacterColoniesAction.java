package com.tlabs.android.jeeves.service.actions;

import android.content.Context;

import com.tlabs.eve.EveRequest;
import com.tlabs.eve.EveResponse;
import com.tlabs.eve.api.character.PlanetaryColoniesRequest;
import com.tlabs.eve.api.character.PlanetaryColoniesResponse;
import com.tlabs.eve.api.character.PlanetaryColony;
import com.tlabs.eve.api.character.PlanetaryPinsRequest;

import java.util.ArrayList;
import java.util.List;

public final class CharacterColoniesAction extends EveAction {

    public CharacterColoniesAction(final Context context, final long charID) {
        super(
            context,
            new PlanetaryColoniesRequest(Long.toString(charID)));
    }

    @Override
    public EveAction onRequestCompleted(EveRequest<? extends EveResponse> request, EveResponse response) {
        final PlanetaryColoniesRequest pr = (PlanetaryColoniesRequest)request;
        final PlanetaryColoniesResponse ps = (PlanetaryColoniesResponse)response;

        if (null == ps.getColonies() || ps.getColonies().isEmpty()) {
            return null;
        }

        final List<PlanetaryPinsRequest> pins = new ArrayList<>(ps.getColonies().size());
        for (PlanetaryColony c: ps.getColonies()) {
            if (c.getNumberOfPins() > 0) {
                pins.add(new PlanetaryPinsRequest(pr.getCharacterID(), Long.toString(c.getPlanetID())));
            }
        }
        if (pins.isEmpty()) {
            return null;
        }
        return new EveAction(this, pins.toArray(new EveRequest<?>[pins.size()]));
    }
}
