package com.myst.networking.serverside;

import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.Serializable;

public class PlayAudioData implements Serializable {
   public String clipName;
   public Vector3f location;

   public PlayAudioData(String clipName, Vector3f location) {
       this.clipName = clipName;
       this.location = location;
   }
}