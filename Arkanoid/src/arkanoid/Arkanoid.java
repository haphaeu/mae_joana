/**
 * Very very simple Arkanoid game, with some tricks.
 * 
 * Bugs:
 *  - contact at corners still not stable
 * 
 * Features:
 *  - work on the cursor drawing - make fancy corners :)
 * 
 * @author raf
 */

package arkanoid;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

enum BrickColor {
    Yellow(1, 255, 0, 255), Green(2, 0, 255, 0), Orange(3, 255, 255, 0),
    Red(4, 255, 0, 0), Blue(5, 0, 0, 255), White(99, 255, 255, 255);
    private final int points;
    private final Color color;
    private BrickColor(int p, int r, int g, int b) {
        points = p;
        color = new Color(r, g, b);
    }
    public int getPoints() {
        return points;
    }
    public Color getColor() {
        return color;
    }   
}

public class Arkanoid implements KeyListener, MouseMotionListener, MouseListener {
    MyDrawPanel panel;
    int waitTime=5;
    int eps=5;
    float xBall=250.0f, yBall=250.0f;
    float vxBall=0.0f, vyBall=0.0f;
    int xCursor=250, yCursor=300, lCursor=60, tCursor=10;
    int lBricks = 50, tBricks = 20;
    int gap = 5;
    final int sizeBall = 10;
    boolean isGameOver = false;
    boolean isPaused = false;
    boolean isGameStarted = false;
    boolean godMode = false;
    boolean fly = false;
    
    int cols = 10;
    int rows = 5;
    int nBricks, totalPoints;
    int[][] xBricks = new int[rows][cols];
    int[][] yBricks = new int[rows][cols];
    int[][] points = new int[rows][cols];
    boolean[][] alive = new boolean[rows][cols];
    BrickColor[][] bricksColors = new BrickColor[rows][cols];
    
    Random rand = new Random();
    
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
        frame.addMouseListener(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(cols*(lBricks+gap)+gap, 350);
        frame.setResizable(false);
        frame.setVisible(true);
        initBricks();
        start();
    }
    
    void initBricks() {
        totalPoints = 0;
        for(int r=0; r < rows; r++) {
            for(int c=0; c < cols; c++) {
                alive[r][c] = true;
                xBricks[r][c] = (lBricks+gap)*c;
                yBricks[r][c] = (tBricks+gap)*r;
                // here the "level" must be built
                // colors are implemented from 1 to 5
                points[r][c] = 5-r;  
                
                totalPoints += points[r][c];
                bricksColors[r][c] = setupColor(points[r][c]);
            }
        }
        nBricks = cols * rows;
    }
    
    private BrickColor setupColor(int points) {
        switch (points) {
            case 1:
                return BrickColor.Yellow;
            case 2:
                return BrickColor.Green;
            case 3:
                return BrickColor.Orange;
            case 4:
                return BrickColor.Red;
            case 5:
                return BrickColor.Blue;
            default:
                return BrickColor.White;
        }
    }
    
    void start() {
        System.out.println(panel.getWidth() + " " + panel.getHeight());
        while (true) {
            if (!isGameOver && !isPaused) {
                // game over if touches bottom of screen
                if (yBall+vyBall > panel.getHeight()-sizeBall) {
                    if (!godMode) {
                        gameOver();
                    } else {
                        vyBall *= -1;
                    }
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
                
                xBall += vxBall;
                yBall += vyBall;
               
                panel.repaint();
            }
            try {
                Thread.sleep(waitTime);
            } catch (InterruptedException ex) {
                System.out.println("OW NO! WHAT NOW??");
            }
        }
    }
    
    void checkAllBricks() {
        for(int r=0; r < rows; r++) {
            for(int c=0; c < cols; c++) {
                if (alive[r][c]) {
                    if (checkBallBounce(xBricks[r][c], yBricks[r][c], lBricks, tBricks)) {
                        // when ball hits a brick, the brick loses 1 point
                        totalPoints--;
                        bricksColors[r][c] = setupColor(--points[r][c]);
                        if (points[r][c] == 0) {
                            alive[r][c] = false;
                            nBricks--;
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
            if (!godMode) {
                vyBall *= -1.0f;
            }
            return true;
        // bounce at sides of cursor
        } else if ((yBall > yTarget+eps) && 
                   (yBall < yTarget+tTarget-eps) &&
                   (((xBall > xTarget-sizeBall) &&
                     (xBall < xTarget+eps)) ||
                    ((xBall < xTarget+lTarget+sizeBall) &&
                     (xBall > xTarget+lTarget-eps)))) {
            if (!godMode) {
                vxBall *= -1.0f;
            }
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
            if (!godMode) {
                vxBall *= -1.0f + (rand.nextFloat()/2.0f - 0.25f); // +/-0.25 randomly
                vyBall *= -1.0f + (rand.nextFloat()/2.0f - 0.25f);
            }
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
        System.out.print("Key pressed...");
        switch (ev.getKeyCode()) {
            case KeyEvent.VK_R:
                // restart
                System.out.println("restart");
                xBall         = 250.0f;
                yBall         = 250.0f;
                xCursor       = 250;
                yCursor       = 300;
                vxBall        = 0.0f;
                vyBall        = 0.0f;
                isGameStarted = false;
                isGameOver    = false;
                fly           = false;
                initBricks();
                panel.repaint();
                break;
            case KeyEvent.VK_P:
                System.out.println("pause");
                isPaused = !isPaused;
                panel.repaint();
                break;
            case KeyEvent.VK_F:
                System.out.println("weeeee");
                fly = !fly;
                panel.repaint();
                break;
            case KeyEvent.VK_G:
                System.out.println("funny things...");
                godMode = !godMode;
            default:
                System.out.println("not implemented");
                break;
        }
    }
    @Override public void keyReleased(KeyEvent ev) {}
    @Override public void keyTyped(KeyEvent ev) {}
    
    // MouseMotionListener interface
    @Override
    public void mouseMoved(MouseEvent e){
        if (isGameStarted) {
        xCursor = e.getPoint().x - 50; 
            if (fly) {
                yCursor = e.getPoint().y;
            }
        }
    }
    @Override public void mouseDragged(MouseEvent e) {}
   
    // MouseInterface
    @Override public void mouseClicked​(MouseEvent e) {
        System.out.println("Mouse clicked.");
        if (!isGameStarted) {
            vxBall = 0.66f;
            vyBall = -1.0f;
            isGameStarted = true;
        } else {
            isPaused = !isPaused;
            panel.repaint();
        }
    }
    @Override public void mouseEntered​(MouseEvent e) {}
    @Override public void mouseExited​(MouseEvent e)  {}
    @Override public void mousePressed​(MouseEvent e)  {}
    @Override public void mouseReleased​(MouseEvent e) {}
            
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
                gfx.drawString(" vx " + String.format("%.2f", vxBall) + 
                               " vy " + String.format("%.2f", vyBall) +
                               "  v " + String.format("%.2f", Math.sqrt(vxBall*vxBall+vyBall*vyBall)), 10, 320); 
                gfx.drawString("Remaining: " + nBricks + " bricks | " + 
                               totalPoints + " hit points", 350, 320);
                
                gfx.setColor(Color.white);
                gfx.fillRect(xCursor, yCursor, lCursor, tCursor);
                
                for(int r=0; r < rows; r++) {
                    for(int c=0; c < cols; c++) {
                        if (alive[r][c]) {
                            gfx.setColor(bricksColors[r][c].getColor());
                            gfx.fillRect(xBricks[r][c], yBricks[r][c], lBricks, tBricks);
                        }
                    }
                }
                
                gfx.setColor(Color.red);
                gfx.fillOval((int)xBall, (int)yBall, sizeBall, sizeBall);
                
                if (nBricks == 0) {
                    gameOver();
                }
                
                if (!isGameStarted) {
                    gfx.setColor(Color.orange);
                    gfx.drawString("Push mouse button to start", 200, 170);
                }
            }
            if (fly) {
                gfx.setColor(Color.red);
                gfx.drawString("weeeee", 50, 320);
            }     
        }
    }
}
