package com.myst.networking.serverside;


import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Creates a client table for a server
 */
public class ClientTable {
    private ConcurrentMap<String, BlockingQueue> table;

    /**
     * Initialises a client table
     */
    ClientTable(){
        this.table = new ConcurrentHashMap<>();
    }

    /**
     * Adds a client to the table
     * @param clientID The ID of the client to be added
     */
    public void addClient(String clientID){
        this.table.put(clientID, new LinkedBlockingQueue<Object>());
    }

    /**
     * Gets the client queue
     * @param clientID The ID of the client
     * @return Returns the queue
     */
    public BlockingQueue<Object> getQueue(String clientID) {
        return table.get(clientID);
    }

    /**
     * Adds client to the queue
     * @param clientID Client being added
     * @param toSend Object to send
     */
    public void addToClientQueue(String clientID, Object toSend){
        table.get(clientID).add(toSend);
    }

    public void addToEveryQueue(Object data){
        for (Map.Entry<String, BlockingQueue> entry : table.entrySet()) {
            addToClientQueue(entry.getKey(), data);
        }
    }

//    doesn't add to the client name givens queue
    public void addToEveryQueue(String clientID, Object data){
        for (Map.Entry<String, BlockingQueue> entry : table.entrySet()) {
            if (!(entry.getKey().equals(clientID))){
                addToClientQueue(entry.getKey(), data);
            }
        }
    }
}
