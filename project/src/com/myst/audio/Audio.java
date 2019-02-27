package com.myst.audio;
import com.myst.input.Input;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import javax.sound.sampled.*;
import javax.xml.stream.Location;
import java.io.File;
import java.io.IOException;

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
    private final double GUN_DIST = 100;
    private final double HIT_DIST = 50;
    private final double FOOTSTEPS_DIST = 20;
    private double distance;

    public static final int MIN_VOLUME = 0;
    public static final int MAX_VOLUME = 5;
    boolean muted = false;
    int volume = 3;

    private Input input;

    File theme = new File(PATH + THEME + WAV);
    File gun = new File(PATH + GUN + WAV);
    File hit = new File(PATH + HIT + WAV);
    File footsteps = new File(PATH + FOOTSTEPS + WAV);

    AudioInputStream themeStream;
    AudioInputStream gunStream;
    AudioInputStream hitStream;
    AudioInputStream footstepsStream;

    Clip themeClip;
    Clip gunClip;
    Clip hitClip;
    Clip footstepsClip;

    FloatControl themeGainControl;
    FloatControl gunGainControl;
    FloatControl hitGainControl;
    FloatControl footstepsGainControl;

    float themeRange;
    float gunRange;
    float hitRange;
    float footstepsRange;

    float gain;

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

        themeGainControl = (FloatControl) themeClip.getControl(FloatControl.Type.MASTER_GAIN);
        gunGainControl = (FloatControl) themeClip.getControl(FloatControl.Type.MASTER_GAIN);
        hitGainControl = (FloatControl) themeClip.getControl(FloatControl.Type.MASTER_GAIN);
        footstepsGainControl = (FloatControl) themeClip.getControl(FloatControl.Type.MASTER_GAIN);

        themeRange = themeGainControl.getMaximum() - themeGainControl.getMinimum();
        gunRange = gunGainControl.getMaximum() - gunGainControl.getMinimum();
        hitRange = hitGainControl.getMaximum() - hitGainControl.getMinimum();
        footstepsRange = footstepsGainControl.getMaximum() - footstepsGainControl.getMinimum();

        volume(0);
        theme();
    }

    public void initInput(Input input) {
        this.input = input;
    }

    public static double getPythagoras(int x, int y) {
        double res = Math.sqrt(x ^ 2 + y ^ 2);
        return res;
    }

    public static double calculateDistance(Vector2f playerLocation, Vector2f soundLocation) {
        double x = playerLocation.x - soundLocation.x;
        double y = playerLocation.y - soundLocation.y;
        double res = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
        return res;
    }

    // Object Sender
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

    // Object Sender
    public void volume(int change) {
        // TODO - something has to continuously check the volume
        // TODO - OR change it straight away
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

        //System.out.println(themeRange);
        gain = (themeRange / MAX_VOLUME * volume) + themeGainControl.getMinimum();
        themeGainControl.setValue(gain);

        //System.out.println(gunRange);
        gain = (gunRange / MAX_VOLUME * volume) + gunGainControl.getMinimum();
        gunGainControl.setValue(gain);

        //System.out.println(hitRange);
        gain = (hitRange / MAX_VOLUME * volume) + hitGainControl.getMinimum();
        hitGainControl.setValue(gain);

        //System.out.println(footstepsRange);
        gain = (footstepsRange / MAX_VOLUME * volume) + footstepsGainControl.getMinimum();
        footstepsGainControl.setValue(gain);
    }

    public void theme() {
        themeClip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void play(String clipName) { //, Vector2f playerLocation, Vector2f soundLocation) {
        if (!muted) {
            switch (clipName) {
                case GUN:
                    if (calculateDistance(playerLocation, soundLocation) < GUN_DIST) {
                        gunClip.loop(0);
                    }
                    break;
                case HIT:
                    if (calculateDistance(playerLocation, soundLocation) < HIT_DIST) {
                        hitClip.loop(0);
                    }
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
        } else {
            // muted, do nothing
        }
    }

    public int getVolume(String clipName, Vector2f soundLocation) {
        int result = volume;
        // TODO - calculate how far it is (0-1) * how high the volume is (0-5)
        return result;
    }

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

    public void update() {
        if(input.isKeyPressed(GLFW.GLFW_KEY_M)){
            mute();
        }
    }
}