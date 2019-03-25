package com.myst.world.entities;

import com.myst.world.collisions.Line;

import java.io.Serializable;

public class BulletData implements Serializable {
    public float damage;
    public float length;
    public Line line;
    public boolean checked = false;
}
