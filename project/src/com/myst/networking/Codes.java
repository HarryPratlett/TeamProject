package com.myst.networking;

import java.io.Serializable;

public enum Codes implements Serializable {
    SET_CLIENT_ID,
    UPDATE_SERVER,
    ENTITY_UPDATE,
    NO_AVAILABLE_SPACES,
    ERROR,
    SUCCESS,
    ID_UNAVAILABLE;
}
