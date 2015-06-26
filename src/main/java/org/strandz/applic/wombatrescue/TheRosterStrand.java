package org.strandz.applic.wombatrescue;

import org.strandz.core.applichousing.VisibleStrand;
import org.strandz.core.interf.Application;
import org.strandz.core.interf.ApplicationHelper;
import org.strandz.core.interf.Node;
import org.strandz.core.interf.SdzBagI;
import org.strandz.data.wombatrescue.business.ParticularRosterI;
import org.strandz.data.wombatrescue.business.RostererSessionI;
import org.strandz.lgpl.note.WombatNote;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Utils;

import javax.swing.SwingUtilities;
import java.lang.reflect.InvocationTargetException;

public class TheRosterStrand extends VisibleStrand
{
    private static String portableFilename = "dt-files/TheRoster.xml";
    //private static final String FILENAME = "C:\\sdz-zone\\dt-files\\wombatrescue\\TheRoster.xml";
    //public SdzBagI sbI;
    public TheRosterTriggers triggers;
    public TheRosterEvents events;
    private TheRosterDT dt;
    private RosterWorkersStrand rosterWorkersStrand;
    private ApplicationHelper applicationHelper;
    private ParticularRosterI particularRoster;
    private RostererSessionI rostererSession;
    private boolean debug;

    public TheRosterStrand( Application a, RosterWorkersStrand visibleStrand, boolean debug)
    {
        super( a);
        setDataStore( a.getDataStore());
        this.rosterWorkersStrand = visibleStrand;
        this.applicationHelper = a.getApplicationHelper();
        this.debug = debug;
    }
    
    public void setBusinessObjects(ParticularRosterI particularRoster, RostererSessionI rostererSession)
    {
        Assert.notNull( particularRoster, "Need a particularRoster");
        Assert.notNull( rostererSession, "Need a rostererSession");
        this.particularRoster = particularRoster;
        this.rostererSession = rostererSession;
    }

    /* Always gen roster on select
    public void display( boolean b)
    {
        if(b)
        {
            exeQry();
        }
        super.display(b);
    }
    */
    
    private void exeQry()
    {
        Node firstNode = dt.strand.getNodes().get(0);
        if(firstNode != dt.strand.getCurrentNode())
        {
            boolean ok = firstNode.GOTO();
            if(!ok)
            {
                Err.error( "Had a problem going to node " + firstNode + " Problem: " +
                    dt.strand.getErrorMessage());
            }
        }
        dt.strand.EXECUTE_QUERY();
    }

    public boolean select( boolean b, String reason)
    {
        //Err.pr( "select " + b + " in " + NameUtils.tailOfPackage( this.getClass().getName()) + " b/c " + reason);
        boolean result = true;
        if(!b)
        {
            if(sbI.getStrand().hasChanged())
            {
                //sbI.getStrand().POST();
                Err.pr( "\tWhat need to do when un-select a roster? A: Nufin (but weird strand that is RO has changed at all)");
            }
        }
        else
        {
            exeQry();
        }
        return result;
    }

    private static class RunLater implements Runnable
    {
        TheRosterStrand obj;

        RunLater( TheRosterStrand obj)
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
        Runnable runLater = new RunLater( this);
        try
        {
            if(SwingUtilities.isEventDispatchThread())
            {
                if(!callDirectly)
                {
                    SwingUtilities.invokeLater( runLater);
                }
                else
                {
                    sdzSetup();
                    preForm();
                }
            }
            else
            {
                SwingUtilities.invokeAndWait( runLater);
            }
        }
        catch(InterruptedException e) {
            Err.error( e);
        }
        catch(InvocationTargetException e) {
            Err.error( e, "Check the 'Caused by:' exception");
        }
    }

    public void sdzSetup()
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
        /*
        sbI = (SdzBagI)Utils.loadXMLFromFile( FILENAME, true);
        if(sbI == null)
        {
            Err.error( "Reading in not successful, from " + FILENAME);
        }
        */
        dt = new TheRosterDT( sbI);
        events = new TheRosterEvents( dt, rosterWorkersStrand, this, applicationHelper);
        triggers = new TheRosterTriggers( getDataStore(), dt, sbI, debug);
        Assert.notNull( particularRoster, "Need to call setBusinessObjects() as no particularRoster");
        Assert.notNull( rostererSession, "Need to call setBusinessObjects() as no rostererSession");
        triggers.setBusinessObjects( particularRoster, rostererSession);
        if(getApplication() != null)
        {
            initSdzBag( sbI);
            setPanelNodeTitle( sbI.getPane( 0), sbI.getNode( 0), sbI.getNode( 0).getDisplayName());
        }
        else
        {
            Err.error( NEED_APPLICATION_MSG);
        }
    }
    
    public TheRosterDT getDt()
    {
        return dt;
    }

    public void preForm()
    {
    }

    public void postForm()
    {
    }
}