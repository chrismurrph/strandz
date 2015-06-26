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
package org.strandz.applic.wombatrescue;

import org.strandz.core.interf.Application;
import org.strandz.core.interf.SdzBagI;
import org.strandz.core.applichousing.VisibleStrand;
import org.strandz.lgpl.note.WombatNote;
import org.strandz.lgpl.store.DataStore;
import org.strandz.lgpl.store.DomainQueriesI;
import org.strandz.lgpl.store.EntityManagedDataStore;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Utils;

import javax.swing.SwingUtilities;
import java.lang.reflect.InvocationTargetException;

public class BuddyManagerStrand extends VisibleStrand
{
    public DomainQueriesI queriesI;
    //public SdzBagI sbI;
    public BuddyManagerTriggers triggers;
    private BuddyManagerDT dt;
    
    /* Auto-commiting causing bugs so make simpler */
    private static final boolean AUTO_COMMIT = false;
    /**
     * Only being used when WombatrescueApplicationData.portable == false, which
     * don't tend to use anymore.
     */
    private static final String FILENAME = "C:\\sdz-zone\\dt\\wombatrescue\\BuddyManagers_NEW_FORMAT.xml";
    /**
     * Used when WombatrescueApplicationData.portable == true. In this case this
     * file will need to be in a jar. For this reason the 'deploy' button must
     * have been pressed.
     */
    private static String portableFilename = "dt-files/BuddyManagers_NEW_FORMAT.xml";


    public BuddyManagerStrand(DataStore ds)
    {
        init(ds);
    }

    public BuddyManagerStrand(Application a, DataStore ds)
    {
        super(a);
        init(ds);
    }

    private void init(DataStore ds)
    {
        //DataStoreFactory factory = new DataStoreFactory( true);
        setDataStore(ds);
        queriesI = (DomainQueriesI) ds.getDomainQueries();
    }

    public void display(boolean b)
    {
        if(!b)
        {
            if(AUTO_COMMIT) //Fact that have twice (!select and !display) is SO wrong
            {
                //dt.shiftManagerNode.GOTO();
                Err.pr(WombatNote.GENERIC, "### UN display() BuddyManagerStrand");
                // COMMIT_ONLY stuffs up the data
                dt.strand.COMMIT_RELOAD();
            }
        }
        else
        {
            if(dt.strand.getCurrentNode() != dt.shiftManagerNode)
            {
                dt.shiftManagerNode.GOTO();
                dt.strand.EXECUTE_QUERY();
            }
            Err.pr(WombatNote.GENERIC, "### display() BuddyManagerStrand");
        }
        super.display(b);
    }

    private static class RunLater implements Runnable
    {
        BuddyManagerStrand obj;

        RunLater(BuddyManagerStrand obj)
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
        //sbI = (SdzBagI)pUtils.loadXML( filename, portableFilename, this, false);
        sbI = (SdzBagI) Utils.loadXMLFromResource(portableFilename, this, false);
        if(sbI == null)
        {
            Err.error("Reading in not successful, from " + portableFilename);
        }
        dt = new BuddyManagerDT(sbI);
        triggers = new BuddyManagerTriggers((EntityManagedDataStore) getDataStore(), queriesI, dt, sbI);
        if(getApplication() != null)
        {
            initSdzBag( sbI);
            setPanelNodeTitle(dt.ui0, dt.shiftManagerNode, "Shift Managers");
        }
        else
        {
            Err.error( NEED_APPLICATION_MSG);
        }
    }

    public boolean select(boolean b, String reason)
    {
        boolean result = true;
        if(!b)
        {
            if(AUTO_COMMIT)
            {
                Err.pr(WombatNote.APPLICATION_COMMIT, "UN select() BuddyManagerStrand");
                result = sbI.getStrand().COMMIT_RELOAD();
            }
        }
        return result;
    }

    public void preForm()
    {
    }

}
