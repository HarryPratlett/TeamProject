package com.myst.AI;

import com.myst.datatypes.TileCoords;
import com.myst.world.World;
import org.joml.Vector3f;

import java.util.Optional;

/**
 * Map Nodes for AI search
 */
public class MapNode implements Comparable<MapNode> {

    private Vector3f position;
    private Vector3f goal;
    private Optional<MapNode> parent;
    private World world;

    private int f;
    private int g;
    private int h;

    /**
     * Constructor for Map Node
     * @param position Position of bot
     * @param goal Goal for bot
     * @param parent Parent node
     * @param world World bot is in
     */
    public MapNode(Vector3f position, Vector3f goal, MapNode parent, World world) {
        this.position = position;
        this.goal = goal;
        this.parent = Optional.ofNullable(parent);
        this.world = world;

        g = this.parent.isPresent() ? parent.g + 1 : 0;
        h = euclideanDistance();
        f = g + h;
    }

    /**
     * Gets f value
     * @return Returns updated f value
     */
    public int getF() {
        return f;
    }

    /**
     * Gets position of bot
     * @return Position of bot
     */
    public Vector3f getPosition() {
        return position;
    }

    /**
     * Gets parent map node
     * @return Parent map node
     */
    public Optional<MapNode> getParent(){
        return parent;
    }

    /**
     * Gets euclidean distance between two points
     * @return Euclidean distance between two points as an int
     */
    private int euclideanDistance() {
        TileCoords coords = new TileCoords((int)position.x,(int)-position.y);
        if(world.getTile(coords) == null) {
            return Integer.MAX_VALUE;
        }

        else if (world.getTile(coords).isSolid()) {
            return Integer.MAX_VALUE;
        }

        else {
            int x = (int)Math.abs(position.x - goal.x);
            int y = (int)Math.abs((-position.y) - (-goal.y));
            return x + y;
        }
    }

    /**
     * Compares two map nodes
     * @param node Map node to be compared against
     * @return Returns int whether they are same or not
     */
    @Override
    public int compareTo(MapNode node) {
        return f - node.getF();
    }

}
