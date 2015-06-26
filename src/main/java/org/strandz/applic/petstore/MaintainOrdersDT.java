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
package org.strandz.applic.petstore;

import org.strandz.core.interf.Cell;
import org.strandz.core.interf.Node;
import org.strandz.core.interf.NonVisualAttribute;
import org.strandz.core.interf.SdzBagI;
import org.strandz.core.interf.Strand;
import org.strandz.core.interf.VisualAttribute;
import org.strandz.view.petstore.AccountPanel;
import org.strandz.view.petstore.CustomerPanel;
import org.strandz.view.petstore.LineItemPanel;
import org.strandz.view.petstore.OrderPanel;

public class MaintainOrdersDT
{
    public AccountPanel ui0;
    public OrderPanel ui1;
    public LineItemPanel ui2;
    public CustomerPanel ui3;
    public Strand strand;
    public Node accountNode;
    public Cell accountCell;
    public VisualAttribute accountEmailAttribute;
    public VisualAttribute accountFirstNameAttribute;
    public VisualAttribute accountLastNameAttribute;
    public Node orderReferenceDetailNode;
    public Cell orderReferenceDetailCell;
    public VisualAttribute orderOrderDateAttribute;
    public VisualAttribute orderTotalPriceAttribute;
    public Node lineitemReferenceDetailNode;
    public Cell lineitemReferenceDetailCell;
    private NonVisualAttribute itemAttribute;
    public VisualAttribute lineitemQuantityAttribute;
    public VisualAttribute lineitemUnitPriceAttribute;
    public Cell itemLookupCell;
    private NonVisualAttribute productAttribute;
    public Cell productLookupCell;
    public VisualAttribute productNameAttribute;

    public MaintainOrdersDT(SdzBagI sdzBag)
    {
        ui0 = (AccountPanel) sdzBag.getPane(0);
        ui1 = (OrderPanel) sdzBag.getPane(1);
        ui2 = (LineItemPanel) sdzBag.getPane(2);
        ui3 = (CustomerPanel) sdzBag.getPane(3);
        strand = sdzBag.getStrand();
        accountNode = strand.getNodeByName("Account Node");
        accountCell = accountNode.getCell();
        accountEmailAttribute = (VisualAttribute) accountCell.getAttributeByName(
            "Account Email");
        accountFirstNameAttribute = (VisualAttribute) accountCell.getAttributeByName(
            "Account FirstName");
        accountLastNameAttribute = (VisualAttribute) accountCell.getAttributeByName(
            "Account LastName");
        orderReferenceDetailNode = strand.getNodeByName(
            "order Reference Detail Node");
        orderReferenceDetailCell = orderReferenceDetailNode.getCell();
        orderOrderDateAttribute = (VisualAttribute) orderReferenceDetailCell.getAttributeByName(
            "order OrderDate");
        orderTotalPriceAttribute = (VisualAttribute) orderReferenceDetailCell.getAttributeByName(
            "order TotalPrice");
        lineitemReferenceDetailNode = strand.getNodeByName(
            "lineitem Reference Detail Node");
        lineitemReferenceDetailCell = lineitemReferenceDetailNode.getCell();
        itemAttribute = (NonVisualAttribute) lineitemReferenceDetailCell.getAttributeByName(
            "item");
        lineitemQuantityAttribute = (VisualAttribute) lineitemReferenceDetailCell.getAttributeByName(
            "lineitem Quantity");
        lineitemUnitPriceAttribute = (VisualAttribute) lineitemReferenceDetailCell.getAttributeByName(
            "lineitem UnitPrice");
        itemLookupCell = lineitemReferenceDetailCell.getCellByName(
            "item Lookup Cell");
        productAttribute = (NonVisualAttribute) itemLookupCell.getAttributeByName(
            "product");
        productLookupCell = itemLookupCell.getCellByName("product Lookup Cell");
        productNameAttribute = (VisualAttribute) productLookupCell.getAttributeByName(
            "product Name");
    }
}
