package com.myst.networking.clientside;

import com.myst.networking.Codes;
import com.myst.networking.EntityData;
import com.myst.networking.Message;
import com.myst.world.entities.Enemy;
import com.myst.world.entities.Entity;
import com.myst.world.entities.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class ClientReceiver extends Thread {
    private ClientSender toServer;
    private ObjectInputStream fromServer;
    private ConcurrentHashMap<String,HashMap<Integer, Entity>> entities;
    private ConcurrentHashMap<String,HashMap<Integer, EntityData>> toRender;
    private String clientID;

//    convert entity[] into hash map potentially currently the arrays indexes corresponds to the entity's ID
    public ClientReceiver(ObjectInputStream fromServer,
                          ClientSender toServer,
                          ConcurrentHashMap<String,HashMap<Integer, Entity>> entities,
                          ConcurrentHashMap<String,HashMap<Integer, EntityData>> toRender,
                          String clientID){
        this.fromServer = fromServer;
        this.toServer = toServer;
        this.entities = entities;
        this.clientID = clientID;
        this.toRender = toRender;
    }

    @Override
    public void run(){
        System.out.println("client receiver ran");
        while(true){
            try {
                Message msg = (Message) fromServer.readObject();
//                System.out.println(msg.header);
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
                    default:
                        break;
                }

            } catch (IOException e) {
                System.out.println("something happened");
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

//    sends the entities positions to the server
    private void sendEntities(){
//        this needs refactoring
        HashMap<Integer, Entity> myEntities = this.entities.get(clientID);
        ArrayList<EntityData> toSend = new ArrayList<EntityData>();
        if(myEntities ==  null){
//            this is sloppy and needs tidying once debugging has finished
            return;
        }
        for(Integer i: myEntities.keySet()){
            if (myEntities.get(i) != null){
                toSend.add(myEntities.get(i).getData());
//                System.out.println("sending to Server:");
//                System.out.println("entity owned by " + myEntities.get(i).getData().ownerID);
//                System.out.println("entity with ID " + myEntities.get(i).getData().localID);
//                System.out.println("with position" + myEntities.get(i).getData().transform.pos);
            }
        }
        Message msg = new Message(Codes.UPDATE_SERVER,toSend);
        toServer.addToQueue(msg);
//        this tells the Server sender to empty it's queue to the server
        toServer.sendQueue();
    }

//    this can be modified
    private void readInEntities(Object data) {
        ArrayList<EntityData> entityData = (ArrayList<EntityData>) data;

//        System.out.println("Entity data length " + entityData.size());
        for (int i = 0; i < entityData.size(); i++) {
            if (entityData.get(i) != null) {
                EntityData entity = entityData.get(i);
//                System.out.println(entityData.get(i).transform.pos);
                if(entities.get(entity.ownerID) == null){
                    entities.put(entity.ownerID, new HashMap<Integer,Entity>());
                    if(toRender.get(entity.ownerID) == null){
                        toRender.put(entity.ownerID, new HashMap<Integer,EntityData>());
                    }
                }
                if(entities.get(entity.ownerID).get(entity.localID) == null){
                    if(toRender.get(entity.ownerID).get(entity.localID) == null){
                        toRender.get(entity.ownerID).put(entity.localID, entity);
                    }
                }
                else if (!entity.ownerID.equals(clientID)) {
                    entities.get(entity.ownerID).get(entity.localID).transform = entityData.get(i).transform;
                }
            }
        }


    }


}
