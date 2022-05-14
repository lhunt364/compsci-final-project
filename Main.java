import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class Main extends JFrame implements ActionListener, MouseListener
{
	private Timer t;
	private char state; //the current state of the game (m = menu, p = playing)

	private Player player; //the player

	private Weapon equippedWeapon;
	private ArrayList<Weapon> weapons;

	private MainPanel p;

	public Main()
	{
		//set up weapons
		weapons = new ArrayList<Weapon>();
		weapons.add(new Weapon("AKM", 50, 600, null, null, null, Color.orange, 30));
		weapons.add(new Weapon("SIG MCX", 35, 800, null, null, null, Color.darkGray, 30));
		weapons.add(new Weapon("ORSIS T-5000M", 100, 30, null, null, null, Color.gray, 5));
		equippedWeapon = weapons.get(0);
		//set up other important stuff
		player = new Player(0,0);
		state = 'm';
		//set up frame stuff
		setBounds(100,100,600,600);
		setTitle("joe");
		setResizable(false);
		
		//set up panel
		p = new MainPanel(player);
		add(p);
		p.setPreferredSize(new Dimension(600,600));
		
		//add key listener
		addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {}

			@Override
			public void keyPressed(KeyEvent e) 
			{
				if(e.getKeyCode() == 'w') player.setDy(-2);
				if(e.getKeyCode() == 's') player.setDy(2);
				if(e.getKeyCode() == 'a') player.setDx(-2);
				if(e.getKeyCode() == 'd') player.setDx(2);
				
			}

			@Override
			public void keyReleased(KeyEvent e) 
			{
				if(e.getKeyCode() == 'w') player.setDy(0);
				if(e.getKeyCode() == 's') player.setDy(0);
				if(e.getKeyCode() == 'a') player.setDx(0);
				if(e.getKeyCode() == 'd') player.setDx(0);

			}
			
		});

		//finish frame stuff
		//pack();
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		//timer
		t = new Timer(20, this);
		t.start();
	}

	public static void main(String[] args) //main method
	{
		new Main();
	}

	@Override
	public void actionPerformed(ActionEvent e) //updates every frame (50fps)
	{
		
	}

	//handle mouse stuff
	@Override
	public void mousePressed(MouseEvent e)
	{
		e.getLocationOnScreen().getX();
		e.getLocationOnScreen().getY();

	}
	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}

	//drawing data getters

	/**@return ArrayList of all Enemies*/
	public ArrayList<Enemy> getEnemies() {return p.getEnemies();}
	/**@return ArrayList of all Bullets*/
	public ArrayList<Bullet> getBullets() {return p.getBullets();}
	/**@return ArrayList of all Walls*/
	public ArrayList<Wall> getWalls() {return p.getWalls();}
}

//the panel that contains everything being drawn (players, enemies, walls, etc.)
@SuppressWarnings("serial")
class MainPanel extends JPanel
{
	Player player;
	//set up panel
	public MainPanel(Player player)
	{
		this.player = player;
		setBackground(Color.lightGray);
	}
	
	//draw stuff
	ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	ArrayList<Wall> walls = new ArrayList<Wall>();
	ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		//draw player
		final int playerSize = 50; //the size of the player (keep this at 50 unless doing some testing stuff or whatever)
		try {
			//Image image = ImageIO.read(new File("hannkschrader.png")).getScaledInstance(playerSize, playerSize, Image.SCALE_SMOOTH);
			Image image = ImageIO.read(new File("hannkschrader50x50.png"));
			g.drawImage(image, 275, 275, null);
		} catch (IOException e) {}
		//draw walls
		for(int i = 0; i < walls.size(); i++)
		{
			Wall wall = walls.get(i);
			g.drawRect(wall.getX(), wall.getY(), wall.getWidth(), wall.getHeight());

		}

		//draw enemies
		for(int i = 0; i < enemies.size(); i++)
		{

		}
	}

	//panel getters
	public ArrayList<Enemy> getEnemies() {return enemies;}
	public ArrayList<Bullet> getBullets() {return bullets;}
	public ArrayList<Wall> getWalls() {return walls;}
	
	
	
}


