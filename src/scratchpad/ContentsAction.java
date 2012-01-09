/*
 * Copyright 2002-2011 Charles Amey
 * 
 * This file is part of Scratchpad.
 * 
 * Scratchpad is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Scratchpad is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Scratchpad.  If not, see <http://www.gnu.org/licenses/>.
*/

package scratchpad;

import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import javax.swing.JFrame;
import java.net.URL;
import javax.help.*;

public class ContentsAction extends AbstractAction
{
	public ContentsAction(tree t,String text, String desc,KeyStroke accelerator)
	{
		super(text); //text is the actual name
		myTree = t;
		putValue(SHORT_DESCRIPTION, desc); //used for tooltip text
		putValue(ACCELERATOR_KEY,accelerator);
	}
	public void actionPerformed(ActionEvent e)
	{
		System.out.println("show the help files,  " + e.getActionCommand());
		System.out.println("before the try");
		try
		{
			//URL soundFileUrl = getClass().getResource("/resources/sounds/"+s+".wav");
			URL help = getClass().getResource("/resources/help/myhelp.hs");
			//URL help = new URL("file://"+path+"help/myhelp.hs");
			HelpSet hs = new HelpSet(null, help);
			HelpBroker broker = hs.createHelpBroker();
// 			Point p = new Point ();
// 			p.setLocation(100,100);         //x, y
// 			Dimension d = new Dimension(700,600);   //x y
// 			broker.setSize(d);
// 			broker.setLocation(p);
			System.out.println("where re we");
			broker.setDisplayed(true);
		}
		catch (Exception ew)
		{
// 		JOptionPane.showMessageDialog(parentWindow,"Sorry, the Documentation is" +
// 									"  currently Unavailable","ERROR",1);
			System.out.println("no documentation available");
		}
	}//end actionPerformed
	
	private tree myTree;
}//end ContentsAction


