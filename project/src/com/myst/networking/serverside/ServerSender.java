package com.myst.networking.serverside;

import com.myst.networking.Codes;
import com.myst.networking.Message;
import com.myst.networking.serverside.model.WorldModel;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.BlockingQueue;

// Continuously reads from message queue for a particular client,
// forwarding to the client.

public class ServerSender extends Thread {
    private BlockingQueue<Object> clientQueue;
    private ObjectOutputStream client;
    private String clientID;
    private WorldModel world;
    private boolean updateClient;

    public ServerSender(BlockingQueue<Object> q, ObjectOutputStream c, String clientID, WorldModel world) {
        clientQueue = q;
        client = c;
        this.clientID = clientID;
        this.world = world;
        this.updateClient = false;

        world.addSender(this);
    }

    @Override
    public void run() {
        try {
            while (true) {
//        don't know why but it won't work without a thread.sleep()
                Thread.sleep(1);
                if (updateClient) {
//          System.out.println("updating client");
                    this.updateClient = false;
                    while (!clientQueue.isEmpty()) {
                        Object msg = clientQueue.take();
                        client.writeObject(msg);
                    }
                }

            }

        } catch (IOException | InterruptedException e) {
            System.out.println("server sender for " + clientID + " ending");

        }
    }

    public void updateClient() {
        clientQueue.add(new Message(Codes.ENTITY_UPDATE, world.getWorldData()));
        this.updateClient = true;
    }

    public void requestClientUpdate() {
        clientQueue.add(new Message(Codes.UPDATE_SERVER, null));
    }

    public void addMessage(Message message) {
        clientQueue.add(message);
        this.updateClient = true;
    }
}


/*

 * Throws InterruptedException if interrupted while waiting

 * See https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/BlockingQueue.html#take--

 */
