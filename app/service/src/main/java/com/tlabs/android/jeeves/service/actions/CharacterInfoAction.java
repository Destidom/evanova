package com.tlabs.android.jeeves.service.actions;

import android.content.Context;

import com.tlabs.eve.EveRequest;
import com.tlabs.eve.EveResponse;
import com.tlabs.eve.api.AssetListResponse;
import com.tlabs.eve.api.character.CharacterAssetsRequest;
import com.tlabs.eve.api.character.CharacterInfo;
import com.tlabs.eve.api.character.CharacterInfoRequest;
import com.tlabs.eve.api.character.CharacterInfoResponse;
import com.tlabs.eve.api.character.CharacterSheetRequest;
import com.tlabs.eve.api.character.CharacterSheetResponse;
import com.tlabs.eve.api.character.CharacterTrainingQueueRequest;
import com.tlabs.eve.api.character.CharacterTrainingQueueResponse;
import com.tlabs.eve.api.mail.MailingListsRequest;
import com.tlabs.eve.api.mail.MailingListsResponse;

public final class CharacterInfoAction extends EveAction {

    private CharacterInfo characterInfo;

    public CharacterInfoAction(final Context context, final long charID) {
        super(
                context,
                new CharacterInfoRequest(Long.toString(charID)),
                new CharacterSheetRequest(Long.toString(charID)),
                new CharacterTrainingQueueRequest(Long.toString(charID)),
                new CharacterAssetsRequest(Long.toString(charID)),
                new MailingListsRequest(Long.toString(charID)));

    }
            
    @Override
    public EveAction onRequestCompleted(EveRequest<? extends EveResponse> request, EveResponse response) {
        if (request instanceof CharacterInfoRequest) {
            this.characterInfo = ((CharacterInfoResponse)response).getCharacterInfo();
            return null;
        }

        if (response.getCached()) {
            return null;
        }

        if (request instanceof CharacterSheetRequest) {
            onActionCompleted((CharacterSheetRequest)request, (CharacterSheetResponse)response);
            return null;
        }

        if (request instanceof CharacterTrainingQueueRequest) {
            onActionCompleted((CharacterTrainingQueueRequest)request, (CharacterTrainingQueueResponse)response);
            return null;
        }
        if (request instanceof CharacterAssetsRequest) {
            onActionCompleted((CharacterAssetsRequest)request, (AssetListResponse)response);
        }
        if (request instanceof MailingListsRequest) {
            onActionCompleted((MailingListsRequest) request, (MailingListsResponse) response);
            return null;
        }
        return null;
    }

    private void onActionCompleted(CharacterSheetRequest request, CharacterSheetResponse response) {
        evanova.updateCharacter(this.characterInfo, response.getCharacter());
    }
        
    private void onActionCompleted(CharacterTrainingQueueRequest request, CharacterTrainingQueueResponse response) {
        evanova.saveTrainingQueue(Long.parseLong(request.getCharacterID()), response.getTrainingQueue());
    }

    private void onActionCompleted(CharacterAssetsRequest request, AssetListResponse response) {
        if (response.getAssets().size() == 0) {
            return;
        }

        evanova.saveAssets(
                Long.parseLong(request.getCharacterID()),
                response.getAssets());
    }

    private void onActionCompleted(MailingListsRequest request, MailingListsResponse response) {
        this.mail.setup(Long.parseLong(request.getCharacterID()), response.getMailingLists());
    }
}
