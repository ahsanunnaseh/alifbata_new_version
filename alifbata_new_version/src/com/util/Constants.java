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

    public static final String IMAGE_ROOT_PATH = "audio_image/";
    public static final String IMAGE_ROOT_PATH_LEVEL2 = "/audio_image/level2/";
    public static final String IMAGE_ROOT_PATH_LEVEL3 = "/audio_image/level3/";
    public static final String AUDIO_ROOT_PATH = "audio_file/";
    public static final String AUDIO_ROOT_PATH_LEVEL2 = "/audio_file/level2";
    public static final String AUDIO_ROOT_PATH_LEVEL3 = "/audio_file/level3";
    public static final String TXT_ROOT_AUDIO_PATH= "txt_audio_file/";
    public static final String TXT_ROOT_AUDIO_PATH_LEVEL2 = "/txt_audio_file/level2/";
    public static final String TXT_ROOT_AUDIO_PATH_LEVEL3 = "/txt_audio_file/level3/";
    
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
