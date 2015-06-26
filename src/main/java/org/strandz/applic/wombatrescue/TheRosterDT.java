package org.strandz.applic.wombatrescue;import org.strandz.core.interf.SdzBagI;import org.strandz.core.interf.RuntimeAttribute;import org.strandz.core.interf.Cell;import org.strandz.core.interf.Node;import org.strandz.core.interf.Strand;import org.strandz.lgpl.util.Err;import org.strandz.view.wombatrescue.RosterTablePanel;

public class TheRosterDT
{
    public RosterTablePanel ui0;
    public Strand strand;

    public Node rosterMonthNode;
    public Cell rosterMonthCell;
    public RuntimeAttribute rosterMonthmonthAttribute;
    public RuntimeAttribute rosterMonthyearAttribute;

    public Cell monthLookupCell;
    public RuntimeAttribute monthInYearnameAttribute;

    public Node nightsListDetailNode;
    public Cell nightsListDetailCell;
    public RuntimeAttribute nightformattedDateAttribute;

    public Cell firstEveningLookupCell;
    public RuntimeAttribute firsteveningparticularShiftworkerAttribute;

    public Cell firsteveningworkerLookupCell;
    public RuntimeAttribute firsteveningworkertoShortAttribute;

    public Cell secondEveningLookupCell;
    public RuntimeAttribute secondeveningparticularShiftworkerAttribute;

    public Cell secondeveningworkerLookupCell;
    public RuntimeAttribute secondeveningworkertoShortAttribute;

    public Cell thirdEveningLookupCell;
    public RuntimeAttribute thirdeveningparticularShiftworkerAttribute;

    public Cell thirdeveningworkerLookupCell;
    public RuntimeAttribute thirdeveningworkertoShortAttribute;

    public Cell firstOvernightLookupCell;
    public RuntimeAttribute firstovernightparticularShiftworkerAttribute;

    public Cell firstovernightworkerLookupCell;
    public RuntimeAttribute firstovernightworkertoShortAttribute;

    public Cell secondOvernightLookupCell;
    public RuntimeAttribute secondovernightparticularShiftworkerAttribute;

    public Cell secondovernightworkerLookupCell;
    public RuntimeAttribute secondovernightworkertoShortAttribute;

    public Cell weekInMonthLookupCell;
    public RuntimeAttribute weekInMonthpkIdAttribute;

    public Node clashesListDetailNode;
    public Cell clashesCell;
    public RuntimeAttribute clashfirstEveningAttribute;
    public RuntimeAttribute clashfirstOvernightAttribute;
    public RuntimeAttribute clashformattedDateAttribute;
    public RuntimeAttribute clashsecondEveningAttribute;
    public RuntimeAttribute clashsecondOvernightAttribute;
    public RuntimeAttribute clashthirdEveningAttribute;
    public RuntimeAttribute clashtoReasonAttribute;
    public RuntimeAttribute clashtoSentenceAttribute;
    public RuntimeAttribute weekInMonthAttribute;

    public Cell clashesfirstEveningLookupCell;
    public RuntimeAttribute clashesfirsteveningparticularShiftworkerAttribute;

    public Cell clashesfirsteveningworkerLookupCell;
    public RuntimeAttribute clashesfirsteveningworkertoShortAttribute;

    public Cell clashesfirstOvernightLookupCell;
    public RuntimeAttribute clashesfirstovernightparticularShiftworkerAttribute;

    public Cell clashesfirstovernightworkerLookupCell;
    public RuntimeAttribute clashesfirstovernightworkertoShortAttribute;

    public Cell clashessecondEveningLookupCell;
    public RuntimeAttribute clashessecondeveningparticularShiftworkerAttribute;

    public Cell clashessecondeveningworkerLookupCell;
    public RuntimeAttribute clashessecondeveningworkertoShortAttribute;

    public Cell clashessecondOvernightLookupCell;
    public RuntimeAttribute clashessecondovernightparticularShiftworkerAttribute;

    public Cell clashessecondovernightworkerLookupCell;
    public RuntimeAttribute clashessecondovernightworkertoShortAttribute;

    public Cell clashesthirdEveningLookupCell;
    public RuntimeAttribute clashesthirdeveningparticularShiftworkerAttribute;

    public Cell clashesthirdeveningworkerLookupCell;
    public RuntimeAttribute clashesthirdeveningworkertoShortAttribute;

    public Cell clashweekInMonthLookupCell;
    public RuntimeAttribute clashweekInMonthpkIdAttribute;

    public Node failedNightsListDetailNode;
    public Cell failedNightsCell;
    public RuntimeAttribute nightfirstEveningAttribute;
    public RuntimeAttribute nightfirstOvernightAttribute;
    public RuntimeAttribute failednightformattedDateAttribute;
    public RuntimeAttribute nightsecondEveningAttribute;
    public RuntimeAttribute nightsecondOvernightAttribute;
    public RuntimeAttribute nightthirdEveningAttribute;
    public RuntimeAttribute nightweekInMonthAttribute;

    public Cell failedNightsfirstEveningLookupCell;
    public RuntimeAttribute failedNightsfirsteveningparticularShiftworkerAttribute;

    public Cell failedNightsfirsteveningworkerLookupCell;
    public RuntimeAttribute failedNightsfirsteveningworkertoShortAttribute;

    public Cell failedNightssecondEveningLookupCell;
    public RuntimeAttribute failedNightssecondeveningparticularShiftworkerAttribute;

    public Cell failedNightssecondeveningworkerLookupCell;
    public RuntimeAttribute failedNightssecondeveningworkertoShortAttribute;

    public Cell failednightsthirdEveningLookupCell;
    public RuntimeAttribute failednightsthirdeveningparticularShiftworkerAttribute;

    public Cell failednightsthirdeveningworkerLookupCell;
    public RuntimeAttribute failednightsthirdeveningworkertoShortAttribute;

    public Cell failednightsfirstOvernightLookupCell;
    public RuntimeAttribute failednightsfirstovernightparticularShiftworkerAttribute;

    public Cell failednightsfirstovernightworkerLookupCell;
    public RuntimeAttribute failednightsfirstovernightworkertoShortAttribute;

    public Cell failednightssecondOvernightLookupCell;
    public RuntimeAttribute failednightssecondovernightparticularShiftworkerAttribute;

    public Cell failednightssecondovernightworkerLookupCell;
    public RuntimeAttribute failednightssecondovernightworkertoShortAttribute;

    public Cell failednightweekInMonthLookupCell;
    public RuntimeAttribute failednightweekInMonthpkIdAttribute;

    public Node unrosteredAvailableWorkersListDetailNode;
    public Cell unrosteredAvailableWorkersCell;
    public RuntimeAttribute workertoShortAttribute;

    public TheRosterDT( SdzBagI sdzBag)
    {
        try
        {
            ui0 = (RosterTablePanel)sdzBag.getPane( 0);
        }
        catch(ClassCastException ex)
        {
            Err.error( "Did not expect a " + sdzBag.getPane( 0).getClass().getName());
        }

        strand = sdzBag.getStrand();

        rosterMonthNode = strand.getNodeByName( "RosterMonth Node");

        rosterMonthCell = rosterMonthNode.getCell();
        rosterMonthmonthAttribute = rosterMonthCell.getAttributeByName( "rosterMonth month");
        rosterMonthyearAttribute = rosterMonthCell.getAttributeByName( "rosterMonth year");

        monthLookupCell = rosterMonthCell.getCellByName( "month Lookup Cell");
        monthInYearnameAttribute = monthLookupCell.getAttributeByName( "monthInYear name");


        nightsListDetailNode = strand.getNodeByName( "nights List Detail Node");

        nightsListDetailCell = nightsListDetailNode.getCell();
        nightformattedDateAttribute = nightsListDetailCell.getAttributeByName( "night formattedDate");

        firstEveningLookupCell = nightsListDetailCell.getCellByName( "firstEvening Lookup Cell");
        firsteveningparticularShiftworkerAttribute = firstEveningLookupCell.getAttributeByName( "first evening particularShift worker");

        firsteveningworkerLookupCell = firstEveningLookupCell.getCellByName( "first evening worker Lookup Cell");
        firsteveningworkertoShortAttribute = firsteveningworkerLookupCell.getAttributeByName( "first evening worker toShort");

        secondEveningLookupCell = nightsListDetailCell.getCellByName( "secondEvening Lookup Cell");
        secondeveningparticularShiftworkerAttribute = secondEveningLookupCell.getAttributeByName( "second evening particularShift worker");

        secondeveningworkerLookupCell = secondEveningLookupCell.getCellByName( "second evening worker Lookup Cell");
        secondeveningworkertoShortAttribute = secondeveningworkerLookupCell.getAttributeByName( "second evening worker toShort");

        thirdEveningLookupCell = nightsListDetailCell.getCellByName( "thirdEvening Lookup Cell");
        thirdeveningparticularShiftworkerAttribute = thirdEveningLookupCell.getAttributeByName( "third evening particularShift worker");

        thirdeveningworkerLookupCell = thirdEveningLookupCell.getCellByName( "third evening worker Lookup Cell");
        thirdeveningworkertoShortAttribute = thirdeveningworkerLookupCell.getAttributeByName( "third evening worker toShort");

        firstOvernightLookupCell = nightsListDetailCell.getCellByName( "firstOvernight Lookup Cell");
        firstovernightparticularShiftworkerAttribute = firstOvernightLookupCell.getAttributeByName( "first overnight particularShift worker");

        firstovernightworkerLookupCell = firstOvernightLookupCell.getCellByName( "first overnight worker Lookup Cell");
        firstovernightworkertoShortAttribute = firstovernightworkerLookupCell.getAttributeByName( "first overnight worker toShort");

        secondOvernightLookupCell = nightsListDetailCell.getCellByName( "secondOvernight Lookup Cell");
        secondovernightparticularShiftworkerAttribute = secondOvernightLookupCell.getAttributeByName( "second overnight particularShift worker");

        secondovernightworkerLookupCell = secondOvernightLookupCell.getCellByName( "second overnight worker Lookup Cell");
        secondovernightworkertoShortAttribute = secondovernightworkerLookupCell.getAttributeByName( "second overnight worker toShort");

        weekInMonthLookupCell = nightsListDetailCell.getCellByName( "weekInMonth Lookup Cell");
        weekInMonthpkIdAttribute = weekInMonthLookupCell.getAttributeByName( "weekInMonth pkId");


        clashesListDetailNode = strand.getNodeByName( "clashes List Detail Node");

        clashesCell = clashesListDetailNode.getCell();
        clashfirstEveningAttribute = clashesCell.getAttributeByName( "clash firstEvening");
        clashfirstOvernightAttribute = clashesCell.getAttributeByName( "clash firstOvernight");
        clashformattedDateAttribute = clashesCell.getAttributeByName( "clash formattedDate");
        clashsecondEveningAttribute = clashesCell.getAttributeByName( "clash secondEvening");
        clashsecondOvernightAttribute = clashesCell.getAttributeByName( "clash secondOvernight");
        clashthirdEveningAttribute = clashesCell.getAttributeByName( "clash thirdEvening");
        clashtoReasonAttribute = clashesCell.getAttributeByName( "clash toReason");
        clashtoSentenceAttribute = clashesCell.getAttributeByName( "clash toSentence");
        weekInMonthAttribute = clashesCell.getAttributeByName( "weekInMonth");

        clashesfirstEveningLookupCell = clashesCell.getCellByName( "clashes firstEvening Lookup Cell");
        clashesfirsteveningparticularShiftworkerAttribute = clashesfirstEveningLookupCell.getAttributeByName( "clashes first evening particularShift worker");

        clashesfirsteveningworkerLookupCell = clashesfirstEveningLookupCell.getCellByName( "clashes first evening worker Lookup Cell");
        clashesfirsteveningworkertoShortAttribute = clashesfirsteveningworkerLookupCell.getAttributeByName( "clashes first evening worker toShort");

        clashesfirstOvernightLookupCell = clashesCell.getCellByName( "clashes firstOvernight Lookup Cell");
        clashesfirstovernightparticularShiftworkerAttribute = clashesfirstOvernightLookupCell.getAttributeByName( "clashes first overnight particularShift worker");

        clashesfirstovernightworkerLookupCell = clashesfirstOvernightLookupCell.getCellByName( "clashes first overnight worker Lookup Cell");
        clashesfirstovernightworkertoShortAttribute = clashesfirstovernightworkerLookupCell.getAttributeByName( "clashes first overnight worker toShort");

        clashessecondEveningLookupCell = clashesCell.getCellByName( "clashes secondEvening Lookup Cell");
        clashessecondeveningparticularShiftworkerAttribute = clashessecondEveningLookupCell.getAttributeByName( "clashes second evening particularShift worker");

        clashessecondeveningworkerLookupCell = clashessecondEveningLookupCell.getCellByName( "clashes second evening worker Lookup Cell");
        clashessecondeveningworkertoShortAttribute = clashessecondeveningworkerLookupCell.getAttributeByName( "clashes second evening worker toShort");

        clashessecondOvernightLookupCell = clashesCell.getCellByName( "clashes secondOvernight Lookup Cell");
        clashessecondovernightparticularShiftworkerAttribute = clashessecondOvernightLookupCell.getAttributeByName( "clashes second overnight particularShift worker");

        clashessecondovernightworkerLookupCell = clashessecondOvernightLookupCell.getCellByName( "clashes second overnight worker Lookup Cell");
        clashessecondovernightworkertoShortAttribute = clashessecondovernightworkerLookupCell.getAttributeByName( "clashes second overnight worker toShort");

        clashesthirdEveningLookupCell = clashesCell.getCellByName( "clashes thirdEvening Lookup Cell");
        clashesthirdeveningparticularShiftworkerAttribute = clashesthirdEveningLookupCell.getAttributeByName( "clashes third evening particularShift worker");

        clashesthirdeveningworkerLookupCell = clashesthirdEveningLookupCell.getCellByName( "clashes third evening worker Lookup Cell");
        clashesthirdeveningworkertoShortAttribute = clashesthirdeveningworkerLookupCell.getAttributeByName( "clashes third evening worker toShort");

        clashweekInMonthLookupCell = clashesCell.getCellByName( "clash weekInMonth Lookup Cell");
        clashweekInMonthpkIdAttribute = clashweekInMonthLookupCell.getAttributeByName( "clash weekInMonth pkId");


        failedNightsListDetailNode = strand.getNodeByName( "failedNights List Detail Node");

        failedNightsCell = failedNightsListDetailNode.getCell();
        nightfirstEveningAttribute = failedNightsCell.getAttributeByName( "night firstEvening");
        nightfirstOvernightAttribute = failedNightsCell.getAttributeByName( "night firstOvernight");
        failednightformattedDateAttribute = failedNightsCell.getAttributeByName( "failed night formattedDate");
        nightsecondEveningAttribute = failedNightsCell.getAttributeByName( "night secondEvening");
        nightsecondOvernightAttribute = failedNightsCell.getAttributeByName( "night secondOvernight");
        nightthirdEveningAttribute = failedNightsCell.getAttributeByName( "night thirdEvening");
        nightweekInMonthAttribute = failedNightsCell.getAttributeByName( "night weekInMonth");

        failedNightsfirstEveningLookupCell = failedNightsCell.getCellByName( "failedNights firstEvening Lookup Cell");
        failedNightsfirsteveningparticularShiftworkerAttribute = failedNightsfirstEveningLookupCell.getAttributeByName( "failedNights first evening particularShift worker");

        failedNightsfirsteveningworkerLookupCell = failedNightsfirstEveningLookupCell.getCellByName( "failedNights first evening worker Lookup Cell");
        failedNightsfirsteveningworkertoShortAttribute = failedNightsfirsteveningworkerLookupCell.getAttributeByName( "failedNights first evening worker toShort");

        failedNightssecondEveningLookupCell = failedNightsCell.getCellByName( "failedNights secondEvening Lookup Cell");
        failedNightssecondeveningparticularShiftworkerAttribute = failedNightssecondEveningLookupCell.getAttributeByName( "failedNights second evening particularShift worker");

        failedNightssecondeveningworkerLookupCell = failedNightssecondEveningLookupCell.getCellByName( "failedNights second evening worker Lookup Cell");
        failedNightssecondeveningworkertoShortAttribute = failedNightssecondeveningworkerLookupCell.getAttributeByName( "failedNights second evening worker toShort");

        failednightsthirdEveningLookupCell = failedNightsCell.getCellByName( "failed nights thirdEvening Lookup Cell");
        failednightsthirdeveningparticularShiftworkerAttribute = failednightsthirdEveningLookupCell.getAttributeByName( "failed nights third evening particularShift worker");

        failednightsthirdeveningworkerLookupCell = failednightsthirdEveningLookupCell.getCellByName( "failed nights third evening worker Lookup Cell");
        failednightsthirdeveningworkertoShortAttribute = failednightsthirdeveningworkerLookupCell.getAttributeByName( "failed nights third evening worker toShort");

        failednightsfirstOvernightLookupCell = failedNightsCell.getCellByName( "failed nights firstOvernight Lookup Cell");
        failednightsfirstovernightparticularShiftworkerAttribute = failednightsfirstOvernightLookupCell.getAttributeByName( "failed nights first overnight particularShift worker");

        failednightsfirstovernightworkerLookupCell = failednightsfirstOvernightLookupCell.getCellByName( "failed nights first overnight worker Lookup Cell");
        failednightsfirstovernightworkertoShortAttribute = failednightsfirstovernightworkerLookupCell.getAttributeByName( "failed nights first overnight worker toShort");

        failednightssecondOvernightLookupCell = failedNightsCell.getCellByName( "failed nights secondOvernight Lookup Cell");
        failednightssecondovernightparticularShiftworkerAttribute = failednightssecondOvernightLookupCell.getAttributeByName( "failed nights second overnight particularShift worker");

        failednightssecondovernightworkerLookupCell = failednightssecondOvernightLookupCell.getCellByName( "failed nights second overnight worker Lookup Cell");
        failednightssecondovernightworkertoShortAttribute = failednightssecondovernightworkerLookupCell.getAttributeByName( "failed nights second overnight worker toShort");

        failednightweekInMonthLookupCell = failedNightsCell.getCellByName( "failed night weekInMonth Lookup Cell");
        failednightweekInMonthpkIdAttribute = failednightweekInMonthLookupCell.getAttributeByName( "failed night weekInMonth pkId");


        unrosteredAvailableWorkersListDetailNode = strand.getNodeByName( "unrosteredAvailableWorkers List Detail Node");

        unrosteredAvailableWorkersCell = unrosteredAvailableWorkersListDetailNode.getCell();
        workertoShortAttribute = unrosteredAvailableWorkersCell.getAttributeByName( "worker toShort");


    }
}