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
package org.strandz.applic.timesheet2;

import org.strandz.core.interf.Application;
import org.strandz.core.interf.SdzBagI;
import org.strandz.core.applichousing.VisibleStrand;
import org.strandz.core.applichousing.SdzBag;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Utils;
import org.strandz.store.timesheet.TimesheetApplicationData;
import org.strandz.store.timesheet.TimesheetQueries;

import javax.swing.SwingUtilities;
import java.lang.reflect.InvocationTargetException;

public class TimesheetStrand extends VisibleStrand
{
    private static String filename = "C:\\cml\\dt\\timesheet2\\MaintainTimesheet.xml";
    public TimesheetQueries queries;
    //public SdzBagI sbI;
    public TimesheetTriggers triggers;
    private TimesheetDT dt;

    public TimesheetStrand()
    {
        init();
    }

    public TimesheetStrand(Application a)
    {
        super(a);
        init();
    }

    private void init()
    {
        setDataStore(TimesheetApplicationData.getInstance().getData());
        queries = TimesheetApplicationData.getInstance().getQueries();
    }

    public boolean select(boolean b, String reason)
    {
        boolean result = true;
        if(!b)
        {
            result = sbI.getStrand().POST();
        }
        return result;
    }

    public void display(boolean b)
    {
        if(!b)
        {
            //done above now
            //sbI.getStrand().POST();
        }
        else
        {
            // this.title = title;
            // triggers.setTitle( title);
            if(!firstTime)
            {
                dt.strand.COMMIT_RELOAD();
            }
            else
            {
                dt.strand.EXECUTE_QUERY();
                firstTime = false;
            }
        }
        super.display(b);
    }

    private static class RunLater implements Runnable
    {
        TimesheetStrand obj;

        RunLater(TimesheetStrand obj)
        {
            this.obj = obj;
        }

        public void run()
        {
            obj.sdzSetup();
        }
    }

    public void sdzInit()
    {
        boolean callDirectly = false;
        Runnable runLater = new RunLater(this);
        try
        {
            if(SwingUtilities.isEventDispatchThread())
            {
                if(!callDirectly)
                {
                    SwingUtilities.invokeLater(runLater);
                }
                else
                {
                    sdzSetup();
                    preForm();
                }
            }
            else
            {
                SwingUtilities.invokeAndWait(runLater);
            }
        }
        catch(InterruptedException e)
        {
            Err.error(e);
        }
        catch(InvocationTargetException e)
        {
            Err.error(e);
        }
    }

    public void sdzSetup()
    {
        sbI = (SdzBagI) Utils.loadXMLFromResource(filename, this, false);
        if(sbI == null)
        {
            Err.error("Reading in not successful, from " + filename);
        }
        dt = new TimesheetDT(sbI);
        triggers = new TimesheetTriggers(getDataStore(), queries, dt, sbI);
        if(getApplication() != null)
        {
            initSdzBag((SdzBag) sbI);
            setPanelNodeTitle(dt.ui0, dt.timesheetNode, "timesheetNode");
        }
        else
        {
            Err.error(
                "Not being part of an application s/be" + " becoming impossible");
        }
    }

    public void preForm()
    {
    }

}
