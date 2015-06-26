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
package org.strandz.view.wombatrescue;

import org.strandz.lgpl.widgets.table.ColumnWidthsInformerI;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.util.Err;
import org.strandz.view.util.ROCellComponentCreator;
import org.strandz.data.wombatrescue.calculated.Night;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class WorkersColumnWidthsInformer implements ColumnWidthsInformerI
{
    static int CHAR_WIDTH = Utils.UNSET_INT;
    
    static
    {
        JLabel label = new JLabel();
        CHAR_WIDTH = SwingUtilities.computeStringWidth( label.getFontMetrics( ROCellComponentCreator.FONT), "a");
    }
    
    public double getColumnWidthAt( int column, int numOfColumns)
    {
        double result = Utils.UNSET_INT;
        Assert.isFalse( column == -1);
        if(column == 0)
        {
            result = Night.NAME_WIDTH * CHAR_WIDTH;
        }
        else
        {
            Err.error( "Supposed to just be a list of workers");            
        }
        return result;
    }
}
