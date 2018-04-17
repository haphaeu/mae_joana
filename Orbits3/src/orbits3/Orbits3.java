/*
 * Orbits2
 *
 * Planets orbiting around each other, or colliding =)
 *
 * Calculations are done using classic Newton mechanics.
 *
 *
 */
package orbits3;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import static java.lang.Math.abs;
import static java.lang.Math.atan;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import java.util.ArrayList;

/**
 *
 * @author raf
 */
public class Orbits3 implements KeyListener, 
                                MouseListener,
                                MouseMotionListener, 
                                MouseWheelListener {

    MyDrawPanel panel;
    ArrayList<Planet1> planets = new ArrayList<>();
    
    boolean debug = false;
    boolean paused = false;
    int timer = 10;
    
    boolean showOrbits = true;
    boolean addingPlanetMode = false;
    
    double scale;
    int shiftX, shiftY;
    
    double newPlanetX, newPlanetY;
    
    public static void main(String[] args) {
        System.out.println("main()");
        Orbits3 orbit = new Orbits3();
        orbit.setup();
    }
    
    public void setup() {
        System.out.println("setup()");
        JFrame frame = new JFrame("Orbits");
        panel = new MyDrawPanel();
        frame.getContentPane().add(panel);
        frame.addKeyListener(this);
        frame.addMouseListener(this);
        frame.addMouseMotionListener(this);
        frame.addMouseWheelListener(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 1000);
        frame.setResizable(true);
        frame.setVisible(true);
        init();
        start();
    }
    
    private void init() {
        System.out.println("init()");
        
        int w = panel.getWidth();
        int h = panel.getHeight();
        scale = 15000.0;
        shiftX = w/2;
        shiftY = h/2;
        
        planets.add(new Planet1("earth",
                               5.9723e24, 
                               6378.137e3,
                               new double[] {0, 0}, 
                               new double[] {0, 0},
                               Color.blue));
        
            // Init all planets at north pole
        newPlanetX = 0; 
        newPlanetY = -6378.137e3;
 
    }
    private void addPlanet() {
        planets.add(new Planet1("nameless",
                               100, 
                               50000,
                               new double[] {velX, velY}, 
                               new double[] {newPlanetX, newPlanetY},
                               Color.red));
    }
    
    
    private void start() {
        System.out.println("start()");
        System.out.println(panel.getWidth() + " " + panel.getHeight());
        while (true) {
            
            if (!paused) {
                // First update the speed of all planets
                planets.forEach((p) -> {
                    p.updateVelocity(planets.get(0));
                });
                // and then update their positions
                planets.forEach((p) -> {
                    p.updateOrbit();
                });
                checkCollisions();
            }
            panel.repaint();
            try {
                Thread.sleep(timer);
            } catch (InterruptedException ex) {
                System.out.println("OW NO! WHAT NOW??");
            }
        }
    }
    private void checkCollisions() {
        Planet1 pj = planets.get(0);  // Earth
        for (int i=1; i < planets.size(); i++) {
            Planet1 pi = planets.get(i);
            double dist = sqrt(pow(pi.position[0] - pj.position[0], 2) +
                               pow(pi.position[1] - pj.position[1], 2));
                if (dist < pj.radius) {
                    pi.chibakuTensei();
                }
        }
    }
    
    // KeyPressed interface
    @Override
    public void keyPressed(KeyEvent ev) {
        System.out.print("Key pressed: ");
        switch (ev.getKeyCode()) {
            case KeyEvent.VK_A:
                if (!addingPlanetMode) {
                    System.out.println("A");
                    System.out.println("   adding planet");
                    newPlanetSpeedX = newPlanetX;
                    newPlanetSpeedY = newPlanetY;
                    addingPlanetMode = true;
                    paused = true;
                }
                break;
            case KeyEvent.VK_R:
                System.out.println("R");
                System.out.println("   remove last added planet");
                if (planets.size() > 1)
                    planets.remove(planets.size()-1);
                break;
            case KeyEvent.VK_O:
                System.out.println("O");
                showOrbits = !showOrbits;
                System.out.println("   showOrbit " + showOrbits);
                panel.repaint();
                break;
            case KeyEvent.VK_E:
                System.out.println("E");
                System.out.println("   erase orbits ");
                planets.forEach((p) -> { p.orbitPoints = 0; });
                panel.repaint();
                break;
            case KeyEvent.VK_P:
                System.out.println("P");
                paused = !paused;
                System.out.println("   paused " + paused);
                break;
            case KeyEvent.VK_UP:
                System.out.println("up");
                System.out.println("   scroll up ");
                shiftY -= 10;
                break;
            case KeyEvent.VK_DOWN:
                System.out.println("down");
                System.out.println("   scroll down ");
                shiftY += 10;
                break;
            case KeyEvent.VK_LEFT:
                System.out.println("left");
                System.out.println("   scroll left ");
                shiftX -= 10;
                break;
            case KeyEvent.VK_RIGHT:
                System.out.println("right");
                System.out.println("   scroll right ");
                shiftX += 10;
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
    double newPlanetSpeedX, newPlanetSpeedY;
    double velX, velY;
    @Override
    public void mouseMoved(MouseEvent e){
        mouseX = e.getPoint().x - 1; 
        mouseY = e.getPoint().y - 24;
        
        if (addingPlanetMode) {
            newPlanetSpeedX = (mouseX-shiftX)*scale;
            newPlanetSpeedY = (mouseY-shiftY)*scale;
            if (abs(newPlanetSpeedX - newPlanetX) >= 10*scale)
                velX = (newPlanetSpeedX - newPlanetX) / 1e3;
            else
                velX = 0;
            if (abs(newPlanetSpeedY - newPlanetY) >= 10*scale)
                velY = (newPlanetSpeedY - newPlanetY) / 1e3;
            else
                velY = 0;
        }
    }
   
    // MouseWheelInterface
    @Override public void mouseWheelMoved(MouseWheelEvent e) {
        if (!addingPlanetMode) {
            int cX = (int)((mouseX-shiftX)*scale);
            int cY = (int)((mouseY-shiftY)*scale);
            
            int notches = e.getWheelRotation();
            if (notches > 0)
                scale *= 1.1;
            else if (notches < 0)
                scale /= 1.1;
            
            shiftX = mouseX - (int)(cX/scale);
            shiftY = mouseY - (int)(cY/scale);
            System.out.println("Rescale " + scale);
            System.out.println(" Shift " + shiftX + " " + shiftY);
        }
    }

    // MouseListener
    
    @Override public void mouseClicked(MouseEvent me) {
        if (addingPlanetMode){
            addingPlanetMode = false;
            paused = false;
            addPlanet();
        }
    }

    int panX, panY;
    boolean panMode = false;
    @Override public void mousePressed(MouseEvent me) {
        if (!addingPlanetMode) {
            panMode = true;
            panX = me.getX();
            panY = me.getY();
        }
    }
    @Override public void mouseDragged(MouseEvent e) {
        if (panMode) {
            shiftX += e.getX() - panX;
            shiftY += e.getY() - panY;
            panX = e.getX();
            panY = e.getY();
        }
    }
    @Override public void mouseReleased(MouseEvent me) {
        if (panMode) {
            panMode = false;
        }
    }

    @Override public void mouseEntered(MouseEvent me) {}

    @Override public void mouseExited(MouseEvent me) {}
    
    // drawings
    class MyDrawPanel extends JPanel {
        @Override
        public void paintComponent(Graphics gfx) {
            
            /*
             real_world = (screen-shift)*scale
             screen = real/scale + shift
            */
            
            
            int w = this.getWidth();
            int h = this.getHeight();
            
            gfx.fillRect(0, 0, w, h);
            gfx.setColor(Color.white);
            gfx.drawString("Mouse " + mouseX + " " + mouseY, 10, 10);
            gfx.drawString(String.format("Scale %.1f", scale), 10, 25);

            for (Planet1 p: planets) {
                if (debug) {
                    System.out.println("Drawing" + p.name);
                    System.out.println("   pos " + p.position[0] + " " + p.position[1]);
                    System.out.println("   rad " + p.radius);
                }
                gfx.setColor(p.color);
                int x = (int)((p.position[0]-p.radius)/scale + shiftX);
                int y = (int)((p.position[1]-p.radius)/scale + shiftY);
                int r = (int)(p.radius/scale);
                gfx.fillOval(x, y, 2*r, 2*r);
            }
            
            if (addingPlanetMode) {
                gfx.setColor(Color.gray);
                gfx.drawString(String.format("Velocity vx=%.0f, vy=%.0f, v=%.0f, theta=%.1f",
                        velX, velY, sqrt(velX*velX+velY*velY), 57.2957795131*atan(-velY/velX)),
                        10, 40);
                gfx.drawLine((int)(newPlanetX/scale)+shiftX,
                             (int)(newPlanetY/scale)+shiftY,
                             (int)(newPlanetSpeedX/scale)+shiftX,
                             (int)(newPlanetSpeedY/scale)+shiftY);
            }

            if (showOrbits) {
                //if (debug) System.out.println("Showing orbits");
                gfx.setColor(Color.gray);
                for (Planet1 p: planets) {
                    //if (debug) System.out.println("  " + p.name);
                    for (int i=0; i < p.orbitPoints-1; i++) {
                        //if (debug)
                        //    System.out.println("   " + p.orbit[i][0] + "   " + p.orbit[i][1]);
                        gfx.drawLine((int)(p.orbit[i  ][0]/scale + shiftX), 
                                     (int)(p.orbit[i  ][1]/scale + shiftY),
                                     (int)(p.orbit[i+1][0]/scale + shiftX),
                                     (int)(p.orbit[i+1][1]/scale + shiftY));
                    }
                }
            }
        }
    }
}

/*
 * Deviation here
 * Planets are onlly checked against the Earth
 *
 *
 */
class Planet1 {
    static final double G = 6.674e-11;
    //static final double G = 1.0e-3;
    static double dt = 10;
    
    boolean debug = false;
    private boolean hasCollided = false;
    
    String name;
    double mass, radius;
    double[] velocity = new double[2];
    double[] position = new double[2];
    final int size = 5000;
    double[][] orbit;
    int orbitPoints;
    Color color;
    
    double[] u = new double[2];
    double distance;
    
    public Planet1(String nm, double m, double r,
                  double[] v, double[] p, Color c) {
        System.out.println("new Planet() ");
        name = nm;
        mass = m;
        radius = r;
        position = p;
        velocity = v;
        color = c;
        
        orbit = new double[size][2];
        orbitPoints = 0;
        orbit[orbitPoints][0] = position[0];
        orbit[orbitPoints][1] = position[1];
        orbitPoints++;
        System.out.println("   Position " + position[0] + " "+ position[1]);
        System.out.println("   Mass " + mass);
        System.out.println("   Radius " + radius);
        System.out.println("   Speed " + velocity[0] + " " + velocity[1]);
    }
    
    public void updateVelocity(Planet1 p /*Earth*/) {
        if (!hasCollided) {
            double a;
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
        if (!hasCollided) {
            position[0] += velocity[0]*dt; 
            position[1] += velocity[1]*dt;

            // Check if need to re-allocate orbit arrays
            if (orbitPoints == orbit.length) {
                if (debug) System.out.println("Increasing orbit array size...");
                double[][] tmp = new double[orbit.length + size][2];
                System.arraycopy(orbit, 0, tmp, 0, orbit.length);
                orbit = tmp; 
            }

            orbit[orbitPoints][0] = position[0];
            orbit[orbitPoints][1] = position[1];
            orbitPoints++;

            if (debug) {
                System.out.println("Updating orbit of " + name);
                System.out.println("   Dist " + distance);
                System.out.println("   u " + u[0] + " " + u[1]);
                System.out.println("   vel " + velocity[0] + " " + velocity[1]);
                System.out.println("   pos " + position[0] + " " + position[1]);
            }
        }
    }
    public void chibakuTensei() {
        hasCollided = true;
    }
    
}

