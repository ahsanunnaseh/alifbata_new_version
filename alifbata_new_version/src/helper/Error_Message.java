/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package helper;

import javax.swing.JOptionPane;

/**
 *
 * @author Fauziah Ifa Hasan
 */
public abstract class Error_Message {
    public static boolean isWindows() {
        return System.getProperty("os.name").toUpperCase().contains("WIN");
    }

    
}
