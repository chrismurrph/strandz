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

import org.strandz.lgpl.util.Err;

import javax.swing.JTextField;
import javax.swing.text.Document;

public class DebugTextField extends JTextField
{
    private boolean finishedConstruction = false;
    private static int timesSet;
    private static int timesIs;
    private static boolean debugging = true;
    private static int constructed = 0;
    public int id;

    private void init()
    {
        constructed++;
        id = constructed;
        finishedConstruction = true;
        pr( "constructed DebugTextField id: " + id);
        if(id == 3)
        {
            Err.debug();
        }
    }

    public DebugTextField()
    {
        super();
        init();
    }

    public DebugTextField(String text)
    {
        super(text);
        init();
    }

    public DebugTextField(int columns)
    {
        super(columns);
        init();
    }

    public DebugTextField(String text, int columns)
    {
        super(text, columns);
        init();
    }

    public DebugTextField(Document doc, String text, int columns)
    {
        super(doc, text, columns);
        init();
    }

    public void setText(String s)
    {
        /*
        Err.pr( "DebugTextField, setText() to <" + s + ">");
        if(s == null)
        {
        Err.stack();
        }
        */
        super.setText(s);
    }

    public String getText()
    {
        return super.getText();
    }

    private void pr(String txt)
    {
        if(debugging && finishedConstruction)
        {
            Err.pr(txt);
        }
    }

    public String toString()
    {
        return getName().getClass() + ", ID:" + id;
    }
    
    public boolean isEditable()
    {
        boolean result = super.isEditable();
        /* Constant painting as cursor flashes I guess!
        timesIs++;
        pr(
            "IS EDITABLE (DebugTextField[" + getName() + "], ID:" + id + "): " + result + " times "
                + timesIs);
        if(timesIs == 39)
        {
            Err.stack();
        }
        */
        return result;
    }

    public void setEditable(boolean b)
    {
        super.setEditable(b);
        timesSet++;
        pr(
            "SET EDITABLE (DebugTextField[" + getName() + "], ID:" + id + "): " + b + " times "
                + timesSet);
        if(timesSet == 0) //24, 58
        {
            Err.stack();
        }
    }
}
