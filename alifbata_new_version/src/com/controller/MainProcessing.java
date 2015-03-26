/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Fauziah Ifa Hasan
 */
public class MainProcessing {

    public static void main(String[] argv) throws Exception {
        
        
        readAudioBuffertoSave();
//        double hasil1 = calculate_diff("audio/001_alif.wav", "audio/001_alif.txt");
//        double hasil2 = calculate_diff("audio/001_123.wav", "audio/001_alif.txt");
//        double hasil3 = calculate_diff("audio/001_ccc.wav", "audio/001_alif.txt");
//        double hasil4 = calculate_diff("audio/001_dsds.wav", "audio/001_alif.txt");
//        double hasil5 = calculate_diff("audio/003_dfd.wav", "audio/001_alif.txt");
//
//        double[] data = {hasil1, hasil2, hasil3, hasil4, hasil5};
//        float[] floatArray = new float[data.length];
//        for (int i = 0; i < data.length; i++) {
//            floatArray[i] = (float) data[i];
//        }
//        Divide_And_Conquer m = new Divide_And_Conquer(floatArray, data.length);
//        System.out.println("Contents of the Array");
//        m.print();
//        m.dacMaxMini();
//        System.out.println("In this array maximum element : " + m.getMax());
//        System.out.println("In this array minimum element : " + m.getMin());

    }
    public static boolean recordToSave(){
        return false;
        
    }    
    public static boolean recordTocompare(){
        AudioCapture capture=new AudioCapture();
        AudioCapture.start();
        capture.stop();  
        return false;
        
    }
    
    public static double calculate_diff(String pathfileaudioinput, String paath_buffer_filename) throws Exception {
        float[] data_input_buffer = readAudioBuffertoCompare(pathfileaudioinput);
        float[] data_file_buffer = readFromFile(paath_buffer_filename);
        double hasil = calculateDistance(data_input_buffer, data_file_buffer);
        return hasil;
    }

    public static float[] readAudioBuffertoCompare(String audioPath) throws Exception {
        Divide_And_Conquer divide_and_conquer;
        WaveDecoder decoder = new WaveDecoder(new FileInputStream(audioPath));
        float[] samples = new float[1024];
        FFT fft = new FFT(1024, 44100);
        int i = 0;
        float[] datasample_fft;
        int limit = decoder.readSamples(samples);
        float[] datasample = new float[limit];
        while (decoder.readSamples(samples) > 0) {
            fft.forward(samples);
            datasample_fft = fft.inverse(samples);
            divide_and_conquer = new Divide_And_Conquer(datasample_fft, datasample_fft.length);
            divide_and_conquer.dacMaxMini();
            float minimal = divide_and_conquer.getMin();
            float maximal = divide_and_conquer.getMax();
            try {
                datasample[i] = minimal;
                datasample[i + 1] = maximal;
                if (i == limit) {
                    i++;
                } else {
                    i += 2;
                }
            } catch (Exception ex) {
                System.out.println(" error : " + ex.getMessage());
            }

        }
        return datasample;
    }

    public static void readAudioBuffertoSave() throws Exception {
        
        Divide_And_Conquer divide_and_conquer;
        WaveDecoder decoder = new WaveDecoder(new FileInputStream("audio/001_alif.wav"));
        float[] samples = new float[1024];
                    
        FFT fft = new FFT(1024, 44100);
        int i = 0;
        float[] datasample_fft;
        int limit = decoder.readSamples(samples);
        float[][] datasample = new float[limit][2];
        while (decoder.readSamples(samples) > 0) {
           
            fft.forward(samples);
            datasample_fft = fft.inverse(samples);
            divide_and_conquer = new Divide_And_Conquer(datasample_fft, datasample_fft.length);
            divide_and_conquer.dacMaxMini();
            float minimal = divide_and_conquer.getMin();
            float maximal = divide_and_conquer.getMax();
            // System.out.println("minimal : "  + minimal);
            datasample[i][0] = minimal;
            datasample[i][1] = maximal;
            i++;
        }
        writeArrayToFile("audio/001_alif.txt", datasample);
        readFromFile("audio/001_alif.txt");
    }

    public static void writeArrayToFile(String filename, float[][] array) {
        PrintStream ps;
        try {
            ps = new PrintStream(new FileOutputStream(filename));
            for (int row = 0; row < array.length; row++) {
                for (int col = 0; col < array[row].length; col++) {
                    float s = array[row][col];
                    ps.println(s);
                }
            }
            ps.close();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public static float[] readFromFile(String filename) throws FileNotFoundException {
        String token1 = "";
        Scanner inFile1 = new Scanner(new File(filename)).useDelimiter("\\n");
        List<String> temps = new ArrayList<>();

        // while loop
        while (inFile1.hasNext()) {
            // find next line
            token1 = inFile1.next();
            temps.add(token1);
        }
        inFile1.close();
        String[] tempsArray = temps.toArray(new String[0]);
        float[] data_buffer = new float[tempsArray.length];
        for (int i = 0; i < tempsArray.length; i++) {
            data_buffer[i] = Float.valueOf(tempsArray[i]);
        }

        return data_buffer;

    }

    public static Float[] tes(String file) throws FileNotFoundException {
        Scanner inFile1 = new Scanner(new File(file)).useDelimiter("\\n");
        List<Float> temps = new ArrayList<Float>();

        // while loop
        while (inFile1.hasNext()) {
            // find next line
            String www = inFile1.next();

            float token1 = Float.parseFloat(www.trim());
            temps.add(token1);
        }
        inFile1.close();

        Float[] tempsArray = temps.toArray(new Float[0]);

        return tempsArray;
    }

    public static double calculateDistance(float[] array1, float[] array2) {
        double Sum = 0.0;
        for (int i = 0; i < array1.length; i++) {
            Sum = Sum + Math.pow((array1[i] - array2[i]), 2.0);
        }
        return Math.sqrt(Sum);
    }

}
