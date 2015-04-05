/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/**
 *
 * @author Fauziah Ifa Hasan
 */
public class DataTypeConversion {

    public DataTypeConversion() {

    }

    public static float[] convertToFloat(byte[] bytes) {

        float[] asFloat = new float[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            int qw = ((bytes[i] & 0xFF) << 8);
            asFloat[i] = Float.intBitsToFloat(qw);
        }

        return asFloat;
    }

    public static double[] convertFloatsToDoubles(float[] input) {
        if (input == null) {
            return null; // Or throw an exception - your choice
        }
        double[] output = new double[input.length];
        for (int i = 0; i < input.length; i++) {
            output[i] = input[i];
        }
        return output;
    }

    public static float[] convertDoublesToFloats(double[] input) {
        float[] floatArray = new float[input.length];
        for (int i = 0; i < input.length; i++) {
            floatArray[i] = (float) input[i];
        }
        return floatArray;
    }

}
