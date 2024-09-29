package main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.net.URL;

public class Sound {

    public FloatControl volumeControl;
    Clip clip;
    URL[] soundURL = new URL[12];
    GamePanel gp;

    public Sound(GamePanel gp) {
        soundURL[0] = getClass().getResource("/sound/powerup.wav");
        soundURL[1] = getClass().getResource("/sound/hit.wav");
        soundURL[2] = getClass().getResource("/sound/receivedamage.wav");
        soundURL[3] = getClass().getResource("/sound/ping.wav");
        soundURL[4] = getClass().getResource("/sound/explosion.wav");
        soundURL[6] = getClass().getResource("/sound/main_theme.wav");
        soundURL[7] = getClass().getResource("/sound/menu_theme.wav");
        soundURL[8] = getClass().getResource("/sound/nuke.wav");
        soundURL[9] = getClass().getResource("/sound/win.wav");
        soundURL[10] = getClass().getResource("/sound/lose.wav");
        soundURL[11] = getClass().getResource("/sound/tank_moving.wav");
        this.gp = gp;
    }

    public void setFile(int i) {

        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);

            volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play() {
        volumeControl.setValue(gp.volumeValue);
        clip.start();
    }

    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        if (clip != null){
            clip.stop();
        }
    }
}
