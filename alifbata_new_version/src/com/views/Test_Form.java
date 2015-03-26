
package com.views;

import com.audio.FormatControlConf;
import com.audio.WaveData;
import com.controller.JavaSoundRecorder;
import com.util.Error_Message;
import com.util.Global_Function;
import com.util.MessageType;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Vector;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Fauziah Ifa Hasan
 */
public class Test_Form extends JFrame implements ActionListener {

    private final Error_Message error_message;
    private final Global_Function global_function;
    private JavaSoundRecorder recorder;


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

    public Test_Form(boolean isDrawingRequired, boolean isSaveRequired) {
        wd = new WaveData();
        this.isDrawingRequired = isDrawingRequired;
        this.isSaveRequired = isSaveRequired;
        initComponents();
        error_message = new Error_Message();
        global_function = new Global_Function();

        if (isDrawingRequired) {
           
//                        EmptyBorder eb = new EmptyBorder(1, 1, 1, 1);
//			SoftBevelBorder sbb = new SoftBevelBorder(SoftBevelBorder.LOWERED);
//			samplingPanel.setBorder(new CompoundBorder(eb, sbb));
            samplingPanel.add(samplingGraph = new SamplingGraph());
            samplingPanel.setBackground(new java.awt.Color(29, 13, 13));
            samplingPanel.setBounds(20, 260, 610, 110);
            add(samplingPanel);
        }

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

        imagePanel = new javax.swing.JPanel();
        captB = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        imagePanel.setBackground(new java.awt.Color(48, 200, 65));
        getContentPane().add(imagePanel);
        imagePanel.setBounds(200, 10, 200, 210);

        captB.setText("Record");
        captB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                captBActionPerformed(evt);
            }
        });
        getContentPane().add(captB);
        captB.setBounds(200, 230, 200, 23);
        captB.setPreferredSize(new Dimension(85, 24));
        captB.addActionListener(this);
        captB.setEnabled(true);
        captB.setFocusable(false);

        setBounds(0, 0, 662, 424);
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
                new Test_Form(true, true).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton captB;
    private javax.swing.JPanel imagePanel;
    // End of variables declaration//GEN-END:variables
}
