package com.myst.audio;
import com.myst.world.entities.Player;
import org.joml.Vector2f;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Audio {

    public static final String THEME = "theme";
    public static final String GUN = "gun";
    public static final String HIT = "hit";
    public static final String FOOTSTEPS = "footsteps";

    public static final String WAV = ".wav";
    public static final String PATH = "assets/sounds/";

    public static final int MIN_VOLUME = 0;
    public static final int MAX_VOLUME = 5;

    File theme = new File (PATH + THEME + WAV);
    File gun = new File (PATH + GUN + WAV);
    File hit = new File (PATH + HIT + WAV);
    File footsteps = new File (PATH + FOOTSTEPS + WAV);

    AudioInputStream themeStream;
    AudioInputStream gunStream;
    AudioInputStream hitStream;
    AudioInputStream footstepsStream;

    boolean muted = false;

    //AudioFormat format = audioStream.getFormat();
    //DataLine.Info info = new DataLine.Info(Clip.class, format);

    Clip themeClip;
    Clip gunClip;
    Clip hitClip;
    Clip footstepsClip;

    private static Audio audio = new Audio();

    public static Audio getAudio() {
        return audio;
    }

    private Audio() {
        try {
            themeStream = AudioSystem.getAudioInputStream(theme);
            themeClip = AudioSystem.getClip();
            themeClip.open(themeStream);

            gunStream = AudioSystem.getAudioInputStream(gun);
            gunClip = AudioSystem.getClip();
            gunClip.open(gunStream);

            hitStream = AudioSystem.getAudioInputStream(hit);
            hitClip = AudioSystem.getClip();
            hitClip.open(hitStream);

            footstepsStream = AudioSystem.getAudioInputStream(footsteps);
            footstepsClip = AudioSystem.getClip();
            footstepsClip.open(footstepsStream);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
    }

    public static double getPythagoras(int x, int y) {
        double res = Math.sqrt(x ^ 2 + y ^ 2);
        return res;
    }

    // Object Sender
    public void mute() {
        muted = !muted;

        if(muted) {
            themeClip.stop();
            gunClip.stop();
            hitClip.stop();
            footstepsClip.stop();
        }
        else {
            themeClip.start();
        }

    }

    // Object Sender
    public void volume(int change) {
        // TODO - something has to continuously check the volume
        // TODO - OR change it straight away
        if(change < MIN_VOLUME) {
            if (Player.getVolume() <= MIN_VOLUME) {
                // nothing
            } else {
                Player.setVolume(change);
            }
        } else {
            if (Player.getVolume() >= MAX_VOLUME) {
                // nothing
            } else {
                Player.setVolume(change);
            }
        }
        /*
        // only sets at the beginning
        FloatControl gainControl = (FloatControl) themeClip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(0.0f);
        */
    }

    public void theme() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        themeClip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    // later with ", Vector2f location"
    public void play(String clipName) {
        if (!muted) {
            switch(clipName) {
                case GUN:
                    // TODO - check location, get volume
                    gunClip.loop(0);
                    gunClip.start();
                    break;
                case HIT:
                    // TODO - check location, get volume
                    hitClip.loop(0);
                    hitClip.start();
                    break;
                case FOOTSTEPS:
                    // TODO - check location, get volume
                    footstepsClip.loop(0);
                    footstepsClip.start();
                    break;
                default:
                    //none
            }
        } else {
            // muted, do nothing
        }
    }

    public int getVolume(String clipName, Vector2f soundLocation) {
        int volume = Player.getVolume();
        // TODO - calculate how far it is (0-1) * how high the volume is (0-5)
        return volume;
    }
}