package com.myst.networking;

import com.myst.world.collisions.AABB;
import com.myst.world.entities.EntityTypes;
import com.myst.world.view.Transform;

import java.io.Serializable;

public class EntityData implements Serializable {
    public String ownerID;
    public Integer localID;
    public Transform transform;
    public AABB boundingBox;
    public EntityTypes type;
    public long time;
    public boolean lightSource;
    public float lightDistance;

    public EntityData() {

    }

    public EntityData(String ownerID, Integer localID, Transform transform, AABB boundingBox, EntityTypes type, long time, boolean lightSource, float lightDistance) {
        this.ownerID = ownerID;
        this.localID = localID;
        this.transform = transform;
        this.boundingBox = boundingBox;
        this.type = type;
        this.time = time;
        this.lightSource = lightSource;
        this.lightDistance = lightDistance;
    }
}
