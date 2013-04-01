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
import javax.swing.JOptionPane;

public class FindAction extends AbstractAction
{
     public FindAction(tree t, String text, String desc, int mnemonic,KeyStroke accelerator)
     {
          super(text); //text is the actual name
          myTreeClass = t;
          findMe = "";
          putValue(SHORT_DESCRIPTION, desc); //used for tooltip text
          putValue(MNEMONIC_KEY, mnemonic);
          putValue(ACCELERATOR_KEY,accelerator);
     }
     public void actionPerformed(ActionEvent e)
     {
          JTree tree;
          System.out.println("with the new actions stuff,  " + e.getActionCommand());
          String f = (String)JOptionPane.showInputDialog(
               myTreeClass.getParentWindow(),
               "What are you looking for?",
               "Find",
               JOptionPane.PLAIN_MESSAGE,
               null,
               null,
               ""
          );
          findMe = f;
          traverse();
     }//end actionPerformed
     
     public void traverse() 
     { 
          JTree theTree = myTreeClass.getTree();
          TreeModel model = theTree.getModel();
          if (model != null)
          {
               DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot();
               DataInfo rootInfo = (DataInfo)root.getUserObject();
               int index = rootInfo.toString().indexOf(findMe);
               if (index < 0)     //not the root node name
               {
                    index = rootInfo.getData().indexOf(findMe);     //search the root node's data
                    if (index < 0)     //not in the root node's data
                    {
                         System.out.println("not in the root node");
                         walk(model,root);
                    }
                    else
                    {
                         System.out.println("its in the root node's data");
                    }
               }
               else
               {
                    System.out.println("its the root node name, don't search further");
               }
          }
          else
               System.out.println("Tree is empty.");
     }//end traverse
          
     protected void walk(TreeModel model, DefaultMutableTreeNode o)
     {
          int  cc;
          cc = model.getChildCount(o);
          for( int i=0; i < cc; i++)
          {
               DefaultMutableTreeNode childNode = (DefaultMutableTreeNode)model.getChild(o,i);
               DefaultMutableTreeNode childsParent = (DefaultMutableTreeNode)childNode.getParent();
               DataInfo child = (DataInfo)childNode.getUserObject();
               if (model.isLeaf(childNode))
               {
                    int index = child.toString().indexOf(findMe);
                    if (index < 0)     //not in the nodes name
                    {
                         index = child.getData().indexOf(findMe);
                         if (index < 0 )//not in the data
                         {
                              System.out.println("not here yet");
                         }
                         else
                         {
                              System.out.println("its in the nodes data!");
                              break;
                         }
                    }
                    else
                    {
                         System.out.println("its in the nodes name!!");
                         break;
                    }
                    //System.out.println("walk if: look at the node and its data???");
               }
               else
               {
                    //System.out.println("walk else: look at the node and its data???");
                    walk(model,childNode ); 
               }
          }//end loop
     }//end walk
     
     private tree myTreeClass;
     private String findMe;
}//end FindAction
