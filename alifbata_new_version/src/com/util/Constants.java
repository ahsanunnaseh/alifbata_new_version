/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.util;

import java.awt.Toolkit;

/**
 *
 * @author Fauziah Ifa Hasan
 */
public final class Constants {

    public static final String IMAGE_PATH = "audio_image/";
    public static final String AUDIO_PATH = "audio_file/";
    public static final String TXT_AUDIO_PATH = "txt_audio_file";
    public static Constants instance;

    private Constants() {

    }

    private static Constants getInstance() {
        if (instance == null) {
            instance = new Constants();
        }

        return instance;
    }
}
