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

/**
 * If you want any table/grid/similar to communicate with
 * strandz then you can implement this interface to pick up
 * the information the table can send you. This information
 * s/be enough for you to be able to visually direct the
 * table you have. For example when setNode is called you can
 * get Node's controlActionListener, which you can later use
 * to inform strandz that the user has changed the current
 * row of your table.
 * Importantly, node.getTableModel()
 * gives you a NodeTableModelInterface which gives you access
 * to the data itself. You will need to feed this data to your
 * applichousing table so that the data can be seen by the user! Thus
 * the code that implements this interface needs to deal with
 * both the information going into and coming out of core.info.
 */
abstract public class NodeTableMethods
{
    abstract public void setNode(AbstNodeTable node, NodeTableModelI model);

    abstract public NodeTableModelI getNodeTableModel();

    abstract public void fireTableDataChanged( String reason);
    
    public void fireTableDataChanged( int firstRow, int lastRow, String reason)
    {
        fireTableDataChanged( reason);
    }

    abstract public void fireRowChangedTo(int row);

    abstract public void fireTableRowsDeleted(int firstRow, int lastRow);

    abstract public void oneRowSelectionOn(Object tableView); // TODO remove param

    abstract public void acceptEdit();

    abstract public void rejectEdit();

    abstract public void fireTableRowsInserted(int firstRow, int lastRow);

    abstract public void setDebug(boolean b);

    //abstract public void repositionTo(Object itemAdapter);

    abstract public String getTableName();
}
