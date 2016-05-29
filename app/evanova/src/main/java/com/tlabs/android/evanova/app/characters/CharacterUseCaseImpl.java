package com.tlabs.android.evanova.app.characters;

import android.content.Context;

import com.tlabs.android.evanova.content.ContentFacade;
import com.tlabs.android.jeeves.model.EveAccount;
import com.tlabs.android.jeeves.model.EveCharacter;
import com.tlabs.android.jeeves.network.EveCrest;
import com.tlabs.eve.api.character.CharacterCalendar;

import org.devfleet.crest.model.CrestItem;
import org.devfleet.crest.model.CrestLocation;
import org.devfleet.crest.model.CrestSolarSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CharacterUseCaseImpl implements CharacterUseCase {

    private static final Logger LOG = LoggerFactory.getLogger(CharacterUseCaseImpl.class);

    private final Context context;
    private final ContentFacade content;

    @Inject
    public CharacterUseCaseImpl(
            final Context context,
            final ContentFacade content) {
        this.context = context;
        this.content = content;
    }

    @Override
    public List<EveCharacter> loadCharacters() {
        final List<EveCharacter> characters = new ArrayList<>();
        for (Long id: content.listCharacters()) {
            characters.add(content.getCharacter(id, false));
        }
        return characters;
    }

    @Override
    public EveCharacter loadCharacter(long id) {
        return content.getCharacter(id, true);
    }

    @Override
    public Subscription subscribe(final EveCharacter character, final Observer<EveCharacter> observer) {
        if (null == character) {
            LOG.warn("subscribe: null character");
            return observable(Observable.<EveCharacter>just(null)).subscribe(observer);
        }

        final EveAccount account = content.getOwnerAccount(character.getID());
        if (null == account) {
            LOG.warn("subscribe: null account");
            return observable(Observable.just(character)).subscribe(observer);
        }

        return observable(account, character).subscribe(observer);
    }

    @Override
    public CharacterCalendar loadCalendar(final long ownerId) {
        return this.content.getCharacterCalendar(ownerId);
    }

    private Observable<EveCharacter> observable(final EveAccount account, final EveCharacter character) {
        return observable(Observable
                .interval(1, 5, TimeUnit.SECONDS)
                .map(t -> EveCrest.obtainService(context, account))
                .map(crest -> {
                    if (null == crest) {
                        LOG.warn("Could not obtain a CrestService");
                        return character;
                    }
                    try {
                        final CrestLocation location = crest.getLocation();
                        if ((null == location) || (null == location.getSolarSystem())) {
                            return character;
                        }
                        final CrestSolarSystem solarSystem = crest.getSolarSystem(location.getSolarSystem().getId());
                        if (null == solarSystem) {
                            return character;
                        }
                        setLocation(character, location, solarSystem);
                        return character;
                    }
                    catch (IllegalStateException e) {
                        LOG.error(e.getLocalizedMessage());
                        return character;
                    }
                }));
    }

    private static void setLocation(final EveCharacter character, CrestLocation location, CrestSolarSystem solarSystem) {
        final EveCharacter.Location l = character.getLocation();
        l.setLocationName(solarSystem.getName());
        l.setLocationID(solarSystem.getId());
        l.setConstellationID(solarSystem.getConstellation().getId());
        l.setSecurityStatus(solarSystem.getSecurityStatus());
        l.setSovereignty(solarSystem.getSovereignty().getName());
        l.setSovereigntyID(solarSystem.getSovereignty().getId());

        final CrestItem station = location.getStation();
        if (null == station) {
            l.setStationID(0);
            l.setStationName(null);
        }
        else {
            l.setStationID(station.getId());
            l.setStationName(station.getName());
        }
    }

    private static <T> Observable<T> observable(final Observable<T> observable) {
        return observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

}
