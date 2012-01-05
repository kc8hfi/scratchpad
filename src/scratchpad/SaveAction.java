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
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import javax.swing.JFileChooser;
import java.io.File;


public class SaveAction extends AbstractAction
{
	public SaveAction(tree t,String text, String actionCmd,String toolTip,ImageIcon icon,
					int mnemonic, KeyStroke accelerator)
	{
		super(text,icon); //text is the actual name
		myTreeClass = t;
		putValue(ACTION_COMMAND_KEY,actionCmd);	//action command
		putValue(SHORT_DESCRIPTION, toolTip); //used for tooltip text
		putValue(MNEMONIC_KEY, mnemonic);
		putValue(ACCELERATOR_KEY,accelerator);
	}//end constructor
	
	public void actionPerformed(ActionEvent e)
	{
		System.out.println("with the new actions stuff,  " + e.getActionCommand());
		if (myTreeClass.getFileSaved() == 0)	//the file is not saved
		{
			if (myTreeClass.getFileName() == "")
			{
				System.out.print("show the file chooser dialog to get a file first");
				JFileChooser filePicker = new JFileChooser();
				int returnValue = filePicker.showSaveDialog(myTreeClass.getParentWindow());
				if (returnValue == filePicker.APPROVE_OPTION)
				{
					System.out.println("they clicked ok");
					File f = filePicker.getSelectedFile();
					System.out.println(f.getName());
					
					//create a new SaveToDisk object
					SaveToDisk saveme = new SaveToDisk(myTreeClass.getTree(),f);
					saveme.write();
					myTreeClass.setFileName(f.getPath());
					myTreeClass.setFileSaved(1);
					//saveAction.setEnabled(false);
					setEnabled(false);
				}//they clicked ok
			}
			else
			{
				System.out.println(myTreeClass.getFileName());
				//create a new SaveToDisk object
				File f = new File(myTreeClass.getFileName());
				SaveToDisk saveme = new SaveToDisk(myTreeClass.getTree(),f);
				saveme.write();

				myTreeClass.setFileSaved(1);
				//saveAction.setEnabled(false);
				setEnabled(false);
			}
		}//end file was not saved
	}//end actionPerformed
	
	private tree myTreeClass;
}//end SaveAction

