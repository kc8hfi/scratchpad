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

import java.util.*;

public class Csv
{
     public Csv()
     {
     }
     
     public String combine(ArrayList v)
     {
          String s = "";
          for (int i=0;i<v.size();i++)
          {
               s = s + "\"" + v.get(i) + "\",";
          }
          s = s.substring(0,s.length()-1);
          return s;
     }
     
     public ArrayList<String> parse(String s)
     {
          ArrayList<String> list = new ArrayList<String>();
          char [] arr = s.toCharArray();
          int length = arr.length;
          char oldchar = ' ';
          String item = "";
          int count=1;
          System.out.println("parse this: " + s);
          for ( char c : arr )
          {
               System.out.println(c);
               if (c == '"')
               {
                    if (oldchar == '"')
                    {
                         //item += Character.toString(c);
                         System.out.println("item is: " + item);
                         System.out.println("both char is a quote??");
                         list.add(item);
                    }
                    else if (length == count)
                    {
                         //replace || with the new line character
                         item = item.replaceAll(";;",System.getProperty("line.separator"));
                         //System.out.println("item length=count: " + item);
                         list.add(item);
                    }
                    //else
                         //list.add(item);
               }
               else if (c == ',')
                    {
                         if (oldchar != '"')
                         {
                              item += Character.toString(c);;
                         }
                         else
                         {
                              //replace || with the new line character
                              item = item.replaceAll(";;",System.getProperty("line.separator"));
                              list.add(item);
                              item = "";
                         }
                    }
                    else
                    {
                         item += Character.toString(c);
                    }
               oldchar = c;
               count++;
          }
          return list;
     }//end parseme
}//end class Csv
