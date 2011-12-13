package scratchpad;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.beans.*;

public class Rename extends JDialog
{
	public Rename ()
	{
		Action okButtonAction = new OkButtonAction("Ok",this);
		Action cancelButtonAction = new CancelButtonAction("Cancel");
		
		okButton = new JButton(okButtonAction);
		cancelButton = new JButton(cancelButtonAction);

		renameField = new JTextField(15);
		label = new JLabel("New Name:");
		newName = "";
		
		JPanel namePanel = new JPanel();
		namePanel.add(label);
		namePanel.add(renameField);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);
		
		JPanel everything = new JPanel();
		everything.setLayout(new BoxLayout(everything,BoxLayout.PAGE_AXIS));
		
		everything.add(namePanel);
		everything.add(buttonPanel);
		
		setContentPane(everything);
		setTitle("Rename Item");
		setModal(true);
		pack();
		setMinimumSize(new Dimension(250,150));
	}
	
	private JTextField renameField;
	private JButton okButton;
	private JButton cancelButton;
	private JLabel label;
	private String newName;
	
	public String getName()
	{
		return newName;
	}
	
	public void setName(String n)
	{
		renameField.setText(n);
		renameField.requestFocus();
		renameField.selectAll();
	}
	

	class OkButtonAction extends AbstractAction
	{
		public OkButtonAction(String t,JDialog p)
		{
			super(t);
			parent = p;
		}
		public void actionPerformed(ActionEvent e)
		{
			if (renameField.getText().equals("") != true)
			{
				newName = renameField.getText();
				renameField.setText("");
				setVisible(false);
			}
			else
			{
				System.out.println("tell them to type in a name!");
				JOptionPane.showMessageDialog(parent,"Please type in a name.","Error!",
										JOptionPane.ERROR_MESSAGE);
			}
		}
		private JDialog parent;
	}

	class CancelButtonAction extends AbstractAction
	{
		public CancelButtonAction(String t)
		{
			super(t);
		}
		public void actionPerformed(ActionEvent e)
		{
			renameField.setText("");
			newName = "";
			setVisible(false);
		}
	}


}//end Rename class