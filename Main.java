import java.awt.event.*;
import java.awt.geom.Line2D;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class Main extends JFrame implements ActionListener
{
	private Timer t;
	//Alex Test
	private Player player; //the player

	private Weapon equippedWeapon;
	private ArrayList<Weapon> weapons;

	private CardLayout cardLayout;
	private JPanel cardPanel;
	private MainPanel p;
	private EndPanel e;

	private double gunVolume; //volume of gun sounds

	private String map; //current map

	private char state; //game state. m = menu, g = game, e = end screen
	private int score; //score of the current game. is reset when goToEndScreen() is called
	private int kills; //amount of kills. displays on end screen

	public Main()
	{
		state = 'g';
		score = 0;
		kills = 0;
		//set up weapons
		weapons = new ArrayList<>();
		String[] akmSounds = {"sounds/akmfire.wav", "sounds/akmfire2.wav", "sounds/akmfire3.wav", "sounds/akmfire4.wav", "sounds/akmfire5.wav"};
		String[] vectorSounds = {"sounds/vectorfire1.wav", "sounds/vectorfire2.wav", "sounds/vectorfire3.wav", "sounds/vectorfire4.wav", "sounds/vectorfire5.wav"};
		weapons.add(new Weapon("AKM", 50, 600, akmSounds, "sounds/akmreload.wav", "sounds/akmemptyreload.wav", 3000, 3700, 0, Color.orange, 30, 40, this));
		weapons.add(new Weapon("ORSIS T-5000M", 400, 50, new String[]{"sounds/t5kfire.wav"}, "sounds/t5kreload.wav", "sounds/t5kemptyreload.wav", 3000, 4000, 1, Color.gray, 5, 65, this));
		weapons.add(new Weapon("HK G28", 100, 700, new String[]{"sounds/g28fire.wav"}, "sounds/g28reload.wav", "sounds/g28emptyreload.wav", 3500, 3000, 1, new Color(225,208,126), 20, 55, this));
		weapons.add(new Weapon("KRISS Vector", 20, 1700, vectorSounds, "sounds/vectorreload.wav", "sounds/vectoremptyreload.wav", 3200, 3000, 0, new Color(96,114,74), 50, 25, this));
		weapons.add(new Weapon("KRISS Vector but better", 100, 4000, vectorSounds, "sounds/vectorreload.wav", "sounds/vectoremptyreload.wav", 1, 1, 0, Color.PINK, 98, 25, this));
		equippedWeapon = weapons.get(3);
		//set up other important stuff
		map = "joe"; //<----------------------- SET MAP HERE <--------------------------
		int[] temp = MapReadWrite.readBorders(map);
		player = new Player(temp[2],temp[3], 50, 1000, 3, this);
		//set up frame stuff
		setBounds(100,100,600,600);
		setTitle("joe");
		setResizable(false);

		//set up panels
		cardLayout = new CardLayout();
		cardPanel = new JPanel(cardLayout);
		p = new MainPanel(player, equippedWeapon, 600, 600, 50, map, this); // <<<----- set up panel here. if you want to change player size or window size do it here. pSize is player size.
		cardPanel.add(p, "game");
		e = new EndPanel(600, 600, this);
		cardPanel.add(e, "end");
		add(cardPanel);
		cardLayout.show(cardPanel, "game");

		gunVolume = 0.2; // <<<------------------- VOLUME | 0 = no sound. 1 = full sound. 0.2 is good

		//add key listener
		addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {}

			@Override
			public void keyPressed(KeyEvent e) 
			{
				char key = e.getKeyChar();

				System.out.println("pressed: " + e.getKeyChar());
				if (key == 'w') player.setDy(-1 * player.getSpeed());
				if (key == 's') player.setDy(player.getSpeed());
				if (key == 'a') player.setDx(-1 * player.getSpeed());
				if (key == 'd') player.setDx(player.getSpeed());

				if(key == 'r') equippedWeapon.reload(false);
				if(key == '1' || key == '2' || key == '3' || key == '4' || key == '5') //change guns with number keys. this probably isnt permanent, but is good for testing.
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
		if(state == 'g') //if playing the game
		{
			player.update();
			p.update();
			p.repaint();
		}
		if(player.getHealth() <= 0) //if player is dead
		{
			goToEndScreen();
		}
	}

	/**
	 * tests whether a rectangle collides with the current walls
	 * @param r the rectangle to test
	 * @return true if it does collide, false if not
	 */
	public boolean collidesWithWalls(Rectangle r)
	{
		return p.collidesWithWalls(r);
	}

	/**
	 * ends the game, putting the player on the end screen
	 */
	public void goToEndScreen()
	{
		state = 'e';
		e.updateInfo(score, kills);
		cardLayout.show(cardPanel, "end");
	}

	public void goToMenu()
	{
		state = 'm';

	}

	public void goToGame()
	{
		state = 'g';
		int[] temp = MapReadWrite.readBorders(map);
		player = new Player(temp[2],temp[3], 50, 1000, 3, this);
		p.reset();
		p.loadMap(map);
		cardLayout.show(cardPanel, "game");
	}

	//drawing data getters

	/**@return ArrayList of all Enemies*/
	public ArrayList<Enemy> getEnemies() {return p.getEnemies();}
	/**@return ArrayList of all Bullets*/
	public ArrayList<Bullet> getBullets() {return p.getBullets();}
	/**@return ArrayList of all Walls*/
	public ArrayList<Wall> getWalls() {return p.getWalls();}

	public double getGunVolume() {return gunVolume;}
	public void setDifficulty(double d) {p.setDifficulty(d);}
	public void addScore(int s) {score += s; kills++;}

	public Weapon getEquippedWeapon(){return equippedWeapon;}


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
	public int pSize;
	private Main main;

	//enemy creation stuff
	private double spawnTimer;
	private double spawnTimeScale;
	private double speedScale;
	private double dmgScale;
	private double healthScale;
	private double difficulty;


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
		this.main = main;

		spawnTimer = 0;
		spawnTimeScale = 1;
		speedScale = 1;
		dmgScale = 1;
		healthScale = 1;
		difficulty = 1;
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
		//enemies.add(new Enemy(50,50, pSize,1, 100,10, main));

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
			System.out.println("failed to draw player");
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
				//draw health bar //TODO there's a limitation with Line2D that makes the health bar not accurate at low health amounts. doesn't matter that much but should be changed to using Rectangles
				g2.setStroke(new BasicStroke(5f * pSize / 50));
				g.setColor(Color.RED);
				g2.draw(new Line2D.Double((temp.getX() - player.getX() + (width / 2)) - pSize/2, (temp.getY() - player.getY() + (height / 2)) - pSize/2 - 10, (temp.getX() - player.getX() + (width / 2)) + pSize/2, (temp.getY() - player.getY() + (height / 2)) - pSize/2 - 10));
				g.setColor(Color.GREEN);
				double scale = (temp.getHealth() * 1.0 / temp.getMaxHealth()) - 0.5;
				g2.draw(new Line2D.Double((temp.getX() - player.getX() + (width / 2)) - pSize/2, (temp.getY() - player.getY() + (height / 2)) - pSize/2 - 10, (temp.getX() - player.getX() + (width / 2)) + pSize*scale, (temp.getY() - player.getY() + (height / 2)) - pSize/2 - 10));
				//draw enemy info
				g.setFont(new Font("Monospaced", Font.BOLD, 10));
				g.setColor(Color.GREEN);
				g.drawString(String.format("%03d", temp.getHealth()), (int) ((temp.getX() - player.getX() + (width / 2)) - pSize/2) - 13, (int) ((temp.getY() - player.getY() + (height / 2)) - pSize/2 - 22));
				g.setColor(Color.BLUE);
				g.drawString(String.format("%03.1f", temp.getSpeed()), (int) ((temp.getX() - player.getX() + (width / 2)) - pSize/2) + 17, (int) ((temp.getY() - player.getY() + (height / 2)) - pSize/2 - 22));
				g.setColor(Color.RED);
				g.drawString(String.format("%02d", temp.getDamage()), (int) ((temp.getX() - player.getX() + (width / 2)) - pSize/2) + 47, (int) ((temp.getY() - player.getY() + (height / 2)) - pSize/2 - 22));
			} catch (Exception e) {
				System.out.println("failed to draw enemy #" + i);
			}
		}

		//draw other UI
		//draw ammo count
		if(equippedWeapon.getAmmo() >= equippedWeapon.getMagSize()/2) g.setColor(Color.GREEN);
		else if(equippedWeapon.getAmmo() >= equippedWeapon.getMagSize()/4) g.setColor(Color.YELLOW);
		else g.setColor(Color.RED);
		g.setFont(new Font("Monospaced", Font.BOLD, 50));
		g.drawString(String.format("Ammo: %02d", equippedWeapon.getAmmo()), width - 250, height - 30);
		//draw health bar
		g.setColor(Color.GREEN);
		g.setFont(new Font("Monospaced", Font.BOLD, 25));
		g.drawString("Health: " + player.getHealth(), 25, 30);
		g.setColor(Color.RED);
		g.fillRect(20, 40, 200, 20);
		if(player.getHealth() > 0) //catch for making the health bar go backwards
		{
			double scale = (player.getHealth() * 1.0 / player.getMaxHealth());
			g.setColor(Color.GREEN);
			g.fillRect(20, 40, (int) (200*scale), 20);
		}



	}

	/**
	 Handles shooting/making new bullets when necessary/updating bullets and enemies
	 */
	public void update()
	{
		shootTimer -= 0.02; //TIME BETWEEN FRAMES (0.02 is normal)
		if (shootTimer <= 0 && mouseDown && !equippedWeapon.getReloading() &&((equippedWeapon.getFireMode() == 1 && !triggerDown) || equippedWeapon.getFireMode() == 0)) //if can shoot
		{
			if (equippedWeapon.fire())
			{
				double gunDistance = 20.0 * pSize/50;
				bullets.add(new Bullet((int)((equippedWeapon.getLength()*(pSize/50.0)+gunDistance)*Math.cos(angle)+player.getX()), (int)((equippedWeapon.getLength()*(pSize/50.0)+gunDistance)*Math.sin(angle)+player.getY()), 25, angle, 15, main));
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
		spawnEnemy();


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

	/**
	 * simply resets the ArrayLists storing things to render, as well as some fields
	 */
	public void reset()
	{
		walls = new ArrayList<>();
		enemies = new ArrayList<>();
		bullets = new ArrayList<>();
		spawnTimer = 0;
		spawnTimeScale = 1;
		speedScale = 1;
		dmgScale = 1;
		healthScale = 1;
	}


	public void playerWallCollision(){

	}

	/**
	 * attempts to spawn an enemy somewhere offscreen and increases the difficulty of the next enemy spawned
	 */
	public void spawnEnemy()
	{
		if(spawnTimer <= 0 && enemies.size() < 10) //spawn enemy
		{
			spawnTimer = 2 * spawnTimeScale; //reset timer
			spawnTimeScale -= 0.01;
			Rectangle visibleArea = new Rectangle(player.getX() - width/2, player.getY() - height/2, width, height); //the area visible to the player
			//Rectangle spawnArea = new Rectangle((int)(player.getX() - width*1.5 - pSize), (int)(player.getY() - height*1.5 - pSize), width*3, height*3); || !spawnArea.contains(spawn)
			Point spawn = new Point((int)(Math.random()*mapWidth),(int)(Math.random()*mapHeight));
			while(visibleArea.contains(spawn) || !collidesWithWalls(new Rectangle((int)spawn.getX()-pSize/2, (int)spawn.getY()-pSize/2, pSize, pSize)) ) //do this until spawn location is not visible and valid
			{
				int x = (int)(Math.random()*mapWidth);
				int y = (int)(Math.random()*mapHeight);
				spawn = new Point(x, y);
				visibleArea = new Rectangle(player.getX() - width/2 - pSize, player.getY() - height/2 - pSize, width, height);
			}
			System.out.println("spawned new enemy at " + spawn.getX() + ", " + spawn.getY());
			enemies.add(new Enemy((int)spawn.getX(), (int)spawn.getY(), pSize, 1*speedScale, (int)(100*healthScale), (int)(2*dmgScale), main));
			speedScale += 0.06*difficulty;
			dmgScale += 0.05*difficulty;
			healthScale += 0.08*difficulty;
			if(spawnTimeScale < 0.05) spawnTimeScale = 0.05; //lower limit for spawnTimeScale
			if(speedScale > player.getSpeed()*0.85) speedScale = player.getSpeed()*0.85; //upper limit for speedScale
		}
		spawnTimer -= 0.02; //TIME BETWEEN FRAMES (0.02 is normal)
	}

	/**
	 * tests whether a rectangle collides with the current walls
	 * @param r the rectangle to test
	 * @return true if it does collide, false if not
	 */
	public boolean collidesWithWalls(Rectangle r)
	{
		boolean ret = true;
		for(int i = 0; i < walls.size(); i++)
		{
			if(walls.get(i).getBounds().intersects(r)) ret = false;
		}
		return ret;
	}

	public void setEquippedWeapon(Weapon w) {equippedWeapon = w;}

	//panel getters/setters
	public ArrayList<Enemy> getEnemies() {return enemies;}
	public ArrayList<Bullet> getBullets() {return bullets;}
	public ArrayList<Wall> getWalls() {return walls;}
	public void setDifficulty(double d) {difficulty = d;}
	public int getPSize() {return pSize;}

	
}


