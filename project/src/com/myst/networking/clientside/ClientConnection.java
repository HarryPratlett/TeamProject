package com.myst.networking.clientside;

import com.myst.networking.Codes;
import com.myst.networking.EntityData;
import com.myst.networking.Message;
import com.myst.networking.Report;
import com.myst.world.entities.Entity;
import com.myst.world.map.rendering.Tile;
import org.joml.Vector2f;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Class which client connects to server
 */
public class ClientConnection extends Thread{
    private Integer PORT = 4444;
    private String hostname;
    private ConcurrentHashMap<String, ConcurrentHashMap<Integer, Entity>> entities;
    private ConcurrentHashMap<String, ConcurrentHashMap<Integer, EntityData>> toRender;
    private ObjectOutputStream toServer;
    private ObjectInputStream fromServer;
    private ClientReceiver receiver;
    public  Vector2f startLoc = new Vector2f();
    private String clientID;
    public Tile[][] map;

    public ClientConnection(ConcurrentHashMap<String, ConcurrentHashMap<Integer, Entity>> entities,
                            ConcurrentHashMap<String, ConcurrentHashMap<Integer, EntityData>> toRender,
                            String host,
                            Integer port,
                            String clientID) {
        this.hostname = host;
        this.entities = entities;
        this.toRender = toRender;
        this.clientID = clientID;
        this.PORT = port;
    }

    /**
     * Opens a pair of sockets
     */
    public void run() {
        ObjectOutputStream toServer = null;
        ObjectInputStream fromServer = null;
        Socket server = null;

        try {
            server = new Socket(hostname, PORT);
            toServer = new ObjectOutputStream(server.getOutputStream());
            fromServer = new ObjectInputStream(server.getInputStream());
            this.toServer = toServer;
            this.fromServer = fromServer;
            BlockingQueue<Object> clientqueue = new LinkedBlockingQueue<Object>();

            if (!checkClientID(toServer, fromServer, clientID)) {
                Report.error("client ID was unavailable");
                System.exit(1);
                return;
            }

            map = (Tile[][]) fromServer.readObject();
            Message msg = (Message) fromServer.readObject();
            startLoc.x = ((((int) msg.data) % 2) * 97) + 1;
            if((int) msg.data == 3 || (int) msg.data == 4){
                startLoc.y = -99;
            } else{
                startLoc.y = -1;
            }

            ClientSender sender = new ClientSender(toServer, clientqueue);
            receiver = new ClientReceiver(fromServer, sender, this.entities, toRender, clientID);
            sender.start();
            receiver.start();
        } catch (UnknownHostException e) {
            Report.errorAndGiveUp("Unknown host: " + hostname);
        } catch (IOException e) {
            Report.errorAndGiveUp("The server doesn't seem to be running " + e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks a client ID to see if it is available
     * @param toServer Sends to server
     * @param fromServer Recieves from server
     * @param clientID Given client ID
     * @return True if available false if not
     */
    public boolean checkClientID(ObjectOutputStream toServer, ObjectInputStream fromServer, String clientID) {
        try {
            toServer.writeObject(new Message(Codes.SET_CLIENT_ID, clientID));
            Message response = (Message) fromServer.readObject();
            switch (response.header) {
                case SUCCESS:
                    return true;
                case ID_UNAVAILABLE:
                    return false;
                case ERROR:
                    return false;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
}
