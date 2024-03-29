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

/**
 * models the world
 */
public class WorldModel {

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
        entities.put("items", new ArrayList<>());
        this.entityCount = 0;

        senders = new ArrayList<>();
    }

    public void addSender(ServerSender sender) {
        senders.add(sender);
    }

    /**
     * Sends the server messages to play sounds
     * @param clipName Name of the Clip to be played
     * @param location Location of the sound to be played
a     */
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
                if (!itemData.exists) continue;

                float itemX = itemData.transform.pos.x;
                float itemY = itemData.transform.pos.y;

//              item update code
                if ((playerX - itemX < 0.5f) && (playerX - itemX > -0.5f)) {
                    if ((playerY - itemY < 0.5f) && (playerY - itemY > -0.5f)) {
//                        System.out.println("Health: " + playerSpecific.health);
//                        System.out.println("Bullets: " + playerSpecific.bulletCount);
//                        System.out.println("Invincibility is on: " + playerSpecific.isInvincible);
//                        System.out.println("Infinite bullets is on: " + playerSpecific.hasInfiniteBullets);

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
                                playSound(Audio.MED_KIT, playerData.transform.pos);
                                break;
                            case ITEM_INVINCIBILITY_POTION:
                                itemData.exists = false;
                                itemSpecific.hidden = true;
                                itemSpecific.isChanged = true;
                                playerSpecific.isInvincible = true;
                                playerSpecific.lastInvincibilityPickup = System.currentTimeMillis();
                                playSound(Audio.POTION, playerData.transform.pos);
                                break;
                            case ITEM_INFINITE_BULLETS_POTION:
                                itemData.exists = false;
                                itemSpecific.hidden = true;
                                itemSpecific.isChanged = true;
                                playerSpecific.hasInfiniteBullets = true;
                                playerSpecific.lastInfiniteBulletsPickup = System.currentTimeMillis();
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
                            case ITEM_LIGHT_TRAP:
                                if (canHealOnLightTrap(playerSpecific) && itemSpecific.isHealingLightTrap) {
                                    setHealthOfPlayerData(playerSpecific, playerSpecific.health + 2);
                                    playerSpecific.lastHealOnLightTrap = System.currentTimeMillis();
                                    playSound(Audio.HEALTH_UP, playerData.transform.pos);
                                }
                            default:
                                break;
                        }
                    }
                }


            }
            for(EntityData bulletData : bulletsData){
                BulletData bulletSpecific = (BulletData) bulletData.typeData;

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
                if (System.currentTimeMillis() - itemSpecific.spikeTimer > 3000) {
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
        this.playersAlive = alivePlayers;
        this.players = players;
    }

    /**
     * Checks if the player can take spike damage
     * @param player The player
     * @return True if the player can take spike damage
     */
    public boolean canTakeSpikeDamage(PlayerData player) {
        return System.currentTimeMillis() - player.lastSpikeDamage > 1000;
    }

    /**
     * Checks if the player can heal on light trap
     * @param player The player
     * @return True if the player can take spike damage
     */
    public boolean canHealOnLightTrap(PlayerData player) {
        return System.currentTimeMillis() - player.lastHealOnLightTrap > 1000;
    }

    /**
     * Sets health of the player (unless the player is invincible)
     * @param player The player
     * @param health The new health of the player
     */
    public void setHealthOfPlayerData(PlayerData player, float health) {
        if (System.currentTimeMillis() - player.lastInvincibilityPickup > 20000)
            player.isInvincible = false;

        if (health < 0) health = 0;
        if (health > player.maxHealth) health = player.maxHealth;

        if(!player.isInvincible || health > player.health)
            player.health = health;
    }

    /**
     * Sets bullet count of the player (unless the player has infinite bullets)
     * @param player The player
     * @param bulletCount The new bullet count of the player
     */
    public void setBulletsOfPlayerData(PlayerData player, int bulletCount) {
        if (System.currentTimeMillis() - player.lastInfiniteBulletsPickup > 20000)
            player.hasInfiniteBullets = false;

        if (bulletCount < 0) bulletCount = 0;
        if (bulletCount > player.maxBulletCount) bulletCount = player.maxBulletCount;

        if(!player.hasInfiniteBullets || bulletCount > player.bulletCount)
            player.bulletCount = bulletCount;
    }

    /**
     * @return ArrayList of current players in the game
     */
    public ArrayList<EntityData> getPlayers() {
        ArrayList<EntityData> playersData = new ArrayList<>();

        for(String key: entities.keySet()){
            for(EntityData e: entities.get(key)){
                if(e.exists && (e.type == EntityType.PLAYER)){
                    playersData.add(e);
                }
            }
        }

        return playersData;
    }

    /**
     * @return ArrayList of current items in the game
     */
    public ArrayList<EntityData> getItems() {
        ArrayList<EntityData> itemsData = new ArrayList<>();



        for(String key: entities.keySet()){
            for(EntityData e: entities.get(key)){
                if (e.exists && (e.type == ITEM_APPLE ||
                        e.type == EntityType.ITEM_MED_KIT ||
                        e.type == EntityType.ITEM_BULLETS_SMALL ||
                        e.type == EntityType.ITEM_BULLETS_BIG ||
                        e.type == EntityType.ITEM_SPIKES_HIDDEN ||
                        e.type == EntityType.ITEM_SPIKES_REVEALED ||
                        e.type == EntityType.ITEM_INVINCIBILITY_POTION ||
                        e.type == EntityType.ITEM_INFINITE_BULLETS_POTION ||
                        e.type == EntityType.ITEM_LIGHT_TRAP)) {
                    itemsData.add(e);
                }
            }
        }
        return itemsData;
    }

    /**
     * @return ArrayList of current bullets in the game
     */
    public ArrayList<EntityData> getBullets() {
        ArrayList<EntityData> itemsData = new ArrayList<>();

        for(String key: entities.keySet()){
            for(EntityData e: entities.get(key)){
                if (e.exists && (e.type == BULLET)) {
                    itemsData.add(e);
                }
            }
        }


        return itemsData;
    }

    /**
     * Updates world
     * @param entity Entity updating for
     */
    public void updateWorld(EntityData entity) {
        ArrayList<EntityData> clientEntities = entities.get(entity.ownerID);

        if (entity.localID + 1 > clientEntities.size()) {
            while (entity.localID + 1 > clientEntities.size()) {
                clientEntities.add(null);
            }
        }


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
                    ((PlayerData) entity.typeData).lastHealOnLightTrap = playerData.lastHealOnLightTrap;
                    ((PlayerData) entity.typeData).lastInvincibilityPickup = playerData.lastInvincibilityPickup;
                    ((PlayerData) entity.typeData).lastInfiniteBulletsPickup = playerData.lastInfiniteBulletsPickup;
                    ((PlayerData) entity.typeData).isInvincible = playerData.isInvincible;
                    ((PlayerData) entity.typeData).hasInfiniteBullets = playerData.hasInfiniteBullets;
                    break;
                case ITEM_SPIKES_HIDDEN:
                case ITEM_SPIKES_REVEALED:
                case ITEM_APPLE:
                case ITEM_MED_KIT:
                case ITEM_BULLETS_SMALL:
                case ITEM_BULLETS_BIG:
                case ITEM_INVINCIBILITY_POTION:
                case ITEM_INFINITE_BULLETS_POTION:
                case ITEM_LIGHT_TRAP:
                    ItemData itemData = (ItemData) cEntityData.typeData;
                    ((ItemData) entity.typeData).hidden = itemData.hidden;
                    ((ItemData) entity.typeData).lastSpikeDamage = itemData.lastSpikeDamage;
                    ((ItemData) entity.typeData).spikeTimer = itemData.spikeTimer;
                    ((ItemData) entity.typeData).healingTimer = itemData.healingTimer;
                    ((ItemData) entity.typeData).isHealingLightTrap = itemData.isHealingLightTrap;
                    ((ItemData) entity.typeData).isChanged = itemData.isChanged;
                    break;
                case BULLET:
                    BulletData bulletData = (BulletData) cEntityData.typeData;
                    ((BulletData) entity.typeData).damage =  bulletData.damage;
                    ((BulletData) entity.typeData).length =  bulletData.length;
                    ((BulletData) entity.typeData).checked = bulletData.checked;
                    break;
            }
            entity.exists = clientEntities.get(entity.localID).exists;
        } else {
            if(entity.type == BULLET) {
                String owner = entity.ownerID;
                for(EntityData ed : entities.get(owner)) {
                    if(ed.type == PLAYER) {
                        PlayerData pd = (PlayerData) ed.typeData;
                        if(!((PlayerData) ed.typeData).hasInfiniteBullets)
                            pd.bulletCount--;
                        break;
                    }
                }
            }
        }
        clientEntities.set(entity.localID, entity);
    }

    public void addClient(String clientID) {
        entities.put(clientID, new ArrayList<>());
    }

    /**
     * gets world update
     * @param forceUpdate If true, forces update
     * @return Returns new entity data
     */
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
