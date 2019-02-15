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

import com.myst.networking.Report;
import com.myst.networking.serverside.ServerReceiver;
import com.myst.world.entities.Entity;

import java.io.*;

import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ClientConnection{
  private static final int PORT = 4444;
  private int clientID;
  private String hostname;
  private Entity[] entities;

  public ClientConnection(Entity[] entities, String host){
    this.hostname = host;
    this.entities = entities;
  }

  public void run() {
    // Open sockets:
    ObjectOutputStream toServer = null;
    ObjectInputStream fromServer = null;
    Socket server = null;

    try {
      server = new Socket(hostname, PORT);
      toServer = new ObjectOutputStream(server.getOutputStream());
      fromServer = new ObjectInputStream(server.getInputStream());
      BlockingQueue<Object> clientqueue = new LinkedBlockingQueue<Object>();
      ClientSender sender = new ClientSender(toServer,clientqueue);
      ClientReceiver receiver = new ClientReceiver(fromServer,sender,this.entities);
      sender.start();
      receiver.start();
    } catch (UnknownHostException e) {
      Report.errorAndGiveUp("Unknown host: " + hostname);
    } catch (IOException e) {
      Report.errorAndGiveUp("The server doesn't seem to be running " + e.getMessage());
    }

//    new ClientReceiver();
//    new ClientSender();
  }

}
