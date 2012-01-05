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

public class CutAction extends AbstractAction
{
	public CutAction(tree t, String text, ImageIcon icon,String desc, int mnemonic,KeyStroke accelerator)
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
		//System.out.println(e);
		JTree thetree = myTreeClass.getTree();
		DefaultTreeModel treeModel = (DefaultTreeModel)thetree.getModel();
		int j;
		DefaultMutableTreeNode selectedNode = null;
		TreePath selectedNodePath = thetree.getSelectionPath();
		if (selectedNodePath != null)
		{
			selectedNode = (DefaultMutableTreeNode)(selectedNodePath.getLastPathComponent());
			DefaultMutableTreeNode root = (DefaultMutableTreeNode)treeModel.getRoot();
			if (!root.toString().equals(selectedNode.toString()))
			{
				myTreeClass.setMovingNode(selectedNode);
				treeModel.removeNodeFromParent(myTreeClass.getMovingNode());
				//gotta set the filesaved flag to false, 0
				myTreeClass.setFileSaved(0);
				System.out.println("cut: " + selectedNode.toString());
				myTreeClass.enablePaste();
			}
		}
	}//end actionPerformed
	private tree myTreeClass;
}//end CutAction class
