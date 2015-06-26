package org.strandz.applic.supersix;

import org.strandz.core.interf.Application;
import org.strandz.core.interf.Node;
import org.strandz.core.interf.SdzBagI;
import org.strandz.core.applichousing.VisibleStrand;
import org.strandz.core.applichousing.SdzBag;
import org.strandz.data.supersix.objects.SuperSixLookups;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Utils;

import javax.swing.SwingUtilities;
import java.lang.reflect.InvocationTargetException;

public class MaintainGlobalStrand
        extends VisibleStrand
{

    //private static final String FILENAME = "C:\\sdz-zone\\dt-files\\supersix\\maintainGlobal.xml";
    private static String portableFilename = "dt-files/maintainGlobal.xml";
    //public SdzBagI sbI;
    public MaintainGlobalTriggers triggers;
    private MaintainGlobalDT dt;
    private MaintainGlobalEvents events;
    private SuperSixSdzManager superSixSdzManager;
    private static int timesConstructed;
    private int id;

    public MaintainGlobalStrand(Application a, SuperSixSdzManager superSixSdzManager)
    {
        super(a);
        setDataStore(a.getDataStore());
        this.superSixSdzManager = superSixSdzManager;
        timesConstructed++;
        id = timesConstructed;
    }
    
    public String toString()
    {
        return this.getClass().getName() + ", id " + id;
    }
    
    public void display(boolean b)
    {
        if(b)
        {
            Node firstNode = (Node) dt.strand.getNodes().get(0);
            if(firstNode != dt.strand.getCurrentNode())
            {
                boolean ok = firstNode.GOTO();
                if(!ok)
                {
                    Err.error("Had a problem going to node " + firstNode + " Problem: " +
                            dt.strand.getErrorMessage());
                }
                dt.strand.EXECUTE_QUERY();
            }
        }
        super.display(b);
    }

    public boolean select(boolean b, String reason)
    {
        boolean result = true;
        if(!b)
        {
            sbI.getStrand().POST();
        }
        return result;
    }

    private static class RunLater implements Runnable
    {
        MaintainGlobalStrand obj;

        RunLater(MaintainGlobalStrand obj)
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
        sbI = (SdzBagI) Utils.loadXMLFromResource(portableFilename, this, false);
        if(sbI == null)
        {
            Err.error("Reading in not successful, from " + portableFilename);
        }
        dt = new MaintainGlobalDT(sbI);
        events = new MaintainGlobalEvents(dt, superSixSdzManager, getApplication());
        triggers = new MaintainGlobalTriggers(getDataStore(), dt, sbI, (SuperSixLookups)getDataStore().getLookups());
        if(getApplication() != null)
        {
            initActionable((SdzBag) sbI);
            setPanelNodeTitle(sbI.getPane(0), sbI.getNode(0), sbI.getNode(0).getDisplayName());
        }
        else
        {
            Err.error("Not being part of an application s/be" +
                    " becoming impossible");
        }
    }

    public void preForm()
    {
    }
    
    public void initActionable(SdzBag sbI)
    {
        super.initSdzBag( sbI);
        //Err.pr( "Done initActionable for " + this + " with " + sbI);
    }
}