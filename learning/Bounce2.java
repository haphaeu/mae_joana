import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Bounce2 implements KeyListener {
    MyDrawPanel panel;
    int waitTime=7;
    int xBall=150, yBall=150, vxBall=1, vyBall=1;
    int xCursor=20, yCursor=100, lCursor=60, tCursor=10;
    final int sizeBall = 10;
    boolean isGameOver = false;
    
    public static void main(String[] args) {
        Bounce2 bounce = new Bounce2(); 
        bounce.setup();
    }
    public void setup() {
        JFrame frame = new JFrame("Ping");
        panel = new MyDrawPanel();
        frame.getContentPane().add(panel);
        frame.addKeyListener(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);
        frame.setVisible(true);
        start();
    } 
    void start() {
        System.out.println(panel.getWidth() + " " + panel.getHeight());
        
        while (true) {
            if (!isGameOver) {
                // game over if touches left side of screen
                if (xBall < 0) {
                    gameOver();
                    // bounce at right side of screen
                } else if (xBall+vxBall > panel.getWidth()-sizeBall) {
                    
                    vxBall *= -1;
                    // bounce at top and bottom of screen
                } else if (yBall+vyBall > panel.getHeight()-sizeBall || yBall+vyBall < 0) {
                    vyBall *= -1;
                    // bounce at side of cursor
                } else if ((yBall > yCursor-sizeBall) && 
                           (yBall < yCursor+lCursor-sizeBall) &&
                           (xBall <= xCursor+tCursor) && 
                           (xBall > xCursor-sizeBall)) {
                    vxBall *= -1;
                    // bounce at top or bottom of cursor
                } else if ((xBall <= xCursor+tCursor) && 
                           (xBall > xCursor) &&
                           ((yBall > yCursor-sizeBall) ||
                            (yBall < yCursor+lCursor))) {
                    vyBall *= -1;
                    // bounce at corners
                } else if ((xBall > xCursor-sizeBall) &&
                           (xBall < xCursor-sizeBall/2) &&
                           ((yBall > yCursor-sizeBall) &&
                            (yBall < yCursor-sizeBall/2))
                          ) {
                    vyBall *= -1;
                    vxBall *= -1;
                }
                
                xBall += vxBall;
                yBall += vyBall;
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
        System.out.println("Game over");
        isGameOver = true;
        panel.repaint();
    }
     
    public void keyPressed(KeyEvent ev) {
        if (ev.getKeyCode() == KeyEvent.VK_UP) {
            yCursor -= 10;
            if (yCursor < 1) yCursor = 1;
        } else if (ev.getKeyCode() == KeyEvent.VK_DOWN) {
            yCursor += 10;
            if (yCursor > panel.getHeight()-lCursor) yCursor = panel.getHeight()-lCursor;
        } else if (ev.getKeyCode() == KeyEvent.VK_R) { // restart
            System.out.println("Restarted");
            xBall=150; yBall=150; vxBall=1; vyBall=1;
            isGameOver = false;
            panel.repaint();
        }
    }

    public void keyReleased(KeyEvent ev) {}
    public void keyTyped(KeyEvent ev) {}

    class MyDrawPanel extends JPanel {
        public void paintComponent(Graphics gfx) {
            if (isGameOver) {
                gfx.setColor(Color.orange);
                gfx.drawString("Game over!", 100, 100);
            } else {
                gfx.fillRect(0, 0, this.getWidth(), this.getHeight());
                
                gfx.setColor(Color.white);
                gfx.fillRect(xCursor, yCursor, tCursor, lCursor);
                
                gfx.setColor(Color.red);
                gfx.fillOval(xBall, yBall, sizeBall, sizeBall);
            }
        }
    }
}
