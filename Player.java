
public class Player
{
	private int x, y;
	private int dx, dy;
	public Player(int x, int y)
	{
		this.x = x;
		this.y = y;
		this.dx = 0;
		this.dy = 0;
	}


	public int getDx() {
		return dx;
	}

	public void setDx(int dx) {
		this.dx = dx;
	}

	public int getDy() {
		return dy;
	}

	public void setDy(int dy) {
		this.dy = dy;
	}

	public int getX() {return x;}

	public int getY() {return y;}

	//make move methods
	public void update()
	{
		x+=dx;
		y+=dy;
	}

	//make an update method that moves the player like in the dodgeball project

}
