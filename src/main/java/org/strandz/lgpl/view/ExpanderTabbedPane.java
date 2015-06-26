package org.strandz.lgpl.view;

import org.strandz.lgpl.tablelayout.ModernTableLayout;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.widgets.ExpanderControl;
import org.strandz.lgpl.widgets.NoEdgedButton;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: Chris
 * Date: 06/05/2009
 * Time: 3:46:10 AM
 */
abstract public class ExpanderTabbedPane extends JPanel implements ActionListener
{
    private ExpanderControl expanderControl;
    private JPanel fillerPanel;
    /**
     * This panel is put in a zero dimension spot on a
     * ModernTableLayout. Place where can be put panels
     * that are to be seen in the Designer but not at
     * runtime - not until they are selected as the
     * current tab anyway. If we can see these panels
     * in the Designer then we can bind to their controls.
     */
    private JPanel hiddenPanel;
    /**
     * Having a list of panels is not essential as they are
     * kept in the hiddenPanel anyway. Is just convenient
     * for actionPerformed() to access them.
     */
    private JComponent[] panels;

    static final int EXPANDER_HEIGHT = 30;

    /**
     * Used if panels are going to be added and removed at the behest
     * of runtime code.
     */
    public void init( ExpanderControl expanderControl)
    {
        init( expanderControl, null);
    }

    public void init( ExpanderControl expanderControl, JComponent panels[])
    {
        fillerPanel = new JPanel();
        hiddenPanel = new JPanel();
        //fillerPanel.init();
        double size[][] =
            {
                // Columns
                {ModernTableLayout.FILL, 0},
                // Rows
                {EXPANDER_HEIGHT, ModernTableLayout.FILL, 0}
            };
        setLayout( new ModernTableLayout(size));
        fillerPanel.setName( "fillerPanel");
        fillerPanel.setLayout( new BorderLayout());
        hiddenPanel.setName( "hiddenPanel");
        hiddenPanel.setLayout( null);
        add( expanderControl, "0,0");
        add( fillerPanel, "0,1");
        add( hiddenPanel, "1,2");
        if(panels != null)
        {
            setPanels( panels);
            for (int i = 0; i < panels.length; i++)
            {
                JComponent panel = panels[i];
                hiddenPanel.add( panel);
            }
        }
        else
        {
            //panels will be added/removed dynamically
        }
        setExpanderControl( expanderControl);
        setFillerPanel( fillerPanel);
        setHiddenPanel( hiddenPanel);
        setName( "ExpanderTabbedPane");
        expanderControl.setActionListener( this);
    }

    public JPanel getFillerPanel()
    {
        return fillerPanel;
    }

    public void setFillerPanel(JPanel fillerPanel)
    {
        this.fillerPanel = fillerPanel;
    }

    public JPanel getHiddenPanel()
    {
        return hiddenPanel;
    }

    public void setHiddenPanel(JPanel hiddenPanel)
    {
        this.hiddenPanel = hiddenPanel;
    }

    public ExpanderControl getExpanderControl()
    {
        return expanderControl;
    }

    public void setExpanderControl(ExpanderControl expanderControl)
    {
        this.expanderControl = expanderControl;
    }

    /**/
    public void setPanels(JComponent[] panels)
    {
        this.panels = panels;
    }

    public JComponent[] getPanels()
    {
        return panels;
    }

    /**
     * If you add to this list make sure you call setPanels()
     * directly after
     */
    public List<JComponent> getPanelsList()
    {
        List<JComponent> result = null;
        if(panels != null)
        {
            result = new ArrayList<JComponent>( Arrays.asList( panels));
        }
        return result;
    }

    public JComponent getPanel(int index)
    {
        return panels[index];
    }

    public void setPanel(int index, JComponent panel)
    {
        panels[index] = panel;
    }
    /**/

    /**
     * Called from the user clicking somewhere on the ExpanderControl. If you
     * want to programmatically make this happen getExpanderControl().setSelected( index)
     * will come back to here after having changed the control's appearance as well.
     */
    public void actionPerformed(ActionEvent e)
    {
        int index = getExpanderControl().findIndexOfTitle( e.getActionCommand());
        //Err.pr( "Index of label that selected is <" + index + ">");
        if(getPanels() == null || getPanels().length < index)
        {
            Err.pr( "Do not have enough panels so won't be able to add at " + index);
        }
        else
        {
            getFillerPanel().removeAll();
            if(index >= getPanels().length)
            {
                Err.error( "Not as many tabs added to the ExpanderTabbedPane as the ExpanderControl presumes");
            }
            getFillerPanel().add( getPanel( index));
            getFillerPanel().revalidate();
            getFillerPanel().repaint();
        }
    }

    public String getSelectedTitle()
    {
        return getTitleAt( getSelectedIndex());
    }

    public int getSelectedIndex()
    {
        return expanderControl.getSelected();
    }

    public String getTitleAt( int index)
    {
        String result;
        JLabel label = expanderControl.getLabel( index);
        result = label.getText();
        return result;
    }

    public void setIconAt( int index, Icon icon)
    {
        JLabel label = expanderControl.getLabel( index);
        label.setIcon( icon);
    }

    public void addTab( String title, Component component)
    {
        /*
        super.addTab( title, component);
        //setForegroundAt( indexOfComponent( component), Color.yellow);
        */
        List<JComponent> panels = getPanelsList();
        if(panels == null)
        {
            panels = new ArrayList<JComponent>();
        }
        panels.add( (JComponent)component);
        JComponent comps[] = new JComponent[panels.size()];
        for (int i = 0; i < panels.size(); i++)
        {
            JComponent comp = panels.get(i);
            comps[i] = comp;
        }
        setPanels( comps);
        getHiddenPanel().add( component);

        List<NoEdgedButton> labels = expanderControl.getLabelsList();
        if(labels == null)
        {
            labels = new ArrayList<NoEdgedButton>();
        }
        labels.add( new NoEdgedButton( title));
        NoEdgedButton labs[] = new NoEdgedButton[panels.size()];
        //List<Color> colours = new ArrayList<Color>();
        for (int i = 0; i < labels.size(); i++)
        {
            NoEdgedButton label = labels.get(i);
            labs[i] = label;
            //colours.add( null);
        }
        //expanderControl.setBackgrounds( colours);
        expanderControl.setLabels( labs);
        expanderControl.setSelected( expanderControl.getSelected());
    }

    /**
     * Replicates what JTabbedPane can do
     */
    public int indexOfComponent( Component component)
    {
        int result = Utils.UNSET_INT;
        JComponent[] panels = getPanels();
        for (int i = 0; i < panels.length; i++)
        {
            JComponent panel = panels[i];
            if(panel == component)
            {
                result = i;
                break;
            }
        }
        Assert.isFalse( result == Utils.UNSET_INT,
            "Component named <" + component + "> does not exist in LocalTabbedPane");
        return result;
    }

    /**
     * Replicates what JTabbedPane can do
     */
    public void setBackgroundAt( int index, Color background)
    {
        //So will work for next time a select/init() is done on the expanderControl
        expanderControl.setForeground( index, background);
    }

    public void addChangeListener( ChangeListener l)
    {
        //Err.pr( "addChangeListener() not currently implemented");
        expanderControl.addChangeListener( l);
    }

    public void setSelectedIndex( int index)
    {
        //Err.pr( "setSelectedIndex() not currently implemented");
        expanderControl.setSelected( index);
    }

    public Component getSelectedComponent()
    {
        return getFillerPanel().getComponent( 0);
    }

    public void setToolTipTextAt( int index, String toolTipText)
    {
        expanderControl.setToolTipText( index, toolTipText);
    }
}
