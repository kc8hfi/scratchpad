package scratchpad;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class Rename extends JDialog
{
	public Rename (String currentText)
	{
		renameField = new JTextField(currentText);
	}
	
	private JTextField renameField;
	
}//end Rename class