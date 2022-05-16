import java.awt.event.*;
import java.awt.geom.Line2D;
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
	private char state; //the current state of the game (maybe useful for a menu or something)

	private Player player; //the player

	private Weapon equippedWeapon;
	private ArrayList<Weapon> weapons;

	private MainPanel p;

	private double gunVolume; //volume of gun sounds

	public Main()
	{
		//set up weapons
		weapons = new ArrayList<Weapon>();
		String[] akmSounds = {"sounds/akmfire.wav", "sounds/akmfire2.wav", "sounds/akmfire3.wav", "sounds/akmfire4.wav", "sounds/akmfire5.wav"};
		weapons.add(new Weapon("AKM", 50, 600, akmSounds, "sounds/akmreload.wav", "sounds/akmemptyreload.wav", 3000, 4000, Color.orange, 30, 40, this));
		weapons.add(new Weapon("ORSIS T-5000M", 100, 50, new String[]{"sounds/t5kfire.wav"}, "sounds/t5kreload.wav", "sound/t5kemptyreload.wav", 3000, 4000, Color.gray, 5, 60, this));
		equippedWeapon = weapons.get(1);
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
		gunVolume = 0.2; // <<<------------------- VOLUME | 0 = no sound. 1 = full sound. 0.2 is good
		
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
				if(e.getKeyChar() == 'r') equippedWeapon.reload(false);
				
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
		p.update();
		p.repaint();
		//TODO check collision between player & walls & enemies & bullets and stuff
	}

	//drawing data getters

	/**@return ArrayList of all Enemies*/
	public ArrayList<Enemy> getEnemies() {return p.getEnemies();}
	/**@return ArrayList of all Bullets*/
	public ArrayList<Bullet> getBullets() {return p.getBullets();}
	/**@return ArrayList of all Walls*/
	public ArrayList<Wall> getWalls() {return p.getWalls();}

	public double getGunVolume() {return gunVolume;}
}

//the panel that contains everything being drawn (players, enemies, walls, etc.) and also handles the mouse/shooting
@SuppressWarnings("serial")
class MainPanel extends JPanel
{
	private Player player;
	private int mouseX;
	private int mouseY;
	private double angle; //the angle the player is firing (<-- useful for making bullets, whoever is doing that)
	private Weapon equippedWeapon;
	private boolean mouseDown;
	private double shootTimer; //time since last shot in seconds
	//set up panel
	public MainPanel(Player player, Weapon equippedWeapon)
	{
		this.player = player;
		this.equippedWeapon = equippedWeapon;
		setBackground(Color.lightGray);
		shootTimer = 0;

		//handle mouse stuff (the mouselistener needs to be here so that the mouseevents are relative to the panel and not to the frame
		mouseX = 0;
		mouseY = 0;
		mouseDown = false;
		addMouseListener(new MouseListener() {
			@Override
			public void mousePressed(MouseEvent e)
			{
				mouseDown = true;
				System.out.println("mouse press at " + e.getX() + " " + e.getY());
			}
			@Override
			public void mouseClicked(MouseEvent e) {}
			@Override
			public void mouseReleased(MouseEvent e)
			{
				mouseDown = false;
			}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}

		});

		addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseDragged(MouseEvent e) {
				mouseX = e.getX();
				mouseY = e.getY();
				angle = Math.atan2(mouseY - 300, mouseX - 300); //note: the 300 comes from the pane size/2
			}

			@Override
			public void mouseMoved(MouseEvent e) {
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

	/**
	 Paints pretty much everything i think
	 */
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
			//TODO draw bullets
			//g.fillOval();
		}

		//draw enemies
		for(int i = 0; i < enemies.size(); i++)
		{
			//TODO draw enemies
		}


	}

	/**
	 Handles shooting/making new bullets when necessary
	 */
	public void update()
	{
		shootTimer -= 0.02;
		if (shootTimer <= 0 && mouseDown) //if can shoot
		{
			equippedWeapon.fire();
			shootTimer = 60.0/equippedWeapon.getFireRate();
			//TODO: code to create new bullet
		}
		//TODO update bullets and enemy movement here.
	}

	//panel getters
	public ArrayList<Enemy> getEnemies() {return enemies;}
	public ArrayList<Bullet> getBullets() {return bullets;}
	public ArrayList<Wall> getWalls() {return walls;}
	
	
	
}


