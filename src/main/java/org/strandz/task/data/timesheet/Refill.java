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
package org.strandz.task.data.timesheet;

import org.strandz.data.timesheet.objects.Project;
import org.strandz.data.timesheet.objects.Resource;
import org.strandz.data.timesheet.objects.Task;
import org.strandz.data.timesheet.objects.TaskResource;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Print;
import org.strandz.store.timesheet.TimesheetApplicationData;
import org.strandz.store.timesheet.TimesheetData;

import java.util.ArrayList;
import java.util.List;

public class Refill
{
    private static Resource res1;
    private static Resource res2;
    private static Resource res3;
    private static Project proj1;
    private static Task task1;
    private static Task task2;
    private static Task task3;
    private static Task task4;
    private static Task task5;
    private static Task task6;
    private static Task task7;
    // LOV like this
    // private static List sendingMediums;
    private static TimesheetApplicationData td;
    private static boolean useCaptureFile = false;

    public static void main(String s[])
    {
        if(s.length != 0)
        {
            processParams(s);
        }
        else
        {
            String str[] = {"TimesheetData", TimesheetApplicationData.databaseName};
            processParams(str);
        }
    }

    public static TimesheetApplicationData getData()
    {
        if(td == null)
        {
            String str[] = {};
            main(str);
        }
        return td;
    }

    /**
     * Like removing all the records of a table but
     * leaving the table there!
     */
    private static void processParams(String s[])
    {
         /**/
        if(s[0].equals("TimesheetData"))
        {
            if(s.length == 1)
            {
                Err.error("Need to explicitly specify a database to refill");
            }
            if(s[1].equals(TimesheetApplicationData.PROD))
            {
                Err.error(
                    "Cannot refill the " + TimesheetApplicationData.PROD + " database");
            }
            if(s.length == 2)
            {
                td = TimesheetApplicationData.getInstance(s[1]);
            }
            else
            {
                td = TimesheetApplicationData.getInstance(s[1], s[2]);
            }
            // Err.error( "Use running of a capture file and scrap this!");
            if(!useCaptureFile)
            {
                td.getData().startTx();

                // LOV like this
                // sendingMediums = (List)td.getData().get( TimesheetData.SENDING_MEDIUM);
                 /**/
                List projects = getProjectData();
                Print.pr("************************************");
                Print.pr(projects.size() + " projects");
                Print.pr("************************************");
                td.getData().set(TimesheetData.PROJECT, projects);

                 /**/
                List tasks = getTaskData();
                Print.pr("************************************");
                Print.pr(tasks.size() + " tasks");
                Print.pr("************************************");
                td.getData().set(TimesheetData.TASK, tasks);

                 /**/
                List resources = getResourceData();
                Print.pr("************************************");
                Print.pr(resources.size() + " resources");
                Print.pr("************************************");
                td.getData().set(TimesheetData.RESOURCE, resources);

                 /**/
                List trs = getTaskResourceData();
                Print.pr("************************************");
                Print.pr(trs.size() + " task resources");
                Print.pr("************************************");
                td.getData().set(TimesheetData.TASK_RESOURCE, trs);
                 /**/
                td.getData().commitTx();
            }
            else
            {
                replay();
            }
        }
        else
        {
            Err.error("Unrecognised param " + s[0]);
        }
    }

    private static void replay()
    {
        Err.error("See Wombatrescue application for how to do this");
    }

    private static List getProjectData()
    {
        ArrayList projects = new ArrayList();
        {
            Project project;
            project = new Project();
            project.setName("Seaweed Startup");
            projects.add(project);
            proj1 = project;
        }
        return projects;
    }

    private static List getTaskData()
    {
        ArrayList tasks = new ArrayList();
        {
            Task task;
            task = new Task();
            task.setName("TableLayout screen generator");
            task.setTimePackets(new ArrayList());
            tasks.add(task);
            task1 = task;
            task = new Task();
            task.setName("drag and drop screen layout");
            task.setTimePackets(new ArrayList());
            tasks.add(task);
            task2 = task;
            task = new Task();
            task.setName("drag and drop CML/D");
            task.setTimePackets(new ArrayList());
            tasks.add(task);
            task3 = task;
            task = new Task();
            task.setName("JTable working in the Designer");
            task.setTimePackets(new ArrayList());
            tasks.add(task);
            task4 = task;
            task = new Task();
            task.setName("JDO to work as well as XMLEncoder");
            task.setTimePackets(new ArrayList());
            tasks.add(task);
            task5 = task;
            task = new Task();
            task.setName("Exception handling around obsfucation layer");
            task.setTimePackets(new ArrayList());
            tasks.add(task);
            task6 = task;
            task = new Task();
            task.setName("Business Plan");
            task.setTimePackets(new ArrayList());
            tasks.add(task);
            task7 = task;
        }
        proj1.setTasks(tasks);
        return tasks;
    }

    private static List getResourceData()
    {
        ArrayList resources = new ArrayList();
        {
            Resource resource;
            resource = new Resource();
            resource.setName("Chris Murphy");
            resource.setTimePackets(new ArrayList());
            resources.add(resource);
            res1 = resource;
            resource = new Resource();
            resource.setName("Hendrik Levsen");
            resource.setTimePackets(new ArrayList());
            resources.add(resource);
            res2 = resource;
            resource = new Resource();
            resource.setName("Sean Edmiston");
            resource.setTimePackets(new ArrayList());
            resources.add(resource);
            res3 = resource;
        }
        return resources;
    }

    private static List getTaskResourceData()
    {
        ArrayList trs = new ArrayList();
        {
            TaskResource tr;
            tr = new TaskResource();
            tr.setTask(task1);
            tr.setResource(res2);
            trs.add(tr);
            tr = new TaskResource();
            tr.setTask(task2);
            tr.setResource(res2);
            trs.add(tr);
            tr = new TaskResource();
            tr.setTask(task3);
            tr.setResource(res1);
            trs.add(tr);
            tr = new TaskResource();
            tr.setTask(task4);
            tr.setResource(res1);
            trs.add(tr);
            tr = new TaskResource();
            tr.setTask(task5);
            tr.setResource(res1);
            trs.add(tr);
            tr = new TaskResource();
            tr.setTask(task6);
            tr.setResource(res1);
            trs.add(tr);
            tr = new TaskResource();
            tr.setTask(task7);
            tr.setResource(res3);
            trs.add(tr);
        }
        return trs;
    }
}
