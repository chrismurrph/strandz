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
package org.strandz.store.timesheet;

import org.strandz.data.needs.objects.Contact;
import org.strandz.data.timesheet.business.Timesheet;
import org.strandz.data.timesheet.objects.Resource;
import org.strandz.data.timesheet.objects.Task;
import org.strandz.data.timesheet.objects.TaskResource;
import org.strandz.lgpl.store.XMLFileData;
import org.strandz.lgpl.util.DebugList;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.TimeUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class XMLTimesheetData extends XMLFileData
    implements TimesheetQueries
{
    private static final String testFileName = "C:\\cml\\data\\timesheet"
        + "\\XMLTestData.xml";
    private static final String devFileName = "C:\\cml\\data\\timesheet"
        + "\\XMLDevData.xml";
    private static final String prodFileName = "C:\\cml\\data\\timesheet"
        + "\\XMLProdData.xml";
    private static final FirstSecondNameOrder orderer = new FirstSecondNameOrder();
    private List allResources;
    private List allTasks;

    public XMLTimesheetData()
    {
        this(devFileName);
    }

    public XMLTimesheetData(String dbName)
    {
        Class classes[] = TimesheetData.CLASSES;
        // Print.pr( "Have " + classes.length + " classes");
        super.setClasses(classes);

        String fileName = convertToFileName(dbName);
        super.setFilename(fileName);
    }

    protected List createList(Class clazz)
    {
        List result = super.createList(clazz);
        if(clazz == TimesheetData.TIMESHEET)
        {
            result = new DebugList();
        }
        return result;
    }

    private String convertToFileName(String dbName)
    {
        String result = dbName;
        if(dbName.equals(TimesheetApplicationData.TEST))
        {
            result = testFileName;
        }
        else if(dbName.equals(TimesheetApplicationData.DEV))
        {
            result = devFileName;
        }
        else if(dbName.equals(TimesheetApplicationData.PROD))
        {
            result = prodFileName;
        }
        return result;
    }

    // Generated but won't work as no Timesheets in database
    public Collection queryAllTimesheets()
    {
        Err.error("queryAllTimesheets() will not work as no timesheets in DB");

        List result = (List) get(TimesheetData.TIMESHEET);
        Collections.sort(result, orderer);
        return result;
    }

    /**
     * Timesheets do not exist in the DB but are dynamically worked
     * out each time this query is done.
     * Given today, we can find the Dates for Mon - Sun.
     */
    public Collection queryAllTimesheetsOf(String resourceName, Date today)
    {
        allResources = (List) get(TimesheetData.RESOURCE);
        allTasks = (List) get(TimesheetData.TASK);

        ArrayList result = new ArrayList();
        Resource resource = queryResource(resourceName);
        List tasks = queryAllTasksOf(resource);
        List days = TimeUtils.getDaysOfWeek(today);
        List allHoursOnDayList = (List) get(TimesheetData.HOURS_ON_DAY);
        for(Iterator iter = tasks.iterator(); iter.hasNext();)
        {
            Task task = (Task) iter.next();
            Timesheet timesheet = new Timesheet(resource, task, days,
                allHoursOnDayList// , allResources, allTasks
            );
            result.add(timesheet);
        }
        return result;
    }

    private Resource queryResource(String name)
    {
        Resource result = null;
        for(Iterator iter = allResources.iterator(); iter.hasNext();)
        {
            Resource r = (Resource) iter.next();
            if(r.getName().equals(name))
            {
                result = r;
                break;
            }
        }
        return result;
    }

    private List queryAllTasksOf(Resource resource)
    {
        List result = new ArrayList();
        List taskResources = (List) get(TimesheetData.TASK_RESOURCE);
        for(Iterator iter = taskResources.iterator(); iter.hasNext();)
        {
            TaskResource tr = (TaskResource) iter.next();
            if(tr.getResource().equals(resource))
            {
                result.add(tr.getTask());
            }
        }
        return result;
    }

    private static class FirstSecondNameOrder implements Comparator
    {
        public int compare(Object one, Object two)
        {
            int result = 0;
            if(!(one instanceof Contact))
            {
                return 1;
            }
            if(!(two instanceof Contact))
            {
                return 1;
            }

            Contact con1 = (Contact) one;
            Contact con2 = (Contact) two;
            result = contactCf(con1, con2);
            return result;
        }

        /*
        * Trying to duplicate this:
        * q.setOrdering( "firstName ascending, secondName ascending");
        */
        private static int contactCf(Contact con1, Contact con2)
        {
            int result = -99;
            String text1 = con1.getSecondName();
            String text2 = con2.getSecondName();
            for(int i = 0; result == -99; i++)
            {
                if(text1 == null && text2 == null)
                {
                    if(i == 0)
                    {
                        text1 = con1.getFirstName();
                        text2 = con2.getFirstName();
                    }
                    /*
                    else if(i==1)
                    {
                    text1 = vol1.getGroupName();
                    text2 = vol2.getGroupName();
                    }
                    */
                    else
                    {
                        Err.error("Stopping infinite loop on ordering");
                    }
                }
                else if(text1 == null)
                {
                    if(i == 0)
                    {
                        text1 = con1.getFirstName();
                    }
                    /*
                    else if(i==1)
                    {
                    text1 = vol1.getGroupName();
                    }
                    */
                    else
                    {
                        Err.error("Stopping infinite loop on ordering");
                    }
                }
                else if(text2 == null)
                {
                    if(i == 0)
                    {
                        text2 = con2.getFirstName();
                    }
                    /*
                    else if(i==1)
                    {
                    text2 = con2.getGroupName();
                    }
                    */
                    else
                    {
                        Err.error("Stopping infinite loop on ordering");
                    }
                }
                else
                {
                    result = text1.compareTo(text2);
                }
            }
            return result;
        }
    }
}
