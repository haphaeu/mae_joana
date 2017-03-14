import java.awt.Graphics.*;

public class StraightMover { 
    private int x, y, xDirection, yDirection;
    private Sprite sprite;
    
    public StraightMover(int startX, int startY, Sprite sprite) { 
        x = startX; 
        y = startY; 
        this.sprite = sprite;
    }

    public void setMovementVector(int xIncrement, int yIncrement) { 
        xDirection = xIncrement; 
        yDirection = yIncrement;
    }

    public void draw(Graphics graphics) {
        sprite.draw(graphics, x, y);
        x += xDirection;
        y += yDirection;
    }
}