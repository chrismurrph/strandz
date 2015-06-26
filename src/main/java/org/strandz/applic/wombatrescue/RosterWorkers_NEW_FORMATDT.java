package org.strandz.applic.wombatrescue;import org.strandz.core.interf.SdzBagI;import org.strandz.core.interf.RuntimeAttribute;import org.strandz.core.interf.Cell;import org.strandz.core.interf.Node;import org.strandz.core.interf.Strand;import org.strandz.lgpl.util.Err;import org.strandz.view.wombatrescue.NarrowWorkerPanel;import org.strandz.view.wombatrescue.RosterSlotPanel;

public class RosterWorkers_NEW_FORMATDT
{
    public NarrowWorkerPanel ui0;
    public RosterSlotPanel ui1;
    public Strand strand;

    public Node workerNode;
    public Cell workerCell;
    public RuntimeAttribute belongsToGroupAttribute;
    public RuntimeAttribute workerBirthdayAttribute;
    public RuntimeAttribute christianNameAttribute;
    public RuntimeAttribute commentsAttribute;
    public RuntimeAttribute contactNameAttribute;
    public RuntimeAttribute email1Attribute;
    public RuntimeAttribute email2Attribute;
    public RuntimeAttribute groupContactPersonAttribute;
    public RuntimeAttribute groupNameAttribute;
    public RuntimeAttribute homePhoneAttribute;
    public RuntimeAttribute mobilePhoneAttribute;
    public RuntimeAttribute postcodeAttribute;
    public RuntimeAttribute seniorityAttribute;
    public RuntimeAttribute sexAttribute;
    public RuntimeAttribute shiftPreferenceAttribute;
    public RuntimeAttribute streetAttribute;
    public RuntimeAttribute suburbAttribute;
    public RuntimeAttribute surnameAttribute;
    public RuntimeAttribute unknownAttribute;
    public RuntimeAttribute workPhoneAttribute;
    public RuntimeAttribute workerAway1StartAttribute;
    public RuntimeAttribute workerAway1EndAttribute;
    public RuntimeAttribute workerAway2StartAttribute;
    public RuntimeAttribute workerAway2EndAttribute;
    public RuntimeAttribute flexibilityAttribute;

    public Cell belongsToGroupLookupCell;
    public RuntimeAttribute belongsToGroupGroupNameAttribute;

    public Cell seniorityLookupCell;
    public RuntimeAttribute seniorityNameAttribute;

    public Cell sexLookupCell;
    public RuntimeAttribute sexNameAttribute;

    public Cell shiftPreferenceLookupCell;
    public RuntimeAttribute shiftPreferenceNameAttribute;

    public Cell flexibilityLookupCell;
    public RuntimeAttribute flexibilitynameAttribute;

    public Node rosterSlotsListDetailNode;
    public Cell rosterSlotsCell;
    public RuntimeAttribute rosterSlotdayInWeekAttribute;
    public RuntimeAttribute rosterSlotdisabledAttribute;
    public RuntimeAttribute rosterSlotintervalAttribute;
    public RuntimeAttribute rosterSlotmonthlyRestartAttribute;
    public RuntimeAttribute rosterSlotnotAvailableAttribute;
    public RuntimeAttribute rosterSlotnotInMonthAttribute;
    public RuntimeAttribute rosterSlotonlyInMonthAttribute;
    public RuntimeAttribute rosterSlotoverridesOthersAttribute;
    public RuntimeAttribute rosterSlotspecificDateAttribute;
    public RuntimeAttribute rosterSlotstartDateAttribute;
    public RuntimeAttribute rosterSlotweekInMonthAttribute;
    public RuntimeAttribute rosterSlotwhichShiftAttribute;
    public RuntimeAttribute rosterSlotworkerAttribute;

    public Cell intervalLookupCell;
    public RuntimeAttribute numDaysIntervalnameAttribute;

    public Cell dayInWeekLookupCell;
    public RuntimeAttribute dayInWeeknameAttribute;

    public Cell notInMonthLookupCell;
    public RuntimeAttribute notmonthInYearnameAttribute;

    public Cell onlyInMonthLookupCell;
    public RuntimeAttribute onlymonthInYearnameAttribute;

    public Cell overridesOthersLookupCell;
    public RuntimeAttribute overridenameAttribute;

    public Cell weekInMonthLookupCell;
    public RuntimeAttribute weekInMonthnameAttribute;

    public Cell whichShiftLookupCell;
    public RuntimeAttribute whichShiftnameAttribute;

    public Cell workerLookupCell;
    public RuntimeAttribute workertoAttribute;

    public Node rosterSlotsQuickListDetailNode;
    public Cell rosterSlotsQuickCell;
    public RuntimeAttribute rosterSlotquickactiveAttribute;
    public RuntimeAttribute rosterSlotquicktoSentenceAttribute;

    public RosterWorkers_NEW_FORMATDT( SdzBagI sdzBag)
    {
        try
        {
            ui0 = (NarrowWorkerPanel)sdzBag.getPane( 0);
        }
        catch(ClassCastException ex)
        {
            Err.error( "Did not expect a " + sdzBag.getPane( 0).getClass().getName());
        }
        try
        {
            ui1 = (RosterSlotPanel)sdzBag.getPane( 1);
        }
        catch(ClassCastException ex)
        {
            Err.error( "Did not expect a " + sdzBag.getPane( 0).getClass().getName());
        }

        strand = sdzBag.getStrand();

        workerNode = strand.getNodeByName( "Worker Node");

        workerCell = workerNode.getCell();
        belongsToGroupAttribute = workerCell.getAttributeByName( "belongsToGroup");
        workerBirthdayAttribute = workerCell.getAttributeByName( "Worker Birthday");
        christianNameAttribute = workerCell.getAttributeByName( "christianName");
        commentsAttribute = workerCell.getAttributeByName( "comments");
        contactNameAttribute = workerCell.getAttributeByName( "contactName");
        email1Attribute = workerCell.getAttributeByName( "email1");
        email2Attribute = workerCell.getAttributeByName( "email2");
        groupContactPersonAttribute = workerCell.getAttributeByName( "groupContactPerson");
        groupNameAttribute = workerCell.getAttributeByName( "groupName");
        homePhoneAttribute = workerCell.getAttributeByName( "homePhone");
        mobilePhoneAttribute = workerCell.getAttributeByName( "mobilePhone");
        postcodeAttribute = workerCell.getAttributeByName( "postcode");
        seniorityAttribute = workerCell.getAttributeByName( "seniority");
        sexAttribute = workerCell.getAttributeByName( "sex");
        shiftPreferenceAttribute = workerCell.getAttributeByName( "shiftPreference");
        streetAttribute = workerCell.getAttributeByName( "street");
        suburbAttribute = workerCell.getAttributeByName( "suburb");
        surnameAttribute = workerCell.getAttributeByName( "surname");
        unknownAttribute = workerCell.getAttributeByName( "unknown");
        workPhoneAttribute = workerCell.getAttributeByName( "workPhone");
        workerAway1StartAttribute = workerCell.getAttributeByName( "Worker Away1Start");
        workerAway1EndAttribute = workerCell.getAttributeByName( "Worker Away1End");
        workerAway2StartAttribute = workerCell.getAttributeByName( "Worker Away2Start");
        workerAway2EndAttribute = workerCell.getAttributeByName( "Worker Away2End");
        flexibilityAttribute = workerCell.getAttributeByName( "flexibility");

        belongsToGroupLookupCell = workerCell.getCellByName( "belongsToGroup Lookup Cell");
        belongsToGroupGroupNameAttribute = belongsToGroupLookupCell.getAttributeByName( "belongsToGroup GroupName");

        seniorityLookupCell = workerCell.getCellByName( "seniority Lookup Cell");
        seniorityNameAttribute = seniorityLookupCell.getAttributeByName( "seniority Name");

        sexLookupCell = workerCell.getCellByName( "sex Lookup Cell");
        sexNameAttribute = sexLookupCell.getAttributeByName( "sex Name");

        shiftPreferenceLookupCell = workerCell.getCellByName( "shiftPreference Lookup Cell");
        shiftPreferenceNameAttribute = shiftPreferenceLookupCell.getAttributeByName( "shiftPreference Name");

        flexibilityLookupCell = workerCell.getCellByName( "flexibility Lookup Cell");
        flexibilitynameAttribute = flexibilityLookupCell.getAttributeByName( "flexibility name");


        rosterSlotsListDetailNode = strand.getNodeByName( "rosterSlots List Detail Node");

        rosterSlotsCell = rosterSlotsListDetailNode.getCell();
        rosterSlotdayInWeekAttribute = rosterSlotsCell.getAttributeByName( "rosterSlot dayInWeek");
        rosterSlotdisabledAttribute = rosterSlotsCell.getAttributeByName( "rosterSlot disabled");
        rosterSlotintervalAttribute = rosterSlotsCell.getAttributeByName( "rosterSlot interval");
        rosterSlotmonthlyRestartAttribute = rosterSlotsCell.getAttributeByName( "rosterSlot monthlyRestart");
        rosterSlotnotAvailableAttribute = rosterSlotsCell.getAttributeByName( "rosterSlot notAvailable");
        rosterSlotnotInMonthAttribute = rosterSlotsCell.getAttributeByName( "rosterSlot notInMonth");
        rosterSlotonlyInMonthAttribute = rosterSlotsCell.getAttributeByName( "rosterSlot onlyInMonth");
        rosterSlotoverridesOthersAttribute = rosterSlotsCell.getAttributeByName( "rosterSlot overridesOthers");
        rosterSlotspecificDateAttribute = rosterSlotsCell.getAttributeByName( "rosterSlot specificDate");
        rosterSlotstartDateAttribute = rosterSlotsCell.getAttributeByName( "rosterSlot startDate");
        rosterSlotweekInMonthAttribute = rosterSlotsCell.getAttributeByName( "rosterSlot weekInMonth");
        rosterSlotwhichShiftAttribute = rosterSlotsCell.getAttributeByName( "rosterSlot whichShift");
        rosterSlotworkerAttribute = rosterSlotsCell.getAttributeByName( "rosterSlot worker");

        intervalLookupCell = rosterSlotsCell.getCellByName( "interval Lookup Cell");
        numDaysIntervalnameAttribute = intervalLookupCell.getAttributeByName( "numDaysInterval name");

        dayInWeekLookupCell = rosterSlotsCell.getCellByName( "dayInWeek Lookup Cell");
        dayInWeeknameAttribute = dayInWeekLookupCell.getAttributeByName( "dayInWeek name");

        notInMonthLookupCell = rosterSlotsCell.getCellByName( "notInMonth Lookup Cell");
        notmonthInYearnameAttribute = notInMonthLookupCell.getAttributeByName( "not monthInYear name");

        onlyInMonthLookupCell = rosterSlotsCell.getCellByName( "onlyInMonth Lookup Cell");
        onlymonthInYearnameAttribute = onlyInMonthLookupCell.getAttributeByName( "only monthInYear name");

        overridesOthersLookupCell = rosterSlotsCell.getCellByName( "overridesOthers Lookup Cell");
        overridenameAttribute = overridesOthersLookupCell.getAttributeByName( "override name");

        weekInMonthLookupCell = rosterSlotsCell.getCellByName( "weekInMonth Lookup Cell");
        weekInMonthnameAttribute = weekInMonthLookupCell.getAttributeByName( "weekInMonth name");

        whichShiftLookupCell = rosterSlotsCell.getCellByName( "whichShift Lookup Cell");
        whichShiftnameAttribute = whichShiftLookupCell.getAttributeByName( "whichShift name");

        workerLookupCell = rosterSlotsCell.getCellByName( "worker Lookup Cell");
        workertoAttribute = workerLookupCell.getAttributeByName( "worker to");


        rosterSlotsQuickListDetailNode = strand.getNodeByName( "rosterSlots Quick List Detail Node");

        rosterSlotsQuickCell = rosterSlotsQuickListDetailNode.getCell();
        rosterSlotquickactiveAttribute = rosterSlotsQuickCell.getAttributeByName( "rosterSlot quick active");
        rosterSlotquicktoSentenceAttribute = rosterSlotsQuickCell.getAttributeByName( "rosterSlot quick toSentence");


    }
}