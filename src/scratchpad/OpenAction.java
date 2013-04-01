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
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.DataInputStream;
import java.util.ArrayList;

public class OpenAction extends AbstractAction
{
     public OpenAction(JFrame p,tree t,String text, String actionCmd,String toolTip,ImageIcon icon,
                         int mnemonic,KeyStroke accelerator)
     {
          super(text,icon); //text is the actual name
          putValue(ACTION_COMMAND_KEY,actionCmd);     //action command
          putValue(SHORT_DESCRIPTION, toolTip); //used for tooltip text
          putValue(MNEMONIC_KEY, mnemonic);
          putValue(ACCELERATOR_KEY,accelerator);
          parentWindow = p;
          myTreeClass = t;
     }
     public void actionPerformed(ActionEvent e)
     {
          JTree thetree = myTreeClass.getTree();
          DefaultTreeModel treeModel = (DefaultTreeModel)thetree.getModel();
          DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode)treeModel.getRoot();
          JFileChooser filePicker = new JFileChooser();
          int returnValue = filePicker.showOpenDialog(parentWindow);
          if (returnValue == filePicker.APPROVE_OPTION)
          {
               System.out.println("they clicked ok");
               File f = filePicker.getSelectedFile();
               //System.out.println(f.getName());
               //System.out.println("empty the tree first");
               while(treeModel.getChildCount(rootNode) != 0)
               {
                    DefaultMutableTreeNode n = (DefaultMutableTreeNode)treeModel.getChild(rootNode,0);
                    //System.out.println(n.toString());
                    treeModel.removeNodeFromParent(n);
               }
               //read from the file and build a tree
               try
               {
                    FileInputStream finputstream = new FileInputStream(f.getPath());
                    DataInputStream in = new DataInputStream(finputstream);
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));
                    String line = "";
                    line = br.readLine();     //get the first line, this is the root of the tree
                    System.out.println("first line: " + line);
                    Csv t = new Csv();
                    ArrayList<String> firstline = t.parse(line);
                    System.out.println("root node name: " +firstline.get(0));
                    System.out.println("root node text: " +firstline.get(1));
                    
                    ((DataInfo)rootNode.getUserObject()).setName(firstline.get(0));     //set name
                    ((DataInfo)rootNode.getUserObject()).setData(firstline.get(1));     //set name
                    
                    System.out.println("root node is ok");
                    System.out.println(rootNode);
                    //get the rest of the lines now
                    while((line = br.readLine()) != null)
                    {
                         firstline = t.parse(line);
                         //root,name,data
                         DataInfo item = new DataInfo(firstline.get(1),firstline.get(2));
                         DefaultMutableTreeNode n = new DefaultMutableTreeNode(item);
                         String parentName = firstline.get(0);
                         //System.out.println("look for " + parentName);
                         //go find the parent node
                         DefaultMutableTreeNode p = search(rootNode,parentName);
                         if (p == null)
                         {
                              System.out.println("there is no parent because p is null, so add to root");
                              System.out.println("add " + n.toString() + " to " + rootNode.toString());
                              treeModel.insertNodeInto(n, rootNode, rootNode.getChildCount());
                         }
                         else
                         {
                              System.out.println("add " + n.toString() + " to " + p.toString());
                              treeModel.insertNodeInto(n, p, p.getChildCount());
                         }
                    }//end loop
                    in.close();
                    myTreeClass.setFileName(f.getPath());
                    
                    System.out.println("node count: " + Integer.toString(treeModel.getChildCount(rootNode)));
                    
                    TreeNode [] nodes = treeModel.getPathToRoot(rootNode);
                    TreePath path = new TreePath(nodes);
                    thetree.setSelectionPath(new TreePath(nodes));
                    
                    //System.out.println("call node structure changed");
                    treeModel.nodeStructureChanged(rootNode);

                    myTreeClass.setFileSaved(1);
                    myTreeClass.getParentWindow().setTitle("Scratchpad - " + f.getPath());
               }
               catch(Exception fileopen)
               {
                    System.out.println("something happened with the file");
                    System.out.println(fileopen);
               }
          }//they selected a file and clicked ok
     }//end actionPerformed
     public DefaultMutableTreeNode search(DefaultMutableTreeNode p,String n) 
     { 
          JTree thetree = myTreeClass.getTree();
          DefaultTreeModel treeModel = (DefaultTreeModel)thetree.getModel();
          DefaultMutableTreeNode nameParent = null;
          //TreeModel model = theTree.getModel();
          int index = -1;
          //DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot();
          DataInfo rootInfo = (DataInfo)p.getUserObject();
          if (rootInfo.toString().equals(n))
          {
               //System.out.println("we found it!!");
               return p;
          }
          int childcount = treeModel.getChildCount(p);
          for (int i=0;i<childcount;i++)
          {
               DefaultMutableTreeNode childNode = (DefaultMutableTreeNode)treeModel.getChild(p,i);
               if (childNode.toString().equals(n))
                    nameParent = childNode;
               if (treeModel.isLeaf(childNode))
               {
                    DataInfo childInfo = (DataInfo)childNode.getUserObject();
                    if (childInfo.toString().equals(n))
                         nameParent = childNode;
               }
               else
               {
                    nameParent = search(childNode,n);
               }
          }//end loop
          return nameParent;
     }//end search
     
     private JFrame parentWindow;
     private tree myTreeClass;
}//end OpenAction
