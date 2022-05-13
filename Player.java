
public class Player extends Person
{
	int dx, dy;
	public Player(int x, int y)
	{
		super(x, y);
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



	//make move methods
	public void update(){
		y+=dx;
	}

	//make an update method that moves the player like in the dodgeball project

}
