/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import javax.sound.sampled.SourceDataLine;

/**
 *
 * @author Fauziah Ifa Hasan
 */
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Audio_Reader {

    private final int BUFFER_SIZE = 128000;
    private File soundFile;
    private AudioInputStream audioStream;
    private AudioFormat audioFormat;
    private SourceDataLine sourceLine;
    private final String filename;

    /**
     * @param filename the name of the file that is going to be played
     */
    public Audio_Reader(String filename){
        this.filename=filename;
    }
    public byte [] getAudioBytes() {
        try {
            soundFile = new File(filename);
        } catch (Exception e) {
            System.exit(1);
        }

        try {
            audioStream = AudioSystem.getAudioInputStream(soundFile);
        } catch (UnsupportedAudioFileException | IOException e) {
            System.exit(1);
        }

        audioFormat = audioStream.getFormat();

        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        try {
            sourceLine = (SourceDataLine) AudioSystem.getLine(info);
            sourceLine.open(audioFormat);
        } catch (LineUnavailableException e) {
            System.exit(1);
        }

        sourceLine.start();
        int nBytesRead = 0;
        byte[] abData = new byte[BUFFER_SIZE];
        while (nBytesRead != -1) {
            try {
                nBytesRead = audioStream.read(abData, 0, abData.length);
            } catch (IOException e) {
            }
            if (nBytesRead >= 0) {
                int nBytesWritten = sourceLine.write(abData, 0, nBytesRead);
            }
        }
        sourceLine.drain();
        sourceLine.close();
        return abData;
    }
    public static void main (String [] args){
        String filename="E:\\HARUS SELESAI\\SKRIPSI RASUNA FIX\\File Wav\\cicada.wav";
        Audio_Reader reader=new Audio_Reader(filename);
        byte [] data=reader.getAudioBytes();
        System.out.println(Arrays.toString(data));
        System.out.println("panjangnya adalah : " +  data.length);
    }
 
}
