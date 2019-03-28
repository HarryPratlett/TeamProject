package com.myst.world.entities;

public enum EntityType {
    /**
     * Player
     */
    PLAYER,

    /**
     * Bullet
     */
    BULLET,

    /**
     * Item
     * On collision with player: increase player's health by 10
     */
    ITEM_APPLE,

    /**
     * Item
     * On collision with player: increase player's health by 30
     */
    ITEM_MED_KIT,

    /**
     * Item
     * On collision with player: health will not be decreased for 20 seconds
     * Timer: if not picked up within the last 20 seconds then the effect disappears
     */
    ITEM_INVINCIBILITY_POTION,

    /**
     * Item
     * On collision with player: bullet count will not be decreased for 20 seconds
     * Timer: if not picked up within the last 20 seconds then the effect disappears
     */
    ITEM_INFINITE_BULLETS_POTION,

    /**
     * Item
     * On collision with player: disappear
     * Timer: if not collided within the last 3 seconds, appear
     */
    ITEM_SPIKES_HIDDEN,

    /**
     * Trap
     * On collision with player: appear; decrease player's health by 10 per second
     * Timer 1: if not collided within the last 3 seconds, disappear
     * Timer 2: the player takes damage only once every second while colliding with the trap
     */
    ITEM_SPIKES_REVEALED,

    /**
     * Item
     * On collision with player: increase player's bullet count by 10
     */
    ITEM_BULLETS_SMALL,

    /**
     * Item
     * On collision with player: increase player's bullet count by 30
     */
    ITEM_BULLETS_BIG,

    /**
     * Trap
     * Special properties: emits light
     * On collision with player: increase player's health by 1 per second
     * Timer 1: the player is healed only once every second while colliding with the trap
     */
    ITEM_LIGHT_TRAP
}
