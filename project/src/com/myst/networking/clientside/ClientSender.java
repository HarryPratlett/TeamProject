package com.myst.networking.clientside;

import com.myst.networking.Codes;
import com.myst.networking.EntityData;
import com.myst.networking.Message;
import com.myst.world.entities.Entity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.BlockingQueue;

/**
 * Sends client info to the server
 */
public class ClientSender extends Thread {
    private ObjectOutputStream toServer;
    private BlockingQueue<Object> queue;
    private boolean sendQueue;
    public boolean endMe = false;

    /**
     * Creates a client sender
     * @param toServer Server link
     * @param q Queue of objects
     */
    public ClientSender(ObjectOutputStream toServer, BlockingQueue<Object> q){
        this.toServer = toServer;
        this.queue = q;
        sendQueue = false;
    }


    @Override
    public void run(){
        try {
            while(!endMe) {
                Thread.sleep(1);
                if(sendQueue) {
                    sendQueue = false;
                    while (!queue.isEmpty()) {
                        Message toSend = (Message) queue.take();
                        toServer.writeObject(toSend);
                        toServer.reset();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("im ending");
        try {
            toServer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Method to add to queue
     * @param object Object to add to queue
     */
    public void addToQueue(Object object){
        queue.add(object);
    }

    /**
     * Sends queue to server if true
     */
    public void sendQueue(){
        sendQueue = true;
    }

}
