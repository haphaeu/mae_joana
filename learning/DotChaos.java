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
public class DotChaos {
    MyDrawPanel panel;
    int waitTime=7;
    // test
    int nDots;
    int[] x;
    int[] y;
    int[] vx;
    int[] vy;
    //Color[][] bricksColors = new Color[rows][cols];
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int n = 100;
        if (args.length > 0) {
            n = Integer.parseInt(args[0]);
        }
        DotChaos game = new DotChaos(n); 
        game.setup();
    }
    DotChaos(int n) {
        nDots = n;
        x = new int[nDots];
        y = new int[nDots];
        vx = new int[nDots];
        vy = new int[nDots];
    }
    public void setup() {
        JFrame frame = new JFrame("Dot Chaos");
        panel = new MyDrawPanel();
        panel.addComponentListener(new ComponentAdapter() {
            @Override public void componentResized(ComponentEvent e) {
                int w = panel.getWidth();
                int h = panel.getHeight();
                System.out.println(w + " " + h);
                // bring dots back to screen
                for(int i=0; i < nDots; i++) {
                    if (x[i] > w) { x[i] = w; }
                    if (y[i] > h) { y[i] = h; }
                }
            }
        });
        frame.addKeyListener(new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_R:
                        System.out.println("Restart dots.");
                        initDots();
                        break;
                    default:
                        break;
                }
            }
        });
        frame.getContentPane().add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);
        frame.setVisible(true);
        initDots();
        start();
    }
    void initDots() {
        Random rand = new Random();
        for(int i=0; i < nDots; i++) {
            x[i] = rand.nextInt(panel.getWidth());
            y[i] = rand.nextInt(panel.getHeight());
            vx[i] = 3 - rand.nextInt(6);    // this will give:
            vy[i] = 3 - rand.nextInt(6);    // 3, 2, 1, 0, -1, -2
            if (vx[i] == 0) { vx[i] = -3; } // and we don't want static dots,
            if (vy[i] == 0) { vy[i] = -3; } // nor pire horiz./vert. motion
            }
    }
    void start() {
        while (true) {
            updateSpeeds();
            panel.repaint();
            try {
                Thread.sleep(waitTime);
            } catch (InterruptedException ex) {
                System.out.println("OW NO!");
            }
        }
    }
    void updateSpeeds() {
        for(int i=0; i < nDots; i++) {
            checkBounce(i);
        }
    }
    void checkBounce(int i) {
        // bounce at top of screen
        if (y[i]+vy[i] <  0 || y[i]+vy[i] > panel.getHeight()) {
            vy[i] *= -1;
        // bounce at sides of screen
        } else if (x[i]+vx[i] < 0 || x[i]+vx[i] > panel.getWidth()) {
            vx[i] *= -1.0f;
        }
        x[i] += vx[i];
        y[i] += vy[i];
    }
    // drawings
    class MyDrawPanel extends JPanel {
        @Override
        public void paintComponent(Graphics gfx) {
            gfx.fillRect(0, 0, this.getWidth(), this.getHeight());
            gfx.setColor(Color.white);
            for(int i=0; i < nDots; i++) {
                gfx.fillOval(x[i], y[i], 2, 2);
            }
        }
    }
}
