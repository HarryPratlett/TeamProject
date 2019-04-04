package com.myst.networking;

import com.myst.world.collisions.AABB;
import com.myst.world.entities.EntityType;
import com.myst.world.view.Transform;

import java.io.Serializable;

/**
 * All data for given entities
 */
public class EntityData implements Serializable {
    public String ownerID;
    public Integer localID;

    public Transform transform;
    public AABB boundingBox;
    public EntityType type;

    public boolean lightSource;
    public float lightDistance;

    public boolean exists;
    public Object typeData;
}
