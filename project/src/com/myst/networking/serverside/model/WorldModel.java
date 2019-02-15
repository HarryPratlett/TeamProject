package com.myst.networking.serverside.model;

import com.myst.networking.EntityData;
import com.myst.world.entities.Entity;

public class WorldModel {
    //    max 64 entities
    private EntityData[] entities;
    private int entityCount;

    public WorldModel() {
        this.entities = new EntityData[64];
        this.entityCount = 0;
    }

    public void updateWorld(EntityData entity) {
        if (entities[entity.entityID] == null){
            entityCount++;
        }
        entities[entity.entityID] = entity;
    }

    public EntityData[] getWorldData() {
        EntityData[] out = new EntityData[entityCount];
        int j=0;
        for(int i=0; i < entities.length; i++){
            if (entities[i] != null){
                out[j] = entities[i];
                j++;
            }
        }
        return out;
    }
}
