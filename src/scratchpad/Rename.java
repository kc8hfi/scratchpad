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
import java.awt.event.*;
import java.beans.*;
import java.net.URL;
import javax.sound.sampled.*;

public class Rename extends JDialog
{
     public Rename (JFrame f)
     {
          super (f,true);

          parentWindow = f;
          Action okButtonAction = new OkButtonAction("Ok");
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
          setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
          setModal(true);
          pack();
          setMinimumSize(new Dimension(250,150));
     }//end constructor
     
     private JTextField renameField;
     private JButton okButton;
     private JButton cancelButton;
     private JLabel label;
     private String newName;
     private JFrame parentWindow;
     
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
     
     private void playSound(String s)
     {
          try
          {
               URL soundFileUrl = getClass().getResource("/resources/sounds/"+s+".wav");
               AudioInputStream sound = AudioSystem.getAudioInputStream(soundFileUrl);
               DataLine.Info info = new DataLine.Info(Clip.class,sound.getFormat());
               Clip clip = (Clip)AudioSystem.getLine(info);
               clip.open(sound);
               clip.start();
          }
          catch (Exception soundException)
          {
               System.out.println("no sound, sorry");
          }
     }//end playSound
     

     class OkButtonAction extends AbstractAction
     {
          public OkButtonAction(String t)
          {
               super(t);
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
                    //playSound("monkey");
                    playSound("bullet2");
                    //playSound("357magnum");
                    //playSound("pause");
                    
                    System.out.println("tell them to type in a name!");
                    JOptionPane.showMessageDialog(parentWindow,"Please type in a name.","Error!",
                                                  JOptionPane.ERROR_MESSAGE);
               }
          }
          //private JDialog parent;
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
