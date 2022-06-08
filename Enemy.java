import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;

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

	public double getSpeed() {return speed;}

	public int getDamage() {return damage;}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	//TODO make this class actually work. basically, make an update method that can move this Enemy towards the player.
	public void update(){

		if(this.getBounds().intersects(Player.getBounds())){
			System.out.println("Enemy touched");
			System.out.println(Player.getHealth());
			Player.health -= this.getDamage();
		}

		int playerX = Player.getX();
		int playerY = Player.getY();

		double diffX = playerX - x;
		double diffY = playerY - y;

		float angle = (float)Math.atan2(diffY, diffX);

		x += speed * Math.cos(angle);
		y += speed * Math.sin(angle);

		if(health <= 0)
		{
			main.addScore(maxHealth);
			main.getEnemies().remove(this);
		}



		//collision with bullets
		Weapon currentWeapon = main.getEquippedWeapon();
		int damage = currentWeapon.getDmg();
		int bulletLen = main.getBullets().size();
		for(int i = 0; i<bulletLen; i++){
			if(main.getBullets().size()>0)
			{
				if ((main.getBullets().size() > i))
				{
					if(main.getBullets().get(i).getBounds().intersects(this.getBounds()))
					{
						this.health -= damage;

						if(!main.getBullets().get(i).isPiercing())
						{
							main.getBullets().remove(i);
							i--;
						}
					}
				}
			}
		}
		//collision with walls
		ArrayList<Wall> temp = main.getWalls();
		for(int i = 0; i < temp.size(); i++)
		{
			Wall temp2 = temp.get(i);
			if(this.getBounds().intersects(temp2.getBounds()))
			{
				boolean yCol = false;
				y -= speed * Math.sin(angle);
				if (!this.getBounds().intersects(temp2.getBounds()))
				{
					y -= speed * Math.sin(angle);
					yCol = true;
				}
				y += speed * Math.sin(angle);
				x -= speed * Math.cos(angle);
				if(!this.getBounds().intersects(temp2.getBounds()) && !yCol)
				{
					x -= speed * Math.cos(angle);
				}
				x += speed * Math.cos(angle);

			}

		}

	}

	public Rectangle getBounds()
	{
		return new Rectangle((int)x-size/2, (int)y-size/2, size, size);
	}


}


