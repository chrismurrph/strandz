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
package org.strandz.lgpl.extent;

import org.strandz.lgpl.util.Err;

public class DEErrorContainer
{
    private boolean inError = false;
    private Tie tie;
    private DependentExtent dependentExtent;

    DEErrorContainer(Tie tie)
    {
        this.tie = tie;
    }

    public boolean isInError()
    {
        return inError;
    }

    public void setInError(boolean inError)
    {
        this.inError = inError;
    }

    public DependentExtent getDependentExtent()
    {
        if(inError)
        {
            /*
            * This is the message used to display, and will still display if
            * do not check isInError() first
            */
            Err.error(
                "setDependentExtent not yet been called"
                    + " - setData() may not have been called on child of relationship:"
                    + tie);
        }
        return dependentExtent;
    }

    public String toString()
    {
        String result = null;
        if(tie.getType() == Tie.CHILD_REFERENCE)
        {
            result = "Have not called setData() on the child Node called <"
                + tie.childObj.getName() + ">";
        }
        else if(tie.getType() == Tie.PARENT_LIST)
        {
            result = "Internal Error. setDependentExtent() was supposed to have been called on the tie for this simple aggregate relationship";
        }
        return result;
    }

    public void setDependentExtent(DependentExtent dependentExtent)
    {
        this.dependentExtent = dependentExtent;
    }

    public void setProblemChild(Object object)
    {
    }

    public Object getChildCausingError()
    {
        return tie.getChild();
    }
}
