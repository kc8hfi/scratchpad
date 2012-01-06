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
import java.awt.event.ActionEvent;
import javax.swing.KeyStroke;
import javax.swing.JTree;
import javax.swing.tree.TreePath;
import javax.swing.tree.DefaultMutableTreeNode;

public class RenameAction extends AbstractAction
{
	public RenameAction(tree t,String text, String actionCmd,String toolTip,ImageIcon icon,KeyStroke accelerator)
	{
		super(text,icon); //text is the actual name
		myTree = t;
		putValue(ACTION_COMMAND_KEY,actionCmd);
		putValue(SHORT_DESCRIPTION, toolTip); //used for tooltip text
		putValue(ACCELERATOR_KEY,accelerator);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		//System.out.println("with the new actions stuff,  " + e.getActionCommand());
		JTree thetree = myTree.getTree();
		TreePath path = thetree.getSelectionPath();
		if (path != null)
		{
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
			Rename renameDialog = new Rename();
			renameDialog.setName(node.toString());
			renameDialog.setVisible(true);
			thetree.setEditable(true);
			//System.out.println("before: " + node.toString());
			thetree.startEditingAtPath(path);
			if (renameDialog.getName().equals("") != true)
			{
				//System.out.println("new name will be: " + renameDialog.getName());
				//node.setName(renameDialog.getName());
				DataInfo n = (DataInfo)node.getUserObject();
				n.setName(renameDialog.getName());
				myTree.setFileSaved(0);
				//saveAction.setEnabled(true);
			}
			thetree.setEditable(false);
			//get rid of the dialog box
			renameDialog.dispose();
		}
	}//end actionPerformed
	
	private tree myTree;
	
}//end RenameAction class
