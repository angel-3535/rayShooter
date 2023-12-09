package util.io;

import gfx.Window;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.sound.sampled.*;

public class Sound {

    public static final Sound M_HELL_ON_EARTH = new Sound("/assets/sounds/music/01. Hell On Earth.wav");
    public static final Sound S_STEP_GRASS = new Sound("/assets/sounds/sfx/sfx_step_grass_r.wav");


    public static final Clip[] musicClip = new Clip[1];
    public final Set<Clip> clips = Collections.synchronizedSet(new HashSet<>());

    private final AudioFormat format;
    private final byte[] bytes;
    private float volumeF;


    public Sound(String path) {
        volumeF =  1f;
        try {
            System.out.println("loading sound: "+ path);
            AudioInputStream stream = AudioSystem.getAudioInputStream(
                    new BufferedInputStream(Window.class.getResourceAsStream(path)));

            this.format = stream.getFormat();
            this.bytes = stream.readAllBytes();

            for (int i = 0; i < 4; i++) {
                this.createNewClip();
            }
        } catch (IOException | UnsupportedAudioFileException e) {
            throw new Error(e);
        }
    }

    private Clip createNewClip() {
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(this.format, this.bytes, 0, this.bytes.length);
            clips.add(clip);
            return clip;
        } catch (LineUnavailableException e) {
            throw new Error(e);
        }
    }

    public void play() {
        new Thread(() -> {
            Clip clip = clips.stream()
                    .filter(c ->
                            c.getFramePosition() == 0 ||
                                    c.getFramePosition() == c.getFrameLength())
                    .findFirst()
                    .orElseGet(this::createNewClip);

            clip.setFramePosition(0);
            clip.start();
        }).start();
    }


    public static void playMusic(Clip s){

        if(musicClip[0] != null ){
            musicClip[0].stop();
        }
        musicClip[0] = s;
        musicClip[0].setFramePosition(0);
        musicClip[0].loop(Clip.LOOP_CONTINUOUSLY);
    }
    public static void setVolume(float f, Sound sound){

        for (Clip c: sound.clips){

            FloatControl volume = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
            volume.setValue(f);
        }



    }

    public Clip getClip(){
        return clips.stream()
                .filter(c ->c.getFramePosition() == 0 || c.getFramePosition() == c.getFrameLength())
                .findFirst()
                .orElseGet(this::createNewClip);

    }

}