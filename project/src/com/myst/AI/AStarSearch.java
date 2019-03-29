package com.myst.AI;

import java.lang.reflect.Array;
import java.nio.IntBuffer;
import java.util.*;

import com.myst.world.map.rendering.Tile;
import com.sun.org.apache.xml.internal.utils.IntStack;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;

import com.myst.datatypes.TileCoords;
import com.myst.datatypes.WorldCoords;
import com.myst.helper.Timer;
import com.myst.world.World;

public class AStarSearch {

    private World world;
    private PriorityQueue<MapNode> openList = new PriorityQueue<MapNode>();
    private ArrayList<MapNode> closedList = new ArrayList<MapNode>();
    private Vector3f position;
    private Vector3f goal;
    private HashMap<List<Integer>,ArrayList<int[]>> graph = new HashMap<>();
    private int mapHeight;
    private int mapWidth;


    public AStarSearch(Vector3f position, Vector3f goal, World world) {
        this.position = position;
        this.goal = goal;
        this.world = world;
        this.mapWidth = world.map.length;
        this.mapHeight = world.map[0].length;
    }

    public HashMap<List<Integer>,ArrayList<int[]>> MapWorld(){
        for(int x=0; x < world.map.length; x++){
            for(int y=0; y < world.map[x].length; y++){
                Tile currentTile = world.map[x][y];
                if(currentTile.isSolid()) continue;
                ArrayList<int[]> xyArray = new ArrayList<int[]>();
                //checking the tile isn't out of bounds
                if (x > 0) {
                    if (!world.map[x - 1][y].isSolid()) {
                        xyArray.add(new int[]{x - 1, y});
//                        checking the diagnol tile isn't out of bounds
                        if(y > 0){
                            if(!world.map[x-1][y-1].isSolid()){
                                xyArray.add(new int[]{x-1,y-1});
                            }
                        }
                        if(y < world.map[y].length - 1){
                            if(!world.map[x-1][y+1].isSolid()){
                                xyArray.add(new int[]{x-1,y+1});
                            }
                        }
                    }

                }
                if(x < world.map[x].length - 1){
                    if(!world.map[x+1][y].isSolid()){
                        xyArray.add(new int[]{x+1,y});
                    }
                    if(y < world.map[y].length - 1){
                        if(!world.map[x+1][y+1].isSolid()){
                            xyArray.add(new int[]{x+1,y+1});
                        }
                    }
                    if(y > 0){
                        if(!world.map[x+1][y-1].isSolid()){
                            xyArray.add(new int[]{x+1,y-1});
                        }
                    }
                }
                if(y < world.map[y].length - 1){
                    if(!world.map[x][y+1].isSolid()){
                        xyArray.add(new int[]{x,y+1});
                    }

                }
                if(y > 0){
                    if(!world.map[x][y-1].isSolid()){
                        xyArray.add(new int[]{x,y-1});
                    }
                }
                graph.put(Arrays.asList(x,y),xyArray);
            }
        }
        return graph;
    }

    public Stack<int[]> findRoute(int[] start, int[] goal){
        Set<List<Integer>> closedSet = new HashSet<>();
        Set<List<Integer>> openSet = new HashSet<>();
        openSet.add(Arrays.asList(start[0],start[1]));

        Comparator<Node> costSorter = Comparator.comparing(Node::getCost);
        PriorityQueue<Node> openSetQueue = new PriorityQueue<>(costSorter);
        openSetQueue.add(new Node(start,heuristicCost(start,goal)));

        HashMap<List<Integer>,int[]> camefrom = new HashMap<>();

        float[][] gScore = new float[mapWidth][mapHeight];
        float[][] fScore = new float[mapWidth][mapHeight];


        for(int i=0; i<gScore.length;i++){
            for(int j=0; j < gScore[i].length; j++){
                gScore[i][j] = Float.POSITIVE_INFINITY;
                fScore[i][j] = Float.POSITIVE_INFINITY;
            }
        }

        gScore[start[0]][start[1]] = 0;
        fScore[start[0]][start[1]] = heuristicCost(start,goal);

        while (!openSet.isEmpty()){
            Node current = openSetQueue.poll();
            if(current.pos[0] == goal[0] && current.pos[1] == goal[1]){
                Stack<int[]> path= reconstructPath(camefrom,current);
                path.push(start);
                return path;
            }

            openSet.remove(Arrays.asList(current.pos[0],current.pos[1]));
            closedSet.add(Arrays.asList(current.pos[0],current.pos[1]));

            for(int[] neighbor: graph.get(Arrays.asList(current.pos[0],current.pos[1]))){
                if(closedSet.contains(Arrays.asList(neighbor[0],neighbor[1]))) continue;

                float tentative_gScore = gScore[current.pos[0]][current.pos[1]] + heuristicCost(neighbor,goal);
                if(!openSet.contains(Arrays.asList(neighbor[0],neighbor[1]))){
                    openSet.add(Arrays.asList(neighbor[0],neighbor[1]));
                    openSetQueue.add(new Node(neighbor,heuristicCost(neighbor,goal)));
                }else if (tentative_gScore >= gScore[neighbor[0]][neighbor[1]]){
                    continue;
                }
                camefrom.put(Arrays.asList(neighbor[0],neighbor[1]),current.pos);
                gScore[neighbor[0]][neighbor[1]] = tentative_gScore;
                fScore[neighbor[0]][neighbor[1]] = tentative_gScore + heuristicCost(neighbor,goal);
            }

        }
        return null;



    }

    public static float heuristicCost(int[] i,int[] j){
        return ((i[0]-j[0])*(i[0]-j[0])) + ((i[1]-j[1])*(i[1]-j[1]));
    }

    public static Stack<int[]> reconstructPath(HashMap<List<Integer>,int[]> cameFrom, Node n){
        Stack<int[]> route = new Stack<>();
        int[] current = n.pos;
        while(cameFrom.keySet().contains(Arrays.asList(current[0],current[1]))){
            current = cameFrom.get(Arrays.asList(current[0],current[1]));
            route.push(current);
        }
        return route;
    }


//    public ArrayList<Vector3f> getPath(){
//        ArrayList<Vector3f> routeAsCoords = new ArrayList<Vector3f>();
//        ArrayList<MapNode> route = search();
//        for(MapNode node : route) {
//            routeAsCoords.add(node.getPosition());
//
//        }
//        return routeAsCoords;
//    }
//
//    private ArrayList<MapNode> search(){
//        MapNode start = new MapNode(position, goal, null, world);
//        openList.add(start);
//        while(!openList.isEmpty()) {
//            MapNode searchNode = openList.poll();
//            if(searchNode.getPosition().equals(goal)) {
//                return getRoute(searchNode);
//            }
//            ArrayList<MapNode> children = generateChildren(searchNode);
//
//
//            for(MapNode child:children) {
//                if(!closedList.contains(child)) {
//                    if(child.getF() != Integer.MAX_VALUE) {
//                        openList.add(child);
//                    }
//                }
//            }
//            closedList.add(searchNode);
//        }
//        return null;
//    }
//
//    private ArrayList<MapNode> generateChildren(MapNode parent){
//        ArrayList<MapNode> children = new ArrayList<MapNode>();
//        //double startTime = Timer.getTime();
//        for(int x = -1; x <= 1; x++) {
//            for(int y = -1; y <= 1; y++) {
//                Vector3f childPosition = new Vector3f(parent.getPosition().x + x, parent.getPosition().y + y, 1);
//                if(world.getTile((int)childPosition.x,(int)-childPosition.y) != null && !world.getTile((int) childPosition.x, (int) -childPosition.y).isSolid()) {
//                    children.add(new MapNode(childPosition, goal, parent, world));
//                    //if((Timer.getTime()-startTime)>= 3.00) {
//                      //  return children;
//                    //}
//                }
//            }
//        }
//
//        return children;
//    }
//
//    public ArrayList<MapNode> getRoute(MapNode node){
//        ArrayList<MapNode> route = new ArrayList<MapNode>();
//        Optional<MapNode> parent = node.getParent();
//        if(parent.isPresent()) {
//            route.addAll(getRoute(parent.get()));
//        }
//        route.add(node);
//        return route;
//    }

}
