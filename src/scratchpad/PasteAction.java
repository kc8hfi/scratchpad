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
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

public class PasteAction extends AbstractAction
{
	public PasteAction(tree t,String text, ImageIcon icon,String desc, int mnemonic,KeyStroke accelerator)
	{
		super(text,icon); //text is the actual name
		myTreeClass = t;
		putValue(SHORT_DESCRIPTION, desc); //used for tooltip text
		putValue(MNEMONIC_KEY, mnemonic);
		putValue(ACCELERATOR_KEY,accelerator);
	}
	public void actionPerformed(ActionEvent e)
	{
		//System.out.println("with the new actions stuff,  " + e.getActionCommand());
		JTree thetree = myTreeClass.getTree();
		DefaultTreeModel treeModel = (DefaultTreeModel)thetree.getModel();
		int j;
		DefaultMutableTreeNode selectedNode = null;
		TreePath selectedNodePath = thetree.getSelectionPath();
		if (selectedNodePath != null)
		{
			selectedNode = (DefaultMutableTreeNode)(selectedNodePath.getLastPathComponent());
// 				System.out.println("selected node: " + selectedNode.toString());
// 				System.out.println("child count: " + Integer.toString(selectedNode.getChildCount()));
// 				System.out.println("moving node: " + movingNode.toString());
// 				System.out.println("insert is about to break..");
			if (myTreeClass.getMovingNode() != null)
			{
				treeModel.insertNodeInto(myTreeClass.getMovingNode(),selectedNode,selectedNode.getChildCount());
				//movingNode = selectedNode;
				//treeModel.removeNodeFromParent(movingNode);
				//gotta set the filesaved flag to false, 0
				myTreeClass.disablePaste();
				myTreeClass.setFileSaved(0);
				System.out.println("paste : " + myTreeClass.getMovingNode().toString());
			}
		}
	}
	private tree myTreeClass;
}//end PasteAction
