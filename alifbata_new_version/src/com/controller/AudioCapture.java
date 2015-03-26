/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.controller;

/**
 *
 * @author Fauziah Ifa Hasan
 */

import java.util.Arrays;
import javax.sound.sampled.*;

/**
 * Represents the method to capture audio from LINE IN.
 *
 * @author Naseh
 */
public final class AudioCapture implements Runnable {
    private final int CHANNELS = 2;
    private final int RATE = 44100;
    private final int SAMPLE_BITS = 16;
    private final String INPUT_LINE = Port.Info.LINE_IN.getName();

    private AudioFormat audioFormat;
    private byte[] byteMic;
    private DataLine.Info info;
    private Port.Info portInfo;
    private TargetDataLine targetDataLine;
    private Mixer targetMixer;
    private Thread thread;

    public AudioCapture() {
        initialize();
    }
    /**
     * Starts capture audio from microphone.
     *
     * @return Audio capture instance.
     */
    public static AudioCapture start() {
        AudioCapture instance = new AudioCapture();
        if (instance.targetMixer != null) {
            instance.thread = new Thread(instance);
            instance.thread.start();

            return instance;
        }

        return null;
    }

    /**
     * Gets captured audio data sample from microphone.
     *
     * @return Audio data sample.
     */
    public byte[] getAudioData() {
        return byteMic;
    }

    @Override
    public void run() {
        targetDataLine.start();
        int frameSizeInBytes = audioFormat.getFrameSize();
        int bufferLengthInFrames = targetDataLine.getBufferSize() / 8;
        int bufferLengthInBytes = bufferLengthInFrames * frameSizeInBytes;
        while (thread != null) {
            byteMic = new byte[bufferLengthInBytes];
            targetDataLine.read(byteMic, 0, bufferLengthInBytes);
        }
    }

    /**
     * Stops audio capture.
     */
    public void stop() {
        if (thread != null) {
            thread.interrupt();
            thread = null;
        }
    }

    private Mixer getMixerPort(Mixer.Info mixerInfo) {
        try {
            Mixer targetMixer = AudioSystem.getMixer(mixerInfo);

            targetMixer.open();

            //Check if it supports the desired format
            if (targetMixer.isLineSupported(info)) {
                //now go back and start again trying to match a mixer to a port
                for (Mixer.Info _mixerInfo : AudioSystem.getMixerInfo()) {
                    if (!_mixerInfo.getName().contains(mixerInfo.getName())) {
                        continue;
                    }
                    try (Mixer mixerPort = AudioSystem.getMixer(_mixerInfo)) {
                        mixerPort.open();

                        //now check the mixer eg. LINE_IN
                        if (mixerPort.isLineSupported(portInfo)) {
                            mixerPort.close();
                            targetMixer.close();
                            return mixerPort;
                        }
                    }
                }
            }

            targetMixer.close();
        } catch (LineUnavailableException ignore) {
        }

        return null;
    }

    private void initialize() {
        Mixer mixerPort;

        audioFormat = new AudioFormat(RATE, SAMPLE_BITS, CHANNELS, true, false);
        info = new DataLine.Info(TargetDataLine.class, audioFormat);
        portInfo = new Port.Info(Port.class, INPUT_LINE, true);

        for (Mixer.Info mixerInfo : AudioSystem.getMixerInfo()) {
            mixerPort = getMixerPort(mixerInfo);
            System.out.println("mixer : " +  mixerInfo);
            if (mixerPort != null) {
                targetMixer = AudioSystem.getMixer(mixerInfo);
                break;
            }
        }

        if (targetMixer == null || !targetMixer.isLineSupported(info)) {
            stop();
            return;
        }

        try {
            targetDataLine = (TargetDataLine)targetMixer.getLine(info);
            targetMixer.open();
            targetDataLine.open(audioFormat, targetDataLine.getBufferSize());
        } catch (LineUnavailableException ignored) {
        } catch (SecurityException exc) {
            stop();
            exc.printStackTrace();
        }
    }
    public static void main (String [] args){
        AudioCapture ac= new AudioCapture();
        AudioCapture.start();
        ac.run();
        System.out.println(Arrays.toString(ac.getAudioData()));
    }
}
