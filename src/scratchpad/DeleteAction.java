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

import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import java.net.URL;
import javax.sound.sampled.*;


public class DeleteAction extends AbstractAction
{
	public DeleteAction(JFrame p,JTree t,String text, ImageIcon icon,String desc, KeyStroke accelerator)
	{
		super(text,icon); //text is the actual name
		putValue(SHORT_DESCRIPTION, desc); //used for tooltip text
		putValue(ACCELERATOR_KEY,accelerator);
		
		thetree = t;
		parentWindow = p;
	}
	public void actionPerformed(ActionEvent e)
	{
		//System.out.println("delete?,  " + e.getActionCommand());
		DefaultTreeModel treeModel = (DefaultTreeModel)thetree.getModel();
		int j;
		DefaultMutableTreeNode selectedNode = null;
		TreePath selectedNodePath = thetree.getSelectionPath();
		if (selectedNodePath != null)
		{
			selectedNode = (DefaultMutableTreeNode)(selectedNodePath.getLastPathComponent());
			if (selectedNode.getParent() != null) //its not the root node
			{
				DefaultMutableTreeNode parent = (DefaultMutableTreeNode)selectedNode.getParent();
				//System.out.println("selected node parent: " + parent.toString());
				//System.out.println("selected node children: " + treeModel.getChildCount(selectedNode));
				int total = treeModel.getChildCount(selectedNode);
				if (treeModel.getChildCount(selectedNode) >=1)
				{
					//play a sound? 
					playSound("pause");
					j = showDialog();
					//System.out.println("their choice: " + Integer.toString(j));
					if (j == 0)
					{
						//go ahead and delete the node
						while(treeModel.getChildCount(selectedNode) != 0)
						{
							DefaultMutableTreeNode c = (DefaultMutableTreeNode)treeModel.getChild(selectedNode,0);
							//System.out.println("move: " + c.toString());
							//System.out.println("before parent count: " + treeModel.getChildCount(parent));
							treeModel.insertNodeInto(c,parent,treeModel.getChildCount(parent));
							//System.out.println("after parent count: " + treeModel.getChildCount(parent));
							//System.out.println("new child count of selectednode: " +treeModel.getChildCount(selectedNode));
						}
						//delete the selected node
						treeModel.removeNodeFromParent(selectedNode);
					}//they picked yes to delete the node, end if
				}//there was more than one child, end if
				else
				{
					playSound("pause");
					//System.out.println("remove the node since theres no children");
					j = showDialog();
					//System.out.println("their choice: " + Integer.toString(j));
					if (j == 0)
					{
						//go ahead and delete the node
						treeModel.removeNodeFromParent(selectedNode);
					}
				}
			}//end selected node is not the root node.  root node does not have a parent
		}//end if they have a path to a selected node
	}//end actionPerformed
	
	private void playSound(String s)
	{
		try
		{
			URL soundFileUrl = getClass().getResource("/sounds/"+s+".wav");
			AudioInputStream sound = AudioSystem.getAudioInputStream(soundFileUrl);
			DataLine.Info info = new DataLine.Info(Clip.class,sound.getFormat());
			Clip clip = (Clip)AudioSystem.getLine(info);
			clip.open(sound);
			clip.start();
		}
		catch (Exception soundException)
		{
			System.out.println("no sound, sorry");
		}
	}//end playSound
	
	private int showDialog()
	{
		int i = JOptionPane.showConfirmDialog(
			parentWindow,"Do you really want to delete this item?",
			"Delete Item?",JOptionPane.YES_NO_OPTION,
			JOptionPane.QUESTION_MESSAGE
		);
		return i;
	}
	
	private JFrame parentWindow;
	private JTree thetree;
}//end class DeleteAction