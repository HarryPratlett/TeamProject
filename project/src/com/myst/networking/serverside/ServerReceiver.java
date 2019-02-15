package com.myst.networking.serverside;

import com.myst.networking.EntityData;
import com.myst.networking.Message;
import com.myst.networking.Report;
import com.myst.networking.serverside.model.WorldModel;

import java.io.IOException;
import java.io.ObjectInputStream;

// Gets messages from client and puts them in a queue, for another
// thread to forward to the appropriate client.

public class ServerReceiver extends Thread {
  private Integer clientID;
  private ObjectInputStream myClient;
  private ClientTable clientTable;
  private ServerSender companion;
  private WorldModel world;

  /**
   * Constructs a new server receiver.
   * @param n the name of the client with which this server is communicating
   * @param c the reader with which this receiver will read data
   * @param t the table of known clients and connections
   * @param s the corresponding sender for this receiver
   */
  public ServerReceiver(Integer n, ObjectInputStream c, ClientTable t, ServerSender s, WorldModel world) {
    System.out.println("server receiver for " + n + " created");
    clientID = n;
    myClient = c;
    clientTable = t;
    companion = s;
    this.world = world;
  }

  /**
   * Starts this server receiver.
   */
  public void run() {
    try {
      while (true) {
        try {
          Message userMessage = (Message) myClient.readObject();
          System.out.println("received data from client");
          switch(userMessage.header){
            case UPDATE_SERVER:
              updateWorld(userMessage.data);
              break;
          }


        } catch(ClassNotFoundException e){
          Report.error("corrupted / incorrect information received from the client");
          e.printStackTrace();
        }
      }
    } catch (IOException e) {
      Report.error("Something went wrong with the client " + clientID + " " + e.getMessage());
      // No point in trying to close sockets. Just give up.
      // We end this thread (we don't do System.exit(1)).
    }

    Report.behaviour("Server receiver ending for " + clientID);
    companion.interrupt();
  }

  public void updateWorld(Object data){
    EntityData[] entityData = (EntityData[]) data;
    System.out.println(entityData[0].transform.pos);
    for(int i=0; i < entityData.length; i++){
      world.updateWorld(entityData[i]);
    }
  }
}
