package org.strandz.applic.wombatrescue;import org.strandz.core.interf.SdzBagI;import org.strandz.core.interf.RuntimeAttribute;import org.strandz.core.interf.Cell;import org.strandz.core.interf.Node;import org.strandz.core.interf.Strand;import org.strandz.lgpl.util.Err;import org.strandz.view.wombatrescue.ShiftManagersPanel;

public class BuddyManagerDT
{
    public ShiftManagersPanel ui0;
    public Strand strand;

    public Node shiftManagerNode;
    public Cell shiftManagerCell;
    public RuntimeAttribute shiftManagerFriDinnerWkerAttribute;
    public RuntimeAttribute shiftManagerFriOvernightWkerAttribute;
    public RuntimeAttribute shiftManagerMonDinnerWkerAttribute;
    public RuntimeAttribute shiftManagerMonOvernightWkerAttribute;
    public RuntimeAttribute shiftManagerSatDinnerWkerAttribute;
    public RuntimeAttribute shiftManagerSatOvernightWkerAttribute;
    public RuntimeAttribute shiftManagerTueDinnerWkerAttribute;
    public RuntimeAttribute shiftManagerTueOvernightWkerAttribute;
    public RuntimeAttribute shiftManagerWedDinnerWkerAttribute;
    public RuntimeAttribute shiftManagerWedOvernightWkerAttribute;
    public RuntimeAttribute shiftManagerThuDinnerWkerAttribute;
    public RuntimeAttribute shiftManagerThuOvernightWkerAttribute;

    public Cell friDinnerWkerLookupCell;
    public RuntimeAttribute friDinnerWkerToAttribute;

    public Cell friOvernightWkerLookupCell;
    public RuntimeAttribute friOvernightWkerToAttribute;

    public Cell monDinnerWkerLookupCell;
    public RuntimeAttribute monDinnerWkerToAttribute;

    public Cell monOvernightWkerLookupCell;
    public RuntimeAttribute monOvernightWkerToAttribute;

    public Cell satDinnerWkerLookupCell;
    public RuntimeAttribute satDinnerWkerToAttribute;

    public Cell satOvernightWkerLookupCell;
    public RuntimeAttribute satOvernightWkerToAttribute;

    public Cell tueDinnerWkerLookupCell;
    public RuntimeAttribute tueDinnerWkerToAttribute;

    public Cell tueOvernightWkerLookupCell;
    public RuntimeAttribute tueOvernightWkerToAttribute;

    public Cell wedDinnerWkerLookupCell;
    public RuntimeAttribute wedDinnerWkerToAttribute;

    public Cell wedOvernightWkerLookupCell;
    public RuntimeAttribute wedOvernightWkerToAttribute;

    public Cell thuDinnerWkerLookupCell;
    public RuntimeAttribute thuDinnerWkerToAttribute;

    public Cell thuOvernightWkerLookupCell;
    public RuntimeAttribute thuOvernightWkerToAttribute;

    public BuddyManagerDT( SdzBagI sdzBag)
    {
        try
        {
            ui0 = (ShiftManagersPanel)sdzBag.getPane( 0);
        }
        catch(ClassCastException ex)
        {
            Err.error( "Did not expect a " + sdzBag.getPane( 0).getClass().getName());
        }

        strand = sdzBag.getStrand();

        shiftManagerNode = strand.getNodeByName( "ShiftManager Node");

        shiftManagerCell = shiftManagerNode.getCell();
        shiftManagerFriDinnerWkerAttribute = shiftManagerCell.getAttributeByName( "ShiftManager FriDinnerWker");
        shiftManagerFriOvernightWkerAttribute = shiftManagerCell.getAttributeByName( "ShiftManager FriOvernightWker");
        shiftManagerMonDinnerWkerAttribute = shiftManagerCell.getAttributeByName( "ShiftManager MonDinnerWker");
        shiftManagerMonOvernightWkerAttribute = shiftManagerCell.getAttributeByName( "ShiftManager MonOvernightWker");
        shiftManagerSatDinnerWkerAttribute = shiftManagerCell.getAttributeByName( "ShiftManager SatDinnerWker");
        shiftManagerSatOvernightWkerAttribute = shiftManagerCell.getAttributeByName( "ShiftManager SatOvernightWker");
        shiftManagerTueDinnerWkerAttribute = shiftManagerCell.getAttributeByName( "ShiftManager TueDinnerWker");
        shiftManagerTueOvernightWkerAttribute = shiftManagerCell.getAttributeByName( "ShiftManager TueOvernightWker");
        shiftManagerWedDinnerWkerAttribute = shiftManagerCell.getAttributeByName( "ShiftManager WedDinnerWker");
        shiftManagerWedOvernightWkerAttribute = shiftManagerCell.getAttributeByName( "ShiftManager WedOvernightWker");
        shiftManagerThuDinnerWkerAttribute = shiftManagerCell.getAttributeByName( "ShiftManager ThuDinnerWker");
        shiftManagerThuOvernightWkerAttribute = shiftManagerCell.getAttributeByName( "ShiftManager ThuOvernightWker");

        friDinnerWkerLookupCell = shiftManagerCell.getCellByName( "friDinnerWker Lookup Cell");
        friDinnerWkerToAttribute = friDinnerWkerLookupCell.getAttributeByName( "friDinnerWker To");

        friOvernightWkerLookupCell = shiftManagerCell.getCellByName( "friOvernightWker Lookup Cell");
        friOvernightWkerToAttribute = friOvernightWkerLookupCell.getAttributeByName( "friOvernightWker To");

        monDinnerWkerLookupCell = shiftManagerCell.getCellByName( "monDinnerWker Lookup Cell");
        monDinnerWkerToAttribute = monDinnerWkerLookupCell.getAttributeByName( "monDinnerWker To");

        monOvernightWkerLookupCell = shiftManagerCell.getCellByName( "monOvernightWker Lookup Cell");
        monOvernightWkerToAttribute = monOvernightWkerLookupCell.getAttributeByName( "monOvernightWker To");

        satDinnerWkerLookupCell = shiftManagerCell.getCellByName( "satDinnerWker Lookup Cell");
        satDinnerWkerToAttribute = satDinnerWkerLookupCell.getAttributeByName( "satDinnerWker To");

        satOvernightWkerLookupCell = shiftManagerCell.getCellByName( "satOvernightWker Lookup Cell");
        satOvernightWkerToAttribute = satOvernightWkerLookupCell.getAttributeByName( "satOvernightWker To");

        tueDinnerWkerLookupCell = shiftManagerCell.getCellByName( "tueDinnerWker Lookup Cell");
        tueDinnerWkerToAttribute = tueDinnerWkerLookupCell.getAttributeByName( "tueDinnerWker To");

        tueOvernightWkerLookupCell = shiftManagerCell.getCellByName( "tueOvernightWker Lookup Cell");
        tueOvernightWkerToAttribute = tueOvernightWkerLookupCell.getAttributeByName( "tueOvernightWker To");

        wedDinnerWkerLookupCell = shiftManagerCell.getCellByName( "wedDinnerWker Lookup Cell");
        wedDinnerWkerToAttribute = wedDinnerWkerLookupCell.getAttributeByName( "wedDinnerWker To");

        wedOvernightWkerLookupCell = shiftManagerCell.getCellByName( "wedOvernightWker Lookup Cell");
        wedOvernightWkerToAttribute = wedOvernightWkerLookupCell.getAttributeByName( "wedOvernightWker To");

        thuDinnerWkerLookupCell = shiftManagerCell.getCellByName( "thuDinnerWker Lookup Cell");
        thuDinnerWkerToAttribute = thuDinnerWkerLookupCell.getAttributeByName( "thuDinnerWker To");

        thuOvernightWkerLookupCell = shiftManagerCell.getCellByName( "thuOvernightWker Lookup Cell");
        thuOvernightWkerToAttribute = thuOvernightWkerLookupCell.getAttributeByName( "thuOvernightWker To");


    }
}