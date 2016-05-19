package com.tlabs.android.jeeves.service.actions;

import android.content.Context;

import com.tlabs.eve.api.character.CharacterCalendarRequest;
import com.tlabs.eve.api.character.CharacterIndustryJobsRequest;
import com.tlabs.eve.api.character.CharacterMarketOrderRequest;
import com.tlabs.eve.api.character.CharacterResearchRequest;
import com.tlabs.eve.api.character.CharacterWalletJournalRequest;
import com.tlabs.eve.api.character.CharacterWalletTransactionsRequest;

public final class CharacterCacheAction extends EveAction {
    public CharacterCacheAction(final Context context, final long charID) {
        super(
                context,
                false,
                new CharacterMarketOrderRequest(Long.toString(charID)),
                new CharacterWalletTransactionsRequest(Long.toString(charID)),
                new CharacterWalletJournalRequest(Long.toString(charID)),
                new CharacterIndustryJobsRequest(Long.toString(charID)),
                new CharacterResearchRequest(Long.toString(charID)),
                new CharacterCalendarRequest(Long.toString(charID)));
    }
}
