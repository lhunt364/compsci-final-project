import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class Main extends JFrame implements ActionListener
{
	private Timer t;
	
	public Main()
	{
		//set up frame stuff
		setBounds(100,100,600,600);
		setTitle("joe");
		setResizable(false);
		setBackground(Color.lightGray);
		
		
		MainPanel p = new MainPanel();
		add(p);
		
		
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
	
}


