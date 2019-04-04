package com.myst.networking.serverside;

import com.myst.networking.Codes;
import com.myst.networking.Message;
import com.myst.networking.serverside.model.WorldModel;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.BlockingQueue;

/**
 * Continuously reads from message queue for a particular client,
 * forwarding to the client.
 */

public class ServerSender extends Thread {
    private BlockingQueue<Object> clientQueue;
    private ObjectOutputStream client;
    private String clientID;
    private WorldModel world;
    private boolean updateClient;
    private boolean end = false;

    public ServerSender(BlockingQueue<Object> q, ObjectOutputStream c, String clientID, WorldModel world) {
        clientQueue = q;
        client = c;
        this.clientID = clientID;
        this.world = world;

        world.addSender(this);
    }

    /**
     * Runs the server sender
     */
    @Override
    public void run() {
        try {
            while (!end) {
                Thread.sleep(1);
                    while (!clientQueue.isEmpty()) {
                        Object msg = clientQueue.take();
                        client.writeObject(msg);
                        client.reset();
                    }
            }

        } catch (IOException | InterruptedException e) {
            System.out.println("server sender for " + clientID + " ending");
        }
    }

    /**
     * Requests an update for the client
     */
    public void requestClientUpdate() {
        clientQueue.add(new Message(Codes.UPDATE_SERVER, null));
    }

    /**
     * Adds a server message
     * @param message The message to be added
     */
    public void addMessage(Message message) {
        clientQueue.add(message);
    }

    /**
     * Ends a server sender
     */
    public void end(){end = true;}

    /**
     * Ends a game
     */
    public void endGame(){
        clientQueue.add(new Message(Codes.GAME_ENDED,null));
    }

}

