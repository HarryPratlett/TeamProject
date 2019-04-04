package com.myst.networking.serverside;

import com.myst.networking.Report;
import com.myst.networking.serverside.model.WorldModel;
import com.myst.world.map.generating.MapGenerator;
import com.myst.world.map.rendering.Tile;

import java.io.IOException;
import java.net.Inet6Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Creates a server for the game
 */
public class Server extends Thread{
    private static final int PORT = 0;
    private static final int MAX_CLIENTS = 8;
    public static Integer port = null;
    public static String IP;
    public static boolean foundIPAndPort = false;
    public boolean endMe = false;


    /**
     * Start the server listening for connections.
     */
    public static void main(String[] args) {
        new Server().run();
    }

    /**
     * Runs a server
     */
    public void run() {
        int playerCount;
        System.out.println("starting server");
        WorldModel world = new WorldModel();


        ClientTable clientTable = new ClientTable();


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

//        Tile [][] map = null;
        world.map = map;

        try {
            serverSocket = new ServerSocket(PORT);
            IP = Inet6Address.getLocalHost().getHostAddress();
            System.out.println(IP);

        } catch (IOException e) {
            Report.errorAndGiveUp("Couldn't listen on port " + PORT);
        }

        port = serverSocket.getLocalPort();


        foundIPAndPort = true;

        System.out.println("found IP and port");

        Object IDKey = new Object();
        Object PlayerKey = new Object();

        ArrayList<String> usedIDs = new ArrayList<>();

        TickManager ticker = new TickManager(world);
        ticker.start();

        final ServerSocket s = serverSocket;
        try {
            // We loop for ever, as servers usually do.
            while (true) {
                Socket socket = s.accept();

                new ClientConnectionThread(socket, clientTable, usedIDs, IDKey, world, ticker).start();
            }
        } catch (IOException e) {
            Report.error("IO error " + e.getMessage());
        }

//        new Timer().schedule(new TimerTask() {
//            @Override
//            public void run() {
//                ItemGenerator.genItems();
//            }
//        }, 5000);

    }


}
