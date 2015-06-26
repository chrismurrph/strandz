/*
    Strandz - an API that matches the user to the data.
    Copyright (C) 2007 Chris Murphy

    Strandz is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    Strandz is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Strandz; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

    In addition, as a special exception, Chris Murphy gives
    permission to link the code of this program with any Java software for
    which complete source code is available at no cost (even if distribution
    or modification of that source code is restricted), and distribute the
    results of that linking. You must obey the GNU General Public
    License in all respects for all of the code used other than this Java code.
    If you modify this file, you may extend this exception to your version of
    the file, but you are not obligated to do so. If you do not wish to do so,
    delete this exception statement from your version.

    The authors can be contacted via www.strandz.org
*/
package org.strandz.core.info.domain;

import org.strandz.lgpl.util.Err;

import java.awt.Color;
import java.lang.reflect.Method;

public class ItemDescriptor
{
    public static int NULL_KEYWORD = 1; /* var != null */
    public static int EQUALS_OPERATOR = 2; /* == */
    public static int NULL_STRING = 3; /* "" */
    public static int EQUALS_METHOD = 4; /* var1.equals( var2) */
    public static int NOT_USED = 5; /* boolean controls either checked
   or not, there is no such thing
   null */

    public ItemDescriptor(
        Class controlClass,
        String prefix,
        Class classClass,
        Method setControlMethod,
        Method setToolTipMethod,
        Method getControlMethod,
        Method setEditableControlMethod,
        Method getEditableControlMethod,
        Method requestFocusMethod,
        Method addActionListenerMethod,
        Method removeActionListenerMethod,
        Method removeAllItemsMethod,
        Method addItemMethod,
        int blankControlOperator,
        int blankControlOperand,
        int blankingPolicy,
        Class replacementCellRenderer,
        boolean isMLRenderer,
        AbstractOwnFieldMethods ownFieldMethods,
        Color designTimeColor,
        String defaultPropertyDisplayName
    )
    {
        this.controlClass = controlClass;
        this.prefix = prefix;
        this.classClass = classClass;
        this.setControlMethod = setControlMethod;
        this.setToolTipMethod = setToolTipMethod;
        this.getControlMethod = getControlMethod;
        this.setEditableControlMethod = setEditableControlMethod;
        this.getEditableControlMethod = getEditableControlMethod;
        this.requestFocusMethod = requestFocusMethod;
        this.removeAllItemsMethod = removeAllItemsMethod;
        this.addItemMethod = addItemMethod;
        this.blankControlOperator = blankControlOperator;
        this.blankControlOperand = blankControlOperand;
        this.blankingPolicy = blankingPolicy;
        this.addActionListenerMethod = addActionListenerMethod;
        this.removeActionListenerMethod = removeActionListenerMethod;
        this.replacementCellRenderer = replacementCellRenderer;
        this.isSdzRenderer = isMLRenderer;
        this.ownFieldMethods = ownFieldMethods;
        this.designTimeColor = designTimeColor;
        this.defaultPropertyDisplayName = defaultPropertyDisplayName;
        // Trying to write this kind of debugging just shows that I didn't understand!
        // if(replacementCellRenderer != null)
        // {
        // Err.error( "Has a replacementCellRenderer really be written - would have thought it would be a table thing!");
        // }
        if(replacementCellRenderer != null && isMLRenderer)
        {
            Err.error("Configuration error to be an MLRender, and have a replacement");
        }
    }

    public boolean equals(Object o)
    {
        /*
        boolean result = true;
        if(o == this)
        {
        result = true;
        }
        else if(!(o instanceof Node))
        {
        result = false;
        }
        else
        {
        result = false;
        Node test = (Node)o;
        if(name == null ? test.name == null : name.equals( test.name))
        {
        if((cell == null ? test.cell == null : cell.equals( test.cell)))
        {
        result = true;
        }
        else
        {
        Err.pr( "cell comparison failed: " + cell + " with " + test.cell);
        }
        }
        else
        {
        Err.pr( "name comparison failed: " + name + " with " + test.name);
        }
        }
        return result;
        */
        boolean result = super.equals(o);
        /*
        if(result)
        {
        Err.pr( this + " EQUALS " + o);
        }
        else
        {
        Err.pr( this + " NOT EQUALS " + o);
        }
        */
        return result;
    }

    public int hashCode()
    {
        /*
        int result = 17;
        result = 37*result + (name == null ? 0 : name.hashCode());
        result = 37*result + (cell == null ? 0 : cell.hashCode());
        return result;
        */
        int result = super.hashCode();
        // Err.pr( "hashCode: " + result);
        return result;
    }

    public String toString()
    {
        String replaceName = "";
        if(replacementCellRenderer != null)
        {
            replaceName = " (has replacementCellRenderer "
                + replacementCellRenderer.getName() + ")";
        }
        return controlClass.getName() + replaceName;
    }

    public Class controlClass;
    public String prefix;
    public Class classClass;
    public Method setControlMethod;
    public Method setToolTipMethod;
    public Method getControlMethod;
    public Method setEditableControlMethod;
    public Method getEditableControlMethod; // only required for debugging
    public Method requestFocusMethod;
    public Method addActionListenerMethod;
    public Method removeActionListenerMethod;
    public Method removeAllItemsMethod;
    public Method addItemMethod;
    public int blankControlOperator;
    public int blankControlOperand;
    public int blankingPolicy;
    public Class replacementCellRenderer;
    public boolean isSdzRenderer;
    public AbstractOwnFieldMethods ownFieldMethods;
    public Color designTimeColor;
    public String defaultPropertyDisplayName;
}
