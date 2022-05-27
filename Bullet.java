import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Bullet extends JComponent
{
    //TODO make this class work. (:
    // for starters, a constructor with x, y, angle, and speed should probably be made
    // make an update method that moves the bullet along its path according to its angle and speed.
    // speed will probably just be a constant unless we want different bullet speeds for different guns
    // also it should be able to do different damage depending on the equippedWeapon in Main, so storing that would be good too.

    private Ellipse2D.Double b;
    private int dx;
    private int dy;

    private int speed;
    private double angle;
    public Bullet(int x,int y, int speed, double angle)
    {
        this.speed = speed;
        this.angle = angle;
        this.setLocation(x,y);
        dx = (int)(Math.cos(angle) * speed);
        dy = (int)(Math.sin(angle) * speed);
        this.setSize(15,15);

        b = new Ellipse2D.Double(0,0,14,14);

    }
    public void paintComponent(Graphics g)
    {

        Graphics2D g2 = (Graphics2D)g;

        g2.fill(b);


    }
    public void setDx(int x)
    {
        dx = x;
    }
    public void setDy(int y)
    {
        dy = y;
    }

    public int getDx()
    {
        return dx;
    }
    public int getDy()
    {
        return dy;
    }

    public void update()
    {
        this.setLocation(this.getX()+dx,this.getY()+dy);
    }
}




