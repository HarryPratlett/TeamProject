package com.myst.networking.clientside;

import com.myst.GameMain;
import com.myst.AI.BotMain;
import com.myst.audio.Audio;
import com.myst.networking.Codes;
import com.myst.networking.EntityData;
import com.myst.networking.Message;
import com.myst.networking.serverside.PlayAudioData;
import com.myst.world.entities.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Recieves information from server
 */
public class ClientReceiver extends Thread {
    private ClientSender toServer;
    private ObjectInputStream fromServer;
    private ConcurrentHashMap<String,ConcurrentHashMap<Integer, Entity>> entities;
    private ConcurrentHashMap<String,ConcurrentHashMap<Integer, EntityData>> toRender;
    private String clientID;
    private boolean endMe = false;

    public ClientReceiver(ObjectInputStream fromServer,
                          ClientSender toServer,
                          ConcurrentHashMap<String,ConcurrentHashMap<Integer, Entity>> entities,
                          ConcurrentHashMap<String,ConcurrentHashMap<Integer, EntityData>> toRender,
                          String clientID){
        this.fromServer = fromServer;
        this.toServer = toServer;
        this.entities = entities;
        this.clientID = clientID;
        this.toRender = toRender;
    }

    /**
     * Runs a client reciever to recieve stuff from the server
     */
    @Override
    public void run(){
        while(!endMe){
            try {
                Message msg = (Message) fromServer.readObject();
                switch(msg.header){
                    case ENTITY_UPDATE:
                        readInEntities(msg.data);
                        break;
                    case UPDATE_SERVER:
                        sendEntities();
                        break;
                    case NO_AVAILABLE_SPACES:
                        System.out.println("no more spaces on the server");
                        System.exit(1);
                        break;
                    case PLAY_AUDIO:
                        playAudio((PlayAudioData) msg.data);
                        break;
                    case GAME_ENDED:
                        System.out.println("trying to end game");
                        endMe = true;
                        GameMain.endOfGame = true;
                        BotMain.endOfGame = true;
                        break;
                    default:
                        break;
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        toServer.endMe = true;
        try {
            fromServer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Client receiver ended");
    }

    /**
     * Plays audio from the server
     * @param playAudioData
     */
    public void playAudio(PlayAudioData playAudioData) {
        Audio.getAudio().play(playAudioData.clipName, playAudioData.location);
    }

    /**
     * Sends entities to the server
     */
    private void sendEntities(){

        ConcurrentHashMap<Integer, Entity> myEntities = this.entities.get(clientID);
        ArrayList<EntityData> toSend = new ArrayList<EntityData>();
        if(myEntities ==  null){
            return;
        }
        for(Integer i: myEntities.keySet()){
            if (myEntities.get(i) != null){
                EntityData data= myEntities.get(i).getData();
                data.lightSource = myEntities.get(i).visibleToEnemy;
                toSend.add(data);
            }
        }
        Message msg = new Message(Codes.UPDATE_SERVER,toSend);
        toServer.addToQueue(msg);

        toServer.sendQueue();
    }

    /**
     * Reads in entities from map
     * @param data Given map
     */
    private void readInEntities(Object data) {
        ArrayList<EntityData> entityData = (ArrayList<EntityData>) data;
        for (int i = 0; i < entityData.size(); i++) {
            if (entityData.get(i) != null) {
                EntityData entity = entityData.get(i);
                if(!toRender.containsKey(entity.ownerID)){
                    toRender.put(entity.ownerID, new ConcurrentHashMap<Integer,EntityData>());
                }
                if(!entities.containsKey(entity.ownerID)){
                    entities.put(entity.ownerID, new ConcurrentHashMap<Integer,Entity>());
                }
                if(!entities.get(entity.ownerID).containsKey(entity.localID)){
                    toRender.get(entity.ownerID).put(entity.localID, entity);
                }
                else if(entity.ownerID.equals(clientID)){
                    Entity clientEntity = entities.get(clientID).get(entity.localID);
                    switch(clientEntity.getType()){
                        case PLAYER:
                            if(clientEntity.isBot()) {
                                ((Bot) clientEntity).health = ((PlayerData) entity.typeData).health;
                            }else {
                                ((Player) clientEntity).health = ((PlayerData) entity.typeData).health;
                            }
                            break;

                    }
                }
                else if (!entity.ownerID.equals(clientID)) {
                    entities.get(entity.ownerID).get(entity.localID).readInEntityData(entityData.get(i));
                } else {
                    if(entity.type == EntityType.PLAYER) {
                        ((Player) entities.get(entity.ownerID).get(entity.localID)).readInPlayerData((PlayerData) entity.typeData);
                    }
                }
            }
        }
    }
}
