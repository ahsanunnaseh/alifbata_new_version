package com.views;

import javax.swing.*;
import java.awt.*;

/**
 * Represents the graph panel.
 *
 * @author aryosanjaya
 */
public class JVisualizer extends JPanel {
    private boolean reseted;
    private byte[] samples;
    private int xOffset;

    public JVisualizer() {
        this.xOffset = 0;
    }

    public JVisualizer(int xOffset) {
        this.xOffset = xOffset;
    }

    public void reset() {
        reseted = true;
        samples = null;

        revalidate();
        repaint();
    }

    public void setSamples(byte[] samples) {
        this.samples = samples;
        revalidate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (samples != null) {
            short left;
            short right;

            int j = 0;
            int yOffset = getHeight() / 2;
            float yFactor = (float)yOffset / 32768f;

            for (int i = 0; (i < samples.length) && (j < (getWidth() - (xOffset * 2))); i += 4) {
                left = (short)((samples[i + 1] << 8) + (samples[i] & 0xff));
                right = (short)((samples[i + 3] << 8) + (samples[i + 2] & 0xff));

                g.setColor(left < 300 ? Color.RED : left < 2000 ? Color.YELLOW : Color.GREEN);
                g.drawLine(j + xOffset, yOffset, j + xOffset, yOffset - (int)((float)Math.abs(left) * yFactor));
                g.setColor(right < 300 ? Color.RED : right < 2000 ? Color.YELLOW : Color.GREEN);
                g.drawLine(j + xOffset, yOffset, j + xOffset, yOffset + (int)((float)Math.abs(right) * yFactor));

                j++;
            }
        }

        if (reseted) {
            reseted = false;
            g.drawRect(-1, -1, getWidth() + 1, getHeight() + 1);
        }
    }
}
