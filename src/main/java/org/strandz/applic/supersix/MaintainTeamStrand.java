package org.strandz.applic.supersix;

import org.strandz.core.interf.Application;
import org.strandz.core.interf.Node;
import org.strandz.core.interf.SdzBagI;
import org.strandz.core.applichousing.VisibleStrand;
import org.strandz.core.applichousing.SdzBag;
import org.strandz.data.supersix.objects.Division;
import org.strandz.data.supersix.objects.SuperSixLookups;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Utils;

import javax.swing.SwingUtilities;
import java.lang.reflect.InvocationTargetException;

public class MaintainTeamStrand
        extends VisibleStrand
{
    //private static final String FILENAME = "C:\\sdz-zone\\dt-files\\supersix\\maintainTeam.xml";
    private static String portableFilename = "dt-files/maintainTeam.xml";
    //public SdzBagI sbI;
    public MaintainTeamTriggers triggers;
    public MaintainTeamEvents events;
    MaintainTeamDT dt;
    private SuperSixSdzManager superSixSdzManager;
    private Division division;

    public MaintainTeamStrand(Application a, SuperSixSdzManager superSixSdzManager, Division division)
    {
        super(a);
        setDataStore(a.getDataStore());
        this.superSixSdzManager = superSixSdzManager;
        this.division = division;
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
        MaintainTeamStrand obj;

        RunLater(MaintainTeamStrand obj)
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
        dt = new MaintainTeamDT(sbI);
        superSixSdzManager.setMaintainTeamDT( dt);
        AgeCalculator ageCalculator = new AgeCalculator( dt.dateOfBirthAttribute, dt.ui1.getLAge());
        events = new MaintainTeamEvents(dt, ageCalculator);
        SuperSixLookups superSixLookups = (SuperSixLookups)getDataStore().getLookups();
        triggers = new MaintainTeamTriggers(getDataStore(), dt, sbI, ageCalculator, division, superSixLookups);
        if(getApplication() != null)
        {
            initSdzBag((SdzBag) sbI);
            setPanelNodeTitle(sbI.getPane(0), sbI.getNode(0), sbI.getNode(0).getDisplayName());
            setPanelNodeTitle(sbI.getPane(1), sbI.getNode(1), sbI.getNode(1).getDisplayName());
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

    public void postForm()
    {
    }
}