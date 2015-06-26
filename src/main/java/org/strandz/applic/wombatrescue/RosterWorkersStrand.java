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

import org.strandz.core.applichousing.SdzBag;
import org.strandz.core.applichousing.VisibleStrand;
import org.strandz.core.interf.Application;
import org.strandz.core.interf.Node;
import org.strandz.core.interf.SdzBagI;
import org.strandz.data.wombatrescue.domain.WombatDomainQueryEnum;
import org.strandz.lgpl.store.NotPersistedLookupsI;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.note.WombatNote;
import org.strandz.lgpl.store.DataStore;
import org.strandz.lgpl.store.DomainQueriesI;
import org.strandz.lgpl.store.EntityManagedDataStore;
import org.strandz.lgpl.store.LookupsI;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.NameUtils;
import org.strandz.view.wombatrescue.WorkerPanelWithTab;

import javax.swing.SwingUtilities;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Most of this file will be exactly the same no matter what
 * are running. Thus all in here apart from events stuff will
 * be read from a file that the user specifies in Context.
 * The shipping .java file will have only a main
 */
public class RosterWorkersStrand extends VisibleStrand
{
    private static String portableFilename = "dt-files/RosterWorkers_NEW_FORMAT.xml";
    public DomainQueriesI queriesI;
    public RosterWorkersTriggers volunteerTriggers;
    private RosterWorkersEvents volunteerSwing;
    private RosterWorkers_NEW_FORMATDT dt;
    private LookupsI wombatLookups;
    private String triggersTitle;
    private SelectDifferentStrandI selectStrandI;
    private static int timesConstructed;
    private static int times;
    private int id;
    static final String ALL_VOLS = "All Workers";
    /* Auto-commiting causing bugs so make simpler */
    private static final boolean AUTO_COMMIT = false;

    public RosterWorkersStrand()
    {
        init(null);
    }

    /**
     * Useful for testing, as can pass in a model that is made up of all non applichousing
     * attributes.
     *
     * @param a
     * @param portableFilename An alternative model
     */
    public RosterWorkersStrand(Application a, String portableFilename)
    {
        super(a);
        RosterWorkersStrand.portableFilename = portableFilename;
        init(a.getDataStore());
        chkLookups( (NotPersistedLookupsI)a.getDataStore().getLookups());
        this.wombatLookups = (LookupsI)a.getDataStore().getLookups();
        this.triggersTitle = WombatDomainQueryEnum.ROSTERABLE_WORKERS.getDescription(); 
    }
    
    public RosterWorkersStrand( String triggersTitle, Application a)
    {
        this( triggersTitle, a, (String)null, (SelectDifferentStrandI)null);
    }
    
    public RosterWorkersStrand(String triggersTitle, Application a 
                               ,SelectDifferentStrandI selectStrandI
    )
    {
        this( triggersTitle, a, (String)null, selectStrandI);
    }

    public RosterWorkersStrand(String triggersTitle, Application a, String portableFilename, 
                               SelectDifferentStrandI selectStrandI)
    {
        super(a);
        init( triggersTitle, a.getDataStore(), portableFilename, selectStrandI);
    }
    
    public RosterWorkersStrand(String triggersTitle, DataStore ds, String portableFilename)
    {
        init( triggersTitle, ds, portableFilename, (SelectDifferentStrandI)null);
    }
    
    private void init( String triggersTitle, DataStore ds, String portableFilename, 
                       SelectDifferentStrandI selectStrandI)
    {
        if(portableFilename != null)
        {
            RosterWorkersStrand.portableFilename = portableFilename;
        }
        else
        {
            //Stick with default
        }
        init( ds);
        chkLookups( (NotPersistedLookupsI)ds.getLookups());
        this.wombatLookups = (LookupsI)ds.getLookups();
        this.triggersTitle = triggersTitle;
        this.selectStrandI = selectStrandI;
    }
    
    private void chkLookups( NotPersistedLookupsI wombatLookups)
    {
        if(wombatLookups == null)
        {
            Err.error( "lookups == null");
        }
    }

    private void init(DataStore dataStore)
    {
        setDataStore(dataStore);
        queriesI = dataStore.getDomainQueries();
        timesConstructed++;
        id = timesConstructed;
    }

    private static class RunLater implements Runnable
    {
        RosterWorkersStrand obj;

        RunLater(RosterWorkersStrand obj)
        {
            this.obj = obj;
        }

        public void run()
        {
            obj.sdzSetup();
        }
    }

    /**
     * This sdzInit() will be called three times in the application. Once for
     * "Volunteers (rosterable only)"
     * Again for
     * "Unrosterable Volunteers"
     * Again for
     * "Group Workers"
     * These are VisibleStrandActions.
     */
    public void sdzInit()
    {
        /*
        times++;
        Err.pr( "sdzInit() being called " + times + " times");
        if(times == 0)
        {
        Err.stack();
        }
        */
        boolean callDirectly = true;
        Runnable runLater = new RunLater(this);
        try
        {
            if(SwingUtilities.isEventDispatchThread())
            {
                if(!callDirectly)
                {
                    // Doing in a seperate thread like this speeds up
                    // going to a blank screen. If this is not what want
                    // then call sdzSetup() directly.
                    SwingUtilities.invokeLater(runLater);
                }
                else
                {
                    // Advantage here is that when screen comes up it is already
                    // filled. Disadvantage is the wait for the new screen, and
                    // associated menu painting problems.
                    sdzSetup();
                }
            }
            else
            {
                // Called like this when running from a main. Doing any significant
                // gui work in the System thread always ends up with problems. Here we
                // are able toguarrantee that the code will run on the EventDispatchThread.
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

    private void sdzSetup()
    {
        Object obj = Utils.loadXMLFromResource(portableFilename, this, false);
        if(obj != null)
        {
            Err.pr(WombatNote.GENERIC, "Instance loaded is of type " + obj.getClass());
        }
        sbI = (SdzBagI) obj;
        if(sbI == null)
        {
            Err.error("Reading in not successful, from " + portableFilename);
        }
        if(!(sbI.getPane(0) instanceof WorkerPanelWithTab))
        {
            Err.error(
                "Expected WorkerPanel but got "
                    + sbI.getPane(0).getClass().getName());
        }

        SdzBag sdzBag = (SdzBag) sbI;
        dt = new RosterWorkers_NEW_FORMATDT(sdzBag);
        volunteerSwing = new RosterWorkersEvents( dt, this);
        Assert.notNull( triggersTitle, "No triggersTitle - specify in constructor");
        if(triggersTitle.equals( WombatDomainQueryEnum.ROSTERABLE_WORKERS.getDescription()))
        {
            volunteerTriggers = new RosterableWorkersTriggers(
                    sdzBag, dt, (EntityManagedDataStore) getDataStore(),
                    queriesI, volunteerSwing, (LookupsI)wombatLookups, this);
            Err.pr( SdzNote.BI_AI, triggersTitle + " is connected with strand with ID: " + sdzBag.getStrand().id);
        }
        else if(triggersTitle.equals( WombatDomainQueryEnum.UNROSTERABLE_WORKERS.getDescription()))
        {
            volunteerTriggers = new UnRosterableWorkersTriggers(sdzBag, dt, (EntityManagedDataStore) getDataStore(),
                queriesI, volunteerSwing, (LookupsI)wombatLookups, this);
            Err.pr( SdzNote.BI_AI, triggersTitle + " is connected with strand with ID: " + sdzBag.getStrand().id);
        }
        else if(triggersTitle.equals( WombatDomainQueryEnum.GROUP_WORKERS.getDescription()))
        {
            volunteerTriggers = new GroupWorkersTriggers(sdzBag, dt, (EntityManagedDataStore) getDataStore(),
                queriesI, volunteerSwing, (LookupsI)wombatLookups, this);
            Err.pr( SdzNote.BI_AI, triggersTitle + " is connected with strand with ID: " + sdzBag.getStrand().id);
        }
        else
        {
            Err.error( "Unknown triggers title: " + triggersTitle);
        }
        if(getApplication() != null)
        {
            initSdzBag(sdzBag);
            setPanelNodeTitle(sbI.getPane(0), dt.workerNode, "Worker");
            setPanelNodeTitle(sbI.getPane(1), dt.rosterSlotsListDetailNode,
                "Roster Slots");
        } 
    }

    public void disableToggle(boolean b)
    {
        volunteerSwing.disableToggle(b);
    }

    public boolean select(boolean b, String reason)
    {
        Err.pr( SdzNote.COMMIT_SELECT_DISPLAY, "select() arg " + b + " in " + NameUtils.tailOfPackage( this.getClass().getName()) + " b/c " + reason);
        boolean result = true;
        if(!b)
        {
            Err.pr(WombatNote.APPLICATION_COMMIT, "UN select() RosterWorkersStrand, to " + triggersTitle);
            //TODO - POST ought to be enough - need to pin value in cache
            //sbI.getStrand().POST();
            if(AUTO_COMMIT)
            {
                if(sbI.getStrand().hasChanged())
                {
                    Err.pr( SdzNote.ROSTERABILITY, "Changes detected in " + triggersTitle);
                    boolean unknownChanged = dt.unknownAttribute.hasChanged(); 
                    if(unknownChanged)
                    {
                        selectStrandI.unselectedAndChangedRosterability( triggersTitle);
                    }
                    else
                    {
                        sbI.getStrand().COMMIT_RELOAD();
                    }
                }
            }
            /*
            // post to the list so rostering will have it, see Bug for why COMMIT_RELOAD not POST
            if(!title.equals( triggersTitle))
            {
                if(triggersTitle.equals( WombatDomainQueryEnum.ROSTERABLE_WORKERS.getDescription()) ||
                        triggersTitle.equals( WombatDomainQueryEnum.UNROSTERABLE_WORKERS.getDescription()) ||
                        triggersTitle.equals( WombatDomainQueryEnum.GROUP_WORKERS.getDescription()))
                {
                    result = sbI.getStrand().COMMIT_RELOAD();
                }
                else
                {
                    result = sbI.getStrand().POST();
                }
            }
            */
        }
        return result;
    }

    public void display( boolean b)
    {
        Err.pr( SdzNote.COMMIT_SELECT_DISPLAY, "display() arg " + b + " in " + NameUtils.tailOfPackage( this.getClass().getName()));
        if(b)
        {
            Err.pr(SdzNote.TIGHTEN_RECORD_VALIDATION, "### display() RosterWorkersStrand");
            // (String)vsa.getValue( AbstractAction.NAME)
            volunteerTriggers.setTitle(triggersTitle);
            Node firstNode = dt.strand.getNodes().get(0);
            if(firstNode != dt.strand.getCurrentNode())
            {
                Err.pr(SdzNote.NOT_KEEPING_PLACE, "First Node: " + firstNode);
                Err.pr(SdzNote.NOT_KEEPING_PLACE, "Current Node: " + dt.strand.getCurrentNode());
                boolean ok = firstNode.GOTO();
                if(!ok)
                {
                    Err.error("Had a problem going to node " + firstNode + " Problem: " +
                        dt.strand.getErrorMessage() + " times " + times);
                }
            }
            dt.strand.EXECUTE_QUERY();
        }
        super.display(b);
    }

    public void preForm()
    {
        volunteerTriggers.preForm();
    }

    /*
    * These 2 s/not stay in this to be generated file
    */
    public List getRosterSlotAbilities()
    {
        return volunteerTriggers.rosterSlotAbilities;
    }

    public List getVolunteerAbilities()
    {
        return volunteerTriggers.workerAbilities;
    }

    public RosterWorkers_NEW_FORMATDT getDt()
    {
        return dt;
    }

    public void setDt(RosterWorkers_NEW_FORMATDT dt)
    {
        this.dt = dt;
    }    
}
