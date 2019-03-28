package com.myst.world.entities;

import java.io.Serializable;

public class ItemData implements Serializable {
    public boolean hidden;
    public long lastSpikeDamage;
    public long spikeTimer;
    public long healingTimer;
    public boolean isHealingLightTrap;
    public boolean isChanged;
}
