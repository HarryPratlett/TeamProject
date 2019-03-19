package com.myst.networking.serverside;

import com.myst.networking.Report;
import com.myst.networking.serverside.model.WorldModel;

public class TickManager extends Thread{
    private final int TICKRATE = 60;
    private ServerSender[] senders = new ServerSender[8];
    private Object senderListKey = new Object();
    private WorldModel wm;
    TickManager(WorldModel wm){
        this.wm = wm;
    }

    @Override
    public void run(){
        while(true){

            for (int i=0; i< senders.length; i++){
                if(senders[i] != null) {
//                    System.out.println("tick");
                    senders[i].requestClientUpdate();
                    senders[i].updateClient();

                }
            }
            wm.update();
            try {
                Thread.sleep((long) 1000 / (long) TICKRATE);
            } catch (InterruptedException e){
                Report.error("Tick Manager is not running correctly");
            }
        }
    }

    public void addSender(ServerSender sender){
        Boolean added = false;
        synchronized (senderListKey){
            for(int i=0; i<senders.length; i++){
                if (senders[i] == null){
                    senders[i] = sender;
                    senders[i].start();
                    added = true;
                    break;
                }
            }
        }
        if (!added){
            throw new IllegalStateException("no more space to add a server sender");
        }
    }

}
