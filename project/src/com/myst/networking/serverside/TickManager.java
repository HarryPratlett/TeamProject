package com.myst.networking.serverside;

import com.myst.networking.Codes;
import com.myst.networking.EntityData;
import com.myst.networking.Message;
import com.myst.networking.Report;
import com.myst.networking.serverside.model.WorldModel;
import com.myst.world.entities.EntityType;
import com.myst.world.entities.ItemData;

import java.util.ArrayList;

public class TickManager extends Thread{
    private final int TICKRATE = 60;
    private ServerSender[] senders = new ServerSender[8];
    private Object senderListKey = new Object();
    private WorldModel wm;
    private ItemGenerator itemGenerator;
    TickManager(WorldModel wm){
        this.wm = wm;
        itemGenerator = new ItemGenerator(wm);
    }

    @Override
    public void run(){
        while(true){
            wm.update();
            itemGenerator.update();

            ArrayList<EntityData> worldData = wm.getWorldData(false);
            for (int i=0; i< senders.length; i++){
                if(senders[i] != null) {


                    for(EntityData ed : worldData) {
                        if(ed.type == EntityType.ITEM_APPLE) {
                            ItemData id = (ItemData) ed.typeData;
                            System.out.println(id.hidden);
                        }
                    }

                    senders[i].requestClientUpdate();
                    senders[i].addMessage(new Message(Codes.ENTITY_UPDATE, worldData));

                }
            }

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
