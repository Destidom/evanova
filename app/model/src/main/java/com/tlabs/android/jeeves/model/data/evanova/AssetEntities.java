package com.tlabs.android.jeeves.model.data.evanova;


import com.tlabs.android.jeeves.model.data.evanova.entities.AssetEntity;
import com.tlabs.android.jeeves.model.data.sde.EveFacade;
import com.tlabs.eve.api.Asset;
import com.tlabs.eve.api.Item;

import java.util.ArrayList;
import java.util.List;

public final class AssetEntities {

    private AssetEntities() {
    }

    public static Asset transform(final long ownerID, final AssetEntity entity) {
        if (null == entity) {
            return null;
        }

        final Asset asset = new Asset();
        asset.setAssetID(entity.getId());
        asset.setCategoryID(entity.getCategoryID());
        asset.setInventoryFlag(entity.getInventoryFlag());
        //asset.setInventoryFlagName(entity.g);
        asset.setCategoryID(entity.getCategoryID());
        asset.setPackaged(entity.getPackaged() == 1);
        asset.setQuantity((long)entity.getQuantity());
        asset.setRawQuantity((long)entity.getRawQuantity());
        //asset.setAssets();
        //asset.setInventoryName(entity.g);
        asset.setItemID(entity.getTypeID());
        //asset.setItem();
        asset.setLocationID(entity.getLocationID());
        //asset.setLocationName(entity.g);

        return asset;
    }

    public static AssetEntity transform(final EveFacade eve, final long ownerID, final Asset asset) {
        final Item item = eve.getItem(asset.getItemID());
        if (null == item) {
            return null;
        }
        final AssetEntity entity = new AssetEntity();
        entity.setCategoryID(item.getCategoryID());
        entity.setMarketGroupID(eve.getParentMarketGroup(item.getMarketGroupID(), true));
        entity.setOwnerID(ownerID);
        entity.setInventoryFlag(asset.getInventoryFlag());
        entity.setLocationID(asset.getLocationID());
        entity.setPackaged(asset.isPackaged() ? 1 : 0);
        entity.setParentID(-1);
        entity.setQuantity(asset.getQuantity());
        entity.setTypeID(asset.getItemID());
        entity.setRawQuantity(asset.getRawQuantity());

        final List<Asset> children = asset.getAssets();
        final List<AssetEntity> kids = new ArrayList<>(children.size());
        for (Asset child: children) {
            final AssetEntity kid = transform(eve, ownerID, child);
            if (null != kid) {
                kids.add(kid);
            }
        }
        entity.setAssets(kids);
        return entity;
    }

}