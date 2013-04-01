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

public class DataInfo
{
     public DataInfo ()
     {
          name = "no name";
          data = "no data";
     }
     public DataInfo (String n, String d)
     {
          name = n;
          data = d;
     }
     public String toString()
     {
          return name;
     }
     
     public void setName(String n)
     {
          name = n;
     }
     
     public String getData()
     {
          return data;
     }
     
     public void setData(String d)
     {
          data = d;
     }
     
     private String name;
     private String data;
}//end datainfo
