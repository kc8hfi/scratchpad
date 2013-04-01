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
import javax.swing.JFrame;
import javax.swing.JTree;
import java.awt.event.ActionEvent;
import javax.swing.JFileChooser;
import java.io.File;

public class SaveAsAction extends AbstractAction
{
     public SaveAsAction(JFrame p,tree t,String text,String desc)
     {
          super(text); //text is the actual name
          parentWindow = p;
          myTreeClass = t;
          putValue(SHORT_DESCRIPTION, desc); //used for tooltip text
     }
     public void actionPerformed(ActionEvent e)
     {
          System.out.println("with the new actions stuff,  " + e.getActionCommand());
          JFileChooser filePicker = new JFileChooser();
          int returnValue = filePicker.showSaveDialog(parentWindow);
          if (returnValue == filePicker.APPROVE_OPTION)
          {
               System.out.println("they clicked ok");
               File f = filePicker.getSelectedFile();
               System.out.println(f.getName());
               
               //create a new SaveToDisk object
               SaveToDisk saveme = new SaveToDisk(myTreeClass,f);
               saveme.write();
               myTreeClass.setFileName(f.getPath());
               myTreeClass.setFileSaved(1);
               
               //myTreeClass.getParentWindow().setTitle("inside save as action");
               
               //System.out.println("get path: " + f.getPath());
               //System.out.println("get absolute path: " + f.getAbsolutePath());
               
          }//they clicked ok
     }
     private JFrame parentWindow;
     private tree myTreeClass;
}//end class
