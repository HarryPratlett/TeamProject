package com.myst.networking;

import java.io.Serializable;

/**
 * Class to send/display messages
 */
public class Message implements Serializable {
    public Codes header;
    public Object data;
    public Message(Codes header, Object data){
        this.header = header;
        this.data = data;
    }
}
