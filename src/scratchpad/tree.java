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

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JTree;
import javax.swing.tree.*;
import javax.swing.event.*;
import java.net.URL;
import javax.sound.sampled.*;

public class tree extends JPanel implements KeyListener
{
	public tree(JFrame parentWindow)
	{
		parentWindow.setTitle("Scratchpad - untitled.txt");
		
		setLayout(new BorderLayout());
		
		nodeCount = 0;
		
		articleSaved = 1;	//at the beginning,  everything is saved
		fileSaved = 1;		//at the beginning,  everything is saved
		
		previousSelectedNode = new DefaultMutableTreeNode("previous");
		
		item = new DataInfo("untitled","nothing to see here");
		rootNode = new DefaultMutableTreeNode(item);

		treeModel = new DefaultTreeModel(rootNode);
		treeModel.addTreeModelListener(new MyListener());
		
		thetree = new JTree(treeModel);
		//thetree.setEditable(true);
		thetree.setShowsRootHandles(true);
		//Where the tree is initialized:
		thetree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		thetree.addTreeSelectionListener(new MyMouseListener());
		
		thetree.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_F2,0),"rename");
		thetree.getActionMap().put("rename",renameAction);
		
		thetree.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_INSERT,ActionEvent.CTRL_MASK),"add item");
		thetree.getActionMap().put("add item",addNodeAction);
		
		thetree.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE,ActionEvent.CTRL_MASK),"delete");
		thetree.getActionMap().put("delete",deleteAction);
		
		
		thetree.getActionMap().put("cut",cutAction);

		newAction = new NewAction("New","new","New Document",createImageIcon("/new.gif","new icon"),KeyEvent.VK_N,
							 KeyStroke.getKeyStroke(KeyEvent.VK_N,ActionEvent.CTRL_MASK));
		openAction = new OpenAction("Open","open","Open Document",createImageIcon("/open.gif","open icon"),
							   KeyEvent.VK_O,KeyStroke.getKeyStroke(KeyEvent.VK_O,ActionEvent.CTRL_MASK));
		saveAction = new SaveAction("Save","save","Save Document",createImageIcon("/save.gif","save icon"),
							   KeyEvent.VK_S,KeyStroke.getKeyStroke(KeyEvent.VK_S,ActionEvent.CTRL_MASK));
		saveAsAction = new SaveAsAction("Save As","save as");
		quitAction = new QuitAction("Quit","quit",KeyEvent.VK_Q,
							   KeyStroke.getKeyStroke(KeyEvent.VK_Q,ActionEvent.CTRL_MASK));
		
		findAction = new FindAction("Find","find",KeyEvent.VK_F,
								KeyStroke.getKeyStroke(KeyEvent.VK_F,ActionEvent.CTRL_MASK));
		cutAction = new CutAction("Cut",createImageIcon("/cut.gif","cut icon"),"cut",KeyEvent.VK_T,
								KeyStroke.getKeyStroke(KeyEvent.VK_X,ActionEvent.CTRL_MASK));
		copyAction = new CopyAction("Copy",createImageIcon("/copy.gif","copy icon"),"copy",KeyEvent.VK_C,
								KeyStroke.getKeyStroke(KeyEvent.VK_C,ActionEvent.CTRL_MASK));
		pasteAction = new PasteAction("Paste",createImageIcon("/paste.gif","paste icon"),"paste",KeyEvent.VK_P,
								KeyStroke.getKeyStroke(KeyEvent.VK_V,ActionEvent.CTRL_MASK));
		
		saveArticleAction = new SaveArticleAction("Save Article","save article","Save Article",createImageIcon("/saveart.jpg","save article icon"));
		saveArticleAction.setEnabled(false);
		renameAction = new RenameAction("Rename","rename","Rename",createImageIcon("/chname.jpg","rename icon"),
								KeyStroke.getKeyStroke(KeyEvent.VK_F2,0));
		addNodeAction = new AddNodeAction("Add Item","additem","Add Item",createImageIcon("/newnode.jpg"," icon"),
								KeyStroke.getKeyStroke(KeyEvent.VK_INSERT,ActionEvent.CTRL_MASK));
		deleteAction = new DeleteAction(parentWindow,this,"Delete","delete","Delete",createImageIcon("/del.jpg","delete icon"),
								KeyStroke.getKeyStroke(KeyEvent.VK_DELETE,ActionEvent.CTRL_MASK));
		moveUpAction = new MoveUpAction("Move Node Up","move up","Move Up",createImageIcon("/moveup.jpg","move up icon"),
								KeyStroke.getKeyStroke(KeyEvent.VK_U,ActionEvent.ALT_MASK));
		moveDownAction = new MoveDownAction("Move Node Down","move down","Move Down",createImageIcon("/movedown.jpg","move down icon"),
								KeyStroke.getKeyStroke(KeyEvent.VK_D,ActionEvent.ALT_MASK));

		contentsAction = new ContentsAction("Contents", "contents",KeyStroke.getKeyStroke(KeyEvent.VK_F1,0));
		
		aboutAction = new AboutAction("About", "about","About Scratchpad",KeyStroke.getKeyStroke(KeyEvent.VK_B,ActionEvent.CTRL_MASK));

		JScrollPane treeView = new JScrollPane(thetree);
	
		textPane = new JTextPane();
		textPane.addKeyListener(this);
		JScrollPane textView = new JScrollPane(textPane);
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,treeView,textView);
		this.add(splitPane);
		
		//remove this line later
		populate();
		
		//initally select the root node
		TreePath p = thetree.getPathForRow(0);
		thetree.setSelectionPath(p);

	}//end constructor
	
	protected ImageIcon createImageIcon(String path, String description)
	{
		URL imgUrl = getClass().getResource(path);
		if (imgUrl != null)
			return new ImageIcon(imgUrl,description);
		else
			return null;
	}
	
	public JMenuBar createMenuBar()
	{
		JMenuBar menuBar = new JMenuBar();		
		JMenuItem item = null;

		//build the file menu
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		
		Action [] fileActions = {newAction,openAction,saveAction,saveAsAction,quitAction};
		for(int i=0;i<fileActions.length;i++)
		{
			item = new JMenuItem(fileActions[i]);
			//fileMenu.add(item);
			if (item.getText().equals("New"))
			{
				fileMenu.add(item);
				fileMenu.addSeparator();
			}
			else if (item.getText().equals("Quit"))
			{
				fileMenu.addSeparator();
				fileMenu.add(item);
			}
			else
			{
				fileMenu.add(item);
			}
		}//end loop
		
		JMenu editMenu = new JMenu("Edit");
		editMenu.setMnemonic(KeyEvent.VK_E);
		
		Action [] editActions = {findAction,cutAction,copyAction,pasteAction};
		for(int i=0;i<editActions.length;i++)
		{
			item = new JMenuItem(editActions[i]);
			//editMenu.add(item);
			if (item.getText().equals("Find"))
			{
				editMenu.add(item);
				editMenu.addSeparator();
			}
			else
			{
				editMenu.add(item);
			}
		}//end loop
		
		JMenu toolsMenu = new JMenu("Options");
		toolsMenu.setMnemonic(KeyEvent.VK_O);
		Action [] optionsActions = {saveArticleAction,renameAction,
			addNodeAction,deleteAction,moveUpAction,moveDownAction
		};
		for(int i=0;i<optionsActions.length;i++)
		{
			item = new JMenuItem(optionsActions[i]);
			//toolsMenu.add(item);
			if (item.getText().equals("Save Article") || item.getText().equals("Delete") )
			{
				toolsMenu.add(item);
				toolsMenu.addSeparator();
			}
			else
			{
				toolsMenu.add(item);
			}
		}//end loop

		JMenu helpMenu = new JMenu("Help");
		helpMenu.setMnemonic(KeyEvent.VK_O);
		Action [] helpActions = {contentsAction,aboutAction};
		for(int i=0;i<helpActions.length;i++)
		{
			item = new JMenuItem(helpActions[i]);
			if (item.getText().equals("About"))
			{
				helpMenu.addSeparator();
				helpMenu.add(item);
			}
			else
			{
				helpMenu.add(item);
			}
		}//end loop


		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		menuBar.add(toolsMenu);
		menuBar.add(helpMenu);
		return menuBar;
	}
	
	public void createToolBar()
	{
		JButton button = null;

		JToolBar toolBar = new JToolBar();
		add(toolBar,BorderLayout.PAGE_START);

		Action [] toolbarActions = {newAction,openAction,saveAction,renameAction,addNodeAction,
					deleteAction,moveUpAction,moveDownAction,saveArticleAction,aboutAction};
					
		for (int i=0;i<toolbarActions.length;i++)
		{
			button = new JButton(toolbarActions[i]);
			if (!button.getActionCommand().equals("about"))
			{
				button.setText("");
			}
			else
			{
				toolBar.addSeparator();
			}
			if (button.getActionCommand().equals("new") || 
				button.getActionCommand().equals("save") ||
				button.getActionCommand().equals("delete") ||
				button.getActionCommand().equals("move down")
			)
			{
				toolBar.add(button);
				toolBar.addSeparator();
			}
			else
			{
				toolBar.add(button);
			}
/*			else  if (!button.getText().equals("About")
			{
				button.setText("")
				toolBar.add(button);
				//toolBar.addSeparator();
			}*/
			
		}
	}//end createToolBar
		
	/** Handle the key typed event from the text field. */
	public void keyTyped(KeyEvent e) 
	{
		articleSaved = 0;
		fileSaved = 0;
		saveArticleAction.setEnabled(true);
		System.out.println("this node was changed: ");
		TreePath path = thetree.getSelectionPath();
		if (path != null)
		{
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
			previousSelectedNode = node;
			System.out.println(previousSelectedNode.toString());
		}
	}//end keyTyped
	
	/** Handle the key pressed event from the text field. */
	public void keyPressed(KeyEvent e) 
	{
	}

	/** Handle the key released event from the text field. */
	public void keyReleased(KeyEvent e) 
	{
	}
		
	public void setArticleSaved(int i)
	{
		articleSaved = i;
	}
	
	public int getArticleSaved()
	{
		return articleSaved;
	}
	
	public void setFileSaved(int i)
	{
		fileSaved = i;
	}
	
	public int getFileSaved()
	{
		return fileSaved;
	}
	public JTree getTree()
	{
		return thetree;
	}
		
	private JFrame parentWindow;
	private JTree thetree;
	private JTextPane textPane;
	private Action newAction,openAction,saveAction,saveAsAction,quitAction;
	private Action findAction,cutAction,copyAction,pasteAction;
	private Action saveArticleAction,renameAction,addNodeAction,deleteAction,
				moveUpAction,moveDownAction;
	private Action contentsAction,aboutAction;
	
	private DefaultMutableTreeNode rootNode;
	private DefaultTreeModel treeModel;
	private DataInfo item;

	private int nodeCount;	//add this number to the name of the newly created nodes by default
	
	private DefaultMutableTreeNode previousSelectedNode;
	
	private int articleSaved;	//keeps track if the article is saved or not
	private int fileSaved;		//keeps track if the file is saved or not
	
	public class NewAction extends AbstractAction
	{
		public NewAction(String text, String actionCmd,String toolTip, ImageIcon icon, 
					  int mnemonic, KeyStroke accelerator)		
		//public NewAction(String text, ImageIcon icon,String desc, int mnemonic, KeyStroke accelerator)
		{
			super(text,icon); //text is the actual name
			putValue(ACTION_COMMAND_KEY,actionCmd);	//set the actionCommand
			putValue(SHORT_DESCRIPTION, toolTip); //set tooltiptext
			putValue(MNEMONIC_KEY, mnemonic);	//set the underlined letter in the text
			putValue(ACCELERATOR_KEY,accelerator);	//set the keyboard shortcut
		}
		public void actionPerformed(ActionEvent e)
		{
			System.out.println("with the new actions stuff,  " + e.getActionCommand());
			System.out.println("empty the table model, and clear the jtree");
			System.out.println(Integer.toString(treeModel.getChildCount()));
System.out.println("someting is trigering hte save dialog box");


			//DefaultMutableTreeNode newTree = new DefaultMutableTreeNode(new DataInfo("new tree","brand new tree"));
			//treeModel.setRoot(newTree);
			//treeModel.nodeStructureChanged(newTree);
			//thetree.setModel(treeModel);
			//thetree.setSelectionPath(new TreePath(treeModel.getPathToRoot(newTree)));
		}
	}	
	public class OpenAction extends AbstractAction
	{
		public OpenAction(String text, String actionCmd,String toolTip,ImageIcon icon,
					   int mnemonic,KeyStroke accelerator)
		{
			super(text,icon); //text is the actual name
			putValue(ACTION_COMMAND_KEY,actionCmd);	//action command
			putValue(SHORT_DESCRIPTION, toolTip); //used for tooltip text
			putValue(MNEMONIC_KEY, mnemonic);
			putValue(ACCELERATOR_KEY,accelerator);
		}
		public void actionPerformed(ActionEvent e)
		{
			System.out.println("with the new actions stuff,  " + e.getActionCommand());
		}
	}		
	public class SaveAction extends AbstractAction
	{
		public SaveAction(String text, String actionCmd,String toolTip,ImageIcon icon,
					   int mnemonic, KeyStroke accelerator)
		{
			super(text,icon); //text is the actual name
			putValue(ACTION_COMMAND_KEY,actionCmd);	//action command
			putValue(SHORT_DESCRIPTION, toolTip); //used for tooltip text
			putValue(MNEMONIC_KEY, mnemonic);
			putValue(ACCELERATOR_KEY,accelerator);
		}
		public void actionPerformed(ActionEvent e)
		{
			System.out.println("with the new actions stuff,  " + e.getActionCommand());
		}
	}	
	public class SaveAsAction extends AbstractAction
	{
		public SaveAsAction(String text,String desc)
		{
			super(text); //text is the actual name
			putValue(SHORT_DESCRIPTION, desc); //used for tooltip text
		}
		public void actionPerformed(ActionEvent e)
		{
			System.out.println("with the new actions stuff,  " + e.getActionCommand());
		}
	}	
	public class QuitAction extends AbstractAction
	{
		public QuitAction(String text,String desc, int mnemonic,KeyStroke accelerator)
		{
			super(text); //text is the actual name
			putValue(SHORT_DESCRIPTION, desc); //used for tooltip text
			putValue(MNEMONIC_KEY, mnemonic);
			putValue(ACCELERATOR_KEY,accelerator);
		}
		public void actionPerformed(ActionEvent e)
		{
			System.out.println("with the new actions stuff,  " + e.getActionCommand());
		}
	}	

	public class FindAction extends AbstractAction
	{
		public FindAction(String text, String desc, int mnemonic,KeyStroke accelerator)
		{
			super(text); //text is the actual name
			putValue(SHORT_DESCRIPTION, desc); //used for tooltip text
			putValue(MNEMONIC_KEY, mnemonic);
			putValue(ACCELERATOR_KEY,accelerator);
		}
		public void actionPerformed(ActionEvent e)
		{
			System.out.println("with the new actions stuff,  " + e.getActionCommand());
		}
	}	
	public class CutAction extends AbstractAction
	{
		public CutAction(String text, ImageIcon icon,String desc, int mnemonic,KeyStroke accelerator)
		{
			super(text,icon); //text is the actual name
			putValue(SHORT_DESCRIPTION, desc); //used for tooltip text
			putValue(MNEMONIC_KEY, mnemonic);
			putValue(ACCELERATOR_KEY,accelerator);
		}
		public void actionPerformed(ActionEvent e)
		{
			//System.out.println("with the new actions stuff,  " + e.getActionCommand());
			//System.out.println("delete?,  " + e.getActionCommand());
			DefaultTreeModel treeModel = (DefaultTreeModel)thetree.getModel();
			int j;
			DefaultMutableTreeNode selectedNode = null;
			TreePath selectedNodePath = thetree.getSelectionPath();
			if (selectedNodePath != null)
			{
				selectedNode = (DefaultMutableTreeNode)(selectedNodePath.getLastPathComponent());
				System.out.println("change color of " + selectedNode.toString());
				previousSelectedNode = selectedNode;
			}
		}//end actionPerformed
	}//end CutAction class
	public class CopyAction extends AbstractAction
	{
		public CopyAction(String text, ImageIcon icon,String desc, int mnemonic,KeyStroke accelerator)
		{
			super(text,icon); //text is the actual name
			putValue(SHORT_DESCRIPTION, desc); //used for tooltip text
			putValue(MNEMONIC_KEY, mnemonic);
			putValue(ACCELERATOR_KEY,accelerator);
		}
		public void actionPerformed(ActionEvent e)
		{
			System.out.println("with the new actions stuff,  " + e.getActionCommand());
		}
	}
	public class PasteAction extends AbstractAction
	{
		public PasteAction(String text, ImageIcon icon,String desc, int mnemonic,KeyStroke accelerator)
		{
			super(text,icon); //text is the actual name
			putValue(SHORT_DESCRIPTION, desc); //used for tooltip text
			putValue(MNEMONIC_KEY, mnemonic);
			putValue(ACCELERATOR_KEY,accelerator);
		}
		public void actionPerformed(ActionEvent e)
		{
			System.out.println("with the new actions stuff,  " + e.getActionCommand());
		}
	}

	public class SaveArticleAction extends AbstractAction
	{
		public SaveArticleAction(String text, String actionCmd,String toolTip,ImageIcon icon)
		{
			super(text,icon); //text is the actual name
			putValue(ACTION_COMMAND_KEY,actionCmd);
			putValue(SHORT_DESCRIPTION, toolTip); //used for tooltip text
		}
		public void actionPerformed(ActionEvent e)
		{
			System.out.println("with the new actions stuff,  " + e.getActionCommand());
			TreePath path = thetree.getSelectionPath();
			if (path != null)
			{
				DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
				//System.out.println("before: " + node.toString());
				DataInfo d = (DataInfo)(node.getUserObject());
				System.out.println(node.toString());
				d.setData(textPane.getText());
				System.out.println(textPane.getText());
				articleSaved = 1;
				saveArticleAction.setEnabled(false);
			}
		}
	}//end SaveArticleAction
	
	public class RenameAction extends AbstractAction
	{
		public RenameAction(String text, String actionCmd,String toolTip,ImageIcon icon,KeyStroke accelerator)
		{
			super(text,icon); //text is the actual name
			putValue(ACTION_COMMAND_KEY,actionCmd);
			putValue(SHORT_DESCRIPTION, toolTip); //used for tooltip text
			putValue(ACCELERATOR_KEY,accelerator);
		}
		public void actionPerformed(ActionEvent e)
		{
			//System.out.println("with the new actions stuff,  " + e.getActionCommand());
			TreePath path = thetree.getSelectionPath();
			if (path != null)
			{
				DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
				Rename renameDialog = new Rename();
				renameDialog.setName(node.toString());
				renameDialog.setVisible(true);
				thetree.setEditable(true);
				//System.out.println("before: " + node.toString());
				thetree.startEditingAtPath(path);
				if (renameDialog.getName().equals("") != true)
				{
					//System.out.println("new name will be: " + renameDialog.getName());
					//node.setName(renameDialog.getName());
					DataInfo n = (DataInfo)node.getUserObject();
					n.setName(renameDialog.getName());
					fileSaved = 0;
				}
				thetree.setEditable(false);
			}
		}
	}
	public class AddNodeAction extends AbstractAction
	{
		public AddNodeAction(String text, String actionCmd,String toolTip,ImageIcon icon,KeyStroke accelerator)
		{
			super(text,icon); //text is the actual name
			putValue(ACTION_COMMAND_KEY,actionCmd);
			putValue(SHORT_DESCRIPTION, toolTip); //used for tooltip text
			putValue(ACCELERATOR_KEY,accelerator);
		}
		public void actionPerformed(ActionEvent e)
		{
			//System.out.println("add node command: " + e.getActionCommand());
			DataInfo n = new DataInfo("new node " + Integer.toString(nodeCount),"");
			DefaultMutableTreeNode parentNode = null;
			TreePath parentPath = thetree.getSelectionPath();

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
			fileSaved = 0;
			thetree.scrollPathToVisible(new TreePath(newNode.getPath()));
			nodeCount++;
		}//end actionPerformed
	}
	
	 void populate() {
	 
	 DefaultMutableTreeNode a = new DefaultMutableTreeNode(new DataInfo("Apple","this is an apple"));
	 treeModel.insertNodeInto(a, rootNode, rootNode.getChildCount());
	 
	 DefaultMutableTreeNode m = new DefaultMutableTreeNode(new DataInfo("Mango","this is a mango"));
	 treeModel.insertNodeInto(m, rootNode, rootNode.getChildCount());
 
	 DefaultMutableTreeNode g = new DefaultMutableTreeNode(new DataInfo("Guava","this is something"));
	 treeModel.insertNodeInto(g, rootNode, rootNode.getChildCount());
 
	}
	
	public class MoveUpAction extends AbstractAction
	{
		public MoveUpAction(String text, String actionCmd,String toolTip,ImageIcon icon,KeyStroke accelerator)
		{
			super(text,icon); //text is the actual name
			putValue(ACTION_COMMAND_KEY,actionCmd);
			putValue(SHORT_DESCRIPTION, toolTip); //used for tooltip text
			putValue(ACCELERATOR_KEY,accelerator);
		}
		public void actionPerformed(ActionEvent e)
		{
			DefaultMutableTreeNode selectedNode = null;
			TreePath selectedNodePath = thetree.getSelectionPath();
			if (selectedNodePath != null)
			{
				selectedNode = (DefaultMutableTreeNode)(selectedNodePath.getLastPathComponent());
				//System.out.println(selectedNode.toString());
				DefaultMutableTreeNode selectedNodeParent = (DefaultMutableTreeNode)selectedNode.getParent();
				//System.out.println("parent:"+selectedNodeParent.toString());
				int index = treeModel.getIndexOfChild(selectedNodeParent,selectedNode);
				if (index >0)
				{
					//System.out.println("move the node");
					treeModel.removeNodeFromParent(selectedNode);
					treeModel.insertNodeInto(selectedNode,selectedNodeParent,index-1);
					fileSaved = 0;
				}
			}//no selected node
		}//end actionPerformed
	}
	public class MoveDownAction extends AbstractAction
	{
		public MoveDownAction(String text, String actionCmd,String toolTip,ImageIcon icon,KeyStroke accelerator)
		{
			super(text,icon); //text is the actual name
			putValue(ACTION_COMMAND_KEY,actionCmd);
			putValue(SHORT_DESCRIPTION, toolTip); //used for tooltip text
			putValue(ACCELERATOR_KEY,accelerator);
		}
		public void actionPerformed(ActionEvent e)
		{
			System.out.println("with the new actions stuff,  " + e.getActionCommand());
			DefaultMutableTreeNode selectedNode = null;
			TreePath selectedNodePath = thetree.getSelectionPath();
			if (selectedNodePath != null)
			{
				selectedNode = (DefaultMutableTreeNode)(selectedNodePath.getLastPathComponent());
				//System.out.println(selectedNode.toString());
				DefaultMutableTreeNode selectedNodeParent = (DefaultMutableTreeNode)selectedNode.getParent();
				//System.out.println("parent:"+selectedNodeParent.toString());
				int index = treeModel.getIndexOfChild(selectedNodeParent,selectedNode);
				if (index < (treeModel.getChildCount(selectedNodeParent)-1) )
				{
					//System.out.println("move the node");
					treeModel.removeNodeFromParent(selectedNode);
					treeModel.insertNodeInto(selectedNode,selectedNodeParent,index+1);
					fileSaved = 0;
				}
			}//no selected node
		}//end actionPerformed
	}//end class MoveDownAction
	
	public class ContentsAction extends AbstractAction
	{
		public ContentsAction(String text, String desc,KeyStroke accelerator)
		{
			super(text); //text is the actual name
			putValue(SHORT_DESCRIPTION, desc); //used for tooltip text
			putValue(ACCELERATOR_KEY,accelerator);
		}
		public void actionPerformed(ActionEvent e)
		{
			System.out.println("show the help files,  " + e.getActionCommand());
		}
	}
	public class AboutAction extends AbstractAction
	{
		//(text,action command, tooltip,keystrokc
		public AboutAction(String text, String cmd,String toolTip,KeyStroke accelerator)
		{
			super(text); //text is the actual name
			putValue(ACTION_COMMAND_KEY,cmd);	//set action command
			putValue(SHORT_DESCRIPTION, toolTip); //used for tooltip text
			putValue(ACCELERATOR_KEY,accelerator);
		}
		public void actionPerformed(ActionEvent e)
		{
			System.out.println("show the about dialog box" + e.getActionCommand());
			About about = new About(parentWindow);
			about.display();
		}
	}
	
	public class MyListener implements TreeModelListener
	{
		public void treeNodesChanged(TreeModelEvent e)
		{
			DefaultMutableTreeNode node;
			node = (DefaultMutableTreeNode)(e.getTreePath().getLastPathComponent());
			//DataInfo n = (DataInfo)node.getUserObject();
			
			//System.out.println("before name: " + node.toString());
			try
			{
				int index = e.getChildIndices()[0];
				node = (DefaultMutableTreeNode)(node.getChildAt(index));
				//System.out.println("node changed: " +e.toString());
			}
			catch(NullPointerException npe)
			{
				
			}
			//System.out.println("user finished editing");
			//System.out.println("new value: " + node.getUserObject());
		}//end treeNodesChanged
		
		public void treeNodesInserted(TreeModelEvent e) 
		{
			System.out.println("node inserted");
		}
		public void treeNodesRemoved(TreeModelEvent e) 
		{
			System.out.println("node removed");
		}
		public void treeStructureChanged(TreeModelEvent e) 
		{
			System.out.println("structure changed");
		}

	}//end MyListener
	
	public class MyMouseListener implements TreeSelectionListener
	{
		public void valueChanged(TreeSelectionEvent e)
		{
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)thetree.getLastSelectedPathComponent();
			if (node != null && articleSaved == 1)
			{
				DataInfo n = (DataInfo)node.getUserObject();
				textPane.setText(n.getData());
			}
			//the article wasn't saved! 
			else
			{
				System.out.println("ask to save changes");
				int n = JOptionPane.showConfirmDialog(parentWindow,
						"Do you wish to save your changes first?",
						"Save changes",
						JOptionPane.YES_NO_OPTION
				);
				System.out.println(Integer.toString(n));
				if (n == 0)
				{
					System.out.println("save the changes to the previousSelectedNode");
					System.out.println(previousSelectedNode.toString());
					DataInfo d = (DataInfo)previousSelectedNode.getUserObject();
					d.setData(textPane.getText());
					System.out.println("update the textPane with the new node's data");
					d = (DataInfo)node.getUserObject();
					textPane.setText(d.getData());
				}
				else
				{
					DataInfo d = (DataInfo)node.getUserObject();
					textPane.setText(d.getData());
				}
				articleSaved = 1;
			}//end article wasn't saved
		}//end valueChanged
	}//end class MyMouseListener

}//end tree class

