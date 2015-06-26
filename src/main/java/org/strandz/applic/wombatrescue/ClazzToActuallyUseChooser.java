package org.strandz.applic.wombatrescue;

import org.strandz.lgpl.util.Clazz;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.persist.ORMTypeEnum;
import org.strandz.lgpl.data.objects.MonthInYear;
import org.strandz.core.interf.Cell;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Chris
 * Date: 1/09/2008
 * Time: 13:29:16
 */
class ClazzToActuallyUseChooser
{
    private Map<String, TypesToUse> map = new HashMap<String, TypesToUse>();

    private static class TypesToUse
    {
        Class primary;
        Class secondary;

        private TypesToUse(Class primary, Class secondary)
        {
            this.primary = primary;
            this.secondary = secondary;
        }
    }

    ClazzToActuallyUseChooser( ORMTypeEnum type)
    {
        String cellName;
        Class primaryTypeToUse;
        Class secondaryTypeToUse;
        {
            cellName = "Worker Cell";
            if(type.isCayenne())
            {
                if(type == ORMTypeEnum.CAYENNE_SERVER)
                {
                    primaryTypeToUse = org.strandz.data.wombatrescue.objects.cayenne.Worker.class;
                }
                else
                {
                    primaryTypeToUse = org.strandz.data.wombatrescue.objects.cayenne.client.Worker.class;
                }
                secondaryTypeToUse = org.strandz.data.wombatrescue.objects.Worker.class;
            }
            else
            {
                primaryTypeToUse = org.strandz.data.wombatrescue.objects.Worker.class;
                secondaryTypeToUse = null;
            }
            map.put( cellName, new TypesToUse( primaryTypeToUse, secondaryTypeToUse));
        }
        {
            cellName = "belongsToGroup Lookup Cell";
            if(type.isCayenne())
            {
                if(type == ORMTypeEnum.CAYENNE_SERVER)
                {
                    primaryTypeToUse = org.strandz.data.wombatrescue.objects.cayenne.Worker.class;
                }
                else
                {
                    primaryTypeToUse = org.strandz.data.wombatrescue.objects.cayenne.client.Worker.class;
                }
                secondaryTypeToUse = org.strandz.data.wombatrescue.objects.Worker.class;
            }
            else
            {
                primaryTypeToUse = org.strandz.data.wombatrescue.objects.Worker.class;
                secondaryTypeToUse = null;
            }
            map.put( cellName, new TypesToUse( primaryTypeToUse, secondaryTypeToUse));
        }
        {
            cellName = "seniority Lookup Cell";
            if(type.isCayenne())
            {
                if(type == ORMTypeEnum.CAYENNE_SERVER)
                {
                    primaryTypeToUse = org.strandz.data.wombatrescue.objects.cayenne.Seniority.class;
                }
                else
                {
                    primaryTypeToUse = org.strandz.data.wombatrescue.objects.cayenne.client.Seniority.class;
                }
                secondaryTypeToUse = org.strandz.data.wombatrescue.objects.Seniority.class;
            }
            else
            {
                primaryTypeToUse = org.strandz.data.wombatrescue.objects.Seniority.class;
                secondaryTypeToUse = null;
            }
            map.put( cellName, new TypesToUse( primaryTypeToUse, secondaryTypeToUse));
        }
        {
            cellName = "sex Lookup Cell";
            if(type.isCayenne())
            {
                if(type == ORMTypeEnum.CAYENNE_SERVER)
                {
                    primaryTypeToUse = org.strandz.data.wombatrescue.objects.cayenne.Sex.class;
                }
                else
                {
                    primaryTypeToUse = org.strandz.data.wombatrescue.objects.cayenne.client.Sex.class;
                }
                secondaryTypeToUse = org.strandz.data.wombatrescue.objects.Sex.class;
            }
            else
            {
                primaryTypeToUse = org.strandz.data.wombatrescue.objects.Sex.class;
                secondaryTypeToUse = null;
            }
            map.put( cellName, new TypesToUse( primaryTypeToUse, secondaryTypeToUse));
        }
        {
            cellName = "shiftPreference Lookup Cell";
            if(type.isCayenne())
            {
                if(type == ORMTypeEnum.CAYENNE_SERVER)
                {
                    primaryTypeToUse = org.strandz.data.wombatrescue.objects.cayenne.WhichShift.class;
                }
                else
                {
                    primaryTypeToUse = org.strandz.data.wombatrescue.objects.cayenne.client.WhichShift.class;
                }
                secondaryTypeToUse = org.strandz.data.wombatrescue.objects.WhichShift.class;
            }
            else
            {
                primaryTypeToUse = org.strandz.data.wombatrescue.objects.WhichShift.class;
                secondaryTypeToUse = null;
            }
            map.put( cellName, new TypesToUse( primaryTypeToUse, secondaryTypeToUse));
        }
        {
            cellName = "flexibility Lookup Cell";
            if(type.isCayenne())
            {
                if(type == ORMTypeEnum.CAYENNE_SERVER)
                {
                    primaryTypeToUse = org.strandz.data.wombatrescue.objects.cayenne.Flexibility.class;
                }
                else
                {
                    primaryTypeToUse = org.strandz.data.wombatrescue.objects.cayenne.client.Flexibility.class;
                }
                secondaryTypeToUse = org.strandz.data.wombatrescue.objects.Flexibility.class;
            }
            else
            {
                primaryTypeToUse = org.strandz.data.wombatrescue.objects.Flexibility.class;
                secondaryTypeToUse = null;
            }
            map.put( cellName, new TypesToUse( primaryTypeToUse, secondaryTypeToUse));
        }
        {
            cellName = "rosterSlots Cell";
            if(type.isCayenne())
            {
                if(type == ORMTypeEnum.CAYENNE_SERVER)
                {
                    primaryTypeToUse = org.strandz.data.wombatrescue.objects.cayenne.RosterSlot.class;
                }
                else
                {
                    primaryTypeToUse = org.strandz.data.wombatrescue.objects.cayenne.client.RosterSlot.class;
                }
                secondaryTypeToUse = org.strandz.data.wombatrescue.objects.RosterSlot.class;
            }
            else
            {
                primaryTypeToUse = org.strandz.data.wombatrescue.objects.RosterSlot.class;
                secondaryTypeToUse = null;
            }
            map.put( cellName, new TypesToUse( primaryTypeToUse, secondaryTypeToUse));
        }
        {
            cellName = "interval Lookup Cell";
            if(type.isCayenne())
            {
                if(type == ORMTypeEnum.CAYENNE_SERVER)
                {
                    primaryTypeToUse = org.strandz.data.wombatrescue.objects.cayenne.NumDaysInterval.class;
                }
                else
                {
                    primaryTypeToUse = org.strandz.data.wombatrescue.objects.cayenne.client.NumDaysInterval.class;
                }
                secondaryTypeToUse = org.strandz.data.wombatrescue.objects.NumDaysInterval.class;
            }
            else
            {
                primaryTypeToUse = org.strandz.data.wombatrescue.objects.NumDaysInterval.class;
                secondaryTypeToUse = null;
            }
            map.put( cellName, new TypesToUse( primaryTypeToUse, secondaryTypeToUse));
        }
        {
            cellName = "dayInWeek Lookup Cell";
            if(type.isCayenne())
            {
                if(type == ORMTypeEnum.CAYENNE_SERVER)
                {
                    primaryTypeToUse = org.strandz.data.wombatrescue.objects.cayenne.DayInWeek.class;
                }
                else
                {
                    primaryTypeToUse = org.strandz.data.wombatrescue.objects.cayenne.client.DayInWeek.class;
                }
                secondaryTypeToUse = org.strandz.lgpl.data.objects.DayInWeek.class;
            }
            else
            {
                primaryTypeToUse = org.strandz.lgpl.data.objects.DayInWeek.class;
                secondaryTypeToUse = null;
            }
            map.put( cellName, new TypesToUse( primaryTypeToUse, secondaryTypeToUse));
        }
        {
            cellName = "notInMonth Lookup Cell";
            if(type.isCayenne())
            {
                if(type == ORMTypeEnum.CAYENNE_SERVER)
                {
                    primaryTypeToUse = org.strandz.data.wombatrescue.objects.cayenne.MonthInYear.class;
                }
                else
                {
                    primaryTypeToUse = org.strandz.data.wombatrescue.objects.cayenne.client.MonthInYear.class;
                }
                secondaryTypeToUse = MonthInYear.class;
            }
            else
            {
                primaryTypeToUse = MonthInYear.class;
                secondaryTypeToUse = null;
            }
            map.put( cellName, new TypesToUse( primaryTypeToUse, secondaryTypeToUse));
        }
        {
            cellName = "onlyInMonth Lookup Cell";
            if(type.isCayenne())
            {
                if(type == ORMTypeEnum.CAYENNE_SERVER)
                {
                    primaryTypeToUse = org.strandz.data.wombatrescue.objects.cayenne.MonthInYear.class;
                }
                else
                {
                    primaryTypeToUse = org.strandz.data.wombatrescue.objects.cayenne.client.MonthInYear.class;
                }
                secondaryTypeToUse = MonthInYear.class;
            }
            else
            {
                primaryTypeToUse = MonthInYear.class;
                secondaryTypeToUse = null;
            }
            map.put( cellName, new TypesToUse( primaryTypeToUse, secondaryTypeToUse));
        }
        {
            cellName = "overridesOthers Lookup Cell";
            if(type.isCayenne())
            {
                if(type == ORMTypeEnum.CAYENNE_SERVER)
                {
                    primaryTypeToUse = org.strandz.data.wombatrescue.objects.cayenne.Override.class;
                }
                else
                {
                    primaryTypeToUse = org.strandz.data.wombatrescue.objects.cayenne.client.Override.class;
                }
                secondaryTypeToUse = org.strandz.data.wombatrescue.objects.Override.class;
            }
            else
            {
                primaryTypeToUse = org.strandz.data.wombatrescue.objects.Override.class;
                secondaryTypeToUse = null;
            }
            map.put( cellName, new TypesToUse( primaryTypeToUse, secondaryTypeToUse));
        }
        {
            cellName = "weekInMonth Lookup Cell";
            if(type.isCayenne())
            {
                if(type == ORMTypeEnum.CAYENNE_SERVER)
                {
                    primaryTypeToUse = org.strandz.data.wombatrescue.objects.cayenne.WeekInMonth.class;
                }
                else
                {
                    primaryTypeToUse = org.strandz.data.wombatrescue.objects.cayenne.client.WeekInMonth.class;
                }
                secondaryTypeToUse = org.strandz.data.wombatrescue.objects.WeekInMonth.class;
            }
            else
            {
                primaryTypeToUse = org.strandz.data.wombatrescue.objects.WeekInMonth.class;
                secondaryTypeToUse = null;
            }
            map.put( cellName, new TypesToUse( primaryTypeToUse, secondaryTypeToUse));
        }
        {
            cellName = "whichShift Lookup Cell";
            if(type.isCayenne())
            {
                if(type == ORMTypeEnum.CAYENNE_SERVER)
                {
                    primaryTypeToUse = org.strandz.data.wombatrescue.objects.cayenne.WhichShift.class;
                }
                else
                {
                    primaryTypeToUse = org.strandz.data.wombatrescue.objects.cayenne.client.WhichShift.class;
                }
                secondaryTypeToUse = org.strandz.data.wombatrescue.objects.WhichShift.class;
            }
            else
            {
                primaryTypeToUse = org.strandz.data.wombatrescue.objects.WhichShift.class;
                secondaryTypeToUse = null;
            }
            map.put( cellName, new TypesToUse( primaryTypeToUse, secondaryTypeToUse));
        }
        {
            cellName = "worker Lookup Cell";
            if(type.isCayenne())
            {
                if(type == ORMTypeEnum.CAYENNE_SERVER)
                {
                    primaryTypeToUse = org.strandz.data.wombatrescue.objects.cayenne.Worker.class;
                }
                else
                {
                    primaryTypeToUse = org.strandz.data.wombatrescue.objects.cayenne.client.Worker.class;
                }
                secondaryTypeToUse = org.strandz.data.wombatrescue.objects.Worker.class;
            }
            else
            {
                primaryTypeToUse = org.strandz.data.wombatrescue.objects.Worker.class;
                secondaryTypeToUse = null;
            }
            map.put( cellName, new TypesToUse( primaryTypeToUse, secondaryTypeToUse));
        }
        {
            cellName = "rosterSlots Quick Cell";
            if(type.isCayenne())
            {
                if(type == ORMTypeEnum.CAYENNE_SERVER)
                {
                    primaryTypeToUse = org.strandz.data.wombatrescue.objects.cayenne.RosterSlot.class;
                }
                else
                {
                    primaryTypeToUse = org.strandz.data.wombatrescue.objects.cayenne.client.RosterSlot.class;
                }
                secondaryTypeToUse = org.strandz.data.wombatrescue.objects.RosterSlot.class;
            }
            else
            {
                primaryTypeToUse = org.strandz.data.wombatrescue.objects.RosterSlot.class;
                secondaryTypeToUse = null;
            }
            map.put( cellName, new TypesToUse( primaryTypeToUse, secondaryTypeToUse));
        }
        {
            cellName = "belongsToGroup Lookup Cell";
            if(type.isCayenne())
            {
                if(type == ORMTypeEnum.CAYENNE_SERVER)
                {
                    primaryTypeToUse = org.strandz.data.wombatrescue.objects.cayenne.Worker.class;
                }
                else
                {
                    primaryTypeToUse = org.strandz.data.wombatrescue.objects.cayenne.client.Worker.class;
                }
                secondaryTypeToUse = org.strandz.data.wombatrescue.objects.Worker.class;
            }
            else
            {
                primaryTypeToUse = org.strandz.data.wombatrescue.objects.Worker.class;
                secondaryTypeToUse = null;
            }
            map.put( cellName, new TypesToUse( primaryTypeToUse, secondaryTypeToUse));
        }
        {
            cellName = "ShiftManager Cell";
            //if(type == ORMTypeEnum.CAYENNE)
            //{
                primaryTypeToUse = org.strandz.data.wombatrescue.calculated.ShiftManagers.class;
                secondaryTypeToUse = null;
            //}
            //else
            //{
            //    primaryTypeToUse = org.strandz.data.wombatrescue.calculated.ShiftManagers.class;
            //}
            map.put( cellName, new TypesToUse( primaryTypeToUse, secondaryTypeToUse));
        }
        {
            cellName = "friDinnerWker Lookup Cell";
            if(type.isCayenne())
            {
                if(type == ORMTypeEnum.CAYENNE_SERVER)
                {
                    primaryTypeToUse = org.strandz.data.wombatrescue.objects.cayenne.Worker.class;
                }
                else
                {
                    primaryTypeToUse = org.strandz.data.wombatrescue.objects.cayenne.client.Worker.class;
                }
                secondaryTypeToUse = org.strandz.data.wombatrescue.objects.Worker.class;
            }
            else
            {
                primaryTypeToUse = org.strandz.data.wombatrescue.objects.Worker.class;
                secondaryTypeToUse = null;
            }
            map.put( cellName, new TypesToUse( primaryTypeToUse, secondaryTypeToUse));
        }
        {
            cellName = "friOvernightWker Lookup Cell";
            if(type.isCayenne())
            {
                if(type == ORMTypeEnum.CAYENNE_SERVER)
                {
                    primaryTypeToUse = org.strandz.data.wombatrescue.objects.cayenne.Worker.class;
                }
                else
                {
                    primaryTypeToUse = org.strandz.data.wombatrescue.objects.cayenne.client.Worker.class;
                }
                secondaryTypeToUse = org.strandz.data.wombatrescue.objects.Worker.class;
            }
            else
            {
                primaryTypeToUse = org.strandz.data.wombatrescue.objects.Worker.class;
                secondaryTypeToUse = null;
            }
            map.put( cellName, new TypesToUse( primaryTypeToUse, secondaryTypeToUse));
        }
        {
            cellName = "monDinnerWker Lookup Cell";
            if(type.isCayenne())
            {
                if(type == ORMTypeEnum.CAYENNE_SERVER)
                {
                    primaryTypeToUse = org.strandz.data.wombatrescue.objects.cayenne.Worker.class;
                }
                else
                {
                    primaryTypeToUse = org.strandz.data.wombatrescue.objects.cayenne.client.Worker.class;
                }
                secondaryTypeToUse = org.strandz.data.wombatrescue.objects.Worker.class;
            }
            else
            {
                primaryTypeToUse = org.strandz.data.wombatrescue.objects.Worker.class;
                secondaryTypeToUse = null;
            }
            map.put( cellName, new TypesToUse( primaryTypeToUse, secondaryTypeToUse));
        }
        {
            cellName = "monOvernightWker Lookup Cell";
            if(type.isCayenne())
            {
                if(type == ORMTypeEnum.CAYENNE_SERVER)
                {
                    primaryTypeToUse = org.strandz.data.wombatrescue.objects.cayenne.Worker.class;
                }
                else
                {
                    primaryTypeToUse = org.strandz.data.wombatrescue.objects.cayenne.client.Worker.class;
                }
                secondaryTypeToUse = org.strandz.data.wombatrescue.objects.Worker.class;
            }
            else
            {
                primaryTypeToUse = org.strandz.data.wombatrescue.objects.Worker.class;
                secondaryTypeToUse = null;
            }
            map.put( cellName, new TypesToUse( primaryTypeToUse, secondaryTypeToUse));
        }
        {
            cellName = "tueDinnerWker Lookup Cell";
            if(type.isCayenne())
            {
                if(type == ORMTypeEnum.CAYENNE_SERVER)
                {
                    primaryTypeToUse = org.strandz.data.wombatrescue.objects.cayenne.Worker.class;
                }
                else
                {
                    primaryTypeToUse = org.strandz.data.wombatrescue.objects.cayenne.client.Worker.class;
                }
                secondaryTypeToUse = org.strandz.data.wombatrescue.objects.Worker.class;
            }
            else
            {
                primaryTypeToUse = org.strandz.data.wombatrescue.objects.Worker.class;
                secondaryTypeToUse = null;
            }
            map.put( cellName, new TypesToUse( primaryTypeToUse, secondaryTypeToUse));
        }
        {
            cellName = "tueOvernightWker Lookup Cell";
            if(type.isCayenne())
            {
                if(type == ORMTypeEnum.CAYENNE_SERVER)
                {
                    primaryTypeToUse = org.strandz.data.wombatrescue.objects.cayenne.Worker.class;
                }
                else
                {
                    primaryTypeToUse = org.strandz.data.wombatrescue.objects.cayenne.client.Worker.class;
                }
                secondaryTypeToUse = org.strandz.data.wombatrescue.objects.Worker.class;
            }
            else
            {
                primaryTypeToUse = org.strandz.data.wombatrescue.objects.Worker.class;
                secondaryTypeToUse = null;
            }
            map.put( cellName, new TypesToUse( primaryTypeToUse, secondaryTypeToUse));
        }
        {
            cellName = "wedDinnerWker Lookup Cell";
            if(type.isCayenne())
            {
                if(type == ORMTypeEnum.CAYENNE_SERVER)
                {
                    primaryTypeToUse = org.strandz.data.wombatrescue.objects.cayenne.Worker.class;
                }
                else
                {
                    primaryTypeToUse = org.strandz.data.wombatrescue.objects.cayenne.client.Worker.class;
                }
                secondaryTypeToUse = org.strandz.data.wombatrescue.objects.Worker.class;
            }
            else
            {
                primaryTypeToUse = org.strandz.data.wombatrescue.objects.Worker.class;
                secondaryTypeToUse = null;
            }
            map.put( cellName, new TypesToUse( primaryTypeToUse, secondaryTypeToUse));
        }
        {
            cellName = "wedOvernightWker Lookup Cell";
            if(type.isCayenne())
            {
                if(type == ORMTypeEnum.CAYENNE_SERVER)
                {
                    primaryTypeToUse = org.strandz.data.wombatrescue.objects.cayenne.Worker.class;
                }
                else
                {
                    primaryTypeToUse = org.strandz.data.wombatrescue.objects.cayenne.client.Worker.class;
                }
                secondaryTypeToUse = org.strandz.data.wombatrescue.objects.Worker.class;
            }
            else
            {
                primaryTypeToUse = org.strandz.data.wombatrescue.objects.Worker.class;
                secondaryTypeToUse = null;
            }
            map.put( cellName, new TypesToUse( primaryTypeToUse, secondaryTypeToUse));
        }
        {
            cellName = "thuDinnerWker Lookup Cell";
            if(type.isCayenne())
            {
                if(type == ORMTypeEnum.CAYENNE_SERVER)
                {
                    primaryTypeToUse = org.strandz.data.wombatrescue.objects.cayenne.Worker.class;
                }
                else
                {
                    primaryTypeToUse = org.strandz.data.wombatrescue.objects.cayenne.client.Worker.class;
                }
                secondaryTypeToUse = org.strandz.data.wombatrescue.objects.Worker.class;
            }
            else
            {
                primaryTypeToUse = org.strandz.data.wombatrescue.objects.Worker.class;
                secondaryTypeToUse = null;
            }
            map.put( cellName, new TypesToUse( primaryTypeToUse, secondaryTypeToUse));
        }
        {
            cellName = "thuOvernightWker Lookup Cell";
            if(type.isCayenne())
            {
                if(type == ORMTypeEnum.CAYENNE_SERVER)
                {
                    primaryTypeToUse = org.strandz.data.wombatrescue.objects.cayenne.Worker.class;
                }
                else
                {
                    primaryTypeToUse = org.strandz.data.wombatrescue.objects.cayenne.client.Worker.class;
                }
                secondaryTypeToUse = org.strandz.data.wombatrescue.objects.Worker.class;
            }
            else
            {
                primaryTypeToUse = org.strandz.data.wombatrescue.objects.Worker.class;
                secondaryTypeToUse = null;
            }
            map.put( cellName, new TypesToUse( primaryTypeToUse, secondaryTypeToUse));
        }
        {
            cellName = "friDinnerWker Lookup Cell";
            if(type.isCayenne())
            {
                if(type == ORMTypeEnum.CAYENNE_SERVER)
                {
                    primaryTypeToUse = org.strandz.data.wombatrescue.objects.cayenne.Worker.class;
                }
                else
                {
                    primaryTypeToUse = org.strandz.data.wombatrescue.objects.cayenne.client.Worker.class;
                }
                secondaryTypeToUse = org.strandz.data.wombatrescue.objects.Worker.class;
            }
            else
            {
                primaryTypeToUse = org.strandz.data.wombatrescue.objects.Worker.class;
                secondaryTypeToUse = null;
            }
            map.put( cellName, new TypesToUse( primaryTypeToUse, secondaryTypeToUse));
        }
        {
            cellName = "friOvernightWker Lookup Cell";
            if(type.isCayenne())
            {
                if(type == ORMTypeEnum.CAYENNE_SERVER)
                {
                    primaryTypeToUse = org.strandz.data.wombatrescue.objects.cayenne.Worker.class;
                }
                else
                {
                    primaryTypeToUse = org.strandz.data.wombatrescue.objects.cayenne.client.Worker.class;
                }
                secondaryTypeToUse = org.strandz.data.wombatrescue.objects.Worker.class;
            }
            else
            {
                primaryTypeToUse = org.strandz.data.wombatrescue.objects.Worker.class;
                secondaryTypeToUse = null;
            }
            map.put( cellName, new TypesToUse( primaryTypeToUse, secondaryTypeToUse));
        }
        {
            cellName = "satDinnerWker Lookup Cell";
            if(type.isCayenne())
            {
                if(type == ORMTypeEnum.CAYENNE_SERVER)
                {
                    primaryTypeToUse = org.strandz.data.wombatrescue.objects.cayenne.Worker.class;
                }
                else
                {
                    primaryTypeToUse = org.strandz.data.wombatrescue.objects.cayenne.client.Worker.class;
                }
                secondaryTypeToUse = org.strandz.data.wombatrescue.objects.Worker.class;;
            }
            else
            {
                primaryTypeToUse = org.strandz.data.wombatrescue.objects.Worker.class;
                secondaryTypeToUse = null;
            }
            map.put( cellName, new TypesToUse( primaryTypeToUse, secondaryTypeToUse));
        }
        {
            cellName = "satOvernightWker Lookup Cell";
            if(type.isCayenne())
            {
                if(type == ORMTypeEnum.CAYENNE_SERVER)
                {
                    primaryTypeToUse = org.strandz.data.wombatrescue.objects.cayenne.Worker.class;
                }
                else
                {
                    primaryTypeToUse = org.strandz.data.wombatrescue.objects.cayenne.client.Worker.class;
                }
                secondaryTypeToUse = org.strandz.data.wombatrescue.objects.Worker.class;
            }
            else
            {
                primaryTypeToUse = org.strandz.data.wombatrescue.objects.Worker.class;
                secondaryTypeToUse = null;
            }
            map.put( cellName, new TypesToUse( primaryTypeToUse, secondaryTypeToUse));
        }
    }

    Clazz getClazzToUse( Cell cell)
    {
        Clazz result;
        TypesToUse typesToUse = map.get( cell.getName());
        Assert.notNull( typesToUse.primary, "No entry for cell named: <" + cell.getName() + ">");
        result = new Clazz( typesToUse.primary);
        return result;
    }

    Clazz getSecondaryClazzToUse( Cell cell)
    {
        Clazz result = null;
        TypesToUse typesToUse = map.get( cell.getName());
        if(typesToUse.secondary != null)
        {
            result = new Clazz( typesToUse.secondary);
        }
        return result;
    }
}
