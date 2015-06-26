package org.strandz.applic.supersix;import org.strandz.core.interf.SdzBagI;import org.strandz.core.interf.RuntimeAttribute;import org.strandz.core.interf.Cell;import org.strandz.core.interf.Node;import org.strandz.core.interf.Strand;import org.strandz.lgpl.util.Err;import org.strandz.view.supersix.GlobalPanel;

public class MaintainGlobalDT
{
    public GlobalPanel ui0;
    public Strand strand;

    public Node globalNode;
    public Cell globalCell;
    public RuntimeAttribute globalcurrentSeasonYearAttribute;
    public RuntimeAttribute currentCompetitionAttribute;

    public Cell currentSeasonYearLookupCell;
    public RuntimeAttribute seasonYearnameAttribute;

    public Cell currentCompetitionLookupCell;
    public RuntimeAttribute competitionnameAttribute;

    public MaintainGlobalDT( SdzBagI sdzBag)
    {
        try
        {
            ui0 = (GlobalPanel)sdzBag.getPane( 0);
        }
        catch(ClassCastException ex)
        {
            Err.error( "Did not expect a " + sdzBag.getPane( 0).getClass().getName());
        }

        strand = sdzBag.getStrand();

        globalNode = strand.getNodeByName( "Global Node");

        globalCell = globalNode.getCell();
        globalcurrentSeasonYearAttribute = globalCell.getAttributeByName( "global currentSeasonYear");
        currentCompetitionAttribute = globalCell.getAttributeByName( "currentCompetition");

        currentSeasonYearLookupCell = globalCell.getCellByName( "currentSeasonYear Lookup Cell");
        seasonYearnameAttribute = currentSeasonYearLookupCell.getAttributeByName( "seasonYear name");

        currentCompetitionLookupCell = globalCell.getCellByName( "currentCompetition Lookup Cell");
        competitionnameAttribute = currentCompetitionLookupCell.getAttributeByName( "competition name");


    }
}