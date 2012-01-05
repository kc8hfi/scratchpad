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

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Container;
import javax.swing.JFrame;

class PopupListener extends MouseAdapter
{
	public void mousePressed(MouseEvent e) 
	{
		maybeShowPopup(e);
	}

	public void mouseReleased(MouseEvent e)
	{
		maybeShowPopup(e);
	}

	private void maybeShowPopup(MouseEvent e)
	{
		if (e.isPopupTrigger()) 
		{
			/*		System.out.println(e.getComponent());	//jtree
			System.out.println(e.getComponent().getParent());//jviewport
			System.out.println(e.getComponent().getParent().getParent());//jscrollpane
			System.out.println(e.getComponent().getParent().getParent().getParent());//jsplitpane
			System.out.println(e.getComponent().getParent().getParent().getParent().getParent());//tree
			System.out.println(e.getComponent().getParent().getParent().getParent().getParent().getParent());//jlayeredpane
			System.out.println(e.getComponent().getParent().getParent().getParent().getParent().getParent().getParent());//jrootpane
			System.out.println(e.getComponent().getParent().getParent().getParent().getParent().getParent().getParent().getParent() + "\n\n");//jframe
			System.out.println(e.getComponent().getParent().getParent().getParent().getParent().getParent().getParent().getParent().getParent()+ "\n\n");//*/
			Container last = e.getComponent().getParent();
			Container c = e.getComponent().getParent();
			while (c != null)
			{
				//System.out.println(c + "\n");
				last = c;
				c = c.getParent();
			}
			//System.out.println("this is the last one: " + last);
			JFrame f = (JFrame)last;
			tree myTreeClass = (tree)f.getContentPane();
			myTreeClass.getPopupMenu().show(e.getComponent(),e.getX(),e.getY());
			//System.out.println("this is the last one: " + myTreeClass);
		}
	}//end maybeShowPopup
}//end PopupListener

