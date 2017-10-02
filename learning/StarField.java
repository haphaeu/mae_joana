/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
/**
 *
 * @author raf
 */
public class StarField {
    MyDrawPanel panel;
    int waitTime=7;
    // test
    int nDots;
    float[] x;
    float[] y;
    float[] vx;
    float[] vy;
    float speed = 1.0f;
    int w = 500, h = 300;  // width and height of panel
    //Color[][] bricksColors = new Color[rows][cols];
    Random rand = new Random();
    boolean showInfo = false;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int n = 100;
        if (args.length > 0) {
            n = Integer.parseInt(args[0]);
        }
        StarField game = new StarField(n); 
        game.setup();
    }
    StarField(int n) {
        nDots = n;
        x = new float[nDots];
        y = new float[nDots];
        vx = new float[nDots];
        vy = new float[nDots];
    }
    public void setup() {
        JFrame frame = new JFrame("Dot Chaos");
        panel = new MyDrawPanel();
        panel.addComponentListener(new ComponentAdapter() {
            @Override public void componentResized(ComponentEvent e) {
                w = panel.getWidth();
                h = panel.getHeight();
                System.out.println(w + " " + h);
            }
        });
        frame.addKeyListener(new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_R:
                        System.out.println("Restart dots.");
                        initDots();
                        break;
                    case KeyEvent.VK_W:
                        speed *= 1.1f;
                        updateSpeeds();
                        break;
                    case KeyEvent.VK_S:
                        speed /= 1.1f;
                        updateSpeeds();
                        break;
                    case KeyEvent.VK_A:
                        showInfo = !showInfo;
                        break;
                    default:
                        break;
                }
            }
        });
        frame.getContentPane().add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(w, h);
        frame.setVisible(true);
        initDots();
        start();
    }
    void initDots() {
        System.out.println("initDots()");
        for(int i=0; i < nDots; i++) {
            initDot(i);
        }
    }
    void initDot(int i) {
        x[i] = w * rand.nextFloat();
        y[i] = h * rand.nextFloat();
        initSpeed(i);
    }
    void initSpeed(int i) {
        float x0, y0;
        x0 = x[i] - w/2.0f;
        y0 = y[i] - h/2.0f;
        vx[i] = speed * x0 / (Math.abs(x0)+Math.abs(y0));
        vy[i] = speed * y0 / (Math.abs(x0)+Math.abs(y0)); 
    }
    void updateSpeeds() {
        System.out.println("updateSpeeds()");
        for(int i=0; i < nDots; i++) {
            initSpeed(i);
        }
    }
    void start() {
        while (true) {
            updateStars();
            panel.repaint();
            try {
                Thread.sleep(waitTime);
            } catch (InterruptedException ex) {
                System.out.println("OW NO!");
            }
        }
    }
    void updateStars() {
        for (int i=0; i < nDots; i++) {
            x[i] += vx[i];
            y[i] += vy[i];
            if (outOfScreen(i)) {
                initDot(i);
            }
        }
    }
    boolean outOfScreen(int i) {
        // bounce at top of screen
        if (y[i] <  0 || y[i] > h) {
            return true;
        // bounce at sides of screen
        } else if (x[i] < 0 || x[i] > w) {
            return true;
        }
        return false;
    }
    // drawings
    class MyDrawPanel extends JPanel {
        @Override
        public void paintComponent(Graphics gfx) {
            gfx.fillRect(0, 0, this.getWidth(), this.getHeight());
            gfx.setColor(Color.white);
            for(int i=0; i < nDots; i++) {
                gfx.fillOval((int) x[i], (int) y[i], 2, 2);
            }
            if (showInfo) {
                gfx.drawString("speed " + String.format("%.2f", speed), 10, 20);
                gfx.drawString("Press W/S to increase/decrease.", 10, 40);
            }
        }
    }
}
