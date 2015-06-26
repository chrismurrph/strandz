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
package org.strandz.applic.fault;

import org.strandz.core.interf.Cell;
import org.strandz.core.interf.Node;
import org.strandz.core.interf.RuntimeAttribute;
import org.strandz.core.interf.SdzBagI;
import org.strandz.core.interf.Strand;
import org.strandz.lgpl.util.Err;
import org.strandz.view.fault.FaultPanel;

public class BeanFaultDT
{
    public FaultPanel ui0;
    public Strand strand;
    public Node faultNode;
    public Cell faultCell;
    public RuntimeAttribute faultfaultStatusAttribute;
    public RuntimeAttribute faultnameAttribute;
    public RuntimeAttribute faultproductAttribute;
    public Cell productLookupCell;
    public Cell faultStatusLookupCell;

    public BeanFaultDT(SdzBagI sdzBag)
    {
        try
        {
            ui0 = (FaultPanel) sdzBag.getPane(0);
        }
        catch(ClassCastException ex)
        {
            Err.error(
                "Did not expect a "
                    + sdzBag.getPane(0).getClass().getName());
        }
        strand = sdzBag.getStrand();
        faultNode = strand.getNodeByName("Fault Node");
        faultCell = faultNode.getCell();
        faultfaultStatusAttribute = faultCell.getAttributeByName(
            "fault faultStatus");
        faultnameAttribute = faultCell.getAttributeByName("fault name");
        faultproductAttribute = faultCell.getAttributeByName("fault product");
        productLookupCell = faultCell.getCellByName("product Lookup Cell");
        faultStatusLookupCell = faultCell.getCellByName("faultStatus Lookup Cell");
    }
}
