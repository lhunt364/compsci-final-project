import java.awt.*;

public class Wall
{
	private int x;
	private int y;
	private int height;
	private int width;

	public Wall(int x, int y, int width, int height)
	{
		this.x = x;
		this.y = y;
		this.height = height;
		this.width = width;
	}

	public int getX()
	{
		return x;
	}

	public int getY()
	{
		return y;
	}

	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}

	public Rectangle getBounds()
	{
		return new Rectangle(x, y, width, height);
	}
	//test

}
