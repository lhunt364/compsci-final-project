import java.awt.*;

public class Enemy
{
	private double x;
	private double y;
	private int size;

	private double speed;
	private int damage;

	private int maxHealth;
	private int health;

	private Main main;

	//TODO give enemies health so that they can die, as well making some way for an enemy to remove itself from the enemies ArrayList in Main when it dies.
	public Enemy(int x, int y, int size, double speed, int maxHealth, int damage, Main main)
	{
		this.x = x;
		this.y = y;
		this.size = size;
		this.speed = speed;
		this.maxHealth = maxHealth;
		this.health = maxHealth;
		this.damage = damage;
		this.main = main;
	}

	public double getX()
	{
		return x;
	}

	public double getY()
	{
		return y;
	}

	public int getHealth() {return health;}

	public int getMaxHealth() {return maxHealth;}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	//TODO make this class actually work. basically, make an update method that can move this Enemy towards the player.
	public void update(){

		int playerX = Player.getX();
		int playerY = Player.getY();

		double diffX = playerX - x;
		double diffY = playerY - y;

		float angle = (float)Math.atan2(diffY, diffX);

		x += speed * Math.cos(angle);
		y += speed * Math.sin(angle);
	}

	public Rectangle getBounds()
	{
		return new Rectangle((int)x, (int)y, size, size);
	}


}


