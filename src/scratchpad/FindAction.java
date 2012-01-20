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
import javax.swing.KeyStroke;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import java.util.ArrayList;

public class FindAction extends AbstractAction
{
	public FindAction(tree t, String text, String desc, int mnemonic,KeyStroke accelerator)
	{
		super(text); //text is the actual name
		myTreeClass = t;
		putValue(SHORT_DESCRIPTION, desc); //used for tooltip text
		putValue(MNEMONIC_KEY, mnemonic);
		putValue(ACCELERATOR_KEY,accelerator);
	}
	public void actionPerformed(ActionEvent e)
	{
		JTree tree;
		System.out.println("with the new actions stuff,  " + e.getActionCommand());
		String f = (String)JOptionPane.showInputDialog(
			myTreeClass.getWindow(),
			"What are you looking for?",
			"Find",
			JOptionPane.PLAIN_MESSAGE,
			null,
			null,
			""
		);
		
		
	}
	
	public void traverse() 
	{ 
		JTree theTree = myTreeClass.getTree();
		TreeModel model = theTree.getModel();
		if (model != null)
		{
			DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot();
			DataInfo rootInfo = (DataInfo)root.getUserObject();
			ArrayList<String> list = new ArrayList<String>();
			//list.add(rootInfo.toString());
			//list.add(FixData(rootInfo.getData()));
			Csv makeCsvString = new Csv();
			String s = makeCsvString.combine(list);
			System.out.println(s);
			//out.println(s);
			walk(model,root);    
		}
		else
			System.out.println("Tree is empty.");
	}
		
	protected void walk(TreeModel model, DefaultMutableTreeNode o)
	{
		int  cc;
		cc = model.getChildCount(o);
		for( int i=0; i < cc; i++)
		{
			DefaultMutableTreeNode childNode = (DefaultMutableTreeNode)model.getChild(o,i);
			DefaultMutableTreeNode childsParent = (DefaultMutableTreeNode)childNode.getParent();
			DataInfo child = (DataInfo)childNode.getUserObject();
			//DataInfo info = (DataInfo)child.getUserObject();
			ArrayList<String> list = new ArrayList<String>();
			if (model.isLeaf(childNode))
			{
				//System.out.println(child.toString());
				//System.out.println(info.getData());
				//list.add(childsParent.toString());
				//list.add(child.toString());
				//list.add(FixData(child.getData()));
				Csv makeCsvString = new Csv();
				String s = makeCsvString.combine(list);
				System.out.println(s);
				//out.println(s);
			}
			else
			{
				//System.out.print(child.toString()+"--");
				//list.add(childsParent.toString());
				//list.add(child.toString());
				//list.add(FixData(child.getData()));
				Csv makeCsvString = new Csv();
				String s = makeCsvString.combine(list);
				System.out.println(s);
				//out.println(s);
				walk(model,childNode ); 
			}
		}//end loop
	}//end walk	
	private tree myTreeClass;
}
