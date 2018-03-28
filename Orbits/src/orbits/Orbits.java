/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orbits;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import java.util.Random;

/**
 *
 * @author raf
 */
public class Orbits implements KeyListener //, MouseMotionListener, MouseListener 
{

    MyDrawPanel panel;
    Planet[] planets;
    
    boolean debug = false;
    
    double totalMass;
    double[] cog = new double[2];
    
    boolean showOrbits = false;
    
    
    public static void main(String[] args) {
        System.out.println("main()");
        Orbits orbit = new Orbits();
        orbit.setup();
    }
    
    public void setup() {
        System.out.println("setup()");
        JFrame frame = new JFrame("Orbits");
        panel = new MyDrawPanel();
        frame.getContentPane().add(panel);
        frame.addKeyListener(this);
        //frame.addMouseMotionListener(this);
        //frame.addMouseListener(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setResizable(true);
        frame.setVisible(true);
        initPlanets();
        start();
    }
    
    private void initPlanets() {
        System.out.println("initPlanets()");
        
        rescale();
        
        planets = new Planet[2];
        planets[0] = new Planet("Sun", 1988500.0e24, 695700.0e3,
                                new double[] {0.0, 0.0},
                                new double[] {0.0, 0.0}, 50);
        planets[1] = new Planet("Earth", 5.9723e24, 6378.137e3,
                                 new double[] {0.0, 30.29e3}, 
                                 new double[] {-147.09e9, 0.0}, 400);
    }
    
    private void rescale() {
        int w = panel.getWidth();
        int h = panel.getHeight();
        Planet.drawScale = 299258415000.0 / h;
        Planet.drawShiftX = 10 + w/2;
        Planet.drawShiftY = 10 + h/2;
    }
    
    private void start() {
        System.out.println("start()");
        System.out.println(panel.getWidth() + " " + panel.getHeight());
        while (true) {
                
            for(Planet p: planets)
                for(Planet p1:planets)
                    if (p != p1)
                        p.updateOrbit(p1.mass, p1.position, 24*3600);
            
            panel.repaint();
            
            try {
                Thread.sleep(80);
            } catch (InterruptedException ex) {
                System.out.println("OW NO! WHAT NOW??");
            }
        }
    }
    
    // KeyPressed interface
    @Override
    public void keyPressed(KeyEvent ev) {
        System.out.print("Key pressed...");
        switch (ev.getKeyCode()) {
            case KeyEvent.VK_O:
                // restart
                System.out.println("O");
                showOrbits = !showOrbits;
                panel.repaint();
                break;
            default:
                System.out.println("not implemented");
                break;
        }
    }
    @Override public void keyReleased(KeyEvent ev) {}
    @Override public void keyTyped(KeyEvent ev) {}
    
    // drawings
    class MyDrawPanel extends JPanel {
        @Override
        public void paintComponent(Graphics gfx) {
            int w = this.getWidth();
            int h = this.getHeight();
            
            gfx.fillRect(0, 0, w, h);
            
            gfx.setColor(Color.orange);
            
            for (Planet p: planets) {
                if (debug) {
                    System.out.println("Drawing" + p.name);
                    System.out.println("   pos " + p.positionDraw[0] + " " + p.positionDraw[1]);
                    System.out.println("   rad " + p.radiusDraw);
                }
                gfx.fillOval(p.positionDraw[0], p.positionDraw[1],
                             p.radiusDraw, p.radiusDraw);
            }
            
            if (showOrbits) {
                gfx.setColor(Color.orange);
                for (Planet p: planets) {
                    for (int i=0; i < p.orbitPoints-1; i++) {
                        gfx.drawLine(p.orbitDraw[i][0], p.orbitDraw[i][1],
                                     p.orbitDraw[i+1][0], p.orbitDraw[i+1][1]);
                    }
                }
                        
            }     
        }
    }
}


class Planet {
    static final double G = 6.674e-11;
    static double drawScale;
    static int drawShiftX, drawShiftY;
    boolean debug = false;
    
    String name;
    double mass, radius;
    int radiusDraw;
    double[] velocity = new double[2];
    double[] position = new double[2];
    int[] positionDraw = new int[2];
    double[][] orbit = new double[5000][2];
    int[][] orbitDraw = new int[5000][2];
    int orbitPoints = 0;
    double drawExageration = 1;
    
    double[] u = new double[2];
    double distanceCoG;
    
    public Planet(String nm, double m, double r,
                  double[] v, double[] p, double f) {
        System.out.println("Planet() " + nm);
        name = nm;
        mass = m;
        radius = r;
        position = p;
        velocity = v;
        orbit[orbitPoints++] = position;
        positionDraw[0] = (int)(position[0]/drawScale) + drawShiftX;
        positionDraw[1] = (int)(position[1]/drawScale) + drawShiftY;
        orbitDraw[orbitPoints-1] = positionDraw;
        drawExageration = f;
        radiusDraw = (int)(drawExageration*radius/drawScale);
        System.out.println("   Position " + position[0] + " "+ position[1]);
        System.out.println("   Position Canvas " + positionDraw[0] + " "+ positionDraw[1]);
        
    }
    
    public void updateOrbit(double m, double[] cog, double dt) {
                       
        distanceCoG = sqrt(pow(position[0] - cog[0], 2) + 
                           pow(position[1] - cog[1], 2));
        
        u[0] = -(position[0] - cog[0]) / distanceCoG;
        u[1] = -(position[1] - cog[1]) / distanceCoG;
        
        double a = G * m / pow(distanceCoG, 2);
        
        velocity[0] += a*u[0]*dt;
        velocity[1] += a*u[1]*dt;
        position[0] += velocity[0]*dt; 
        position[1] += velocity[1]*dt;
        
        if (orbitPoints == 499)
            orbitPoints = 0;
        
        orbit[orbitPoints++] = position;
        
        positionDraw[0] = (int)(position[0]/drawScale) + drawShiftX;
        positionDraw[1] = (int)(position[1]/drawScale) + drawShiftY;
        orbitDraw[orbitPoints-1] = positionDraw;
        
        if (debug) {
            System.out.println("Updating orbit of " + name);
            System.out.println("   Dist " + distanceCoG);
            System.out.println("   u " + u[0] + " " + u[1]);
            System.out.println("   a " + a);
            System.out.println("   vel " + velocity[0] + " " + velocity[1]);
            System.out.println("   pos " + position[0] + " " + position[1]);
            System.out.println("   pos dwg " + positionDraw[0] + " " + positionDraw[1]);
        }
    }
}

