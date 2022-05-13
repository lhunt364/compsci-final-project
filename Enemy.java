
public class Enemy
{
	private double x;
	private double y;


	public Enemy(int x, int y) 
	{
		this.x = x;
		this.y = y;
	}

	public double getX()
	{
		return x;
	}

	public double getY()
	{
		return y;
	}
	
	//make update() method which moves enemy towards player. if you need wall data or anything like that
	// Main has getter methods for that

}
