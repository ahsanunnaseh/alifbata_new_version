

package com.views;

import com.audio.FormatControlConf;
import com.audio.WaveData;
import com.audio.preProcessing.FFT;
import com.controller.JavaSoundRecorder;
import com.util.Error_Message;
import com.util.Global_Function;
import com.util.MessageType;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Fauziah Ifa Hasan
 */
public class Training_Form extends JFrame implements ActionListener {

    private final Error_Message error_message;
    private final Global_Function global_function;
    private JavaSoundRecorder recorder;

    byte[] audioBytes = null;
    float[] audioData = null;
    final int BUFFER_SIZE = 16384;
    int counter = 0;
    FormatControlConf formatControls = new FormatControlConf(); // @jve:decl-index=0:
    Capture capture = new Capture(); // @jve:decl-index=0:
    Playback playback = new Playback(); // @jve:decl-index=0:
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

    public Training_Form(boolean isDrawingRequired, boolean isSaveRequired) {
        wd = new WaveData();
        this.isDrawingRequired = isDrawingRequired;
        this.isSaveRequired = isSaveRequired;
        initComponents();
        error_message = new Error_Message();
        global_function = new Global_Function();

        if (isDrawingRequired) {
            JPanel samplingPanel = new JPanel(new BorderLayout());
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
                playB.setEnabled(true);
                // fileName = file.getName();
                long milliseconds = (long) ((audioInputStream.getFrameLength() * 1000) / audioInputStream.getFormat().getFrameRate());
                duration = milliseconds / 1000.0;

                saveB.setEnabled(true);
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
        if (isSaveRequired && obj.equals(saveB)) {
            getFileNameAndSaveFile();
        } else if (obj.equals(playB)) {
            if (playB.getText().startsWith("Play")) {
                playCaptured();
            } else {
                stopPlaying();
            }
        } else if (obj.equals(captB)) {
            if (captB.getText().startsWith("Record")) {
                startRecord();
            } else {
                stopRecording();
            }
        } else if (obj.equals(pausB)) {
            if (pausB.getText().startsWith("Pause")) {
                pausePlaying();
            } else {
                resumePlaying();
            }
        }
    }

    public void playCaptured() {
        playback.start();
        if (isDrawingRequired) {
            samplingGraph.start();
        }
        captB.setEnabled(false);
        pausB.setEnabled(true);
        playB.setText("Stop");
    }

    public void stopPlaying() {
        playback.stop();
        if (isDrawingRequired) {
            samplingGraph.stop();
        }
        captB.setEnabled(true);
        pausB.setEnabled(false);
        playB.setText("Play");
    }

    public void startRecord() {
        file = null;
        capture.start();
        if (isDrawingRequired) {
            samplingGraph.start();
        }
        playB.setEnabled(false);
        pausB.setEnabled(true);
        saveB.setEnabled(false);
        captB.setText("Stop");
    }

    public void stopRecording() {
        lines.removeAllElements();
        capture.stop();
        if (isDrawingRequired) {
            samplingGraph.stop();
        }
        playB.setEnabled(true);
        pausB.setEnabled(false);
        saveB.setEnabled(true);
        captB.setText("Record");
    }

    public void pausePlaying() {

        if (capture.thread != null) {
            capture.line.stop();
        } else {
            if (playback.thread != null) {
                playback.line.stop();
            }
        }
        pausB.setText("Resume");

    }

    public void resumePlaying() {
        if (capture.thread != null) {
            capture.line.start();
        } else {
            if (playback.thread != null) {
                playback.line.start();
            }
        }
        pausB.setText("Pause");
    }

    private String level_translate(JComboBox combo) {
        String prefix_name = "";
        if (combo.getSelectedItem().toString().equalsIgnoreCase("Level 1")) {
            prefix_name = "level1_";
        } else if (combo.getSelectedItem().toString().equalsIgnoreCase("Level 2")) {
            prefix_name = "level2_";
        } else if (combo.getSelectedItem().toString().equalsIgnoreCase("Level 3")) {
            prefix_name = "level3_";
        } else {
            prefix_name = "error_prefix";
        }
        return prefix_name;
    }

    public void getFileNameAndSaveFile() {
        String prefixfile = level_translate(cblevel);
        if (saveFileName != null && saveFileName.equalsIgnoreCase(prefixfile + txname.getText())) {
            JOptionPane.showMessageDialog(this, "Apakah Masih Training Huruf:" + txname.getText(), "Informasi", JOptionPane.YES_NO_CANCEL_OPTION);
        }
        while (saveFileName == null || !saveFileName.equalsIgnoreCase(prefixfile + txname.getText())) {
            saveFileName = JOptionPane.showInputDialog(this, "Apakah Training " + cblevel.getSelectedItem().toString() + " Huruf Hijaiyah berikut :", prefixfile + txname.getText());
        }

        wd.saveToFile(saveFileName, AudioFileFormat.Type.WAVE, audioInputStream);
        wd.extractFloatDataFromAudioInputStream_saveToTXTFile(saveFileName, audioInputStream);
        float data [] =wd.extractFloatDataFromAudioInputStream(audioInputStream);
        FFT fft=new FFT();
        fft.computeFFT(data);

    }

    private Image getScaledImage(Image srcImg, int w, int h) {
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();
        return resizedImg;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cblevel = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        txname = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        txpath = new javax.swing.JTextField();
        playB = new javax.swing.JButton();
        saveB = new javax.swing.JButton();
        captB = new javax.swing.JButton();
        pausB = new javax.swing.JButton();
        lbgambar = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        jLabel1.setText("Gambar");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(20, 120, 70, 17);

        jLabel2.setText("Huruf Hijaiyah");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(20, 30, 110, 20);

        cblevel.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Level 1", "Level 2", "Level 3" }));
        getContentPane().add(cblevel);
        cblevel.setBounds(130, 70, 280, 30);

        jLabel3.setText("Level");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(20, 70, 80, 20);

        txname.setPreferredSize(new java.awt.Dimension(6, 25));
        getContentPane().add(txname);
        txname.setBounds(130, 30, 280, 25);

        jButton1.setText("........");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1);
        jButton1.setBounds(340, 110, 60, 30);

        txpath.setPreferredSize(new java.awt.Dimension(6, 25));
        getContentPane().add(txpath);
        txpath.setBounds(130, 110, 200, 25);

        playB.setText("Play");
        getContentPane().add(playB);
        playB.setBounds(200, 160, 80, 27);
        playB.setPreferredSize(new Dimension(85, 24));
        playB.addActionListener(this);
        playB.setEnabled(false);
        playB.setFocusable(false);

        saveB.setText("Save");
        saveB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveBActionPerformed(evt);
            }
        });
        getContentPane().add(saveB);
        saveB.setBounds(290, 160, 80, 27);
        saveB.setPreferredSize(new Dimension(85, 24));
        saveB.addActionListener(this);
        saveB.setEnabled(false);
        saveB.setFocusable(false);

        captB.setText("Record");
        captB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                captBActionPerformed(evt);
            }
        });
        getContentPane().add(captB);
        captB.setBounds(20, 160, 80, 27);
        captB.setPreferredSize(new Dimension(85, 24));
        captB.addActionListener(this);
        captB.setEnabled(true);
        captB.setFocusable(false);

        pausB.setText("Pause");
        getContentPane().add(pausB);
        pausB.setBounds(110, 160, 80, 27);
        pausB.setPreferredSize(new Dimension(85, 24));
        pausB.addActionListener(this);
        pausB.setEnabled(false);
        pausB.setFocusable(false);

        lbgambar.setBackground(new java.awt.Color(0, 255, 68));
        lbgambar.setForeground(new java.awt.Color(46, 54, 46));
        lbgambar.setOpaque(true);
        getContentPane().add(lbgambar);
        lbgambar.setBounds(430, 30, 200, 220);

        setBounds(0, 0, 662, 424);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        JFileChooser jfc = new JFileChooser("Select Image File to Verify");
        jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        jfc.setSize(new Dimension(541, 326));
        jfc.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public String getDescription() {
                return ".Image Files";
            }

            @Override
            public boolean accept(File f) {
                return (f.getName().toLowerCase().endsWith("PNG") || f.getName().toLowerCase().endsWith("JPEG") || f.isDirectory());
            }
        });
        int chooseOpt = jfc.showOpenDialog(this);
        if (chooseOpt == JFileChooser.APPROVE_OPTION) {
            File imagefile = jfc.getSelectedFile();
            try {
                Image image = getScaledImage(ImageIO.read(imagefile), lbgambar.getWidth(), lbgambar.getHeight());
                BufferedImage img = (BufferedImage) image;
                ImageIcon icon = new ImageIcon(img); // ADDED
                lbgambar.setIcon(icon); // ADDED
                txpath.setText(imagefile.toString());

                Dimension imageSize = new Dimension(lbgambar.getWidth(), lbgambar.getHeight()); // ADDED
                lbgambar.setPreferredSize(imageSize); // ADDED

                lbgambar.revalidate(); // ADDED
                lbgambar.repaint(); // ADDED
            } catch (IOException e1) {
            }

//            System.out.println("selected File " + imagefile);
//            txpath.setText(imagefile.toString());
//            imagePanel.updateUI();
//            ImagePanel panel = new ImagePanel(new ImageIcon(imagefile.toString()).getImage());
//             
//             getContentPane().add(panel);
//             panel.setBounds(430, 20, 200, 210);
        }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void saveBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_saveBActionPerformed

    private void captBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_captBActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_captBActionPerformed

    public class Playback implements Runnable {

        SourceDataLine line;
        Thread thread;

        public void start() {
            errStr = null;
            thread = new Thread(this);
            thread.setName("Playback");
            thread.start();
        }

        public void stop() {
            thread = null;
        }

        private void shutDown(String message) {
            if ((errStr = message) != null) {
                System.err.println(errStr);
                if (isDrawingRequired) {
                    samplingGraph.repaint();
                }
            }
            if (thread != null) {
                thread = null;
                if (isDrawingRequired) {
                    samplingGraph.stop();
                }
                captB.setEnabled(true);
                pausB.setEnabled(false);
                playB.setText("Play");
            }
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Runnable#run()
         */
        public void run() {

            // reload the file if loaded by file
            if (file != null) {
                createAudioInputStream(file, false);
            }

            // make sure we have something to play
            if (audioInputStream == null) {
                shutDown("No loaded audio to play back");
                return;
            }
            // reset to the beginnning of the stream
            try {
                audioInputStream.reset();
            } catch (Exception e) {
                shutDown("Unable to reset the stream\n" + e);
                return;
            }

            // get an AudioInputStream of the desired format for playback
            AudioFormat format = formatControls.getFormat();
            AudioInputStream playbackInputStream = AudioSystem.getAudioInputStream(format, audioInputStream);

            if (playbackInputStream == null) {
                shutDown("Unable to convert stream of format " + audioInputStream + " to format " + format);
                return;
            }

            // define the required attributes for our line,
            // and make sure a compatible line is supported.
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            if (!AudioSystem.isLineSupported(info)) {
                shutDown("Line matching " + info + " not supported.");
                return;
            }

            // get and open the source data line for playback.
            try {
                line = (SourceDataLine) AudioSystem.getLine(info);
                line.open(format, BUFFER_SIZE);
            } catch (LineUnavailableException ex) {
                shutDown("Unable to open the line: " + ex);
                return;
            }

            // play back the captured audio data
            int frameSizeInBytes = format.getFrameSize();
            int bufferLengthInFrames = line.getBufferSize() / 8;
            int bufferLengthInBytes = bufferLengthInFrames * frameSizeInBytes;
            byte[] data = new byte[bufferLengthInBytes];
            int numBytesRead = 0;

            // start the source data line
            line.start();

            while (thread != null) {
                try {
                    if ((numBytesRead = playbackInputStream.read(data)) == -1) {
                        break;
                    }
                    int numBytesRemaining = numBytesRead;
                    while (numBytesRemaining > 0) {
                        numBytesRemaining -= line.write(data, 0, numBytesRemaining);
                    }
                } catch (Exception e) {
                    shutDown("Error during playback: " + e);
                    break;
                }
            }
            // we reached the end of the stream. let the data play out, then
            // stop and close the line.
            if (thread != null) {
                line.drain();
            }
            line.stop();
            line.close();
            line = null;
            shutDown(null);
        }
    } // End class Playback

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

                playB.setEnabled(true);
                pausB.setEnabled(false);
                saveB.setEnabled(true);
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

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.JComponent#paint(java.awt.Graphics)
         */
        public void paint(Graphics g) {

            Dimension d = getSize();
            int w = d.width;
            int h = d.height;
            int INFOPAD = 15;

            Graphics2D g2 = (Graphics2D) g;
            g2.setBackground(getBackground());
            g2.clearRect(0, 0, w, h);
            g2.setColor(Color.white);
            g2.fillRect(0, h - INFOPAD, w, INFOPAD);

            if (errStr != null) {
                g2.setColor(jfcBlue);
                g2.setFont(new Font("serif", Font.BOLD, 18));
                g2.drawString("ERROR", 5, 20);
                AttributedString as = new AttributedString(errStr);
                as.addAttribute(TextAttribute.FONT, font12, 0, errStr.length());
                AttributedCharacterIterator aci = as.getIterator();
                FontRenderContext frc = g2.getFontRenderContext();
                LineBreakMeasurer lbm = new LineBreakMeasurer(aci, frc);
                float x = 5, y = 25;
                lbm.setPosition(0);
                while (lbm.getPosition() < errStr.length()) {
                    TextLayout tl = lbm.nextLayout(w - x - 5);
                    if (!tl.isLeftToRight()) {
                        x = w - tl.getAdvance();
                    }
                    tl.draw(g2, x, y += tl.getAscent());
                    y += tl.getDescent() + tl.getLeading();
                }
            } else if (capture.thread != null) {
                // paint during capture
                g2.setColor(Color.black);
                g2.setFont(font12);
                g2.drawString("Length: " + String.valueOf(seconds), 3, h - 4);
            } else {
                // paint during playback
                g2.setColor(Color.black);
                g2.setFont(font12);
                g2.drawString("Length: " + String.valueOf(duration) + "    Position: " + String.valueOf(seconds), 3, h - 4);

                if (audioInputStream != null) {
                    // .. render sampling graph ..
                    g2.setColor(jfcBlue);
                    for (int i = 1; i < lines.size(); i++) {
                        g2.draw((Line2D) lines.get(i));
                    }

                    // .. draw current position ..
                    if (seconds != 0) {
                        double loc = seconds / duration * w;
                        g2.setColor(pink);
                        g2.setStroke(new BasicStroke(3));
                        g2.draw(new Line2D.Double(loc, 0, loc, h - INFOPAD - 2));
                    }
                }
            }
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
                if ((playback.line != null) && (playback.line.isOpen())) {

                    long milliseconds = (long) (playback.line.getMicrosecondPosition() / 1000);
                    seconds = milliseconds / 1000.0;
                } else if ((capture.line != null) && (capture.line.isActive())) {

                    long milliseconds = (long) (capture.line.getMicrosecondPosition() / 1000);
                    seconds = milliseconds / 1000.0;
                }

                try {
                    thread.sleep(100);
                } catch (Exception e) {
                    break;
                }

                repaint();

                while ((capture.line != null && !capture.line.isActive()) || (playback.line != null && !playback.line.isOpen())) {
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
            public void run() {
                new Training_Form(true, true).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton captB;
    private javax.swing.JComboBox cblevel;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel lbgambar;
    private javax.swing.JButton pausB;
    private javax.swing.JButton playB;
    private javax.swing.JButton saveB;
    private javax.swing.JTextField txname;
    private javax.swing.JTextField txpath;
    // End of variables declaration//GEN-END:variables
}
