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

import javax.swing.JComponent;

public class ComponentLabelField extends LabelField
{
    private JComponent component;
    private static int times = 0;

    public ComponentLabelField()
    {
        super();
    }

    public ComponentLabelField(JComponent c)
    {
        super(c.getClass().getName());
        component = c;
    }

    public JComponent getComponent()
    {
        // Err.pr( "$$$ Getting COMPONENT <" + component + ">");
        return component;
    }

    public void setComponent(JComponent c)
    {
        component = c;
        if(c != null)
        {
            // Err.pr( "$$$ Setting COMPONENT <" + c.getClass().getName() + "> times " + times);
            setText(c.getClass().getName());
        }
        else
        {
            setText("");
        }
    }

    public String getText()
    {
        String result = super.getText();
        // Err.pr( "$$$ Getting TEXT <" + result + ">");
        return result;
    }

    public void setText(String txt)
    {
        /*
        Err.pr( "$$$ Setting TEXT <" + txt + ">");
        if(times == 2)
        {
        Err.error();
        }
        */
        super.setText(txt);
    }
}
