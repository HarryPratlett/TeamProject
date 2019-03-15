package com.myst.networking.serverside.model;

import com.myst.networking.EntityData;
import com.myst.world.entities.EntityType;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import static com.myst.world.entities.EntityType.*;

public class WorldModel {
    //    max 64 entities
//    this needs improving, replace the array list with a hashmap
    private ConcurrentHashMap<String, ArrayList<EntityData>> entities;
    private ArrayList<EntityData> playersData;
    private ArrayList<EntityData> itemsData;
    private int entityCount;

    public WorldModel() {
        this.entities = new ConcurrentHashMap<>();
        this.entityCount = 0;
    }

    public void update() {
        playersData = getPlayers();
        itemsData = getItems();

        for (EntityData playerData : playersData) {
            float playerX = playerData.transform.pos.x;
            float playerY = playerData.transform.pos.y;

            for (EntityData itemData : itemsData) {
                float itemX = itemData.transform.pos.x;
                float itemY = itemData.transform.pos.y;

                if ((playerX - itemX < 0.5f) && (playerX - itemX > -0.5f)) {
                    if ((playerY - itemY < 0.5f) && (playerY - itemY > -0.5f)) {
                        switch (itemData.type) {
                            case ITEM_APPLE:
                                itemData.exists = false;
                                setHealthOfPlayerData(playerData, playerData.health + 10);
//                                Audio.getAudio().play(Audio.APPLE);
                                break;
                            case ITEM_SPIKES_HIDDEN:
                                if (!itemData.hidden) {
                                    itemData.spikeTimer = System.currentTimeMillis();
                                    itemData.hidden = true;
                                }
                                break;
                            case ITEM_SPIKES_REVEALED:
                                if (itemData.hidden) {
                                    itemData.spikeTimer = System.currentTimeMillis();
                                    itemData.hidden = false;
                                } else {
                                    if (canTakeSpikeDamage(playerData)) {
                                        setHealthOfPlayerData(playerData, playerData.health - 10);
                                        playerData.lastSpikeDamage = System.currentTimeMillis();
                                    }
                                }

                        }
                    }
                }
            }
        }

        for (EntityData itemData : itemsData) {
            if (itemData.type == ITEM_SPIKES_REVEALED || itemData.type == ITEM_SPIKES_HIDDEN) {
                if (System.currentTimeMillis() - itemData.spikeTimer > 5000) {
                    if (itemData.type == ITEM_SPIKES_HIDDEN) itemData.hidden = false;
                    else itemData.hidden = true;

                    itemData.spikeTimer = Long.MAX_VALUE;
                }
            }
        }
    }

    public boolean canTakeSpikeDamage(EntityData player) {
        return System.currentTimeMillis() - player.lastSpikeDamage > 1000;
    }

    public void setHealthOfPlayerData(EntityData player, float health) {
        if (health < 0) health = 0;
        if (health > player.maxHealth) health = player.maxHealth;

        player.health = health;
    }

    public ArrayList<EntityData> getPlayers() {
        ArrayList<EntityData> playersData = new ArrayList<>();

        entities.values().forEach(entityDataArrayList -> {
            for (EntityData e : entityDataArrayList) {
                if (e.exists && (e.type == EntityType.PLAYER))
                    playersData.add(e);
            }
        });

        return playersData;
    }

    public ArrayList<EntityData> getItems() {
        ArrayList<EntityData> itemsData = new ArrayList<>();

        entities.values().forEach(entityDataArrayList -> {
            for (EntityData e : entityDataArrayList) {
                if (e.exists && (e.type == ITEM_APPLE ||
                        e.type == EntityType.ITEM_SPIKES_HIDDEN ||
                        e.type == EntityType.ITEM_SPIKES_REVEALED))
                    itemsData.add(e);
            }
        });

        return itemsData;
    }


    public void updateWorld(EntityData entity) {
        ArrayList<EntityData> clientEntities = entities.get(entity.ownerID);

//        this is sloppy and needs redoing after the prototype and replacing with a better system
//        a better system may be replacing it with a hashmap which may even be more efficient

        if (entity.localID + 1 > clientEntities.size()) {
            while (entity.localID + 1 > clientEntities.size()) {
                clientEntities.add(null);
            }
        }

//        try and catch need to catch when you try and access an element which doesn't exist
//        try {
        if (clientEntities.get(entity.localID) != null) {
            entity.health = clientEntities.get(entity.localID).health;
            entity.maxHealth = clientEntities.get(entity.localID).maxHealth;
            entity.exists = clientEntities.get(entity.localID).exists;
            entity.hidden = clientEntities.get(entity.localID).hidden;
            entity.lastSpikeDamage = clientEntities.get(entity.localID).lastSpikeDamage;
            entity.spikeTimer = clientEntities.get(entity.localID).spikeTimer;
        }

        clientEntities.set(entity.localID, entity);
//        } catch() {

//        }
    }

    public void addClient(String clientID) {
        entities.put(clientID, new ArrayList<>());
    }

    //    simply returns all the data stored by the world model in a form for networking to send
    public ArrayList<EntityData> getWorldData() {
        ArrayList<EntityData> out = new ArrayList<>();
        int j = 0;
        for (String key : entities.keySet()) {
            out.addAll(this.entities.get(key));
        }
        return out;
    }
}
