package com.myst.networking;

import com.myst.world.collisions.AABB;
import com.myst.world.entities.EntityType;
import com.myst.world.view.Transform;

import java.io.Serializable;

public class EntityData implements Serializable {
    public String ownerID;
    public Integer localID;

    public Transform transform;
    public AABB boundingBox;
    public EntityType type;
    public long time;

    public boolean lightSource;
    public float lightDistance;

    public float health;
    public float maxHealth;

    public boolean hidden;
    public boolean exists;
    public long lastSpikeDamage;
    public long spikeTimer;

    public EntityData() {

    }
}
