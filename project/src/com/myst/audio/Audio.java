package com.myst.audio;
import com.myst.input.Input;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import javax.sound.sampled.*;
import javax.xml.stream.Location;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class Audio {

    Vector2f playerLocation;
    Vector2f soundLocation;

    public static final String THEME = "theme";
    public static final String GUN = "gun";
    public static final String HIT = "hit";
    public static final String FOOTSTEPS = "footsteps";

    private final String WAV = ".wav";
    private final String PATH = "assets/sounds/";

    public static final int MAP_LENGTH = 100;
    public static final double MAP_WIDTH = 100;
    private final double GUN_DIST = 70;
    private final double HIT_DIST = 35;
    private final double FOOTSTEPS_DIST = 10;
    private double distance;

    public static final int MIN_VOLUME = 0;
    public static final int MAX_VOLUME = 5;
    boolean muted = false;
    int volume = 3;

    private Input input;

    private File theme = new File(PATH + THEME + WAV);
    private File gun = new File(PATH + GUN + WAV);
    private File hit = new File(PATH + HIT + WAV);
    private File footsteps = new File(PATH + FOOTSTEPS + WAV);

    private AudioInputStream themeStream;
    private AudioInputStream gunStream;
    private AudioInputStream hitStream;
    private AudioInputStream footstepsStream;

    private Clip themeClip;
    private Clip gunClip;
    private Clip hitClip;
    private Clip footstepsClip;

    private FloatControl themeGainControl;
    private FloatControl gunGainControl;
    private FloatControl hitGainControl;
    private FloatControl footstepsGainControl;

    private float themeRange;
    private float gunRange;
    private float hitRange;
    private float footstepsRange;

    private float gain;

    private static Audio audio = new Audio();

    public static Audio getAudio() {
        return audio;
    }

    /**
     * creating streams, clips, gain, calculating volume range
     */
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

        themeGainControl = (FloatControl) themeClip.getControl(FloatControl.Type.MASTER_GAIN);
        gunGainControl = (FloatControl) gunClip.getControl(FloatControl.Type.MASTER_GAIN);
        hitGainControl = (FloatControl) hitClip.getControl(FloatControl.Type.MASTER_GAIN);
        footstepsGainControl = (FloatControl) footstepsClip.getControl(FloatControl.Type.MASTER_GAIN);

        themeRange = themeGainControl.getMaximum() - themeGainControl.getMinimum();
        gunRange = gunGainControl.getMaximum() - gunGainControl.getMinimum();
        hitRange = hitGainControl.getMaximum() - hitGainControl.getMinimum();
        footstepsRange = footstepsGainControl.getMaximum() - footstepsGainControl.getMinimum();

        volume(0);
        theme();
    }

    /**
     * initialising input
     * @param input - input
     */
    public void initInput(Input input) {
        this.input = input;
    }

    /**
     * calculating the distance between the player and sound source
     * @param playerLocation - location of the player
     * @param soundLocation - location of the sound source
     * @return
     */
    public static double calculateDistance(Vector2f playerLocation, Vector2f soundLocation) {
        double x = playerLocation.x - soundLocation.x;
        double y = playerLocation.y - soundLocation.y;
        double res = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
        return res;
    }

    /**
     * muting the client
     */
    public void mute() {
        muted = !muted;
        if (muted) {
            themeClip.stop();
            gunClip.stop();
            hitClip.stop();
            footstepsClip.stop();
        } else {
            themeClip.start();
        }
    }

    /**
     * changing the volume
     * @param change how much the volume is increased / decreased
     */
    public void volume(int change) {
        if (change < MIN_VOLUME) {
            if ((volume + change) < MIN_VOLUME) {
                volume = MIN_VOLUME;
            } else {
                volume = volume + change;
            }
        } else {
            if ((volume + change) > MAX_VOLUME) {
                volume = MAX_VOLUME;
            } else {
                volume = volume + change;
            }
        }

        gain = (themeRange / MAX_VOLUME * volume) + themeGainControl.getMinimum();
        themeGainControl.setValue(gain);

        gain = (gunRange / MAX_VOLUME * volume) + gunGainControl.getMinimum();
        gunGainControl.setValue(gain);

        gain = (hitRange / MAX_VOLUME * volume) + hitGainControl.getMinimum();
        hitGainControl.setValue(gain);

        gain = (footstepsRange / MAX_VOLUME * volume) + footstepsGainControl.getMinimum();
        footstepsGainControl.setValue(gain);
    }

    /**
     * playing the theme song
     */
    public void theme() {
        themeClip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    /**
     * playing a clip
     * @param clipName - name of the clip
     */
    public void play(String clipName) { //, Vector2f playerLocation, Vector2f soundLocation) {
        if (!muted) {
            switch (clipName) {
                case GUN:
                    //if (calculateDistance(playerLocation, soundLocation) < GUN_DIST) {}
                    if (gunClip.getFramePosition() >= gunClip.getFrameLength())
                        gunClip.setFramePosition(0);
                    gunClip.loop(0);
                    break;
                case HIT:
                    //if (calculateDistance(playerLocation, soundLocation) < HIT_DIST) {}
                    hitClip.loop(0);
                    break;
                case FOOTSTEPS:
                    // TODO - check location, get volume
                    if (footstepsClip.getFramePosition() >= footstepsClip.getFrameLength())
                        footstepsClip.setFramePosition(0);
                    footstepsClip.loop(0);
                    break;
                default:
                    //none
            }
        }
    }

    /**
     * calculating the volume depending on sound source location
     * @param clipName - the name of the clip
     * @param soundLocation - the location of sound source
     * @return
     */
    public int calculateVolume(String clipName, Vector2f soundLocation) {
        int result = volume;
        // TODO - calculate how far it is (0-1) * how high the volume is (0-5)
        return result;
    }

    /**
     * stopping a track
     * @param clipName - the name of the clip
     */
    public void stop(String clipName) {
        switch (clipName) {
            case THEME:
                themeClip.stop();
                break;
            case GUN:
                gunClip.stop();
                break;
            case HIT:
                hitClip.stop();
                break;
            case FOOTSTEPS:
                footstepsClip.stop();
                break;
            default:
                //none
        }
    }

    /**
     * checking if the key M is pressed
     * if true, calls mute
     */
    public void update() {
        if(input.isKeyPressed(GLFW.GLFW_KEY_M)){
            mute();
        }
    }
}