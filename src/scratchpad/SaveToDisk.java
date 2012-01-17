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

import javax.swing.tree.*;
import javax.swing.JTree;
import java.io.*;
import java.util.*;

public class SaveToDisk
{
	public SaveToDisk (tree mt,File f)
	{
		myTreeClass = mt;
		theTree = myTreeClass.getTree();
		theFile = f;
	}
	
	public void write()
	{
		System.out.println("do all the work here");
		//open the file
		try 
		{
			writer = new FileWriter(theFile);
			bufferedWriter = new BufferedWriter(writer);
			out = new PrintWriter(bufferedWriter);
			//loop through the tree
			//out.println("some string");
			traverse();
			out.close();
			bufferedWriter.close();
			writer.close();
			System.out.println("everything is closed");
			System.out.println("already set the title " + theFile);
			myTreeClass.getParentWindow().setTitle("Scratchpad - " + theFile);
			
		}
		catch (Exception fe)
		{
		}
	}
	
	public void traverse() 
	{ 
		TreeModel model = theTree.getModel();
		if (model != null)
		{
			DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot();
			DataInfo rootInfo = (DataInfo)root.getUserObject();
			ArrayList<String> list = new ArrayList<String>();
			list.add(rootInfo.toString());

			list.add(FixData(rootInfo.getData()));
			Csv makeCsvString = new Csv();
			String s = makeCsvString.combine(list);
			System.out.println(s);
			out.println(s);
			walk(model,root);    
		}
		else
			System.out.println("Tree is empty.");
	}
		
	protected void walk(TreeModel model, DefaultMutableTreeNode o)
	{
		int  cc;
		cc = model.getChildCount(o);
		for( int i=0; i < cc; i++)
		{
			DefaultMutableTreeNode childNode = (DefaultMutableTreeNode)model.getChild(o,i);
			DefaultMutableTreeNode childsParent = (DefaultMutableTreeNode)childNode.getParent();
			DataInfo child = (DataInfo)childNode.getUserObject();
			//DataInfo info = (DataInfo)child.getUserObject();
			ArrayList<String> list = new ArrayList<String>();
			if (model.isLeaf(childNode))
			{
				//System.out.println(child.toString());
				//System.out.println(info.getData());
				list.add(childsParent.toString());
				list.add(child.toString());
				list.add(FixData(child.getData()));
				Csv makeCsvString = new Csv();
				String s = makeCsvString.combine(list);
				System.out.println(s);
				out.println(s);
			}
			else
			{
				//System.out.print(child.toString()+"--");
				list.add(childsParent.toString());
				list.add(child.toString());
				list.add(FixData(child.getData()));
				Csv makeCsvString = new Csv();
				String s = makeCsvString.combine(list);
				System.out.println(s);
				out.println(s);
				walk(model,childNode ); 
			}
		}//end loop
	}//end walk
	
	private String FixData (String string)
	{
		String fixme = string;
		//replace new line characters
		fixme = fixme.replaceAll(System.getProperty("line.separator"),";;");
		
		//replace commas
		//fixme = fixme.replaceAll(",",";,;");
		
		//replace " marks
		fixme = fixme.replaceAll("\"","\"\"");
		
		return fixme;
	}//end FixData
	
	
	private tree myTreeClass;
	private JTree theTree;
	private File theFile;
	private FileWriter writer;
	private BufferedWriter bufferedWriter;
	private PrintWriter out;
	
}//end Csv class