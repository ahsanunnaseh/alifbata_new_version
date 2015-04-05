/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.audio.processing;

import com.util.DataTypeConversion;
import com.util.Error_Message;
import com.util.Scoring;
import com.views.Main_Frame;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Fauziah Ifa Hasan
 */
public class MainProcessing {

    private final double[] samples;
    private final FFT fft;
    private Divide_And_Conquer divide_and_conquer;

    public MainProcessing(double[] samples) {
        this.samples = samples;
        fft = new FFT(1024, 44100);
    }

    public String doMatching(float[] samples, String path) {

        fft.forward(samples);
        float[] datasample_fft = fft.inverse(samples);
        File[] file = finder("./txt_audio_file/");
        int limit=1024;
        if (file.length < 0) {
            Error_Message.confirm_dialog( "Informasi", "Belum ada training huruf hijaiyah  " + path);
        } else {
            float[] datasample = new float[limit];
            double[] hasil = new double[file.length];
            for (int i = 0; i < file.length; i++) {
                try {
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
                    float[] file_sample = readSampleFromFile(path + "/" + file[i].getName());
                    hasil[i] = calculateDistance(datasample, file_sample);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(MainProcessing.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println(file[i].getName());
            }
            float[] shorting = DataTypeConversion.convertDoublesToFloats(hasil);
            divide_and_conquer = new Divide_And_Conquer(shorting, shorting.length);

        }
        float minimal = divide_and_conquer.getMin();
        Scoring scor = new Scoring(minimal);
        return new DecimalFormat("##.##").format(scor.getScore());
    }

    public static File[] finder(String dirName) {
        File dir = new File(dirName);
        return dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                return filename.endsWith(".txt");
            }
        });

    }

    public static float[] readSampleFromFile(String filename) throws FileNotFoundException {
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

    public static double calculateDistance(float[] array1, float[] array2) {
        double Sum = 0.0;
        for (int i = 0; i < array1.length; i++) {
            Sum = Sum + Math.pow((array1[i] - array2[i]), 2.0);
        }
        return Math.sqrt(Sum);
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

        readSampleFromFile("audio/001_alif.txt");
    }



}
