package com.myst.networking.serverside;

import org.joml.Vector3f;

import java.io.Serializable;

/**
 * Plays a audio in a location
 */
public class PlayAudioData implements Serializable {
   public String clipName;
   public Vector3f location;

   public PlayAudioData(String clipName, Vector3f location) {
       this.clipName = clipName;
       this.location = location;
   }
}