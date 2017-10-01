/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arkanoid;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
/**
 *
 * @author raf
 */
public class Arkanoid implements KeyListener, MouseMotionListener {
    MyDrawPanel panel;
    int waitTime=7;
    int eps=5;
    int xBall=150, yBall=150;
    float vxBall=1.0f, vyBall=-1.0f;
    int xCursor=20, yCursor=250, lCursor=60, tCursor=10;
    int lBricks = 50, tBricks = 20;
    int gap = 5;
    final int sizeBall = 10;
    boolean isGameOver = false;
    boolean isPaused = false;
    boolean fly = false;
    
    // test
    int cols = 10;
    int rows = 5;
    int nBricks = cols * rows;
    int[][] xBricks = new int[rows][cols];
    int[][] yBricks = new int[rows][cols];
    boolean[][] alive = new boolean[rows][cols];
    Color[][] bricksColors = new Color[rows][cols];
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Arkanoid game = new Arkanoid(); 
        game.setup();
    }
    public void setup() {
        JFrame frame = new JFrame("Arkanoid");
        panel = new MyDrawPanel();
        frame.getContentPane().add(panel);
        frame.addKeyListener(this);
        frame.addMouseMotionListener(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(cols*(lBricks+gap)+gap, 300);
        frame.setVisible(true);
        initBricks();
        start();
    }
    void initBricks() {
        Random rand = new Random();
        for(int r=0; r < rows; r++) {
            for(int c=0; c < cols; c++) {
                alive[r][c] = true;
                bricksColors[r][c] = new Color(rand.nextInt(255), 
                                               rand.nextInt(255), 
                                               rand.nextInt(255));
                xBricks[r][c] = (lBricks+gap)*c;
                yBricks[r][c] = (tBricks+gap)*r;
            }
        }
        nBricks = cols * rows;
    }
    void start() {
        System.out.println(panel.getWidth() + " " + panel.getHeight());
        while (true) {
            if (!isGameOver && !isPaused) {
                // game over if touches bottom of screen
                if (yBall+vyBall > panel.getHeight()-sizeBall) {
                    gameOver();
                // bounce at top of screen
                } else if (yBall+vyBall <  0) {
                    vyBall *= -1.0f;
                // bounce at sides of screen
                } else if (xBall+vxBall < 0 || xBall+vxBall > panel.getWidth()-sizeBall) {
                    vxBall *= -1.0f;
                } else if (checkBallBounce(xCursor, yCursor, lCursor, tCursor)) {
                } else {
                    checkAllBricks();
                }
                
                xBall += (int) vxBall;
                yBall += (int) vyBall;
               
                panel.repaint();
            }
            try {
                Thread.sleep(waitTime);
            } catch (InterruptedException ex) {
                System.out.println("OW NO!");
            }
        }
    }
    void checkAllBricks() {
        for(int r=0; r < rows; r++) {
            for(int c=0; c < cols; c++) {
                if (alive[r][c]) {
                    if (checkBallBounce(xBricks[r][c], yBricks[r][c], lBricks, tBricks)) {
                        alive[r][c] = false;
                        if (--nBricks == 0) {
                            gameOver();
                        }
                        return;
                    }
                }
            }
        }
    }
    boolean checkBallBounce(int xTarget, int yTarget, int lTarget, int tTarget) {
    // bounce at top or bottom of cursor
    if ((xBall > xTarget+eps) && 
               (xBall < xTarget+lTarget-eps) &&
               (((yBall > yTarget-sizeBall) &&
                 (yBall < yTarget+eps)) ||
                ((yBall < yTarget+tTarget) &&
                 (yBall > yTarget+tTarget-eps)))) {
        vyBall *= -1.0f;
        return true;
    // bounce at sides of cursor
    } else if ((yBall > yTarget+eps) && 
               (yBall < yTarget+tTarget-eps) &&
               (((xBall > xTarget-sizeBall) &&
                 (xBall < xTarget+eps)) ||
                ((xBall < xTarget+lTarget+sizeBall) &&
                 (xBall > xTarget+lTarget-eps)))) {
        vxBall *= -1.0f;
        return true;
    // bounces at corners of cursor
    } else if ( ( (xBall > xTarget-sizeBall) &&  // top left corner
                  (xBall < xTarget+eps) &&
                  (yBall > yTarget-sizeBall) &&
                  (yBall < yTarget+eps) ) ||
                ( (xBall > xTarget+lTarget-eps) &&  // top right corner
                  (xBall < xTarget+lTarget-sizeBall) &&
                  (yBall > yTarget-sizeBall) &&
                  (yBall < yTarget+eps) ) ||
                ( (xBall > xTarget-sizeBall) &&  // bottom left corner
                  (xBall < xTarget+eps) &&
                  (yBall > yTarget+tTarget-eps) &&
                  (yBall < yTarget+tTarget+sizeBall) ) ||
                ( (xBall > xTarget+lTarget-eps) &&  // bottom right corner
                  (xBall < xTarget+lTarget-sizeBall) &&
                  (yBall > yTarget+tTarget-eps) &&
                  (yBall < yTarget+tTarget+sizeBall) )
               ) {
        vxBall *= -1.0f;
        vyBall *= -1.0f;
        return true;
    }
    return false;
}
    void gameOver() {
        System.out.println("Game over");
        isGameOver = true;
        panel.repaint();
    }
        // KeyPressed interface
    @Override
    public void keyPressed(KeyEvent ev) {
        switch (ev.getKeyCode()) {
            case KeyEvent.VK_R:
                // restart
                System.out.println("Restarted");
                xBall=150;
                yBall=150;
                vxBall=1;
                vyBall=-1;
                isGameOver = false;
                fly = false;
                initBricks();
                panel.repaint();
                break;
            case KeyEvent.VK_P:
                isPaused = !isPaused;
                panel.repaint();
                break;
            case KeyEvent.VK_F:
                fly = !fly;
                panel.repaint();
                break;
            default:
                break;
        }
    }
    @Override public void keyReleased(KeyEvent ev) {}
    @Override public void keyTyped(KeyEvent ev) {}
    
    // MouseListener interface
    @Override
    public void mouseMoved(MouseEvent e){
        xCursor = e.getPoint().x - 50; 
        if (fly) {
            yCursor = e.getPoint().y;
        }
    }
    @Override public void mouseDragged(MouseEvent e) {}
   
    // drawings
    class MyDrawPanel extends JPanel {
        @Override
        public void paintComponent(Graphics gfx) {
            if (isGameOver) {
                gfx.setColor(Color.orange);
                gfx.drawString("Game over", 200, 170);
                gfx.drawString("Press R to restart.", 50, 250);
            } else if (isPaused) {
                gfx.setColor(Color.orange);
                gfx.drawString("Game paused", 200, 170);
            } else {
                gfx.fillRect(0, 0, this.getWidth(), this.getHeight());
                
                gfx.setColor(Color.orange);
                gfx.drawString("vx " + String.format("%.02f", vxBall) + 
                               " | vy " + String.format("%.02f", vyBall) + 
                               " | left " + nBricks, 370, 250);
                
                gfx.setColor(Color.white);
                gfx.fillRect(xCursor, yCursor, lCursor, tCursor);
                
                for(int r=0; r < rows; r++) {
                    for(int c=0; c < cols; c++) {
                        if (alive[r][c]) {
                            gfx.setColor(bricksColors[r][c]);
                            gfx.fillRect(xBricks[r][c], yBricks[r][c], lBricks, tBricks);
                        }
                    }
                }
                
                gfx.setColor(Color.red);
                gfx.fillOval(xBall, yBall, sizeBall, sizeBall);
            }
            if (fly) {
                gfx.setColor(Color.red);
                gfx.drawString("weeeee", 50, 250);
            }
                
        }
    }
}
