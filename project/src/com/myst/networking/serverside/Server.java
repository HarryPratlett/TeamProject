package com.myst.networking.serverside;
// Usage:
//        java com.myst.networking.serverside.Server
//
// There is no provision for ending the server gracefully.  It will
// end if (and only if) something exceptional happens.

import com.myst.networking.Report;
import com.myst.networking.serverside.model.WorldModel;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
  private static final int PORT = 3465;
  private static final int MAX_CLIENTS = 8;

  /**
   * Start the server listening for connections.
   */
  public static void main(String[] args) {

    WorldModel world = new WorldModel();

    // This table will be shared by the server threads:
    ClientTable clientTable = new ClientTable();

    // I am adapting the client table to be used for different group chats
    ServerSocket serverSocket = null;



    try {
      serverSocket = new ServerSocket(PORT);
    } catch (IOException e) {
      //Report.errorAndGiveUp("Couldn't listen on port " + PORT);
      e.printStackTrace();
    }



    Object IDKey = new Object();

    ArrayList<String> usedIDs = new ArrayList<>();

    TickManager ticker = new TickManager();
    ticker.start();


    try {
      // We loop for ever, as servers usually do.
      while (true) {
        // Listen to the socket, accepting connections from new clients:
        Socket socket = serverSocket.accept(); // Matches AAAAA in ClientConnection
        // By creating another thread to deal with client connection it allows
        // multiple users to connect at once
        new ClientConnectionThread(socket, clientTable, usedIDs, IDKey, world, ticker).start();

      }
    } catch (IOException e) {
      // Lazy approach:
      Report.error("IO error " + e.getMessage());
      // A more sophisticated approach could try to establish a new
      // connection. But this is beyond the scope of this simple exercise.
    }
  }
}
