package com.myst.networking.clientside;
// Usage:
//        java ClientConnection user-nickname server-hostname
//
// After initializing and opening appropriate sockets, we start two
// client threads, one to send messages, and another one to get
// messages.
//
// A limitation of our implementation is that there is no provision
// for a client to end after we start it. However, we implemented
// things so that pressing ctrl-c will cause the client to end
// gracefully without causing the server to fail.
//
// Another limitation is that there is no provision to terminate when
// the server dies.

import com.myst.networking.Codes;
import com.myst.networking.EntityData;
import com.myst.networking.Message;
import com.myst.networking.Report;
import com.myst.world.entities.Entity;
import com.myst.world.map.rendering.Tile;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class ClientConnection {
    private static final int PORT = 4444;
    private String hostname;
    private ConcurrentHashMap<String, ConcurrentHashMap<Integer, Entity>> entities;
    private ConcurrentHashMap<String, ConcurrentHashMap<Integer, EntityData>> toRender;
    private ObjectOutputStream toServer;
    private ObjectInputStream fromServer;
    private ClientReceiver receiver;
    public Tile[][] map;

    public ClientConnection(ConcurrentHashMap<String, ConcurrentHashMap<Integer, Entity>> entities,
                            ConcurrentHashMap<String, ConcurrentHashMap<Integer, EntityData>> toRender,
                            String host) {
        this.hostname = host;
        this.entities = entities;
        this.toRender = toRender;
    }

    public void startConnection(String clientID) {
        // Open sockets:
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
//        lazy way of doing this needs to be refactored, only doing for the prototype
                System.exit(1);
                return;
            }

            map = (Tile[][]) fromServer.readObject();

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

    public boolean checkClientID(ObjectOutputStream toServer, ObjectInputStream fromServer, String clientID) {
        try {
//      you ask the server if the client ID is available, if so then it returns true else it returns false
            toServer.writeObject(new Message(Codes.SET_CLIENT_ID, clientID));
            Message response = (Message) fromServer.readObject();
            System.out.println(response.header);
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
