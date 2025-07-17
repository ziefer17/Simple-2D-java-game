/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.game;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioManager {
    private Clip currentClip;
    private String currentTrack;

    public AudioManager() {
        currentClip = null;
        currentTrack = null;
    }

    public void playSound(String filePath, boolean loop) {
        try {
            
            stopSound();

            
            File audioFile = new File(filePath);
            if (!audioFile.exists()) {
                System.err.println("Audio file not found: " + filePath);
                return;
            }
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            currentClip = AudioSystem.getClip();
            currentClip.open(audioStream);
            currentTrack = filePath;

            
            if (loop) {
                currentClip.loop(Clip.LOOP_CONTINUOUSLY);
            }

            
            currentClip.start();
            System.out.println("Playing audio: " + filePath);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Error playing audio: " + filePath);
            e.printStackTrace();
        }
    }

    public void stopSound() {
        if (currentClip != null && currentClip.isRunning()) {
            currentClip.stop();
            currentClip.close();
            System.out.println("Stopped audio: " + currentTrack);
        }
        currentClip = null;
        currentTrack = null;
    }

    public String getCurrentTrack() {
        return currentTrack;
    }
}
