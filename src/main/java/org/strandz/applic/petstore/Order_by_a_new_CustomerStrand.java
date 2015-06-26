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
package org.strandz.applic.petstore;

import org.strandz.core.interf.Application;
import org.strandz.core.interf.SdzBagI;
import org.strandz.core.applichousing.VisibleStrand;
import org.strandz.core.applichousing.SdzBag;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Utils;
import org.strandz.store.petstore.PetstoreApplicationData;
import org.strandz.store.petstore.PetstoreQueries;

import javax.swing.SwingUtilities;
import java.lang.reflect.InvocationTargetException;

public class Order_by_a_new_CustomerStrand extends VisibleStrand
{
    private static String filename = "C:\\cml\\dt\\petstore\\PlayPet.xml";
    public PetstoreQueries queries;
    //public SdzBagI sbI;
    public Order_by_a_new_CustomerTriggers triggers;
    private Order_by_a_new_CustomerDT dt;

    public Order_by_a_new_CustomerStrand()
    {
        init();
    }

    public Order_by_a_new_CustomerStrand(Application a)
    {
        super(a);
        init();
    }

    private void init()
    {
        setDataStore(PetstoreApplicationData.getSetInstance().getData());
        queries = PetstoreApplicationData.getSetInstance().getQueries();
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
        Order_by_a_new_CustomerStrand obj;

        RunLater(Order_by_a_new_CustomerStrand obj)
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
            Err.error(e, "Check the 'Caused by:' exception");
        }
    }

    public void sdzSetup()
    {
        sbI = (SdzBagI) Utils.loadXMLFromResource(filename, this, false);
        if(sbI == null)
        {
            Err.error("Reading in not successful, from " + filename);
        }
        dt = new Order_by_a_new_CustomerDT(sbI);
        triggers = new Order_by_a_new_CustomerTriggers(getDataStore(), queries, dt, sbI);
        if(getApplication() != null)
        {
            initSdzBag((SdzBag) sbI);
            setPanelNodeTitle(sbI.getPane(0), sbI.getNode(0),
                sbI.getNode(0).getDisplayName());
            setPanelNodeTitle(sbI.getPane(1), sbI.getNode(1),
                sbI.getNode(1).getDisplayName());
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
