package com.myst.networking.serverside;

import com.myst.networking.EntityData;
import com.myst.networking.Message;
import com.myst.networking.Report;
import com.myst.networking.serverside.model.WorldModel;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

/**
 * Gets messages from client and puts them in a queue, for another
 */
// thread to forward to the appropriate client.

public class ServerReceiver extends Thread {
    private String clientID;
    private ObjectInputStream myClient;
    private ClientTable clientTable;
    private ServerSender companion;
    private WorldModel world;


    public ServerReceiver(String ID, ObjectInputStream c, ClientTable t, ServerSender s, WorldModel world) {
        System.out.println("server receiver for " + ID + " created");
        clientID = ID;
        myClient = c;
        clientTable = t;
        companion = s;
        this.world = world;
    }

    @Override
    public void run() {
        try {
            while (true) {
                try {
                    Message userMessage = (Message) myClient.readObject();
                    switch (userMessage.header) {
                        case UPDATE_SERVER:
                            updateWorld(userMessage.data);
                            break;
                        case ERROR:
                            Report.error((String) userMessage.data);
                            break;

                    }
                } catch (ClassNotFoundException e) {
                    Report.error("corrupted / incorrect information received from the client");
                }
            }
        } catch (IOException e) {
            Report.error("Something went wrong with the client " + clientID + " " + e.getMessage());
            e.printStackTrace();
            // No point in trying to close sockets. Just give up.
            // We end this thread (we don't do System.exit(1)).
        }

        Report.behaviour("Server receiver ending for " + clientID);
        companion.interrupt();
    }

    /**
     * Updates the world with new data
     * @param data The updated data
     */
    public void updateWorld(Object data) {
        ArrayList<EntityData> entityData = (ArrayList<EntityData>) data;
        for (int i = 0; i < entityData.size(); i++) {
          world.updateWorld(entityData.get(i));
        }
    }
}
