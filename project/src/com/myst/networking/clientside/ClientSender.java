package com.myst.networking.clientside;

import com.myst.networking.Message;
import com.myst.world.entities.Entity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.BlockingQueue;

public class ClientSender extends Thread {
    private ObjectOutputStream toServer;
    private BlockingQueue<Object> queue;
    private boolean sendQueue;

    public ClientSender(ObjectOutputStream toServer, BlockingQueue<Object> q){
        this.toServer = toServer;
        this.queue = q;
        sendQueue = false;
    }

//    this should only be run when the client receives a request to update the server
    @Override
    public void run(){
        try {
            while(true) {
                Thread.sleep(1);
                if(sendQueue) {
                    sendQueue = false;
                    while (!queue.isEmpty()) {
        //            convert entities to entity data objects
        //            send the entity data objects
                            Message toSend = (Message) queue.take();
                            toServer.writeObject(toSend);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void addToQueue(Object object){
        queue.add(object);
    }

    public void sendQueue(){
        sendQueue = true;
    }

}
