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
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.net.URL;

public class About extends JDialog
{
	public About(JFrame p)
	{
		d = new JDialog(p,"About Scratchpad");
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		JPanel labels = new JPanel();
		labels.setLayout(new BoxLayout(labels,BoxLayout.Y_AXIS));
		
		JLabel authorLabel = new JLabel("Author: " + author);
		authorLabel.setFont(new Font("Monospaced",Font.PLAIN,20));
		//authorLabel.setHorizontalTextPosition(JLabel.LEFT);
		labels.add(authorLabel);
		
		JLabel versionLabel = new JLabel("Version: " + version);
		versionLabel.setFont(new Font("Monospaced",Font.PLAIN,20));
		labels.add(versionLabel);
		
		panel.add(labels,BorderLayout.PAGE_START);
		
				
		JTextArea textArea = new JTextArea(10,10);
		JScrollPane textView = new JScrollPane(textArea);
		textArea.setText(license());
		textArea.setEditable(false);
		textArea.setCaretPosition(0);
		panel.add(textView,BorderLayout.CENTER);
		
		
		JPanel icons = new JPanel();
		
		String [] files = {"/resources/javalogo.gif","/resources/gplv3-127x51.png","/resources/reds10.jpg"};
		for (int i=0;i<files.length;i++)
		{
			JLabel l = new JLabel(createImageIcon(files[i]));
			icons.add(l);
		}
		panel.add(icons,BorderLayout.PAGE_END);
		
		d.setContentPane(panel);
	}//end About constructor
	
	public void display()
	{
		d.pack();
		d.setSize(500,500);
		d.setVisible(true);
	}
	
	protected ImageIcon createImageIcon(String path)
	{
		URL imgUrl = getClass().getResource(path);
		if (imgUrl != null)
			return new ImageIcon(imgUrl);
		else
			return null;
	}	
	
	private String license()
	{
		InputStream input = null;
		BufferedReader reader = null;
		String filename = "/resources/COPYING";
		String string = "";
		String line = "";
		try
		{
			input = getClass().getResourceAsStream(filename);
			reader = new BufferedReader(new InputStreamReader(input));
			string = "";
			while (null != (line = reader.readLine()))
			{
				string = string + line + "\n";
			}
			reader.close();
			input.close();
		}
		catch(Exception e)
		{
			string = "This program is licensed under the GPLv3\n";
			string += "Please see http://www.gnu.org/licenses/gpl.html";
		}
		return string;
	}
	
	private JDialog d;
	private String author = "Charles Amey";
	private String version = "0.1";
}//end about
