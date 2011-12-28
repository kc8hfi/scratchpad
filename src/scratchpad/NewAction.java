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
import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;

public class NewAction extends AbstractAction
{
	public NewAction(JFrame p,tree t,String text, String actionCmd,String toolTip, ImageIcon icon, 
					int mnemonic, KeyStroke accelerator)
	{
		super(text,icon); //text is the actual name
		parentWindow = p;
		myTreeClass = t;
		putValue(ACTION_COMMAND_KEY,actionCmd);	//set the actionCommand
		putValue(SHORT_DESCRIPTION, toolTip); //set tooltiptext
		putValue(MNEMONIC_KEY, mnemonic);	//set the underlined letter in the text
		putValue(ACCELERATOR_KEY,accelerator);	//set the keyboard shortcut
	}
	public void actionPerformed(ActionEvent e)
	{
		JTree thetree = myTreeClass.getTree();
		DefaultTreeModel treeModel = (DefaultTreeModel)thetree.getModel();
		DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode)treeModel.getRoot();
		while(treeModel.getChildCount(rootNode) != 0)
		{
			DefaultMutableTreeNode n = (DefaultMutableTreeNode)treeModel.getChild(rootNode,0);
			//System.out.println(n.toString());
			treeModel.removeNodeFromParent(n);
		}
		((DataInfo)rootNode.getUserObject()).setName("brand new name");
		((DataInfo)rootNode.getUserObject()).setData("some new data");

		myTreeClass.setArticleSaved(1);
		
		//DataInfo t = (DataInfo)rootNode.getUserObject();
		TreeNode [] nodes = treeModel.getPathToRoot(rootNode);
		TreePath path = new TreePath(nodes);
		thetree.setSelectionPath(new TreePath(nodes));
		
		//System.out.println("call node structure changed");
		treeModel.nodeStructureChanged(rootNode);

		myTreeClass.setFileSaved(0);
	}//end actionPerformed
	
	private JFrame parentWindow;
	private tree myTreeClass;
}//end actionPerformed	
