package com.myst.audio;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Audio {

    int mapLengthX = 100;
    int mapLengthY = 100;
    double distance = getPythagoras(mapLengthX, mapLengthY);

    double distGunshot = 0.7*distance;
    double distManshot = 0.4*distance;
    double distFootsteps = 0.2*distance;

    String path = "assets/sounds/";

    String theme = "astronaut.wav";
    String gunshot = "gunshot.wav";
    String manshot = "manshot.wav";
    String footsteps = "footsteps.wav";

    static boolean muted = false;

    // TODO - player/client: mute, volume level, location

    public static double getPythagoras(int x, int y) {
        double res = Math.sqrt(x ^ 2 + y ^ 2);
        return res;
    }

    public void mute(Object Sender) {
        muted = !muted;
        if (muted) {
            // TODO - mute main theme
        } else {
            // TODO - unmute main theme
        }
    }

    // Object Sender
    public void volume(int change) throws IOException, UnsupportedAudioFileException, LineUnavailableException, InterruptedException {
        // TODO - just plays for 5 sec as test
        File theme = new File(path + "astronaut.wav");
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(theme);
        Clip clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(0.0f);
        clip.start();
        while (true) {
            TimeUnit.SECONDS.sleep(5);
            break;
        }
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
        if (!muted) {
            Object[][] affected = getAffected(Sender);
            // TODO - play for right clients with right volume
        } else {
            // do nothing
        }
    }
}