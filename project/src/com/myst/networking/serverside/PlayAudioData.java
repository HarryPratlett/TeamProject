package com.myst.networking.serverside;

import java.io.Serializable;

public class PlayAudioData implements Serializable {
   public String clipName;

   public PlayAudioData(String clipName) {
       this.clipName = clipName;
   }
}