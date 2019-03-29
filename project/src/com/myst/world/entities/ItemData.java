package com.myst.world.entities;

import java.io.Serializable;

/**
 * A given items data
 */
public class ItemData implements Serializable {
    public boolean hidden;
    public long lastSpikeDamage;
    public long spikeTimer;
    public long healingTimer;
    public boolean isHealingLightTrap;
    public boolean isChanged;
}
