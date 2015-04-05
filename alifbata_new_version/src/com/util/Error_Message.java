/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.util;

import java.awt.Component;
import javax.swing.JOptionPane;

/**
 *
 * @author Fauziah Ifa Hasan
 */
public class Error_Message {

    public  Error_Message() {

    }

    public static void confirm_dialog(Component comp, String title, String message) {
        JOptionPane.showMessageDialog(comp, message, title, JOptionPane.OK_OPTION);
    }
    
     public static void confirm_dialog(String title, String message) {
        JOptionPane.showMessageDialog(null,message, title, JOptionPane.OK_OPTION);
    }

    public static void error_print(String message) {
        System.err.println(message);
    }
    
}
