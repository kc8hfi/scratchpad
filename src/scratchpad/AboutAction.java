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
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;

public class AboutAction extends AbstractAction
{
	//(text,action command, tooltip,keystrokc
	public AboutAction(JFrame p,String text, String cmd,String toolTip,KeyStroke accelerator)
	{
		super(text); //text is the actual name
		parentWindow = p;
		putValue(ACTION_COMMAND_KEY,cmd);	//set action command
		putValue(SHORT_DESCRIPTION, toolTip); //used for tooltip text
		putValue(ACCELERATOR_KEY,accelerator);
	}
	public void actionPerformed(ActionEvent e)
	{
		//System.out.println("show the about dialog box" + e.getActionCommand());
		About about = new About(parentWindow);
		about.display();
	}//end actionPerformed
	private JFrame parentWindow;
}//end AboutAction
