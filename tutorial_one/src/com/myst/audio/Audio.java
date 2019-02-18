package com.myst.audio;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Audio {

    private static Audio audio = new Audio();
    public static Audio getAudio() {
        return audio;
    }
    private Audio() { }

    int mapLengthX = 100;
    int mapLengthY = 100;
    double distance = getPythagoras(mapLengthX, mapLengthY);

    double distGunshot = 0.7*distance;
    double distManshot = 0.4*distance;
    double distFootsteps = 0.2*distance;

    String path = "assets/sounds/";

    File theme = new File (path + "astronaut.wav");
    File gunshot = new File (path + "gunshot.wav");
    File manshot = new File (path + "manshot.wav");
    File footsteps = new File (path + "footsteps.wav");

    AudioInputStream themeStream;
    Clip themeClip;

    // TODO - player/client: mute, volume level, location

    public static double getPythagoras(int x, int y) {
        double res = Math.sqrt(x ^ 2 + y ^ 2);
        return res;
    }

    // Object Sender
    public void mute() {
        if (themeClip.isRunning())
            themeClip.stop();
    }

    // Object Sender
    public void volume(int change) throws IOException, UnsupportedAudioFileException, LineUnavailableException, InterruptedException {

        /*
        FloatControl gainControl = (FloatControl) themeClip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(0.0f);
        */
    }

    public void theme() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        themeStream = AudioSystem.getAudioInputStream(theme);
        themeClip = AudioSystem.getClip();
        themeClip.open(themeStream);
        themeClip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    // Object Sender
    public void play(String clipName) throws Exception{
        File file = new File(path + clipName);
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
        Clip clip = AudioSystem.getClip();
        clip.open(audioStream);
        clip.start();
    }

    public Object[][] getAffected(Object Sender) {
        Object[][] affected = new Object[3][2];
        double dist;
        for (int i = 0; i<4; i++) {
            // TODO - determine which players are affected by sound source
            // TODO - calculate volume levels for each
        }
        return affected;
    }

    public void play(Object Sender) {
        if (themeClip.isRunning()) {
            Object[][] affected = getAffected(Sender);
            // TODO - play for right clients with right volume
        } else {
            // do nothing
        }
    }

    /*
    // testing - delete
    public static void main(String[] args) throws Exception {
        Audio sound = new Audio();
        // TODO - could make new sound object for each player (sound_[player_name])?
        sound.theme();
        while (true) {
            TimeUnit.SECONDS.sleep(5);
            break;
        }
    }
    */
}