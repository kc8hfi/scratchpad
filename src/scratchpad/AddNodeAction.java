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
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;

public class AddNodeAction extends AbstractAction
{
	public AddNodeAction(tree t,String text, String actionCmd,String toolTip,ImageIcon icon,KeyStroke accelerator)
	{
		super(text,icon); //text is the actual name
		myTree = t;
		putValue(ACTION_COMMAND_KEY,actionCmd);
		putValue(SHORT_DESCRIPTION, toolTip); //used for tooltip text
		putValue(ACCELERATOR_KEY,accelerator);
	}
	public void actionPerformed(ActionEvent e)
	{
		//System.out.println("add node command: " + e.getActionCommand());
		JTree thetree = myTree.getTree();
		DefaultTreeModel treeModel = (DefaultTreeModel)thetree.getModel();
		int nodeCount = myTree.getNodeCount();
		DataInfo n = new DataInfo("new node " + Integer.toString(nodeCount),"");
		DefaultMutableTreeNode parentNode = null;
		TreePath parentPath = thetree.getSelectionPath();
		DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode)treeModel.getRoot();
		if (parentPath == null) 
		{
			//There is no selection. Default to the root node.
			parentNode = rootNode;
		} 
		else 
		{
			parentNode = (DefaultMutableTreeNode)(parentPath.getLastPathComponent());
		}
		DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(n);
		treeModel.insertNodeInto(newNode,parentNode,parentNode.getChildCount());
		myTree.setFileSaved(0);
		thetree.scrollPathToVisible(new TreePath(newNode.getPath()));
		
		nodeCount++;
		myTree.setNodeCount(nodeCount);
	}//end actionPerformed
	
	private tree myTree;
}//end AddNodeAction
