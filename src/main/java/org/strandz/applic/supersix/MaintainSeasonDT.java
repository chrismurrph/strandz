package org.strandz.applic.supersix;import org.strandz.core.interf.SdzBagI;import org.strandz.core.interf.RuntimeAttribute;import org.strandz.core.interf.Cell;import org.strandz.core.interf.Node;import org.strandz.core.interf.Strand;import org.strandz.lgpl.util.Err;import org.strandz.view.supersix.SeasonPanel;import org.strandz.view.supersix.MeetPanel;import org.strandz.view.supersix.MainMatchPanel;

public class MaintainSeasonDT
{
    public SeasonPanel ui0;
    public MeetPanel ui1;
    public MainMatchPanel ui2;
    public Strand strand;

    public Node seasonNode;
    public Cell seasonCell;
    public RuntimeAttribute seasonendDateAttribute;
    public RuntimeAttribute seasonstartDateAttribute;
    public RuntimeAttribute seasonYearAttribute;

    public Cell seasonYearLookupCellOne;
    public RuntimeAttribute seasonYearNameOneAttribute;

    public Node meetsListDetailNode;
    public Cell meetsCell;
    public RuntimeAttribute meetdateAttribute;
    public RuntimeAttribute meetOrdinalOneAttribute;
    public RuntimeAttribute meetseasonAttribute;

    public Cell seasonLookupCell;
    public RuntimeAttribute seasonseasonYearAttribute;

    public Cell seasonYearLookupCellTwo;
    public RuntimeAttribute seasonYearNameTwoAttribute;

    public Node gamesListDetailNode;
    public Cell gamesCell;
    public RuntimeAttribute matchdivisionAttribute;
    public RuntimeAttribute matchkickOffTimeAttribute;
    public RuntimeAttribute matchmeetAttribute;
    public RuntimeAttribute matchpitchAttribute;
    public RuntimeAttribute matchteamOneAttribute;
    public RuntimeAttribute matchteamOneGoalsAttribute;
    public RuntimeAttribute matchteamTwoAttribute;
    public RuntimeAttribute matchteamTwoGoalsAttribute;

    public Cell divisionLookupCell;
    public RuntimeAttribute divisionnameAttribute;

    public Cell kickOffTimeLookupCell;
    public RuntimeAttribute kickOffTimenameAttribute;

    public Cell meetLookupCell;
    public RuntimeAttribute meetOrdinalTwoAttribute;

    public Cell pitchLookupCell;
    public RuntimeAttribute pitchnameAttribute;

    public Cell teamOneLookupCell;
    public RuntimeAttribute teamNameOneAttribute;

    public Cell teamTwoLookupCell;
    public RuntimeAttribute teamNameTwoAttribute;

    public MaintainSeasonDT( SdzBagI sdzBag)
    {
        try
        {
            ui0 = (SeasonPanel)sdzBag.getPane( 0);
        }
        catch(ClassCastException ex)
        {
            Err.error( "Did not expect a " + sdzBag.getPane( 0).getClass().getName());
        }
        try
        {
            ui1 = (MeetPanel)sdzBag.getPane( 1);
        }
        catch(ClassCastException ex)
        {
            Err.error( "Did not expect a " + sdzBag.getPane( 0).getClass().getName());
        }
        try
        {
            ui2 = (MainMatchPanel)sdzBag.getPane( 2);
        }
        catch(ClassCastException ex)
        {
            Err.error( "Did not expect a " + sdzBag.getPane( 0).getClass().getName());
        }

        strand = sdzBag.getStrand();

        seasonNode = strand.getNodeByName( "Season Node");

        seasonCell = seasonNode.getCell();
        seasonendDateAttribute = seasonCell.getAttributeByName( "season endDate");
        seasonstartDateAttribute = seasonCell.getAttributeByName( "season startDate");
        seasonYearAttribute = seasonCell.getAttributeByName( "seasonYear");

        seasonYearLookupCellOne = seasonCell.getCellByName( "seasonYear Lookup Cell One");
        seasonYearNameOneAttribute = seasonYearLookupCellOne.getAttributeByName( "seasonYear Name One");


        meetsListDetailNode = strand.getNodeByName( "meets List Detail Node");

        meetsCell = meetsListDetailNode.getCell();
        meetdateAttribute = meetsCell.getAttributeByName( "meet date");
        meetOrdinalOneAttribute = meetsCell.getAttributeByName( "meet Ordinal One");
        meetseasonAttribute = meetsCell.getAttributeByName( "meet season");

        seasonLookupCell = meetsCell.getCellByName( "season Lookup Cell");
        seasonseasonYearAttribute = seasonLookupCell.getAttributeByName( "season seasonYear");

        seasonYearLookupCellTwo = seasonLookupCell.getCellByName( "seasonYear Lookup Cell Two");
        seasonYearNameTwoAttribute = seasonYearLookupCellTwo.getAttributeByName( "seasonYear Name Two");


        gamesListDetailNode = strand.getNodeByName( "games List Detail Node");

        gamesCell = gamesListDetailNode.getCell();
        matchdivisionAttribute = gamesCell.getAttributeByName( "match division");
        matchkickOffTimeAttribute = gamesCell.getAttributeByName( "match kickOffTime");
        matchmeetAttribute = gamesCell.getAttributeByName( "match meet");
        matchpitchAttribute = gamesCell.getAttributeByName( "match pitch");
        matchteamOneAttribute = gamesCell.getAttributeByName( "match teamOne");
        matchteamOneGoalsAttribute = gamesCell.getAttributeByName( "match teamOneGoals");
        matchteamTwoAttribute = gamesCell.getAttributeByName( "match teamTwo");
        matchteamTwoGoalsAttribute = gamesCell.getAttributeByName( "match teamTwoGoals");

        divisionLookupCell = gamesCell.getCellByName( "division Lookup Cell");
        divisionnameAttribute = divisionLookupCell.getAttributeByName( "division name");

        kickOffTimeLookupCell = gamesCell.getCellByName( "kickOffTime Lookup Cell");
        kickOffTimenameAttribute = kickOffTimeLookupCell.getAttributeByName( "kickOffTime name");

        meetLookupCell = gamesCell.getCellByName( "meet Lookup Cell");
        meetOrdinalTwoAttribute = meetLookupCell.getAttributeByName( "meet Ordinal Two");

        pitchLookupCell = gamesCell.getCellByName( "pitch Lookup Cell");
        pitchnameAttribute = pitchLookupCell.getAttributeByName( "pitch name");

        teamOneLookupCell = gamesCell.getCellByName( "teamOne Lookup Cell");
        teamNameOneAttribute = teamOneLookupCell.getAttributeByName( "team Name One");

        teamTwoLookupCell = gamesCell.getCellByName( "teamTwo Lookup Cell");
        teamNameTwoAttribute = teamTwoLookupCell.getAttributeByName( "team Name Two");


    }
}