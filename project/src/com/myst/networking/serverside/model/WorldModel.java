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

    private ArrayList<ServerSender> senders;

    public WorldModel() {
        this.entities = new ConcurrentHashMap<>();
        entities.put("items", new ArrayList<>());
        this.entityCount = 0;

        senders = new ArrayList<>();
    }

    public void addSender(ServerSender sender) {
        senders.add(sender);
    }

    public void playSound(String clipName, Vector3f location) {
        Message soundMessage = new Message(Codes.PLAY_AUDIO, new PlayAudioData(clipName, location));

        for (ServerSender sender : senders) {
            sender.addMessage(soundMessage);
        }
    }

    public void update() {
        playersData = getPlayers();
        itemsData = getItems();
        bulletsData = getBullets();

        for (EntityData playerData : playersData) {
            float playerX = playerData.transform.pos.x;
            float playerY = playerData.transform.pos.y;
            PlayerData playerSpecific = (PlayerData) playerData.typeData;

            for (EntityData itemData : itemsData) {
                ItemData itemSpecific = (ItemData) itemData.typeData;
                if (!itemData.exists) continue;

                float itemX = itemData.transform.pos.x;
                float itemY = itemData.transform.pos.y;

//              item update code
                if ((playerX - itemX < 0.5f) && (playerX - itemX > -0.5f)) {
                    if ((playerY - itemY < 0.5f) && (playerY - itemY > -0.5f)) {
                        System.out.println("Health: " + playerSpecific.health);
                        System.out.println("Bullets: " + playerSpecific.bulletCount);
                        System.out.println("Invincibility is on: " + playerSpecific.isInvincible);

                        switch (itemData.type) {
                            case ITEM_APPLE:
                                itemData.exists = false;
                                itemSpecific.hidden = true;
                                itemSpecific.isChanged = true;
                                setHealthOfPlayerData(playerSpecific, playerSpecific.health + 10);
                                playSound(Audio.APPLE, playerData.transform.pos);
                                break;
                            case ITEM_MED_KIT:
                                itemData.exists = false;
                                itemSpecific.hidden = true;
                                itemSpecific.isChanged = true;
                                setHealthOfPlayerData(playerSpecific, playerSpecific.health + 30);
//                                playSound(Audio.MED_KIT, playerData.transform.pos);
                                break;
                            case ITEM_INVINCIBILITY_POTION:
                                itemData.exists = false;
                                itemSpecific.hidden = true;
                                itemSpecific.isChanged = true;
                                playerSpecific.isInvincible = true;
                                playerSpecific.lastInvincibilityPickup = System.currentTimeMillis();
                                playSound(Audio.POTION, playerData.transform.pos);
                                break;
                            case ITEM_SPIKES_HIDDEN:
                                if (!itemSpecific.hidden) {
                                    itemSpecific.spikeTimer = System.currentTimeMillis();
                                    itemSpecific.hidden = true;
                                    itemSpecific.isChanged = true;
                                    playSound(Audio.SPIKES, playerData.transform.pos);
                                }
                                break;
                            case ITEM_SPIKES_REVEALED:
                                if (itemSpecific.hidden) {
                                    itemSpecific.spikeTimer = System.currentTimeMillis();
                                    itemSpecific.hidden = false;
                                    itemSpecific.isChanged = true;
                                } else {
                                    if (canTakeSpikeDamage(playerSpecific)) {
                                        setHealthOfPlayerData(playerSpecific, playerSpecific.health - 10);
                                        playerSpecific.lastSpikeDamage = System.currentTimeMillis();
                                        playSound(Audio.HIT_BY_SPIKES, playerData.transform.pos);
                                    }
                                }
                                break;
                            case ITEM_BULLETS_SMALL:
                                itemData.exists = false;
                                itemSpecific.hidden = true;
                                itemSpecific.isChanged = true;
                                setBulletsOfPlayerData(playerSpecific, playerSpecific.bulletCount + 10);
                                playSound(Audio.BULLETS_SMALL, playerData.transform.pos);
                                break;
                            case ITEM_BULLETS_BIG:
                                itemData.exists = false;
                                itemSpecific.hidden = true;
                                itemSpecific.isChanged = true;
                                setBulletsOfPlayerData(playerSpecific, playerSpecific.bulletCount + 30);
                                playSound(Audio.BULLETS_BIG, playerData.transform.pos);
                                break;
                            case ITEM_HEALING_PLATFORM:
                                if (canHealOnPlatform(playerSpecific)) {
                                    setHealthOfPlayerData(playerSpecific, playerSpecific.health + 1);
                                    playerSpecific.lastHealOnPlatform = System.currentTimeMillis();
//                                    playSound(Audio.HIT_BY_SPIKES, playerData.transform.pos);
                                }
                            default:
                                break;
                        }
                    }
                }

//              bullet collision code
            }
            for (EntityData bulletData : bulletsData) {

            }
        }

        for (EntityData itemData : itemsData) {
            if (itemData.type == ITEM_SPIKES_REVEALED || itemData.type == ITEM_SPIKES_HIDDEN) {
                ItemData itemSpecific = (ItemData) itemData.typeData;
                if (System.currentTimeMillis() - itemSpecific.spikeTimer > 5000) {
                    if (itemData.type == ITEM_SPIKES_HIDDEN) {
                        if(itemSpecific.hidden) {
                            itemSpecific.hidden = false;
                        }
                    } else {
                        if(!itemSpecific.hidden) {
                            itemSpecific.hidden = true;
                        }
                    }
                    itemSpecific.isChanged = true;
                    itemSpecific.spikeTimer = Long.MAX_VALUE;
                }
            }
        }
    }

    public boolean canTakeSpikeDamage(PlayerData player) {
        return System.currentTimeMillis() - player.lastSpikeDamage > 1000;
    }

    public boolean canHealOnPlatform(PlayerData player) {
        return System.currentTimeMillis() - player.lastHealOnPlatform > 1000;
    }

    public void setHealthOfPlayerData(PlayerData player, float health) {
        if (System.currentTimeMillis() - player.lastInvincibilityPickup > 20000)
            player.isInvincible = false;

        if (health < 0) health = 0;
        if (health > player.maxHealth) health = player.maxHealth;

        if(!player.isInvincible || health > player.health) {
            player.health = health;
        }
    }

    public void setBulletsOfPlayerData(PlayerData player, int bulletCount) {
        if (bulletCount < 0) bulletCount = 0;
        if (bulletCount > player.maxBulletCount) bulletCount = player.maxBulletCount;

        player.bulletCount = bulletCount;
    }

    public ArrayList<EntityData> getPlayers() {
        ArrayList<EntityData> playersData = new ArrayList<>();

        entities.values().forEach(entityDataArrayList -> {
            for (EntityData e : entityDataArrayList) {
                if (e.exists && (e.type == EntityType.PLAYER)) {
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
                        e.type == EntityType.ITEM_MED_KIT ||
                        e.type == EntityType.ITEM_BULLETS_SMALL ||
                        e.type == EntityType.ITEM_BULLETS_BIG ||
                        e.type == EntityType.ITEM_SPIKES_HIDDEN ||
                        e.type == EntityType.ITEM_SPIKES_REVEALED ||
                        e.type == EntityType.ITEM_INVINCIBILITY_POTION ||
                        e.type == EntityType.ITEM_HEALING_PLATFORM)) {
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
            switch (cEntityData.type) {
                case PLAYER:
                    PlayerData playerData = (PlayerData) cEntityData.typeData;
                    ((PlayerData) entity.typeData).health = playerData.health;
                    ((PlayerData) entity.typeData).maxHealth = playerData.maxHealth;
                    ((PlayerData) entity.typeData).bulletCount = playerData.bulletCount;
                    ((PlayerData) entity.typeData).maxBulletCount = playerData.maxBulletCount;
                    ((PlayerData) entity.typeData).lastSpikeDamage = playerData.lastSpikeDamage;
                    ((PlayerData) entity.typeData).lastHealOnPlatform = playerData.lastHealOnPlatform;
                    ((PlayerData) entity.typeData).lastInvincibilityPickup = playerData.lastInvincibilityPickup;
                    ((PlayerData) entity.typeData).isInvincible = playerData.isInvincible;
                    break;
                case ITEM_SPIKES_HIDDEN:
                case ITEM_SPIKES_REVEALED:
                case ITEM_APPLE:
                case ITEM_MED_KIT:
                case ITEM_BULLETS_SMALL:
                case ITEM_BULLETS_BIG:
                case ITEM_INVINCIBILITY_POTION:
                case ITEM_HEALING_PLATFORM:
                    ItemData itemData = (ItemData) cEntityData.typeData;
                    ((ItemData) entity.typeData).hidden = itemData.hidden;
                    ((ItemData) entity.typeData).lastSpikeDamage = itemData.lastSpikeDamage;
                    ((ItemData) entity.typeData).spikeTimer = itemData.spikeTimer;
                    ((ItemData) entity.typeData).healingTimer = itemData.healingTimer;
                    ((ItemData) entity.typeData).isChanged = itemData.isChanged;
                    break;
                case BULLET:
                    BulletData bulletData = (BulletData) cEntityData.typeData;
                    ((BulletData) entity.typeData).damage = bulletData.damage;
                    ((BulletData) entity.typeData).length = bulletData.length;
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
    public ArrayList<EntityData> getWorldData(boolean forceUpdate) {
        ArrayList<EntityData> out = new ArrayList<>();
        for (String key : entities.keySet()) {
            if (!key.equals("items") || forceUpdate) {
                out.addAll(this.entities.get(key));
            } else {
                for (EntityData entityData : entities.get(key)) {
                    ItemData data = (ItemData) entityData.typeData;
                    if (data.isChanged) {
                        out.add(entityData);
                        data.isChanged = false;
                    }
                }
            }
        }
        return out;
    }
}
