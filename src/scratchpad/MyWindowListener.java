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

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JOptionPane;

import java.awt.Window;

public class MyWindowListener extends WindowAdapter
{
     public void windowClosing(WindowEvent e) 
     {
          JFrame theWindow = (JFrame)e.getWindow();
          tree panel = (tree)theWindow.getContentPane();
          
          //get all the windows, something is still open
/*          Window [] windows = theWindow.getWindows();
          for (int i=0;i<windows.length;i++)
          {
               JFrame t = (JFrame)windows[i];
               System.out.println(t.getTitle());
               System.out.println(t.getDefaultCloseOperation());
          }*/
          
          
          if (panel.getFileSaved() == 0)
          {
               //A pause so user can see the message before
               //the window actually closes.
//                int n = JOptionPane.showConfirmDialog(theWindow,
//                          "You have unsaved changed, do you really want to close?",
//                          "Close Program?",
//                          JOptionPane.YES_NO_OPTION
//                );
               Object[] options = {"Yes","No"};
               int n = JOptionPane.showOptionDialog(
                    theWindow,
                    "You have unsaved changed, do you really want to close?",
                    "Close Program?",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[1]
               );
               
//                System.out.println("answer is: " + Integer.toString(n));
//                System.out.println("yes option: " + Integer.toString(JOptionPane.YES_OPTION));
               if (n == JOptionPane.YES_OPTION)
               {
                    theWindow.setVisible(false);
                    theWindow.dispose();
                    System.exit(0);
               }
          }//file isn't saved
          else
          {
               theWindow.setVisible(false);
               theWindow.dispose();
               System.exit(0);
          }
     }//end windowClosing
}//end MyWindowListener
