package com.myst.networking.serverside;

import com.myst.networking.Report;
import com.myst.networking.serverside.model.WorldModel;

import static jdk.nashorn.internal.objects.Global.println;

public class TickManager extends Thread{
    private final int TICKRATE = 60;
    private ServerSender[] senders = new ServerSender[8];
    private Object senderListKey = new Object();
    private WorldModel wm;
    TickManager(WorldModel wm){
        this.wm = wm;
    }
    int ticks = 60;


    @Override
    public void run(){
        while(true){
            ticks++;
            for (int i=0; i< senders.length; i++){
                if(senders[i] != null) {
                    senders[i].requestClientUpdate();
                    senders[i].updateClient();
                }
            }
            if(wm.playersAlive == 1 && wm.players > 1){
                break;
            }
            wm.update();
            if(ticks >= 60){
                System.out.println("players");
                System.out.println(wm.players);
                System.out.println(wm.playersAlive);
                ticks = 0;
            }

            try {
                Thread.sleep((long) 1000 / (long) TICKRATE);
            } catch (InterruptedException e){
                Report.error("Tick Manager is not running correctly");
            }
        }
        for(int i=0; i < senders.length; i++){
            if(senders[i] != null) {
                senders[i].endGame();
                senders[i].end();
            }
        }
        System.out.println("tick manager is ending");
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
