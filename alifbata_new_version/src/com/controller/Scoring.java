package com.controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

public final class Scoring {
    // Hasil uji coba memakai mic di sigura gura 2.8 dam 31.3
    //33.5
    //33.90
    private final double DECIBEL_TOLERANCE = 2.8;
    private final double SILENT_TOLERANCE = 33.80;
    private final int SAMPLECOUNT = 1024;  
    private final float REFVALUE = 0.1f;
    private final int clamp = 160; 
    

    private double finalScore;
    private int pitchCorrect;
    private int pitchFault;
    private double score;
    private boolean uncounted;
    private int uncountValue;
    private Double valueMic0;
    private Double valueMic1;
    private Double valueMusic0;
    private Double valueMusic1;
    private boolean validated;
    private float rmsValue; 
    private float dbValue;
    private  double decibelsementara;

    public Scoring() {
        decibelsementara=0;
        finalScore = 0;
        pitchCorrect = 0;
        pitchFault = 0;
        score = 0;
        uncountValue = 0;
    }

    /**
     * Gets singer final score.
     *
     * @return final score.
     * @see int
     */
    public double getScore() {
        return finalScore;
    }

    /**
     * Sets audio data from microphone and music.
     *
     * @param audioMic   Audio sample from microphone.
     * @param audioMusic Audio sample from music.
     */
    public void setAudioData(byte[] audioMic, byte[] audioMusic) {
        countScore(countDecibelLevel(audioMic), countDecibelLevel(audioMusic));
    }

    /**
     * Counts decibel level.
     *
     * @param audioData Samples audio data.
     * @return decibel level.
     * @see double
     */
    private double countDecibelLevel(byte[] audioData) {
        long lSum = 0;
        for (byte sample : audioData) {
            lSum = lSum + sample;
        }
        double dAvg = lSum / audioData.length;
        double sumMeanSquare = 0d;
        for (byte sample : audioData) {
            sumMeanSquare = sumMeanSquare + Math.pow(sample - dAvg, 2d);
        }
        double averageMeanSquare = sumMeanSquare / audioData.length;
        int rms = (int)(Math.pow(averageMeanSquare, 0.5d) + 0.5);
        return 20 * Math.log10(rms);
  
    }

    /**
     * Counts singer score by comparing both music and microphone decibel values.
     *
     * @param decibelMic   Decibel value from microphone.
     * @param decibelMusic Decibel value from music.
     */
    private void countScore(double decibelMic, double decibelMusic) {
    System.out.println("decibel = "+ decibelMic);
//        if(decibelMic>decibelsementara){
//            decibelsementara=decibelMic;
//            System.out.println("decibel tertinggi="+decibelsementara);
//        }
        if (uncounted) {
            uncounted = false;
            return;
        }

        if (decibelMic >= SILENT_TOLERANCE) {
            if (valueMic0 == null && valueMusic0 == null) {
                valueMic0 = decibelMic;
                valueMusic0 = decibelMusic;
                validated = valueMusic0 - valueMic0 <= DECIBEL_TOLERANCE;
            } else if (validated) {
                valueMusic1 = decibelMusic;
                valueMic1 = decibelMic;
                validated = false;

                if (valueMusic1 - valueMic1 <= DECIBEL_TOLERANCE) {
                    double musicDeviation = valueMusic0 - valueMusic1;
                    double micDeviation = valueMic0 - valueMic1;

                    if (((musicDeviation == 0) && (micDeviation == 0)) || ((musicDeviation < 0) && (micDeviation < 0)) ||
                        ((musicDeviation >= 0) && (micDeviation >= 0))) {
                        pitchCorrect += 1;
                    } else {
                        pitchFault += 1;
                    }
                } else {
                    pitchFault += 1;
                }

                valueMic0 = null;
                valueMusic0 = null;
            } else {
                pitchFault += 1;
                valueMic0 = null;
                valueMusic0 = null;
            }
        } else {
            uncountValue += 1;
            uncounted = true;
        }
        System.out.println("pitch benar = "+pitchCorrect);
        System.out.println("pitch salah = "+pitchFault);
//        System.out.println("uncount="+uncountValue);
        double total = pitchCorrect + pitchFault;
        double totalFrame = total + uncountValue;
        double minimalSing = totalFrame * 0.05;

        if (minimalSing > total) {
            if(total>200){
                finalScore=20;
            }else{
            finalScore = 0;
            }
        } else {
            score = (pitchCorrect / total) * 100;
            if(score<100){
                finalScore=99;
            }
            if(score<89){
                finalScore=score+10;
            }
            if(score<75){
                finalScore=score + 10;
            }
            if (score < 65) {
                finalScore = score + 20;
            }
            if(score<40){
                finalScore=score+25;
            }
            if(score<25){
                finalScore=score;
            }
//            } else {
//              //  finalScore = Math.round(score * 100.0) / 100.0;
//                finalScore=score;
//            }
        }
//        System.out.println("skore ="+finalScore);
    }
}