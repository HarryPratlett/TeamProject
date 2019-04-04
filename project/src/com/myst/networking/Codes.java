package com.myst.networking;

import java.io.Serializable;

/**
 * Possible codes for game
 */
public enum Codes implements Serializable {
    SET_CLIENT_ID,
    UPDATE_SERVER,
    ENTITY_UPDATE,
    NO_AVAILABLE_SPACES,
    ERROR,
    SUCCESS,
    ID_UNAVAILABLE,
    PLAY_AUDIO,
    GAME_STARTED,
    GAME_ENDED
}
