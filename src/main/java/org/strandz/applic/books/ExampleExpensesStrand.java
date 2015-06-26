package org.strandz.applic.books;

import org.strandz.core.interf.Application;
import org.strandz.core.interf.Node;
import org.strandz.core.interf.SdzBagI;
import org.strandz.core.applichousing.VisibleStrand;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Utils;

import javax.swing.SwingUtilities;
import java.lang.reflect.InvocationTargetException;

public class ExampleExpensesStrand
        extends VisibleStrand
{

    private static String filename = "C:\\sdz-zone\\dt-files\\books\\exampleExpenses.xml";
    //public SdzBagI sbI;
    public ExampleExpensesTriggers triggers;
    private ExampleExpensesDT dt;

    public ExampleExpensesStrand(Application a)
    {
        super(a);
        setDataStore(a.getDataStore());
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
        ExampleExpensesStrand obj;

        RunLater(ExampleExpensesStrand obj)
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
        sbI = (SdzBagI) Utils.loadXMLFromFile(filename, true);
        if(sbI == null)
        {
            Err.error("Reading in not successful, from " + filename);
        }
        dt = new ExampleExpensesDT(sbI);
        triggers = new ExampleExpensesTriggers(getDataStore(), /*queriesI,*/ dt, sbI);
        if(getApplication() != null)
        {
            initSdzBag( sbI);
            setPanelNodeTitle(sbI.getPane(0), sbI.getNode(0), sbI.getNode(0).getDisplayName());
        }
        else
        {
            Err.error( NEED_APPLICATION_MSG);
        }
    }

    public void preForm()
    {
    }

    public void postForm()
    {
    }
}