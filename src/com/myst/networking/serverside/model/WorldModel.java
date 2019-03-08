package com.myst.networking.serverside.model;

import com.myst.networking.EntityData;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class WorldModel {
    //    max 64 entities
//    this needs improving, replace the array list with a hashmap
    private ConcurrentHashMap<String,ArrayList<EntityData>> entities;
    private int entityCount;

    public WorldModel() {
        this.entities = new ConcurrentHashMap<>();
        this.entityCount = 0;
    }

    public void updateWorld(EntityData entity) {
        ArrayList<EntityData> clientEntities = entities.get(entity.ownerID);

//        this is sloppy and needs redoing after the prototype and replacing with a better system
//        a better system may be replacing it with a hashmap which may even be more efficient

        if (entity.localID +1 > clientEntities.size()){
            while(entity.localID +1 > clientEntities.size()){
                clientEntities.add(null);
            }
        }

//        try and catch need to catch when you try and access an element which doesn't exist
//        try {
            clientEntities.set(entity.localID, entity);
//        } catch() {

//        }
    }

    public void addClient(String clientID){
        entities.put(clientID, new ArrayList<>());
    }

//    simply returns all the data stored by the world model in a form for networking to send
    public ArrayList<EntityData> getWorldData() {
        ArrayList<EntityData> out = new ArrayList<>();
        int j=0;
        for(String key: entities.keySet()){
            out.addAll(this.entities.get(key));
        }
        return out;
    }
}
