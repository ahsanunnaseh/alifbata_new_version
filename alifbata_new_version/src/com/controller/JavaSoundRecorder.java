
package com.controller;

/**
 *
 * @author Fauziah Ifa Hasan
 */
import javax.sound.sampled.*;
import java.io.*;

public class JavaSoundRecorder {
    // record duration, in milliseconds
   
    File wavFile;
 
    // format of audio file
    AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
    TargetDataLine line;
    
    public JavaSoundRecorder(String prefix,String namafile){
        wavFile = new File("audio/"+prefix+"_"+namafile+".wav");
    }
 
    /**
     * Defines an audio format
     */
    AudioFormat getAudioFormat() {
        float sampleRate = 44100;
        int sampleSizeInBits = 16;
        int channels = 2;
        boolean signed = true;
        boolean bigEndian = true;
        AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits,
                                             channels, signed, bigEndian);
        return format;
    }
 
    /**
     * Captures the sound and record into a WAV file
     */
   public void start() {
        try {
            AudioFormat format = getAudioFormat();
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
 
            // checks if system supports the data line
            if (!AudioSystem.isLineSupported(info)) {
                System.out.println("Line not supported");
                System.exit(0);
            }
            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();   // start capturing
 
            System.out.println("Start capturing...");
 
            AudioInputStream ais = new AudioInputStream(line);
 
            System.out.println("Start recording...");
 
            // start recording
            AudioSystem.write(ais, fileType, wavFile);
 
        } catch (LineUnavailableException | IOException ex) {
        }
    }
 
    /**
     * Closes the target data line to finish capturing and recording
     */
   public void finish() {
        line.stop();
        line.close();
        System.out.println("Finished");
    }
 

}