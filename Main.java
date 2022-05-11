import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class Main extends JFrame implements ActionListener, MouseListener
{
	private Timer t;
	private char state; //the current state of the game (m = menu, p = playing)
	
	public Main()
	{
		state = 'm';
		//set up frame stuff
		setBounds(100,100,600,600);
		setTitle("joe");
		setResizable(false);
		
		//set up panel
		MainPanel p = new MainPanel();
		add(p);
		p.setPreferredSize(new Dimension(600,600));
		
		//add key listener
		addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) 
			{
				
			}

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

}

//the panel that contains everything being drawn (players, enemies, walls, etc.)
@SuppressWarnings("serial")
class MainPanel extends JPanel
{
	//set up panel
	public MainPanel()
	{
		setBackground(Color.lightGray);
	}
	
	//draw stuff
	Player player;
	ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	ArrayList<Wall> walls = new ArrayList<Wall>();
	ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
	}
	
	
	
}


