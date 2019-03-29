package com.myst.AI;

public class Node {
    public int[] pos;
    public float cost;
    public Node(int[] pos,float cost){
        this.pos = pos;
        this.cost = cost;
    }
    public float getCost(){
        return cost;
    }
}
