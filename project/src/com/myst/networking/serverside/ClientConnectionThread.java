package com.myst.networking.serverside;

import com.myst.networking.Codes;
import com.myst.networking.Message;
import com.myst.networking.Report;
import com.myst.networking.serverside.model.WorldModel;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

/**
 * Thread that runs whilst a client is connecting
 */
public class ClientConnectionThread extends Thread {
    private Socket socket;
    private ClientTable clientTable;
    private static Integer nOfClients = 0;
    public ArrayList<String> usedIDs;
    private Object key;
    private WorldModel world;
    private TickManager ticker;
    private static int playerCount=0;

    public ClientConnectionThread(Socket _socket, ClientTable _clientTable, ArrayList<String> usedIDs, Object key, WorldModel world, TickManager ticker) {
        this.socket = _socket;
        this.clientTable = _clientTable;
        this.key = key;
        this.world = world;
        this.ticker = ticker;
        this.usedIDs = usedIDs;
    }

    /**
     * Runs a client connection thread
     */
    @Override
    public void run() {
        try {
            ObjectInputStream fromClient = new ObjectInputStream(socket.getInputStream());
            System.out.println("someone connected");
            String clientRequest = null;
            ObjectOutputStream toClient = new ObjectOutputStream(socket.getOutputStream());

            String clientID = null;
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
                    playerCount++;
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }



            toClient.writeObject(world.map);

            int playerCountAtStart = playerCount;
            while(playerCount < 2){
                Thread.sleep(100);
            }
            toClient.writeObject(new Message(Codes.GAME_STARTED, playerCountAtStart));

            System.out.println(clientID);

            clientTable.addClient(clientID);
            BlockingQueue<Object> clientQueue = clientTable.getQueue(clientID);
            clientQueue.add(new Message(Codes.ENTITY_UPDATE, world.getWorldData(true)));
            world.addClient(clientID);

            ServerSender serverSender = new ServerSender(clientQueue, toClient, clientID, world);
            ServerReceiver serverReceiver = new ServerReceiver(clientID, fromClient, clientTable, serverSender, world);

            serverReceiver.start();
            ticker.addSender(serverSender);


        } catch (IOException e) {
            Report.error("IO error " + e.getMessage());
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
