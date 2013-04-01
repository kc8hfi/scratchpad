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
import javax.swing.KeyStroke;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;

public class QuitAction extends AbstractAction
{
     public QuitAction(JFrame f,String text,String desc, int mnemonic,KeyStroke accelerator)
     {
          super(text); //text is the actual name
          parentWindow = f;
          putValue(SHORT_DESCRIPTION, desc); //used for tooltip text
          putValue(MNEMONIC_KEY, mnemonic);
          putValue(ACCELERATOR_KEY,accelerator);
     }
     public void actionPerformed(ActionEvent e)
     {
          System.out.println("with the new actions stuff,  " + e.getActionCommand());
          WindowEvent event = new WindowEvent(parentWindow,WindowEvent.WINDOW_CLOSING);
          parentWindow.dispatchEvent(event);
     }
     private JFrame parentWindow;
}//end QuitAction class
