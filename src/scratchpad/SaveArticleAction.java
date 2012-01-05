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
import javax.swing.JTree;
import javax.swing.tree.TreePath;
import javax.swing.tree.DefaultMutableTreeNode;

public class SaveArticleAction extends AbstractAction
{
	public SaveArticleAction(tree t,String text, String actionCmd,String toolTip,ImageIcon icon)
	{
		super(text,icon); //text is the actual name
		myTree = t;
		putValue(ACTION_COMMAND_KEY,actionCmd);
		putValue(SHORT_DESCRIPTION, toolTip); //used for tooltip text
	}
	public void actionPerformed(ActionEvent e)
	{
		//System.out.println("with the new actions stuff,  " + e.getActionCommand());
		JTree thetree = myTree.getTree();
		TreePath path = thetree.getSelectionPath();
		if (path != null)
		{
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
			//System.out.println("before: " + node.toString());
			DataInfo d = (DataInfo)(node.getUserObject());
			System.out.println(node.toString());
			d.setData(myTree.getTextPaneText());
			System.out.println(myTree.getTextPaneText());
			myTree.setArticleSaved(1);
			//articleSaved = 1;
			//saveArticleAction.setEnabled(false);
		}
	}//end actionPerformed
	private tree myTree;
}//end SaveArticleAction
