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
package org.strandz.core.domain;

import org.strandz.lgpl.util.Utils;

public class Independent
{
    private NodeI masterNode;
    private String masterOrDetailField;

    public Independent()
    {
    }

    public Independent(NodeI masterNode,
                       String masterOrDetailField)
    {
        this.masterNode = masterNode;
        this.masterOrDetailField = masterOrDetailField;
    }

/*
This recursion can occur - so in this class will use the name of the node instead
	at org.strandz.core.interf.CollectionLinkAttribute.hashCode(CollectionLinkAttribute.java:95)
	at org.strandz.core.interf.Cell.hashCode(Cell.java:305)
	at org.strandz.core.interf.Node.hashCode(Node.java:248)
	at org.strandz.core.domain.Independent.hashCode(Independent.java:56)
	at org.strandz.core.interf.CollectionLinkAttribute.hashCode(CollectionLinkAttribute.java:95)     
*/
    public int hashCode()
    {
        int result = 17;
        result = 37 * result
            + (masterOrDetailField == null ? 0 : masterOrDetailField.hashCode());
        result = 37 * result + (masterNode.getName() == null ? 0 : masterNode.getName().hashCode());
        return result;
    }

    public boolean equals(Object o)
    {
        Utils.chkType(o, this.getClass());
        if(o == this)
        {
            return true;
        }
        if(!(o instanceof Independent))
        {
            return false;
        }

        Independent other = (Independent) o;
        if((masterOrDetailField == null
            ? other.masterOrDetailField == null
            : masterOrDetailField.equals(other.masterOrDetailField)))
        {
            if((masterNode.getName() == null
                ? other.masterNode.getName() == null
                : masterNode.getName().equals(other.masterNode.getName())))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }

    public String toString()
    {
        String result = "[" + masterNode + "," + masterOrDetailField + "]";
        return result;
    }

    public NodeI getMasterNode()
    {
        return masterNode;
    }

    public String getMasterOrDetailField()
    {
        return masterOrDetailField;
    }

    public void setMasterNode(NodeI masterNode)
    {
        this.masterNode = masterNode;
    }

    public void setMasterOrDetailField(String masterOrDetailField)
    {
        this.masterOrDetailField = masterOrDetailField;
    }
}
