package com.myst.world.entities;

import java.io.Serializable;

public class PlayerData implements Serializable {
    public float health;
    public float maxHealth;
    public int bulletCount;
    public int maxBulletCount;
    public long lastSpikeDamage;
    public long lastHealOnLightTrap;
    public long lastInvincibilityPickup;
    public boolean isInvincible;
    public long lastInfiniteBulletsPickup;
    public boolean hasInfiniteBullets;
}
