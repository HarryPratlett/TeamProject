package com.myst.networking.serverside;

import com.myst.networking.Codes;
import com.myst.networking.EntityData;
import com.myst.networking.Message;
import com.myst.networking.serverside.model.WorldModel;
import com.myst.world.entities.EntityType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

// Continuously reads from message queue for a particular client,
// forwarding to the client.

public class ServerSender extends Thread {
    private BlockingQueue<Object> clientQueue;
    private ObjectOutputStream client;
    private String clientID;
    private WorldModel world;

    public ServerSender(BlockingQueue<Object> q, ObjectOutputStream c, String clientID, WorldModel world) {
        clientQueue = q;
        client = c;
        this.clientID = clientID;
        this.world = world;

        world.addSender(this);
    }

    @Override
    public void run() {
        try {
            while (true) {
//        don't know why but it won't work without a thread.sleep()
                Thread.sleep(1);
                    while (!clientQueue.isEmpty()) {
//                        System.out.println("size: " + clientQueue.size());
                        Object msg = clientQueue.take();

                        try {
                            Message m = (Message) msg;
                            if(m.header == Codes.ENTITY_UPDATE) {
                                ArrayList<EntityData> dataArrayList = (ArrayList<EntityData>) m.data;

                                for(EntityData ed : dataArrayList)
                                    if(ed.type == EntityType.ITEM_APPLE) {
                                        int x = 1 + 1;
                                    }
                            }
                        } catch(Exception e){}

                        client.writeObject(msg);
                        client.reset();
                    }
            }

        } catch (IOException | InterruptedException e) {
            System.out.println("server sender for " + clientID + " ending");

        }
    }

    public void requestClientUpdate() {
        clientQueue.add(new Message(Codes.UPDATE_SERVER, null));
    }

    public void addMessage(Message message) {
        clientQueue.add(message);
//        if(message.header == Codes.PLAY_AUDIO) System.out.println(clientQueue.size());
    }
}


/*

 * Throws InterruptedException if interrupted while waiting

 * See https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/BlockingQueue.html#take--

 */
