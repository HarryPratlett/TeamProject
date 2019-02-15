package com.myst.networking.clientside;

import com.myst.networking.Codes;
import com.myst.networking.EntityData;
import com.myst.networking.Message;
import com.myst.world.entities.Entity;

import java.io.IOException;
import java.io.ObjectInputStream;

public class ClientReceiver extends Thread {
    private ClientSender toServer;
    private ObjectInputStream fromServer;
    private Entity[] entities;
    private final int MAX_ENTITIES = 16;
    private int clientID;

//    convert entity[] into hash map potentially currently the arrays indexes corresponds to the entity's ID
    public ClientReceiver(ObjectInputStream fromServer, ClientSender toServer, Entity[] entities){
        this.fromServer = fromServer;
        this.toServer = toServer;
        this.entities = entities;
    }


//    as a general this should be avoided
    @Override
    public void run(){
        System.out.println("client receiver ran");
        while(true){
            try {
                Message msg = (Message) fromServer.readObject();
                System.out.println(msg.header);
                switch(msg.header){
                    case SET_CLIENT_ID:
                        System.out.println("setting client ID");
                        clientID = (Integer) msg.data;
                        setClientID(clientID);
                        System.out.println("received ID");
                        break;
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
        for(int i=0; i < entities.length; i++){
            if (entities[i] != null){
                EntityData[] data = new EntityData[]{entities[i].getData()};
                data[0].clientID = this.clientID;
                System.out.println("sending to Server:");
                System.out.println(data[0].transform.pos);
                Message msg = new Message(Codes.UPDATE_SERVER,data);
                toServer.addToQueue(msg);
            }
        }
//        this tells the Server sender to empty it's queue to the server
        toServer.sendQueue();
    }

//    this can be modified
    private void readInEntities(Object data){
        EntityData[] entityData = (EntityData[]) data;

        System.out.println("Entity data length " + entityData.length);
        for(int i=0; i < entityData.length;i++){
            System.out.println(entityData[i].transform);
        }
    }

    public void setClientID(){
        for(int i=0; i <entities.length; i++){

        }
    }
}
