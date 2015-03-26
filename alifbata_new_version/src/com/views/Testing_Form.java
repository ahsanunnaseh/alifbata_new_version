/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.views;

import com.audio.processing.AudioCapture;
import java.util.Arrays;

/**
 *
 * @author Fauziah Ifa Hasan
 */
public class Testing_Form extends javax.swing.JFrame {
    private final AudioCapture audiocapture;
    public Testing_Form() {
        initComponents();
        audiocapture=new AudioCapture();
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btstart = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        btstart.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btstart.setText("Testing");
        btstart.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btstartMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btstartMouseReleased(evt);
            }
        });
        btstart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btstartActionPerformed(evt);
            }
        });
        getContentPane().add(btstart);
        btstart.setBounds(340, 280, 110, 110);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(null);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Skor Anda ");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(20, 210, 130, 17);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("Anda Lulus");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(330, 210, 90, 17);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(160, 20, 430, 240);

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton1.setText("SKOR");
        getContentPane().add(jButton1);
        jButton1.setBounds(20, 130, 120, 100);

        jButton2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton2.setText("Level");
        getContentPane().add(jButton2);
        jButton2.setBounds(20, 20, 120, 100);

        setBounds(0, 0, 641, 471);
    }// </editor-fold>//GEN-END:initComponents

    private void btstartMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btstartMousePressed
       
         AudioCapture.start();
         System.out.println("mulai.....");
         
         System.out.println(Arrays.toString(audiocapture.getAudioData()));
    }//GEN-LAST:event_btstartMousePressed

    private void btstartMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btstartMouseReleased
        audiocapture.stop();
        System.out.println("moveS");// TODO add your handling code here:
    }//GEN-LAST:event_btstartMouseReleased

    private void btstartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btstartActionPerformed
       
    }//GEN-LAST:event_btstartActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Testing_Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Testing_Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Testing_Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Testing_Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Testing_Form().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btstart;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
