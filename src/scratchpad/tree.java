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
import java.awt.Window;
import javax.swing.*;
import javax.swing.JTree;
import javax.swing.tree.*;
import javax.swing.event.*;
import java.net.URL;
import javax.sound.sampled.*;
import java.io.*;
import java.util.*;

public class tree extends JPanel implements KeyListener
{
	public tree(JFrame parentWindow)
	{
		parentWindow.setTitle("Scratchpad - untitled.txt");
		
		setLayout(new BorderLayout());
		
		nodeCount = 0;
		moveCount = 1;
		
		articleSaved = 1;	//at the beginning,  everything is saved
		fileSaved = 1;		//at the beginning,  everything is saved
		
		previousSelectedNode = null;
		
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

		newAction = new NewAction(parentWindow,this,"New","new","New Document",createImageIcon("/new.gif","new icon"),KeyEvent.VK_N,
							 KeyStroke.getKeyStroke(KeyEvent.VK_N,ActionEvent.CTRL_MASK));
		openAction = new OpenAction(parentWindow,this,"Open","open","Open Document",createImageIcon("/open.gif","open icon"),
							   KeyEvent.VK_O,KeyStroke.getKeyStroke(KeyEvent.VK_O,ActionEvent.CTRL_MASK));
		saveAction = new SaveAction(this,"Save","save","Save Document",createImageIcon("/save.gif","save icon"),
							   KeyEvent.VK_S,KeyStroke.getKeyStroke(KeyEvent.VK_S,ActionEvent.CTRL_MASK));
		saveAction.setEnabled(false);
		saveAsAction = new SaveAsAction(parentWindow,this,"Save As","save as");
		quitAction = new QuitAction(parentWindow,"Quit","quit",KeyEvent.VK_Q,
							   KeyStroke.getKeyStroke(KeyEvent.VK_Q,ActionEvent.CTRL_MASK));
		
		findAction = new FindAction("Find","find",KeyEvent.VK_F,
								KeyStroke.getKeyStroke(KeyEvent.VK_F,ActionEvent.CTRL_MASK));
		cutAction = new CutAction(this,"Cut",createImageIcon("/cut.gif","cut icon"),"cut",KeyEvent.VK_T,
								KeyStroke.getKeyStroke(KeyEvent.VK_X,ActionEvent.CTRL_MASK));
		copyAction = new CopyAction(this,"Copy",createImageIcon("/copy.gif","copy icon"),"copy",KeyEvent.VK_C,
								KeyStroke.getKeyStroke(KeyEvent.VK_C,ActionEvent.CTRL_MASK));
		pasteAction = new PasteAction(this,"Paste",createImageIcon("/paste.gif","paste icon"),"paste",KeyEvent.VK_P,
								KeyStroke.getKeyStroke(KeyEvent.VK_V,ActionEvent.CTRL_MASK));
		pasteAction.setEnabled(false);
		pasteSiblingAction = new PasteSiblingAction(this,"Paste Sibling","paste sibling",createImageIcon("/paste.gif","paste icon"),"Paste as Sibling",
								KeyStroke.getKeyStroke(KeyEvent.VK_O,ActionEvent.CTRL_MASK));
		pasteSiblingAction.setEnabled(false);
		
		saveArticleAction = new SaveArticleAction(this,"Save Article","save article","Save Article",createImageIcon("/saveart.jpg","save article icon"));
		saveArticleAction.setEnabled(false);
		renameAction = new RenameAction(this,"Rename","rename","Rename",createImageIcon("/chname.jpg","rename icon"),
								KeyStroke.getKeyStroke(KeyEvent.VK_F2,0));
		addNodeAction = new AddNodeAction(this,"Add Item","additem","Add Item",createImageIcon("/newnode.jpg"," icon"),
								KeyStroke.getKeyStroke(KeyEvent.VK_INSERT,ActionEvent.CTRL_MASK));
		deleteAction = new DeleteAction(parentWindow,this,"Delete","delete","Delete",createImageIcon("/del.jpg","delete icon"),
								KeyStroke.getKeyStroke(KeyEvent.VK_DELETE,ActionEvent.CTRL_MASK));
		moveUpAction = new MoveUpAction(this,"Move Node Up","move up","Move Up",createImageIcon("/moveup.jpg","move up icon"),
								KeyStroke.getKeyStroke(KeyEvent.VK_U,ActionEvent.ALT_MASK));
		moveDownAction = new MoveDownAction(this,"Move Node Down","move down","Move Down",createImageIcon("/movedown.jpg","move down icon"),
								KeyStroke.getKeyStroke(KeyEvent.VK_D,ActionEvent.ALT_MASK));

		contentsAction = new ContentsAction("Contents", "contents",KeyStroke.getKeyStroke(KeyEvent.VK_F1,0));
		
		aboutAction = new AboutAction(parentWindow,"About", "about","About Scratchpad",KeyStroke.getKeyStroke(KeyEvent.VK_B,ActionEvent.CTRL_MASK));

		JScrollPane treeView = new JScrollPane(thetree);
	
		textPane = new JTextPane();
		textPane.addKeyListener(this);
		JScrollPane textView = new JScrollPane(textPane);
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,treeView,textView);
		this.add(splitPane);
		
		//create a popup menu
		createPopupMenu();
		
		//remove this line later
		populate();
		
		//initally select the root node
		TreePath p = thetree.getPathForRow(0);
		thetree.setSelectionPath(p);

	}//end constructor
	
	protected ImageIcon createImageIcon(String path, String description)
	{
		URL imgUrl = getClass().getResource("/resources"+path);
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
		
		Action [] editActions = {findAction,cutAction,copyAction,pasteAction,pasteSiblingAction};
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
	}//end createMenuBar
	
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
		
	public void createPopupMenu()
	{
		popup = new JPopupMenu();
		JMenuItem item;
		Action [] popupActions = {cutAction,copyAction,pasteAction,pasteSiblingAction};
		for(int i=0;i<popupActions.length;i++)
		{
			item = new JMenuItem(popupActions[i]);
			popup.add(item);
		}//end loop
		//create mouse listener
		popupListener = new PopupListener();
		//add mouse listener to the tree
		thetree.addMouseListener(popupListener);
		popup.addMouseListener(popupListener);
	}//end createPopupMenu
		
	
	/** Handle the key typed event from the text field. */
	public void keyTyped(KeyEvent e) 
	{
		//articleSaved = 0;
		//fileSaved = 0;
		setArticleSaved(0);
		setFileSaved(0);
		//saveArticleAction.setEnabled(true);
		//saveAction.setEnabled(true);
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
		if (i == 0)
		{
			saveAction.setEnabled(true);
			saveArticleAction.setEnabled(true);
		}
		else
		{
			saveAction.setEnabled(false);
			saveArticleAction.setEnabled(false);
		}
	}
	
	public int getArticleSaved()
	{
		return articleSaved;
	}
	
	public void setFileSaved(int i)
	{
		fileSaved = i;
		if (i == 0)
		{
			//enable the button?
			saveAction.setEnabled(true);
		}
		else
			saveAction.setEnabled(false);
	}
	
	public int getFileSaved()
	{
		return fileSaved;
	}
	public JTree getTree()
	{
		return thetree;
	}
	
	public void setFileName(String s)
	{
		theFile = s;
	}
	
	public String getFileName()
	{
		return theFile;
	}
	
	public JFrame getParentWindow()
	{
		return parentWindow;
	}
	
	public void enablePaste()
	{
		pasteAction.setEnabled(true);
		pasteSiblingAction.setEnabled(true);
	}
	
	public void disablePaste()
	{
		pasteAction.setEnabled(false);
		pasteSiblingAction.setEnabled(false);
	}
	
	public JPopupMenu getPopupMenu()
	{
		return popup;
	}
	
	public void setMovingNode(DefaultMutableTreeNode n)
	{
		movingNode = n;
	}
	
	public DefaultMutableTreeNode getMovingNode()
	{
		return movingNode;
	}
	
	public void setMoveCount(int i)
	{
		moveCount = i;
	}
	
	public int getMoveCount()
	{
		return moveCount;
	}
	
	public String getTextPaneText()
	{
		return textPane.getText();
	}
	
	public void setTextPaneText(String s)
	{
		textPane.setText(s);
	}
	
	public int getNodeCount()
	{
		return nodeCount;
	}
	
	public void setNodeCount(int i)
	{
		nodeCount = i;
	}
	
	private JFrame parentWindow;
	private JTree thetree;
	private JTextPane textPane;
	private Action newAction,openAction,saveAction,saveAsAction,quitAction;
	private Action findAction,cutAction,copyAction,pasteAction,pasteSiblingAction;
	private Action saveArticleAction,renameAction,addNodeAction,deleteAction,
				moveUpAction,moveDownAction;
	private Action contentsAction,aboutAction;
	
	private DefaultMutableTreeNode rootNode;
	private DefaultTreeModel treeModel;
	private DataInfo item;

	private int nodeCount;	//add this number to the name of the newly created nodes by default
	private int moveCount;	//add this number when pasting copied nodes
	
	private DefaultMutableTreeNode previousSelectedNode;
	private DefaultMutableTreeNode movingNode;
	
	private int articleSaved;	//keeps track if the article is saved or not
	private int fileSaved;		//keeps track if the file is saved or not
	
	private String theFile = "";
	
	private JPopupMenu popup;
	private PopupListener popupListener;
	
	
	 void populate() 
	 {

		DefaultMutableTreeNode a = new DefaultMutableTreeNode(new DataInfo("Apple","this is an apple"));
		treeModel.insertNodeInto(a, rootNode, rootNode.getChildCount());

		DefaultMutableTreeNode m = new DefaultMutableTreeNode(new DataInfo("Mango","this is a mango"));
		treeModel.insertNodeInto(m, rootNode, rootNode.getChildCount());

		DefaultMutableTreeNode g = new DefaultMutableTreeNode(new DataInfo("Guava","this is something"));
		treeModel.insertNodeInto(g, rootNode, rootNode.getChildCount());

		thetree.expandPath(new TreePath(rootNode.getPath()));

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
			System.out.println("selection event: " + e.getPath());
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)e.getPath().getLastPathComponent();
			System.out.println(node.toString());
			System.out.println("selection event children: " + Integer.toString(node.getChildCount()));
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
					System.out.println("save the changes to the previousSelectedNode if its not null");
					if (previousSelectedNode != null)
					{
						System.out.println("prev string: " + previousSelectedNode.toString());
						DataInfo d = (DataInfo)previousSelectedNode.getUserObject();
						d.setData(textPane.getText());
						System.out.println("update the textPane with the new node's data");
						d = (DataInfo)node.getUserObject();
						textPane.setText(d.getData());
						//saveArticleAction.setEnabled(false);
					}
					else
					{
						System.out.println("root node since prev node was null " + rootNode.toString());
						DataInfo d = (DataInfo)rootNode.getUserObject();
						d.setData(textPane.getText());
						System.out.println("root node since prev node was null, nodes data");
						d = (DataInfo)node.getUserObject();
						textPane.setText(d.getData());
						//saveArticleAction.setEnabled(false);
					}
				}
				else
				{
					System.out.println("else part getting the nodes object");
					System.out.println(node.toString());
					DataInfo d = (DataInfo)node.getUserObject();
					textPane.setText(d.getData());
					//saveArticleAction.setEnabled(false);
					System.out.println("else part setting the data in the text area");
				}
				articleSaved = 1;
			}//end article wasn't saved
		}//end valueChanged
	}//end class MyMouseListener

}//end tree class

