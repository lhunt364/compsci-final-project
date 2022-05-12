import java.awt.event.*;
import java.util.ArrayList;

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

	public Main()
	{
		//set up weapons
		weapons = new ArrayList<Weapon>();
		weapons.add(new Weapon("AKM", 50, 600, null, null, Color.orange));
		equippedWeapon = null;
		state = 'm';
		//set up frame stuff
		setBounds(100,100,600,600);
		setTitle("joe");
		setResizable(false);
		
		//set up panel
		MainPanel p = new MainPanel(player);
		add(p);
		p.setPreferredSize(new Dimension(600,600));
		
		//add key listener
		addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {}

			@Override
			public void keyPressed(KeyEvent e) 
			{
				if(e.getKeyCode() == 'w') ;
				if(e.getKeyCode() == 's') ;
				if(e.getKeyCode() == 'a') ;
				if(e.getKeyCode() == 'd') ;
				
			}

			@Override
			public void keyReleased(KeyEvent e) 
			{

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

		//draw walls
		for(int i = 0; i < walls.size(); i++)
		{
			Wall wall = walls.get(i);
			//g.drawRect(wall.getX(), wall.getY(), wall.getWidth(), wall.getHeight());

		}

		//draw enemies
		for(int i = 0; i < enemies.size(); i++)
		{

		}
	}
	
	
	
}


