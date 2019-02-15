package com.myst.networking.serverside;


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.Map;

public class ClientTable {
    private ConcurrentMap<Integer, BlockingQueue> table;

    ClientTable(){
        this.table = new ConcurrentHashMap<>();
    }

    public void addClient(Integer clientID){
        this.table.put(clientID, new LinkedBlockingQueue<Object>());
    }

    public BlockingQueue<Object> getQueue(Integer clientID) {
        return table.get(clientID);
    }

    public void addToClientQueue(Integer clientID, Object toSend){
        table.get(clientID).add(toSend);
    }

    public void addToEveryQueue(Object data){
        for (Map.Entry<Integer, BlockingQueue> entry : table.entrySet()) {
            addToClientQueue(entry.getKey(), data);
        }
    }

//    doesn't add to the client name givens queue
    public void addToEveryQueue(Integer clientID, Object data){
        for (Map.Entry<Integer, BlockingQueue> entry : table.entrySet()) {
            if (!(entry.getKey().equals(clientID))){
                addToClientQueue(entry.getKey(), data);
            }
        }
    }


}
