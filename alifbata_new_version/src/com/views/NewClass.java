/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.views;

import java.util.Arrays;
import javax.swing.JOptionPane;

/**
 *
 * @author Fauziah Ifa Hasan
 */
public class NewClass {

    public int isPowerOfTwodd(int x) {
        int[] two = {1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384, 32768,
            65536, 131072, 262144, 524288, 1048576, 2097152, 4194304, 8388608,
            16777216, 33554432, 67108864, 134217728, 268435456, 536870912};
              int [] atas = new int[30];
            int [] bawah = new int[30];
        for(int i=0; i<two.length; i++){
      
            if(x>two[i]){
                atas[i]=two[i];
            }else{
              bawah[i]= two[i]; 
            }
        }
        return getMaxValue(atas);

    }
    public static int getMaxValue(int[] array){  
      int maxValue = array[0];  
      for(int i=1;i < array.length;i++){  
      if(array[i] > maxValue){  
      maxValue = array[i];  

         }  
     }  
             return maxValue;  
}  
    public static void main(String [] args){
        int a=JOptionPane.showConfirmDialog(null, "Apakah Masih Training Huruf:" , "Informasi", JOptionPane.YES_NO_OPTION);
        System.out.println("nilai i:" + a );
    }

  
}
