package com.myst.networking.clientside;

import com.myst.networking.Codes;
import com.myst.networking.EntityData;
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

//    this should only be startConnection when the client receives a request to update the server
    @Override
    public void run(){
        try {
            while(true) {
//                don't know why I have to have a thread.sleep in here but it won't work without
//                due to concurrency issues, needs refactoring
                Thread.sleep(1);
                if(sendQueue) {
                    sendQueue = false;
                    while (!queue.isEmpty()) {
        //            convert entities to entity data objects
        //            send the entity data objects
//                        I only cast the object to message for debugging purposes
                            Message toSend = (Message) queue.take();
//                            if(toSend.header == Codes.UPDATE_SERVER){
//                                EntityData[] entities =(EntityData[]) toSend.data;
//                                System.out.println("Sending from the sender");
//                                System.out.println(entities[0].transform.pos);
//                            }
//                            System.out.println(toSend.hashCode());
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
    }

    public void addToQueue(Object object){
        queue.add(object);
    }

    public void sendQueue(){
        sendQueue = true;
    }

}
