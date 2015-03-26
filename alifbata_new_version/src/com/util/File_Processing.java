/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Fauziah Ifa Hasan
 */
public class File_Processing {

    public static final String DOT_SEPARATOR = ".";
    
    public File_Processing(){
        
    }
    public static void doCopy(File source, File dest) throws IOException {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream(source).getChannel();
            outputChannel = new FileOutputStream(dest).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(File_Processing.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            inputChannel.close();
            outputChannel.close();
        }
    }

    public static String getFileExtension(File file) {
        if (file == null) {
            return null;
        }
        String name = file.getName();
        int extIndex = name.lastIndexOf(File_Processing.DOT_SEPARATOR);

        if (extIndex == -1) {
            return "";
        } else {
            return name.substring(extIndex + 1);
        }
    }

}
