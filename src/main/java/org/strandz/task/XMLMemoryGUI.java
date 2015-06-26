package org.strandz.task;

import info.clearthought.layout.TableLayoutConstraints;
import org.strandz.core.interf.SdzBagI;
import org.strandz.core.applichousing.SdzBag;
import org.strandz.lgpl.util.BeansUtils;
import org.strandz.lgpl.util.DisplayUtils;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.ErrorLogger;
import org.strandz.lgpl.util.SelfReferenceUtils;
import org.strandz.lgpl.util.SpecialClassLoader;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.widgets.DateSelectControl;
import org.strandz.lgpl.widgets.PortableImageIcon;
import org.strandz.lgpl.tablelayout.ConstraintsPersistenceDelegate;
import org.strandz.lgpl.tablelayout.ModernTableLayout;
import org.strandz.lgpl.tablelayout.TableLayoutPersistenceDelegate;
import org.strandz.task.wombatrescue.WorkerAppHelper;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.DefaultPersistenceDelegate;
import java.beans.Encoder;
import java.beans.Expression;
import java.beans.PersistenceDelegate;
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * To use this class create and call a method called popXXX(). This way you
 * can keep a history, and call it again later. This class is all about going
 * to and from an XMLEncoding of an object structure. Use it to test new
 * components that are to be used by Strandz to make sure that they will work
 * with the Designer.
 * <p/>
 * If some of these popXXX() methods do not work here then they belong in
 * a subclass called com.seasoft.test.DsgnrXMLMemoryGUI. This subclass was
 * created so that the Strandz source would all compile without requiring
 * fancy jars, such as TableLayoutPersistenceDelegate.jar. Also DsgnrXMLMemoryGUI
 * is most useful for people who are working with the Designer.
 */
public class XMLMemoryGUI extends JPanel implements ActionListener
{
    private static final String CLASS_INTO_MEM = ".class into Mem";
    private static final String MEM_INTO_XML = "Mem into XML";
    private static final String XML_INTO_MEM = "XML into Mem";
    private static final String SHOW_MEM = "Show Mem";
    private static final String DISPLAY = "Display";
    protected static final String OPERATION = "Operation";
    private String toLoad0;
    private String toLoad1;
    public static final boolean useLoader = true;
    JButton button1;
    JButton button2;
    JButton button3;
    JButton button4;
    JButton button5;
    JButton button6;
    boolean twoPanes = false;
    JPanel p0;
    JPanel p1;
    protected SdzBagI sbI;
    protected String filename_IN;
    protected String filename_OUT;
    private BufferedOutputStream out = null;
    private SpecialClassLoader scl;

    private static String baseDir = "C:\\dev\\classes";
    private static String specialPackage = "org.strandz.view.wombatrescue";
    
    public static void main(String s[])
    {
        main();
    }

    public static void main()
    {
        JFrame frame = new JFrame("XML Memory GUI");
        frame.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                System.exit(0);
            }
        });
        frame.setContentPane(new XMLMemoryGUI());
        frame.pack();
        frame.setVisible(true);
    }
    

    protected void populate()
    {
        //popRosterWorkersNV();
        //popTheRoster();
        //popMetric();
        popMapDetailsPanel();
        //popMapDetailsExpanderTabbedPane();
        //popMapDetailsExpanderControl();
        //popManyItemsControl();
    }

    /**
     * Must have been testing in from xml, then out to a different
     * xml file, to see what was changing.
     * Also incorporates everything prior to reorganisation.
     */
    private void popRosterVolunteers()
    {
        filename_IN = // "C:\\sdz-zone\\dt\\wombatrescue\\ActualPanel.xml";
            // "C:\\sdz-zone\\dt\\wombatrescue\\TestPanels.xml";
            "C:\\sdz-zone\\dt-files\\wombatrescue\\RosterVolunteers_DEBUG.xml";
        filename_OUT = "C:\\sdz-zone\\dt-files\\wombatrescue\\RosterVolunteers_gui.xml";
        toLoad0 = null;
        toLoad1 = null;
        // private String toLoad0 = "org.strandz.view.wombatrescue.VolunteerPanel";
        // private String toLoad1 = "org.strandz.view.wombatrescue.RosterSlotPanel";
    }

    /**
     * Only want to test reading class into mem and writing out to XML
     */
    private void popDateSelectPanel()
    {
        toLoad0 = "org.strandz.widgets.DateSelectControl";
        filename_OUT = "C:\\sdz-zone\\dt-files\\wombatrescue\\DateSelectPanel_out2.xml";
        filename_IN = filename_OUT;
    }

    /**
     * Only want to test reading class into mem and writing out to XML
     */
    private void popVolunteerPanel()
    {
        toLoad0 = "org.strandz.view.wombatrescue.NarrowWorkerPanel";
        filename_OUT = "C:\\sdz-zone\\dt-files\\wombatrescue\\NarrowWorkerPanel_debug.xml";
        filename_IN = null;
    }
    
    /**
     * Only want to test reading class into mem and writing out to XML
     */
    private void popPlayerPanel()
    {
        toLoad0 = "org.strandz.view.supersix.PlayerPanel";
        filename_OUT = "C:\\temp\\PlayerPanel_debug.xml";
        filename_IN = null;
    }

    /**
     * Only want to test reading class into mem and writing out to XML
     */
    private void popSeasonPanel()
    {
        toLoad0 = "org.strandz.view.supersix.SeasonPanel";
        filename_OUT = "C:\\temp\\SeasonPanel_debug.xml";
        filename_IN = null;
    }
    
    /**
     * Only want to test reading class into mem and writing out to XML
     */
    private void popWorkerApp()
    {
        WorkerAppHelper helper = new WorkerAppHelper();
        helper.sdzSetup(); //TODO Make sure this method signature is used everywhere
        sbI = new SdzBag();
        sbI.setNode(0, helper.getWorkerNode());
        sbI.setPane(0, helper.getPanel());
        toLoad0 = null;
        filename_OUT = "C:\\sdz-zone\\dt-files\\wombatrescue\\experiment.xml";
        filename_IN = null;
    }

    private void popBuddyManagers()
    {
        filename_IN = "C:\\sdz-zone\\dt-files\\wombatrescue\\BuddyManagers_NEW_FORMAT_in.xml";
        filename_OUT = "C:\\sdz-zone\\dt-files\\wombatrescue\\BuddyManagers_NEW_FORMAT_out.xml";
    }

    private void popTheRoster()
    {
        filename_IN = "C:\\sdz-zone\\dt-files\\wombatrescue\\TheRoster.xml";
        filename_OUT = "C:\\sdz-zone\\dt-files\\wombatrescue\\TheRoster.xml";
    }

    private void popContextData()
    {
        filename_IN = "C:\\sdz-zone\\data\\sdzdsgnr\\ContextData.xml";
    }

    /**
     * Only want to test reading XML into memory
     */
    private void popDebugFault()
    {
        BeansUtils.setDesignTime(true);
        filename_OUT = "C:\\sdz-zone\\dt-files\\fault\\DebugFault.xml";
        filename_IN = filename_OUT;
    }
    
    private void popRosterWorkersNV()
    {
        //filename_IN = "C:\\sdz-zone\\dt-files\\wombatrescue\\RosterWorkers_NON_VISUAL.xml";
        filename_IN = "C:\\temp\\RosterWorkers_NON_VISUAL.xml";
    }
    
    public void popMetric()
    {
        //toLoad0 = "com.seasoft.view.prodkpi.MetricPanel";
        filename_IN = "C:\\sdz-zone\\dt-files\\prodkpi\\Metric.xml";
        //filename_OUT = "C:\\sdz-zone\\dt-files\\prodkpi\\ManyItemsControl.xml";
    }

    private void popMapDetailsPanel()
    {
        toLoad0 = "com.seasoft.view.prodkpi.MapDetailsPanel";
        filename_IN = "C:\\temp\\MapDetails_in.xml";
        filename_OUT = "C:\\temp\\MapDetails_out.xml";
    }

    private void popMapDetailsExpanderTabbedPane()
    {
        toLoad0 = "com.seasoft.view.prodkpi.MapDetailsExpanderTabbedPane";
        filename_IN = "C:\\temp\\MapDetailsExpanderTabbedPane_in.xml";
        filename_OUT = "C:\\temp\\MapDetailsExpanderTabbedPane_out.xml";
    }

    private void popMapDetailsExpanderControl()
    {
        toLoad0 = "com.seasoft.widgets.prodkpi.MapDetailsExpanderControl";
        filename_IN = "C:\\temp\\MapDetailsExpanderControl_in.xml";
        filename_OUT = "C:\\temp\\MapDetailsExpanderControl_out.xml";
    }

    private void popManyItemsControl()
    {
        toLoad0 = "org.strandz.lgpl.widgets.ManyItemsControl";
        filename_IN = "C:\\temp\\ManyItemsControl_inout.xml";
        filename_OUT = filename_IN;
    }

    public XMLMemoryGUI()
    {
        populate();
        button1 = new JButton();
        button1.setText(CLASS_INTO_MEM);
        button1.addActionListener(this);
        button2 = new JButton();
        button2.setText(MEM_INTO_XML);
        button2.addActionListener(this);
        button3 = new JButton();
        button3.setText(XML_INTO_MEM);
        button3.addActionListener(this);
        button4 = new JButton();
        button4.setText(SHOW_MEM);
        button4.addActionListener(this);
        button5 = new JButton();
        button5.setText(DISPLAY);
        button5.addActionListener(this);
        button6 = new JButton();
        button6.setText(OPERATION);
        button6.addActionListener(this);
        setLayout(new FlowLayout());
        add(button1);
        add(button2);
        add(button3);
        add(button4);
        add(button5);
        add(button6);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }

    private static void displayLocation(JPanel panel)
    {
        if(panel instanceof DateSelectControl)
        {
            DateSelectControl dsControl = (DateSelectControl) panel;
            Err.pr("DateSelectControl called " + dsControl.getName());
            JButton button = dsControl.getBSelectDate();
            Err.pr("button called " + button.getName());
            Icon icon = button.getIcon();
            Err.pr("icon is of type " + icon.getClass().getName());
            if(icon instanceof PortableImageIcon)
            {
                PortableImageIcon piIcon = (PortableImageIcon) icon;
                Err.pr("PortableImageIcon has location " + piIcon.getLocation());
            }
        }
        else
        {
            Err.pr("panel have read in is a " + panel.getClass().getName());
        }
    }

    public void actionPerformed(ActionEvent e)
    {
        if(e.getActionCommand().equals(DISPLAY))
        {
            if(p0 != null)
            {
                DisplayUtils.displayInDialog(p0);
            }
            else
            {
                Err.pr( "No panel available to be displayed");
            }
        }
        else if(e.getActionCommand().equals(CLASS_INTO_MEM))
        {
            if(toLoad0 == null)
            {
                Err.pr("No loadable panel has been specified, just save and check out " + filename_OUT);
            }
            else
            {
                if(sbI == null)
                {
                    sbI = new SdzBag();
                }
                createLoaderForThread(true);
                scl.setCurrentlyLoading(CLASS_INTO_MEM);
                p0 = (JPanel) loadClass(toLoad0);
                sbI.setPane(0, p0);
                if(twoPanes)
                {
                    p1 = (JPanel) loadClass(toLoad1);
                    sbI.setPane(1, p1);
                }
                scl.setCurrentlyLoading(null);
                displayLocation(p0);
            }
        }
        else if(e.getActionCommand().equals(MEM_INTO_XML))
        {
            createLoaderForThread(false);
            // scl.setCurrentlyLoading( true);
            writeOutFile();
            // scl.setCurrentlyLoading( false);
        }
        else if(e.getActionCommand().equals(XML_INTO_MEM))
        {
            createLoaderForThread(true);
            if(filename_IN != null)
            {
                // scl.setCurrentlyLoading( true);
                sbI = (SdzBagI) Utils.loadXMLFromFile(filename_IN, false);
                // scl.setCurrentlyLoading( false);
                p0 = (JPanel) sbI.getPane(0);
                if(twoPanes)
                {
                    p1 = (JPanel) sbI.getPane(1);
                }
                displayLocation(p0);
            }
            else
            {
                Err.pr( "No filename_IN, so can't load it");
            }
        }
        else if(e.getActionCommand().equals(SHOW_MEM))
        {
            if(p0 == null)
            {
                Err.pr("Not loaded anything yet");
            }
            else
            {
                Err.pr("Panel 0 Type: " + p0.getClass().getName());
                Err.pr("Panel 0 Name: " + p0.getName());
                if(p1 == null)
                {
                    Err.pr("p1 is null, so no Panel 1 Type/Name available");
                }
                else
                {
                    Err.pr("Panel 1 Type: " + p1.getClass().getName());
                    Err.pr("Panel 1 Name: " + p1.getName());
                    Err.pr("");
                }
            }
        }
    }

    public static class QuickLogger implements ErrorLogger
    {
        public void log(String msg)
        {
            log(msg, false);
        }

        public void log(String s, boolean b)
        {
            Err.pr(s);
        }
    }

    /**
     * Loads a panel, instantiates it, and calls init on it.
     */
    public Object loadClass(String toLoad)
    {
        Class clazz = null;
        String txt = null;
        try
        {
            if(useLoader)
            {
                scl.setForce(true, toLoad);
                clazz = scl.loadClass(toLoad);
                scl.setForce(false, null);
                txt = "loaded";
            }
            else
            {
                clazz = Class.forName(toLoad);
                txt = "instantiated only";
            }
        }
        catch(ClassNotFoundException e)
        {
            throw new Error(e);
        }

        JPanel obj = (JPanel) factory(clazz, "init");
        System.out.println(
            "Have " + txt + " a " + obj.getClass().getName() + " called "
                + obj.getName());
        return obj;
    }

    private void createLoaderForThread(boolean force)
    {
        if(force || scl == null)
        {
            scl = new SpecialClassLoader(this.getClass().getClassLoader(), baseDir,
                specialPackage, false);
            if(useLoader)
            {
                Thread.currentThread().setContextClassLoader(scl);
            }
        }
    }

    /**
     * Creates an XML file out of the sbI we have in memory.
     */
    private void writeOutFile()
    {
        BeansUtils.setDesignTime(true);
        try
        {
            out = new BufferedOutputStream(new FileOutputStream(filename_OUT));
        }
        catch(FileNotFoundException ex)
        {
            throw new Error(ex);
        }

        XMLEncoder encoder = new XMLEncoder(out);
        setPersistenceDelegates(encoder);
        encoder.writeObject(sbI);
        encoder.close();
        System.out.println(filename_OUT + " has been encoded");
        BeansUtils.setDesignTime(false);
    }

    protected void setPersistenceDelegates(XMLEncoder encoder)
    {
        /*
        encoder.setPersistenceDelegate(TableLayoutConstraints.class,
            new DefaultPersistenceDelegate(new String[]
                {"col1", "row1", "col2", "row2", "hAlign", "vAlign"}));
        */
        encoder.setPersistenceDelegate(TableLayoutConstraints.class,
            new ConstraintsPersistenceDelegate(new String[]
                {"col1", "row1", "col2", "row2", "hAlign", "vAlign"}));
        encoder.setPersistenceDelegate
            (ModernTableLayout.class, new TableLayoutPersistenceDelegate());
        encoder.setPersistenceDelegate(ImageIcon.class,
            new PersistenceDelegate()
            {
                protected Expression instantiate(Object oldInstance,
                                                 Encoder out)
                {
                    return new Expression(oldInstance,
                        oldInstance.getClass(),
                        "new",
                        new Object[]{oldInstance.toString()});
                }
            });
    }

    public static Object factory(Class c, String init)
    {
        Object o = null;
        try
        {
            o = c.newInstance();
        }
        catch(IllegalAccessException e)
        {
            throw new Error(e);
        }
        catch(InstantiationException e)
        {
            throw new Error(
                "Cannot instantiate an interface or abstract class - " + c.getName());
        }
        SelfReferenceUtils.invoke(o, init);
        return o;
    }
}
