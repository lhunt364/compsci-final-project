import java.awt.event.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class Main extends JFrame implements ActionListener
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
		weapons.add(new Weapon("AKM", 50, 600, null, null, null, Color.orange, 30, 40));
		weapons.add(new Weapon("SIG MCX", 35, 800, null, null, null, Color.darkGray, 30, 30));
		weapons.add(new Weapon("ORSIS T-5000M", 100, 30, null, null, null, Color.gray, 5, 60));
		equippedWeapon = weapons.get(0);
		//set up other important stuff
		player = new Player(0,0);
		state = 'm';
		//set up frame stuff
		setBounds(100,100,600,600);
		setTitle("joe");
		setResizable(false);
		
		//set up panel
		p = new MainPanel(player, equippedWeapon);
		add(p);
		p.setPreferredSize(new Dimension(600,600));
		
		//add key listener
		addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {}

			@Override
			public void keyPressed(KeyEvent e) 
			{
				System.out.println("pressed: " + e.getKeyChar());
				if(e.getKeyChar() == 'w') player.setDy(-2);
				if(e.getKeyChar() == 's') player.setDy(2);
				if(e.getKeyChar() == 'a') player.setDx(-2);
				if(e.getKeyChar() == 'd') player.setDx(2);
				
			}

			@Override
			public void keyReleased(KeyEvent e) 
			{
				System.out.println("released: " + e.getKeyChar());
				if(e.getKeyChar() == 'w') player.setDy(0);
				if(e.getKeyChar() == 's') player.setDy(0);
				if(e.getKeyChar() == 'a') player.setDx(0);
				if(e.getKeyChar() == 'd') player.setDx(0);

			}
			
		});


		//finish frame stuff
		pack();
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
		player.update();
		p.repaint();
	}

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
	private Player player;
	private int mouseX;
	private int mouseY;
	private double angle;
	private Weapon equippedWeapon;
	//set up panel
	public MainPanel(Player player, Weapon equippedWeapon)
	{
		this.player = player;
		this.equippedWeapon = equippedWeapon;
		setBackground(Color.lightGray);

		//handle mouse stuff (the mouselistener needs to be here so that the mouseevents are relative to the panel and not to the frame
		mouseX = 0;
		mouseY = 0;
		addMouseListener(new MouseListener() {
			@Override
			public void mousePressed(MouseEvent e)
			{
				System.out.println("mouse press at " + e.getX() + " " + e.getY());


			}
			@Override
			public void mouseClicked(MouseEvent e) {}
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}

		});

		addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseDragged(MouseEvent e) {}

			@Override
			public void mouseMoved(MouseEvent e)
			{
				mouseX = e.getX();
				mouseY = e.getY();
				angle = Math.atan2(mouseY - 300, mouseX - 300);
			}
		});
	}
	
	//draw stuff
	ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	ArrayList<Wall> walls = new ArrayList<Wall>();
	ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		//draw player
		try {
			//Image image = ImageIO.read(new File("hannkschrader.png")).getScaledInstance(50, 50, Image.SCALE_SMOOTH); //this scales a given image. could be useful if we want to make the window resizable but honestly is a pain
			Image image = ImageIO.read(new File("hannkschrader50x50.png"));
			g.drawImage(image, 275, 275, null);
		} catch (IOException e) {}

		//draw gun
		g.setColor(equippedWeapon.getColor());
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(10));
		final int gunDistance = 20;
		Line2D gun = new Line2D.Double(gunDistance*Math.cos(angle)+300, gunDistance*Math.sin(angle)+300, (equippedWeapon.getLength()+gunDistance)*Math.cos(angle)+300, (equippedWeapon.getLength()+gunDistance)*Math.sin(angle)+300);
		g2.draw(gun);

		//draw walls
		g.setColor(Color.BLUE);
		for(int i = 0; i < walls.size(); i++)
		{
			Wall wall = walls.get(i);
			g.drawRect(wall.getX(), wall.getY(), wall.getWidth(), wall.getHeight());

		}

		//draw bullets
		g.setColor(Color.ORANGE);
		for(int i = 0; i < bullets.size(); i++)
		{
			//g.fillOval();
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


