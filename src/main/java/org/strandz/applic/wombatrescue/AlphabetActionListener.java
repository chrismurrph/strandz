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
package org.strandz.applic.wombatrescue;

import org.strandz.core.interf.Cell;
import org.strandz.core.interf.Node;
import org.strandz.core.interf.SdzBagI;
import org.strandz.lgpl.util.Assert;
import org.strandz.data.wombatrescue.objects.WorkerI;

import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

public class AlphabetActionListener implements ActionListener
{
    private SdzBagI sbI;
    private Node workerNode;
    private Cell workerCell;

    AlphabetActionListener(SdzBagI sbI, Node workerNode, Cell workerCell)
    {
        this.sbI = sbI;
        this.workerNode = workerNode;
        this.workerCell = workerCell;
        // Err.pr( "AlphabetActionListener");
    }

    /**
     * @param txt - The letter (or sequence of) that you want to do the action for
     */
    public boolean findWorkerStartsWith( String txt)
    {
        boolean result = true;
        int i = 0;
        Assert.notBlank( txt, "Nothing to search on");
        Assert.notNull( workerCell.getDataRecords(), 
                        "Need to query all workers before can select a particular worker");
        for(Iterator iter = workerCell.getDataRecords().iterator(); iter.hasNext(); i++)
        {
            WorkerI vol = (WorkerI)iter.next();
            if(vol.getOrderBy().startsWith( txt))
            {
                break;
            }
        }
        if(i != workerNode.getRowCount())
        {
            sbI.getStrand().SET_ROW(i);
        }
        else
        {
            result = false;
        }
        return result;
    }

    public void actionPerformed(ActionEvent e)
    {
        JButton button = (JButton) e.getSource();
        // Err.pr( "pressed " + button.getText());
        boolean ok = findWorkerStartsWith(button.getText());
        if(!ok)
        {
            //Don't move from where am if don't find
//            Err.error( "Have not been able to find a worker in cell " + workerCell.getName() + 
//                    " that starts with " + txt);
        }
    }

    public Cell getWorkerCell()
    {
        return workerCell;
    }
}
