package org.strandz.applic.supersix;

import org.strandz.core.interf.SdzBagI;
import org.strandz.core.interf.RuntimeAttribute;
import org.strandz.core.interf.Cell;
import org.strandz.core.interf.Node;
import org.strandz.core.interf.Strand;
import org.strandz.core.interf.StrandI;
import org.strandz.lgpl.util.Err;
import org.strandz.view.supersix.TeamPanel;
import org.strandz.view.supersix.PlayerPanel;


public class MaintainTeamDT
{
    public TeamPanel ui0;
    public PlayerPanel ui1;
    public Strand strand;

    public Node team;
    public Cell teamCell;
    public RuntimeAttribute teamdivisionAttribute;
    public RuntimeAttribute teamnameAttribute;
    public RuntimeAttribute teampaidForCurrentSeasonYearAttribute;

    public Cell divisionLookupCell;
    public RuntimeAttribute divisionnameAttribute;

    public Node players;
    public Cell playersCell;
    public RuntimeAttribute playeremailAttribute;
    public RuntimeAttribute playerfirstNameAttribute;
    public RuntimeAttribute playersurnameAttribute;
    public RuntimeAttribute contactPhoneNumberAttribute;
    public RuntimeAttribute dateOfBirthAttribute;
    public RuntimeAttribute homeAddress1Attribute;
    public RuntimeAttribute homeAddress2Attribute;

    public MaintainTeamDT( SdzBagI sdzBag)
    {
        try
        {
            ui0 = (TeamPanel)sdzBag.getPane( 0);
        }
        catch(ClassCastException ex)
        {
            Err.error( "Did not expect a " + sdzBag.getPane( 0).getClass().getName());
        }
        try
        {
            ui1 = (PlayerPanel)sdzBag.getPane( 1);
        }
        catch(ClassCastException ex)
        {
            Err.error( "Did not expect a " + sdzBag.getPane( 0).getClass().getName());
        }

        strand = sdzBag.getStrand();

        team = strand.getNodeByName( "Team");

        teamCell = team.getCell();
        teamdivisionAttribute = teamCell.getAttributeByName( "team division");
        teamnameAttribute = teamCell.getAttributeByName( "team name");
        teampaidForCurrentSeasonYearAttribute = teamCell.getAttributeByName( "team paidForCurrentSeasonYear");

        divisionLookupCell = teamCell.getCellByName( "division Lookup Cell");
        divisionnameAttribute = divisionLookupCell.getAttributeByName( "division name");


        players = strand.getNodeByName( "Players");

        playersCell = players.getCell();
        playeremailAttribute = playersCell.getAttributeByName( "player email");
        playerfirstNameAttribute = playersCell.getAttributeByName( "player firstName");
        playersurnameAttribute = playersCell.getAttributeByName( "player surname");
        contactPhoneNumberAttribute = playersCell.getAttributeByName( "contactPhoneNumber");
        dateOfBirthAttribute = playersCell.getAttributeByName( "dateOfBirth");
        homeAddress1Attribute = playersCell.getAttributeByName( "homeAddress1");
        homeAddress2Attribute = playersCell.getAttributeByName( "homeAddress2");


    }
}