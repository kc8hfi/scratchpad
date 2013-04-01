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

public class MoveDownAction extends AbstractAction
{
     public MoveDownAction(tree t,String text, String actionCmd,String toolTip,ImageIcon icon,KeyStroke accelerator)
     {
          super(text,icon); //text is the actual name
          myTree  = t;
          putValue(ACTION_COMMAND_KEY,actionCmd);
          putValue(SHORT_DESCRIPTION, toolTip); //used for tooltip text
          putValue(ACCELERATOR_KEY,accelerator);
     }
     public void actionPerformed(ActionEvent e)
     {
          //System.out.println("with the new actions stuff,  " + e.getActionCommand());
          DefaultMutableTreeNode selectedNode = null;
          JTree thetree = myTree.getTree();
          DefaultTreeModel treeModel = (DefaultTreeModel)thetree.getModel();
          TreePath selectedNodePath = thetree.getSelectionPath();
          DefaultMutableTreeNode root = (DefaultMutableTreeNode)treeModel.getRoot();
          if (selectedNodePath != null)
          {
               selectedNode = (DefaultMutableTreeNode)(selectedNodePath.getLastPathComponent());
               //System.out.println(selectedNode.toString());
               if (!root.toString().equals(selectedNode.toString()))
               {
                    DefaultMutableTreeNode selectedNodeParent = (DefaultMutableTreeNode)selectedNode.getParent();
                    //System.out.println("parent:"+selectedNodeParent.toString());
                    int index = treeModel.getIndexOfChild(selectedNodeParent,selectedNode);
                    if (index < (treeModel.getChildCount(selectedNodeParent)-1) )
                    {
                         //System.out.println("move the node");
                         treeModel.removeNodeFromParent(selectedNode);
                         treeModel.insertNodeInto(selectedNode,selectedNodeParent,index+1);
                         myTree.setFileSaved(0);
                    }
               }
          }//no selected node
     }//end actionPerformed
     private tree myTree;
}//end class MoveDownAction
