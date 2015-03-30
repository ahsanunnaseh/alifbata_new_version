package com.views;

import com.audio.wavProcessing.FormatControlConf;
import com.audio.wavProcessing.WaveData;

import com.util.Error_Message;
import com.util.Image_Processing;
import com.util.MessageType;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Fauziah Ifa Hasan
 */
public class Test_Form extends JFrame implements ActionListener {

    private final Error_Message error_message;



    byte[] audioBytes = null;
    float[] audioData = null;
    final int BUFFER_SIZE = 16384;
    int counter = 0;
    FormatControlConf formatControls = new FormatControlConf(); // @jve:decl-index=0:
    Capture capture = new Capture(); // @jve:decl-index=0:

    Vector<Line2D.Double> lines = new Vector<Line2D.Double>(); // @jve:decl-index=0:
    AudioInputStream audioInputStream; // @jve:decl-index=0:
    File file; // @jve:decl-index=0:
    SamplingGraph samplingGraph;
    WaveData wd;

    boolean isDrawingRequired;
    boolean isSaveRequired;
    String saveFileName = null;
    String errStr;
    double duration, seconds;
    JVisualizer samplingPanel = new JVisualizer();

    public Test_Form(boolean isDrawingRequired, boolean isSaveRequired) throws IOException {
        wd = new WaveData();
        this.isDrawingRequired = isDrawingRequired;
        this.isSaveRequired = isSaveRequired;
        initComponents();
        error_message = new Error_Message();
        File aa=new File("audio_image/level1_sdd.jpg");
        setImage(aa);
        if (isDrawingRequired) {

            samplingPanel.add(samplingGraph = new SamplingGraph());
            samplingPanel.setBackground(new java.awt.Color(29, 13, 13));
            samplingPanel.setBounds(20, 260, 610, 110);
            add(samplingPanel);
        }

    }

    public void setImage(File file) throws IOException {
        Image_Processing image_processing = new Image_Processing();

        Image image = image_processing.getScaledImage(ImageIO.read(file), lbgambar.getWidth(), lbgambar.getHeight());

        BufferedImage img = (BufferedImage) image;
        ImageIcon icon = new ImageIcon(img); // ADDED
        lbgambar.setIcon(icon); // ADDED


        Dimension imageSize = new Dimension(lbgambar.getWidth(), lbgambar.getHeight()); // ADDED
        lbgambar.setPreferredSize(imageSize); // ADDED

        lbgambar.revalidate(); // ADDED
        lbgambar.repaint(); // ADDED
    }

    public String getSaveFileName() {
        return saveFileName;
    }

    public void setSaveFileName(String saveFileName) {
        this.saveFileName = saveFileName;
        System.out.println("FileName Changed !!! " + saveFileName);
    }

    public void createAudioInputStream(File file, boolean updateComponents) {
        if (file != null && file.isFile()) {
            try {
                this.file = file;
                errStr = null;
                audioInputStream = AudioSystem.getAudioInputStream(file);

                // fileName = file.getName();
                long milliseconds = (long) ((audioInputStream.getFrameLength() * 1000) / audioInputStream.getFormat().getFrameRate());
                duration = milliseconds / 1000.0;

                if (updateComponents) {
                    formatControls.setFormat(audioInputStream.getFormat());
                    if (isDrawingRequired) {
                        samplingGraph.createWaveForm(null);
                    }
                }
            } catch (Exception ex) {
                reportStatus(ex.toString(), MessageType.ERROR);
            }
        } else {
            reportStatus("Audio file required.", MessageType.INFO);
        }
    }

    private void reportStatus(String msg, MessageType type) {
        if ((errStr = msg) != null) {
            System.out.println(errStr);
            if (isDrawingRequired) {
                samplingGraph.repaint();
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("actionPerformed *********");
        Object obj = e.getSource();

        if (captB.getText().startsWith("Record")) {
            startRecord();
        } else {
            stopRecording();
        }
    }

    public void startRecord() {
        file = null;
        capture.start();
        if (isDrawingRequired) {
            samplingGraph.start();
        }
        captB.setText("Stop");
    }

    public void stopRecording() {
        lines.removeAllElements();
        capture.stop();
        if (isDrawingRequired) {
            samplingGraph.stop();
        }
        captB.setText("Record");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        captB = new javax.swing.JButton();
        lbgambar = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        captB.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/resources/playtest.jpg"))); // NOI18N
        captB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                captBActionPerformed(evt);
            }
        });
        getContentPane().add(captB);
        captB.setBounds(30, 230, 80, 80);
        captB.setPreferredSize(new Dimension(85, 24));
        captB.addActionListener(this);
        captB.setEnabled(true);
        captB.setFocusable(false);

        lbgambar.setBackground(new java.awt.Color(0, 255, 68));
        lbgambar.setForeground(new java.awt.Color(46, 54, 46));
        lbgambar.setOpaque(true);
        getContentPane().add(lbgambar);
        lbgambar.setBounds(180, 100, 190, 200);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(null);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/resources/previousb.jpg"))); // NOI18N
        jPanel1.add(jButton1);
        jButton1.setBounds(10, 20, 50, 50);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/resources/nextb.jpg"))); // NOI18N
        jPanel1.add(jButton2);
        jButton2.setBounds(550, 20, 50, 50);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(10, 320, 610, 100);

        jPanel2.setBackground(java.awt.Color.green);
        jPanel2.setLayout(null);

        jLabel1.setText("100");
        jPanel2.add(jLabel1);
        jLabel1.setBounds(90, 10, 18, 14);

        jLabel2.setText("Skor");
        jPanel2.add(jLabel2);
        jLabel2.setBounds(10, 10, 50, 14);

        getContentPane().add(jPanel2);
        jPanel2.setBounds(130, 10, 130, 30);

        jPanel4.setBackground(java.awt.Color.green);
        getContentPane().add(jPanel4);
        jPanel4.setBounds(360, 10, 90, 30);

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/resources/homeb.jpg"))); // NOI18N
        getContentPane().add(jButton3);
        jButton3.setBounds(590, 0, 50, 50);

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/resources/soundb.jpg"))); // NOI18N
        getContentPane().add(jButton4);
        jButton4.setBounds(0, 0, 50, 50);

        setBounds(0, 0, 656, 481);
    }// </editor-fold>//GEN-END:initComponents

    private void captBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_captBActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_captBActionPerformed

    /**
     * Reads data from the input channel and writes to the output stream
     */
    class Capture implements Runnable {

        TargetDataLine line;
        Thread thread;

        public void start() {
            errStr = null;
            thread = new Thread(this);
            thread.setName("Capture");
            thread.start();
        }

        public void stop() {
            thread = null;
        }

        private void shutDown(String message) {
            if ((errStr = message) != null && thread != null) {
                thread = null;
                if (isDrawingRequired) {
                    samplingGraph.stop();
                }
                captB.setText("Record");
                if (isDrawingRequired) {
                    samplingGraph.repaint();
                }
            }
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Runnable#run()
         */
        @Override
        public void run() {

            duration = 0;
            audioInputStream = null;

            // define the required attributes for our line,
            // and make sure a compatible line is supported.
            AudioFormat format = formatControls.getFormat();
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

            if (!AudioSystem.isLineSupported(info)) {
                shutDown("Line matching " + info + " not supported.");
                return;
            }

            // get and open the target data line for capture.
            try {
                line = (TargetDataLine) AudioSystem.getLine(info);
                line.open(format, line.getBufferSize());
            } catch (LineUnavailableException ex) {
                shutDown("Unable to open the line: " + ex);
                return;
            } catch (SecurityException ex) {
                shutDown(ex.toString());
                // JavaSound.showInfoDialog();
                return;
            } catch (Exception ex) {
                shutDown(ex.toString());
                return;
            }

            // play back the captured audio data
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int frameSizeInBytes = format.getFrameSize();
            int bufferLengthInFrames = line.getBufferSize() / 8;
            int bufferLengthInBytes = bufferLengthInFrames * frameSizeInBytes;
            byte[] data = new byte[bufferLengthInBytes];
            int numBytesRead;

            line.start();

            while (thread != null) {
                if ((numBytesRead = line.read(data, 0, bufferLengthInBytes)) == -1) {
                    break;
                }
                samplingPanel.setSamples(data);

                System.out.println(Arrays.toString(data));
                out.write(data, 0, numBytesRead);
            }

            // we reached the end of the stream. stop and close the line.
            line.stop();
            line.close();
            line = null;

            // stop and close the output stream
            try {
                out.flush();
                out.close();
            } catch (IOException ex) {
                reportStatus("Error on inputstream", MessageType.ERROR);
            }

            // load bytes into the audio input stream for playback
            audioBytes = out.toByteArray();
            System.out.println(out.size());
            ByteArrayInputStream bais = new ByteArrayInputStream(audioBytes);
            audioInputStream = new AudioInputStream(bais, format, audioBytes.length / frameSizeInBytes);

            long milliseconds = (long) ((audioInputStream.getFrameLength() * 1000) / format.getFrameRate());
            duration = milliseconds / 1000.0;

            try {
                audioInputStream.reset();
            } catch (Exception ex) {
                reportStatus("Eor in reseting inputStream", MessageType.ERROR);
            }
            if (isDrawingRequired) {
                samplingGraph.createWaveForm(audioBytes);
            }

        }
    }

    class SamplingGraph extends JPanel implements Runnable {

        private static final long serialVersionUID = 1L;

        private Thread thread;
        private Font font10 = new Font("serif", Font.PLAIN, 10);
        private Font font12 = new Font("serif", Font.PLAIN, 12);
        Color jfcBlue = new Color(204, 204, 255);
        Color pink = new Color(255, 175, 175);
        AudioFormat format;

        public SamplingGraph() {
            setBackground(new Color(20, 20, 20));
        }

        /**
         * Creates the wave form.
         *
         * @param audioBytes the audio bytes
         */
        public void createWaveForm(byte[] audioBytes) {

            lines.removeAllElements(); // clear the old vector

            Dimension d = getSize();
            int w = d.width;
            int h = d.height - 15;
            audioData = null;
            // wd.set
            audioData = wd.extractFloatDataFromAudioInputStream(audioInputStream);
            // ArrayWriter.printFloatArrayToConole(audioData);
            int frames_per_pixel = wd.getAudioBytes().length / wd.getFormat().getFrameSize() / w;
            byte my_byte = 0;
            double y_last = 0;
            // we need format object
            int numChannels = wd.getFormat().getChannels();
            for (double x = 0; x < w && audioData != null; x++) {
                int idx = (int) (frames_per_pixel * numChannels * x);
                if (wd.getFormat().getSampleSizeInBits() == 8) {
                    my_byte = (byte) audioData[idx];
                } else {
                    my_byte = (byte) (128 * audioData[idx] / 32768);
                }
                double y_new = (double) (h * (128 - my_byte) / 256);
                lines.add(new Line2D.Double(x, y_last, x, y_new));
                y_last = y_new;
            }
            // just need lines object to repaint()
            repaint();
        }

        public void start() {
            thread = new Thread(this);
            thread.setName("SamplingGraph");
            thread.start();
            seconds = 0;
        }

        public void stop() {
            if (thread != null) {
                thread.interrupt();
            }
            thread = null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Runnable#run()
         */
        public void run() {
            seconds = 0;
            while (thread != null) {
                if ((capture.line != null) && (capture.line.isActive())) {

                    long milliseconds = (long) (capture.line.getMicrosecondPosition() / 1000);
                    seconds = milliseconds / 1000.0;
                }

                try {
                    thread.sleep(100);
                } catch (Exception e) {
                    break;
                }

                repaint();

                while ((capture.line != null && !capture.line.isActive())) {
                    try {
                        thread.sleep(10);
                    } catch (Exception e) {
                        break;
                    }
                }
            }
            seconds = 0;
            repaint();
        }
    } // End class SamplingGraph

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new Test_Form(true, true).setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(Test_Form.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton captB;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JLabel lbgambar;
    // End of variables declaration//GEN-END:variables
}
