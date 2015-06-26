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
package org.strandz.applic.needs;

import org.strandz.core.interf.Cell;
import org.strandz.core.interf.Node;
import org.strandz.core.interf.NonVisualAttribute;
import org.strandz.core.interf.SdzBagI;
import org.strandz.core.interf.Strand;
import org.strandz.core.interf.VisualAttribute;
import org.strandz.view.needs.MaintainContact;

public class MaintainContactDT
{
    public MaintainContact ui0;
    public Strand strand;
    public Node contactNode;
    public Cell contactCell;
    private NonVisualAttribute contactAddressAttribute;
    public VisualAttribute contactCommentsAttribute;
    public VisualAttribute contactCompanyNameAttribute;
    public VisualAttribute contactDayPhoneAttribute;
    public VisualAttribute contactEmailAddress1Attribute;
    public VisualAttribute contactEmailAddress2Attribute;
    public VisualAttribute contactEveningPhoneAttribute;
    public VisualAttribute contactFirstNameAttribute;
    public VisualAttribute contactJobTitleAttribute;
    public VisualAttribute contactMobilePhoneAttribute;
    public VisualAttribute contactSecondNameAttribute;
    public Cell addressLookupCell;
    public VisualAttribute addressPostcodeAttribute;
    public VisualAttribute addressStreetAttribute;
    public VisualAttribute addressSuburbAttribute;

    public MaintainContactDT(SdzBagI sdzBag)
    {
        ui0 = (MaintainContact) sdzBag.getPane(0);
        strand = sdzBag.getStrand();
        contactNode = strand.getNodeByName("Contact Node");
        contactCell = contactNode.getCell();
        contactAddressAttribute = (NonVisualAttribute) contactCell.getAttributeByName(
            "Contact Address");
        contactCommentsAttribute = (VisualAttribute) contactCell.getAttributeByName(
            "Contact Comments");
        contactCompanyNameAttribute = (VisualAttribute) contactCell.getAttributeByName(
            "Contact CompanyName");
        contactDayPhoneAttribute = (VisualAttribute) contactCell.getAttributeByName(
            "Contact DayPhone");
        contactEmailAddress1Attribute = (VisualAttribute) contactCell.getAttributeByName(
            "Contact EmailAddress1");
        contactEmailAddress2Attribute = (VisualAttribute) contactCell.getAttributeByName(
            "Contact EmailAddress2");
        contactEveningPhoneAttribute = (VisualAttribute) contactCell.getAttributeByName(
            "Contact EveningPhone");
        contactFirstNameAttribute = (VisualAttribute) contactCell.getAttributeByName(
            "Contact FirstName");
        contactJobTitleAttribute = (VisualAttribute) contactCell.getAttributeByName(
            "Contact JobTitle");
        contactMobilePhoneAttribute = (VisualAttribute) contactCell.getAttributeByName(
            "Contact MobilePhone");
        contactSecondNameAttribute = (VisualAttribute) contactCell.getAttributeByName(
            "Contact SecondName");
        addressLookupCell = contactCell.getCellByName("address Lookup Cell");
        addressPostcodeAttribute = (VisualAttribute) addressLookupCell.getAttributeByName(
            "address Postcode");
        addressStreetAttribute = (VisualAttribute) addressLookupCell.getAttributeByName(
            "address Street");
        addressSuburbAttribute = (VisualAttribute) addressLookupCell.getAttributeByName(
            "address Suburb");
    }
}
