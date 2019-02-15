package com.myst.networking.serverside;

import com.myst.networking.Codes;
import com.myst.networking.EntityData;
import com.myst.networking.Message;
import com.myst.networking.Report;
import com.myst.networking.serverside.model.WorldModel;

import java.io.*;
import java.net.Socket;

import java.util.concurrent.BlockingQueue;

public class ClientConnectionThread extends Thread {
  private Socket socket;
  private ClientTable clientTable;
  private static Integer nOfClients = 0;
  public Boolean[] availableIDs;
  private Object key;
  private WorldModel world;
  private TickManager ticker;


  public ClientConnectionThread(Socket _socket, ClientTable _clientTable, Boolean[] availableIDs, Object key, WorldModel world, TickManager ticker) {
    this.socket = _socket;
    this.clientTable = _clientTable;
    this.key = key;
    this.availableIDs = availableIDs;
    this.world = world;
    this.ticker = ticker;
    this.availableIDs = new Boolean[8];
    for(int i=0; i < availableIDs.length; i++){
        this.availableIDs[i] = true;
    }


    // we have a maximum of 8 clients per server
//      availableIDs is a list of booleans which if an element is true means it's index is available as an ID e.g.
//      [T,F,F,F,T] ->  IDs 0,4 are available IDs 1,2,3 are not


  }

  @Override
  public void run() {
      try {
          nOfClients++;
          ObjectInputStream fromClient = new ObjectInputStream(socket.getInputStream());

          System.out.println("someone connected");
          // We ask for the client's request
          String clientRequest = null;


          ObjectOutputStream toClient = new ObjectOutputStream(socket.getOutputStream());
          BlockingQueue<Object> clientqueue = null;
          System.out.println("error");

          Integer clientID = null;
          synchronized(key) {
              for (int i = 0; i < availableIDs.length; i++) {
                  if (availableIDs[i]) {

                      availableIDs[i] = false;
                      clientID = i;
                      break;
                  }
              }
          }

          System.out.println("error2");

          if(clientID == null){
              toClient.writeObject(new Message(Codes.NO_AVAILABLE_SPACES,""));
              socket.close();
          } else {
              System.out.println("error3");
              toClient.writeObject(new Message(Codes.SET_CLIENT_ID, clientID));
              clientTable.addClient(clientID);
              clientqueue = clientTable.getQueue(clientID);

              ServerSender serverSender = new ServerSender(clientqueue, toClient, clientID, world);
              ServerReceiver serverReceiver = new ServerReceiver(clientID, fromClient, clientTable, serverSender, world);

//              the ticker controls when the server sender sends out to the client
              System.out.println();
              serverReceiver.start();
              ticker.addSender(serverSender);
          }

    } catch (IOException e) {
          // Lazy approach:
      Report.error("IO error " + e.getMessage());
      // A more sophisticated approach could try to establish a new
      // connection. But this is beyond the scope of this simple exercise.
    }
  }
}
