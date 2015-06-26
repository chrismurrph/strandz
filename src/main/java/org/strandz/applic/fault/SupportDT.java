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
import org.strandz.core.interf.NonVisualAttribute;
import org.strandz.core.interf.SdzBagI;
import org.strandz.core.interf.Strand;
import org.strandz.core.interf.VisualAttribute;
import org.strandz.view.fault.MainSupportPanel;

public class SupportDT
{
    public MainSupportPanel ui0;
    public Strand strand;
    public Node supportNode;
    public Cell supportCell;
    private NonVisualAttribute clientContactAttribute;
    private NonVisualAttribute faultAttribute;
    private NonVisualAttribute supportPersonAttribute;
    public VisualAttribute supportTextAttribute;
    public Cell clientContactLookupCell;
    public VisualAttribute clientContactNameAttribute;
    public Cell faultLookupCell;
    private NonVisualAttribute faultFaultStatusAttribute;
    public VisualAttribute faultNameAttribute;
    private NonVisualAttribute faultProductAttribute;
    public VisualAttribute faultTextAttribute;
    public Cell faultStatusLookupCell;
    public VisualAttribute faultStatusNameAttribute;
    public Cell productLookupCell;
    public VisualAttribute productNameAttribute;

    public SupportDT(SdzBagI sdzBag)
    {
        ui0 = (MainSupportPanel) sdzBag.getPane(0);
        strand = sdzBag.getStrand();
        supportNode = strand.getNodeByName("Support Node");
        supportCell = supportNode.getCell();
        clientContactAttribute = (NonVisualAttribute) supportCell.getAttributeByName(
            "clientContact");
        faultAttribute = (NonVisualAttribute) supportCell.getAttributeByName("fault");
        supportPersonAttribute = (NonVisualAttribute) supportCell.getAttributeByName(
            "supportPerson");
        supportTextAttribute = (VisualAttribute) supportCell.getAttributeByName(
            "Support Text");
        clientContactLookupCell = supportCell.getCellByName(
            "clientContact Lookup Cell");
        clientContactNameAttribute = (VisualAttribute) clientContactLookupCell.getAttributeByName(
            "clientContact Name");
        faultLookupCell = supportCell.getCellByName("fault Lookup Cell");
        faultFaultStatusAttribute = (NonVisualAttribute) faultLookupCell.getAttributeByName(
            "fault FaultStatus");
        faultNameAttribute = (VisualAttribute) faultLookupCell.getAttributeByName(
            "fault Name");
        faultProductAttribute = (NonVisualAttribute) faultLookupCell.getAttributeByName(
            "fault Product");
        faultTextAttribute = (VisualAttribute) faultLookupCell.getAttributeByName(
            "fault Text");
        faultStatusLookupCell = faultLookupCell.getCellByName(
            "faultStatus Lookup Cell");
        faultStatusNameAttribute = (VisualAttribute) faultStatusLookupCell.getAttributeByName(
            "faultStatus Name");
        productLookupCell = faultLookupCell.getCellByName("product Lookup Cell");
        productNameAttribute = (VisualAttribute) productLookupCell.getAttributeByName(
            "product Name");
    }
}
