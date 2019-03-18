package com.myst.networking.serverside;

import com.myst.networking.Codes;
import com.myst.networking.EntityData;
import com.myst.networking.Message;
import com.myst.networking.Report;
import com.myst.networking.serverside.model.WorldModel;
import com.myst.world.entities.Item;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

public class ClientConnectionThread extends Thread {
    private Socket socket;
    private ClientTable clientTable;
    private static Integer nOfClients = 0;
    public ArrayList<String> usedIDs;
    private Object key;
    private WorldModel world;
    private TickManager ticker;

    public ClientConnectionThread(Socket _socket, ClientTable _clientTable, ArrayList<String> usedIDs, Object key, WorldModel world, TickManager ticker) {
        this.socket = _socket;
        this.clientTable = _clientTable;
        this.key = key;
        this.world = world;
        this.ticker = ticker;
        this.usedIDs = usedIDs;
        // we have a maximum of 8 clients per server
//      availableIDs is a list of booleans which if an element is true means it's index is available as an ID e.g.
//      [T,F,F,F,T] ->  IDs 0,4 are available IDs 1,2,3 are not


    }

    @Override
    public void run() {
        try {
            ObjectInputStream fromClient = new ObjectInputStream(socket.getInputStream());
            System.out.println("someone connected");
            // We ask for the client's request
            String clientRequest = null;
            ObjectOutputStream toClient = new ObjectOutputStream(socket.getOutputStream());

            String clientID = null;
//          the try loop is about setting up the ClientID
//          may have to put if null in to catch any errors
            try {
                Message msg = (Message) fromClient.readObject();
                if (msg.header != Codes.SET_CLIENT_ID) {
                    Message errorMessage = new Message(Codes.ERROR, "the first message you should send should be a " +
                            "request to set the clientID");
                    toClient.writeObject(errorMessage);
                    toClient.close();
                    fromClient.close();
                    return;
                }
                String requestID = (String) msg.data;
                synchronized (key) {
                    for (int i = 0; i < usedIDs.size(); i++) {
                        if (usedIDs.get(i).equals(requestID)) {
                            Message response = new Message(Codes.ID_UNAVAILABLE, null);

                            toClient.writeObject(response);
                            toClient.close();
                            fromClient.close();
                            return;
                        }
                    }
                    clientID = requestID;
                    usedIDs.add(clientID);
                    toClient.writeObject(new Message(Codes.SUCCESS, null));
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            toClient.writeObject(world.map);

            System.out.println(clientID);

            clientTable.addClient(clientID);
            BlockingQueue<Object> clientQueue = clientTable.getQueue(clientID);
            world.addClient(clientID);

            ServerSender serverSender = new ServerSender(clientQueue, toClient, clientID, world);
            ServerReceiver serverReceiver = new ServerReceiver(clientID, fromClient, clientTable, serverSender, world);

//              the ticker controls when the server sender sends out to the client
            System.out.println();
            serverReceiver.start();
            ticker.addSender(serverSender);


        } catch (IOException e) {
            // Lazy approach:
            Report.error("IO error " + e.getMessage());
            e.printStackTrace();
            // A more sophisticated approach could try to establish a new
            // connection. But this is beyond the scope of this simple exercise.
        }
    }


}
