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
		thepad.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		tree p = new tree(thepad);
		thepad.setContentPane(p);
		
		//create the toolbar
		p.createToolBar();
		thepad.setJMenuBar(p.createMenuBar());
		
		thepad.pack();
		thepad.setVisible(true);
	}
}//end scratchpad class