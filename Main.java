import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.File;
import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")	
public class Main extends JFrame implements ActionListener
{
	private Timer t;

	private Player player; //the player

	private Weapon equippedWeapon;
	private ArrayList<Weapon> weapons;

	private MainPanel p;

	private double gunVolume; //volume of gun sounds

	private String map; //current map

	//TODO keep track of how many enemies have been killed so that a score can be calculated.
	public Main()
	{
		
		//set up weapons
		weapons = new ArrayList<>();
		String[] akmSounds = {"sounds/akmfire.wav", "sounds/akmfire2.wav", "sounds/akmfire3.wav", "sounds/akmfire4.wav", "sounds/akmfire5.wav"};
		weapons.add(new Weapon("AKM", 50, 600, akmSounds, "sounds/akmreload.wav", "sounds/akmemptyreload.wav", 3000, 3700, 0, Color.orange, 30, 40, this));
		weapons.add(new Weapon("ORSIS T-5000M", 100, 50, new String[]{"sounds/t5kfire.wav"}, "sounds/t5kreload.wav", "sounds/t5kemptyreload.wav", 3000, 4000, 1, Color.gray, 5, 65, this));
		weapons.add(new Weapon("HK G28", 75, 700, new String[]{"sounds/g28fire.wav"}, "sounds/g28reload.wav", "sounds/g28emptyreload.wav", 3500, 3000, 1, new Color(225,208,126), 20, 55, this));
		equippedWeapon = weapons.get(2);
		//set up other important stuff
		map = "maptest"; //<----------------------- SET MAP HERE <--------------------------
		int[] temp = MapReadWrite.readBorders(map);
		player = new Player(temp[2],temp[3], this);
		//set up frame stuff
		setBounds(100,100,600,600);
		setTitle("joe");
		setResizable(false);

		//set up panel

		p = new MainPanel(player, equippedWeapon, 600, 600, 50, map, this); // <<<----- set up panel here. if you want to change player size or window size do it here. pSize is player size.
		add(p);
		gunVolume = 0.2; // <<<------------------- VOLUME | 0 = no sound. 1 = full sound. 0.2 is good

		//add key listener
		addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {}

			@Override
			public void keyPressed(KeyEvent e) 
			{
				System.out.println("pressed: " + e.getKeyChar());
				char key = e.getKeyChar();
				if(key == 'w') player.setDy(-3);
				if(key == 's') player.setDy(3);
				if(key == 'a') player.setDx(-3);
				if(key == 'd') player.setDx(3);
				if(key == 'r') equippedWeapon.reload(false);
				if(key == '1' || key == '2' || key == '3') //change guns with 1 2 and 3. this probably isnt permanent, but is good for testing.
				{
					equippedWeapon = weapons.get(Integer.parseInt(key+"") - 1);
					p.setEquippedWeapon(equippedWeapon);
				}
				
			}

			@Override
			public void keyReleased(KeyEvent e) 
			{
				System.out.println("released: " + e.getKeyChar());
				char key = e.getKeyChar();
				if(key == 'w') player.setDy(0);
				if(key == 's') player.setDy(0);
				if(key == 'a') player.setDx(0);
				if(key == 'd') player.setDx(0);

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
	private boolean mouseDown; //true if mouse is down. false if not
	private boolean triggerDown; //becomes true when a shot is fired. becomes false when mouseDown = false. used to simulate semi auto fire
	private double shootTimer; //time since last shot in seconds
	private int width, height;
	private int pSize;
	private Main main;

	//stuff to draw
	private ArrayList<Enemy> enemies = new ArrayList<>();
	private ArrayList<Wall> walls = new ArrayList<>();
	private ArrayList<Bullet> bullets = new ArrayList<>();
	private String map;
	private int mapWidth;
	private int mapHeight;

	//set up panel
	public MainPanel(Player player, Weapon equippedWeapon, int width, int height, int pSize, String map, Main main)
	{
		this.player = player;
		this.equippedWeapon = equippedWeapon;
		setBackground(Color.lightGray);
		shootTimer = 0;
		setPreferredSize(new Dimension(width, height));
		this.width = width;
		this.height = height;
		this.pSize = pSize;
		/*
		this.walls = new ArrayList<>();
		this.enemies = new ArrayList<>();
		this.bullets = new ArrayList<>(); */
		this.map = map;
		this.mapWidth = 100;
		this.mapHeight = 100;
		loadMap(map);

		//handle mouse stuff (the mouselistener needs to be here so that the mouseevents are relative to the panel and not to the frame
		mouseX = 0;
		mouseY = 0;
		mouseDown = false;
		triggerDown = false;
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
				triggerDown = false;
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
				angle = Math.atan2(mouseY - height/2, mouseX - width/2);
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				mouseX = e.getX();
				mouseY = e.getY();
				angle = Math.atan2(mouseY - height/2, mouseX - width/2);
			}
		});

		//TODO add walls to make an actual map
		//define things to draw
		//walls.add(new Wall(0, 0, 100, 50)); // <----------------- MANUALLY ADD THINGS HERE <-------------------------
		enemies.add(new Enemy(50,50,1, 100.0, main));

	}
	
	//draw stuff
		/**
	 Paints pretty much everything i think
	 */
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		//draw walls
		g.setColor(Color.BLACK);
		for(int i = 0; i < walls.size(); i++)
		{
			Wall wall = walls.get(i);
			g.fillRect(wall.getX() - player.getX() + (width / 2), wall.getY() - player.getY() + (height / 2), wall.getWidth(), wall.getHeight());
		}

		//draw player
		try {
			Image image = ImageIO.read(new File("hannkschrader50x50.png")).getScaledInstance(pSize, pSize, Image.SCALE_SMOOTH); //this scales a given image. could be useful if we want to make the window resizable but honestly is a pain
			//Image image = ImageIO.read(new File("hannkschrader50x50.png"));
			g.drawImage(image, (width - pSize) / 2, (height - pSize) / 2, null);
		} catch (Exception e) {
		}

		//draw gun
		g.setColor(equippedWeapon.getColor());
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(10f * pSize / 50));
		double gunDistance = 20.0 * pSize / 50;
		Line2D gun = new Line2D.Double((gunDistance * Math.cos(angle) + width / 2), (gunDistance * Math.sin(angle) + height / 2), ((equippedWeapon.getLength() * (pSize / 50.0) + gunDistance) * Math.cos(angle) + width / 2), ((equippedWeapon.getLength() * (pSize / 50.0) + gunDistance) * Math.sin(angle) + height / 2));
		g2.draw(gun);

		//Testing random stuff most not working
		//test in progress- trying to use photo for gun
		/*
//		try {
//			Image image = ImageIO.read(new File("AR.png")).getScaledInstance(pSize, pSize, Image.SCALE_SMOOTH); //this scales a given image. could be useful if we want to make the window resizable but honestly is a pain
//			//Image image = ImageIO.read(new File("hannkschrader50x50.png"));
//
//			g.drawImage(image, (width - pSize) / 2, (height - pSize) / 2, null);
//			double rotationRequired = Math.toRadians (angle);
//			double locationX = 125/ 2;
//			double locationY = 50/ 2;
//			AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
//			AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
//			int drawLocationX = 300;
//			int drawLocationY = 300;
//// Drawing the rotated image at the required drawing locations
//			g.drawImage(op.filter((BufferedImage) image, null), drawLocationX, drawLocationY, null);
////			AffineTransform at = AffineTransform.getTranslateInstance(image.getWidth(null), image.getHeight(null));
////			at.rotate(image.getWidth(null)/2, image.getHeight(null)/2);
////		dd	Graphics2D g2d = (Graphics2D) g;
////			g2d.drawImage(image, at, null);



//			// g.drawImage(image, (int)(gunDistance * Math.cos(angle) + width / 2), (int)(gunDistance * Math.sin(angle) + height / 2), null);
//		} catch (Exception e) {
//		}
		/**/

		//draw bullets
		g.setColor(Color.ORANGE);
		for(int i = 0; i < bullets.size(); i++)
		{
			Bullet bullet = bullets.get(i);
			g.fillOval(bullet.getX() - player.getX() + (width / 2), bullet.getY() - player.getY() + (height / 2), 10, 10);
		}

		//draw enemies
		for(int i = 0; i < enemies.size(); i++)
		{
			try {
				Image image = ImageIO.read(new File("gustavo50x50.png")).getScaledInstance(pSize, pSize, Image.SCALE_SMOOTH);
				Enemy temp = enemies.get(i);
				g.drawImage(image, (int) (temp.getX() - player.getX() + (width / 2)) - pSize/2, (int) (temp.getY() - player.getY() + (height / 2)) - pSize/2, null);
			} catch (Exception e) {
			}
		}

		//draw other UI
		//draw ammo count
		if(equippedWeapon.getAmmo() >= equippedWeapon.getMagSize()/2) g.setColor(Color.GREEN);
		else if(equippedWeapon.getAmmo() >= equippedWeapon.getMagSize()/4) g.setColor(Color.YELLOW);
		else g.setColor(Color.RED);
		g.setFont(new Font("Monospaced", Font.BOLD, 50));
		g.drawString(String.format("%02d", equippedWeapon.getAmmo()), width - 100, height - 50);

	}

	/**
	 Handles shooting/making new bullets when necessary/updating bullets and enemies
	 */
	public void update()
	{
		shootTimer -= 0.02;
		if (shootTimer <= 0 && mouseDown && !equippedWeapon.getReloading() &&((equippedWeapon.getFireMode() == 1 && !triggerDown) || equippedWeapon.getFireMode() == 0)) //if can shoot
		{
			if (equippedWeapon.fire())
			{
				double gunDistance = 20.0 * pSize/50;
				bullets.add(new Bullet((int)((equippedWeapon.getLength()*(pSize/50.0)+gunDistance)*Math.cos(angle)+player.getX()), (int)((equippedWeapon.getLength()*(pSize/50.0)+gunDistance)*Math.sin(angle)+player.getY()), 25, angle));
			}
			triggerDown = true;
			shootTimer = 60.0/equippedWeapon.getFireRate();
		}

		for(int i = 0; i < bullets.size(); i++)
		{
			bullets.get(i).update();
		}

		for(int i = 0; i < enemies.size(); i++)
		{
			enemies.get(i).update();
		}
	}

	/**
	 * Sets instance variables given a map name
	 */
	public void loadMap(String map)
	{
		walls = MapReadWrite.readWalls(map);
		int[] temp = MapReadWrite.readBorders(map);
		mapWidth = temp[0];
		mapHeight = temp[1];
		walls.add(new Wall(-1*mapWidth, -1*mapHeight, mapWidth, 3*mapHeight)); //left border
		walls.add(new Wall(mapWidth, -1*mapHeight, mapWidth, 3*mapHeight)); //right border
		walls.add(new Wall(0, -1*mapHeight, mapWidth, mapHeight)); //top border
		walls.add(new Wall(0, mapHeight, mapWidth, mapHeight)); //bottom border
		System.out.println(walls.size());

	}

	public void setEquippedWeapon(Weapon w) {equippedWeapon = w;}

	//panel getters
	public ArrayList<Enemy> getEnemies() {return enemies;}
	public ArrayList<Bullet> getBullets() {return bullets;}
	public ArrayList<Wall> getWalls() {return walls;}
	public int getPSize() {return pSize;}
	
	
	
}


