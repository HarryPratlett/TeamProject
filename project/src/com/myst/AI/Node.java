package com.myst.AI;

/**
 * Creates a single node
 */
public class Node {
    public int[] pos;
    public float cost;

    /**
     * Constructor for a node
     * @param pos Position of node
     * @param cost Cost of going to node
     */
    public Node(int[] pos,float cost){
        this.pos = pos;
        this.cost = cost;
    }

    /**
     * Returns cost of travelling to node
     * @return
     */
    public float getCost(){
        return cost;
    }
}
