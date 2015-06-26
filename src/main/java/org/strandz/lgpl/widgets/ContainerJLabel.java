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

import javax.swing.JLabel;
import java.awt.Container;

public class ContainerJLabel extends JLabel
{
    private Container component;
    private static int times = 0;

    /**
     * This method made compatible with ControlNode's toString()
     */
    public String toString()
    {
        String result = null;
        if(component != null)
        {
            result = component.getName();
            if(result == null)
            {
                result = component.getClass().getName();
                // Err.pr( "ComponentJLabel, got class of component: " + result);
            }
            else
            {// Err.pr( "ComponentJLabel, got name of component: " + result);
            }
        }
        else
        {
            result = "";
        }
        return result;
    }

    public ContainerJLabel()
    {
        super();
    }

    public ContainerJLabel(Container c)
    {
        super(c.getClass().getName());
        component = c;
        // setName( toString());
    }

    public String getName()
    {
        String result = super.getName();
        // Err.pr( "$$$ Getting NAME <" + result + ">");
        return result;
    }

    public Container getContainer()
    {
        // Err.pr( "$$$ Getting CONTAINER <" + component + ">");
        return component;
    }

    public void setContainer(Container c)
    {
        component = c;
        if(c != null)
        {
            // Err.pr( "$$$ Setting CONTAINER <" + toString() + "> times " + times);
            setText(toString());
        }
        else
        {
            // setTextNull always does this
            // Err.pr( "$$$ Setting CONTAINER NULL");
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
        // Err.pr( "$$$ Setting TEXT <" + txt + ">");
        /*
        if(times == 2)
        {
        Err.error();
        }
        */
        super.setText(txt);
    }
}
