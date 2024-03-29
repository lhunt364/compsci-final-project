import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;

public class Player
{
	private static int x;
	private static int y; //note: this is absolute x and y. the player doesnt move around on screen.
	private static int size;
	private static int dx;
	private static int dy;

	public static int health;
	private int maxHealth;
	private int speed;
	private Main main;

	public Player(int x, int y, int size, int health, int speed, Main main)
	{
		this.x = x;
		this.y = y;
		this.size = size;
		this.dx = 0;
		this.dy = 0;

		this.health = health;
		this.maxHealth = health;
		this.speed = speed;
		this.main = main;
	}
	//TODO make player able to take damage. this can be handled a variety of ways, it just has to work decently efficiently.

	public static int getDx() {
		return dx;
	}

	public void setDx(int dx) {
		this.dx = dx;
	}

	public static int getDy() {
		return dy;
	}

	public void setDy(int dy) {
		this.dy = dy;
	}
	public static int getHealth(){return health;}
	public int getMaxHealth() {return maxHealth;}
	public static int getX() {return x;}

	public static int getY() {return y;}

	public int getSpeed() {return speed;}
	public int getSize(){return size;}
	//make move methods
	public void update()
	{
		//while(this.getHealth()>0){

		//}

			//TODO make player collide with walls. theres a method in Main called getWalls() which could be helpful.
			// note: its not static, and making it static would make 4 million other things static, so passing Main to the player is probably easier

			//TODO make player collide with enemies and get hurt by them. Main has a getEnemies() method


		x += dx;
		y += dy;

		//this took way too much work to get working :C but at least its finally working C:
		ArrayList<Wall> temp = main.getWalls();
		for(int i = 0; i < temp.size(); i++)
		{
			Wall temp2 = temp.get(i);
			if(this.getBounds().intersects(temp2.getBounds()))
			{
				boolean yCol = false;
				y -= dy;
				if (!this.getBounds().intersects(temp2.getBounds()))
				{
					y -= dy;
					yCol = true;
				}
				y += dy;
				x -= dx;
				if(!this.getBounds().intersects(temp2.getBounds()) && !yCol)
				{
					x -= dx;
				}
				x += dx;

			}

		}

	}




	public static Rectangle getBounds()
	{
		return new Rectangle(x-size/2, y-size/2, size, size);
	}


	//make an update method that moves the player like in the dodgeball project

}
