/*
 * Orbits
 *
 * Draws the orbits of planets around the sun.
 *
 * Calculations are done using classic Newton mechanics with planet data
 * from NASA
 * https://nssdc.gsfc.nasa.gov/planetary/factsheet/
 *
 * Calculations are done in real scale and scaled to fit canvas. Need to find a better way of
 * doing that, also implement zoom.
 *
 */
package orbits;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import static java.lang.Math.max;
import static java.lang.Math.abs;

/**
 *
 * @author raf
 */
public class Orbits implements KeyListener, 
                               MouseMotionListener, 
                               MouseWheelListener {

    MyDrawPanel panel;
    Planet[] planets;
    
    boolean debug = false;
    boolean paused = false;
    int timer = 100;
    
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
        frame.addMouseMotionListener(this);
        frame.addMouseWheelListener(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 1000);
        frame.setResizable(true);
        frame.setVisible(true);
        initPlanets();
        paused = true;
        start();
    }
    
    private void initPlanets() {
        System.out.println("initPlanets()");
        
        rescale();
        
        // Planet datasheets from:
        // https://nssdc.gsfc.nasa.gov/planetary/factsheet/
        planets = new Planet[5];
        planets[0] = new Planet("Sun", 1988500.0e24, 695700.0e3, 
                                new double[] {0.0, 0.0}, 
                                new double[] {0.0, 0.0},
                                Color.orange);
        planets[1] = new Planet("Earth", 5.9723e24, 6378.137e3,
                                new double[] {0.0, 30.29e3}, 
                                new double[] {-147.09e9, 0.0},
                                Color.blue);
        planets[2] = new Planet("Venus", 4.8675e24, 6051.8e3,
                                new double[] {0.0, -34.79e3},
                                new double[] {108.94e9, 0.0},
                                Color.yellow);
        planets[3] = new Planet("Mercury", 0.33011e24, 2439.7e3,
                                new double[] {38.86e3, 0.0}, 
                                new double[] {0.0, 69.82e9},
                                Color.red);
        planets[4] = new Planet("Mars", 0.64171e24, 3396.2e3,
                                new double[] {-21.97e3, 0.0},
                                new double[] {0.0, -249.23e9}, 
                                Color.green);
        // can't really see the moon at this scale...
        // need to implement zoon =)
//        planets[5] = new Planet("Moon", 0.07346e24, 1738.1e3,
//                                 new double[] {-0.970e3, 30.29e3}, 
//                                 new double[] {-147.09e9, 0.4055e9}, 
//                                 Color.gray);
    }
    
    private void rescale() {
        int w = panel.getWidth();
        int h = panel.getHeight();
        Planet.drawScale = 2 * 300e9 / h;
        Planet.drawShiftX = 10 + w/2;
        Planet.drawShiftY = 10 + h/2;
    }
    
    private void start() {
        System.out.println("start()");
        System.out.println(panel.getWidth() + " " + panel.getHeight());
        long t0, proc_time;
        while (true) {
            t0 = System.nanoTime();
            if (!paused) {
                // First update the speed of all planets
                for(Planet p: planets)
                    p.updateVelocity(planets);
                // and then update their positions
                for(Planet p: planets)
                    p.updateOrbit();
            }
            panel.repaint();
            proc_time = (int)((System.nanoTime() - t0) / 1e6);
            try {
                Thread.sleep(timer - Math.min(timer, proc_time));
            } catch (InterruptedException ex) {
                System.out.println("OW NO! WHAT NOW??");
            }
        }
    }
    
    // KeyPressed interface
    @Override
    public void keyPressed(KeyEvent ev) {
        System.out.print("Key pressed: ");
        switch (ev.getKeyCode()) {
            case KeyEvent.VK_O:
                // restart
                System.out.println("O");
                showOrbits = !showOrbits;
                System.out.println("   showOrbit " + showOrbits);
                panel.repaint();
                break;
            case KeyEvent.VK_E:
                System.out.println("E");
                System.out.println("   erase orbits ");
                for (Planet p: planets)
                    p.orbitPoints = 0;
                panel.repaint();
                break;
            case KeyEvent.VK_P:
                System.out.println("P");
                paused = !paused;
                System.out.println("   paused " + paused);
                break;
            case KeyEvent.VK_W:
                System.out.println("W");
                System.out.println("   increase Earth orbital velocity");
                planets[0].velocity[0] *= 1.1;
                planets[1].velocity[0] *= 1.1;
                break;
            case KeyEvent.VK_S:
                System.out.println("S");
                System.out.println("   decrease Earth orbital velocity");
                planets[0].velocity[0] /= 1.1;
                planets[1].velocity[0] /= 1.1;
                break;
            default:
                System.out.println("not implemented");
                break;
        }
    }
    @Override public void keyReleased(KeyEvent ev) {}
    @Override public void keyTyped(KeyEvent ev) {}
    
    // MouseMotionListener interface
    int mouseX, mouseY;
    @Override
    public void mouseMoved(MouseEvent e){
        mouseX = e.getPoint().x; 
        mouseY = e.getPoint().y;
    }
    @Override public void mouseDragged(MouseEvent e) {}
   
    // MouseWheelInterface
    @Override public void mouseWheelMoved(MouseWheelEvent e) {
       int notches = e.getWheelRotation();
       if (notches > 0)
           Planet.drawScale *= 1.1;
       else if (notches < 0)
           Planet.drawScale /= 1.1;
       Planet.drawShiftX = (2*Planet.drawShiftX + mouseX) / 3;
       Planet.drawShiftY = (2*Planet.drawShiftY + mouseY) / 3;
       for (Planet p: planets)
           p.rescaleOrbitDraw();
    }
    
    // drawings
    class MyDrawPanel extends JPanel {
        @Override
        public void paintComponent(Graphics gfx) {
            int w = this.getWidth();
            int h = this.getHeight();
            
            gfx.fillRect(0, 0, w, h);

            for (Planet p: planets) {
                if (debug) {
                    System.out.println("Drawing" + p.name);
                    System.out.println("   pos " + p.positionDraw[0] + " " + p.positionDraw[1]);
                    System.out.println("   rad " + p.radiusDraw);
                }
                gfx.setColor(p.color);
                gfx.fillOval(p.positionDraw[0]-p.radiusDraw, p.positionDraw[1]-p.radiusDraw,
                             2*p.radiusDraw, 2*p.radiusDraw);
                if ( (abs(mouseX - p.positionDraw[0]) < 50) &&
                     (abs(mouseY - p.positionDraw[1]) < 50))
                    gfx.drawString(p.name, 10, 10);

            }

            if (showOrbits) {
                if (debug) System.out.println("Showing orbits");
                gfx.setColor(Color.gray);
                for (Planet p: planets) {
                    if (debug) System.out.println("  " + p.name);
                    for (int i=0; i < p.orbitPoints-1; i++) {
                        if (debug)
                            System.out.println("   " + p.orbitDraw[i][0] + "   " + p.orbitDraw[i][1]);
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
    static double dt = 24*3600;
    
    boolean debug = false;
    
    String name;
    double mass, radius;
    int radiusDraw;
    double[] velocity = new double[2];
    double[] position = new double[2];
    int[] positionDraw = new int[2];
    final int size = 5000;
    double[][] orbit;
    int[][] orbitDraw;
    int orbitPoints;
    Color color;
    
    double[] u = new double[2];
    double distance;
    
    public Planet(String nm, double m, double r,
                  double[] v, double[] p, Color c) {
        System.out.println("Planet() " + nm);
        name = nm;
        mass = m;
        radius = r;
        position = p;
        velocity = v;
        color = c;
        
        orbit = new double[size][2];
        orbitDraw = new int[size][2];
        orbitPoints = 0;
        orbit[orbitPoints][0] = position[0];
        orbit[orbitPoints][1] = position[1];
        positionDraw[0] = (int)(position[0]/drawScale) + drawShiftX;
        positionDraw[1] = (int)(position[1]/drawScale) + drawShiftY;
        orbitDraw[orbitPoints][0] = positionDraw[0];
        orbitDraw[orbitPoints][1] = positionDraw[1];
        orbitPoints++;
        radiusDraw = max(8, 2*(int)(radius/drawScale));
        System.out.println("   Position " + position[0] + " "+ position[1]);
        System.out.println("   Position Canvas " + positionDraw[0] + " "+ positionDraw[1]);
        
    }
    
    public void updateVelocity(Planet[] planets) {
        if (debug)
            System.out.println("Updating velocity of " + name);
        double a;
        for (Planet p: planets) {
            if (p != this) {
                if (debug)
                    System.out.println("   with " + p.name);
                
                distance = sqrt(pow(position[0] - p.position[0], 2) + 
                                   pow(position[1] - p.position[1], 2));
        
                // Unit vector pointing towards the other planet
                u[0] = -(position[0] - p.position[0]) / distance;
                u[1] = -(position[1] - p.position[1]) / distance;
        
                // Classic Newton mechanic:
                // F = m1.a = G.m1.m2.a/r² ==> a = G.m2/r²
                a = G * p.mass / pow(distance, 2);
        
                velocity[0] += a*u[0]*dt;
                velocity[1] += a*u[1]*dt;
            }
        }
    }
    public void updateOrbit() {
        
        position[0] += velocity[0]*dt; 
        position[1] += velocity[1]*dt;
        
        // Check if need to re-allocate orbit arrays
        if (orbitPoints == orbit.length) {
            if (debug) System.out.println("Increasing orbit array size...");
            double[][] tmp = new double[orbit.length + size][2];
            int[][] tmp1 = new int[orbitDraw.length + size][2];
            System.arraycopy(orbit, 0, tmp, 0, orbit.length);
            System.arraycopy(orbitDraw, 0, tmp1, 0, orbitDraw.length);
            orbit = tmp;
            orbitDraw = tmp1;
        }
        
        orbit[orbitPoints][0] = position[0];
        orbit[orbitPoints][1] = position[1];
        
        positionDraw[0] = (int)(position[0]/drawScale) + drawShiftX;
        positionDraw[1] = (int)(position[1]/drawScale) + drawShiftY;
        orbitDraw[orbitPoints][0] = positionDraw[0];
        orbitDraw[orbitPoints][1] = positionDraw[1];
        orbitPoints++;
        
        if (debug) {
            System.out.println("Updating orbit of " + name);
            System.out.println("   Dist " + distance);
            System.out.println("   u " + u[0] + " " + u[1]);
            System.out.println("   vel " + velocity[0] + " " + velocity[1]);
            System.out.println("   pos " + position[0] + " " + position[1]);
            System.out.println("   pos dwg " + positionDraw[0] + " " + positionDraw[1]);
        }
    }
    public void rescaleOrbitDraw() {
        for (int i=0; i < orbit.length; i++) {
            orbitDraw[i][0] = (int)(orbit[i][0]/drawScale) + drawShiftX;
            orbitDraw[i][1] = (int)(orbit[i][1]/drawScale) + drawShiftY;
        }
    }
}

