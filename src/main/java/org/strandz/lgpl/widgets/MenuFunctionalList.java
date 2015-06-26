/*
    Strandz LGPL - an API that matches the user to the data.
    Copyright (C) 2007 Chris Murphy

    Strandz LGPL is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA


    The authors can be contacted via www.strandz.org
*/
package org.strandz.lgpl.widgets;

import javax.swing.JList;
import javax.swing.plaf.basic.BasicListUI;

public class MenuFunctionalList extends JList
{
    MenuFunctionalBasicListUI ui;

    public MenuFunctionalList()
    {
        ui = new MenuFunctionalBasicListUI();
        setUI(ui); // Isn't there a createUI we can override?
        // NO Need to see tutorial
        // When implemented ok, maybe submit as note
    }
    /*
    * Not sure what JavaDoc was going on about here!
    public void setUI( MenuFunctionalBasiclistUI newUI)
    {
    super.setUI( newUI);
    }
    public MenuFunctionalBasiclistUI getUI()
    {
    return (MenuFunctionalBasiclistUI)super.getUI();
    }
    */

    static class MenuFunctionalBasicListUI extends BasicListUI
    {/*
     protected KeyListener createKeyListener()
     {
     return new MenuFunctionalKeyHandler();
     }

     class MenuFunctionalKeyHandler extends BasicListUI.KeyHandler
     {
     public void keyTyped( KeyEvent e)
     {
     //Err.pr( "Typed: " + e);
     if(e.getModifiers() != KeyEvent.ALT_MASK)
     {
     super.keyTyped( e);
     }
     }
     }
     */}
}
