package org.psnbtech;

import java.io.File;
import java.nio.file.Paths;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

public class Mediaplay {
    public void play_music(String fileName) {
        File music;
        AudioInputStream stream;
        AudioFormat format;
        DataLine.Info info;

        music = new File(new File("").getAbsolutePath()+File.separator+"Client"+ File.separator+ "resources" + File.separator+"Musics"+ File.separator+fileName);

        Clip clip;

        try {
                stream = AudioSystem.getAudioInputStream(music);
                format = stream.getFormat();
                info = new DataLine.Info(Clip.class, format);
                clip = (Clip)AudioSystem.getLine(info);
                clip.open(stream);
                clip.start();    
        } catch (Exception e) {
                System.out.println("err : " + e);
                }       
        }
}