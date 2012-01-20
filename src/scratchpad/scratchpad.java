package scratchpad;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class scratchpad
{
	public static void main(String [] args)
	{
		System.out.println("hello people");
		JFrame thepad = new JFrame();
		thepad.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		//thepad.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		tree p = new tree(thepad);
		thepad.setContentPane(p);
		
		thepad.addWindowListener(new MyWindowListener());
		
		//create the toolbar
		p.createToolBar();
		
		//create the menu bar
		thepad.setJMenuBar(p.createMenuBar());
		
		thepad.pack();
		thepad.setMinimumSize(new Dimension(500,100));
		thepad.setVisible(true);
	}
	
}//end scratchpad class
