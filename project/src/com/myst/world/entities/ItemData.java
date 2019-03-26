package com.myst.world.entities;

import java.io.Serializable;

public class ItemData implements Serializable {
    public boolean hidden;
    public long lastSpikeDamage;
    public long spikeTimer = Long.MAX_VALUE;
    public long healingTimer;
    public boolean isChanged;
}
