package com.myst.networking.serverside;
// Usage:
//        java com.myst.networking.serverside.Server
//
// There is no provision for ending the server gracefully.  It will
// end if (and only if) something exceptional happens.

import com.myst.networking.Report;
import com.myst.networking.serverside.model.WorldModel;
import com.myst.world.map.generating.MapGenerator;
import com.myst.world.map.rendering.Tile;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;

public class Server {
    private static final int PORT = 4444;
    private static final int MAX_CLIENTS = 8;

    /**
     * Start the server listening for connections.
     */
    public static void main(String[] args) {
        new Server().run();
    }

    public void run() {
        WorldModel world = new WorldModel();

        // This table will be shared by the server threads:
        ClientTable clientTable = new ClientTable();

        // I am adapting the client table to be used for different group chats
        ServerSocket serverSocket = null;

        String[] textures = new String[20];
        String path = ("assets/tileset/");

        textures[0] = path + "tile_01";
        textures[1] = path + "tile_02";
        textures[2] = path + "tile_03";
        textures[3] = path + "tile_04";
        textures[4] = path + "tile_05";
        textures[5] = path + "tile_06";
        textures[6] = path + "tile_07";
        textures[7] = path + "tile_08";
        textures[8] = path + "tile_09";
        textures[9] = path + "tile_10";
        textures[10] = path + "tile_11";
        textures[11] = path + "tile_12";
        textures[12] = path + "tile_13";
        textures[13] = path + "tile_14";
        textures[14] = path + "tile_15";
        textures[15] = path + "tile_16";
        textures[16] = path + "tile_17";
        textures[17] = path + "tile_18";
        textures[18] = path + "tile_19";
        textures[19] = path + "tile_20";

        Tile[][] map = new MapGenerator(textures).generateMap(100, 100);

        world.map = map;

        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            Report.errorAndGiveUp("Couldn't listen on port " + PORT);
        }


        Object IDKey = new Object();

        ArrayList<String> usedIDs = new ArrayList<>();

        TickManager ticker = new TickManager(world);
        ticker.start();

        final ServerSocket s = serverSocket;
        try {
            // We loop for ever, as servers usually do.
            while (true) {
                // Listen to the socket, accepting connections from new clients:
                Socket socket = s.accept(); // Matches AAAAA in ClientConnection
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

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                ItemGen.genItems();
            }
        }, 5000);

    }


}