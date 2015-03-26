/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.util;

import java.io.File;
import java.io.FilenameFilter;

/**
 *
 * @author Fauziah Ifa Hasan
 */
public class Global_Function {

    public Global_Function() {

    }

    public File[] scan_directory(String path) {
        File dir = new File("audio/");
        File[] files = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".wav");
            }
        });

        return files;

    }

}
