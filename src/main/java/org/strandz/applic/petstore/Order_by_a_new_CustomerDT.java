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
import org.strandz.core.interf.RuntimeAttribute;
import org.strandz.core.interf.SdzBagI;
import org.strandz.core.interf.Strand;
import org.strandz.view.petstore.CustomerPanel;

public class Order_by_a_new_CustomerDT
{
    public CustomerPanel ui0;
    public Strand strand;
    public Node accountNode;
    public Cell new_Customer_makes_orderCell;
    public RuntimeAttribute new_Customer_makes_orderEmailAttribute;
    public RuntimeAttribute new_Customer_makes_orderFirstNameAttribute;
    public RuntimeAttribute new_Customer_makes_orderLastNameAttribute;
    public Node orderNode;
    public Cell orderCell;
    public RuntimeAttribute orderOrderDateAttribute;
    public RuntimeAttribute orderTotalPriceAttribute;

    public Order_by_a_new_CustomerDT(SdzBagI sdzBag)
    {
        ui0 = (CustomerPanel) sdzBag.getPane(0);
        strand = sdzBag.getStrand();
        accountNode = strand.getNodeByName("Account Node");
        new_Customer_makes_orderCell = accountNode.getCell();
        new_Customer_makes_orderEmailAttribute = new_Customer_makes_orderCell.getAttributeByName(
            "New_Customer_makes_order Email");
        new_Customer_makes_orderFirstNameAttribute = new_Customer_makes_orderCell.getAttributeByName(
            "New_Customer_makes_order FirstName");
        new_Customer_makes_orderLastNameAttribute = new_Customer_makes_orderCell.getAttributeByName(
            "New_Customer_makes_order LastName");
        orderNode = strand.getNodeByName("Order Node");
        orderCell = orderNode.getCell();
        orderOrderDateAttribute = orderCell.getAttributeByName("Order OrderDate");
        orderTotalPriceAttribute = orderCell.getAttributeByName("Order TotalPrice");
    }
}
