package scratchpad;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JTree;
import javax.swing.tree.*;
import javax.swing.event.*;
import java.net.URL;

public class tree extends JPanel
{
	public tree(JFrame parentWindow)
	{
		parentWindow.setTitle("Scratchpad - untitled.txt");
		
		setLayout(new BorderLayout());
		
		newAction = new NewAction("New",createImageIcon("/new.gif","new icon"),"new",KeyEvent.VK_N,
							 KeyStroke.getKeyStroke(KeyEvent.VK_N,ActionEvent.CTRL_MASK));
		openAction = new OpenAction("Open",createImageIcon("/open.gif","open icon"),"open",KeyEvent.VK_O,
							   KeyStroke.getKeyStroke(KeyEvent.VK_O,ActionEvent.CTRL_MASK));
		saveAction = new SaveAction("Save",createImageIcon("/save.gif","save icon"),"save",KeyEvent.VK_S,
							   KeyStroke.getKeyStroke(KeyEvent.VK_S,ActionEvent.CTRL_MASK));
		saveAsAction = new SaveAsAction("Save As","save as");
		quitAction = new QuitAction("Quit","quit",KeyEvent.VK_Q,
							   KeyStroke.getKeyStroke(KeyEvent.VK_Q,ActionEvent.CTRL_MASK));
		
		findAction = new FindAction("Find","find",KeyEvent.VK_F,
							   KeyStroke.getKeyStroke(KeyEvent.VK_F,ActionEvent.CTRL_MASK));
		cutAction = new CutAction("Cut",createImageIcon("/cut.gif","cut icon"),"save",KeyEvent.VK_T,
							 KeyStroke.getKeyStroke(KeyEvent.VK_X,ActionEvent.CTRL_MASK));
		copyAction = new CopyAction("Copy",createImageIcon("/copy.gif","copy icon"),"save",KeyEvent.VK_C,
							   KeyStroke.getKeyStroke(KeyEvent.VK_C,ActionEvent.CTRL_MASK));
		pasteAction = new PasteAction("Paste",createImageIcon("/paste.gif","paste icon"),"save",KeyEvent.VK_P,
								KeyStroke.getKeyStroke(KeyEvent.VK_V,ActionEvent.CTRL_MASK));
		
		saveArticleAction = new SaveArticleAction("Save Article",createImageIcon("/saveart.jpg","save article icon"),
										  "save article");
		renameAction = new RenameAction("Rename",createImageIcon("/chname.jpg","rename icon"),"rename",
								  KeyStroke.getKeyStroke(KeyEvent.VK_F2,0));
		addNodeAction = new AddNodeAction("Add Item",createImageIcon("/newnode.jpg"," icon"),"add sibling",
										KeyStroke.getKeyStroke(KeyEvent.VK_INSERT,ActionEvent.CTRL_MASK));
		deleteAction = new DeleteAction("Delete",createImageIcon("/del.jpg","delete icon"),"delete",
								  KeyStroke.getKeyStroke(KeyEvent.VK_DELETE,ActionEvent.CTRL_MASK));
		moveUpAction = new MoveUpAction("Move Node Up",createImageIcon("/moveup.jpg","move up icon"),"move up",
								  KeyStroke.getKeyStroke(KeyEvent.VK_U,ActionEvent.ALT_MASK));
		moveDownAction = new MoveDownAction("Move Node Down",createImageIcon("/movedown.jpg","move down icon"),
									 "move down",KeyStroke.getKeyStroke(KeyEvent.VK_D,ActionEvent.ALT_MASK));

		contentsAction = new ContentsAction("Contents", "contents",KeyStroke.getKeyStroke(KeyEvent.VK_F1,0));
		aboutAction = new AboutAction("About", "about",KeyStroke.getKeyStroke(KeyEvent.VK_B,ActionEvent.CTRL_MASK));

		nodeCount = 0;
		
		item = new DataInfo("untitled","");
		rootNode = new DefaultMutableTreeNode(item);

		treeModel = new DefaultTreeModel(rootNode);
		treeModel.addTreeModelListener(new MyListener());
		
		thetree = new JTree(treeModel);
		//thetree.setEditable(false);
		thetree.setShowsRootHandles(true);
		//Where the tree is initialized:
		thetree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		
		JScrollPane treeView = new JScrollPane(thetree);
	
		JTextArea textArea = new JTextArea(5,20);
		JScrollPane textView = new JScrollPane(textArea);
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,treeView,textView);
		this.add(splitPane);
		
	}
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
		
		button = new JButton(newAction);
		toolBar.add(button);
	}//end createToolBar
		
	private JFrame parentWindow;
	private JTree thetree;
	private Action newAction,openAction,saveAction,saveAsAction,quitAction;
	private Action findAction,cutAction,copyAction,pasteAction;
	private Action saveArticleAction,renameAction,addNodeAction,deleteAction,
				moveUpAction,moveDownAction;
	private Action contentsAction,aboutAction;
	
	private DefaultMutableTreeNode rootNode;
	private DefaultTreeModel treeModel;
	private DataInfo item;

	private int nodeCount;
	
	
	
	
	public class NewAction extends AbstractAction
	{
		public NewAction(String text, ImageIcon icon,String desc, int mnemonic, KeyStroke accelerator)
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
	public class OpenAction extends AbstractAction
	{
		public OpenAction(String text, ImageIcon icon,String desc, int mnemonic,KeyStroke accelerator)
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
	public class SaveAction extends AbstractAction
	{
		public SaveAction(String text, ImageIcon icon,String desc, int mnemonic, KeyStroke accelerator)
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
			System.out.println("with the new actions stuff,  " + e.getActionCommand());
		}
	}
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
		public SaveArticleAction(String text, ImageIcon icon,String desc)
		{
			super(text,icon); //text is the actual name
			putValue(SHORT_DESCRIPTION, desc); //used for tooltip text
		}
		public void actionPerformed(ActionEvent e)
		{
			System.out.println("with the new actions stuff,  " + e.getActionCommand());
		}
	}
	public class RenameAction extends AbstractAction
	{
		public RenameAction(String text, ImageIcon icon,String desc,KeyStroke accelerator)
		{
			super(text,icon); //text is the actual name
			putValue(SHORT_DESCRIPTION, desc); //used for tooltip text
			putValue(ACCELERATOR_KEY,accelerator);
		}
		public void actionPerformed(ActionEvent e)
		{
			System.out.println("with the new actions stuff,  " + e.getActionCommand());
		}
	}
	public class AddNodeAction extends AbstractAction
	{
		public AddNodeAction(String text, ImageIcon icon,String desc, KeyStroke accelerator)
		{
			super(text,icon); //text is the actual name
			putValue(SHORT_DESCRIPTION, desc); //used for tooltip text
			putValue(ACCELERATOR_KEY,accelerator);
		}
		public void actionPerformed(ActionEvent e)
		{
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
			thetree.scrollPathToVisible(new TreePath(newNode.getPath()));
			nodeCount++;
		}//end actionPerformed
	}
	
	
	public class DeleteAction extends AbstractAction
	{
		public DeleteAction(String text, ImageIcon icon,String desc, KeyStroke accelerator)
		{
			super(text,icon); //text is the actual name
			putValue(SHORT_DESCRIPTION, desc); //used for tooltip text
			putValue(ACCELERATOR_KEY,accelerator);
		}
		public void actionPerformed(ActionEvent e)
		{
			System.out.println("with the new actions stuff,  " + e.getActionCommand());
		}
	}
	public class MoveUpAction extends AbstractAction
	{
		public MoveUpAction(String text, ImageIcon icon,String desc,KeyStroke accelerator)
		{
			super(text,icon); //text is the actual name
			putValue(SHORT_DESCRIPTION, desc); //used for tooltip text
			putValue(ACCELERATOR_KEY,accelerator);
		}
		public void actionPerformed(ActionEvent e)
		{
			System.out.println("with the new actions stuff,  " + e.getActionCommand());
		}
	}
	public class MoveDownAction extends AbstractAction
	{
		public MoveDownAction(String text, ImageIcon icon,String desc,KeyStroke accelerator)
		{
			super(text,icon); //text is the actual name
			putValue(SHORT_DESCRIPTION, desc); //used for tooltip text
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
			System.out.println("with the new actions stuff,  " + e.getActionCommand());
		}
	}
	public class AboutAction extends AbstractAction
	{
		public AboutAction(String text, String desc,KeyStroke accelerator)
		{
			super(text); //text is the actual name
			putValue(SHORT_DESCRIPTION, desc); //used for tooltip text
			putValue(ACCELERATOR_KEY,accelerator);
		}
		public void actionPerformed(ActionEvent e)
		{
			System.out.println("with the new actions stuff,  " + e.getActionCommand());
		}
	}
	
	public class MyListener implements TreeModelListener
	{
		public void treeNodesChanged(TreeModelEvent e)
		{
			DefaultMutableTreeNode node;
			node = (DefaultMutableTreeNode)(e.getTreePath().getLastPathComponent());
			try
			{
				int index = e.getChildIndices()[0];
				node = (DefaultMutableTreeNode)(node.getChildAt(index));
			}
			catch(NullPointerException npe)
			{
				
			}
		}//end treeNodesChanged
		
		public void treeNodesInserted(TreeModelEvent e) {
		}
		public void treeNodesRemoved(TreeModelEvent e) {
		}
		public void treeStructureChanged(TreeModelEvent e) {
		}

	}

}//end tree class

