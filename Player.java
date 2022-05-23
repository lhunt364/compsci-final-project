
public class Player
{
	private static int x;
	private static int y; //note: this is absolute x and y. the player doesnt move around on screen.
	private int dx, dy;
	public Player(int x, int y)
	{
		this.x = x;
		this.y = y;
		this.dx = 0;
		this.dy = 0;
	}
	//TODO make player able to take damage. this can be handled a variety of ways, it just has to work decently efficiently.

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

	public static int getX() {return x;}

	public static int getY() {return y;}

	//make move methods
	public void update()
	{
		x+=dx;
		y+=dy;
	}

	//make an update method that moves the player like in the dodgeball project

}
