package com.myst.networking.serverside.model;

import com.myst.audio.Audio;
import com.myst.networking.Codes;
import com.myst.networking.EntityData;
import com.myst.networking.Message;
import com.myst.networking.serverside.PlayAudioData;
import com.myst.networking.serverside.ServerSender;
import com.myst.world.entities.BulletData;
import com.myst.world.entities.EntityType;
import com.myst.world.entities.ItemData;
import com.myst.world.entities.PlayerData;
import com.myst.world.map.rendering.Tile;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import static com.myst.world.entities.EntityType.*;

public class WorldModel {
    //    max 64 entities
//    this needs improving, replace the array list with a hashmap
    private ConcurrentHashMap<String, ArrayList<EntityData>> entities;
    private ArrayList<EntityData> playersData;
    private ArrayList<EntityData> itemsData;
    private ArrayList<EntityData> bulletsData;
    private int entityCount;
    public Tile[][] map;
    public int playersAlive = 0;
    public int players = 0;

    private ArrayList<ServerSender> senders;

    public WorldModel() {
        this.entities = new ConcurrentHashMap<>();
        this.entityCount = 0;

        senders = new ArrayList<>();
    }

    public void addSender(ServerSender sender) {
        senders.add(sender);
    }

    public void playSound(String clipName, Vector3f location) {
        Message soundMessage = new Message(Codes.PLAY_AUDIO, new PlayAudioData(clipName, location));

        for(ServerSender sender : senders) {
            sender.addMessage(soundMessage);
        }
    }

    public void update() {
        playersData = getPlayers();
        itemsData = getItems();
        bulletsData = getBullets();
        int alivePlayers = 0;
        int players = 0;

        for (EntityData playerData : playersData) {
            players++;
            float playerX = playerData.transform.pos.x;
            float playerY = playerData.transform.pos.y;
            PlayerData playerSpecific = (PlayerData) playerData.typeData;
            if (playerSpecific.health > 0){
                alivePlayers++;
            }

            for (EntityData itemData : itemsData) {
                ItemData itemSpecific = (ItemData) itemData.typeData;
                if(!itemData.exists) continue;

                float itemX = itemData.transform.pos.x;
                float itemY = itemData.transform.pos.y;

//              item update code
                if ((playerX - itemX < 0.5f) && (playerX - itemX > -0.5f)) {
                    if ((playerY - itemY < 0.5f) && (playerY - itemY > -0.5f)) {
                        switch (itemData.type) {
                            case ITEM_APPLE:
                                itemData.exists = false;
                                setHealthOfPlayerData(playerSpecific, playerSpecific.health + 10);
                                playSound(Audio.APPLE, playerData.transform.pos);
                                break;
                            case ITEM_SPIKES_HIDDEN:
                                if (!itemSpecific.hidden) {
                                    itemSpecific.spikeTimer = System.currentTimeMillis();
                                    itemSpecific.hidden = true;
                                    playSound(Audio.SPIKES, playerData.transform.pos);
                                }
                                break;
                            case ITEM_SPIKES_REVEALED:
                                if (itemSpecific.hidden) {
                                    itemSpecific.spikeTimer = System.currentTimeMillis();
                                    itemSpecific.hidden = false;
                                } else {
                                    if (canTakeSpikeDamage(playerSpecific)) {
                                        setHealthOfPlayerData(playerSpecific, playerSpecific.health - 10);
                                        playerSpecific.lastSpikeDamage = System.currentTimeMillis();
                                        playSound(Audio.HIT_BY_SPIKES, playerData.transform.pos);
                                    }
                                }
                        }
                    }
                }
//              bullet collision code

            }
            for(EntityData bulletData : bulletsData){
                BulletData bulletSpecific = (BulletData) bulletData.typeData;

//                if the bullet is colliding with the owner or we have already calculated whether it collides with
//                something we ignore the bullet

                if(bulletData.ownerID.equals(playerData.ownerID) || bulletSpecific.checked) continue;


                bulletSpecific.line.vector.y *= -1;
                if (playerData.boundingBox.isColliding(bulletSpecific.line,bulletSpecific.length)){
                     playerSpecific.health -= bulletSpecific.damage;
                }
                bulletSpecific.line.vector.y *= -1;
            }
        }

        for(EntityData bulletData : bulletsData){
            ((BulletData) bulletData.typeData).checked = true;
        }

        for (EntityData itemData : itemsData) {
            if (itemData.type == ITEM_SPIKES_REVEALED || itemData.type == ITEM_SPIKES_HIDDEN) {
                ItemData itemSpecific = (ItemData) itemData.typeData;
                if (System.currentTimeMillis() - itemSpecific.spikeTimer > 5000) {
                    if (itemData.type == ITEM_SPIKES_HIDDEN) itemSpecific.hidden = false;
                    else itemSpecific.hidden = true;

                    itemSpecific.spikeTimer = Long.MAX_VALUE;
                }
            }
        }
        this.playersAlive = alivePlayers;
        this.players = players;
    }

    public boolean canTakeSpikeDamage(PlayerData player) {
        return System.currentTimeMillis() - player.lastSpikeDamage > 1000;
    }

    public void setHealthOfPlayerData(PlayerData player, float health) {
        if (health < 0) health = 0;
        if (health > player.maxHealth) health = player.maxHealth;

        player.health = health;
    }

    public ArrayList<EntityData> getPlayers() {
        ArrayList<EntityData> playersData = new ArrayList<>();

        entities.values().forEach(entityDataArrayList -> {
            for (EntityData e : entityDataArrayList) {
                if (e.exists && (e.type == EntityType.PLAYER)){
                    playersData.add(e);
                }

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
                        e.type == EntityType.ITEM_SPIKES_REVEALED)) {
                        itemsData.add(e);

                }
            }
        });

        return itemsData;
    }

    public ArrayList<EntityData> getBullets() {
        ArrayList<EntityData> itemsData = new ArrayList<>();

        entities.values().forEach(entityDataArrayList -> {
            for (EntityData e : entityDataArrayList) {
                if (e.exists && (e.type == BULLET)) {
                    itemsData.add(e);

                }
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
            EntityData cEntityData = clientEntities.get(entity.localID);
            switch(cEntityData.type){
                case PLAYER:
                    PlayerData playerData = (PlayerData) cEntityData.typeData;
                    ((PlayerData) entity.typeData).health = playerData.health;
                    ((PlayerData) entity.typeData).maxHealth = playerData.maxHealth;
                    ((PlayerData) entity.typeData).lastSpikeDamage = playerData.lastSpikeDamage;
                    break;
                case ITEM_SPIKES_HIDDEN:
                case ITEM_SPIKES_REVEALED:
                case ITEM_APPLE:
                    ItemData itemData = (ItemData) cEntityData.typeData;
                    ((ItemData) entity.typeData).hidden = itemData.hidden;
                    ((ItemData) entity.typeData).lastSpikeDamage= itemData.lastSpikeDamage;
                    ((ItemData) entity.typeData).spikeTimer = itemData.spikeTimer;
                    break;
                case BULLET:
                    BulletData bulletData = (BulletData) cEntityData.typeData;
                    ((BulletData) entity.typeData).damage =  bulletData.damage;
                    ((BulletData) entity.typeData).length =  bulletData.length;
                    ((BulletData) entity.typeData).checked = bulletData.checked;
                    break;
            }
            entity.exists = clientEntities.get(entity.localID).exists;
        }
        clientEntities.set(entity.localID, entity);
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
