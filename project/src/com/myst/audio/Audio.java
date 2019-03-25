package com.myst.audio;
import com.myst.input.Input;
import com.myst.world.entities.Player;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Audio {

    Vector3f playerLocation;

    public static final String THEME = "theme";
    public static final String GUN = "gun";
    public static final String HIT_BY_BULLET = "hit_by_bullet";
    public static final String HIT_BY_SPIKES = "hit_by_spikes";
    public static final String FOOTSTEPS = "footsteps";
    public static final String APPLE = "apple";
    public static final String SPIKES = "spikes";
    public static final String POTION = "potion";
    public static final String MONSTER_HIT = "monster";
    public static final String BULLETS_SMALL = "bullets_small";
    public static final String BULLETS_BIG = "bullets_big";
    // TODO - 2 more
    public static final String MED_KIT = "med_kit";
    public static final String HEAL_PLATFORM = "healing_platform";

    private final String WAV = ".wav";
    private final String PATH = "assets/sounds/";

    private final double GUN_DIST = 70;
    private final double SPIKES_DIST = 30;
    private final double HIT_DIST = 35;
    private final double NEARBY_DIST = 10;

    public static final int MIN_VOLUME = 0;
    public static final int MAX_VOLUME = 5;
    boolean muted = false;
    int volume = 3;

    private Input input;

    private File theme = new File(PATH + THEME + WAV);
    private File gun = new File(PATH + GUN + WAV);
    private File hitByBullet = new File(PATH + HIT_BY_BULLET + WAV);
    private File hitBySpikes = new File(PATH + HIT_BY_SPIKES + WAV);
    private File footsteps = new File(PATH + FOOTSTEPS + WAV);
    private File apple = new File(PATH + APPLE + WAV);
    private File spikes = new File(PATH + SPIKES + WAV);
    private File potion = new File(PATH + POTION + WAV);
    private File monsterHit = new File(PATH + MONSTER_HIT + WAV);
    private File bulletsSmall = new File(PATH + BULLETS_SMALL + WAV);
    private File bulletsBig = new File(PATH + BULLETS_BIG + WAV);

    private AudioInputStream themeStream;
    private AudioInputStream gunStream;
    private AudioInputStream hitByBulletStream;
    private AudioInputStream hitBySpikesStream;
    private AudioInputStream footstepsStream;
    private AudioInputStream appleStream;
    private AudioInputStream spikesStream;
    private AudioInputStream potionStream;
    private AudioInputStream monsterHitStream;
    private AudioInputStream bulletsSmallStream;
    private AudioInputStream bulletsBigStream;

    private Clip themeClip;
    private Clip gunClip;
    private Clip hitByBulletClip;
    private Clip hitBySpikesClip;
    private Clip footstepsClip;
    private Clip appleClip;
    private Clip spikesClip;
    private Clip potionClip;
    private Clip monsterHitClip;
    private Clip bulletsSmallClip;
    private Clip bulletsBigClip;

    private FloatControl themeGainControl;
    private FloatControl gunGainControl;
    private FloatControl hitByBulletGainControl;
    private FloatControl hitBySpikesGainControl;
    private FloatControl footstepsGainControl;
    private FloatControl appleGainControl;
    private FloatControl spikesGainControl;
    private FloatControl potionGainControl;
    private FloatControl monsterHitGainControl;
    private FloatControl bulletsSmallGainControl;
    private FloatControl bulletsBigGainControl;

    private float themeRange;
    private float gunRange;
    private float hitByBulletRange;
    private float hitBySpikesRange;
    private float footstepsRange;
    private float appleRange;
    private float spikesRange;
    private float potionRange;
    private float monsterHitRange;
    private float bulletsSmallRange;
    private float bulletsBigRange;

    private float gain;

    private static Audio audio = new Audio();

    public static Audio getAudio() {
        return audio;
    }

    /**
     * creating streams, clips, gain, calculating modVolume range
     */
    private Audio() {
        playerLocation = new Vector3f();

        try {
            themeStream = AudioSystem.getAudioInputStream(theme);
            themeClip = AudioSystem.getClip();
            themeClip.open(themeStream);

            gunStream = AudioSystem.getAudioInputStream(gun);
            gunClip = AudioSystem.getClip();
            gunClip.open(gunStream);

            hitByBulletStream = AudioSystem.getAudioInputStream(hitByBullet);
            hitByBulletClip = AudioSystem.getClip();
            hitByBulletClip.open(hitByBulletStream);

            hitBySpikesStream = AudioSystem.getAudioInputStream(hitBySpikes);
            hitBySpikesClip = AudioSystem.getClip();
            hitBySpikesClip.open(hitBySpikesStream);

            footstepsStream = AudioSystem.getAudioInputStream(footsteps);
            footstepsClip = AudioSystem.getClip();
            footstepsClip.open(footstepsStream);

            appleStream = AudioSystem.getAudioInputStream(apple);
            appleClip = AudioSystem.getClip();
            appleClip.open(appleStream);

            spikesStream = AudioSystem.getAudioInputStream(spikes);
            spikesClip = AudioSystem.getClip();
            spikesClip.open(spikesStream);

            potionStream = AudioSystem.getAudioInputStream(potion);
            potionClip = AudioSystem.getClip();
            potionClip.open(potionStream);

            monsterHitStream = AudioSystem.getAudioInputStream(monsterHit);
            monsterHitClip = AudioSystem.getClip();
            monsterHitClip.open(monsterHitStream);

            bulletsSmallStream = AudioSystem.getAudioInputStream(bulletsSmall);
            bulletsSmallClip = AudioSystem.getClip();
            bulletsSmallClip.open(bulletsSmallStream);

            bulletsBigStream = AudioSystem.getAudioInputStream(bulletsBig);
            bulletsBigClip = AudioSystem.getClip();
            bulletsBigClip.open(bulletsBigStream);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        }

        themeGainControl = (FloatControl) themeClip.getControl(FloatControl.Type.MASTER_GAIN);
        gunGainControl = (FloatControl) gunClip.getControl(FloatControl.Type.MASTER_GAIN);
        hitByBulletGainControl = (FloatControl) hitByBulletClip.getControl(FloatControl.Type.MASTER_GAIN);
        hitBySpikesGainControl = (FloatControl) hitBySpikesClip.getControl(FloatControl.Type.MASTER_GAIN);
        footstepsGainControl = (FloatControl) footstepsClip.getControl(FloatControl.Type.MASTER_GAIN);
        appleGainControl = (FloatControl) appleClip.getControl(FloatControl.Type.MASTER_GAIN);
        spikesGainControl = (FloatControl) spikesClip.getControl(FloatControl.Type.MASTER_GAIN);
        potionGainControl = (FloatControl) potionClip.getControl(FloatControl.Type.MASTER_GAIN);
        monsterHitGainControl = (FloatControl) monsterHitClip.getControl(FloatControl.Type.MASTER_GAIN);
        bulletsSmallGainControl = (FloatControl) bulletsSmallClip.getControl(FloatControl.Type.MASTER_GAIN);
        bulletsBigGainControl = (FloatControl) bulletsBigClip.getControl(FloatControl.Type.MASTER_GAIN);

        themeRange = themeGainControl.getMaximum() - themeGainControl.getMinimum();
        gunRange = gunGainControl.getMaximum() - gunGainControl.getMinimum();
        hitByBulletRange = hitByBulletGainControl.getMaximum() - hitByBulletGainControl.getMinimum();
        hitBySpikesRange = hitBySpikesGainControl.getMaximum() - hitBySpikesGainControl.getMinimum();
        footstepsRange = footstepsGainControl.getMaximum() - footstepsGainControl.getMinimum();
        appleRange = appleGainControl.getMaximum() - appleGainControl.getMinimum();
        spikesRange = spikesGainControl.getMaximum() - spikesGainControl.getMinimum();
        potionRange = potionGainControl.getMaximum() - potionGainControl.getMinimum();
        monsterHitRange = monsterHitGainControl.getMaximum() - monsterHitGainControl.getMinimum();
        bulletsSmallRange = bulletsSmallGainControl.getMaximum() - bulletsSmallGainControl.getMinimum();
        bulletsBigRange = bulletsBigGainControl.getMaximum() - bulletsBigGainControl.getMinimum();

        modVolume(0);
//        theme();
    }

    /**
     * initialising input
     * @param input - input
     */
    public void initInput(Input input) {
        this.input = input;
    }

    public void initAudioWithPlayer(Player player) {
        this.playerLocation = player.transform.pos;
    }

    /**
     * calculating the distance between the player and sound source
     * @param soundLocation - location of the sound source
     * @return
     */
    public double calculateDistanceToPlayer(Vector3f soundLocation) {
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
            hitByBulletClip.stop();
            hitBySpikesClip.stop();
            footstepsClip.stop();
            appleClip.stop();
            spikesClip.stop();
            potionClip.stop();
            monsterHitClip.stop();
            bulletsSmallClip.stop();
            bulletsBigClip.stop();
        } else {
            themeClip.start();
        }
    }

    /**
     * changing the modVolume
     * @param change how much the modVolume is increased / decreased
     */
    public void modVolume(int change) {
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

        gain = (hitByBulletRange / MAX_VOLUME * volume) + hitByBulletGainControl.getMinimum();
        hitByBulletGainControl.setValue(gain);

        gain = (hitBySpikesRange / MAX_VOLUME * volume) + hitBySpikesGainControl.getMinimum();

        gain = (footstepsRange / MAX_VOLUME * volume) + footstepsGainControl.getMinimum();
        footstepsGainControl.setValue(gain);

        gain = (appleRange / MAX_VOLUME * volume) + appleGainControl.getMinimum();
        appleGainControl.setValue(gain);

        gain = (spikesRange / MAX_VOLUME * volume) + spikesGainControl.getMinimum();
        spikesGainControl.setValue(gain);

        gain = (potionRange / MAX_VOLUME * volume) + potionGainControl.getMinimum();
        potionGainControl.setValue(gain);

        gain = (monsterHitRange / MAX_VOLUME * volume) + monsterHitGainControl.getMinimum();
        monsterHitGainControl.setValue(gain);

        gain = (bulletsSmallRange / MAX_VOLUME * volume) + bulletsSmallGainControl.getMinimum();
        bulletsSmallGainControl.setValue(gain);

        gain = (bulletsBigRange / MAX_VOLUME * volume) + bulletsBigGainControl.getMinimum();
        bulletsBigGainControl.setValue(gain);
    }

    public void setControlVolume(FloatControl control, double volumeMod) {
        float range = control.getMaximum() - control.getMinimum();
        double gain = (range / MAX_VOLUME * (volume * volumeMod)) + control.getMinimum();
        control.setValue((float) gain);
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
    public void play(String clipName, Vector3f location) { //, Vector2f playerLocation, Vector2f soundLocation) {
        double dist = calculateDistanceToPlayer(location);

        if(clipName == APPLE) System.out.println(muted);
        if (!muted) {
            switch (clipName) {
                case GUN:
                    if (dist > GUN_DIST) return;
                    setControlVolume(gunGainControl, 1 - dist / GUN_DIST);
                    if (gunClip.getFramePosition() >= gunClip.getFrameLength())
                        gunClip.setFramePosition(0);
                    gunClip.loop(0);
                    break;
                case HIT_BY_BULLET:
                    if(dist > HIT_DIST) return;
                    setControlVolume(hitByBulletGainControl, 1 - dist / NEARBY_DIST);
                    if (hitByBulletClip.getFramePosition() >= hitByBulletClip.getFrameLength())
                        hitByBulletClip.setFramePosition(0);
                    hitByBulletClip.loop(0);
                    break;
                case HIT_BY_SPIKES:
                    if(dist > HIT_DIST) return;
                    setControlVolume(hitBySpikesGainControl, 1 - dist / HIT_DIST);
                    if (hitBySpikesClip.getFramePosition() >= hitBySpikesClip.getFrameLength())
                        hitBySpikesClip.setFramePosition(0);
                    hitBySpikesClip.loop(0);
                    break;
                case FOOTSTEPS:
                    if(dist > NEARBY_DIST) return;
                    setControlVolume(footstepsGainControl, 1 - dist / NEARBY_DIST);
                    if (footstepsClip.getFramePosition() >= footstepsClip.getFrameLength()) {
                        footstepsClip.setFramePosition(0);
                    }
                    footstepsClip.loop(0);
                    break;
                case APPLE:
                    if(dist > NEARBY_DIST) return;
                    setControlVolume(appleGainControl, 1 - dist / NEARBY_DIST);
                    if (appleClip.getFramePosition() >= appleClip.getFrameLength())
                        appleClip.setFramePosition(0);
                    appleClip.loop(0);
                    break;
                case SPIKES:
                    if(dist > SPIKES_DIST) return;
                    setControlVolume(spikesGainControl, 1 - dist / HIT_DIST);
                    if (spikesClip.getFramePosition() >= spikesClip.getFrameLength())
                        spikesClip.setFramePosition(0);
                    spikesClip.loop(0);
                    break;
                case POTION:
                    if(dist > NEARBY_DIST) return;
                    setControlVolume(potionGainControl, 1 - dist / NEARBY_DIST);
                    if (potionClip.getFramePosition() >= potionClip.getFrameLength())
                        potionClip.setFramePosition(0);
                    potionClip.loop(0);
                    break;
                case MONSTER_HIT:
                    if(dist > HIT_DIST) return;
                    setControlVolume(monsterHitGainControl, 1 - dist / HIT_DIST);
                    if (monsterHitClip.getFramePosition() >= monsterHitClip.getFrameLength())
                        monsterHitClip.setFramePosition(0);
                    monsterHitClip.loop(0);
                    break;
                case BULLETS_SMALL:
                    if(dist > NEARBY_DIST) return;
                    setControlVolume(bulletsSmallGainControl, 1 - dist / NEARBY_DIST);
                    if (bulletsSmallClip.getFramePosition() >= bulletsSmallClip.getFrameLength())
                        bulletsSmallClip.setFramePosition(0);
                    bulletsSmallClip.loop(0);
                    break;
                case BULLETS_BIG:
                    if(dist > NEARBY_DIST) return;
                    setControlVolume(bulletsBigGainControl, 1 - dist / NEARBY_DIST);
                    if (bulletsBigClip.getFramePosition() >= bulletsBigClip.getFrameLength())
                        bulletsBigClip.setFramePosition(0);
                    bulletsBigClip.loop(0);
                    break;
                default:
                    //none
            }
        }
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
            case HIT_BY_BULLET:
                hitByBulletClip.stop();
                break;
            case HIT_BY_SPIKES:
                hitBySpikesClip.stop();
                break;
            case FOOTSTEPS:
                footstepsClip.stop();
                break;
            case APPLE:
                appleClip.stop();
                break;
            case SPIKES:
                spikesClip.stop();
                break;
            case POTION:
                potionClip.stop();
                break;
            case MONSTER_HIT:
                monsterHitClip.stop();
                break;
            case BULLETS_SMALL:
                bulletsSmallClip.stop();
                break;
            case BULLETS_BIG:
                bulletsBigClip.stop();
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