import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Bounce2p implements KeyListener, MouseMotionListener {
    MyDrawPanel panel;
    int waitTime=7;
    int winner;
    int xBall=150, yBall=150;
    float vxBall=1.0f, vyBall=1.0f;
    int xCursor1=20, yCursor1=100, xCursor2=460, yCursor2=100, lCursor=60, tCursor=10;
    final int sizeBall = 10;
    boolean isGameOver = false;
    boolean isPaused = false;
    boolean showAuthor = false;
    
    public static void main(String[] args) {
        Bounce2p bounce = new Bounce2p(); 
        bounce.setup();
    }
    public void setup() {
        JFrame frame = new JFrame("Ping");
        panel = new MyDrawPanel();
        frame.getContentPane().add(panel);
        frame.addKeyListener(this);
        frame.addMouseMotionListener(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);
        frame.setVisible(true);
        start();
    } 
    void start() {
        System.out.println(panel.getWidth() + " " + panel.getHeight());
        boolean increaseSpeed = false;
        while (true) {
            if (!isGameOver && !isPaused) {
                // cheating computer player 2
                yCursor2 = yBall;
                
                // player 2 wins if touches left side of screen
                if (xBall < 0) {
                    winner = 2;
                    gameOver();
                // player 1 wins if touches right side of screen
                } else if (xBall+vxBall > panel.getWidth()-sizeBall) {
                    //vxBall *= -1;
                    winner = 1;
                    gameOver();
                // bounce at top and bottom of screen
                } else if (yBall+vyBall > panel.getHeight()-sizeBall || yBall+vyBall < 0) {
                    vyBall *= -1.0f;
                    increaseSpeed = true;
                // bounce at side of cursor 1
                } else if ((yBall > yCursor1-sizeBall) && 
                           (yBall < yCursor1+lCursor-sizeBall) &&
                           (xBall <= xCursor1+tCursor) && 
                           (xBall > xCursor1-sizeBall)) {
                    vxBall *= -1.0f;
                // bounce at side of cursor 2
                } else if ((yBall > yCursor2-sizeBall) && 
                           (yBall < yCursor2+lCursor-sizeBall) &&
                           (xBall >= xCursor2-sizeBall) && 
                           (xBall < xCursor2)) {
                    vxBall *= -1.0f;
                // bounce at top or bottom of cursor
                } else if ((xBall <= xCursor1+tCursor) && 
                           (xBall > xCursor1) &&
                           ((yBall > yCursor1-sizeBall) ||
                            (yBall < yCursor1+lCursor))) {
                    vyBall *= -1.0f;
                // bounce at corners
                } else if ((xBall > xCursor1-sizeBall) &&
                           (xBall < xCursor1-sizeBall/2) &&
                           ((yBall > yCursor1-sizeBall) &&
                            (yBall < yCursor1-sizeBall/2))
                          ) {
                    vyBall *= -1.0f;
                    vxBall *= -1.0f;
                }
                
                xBall += (int) vxBall;
                yBall += (int) vyBall;
                
                if (increaseSpeed) {
                    vxBall *= 1.05f;
                    vyBall *= 1.05f;
                    increaseSpeed=false;
                }
                
                panel.repaint();
            }
            try {
                Thread.sleep(waitTime);
            } catch (InterruptedException ex) {
                System.out.println("OW NO!");
            }
        }
    }
 
    void gameOver() {
        System.out.println("Game over - Player " + winner +  " wins!");
        isGameOver = true;
        panel.repaint();
    }
     
    // KeyPressed interface
    public void keyPressed(KeyEvent ev) {
        if (ev.getKeyCode() == KeyEvent.VK_W) {
            yCursor1 -= 10;
            if (yCursor1 < 1) yCursor1 = 1;
        } else if (ev.getKeyCode() == KeyEvent.VK_S) {
            yCursor1 += 10;
            if (yCursor1 > panel.getHeight()-lCursor) yCursor1 = panel.getHeight()-lCursor;
        } else if (ev.getKeyCode() == KeyEvent.VK_UP) {
            yCursor2 -= 10;
            if (yCursor2 < 1) yCursor2 = 1;
        } else if (ev.getKeyCode() == KeyEvent.VK_DOWN) {
            yCursor2 += 10;
            if (yCursor2 > panel.getHeight()-lCursor) yCursor2 = panel.getHeight()-lCursor;
        } else if (ev.getKeyCode() == KeyEvent.VK_R) { // restart
            System.out.println("Restarted");
            xBall=150; yBall=150; vxBall=1; vyBall=1;
            isGameOver = false;
            showAuthor = false;
            panel.repaint();
        } else if (ev.getKeyCode() == KeyEvent.VK_P) {
            isPaused = !isPaused;
            if (!isPaused) {showAuthor=false;}
            panel.repaint();
        } else if (ev.getKeyCode() == KeyEvent.VK_A) {
            if (isPaused || isGameOver) {
                showAuthor = true;
                panel.repaint();
            }
        }
    }
    public void keyReleased(KeyEvent ev) {}
    public void keyTyped(KeyEvent ev) {}
    
    // MouseListener interface
    public void mouseMoved(MouseEvent e){
        yCursor1 = e.getPoint().y - 50;  
        //yCursor1 = yCursor2;
    }
    public void mouseDragged(MouseEvent e) {}
    

    // drawings
    class MyDrawPanel extends JPanel {
        public void paintComponent(Graphics gfx) {
            if (isGameOver) {
                gfx.setColor(Color.orange);
                gfx.drawString("Game over", 200, 170);
                gfx.drawString(" --- Player " + winner + " wins! ---", 175, 150);
                gfx.drawString("Press R to restart.", 50, 250);
            } else if (isPaused) {
                gfx.setColor(Color.orange);
                gfx.drawString("Game paused", 200, 170);
            } else {
                gfx.fillRect(0, 0, this.getWidth(), this.getHeight());
                
                gfx.setColor(Color.orange);
                gfx.drawString("Player 1", 20, 15);
                gfx.drawString("Player 2", 420, 15);
                gfx.drawString("vx " + String.format("%.02f", vxBall) + 
                               " | vy " + String.format("%.02f", vyBall), 370, 250);
                
                gfx.setColor(Color.white);
                gfx.fillRect(xCursor1, yCursor1, tCursor, lCursor);
                
                gfx.setColor(Color.white);
                gfx.fillRect(xCursor2, yCursor2, tCursor, lCursor);
                
                gfx.setColor(Color.red);
                gfx.fillOval(xBall, yBall, sizeBall, sizeBall);
            }
            if (showAuthor) {
                gfx.setColor(Color.red);
                gfx.drawString("by Raf", 170, 70);
            }
                
        }
    }
}
