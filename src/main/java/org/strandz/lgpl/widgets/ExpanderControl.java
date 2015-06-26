package org.strandz.lgpl.widgets;

import org.strandz.lgpl.tablelayout.ModernTableLayout;
import org.strandz.lgpl.util.BeansUtils;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.util.IdentifierI;
import org.strandz.lgpl.note.SdzNote;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;

/**
 * User: Chris
 * Date: 18/04/2009
 * Time: 3:52:16 PM
 */
public class ExpanderControl extends JPanel implements ActionListener, IdentifierI
{
    /**
     * Theoretically labels could go into extras as well. At least as an array here
     * the accessors will work well at DT.
     */
    private NoEdgedButton[] labels;
    private List<MouseListener> mouseListeners = new ArrayList<MouseListener>();
    /*
    private List<Color> backgrounds;
    private List<Color> foregrounds;
    private List<String> toolTipTexts;
    */
    private List<ExpanderControlTransObj> extras;
    private NoEdgedButton lShiftRight;
    private transient int selected = 0;
    private ActionListener actionListener;
    private List<ChangeListener> changeListeners = new ArrayList<ChangeListener>();
    private String explanation;
    //As we are not calling the setters in init(), these two will always be these
    //default values at design time.
    private boolean foregroundChangesWhenSelected = true;
    private boolean sizeIncreasesWhenSelected = false;

    private static final int BORDER = 15;
    private static final JComponent TYPICAL_SIZED_CONTAINER = new JLabel();
    private static final JComponent TYPICAL_BIG_CONTAINER = new JLabel();
    private static final Font NORMAL_FONT = new Font( "Courier", Font.PLAIN, 12);
    public static final Font BOLD_FONT = new Font( "Courier", Font.BOLD, 12);
    public static final Font BIG_BOLD_FONT = new Font( "Courier", Font.BOLD, 13);
    public static final Color PURPLE = new Color( 102, 14, 122);
    private static int NUM_LEAD_COLS = 3;
    private static int GAP_WIDTH = 12;
    private static int LEFT_EDGE_WIDTH = 7;
    private static Icon portableImageIcon;

    private static int constructedTimes;
    private int id;

    static
    {
        TYPICAL_SIZED_CONTAINER.setFont( BOLD_FONT);
        TYPICAL_BIG_CONTAINER.setFont( BIG_BOLD_FONT);
        /* If store at DT then each ExpanderControl can have its own tooltip
        portableImageIcon = PortableImageIcon.createImageIcon(
                "images/control_end_blue.png", "Change View", PortableImageIcon.STDERR_MSG);
        */
    }

    public ExpanderControl()
    {
        constructedTimes++;
        id = constructedTimes;
    }

    /**
     * We will have particular Expander controls that override this
     * method and call it with their own list of titles.
     * Method call will end up with labels and 'button' that can
     * be XML transported.
     */
    public void init(){}

    public void init( List<String> titles, int selected, String explanation,
                      List<ExpanderControlTransObj> extras)
    {
        this.selected = selected;
        Err.pr( SdzNote.EXPANDER_IDX, "init on <" + getName() + ">, selected <" + selected + ">");
        if(SdzNote.EXPANDER_IDX.isVisible() && "chkIntoExisting".equals( getName()) && selected == 0)
        {
            Err.pr( "Selecting 0 not happening on DB (solution was to write toString())");
        }
        removeAll();
        ModernTableLayout modernTableLayout = new ModernTableLayout();
        modernTableLayout.insertColumn( 0, LEFT_EDGE_WIDTH);
        modernTableLayout.insertColumn( 1, 25-LEFT_EDGE_WIDTH);
        modernTableLayout.insertColumn( 2, GAP_WIDTH);
        int count = NUM_LEAD_COLS;
        for (int i = 0; i < titles.size(); i++)
        {
            String title = titles.get( i);
            long labelWidth;
            if(sizeIncreasesWhenSelected)
            {
                labelWidth = WidgetUtils.calcPixelWidthOfStringOnComponent( title, TYPICAL_BIG_CONTAINER);
            }
            else
            {
                labelWidth = WidgetUtils.calcPixelWidthOfStringOnComponent( title, TYPICAL_SIZED_CONTAINER);
            }
            modernTableLayout.insertColumn( count, labelWidth);
            count++;
            IconLabel iconLabel = getIconLabelAt( i, extras);
            if(iconLabel != null)
            {
                Icon icon = iconLabel.getIcon();
                if(icon != null)
                {
                    modernTableLayout.insertColumn( count, icon.getIconWidth()+7);
                    count++;
                }
                else
                {
                    Err.error( "Expect there to always be an icon in an IconLabel");
                }
            }
            modernTableLayout.insertColumn( count, GAP_WIDTH);
            count++;
        }
        modernTableLayout.insertRow( 0, ModernTableLayout.FILL);
        setLayout( modernTableLayout);
        NoEdgedButton[] labels = new NoEdgedButton[titles.size()];
        lShiftRight = new NoEdgedButton();
        add(lShiftRight, "1,0");
        count = NUM_LEAD_COLS;
        for (int i = 0; i < titles.size(); i++)
        {
            NoEdgedButton item = new NoEdgedButton();
            addMouseListeners( item, mouseListeners);
            item.setActionListener( this);
            if(i == selected)
            {
                if(sizeIncreasesWhenSelected)
                {
                    item.setFont(BIG_BOLD_FONT);
                }
                else
                {
                    item.setFont(BOLD_FONT);
                }
                if(foregroundChangesWhenSelected)
                {
                    item.setForeground( PURPLE);
                }
            }
            else
            {
                item.setFont( NORMAL_FONT);
                item.setForeground( null);
            }
            item.setName( "lExpander control item number " + i);
            String title = titles.get( i);
            item.setText( title);
            item.setHorizontalAlignment( SwingConstants.LEADING);
            add( item, count + ", 0");
            count ++;
            IconLabel iconLabel = getIconLabelAt( i, extras);
            if(iconLabel != null)
            {
                addMouseListeners( iconLabel, mouseListeners);
                add( iconLabel, count + ", 0");
                count++;
            }
            count++;
            labels[i] = item;
            if(extras != null && extras.size() > i)
            {
                if(extras.get( i).getBackground() != null)
                {
                    labels[i].setOpaque( true);
                    labels[i].setBackground( extras.get( i).getBackground());
                }
                else
                {
                    //Err.error( "Do we just setBackground() to null and opaque to false?");
                    labels[i].setOpaque( false);
                    labels[i].setBackground( null);
                }
                if(selected == i && foregroundChangesWhenSelected)
                {
                    /*
                     * Never override the selected foreground being purple if
                     * selected being purple is what has been specified.
                     */
                }
                else
                {
                    if(extras.get( i).getForeground() != null)
                    {
                        labels[i].setForeground( extras.get( i).getForeground());
                    }
                    else
                    {
                        labels[i].setForeground( null);
                    }
                }
                if(extras.get( i).getToolTipText() != null)
                {
                    labels[i].setToolTipText( extras.get( i).getToolTipText());
                }
                else
                {
                    labels[i].setToolTipText( null);
                }
                if(iconLabel != null)
                {
                    if(extras.get( i).getIconToolTipText() != null)
                    {
                        iconLabel.setToolTipText( extras.get( i).getIconToolTipText());
                    }
                    else
                    {
                        iconLabel.setToolTipText( null);
                    }
                }
            }
        }

        lShiftRight.setName( "lShiftRight");
        lShiftRight.setToolTipText( explanation);
        lShiftRight.setActionListener( this);
        PortableImageIcon portableImageIcon = PortableImageIcon.createImageIcon(
                "images/control_end_blue.png", "Change View", PortableImageIcon.STDERR_MSG);
        if(portableImageIcon != null)
        {
            lShiftRight.setIcon( portableImageIcon);
        }

        setLShiftRight( lShiftRight);
        setName( "ExpanderControl");
        setLabels( labels);
        setExplanation( explanation);
        /*
        setBackgrounds( backgrounds);
        setForegrounds( foregrounds);
        setToolTipTexts( toolTipTexts);
        */
        setExtras( extras);
    }

    private IconLabel getIconLabelAt( int index, List<ExpanderControlTransObj> extras)
    {
        IconLabel result = null;
        if(extras != null && extras.size() > index)
        {
            result = extras.get( index).getIconLabel();
        }
        return result;
    }

    /**
     * When the user clicks on this control it calls this, its own
     * action listener method, which propogates the event as well as
     * doing the visible changes.
     */
    public void actionPerformed( ActionEvent e)
    {
        int shiftToIndex;
        if(e.getSource() == getLShiftRight())
        {
            shiftToIndex = getSelected()+1;
            if(shiftToIndex == labels.length)
            {
                shiftToIndex = 0;
            }
        }
        else
        {
            //Err.pr( "Clicked on a dynamic label called: <" + e.getActionCommand() + ">");
            shiftToIndex = findIndexOfTitle( ((NoEdgedButton)e.getSource()).getText());
        }
        if(shiftToIndex != getSelected())
        {
            Err.pr( SdzNote.EXPANDER_IDX, "<" + this.getName() + "> needs change index to <" + shiftToIndex +
                "> from <" + getSelected() + ">");
            ActionListener actionListener = getActionListener();
            if(actionListener != null)
            {
                JLabel label = getLabel( shiftToIndex);
                ActionEvent event = new ActionEvent(
                    label, ActionEvent.ACTION_PERFORMED, label.getText());
                actionListener.actionPerformed( event);
            }
            else
            {
                Err.pr( "No actionListener on <" + getName() + ">");
            }
            setSelected( shiftToIndex);
            for (int i = 0; i < changeListeners.size(); i++)
            {
                ChangeListener changeListener = changeListeners.get(i);
                ChangeEvent changeEvent = new ChangeEvent( this);
                changeListener.stateChanged( changeEvent);
            }
        }
        else
        {
            Err.pr( SdzNote.EXPANDER_IDX, this.getName() + " thinks that <" + shiftToIndex +
                "> is already the current index");
        }
    }

    public int findIndexOfTitle( String title)
    {
        int result = Utils.UNSET_INT;
        JLabel[] labels = getLabels();
        for (int i = 0; i < labels.length; i++)
        {
            JLabel jLabel = labels[i];
            if(jLabel.getText().equals( title))
            {
                result = i;
                break;
            }
        }
        Assert.isFalse( result == Utils.UNSET_INT);
        return result;
    }

    public int getSelected()
    {
        return selected;
    }

    public String getCurrentTitle()
    {
        String result;
        JLabel label = getLabel( getSelected());
        result = label.getText();
        return result;
    }

    /**
     * Rather than using this method, consider finding the button and doing doClick().
     */
    public void setSelected( int selected)
    {
        //Err.pr( "Selecting <" + selected + "> in <" + getName() + "> ID: " + getId());
        if(! BeansUtils.isDesignTime())
        {
            //Err.pr( "Not design time, so must be responding to user");
            init( getTitles( labels), selected, getExplanation(), getExtras());
            /* Don't do this, will be recursive Doh!
            ((NoEdgedButton)labels[selected]).doClick();
            */
        }
        else
        {
            Err.pr( "Design time, so skipping rebuilding");
        }
        this.selected = selected;
    }

//    public void setSelected( int selected)
//    {
//        /* Won't work, so instead we will layout the whole control again
//        labels[selected].setFont( LARGE_FONT);
//        labels[this.selected].setFont( NORMAL_FONT);
//        revalidate();
//        repaint();
//        */
//
//        this.selected = selected;
//    }

    /**
     * If you add to this list make sure you call setLabels()
     * directly after
     */
    public List<NoEdgedButton> getLabelsList()
    {
        List<NoEdgedButton> result = null;
        if(labels != null)
        {
            result = new ArrayList<NoEdgedButton>( Arrays.asList( labels));
        }
        return result;
    }

    public void setLabels(NoEdgedButton[] labels)
    {
        this.labels = labels;
    }

    public NoEdgedButton[] getLabels()
    {
        return labels;
    }

    public NoEdgedButton getLabel(int index)
    {
        return labels[index];
    }

    public void setLabel(int index, NoEdgedButton label)
    {
        labels[index] = label;
    }

    private static List<String> getTitles( JLabel[] labels)
    {
        List<String> result = new ArrayList<String>();
        for (int i = 0; i < labels.length; i++)
        {
            JLabel label = labels[i];
            result.add( label.getText());
        }
        return result;
    }

    public NoEdgedButton getLShiftRight()
    {
        return lShiftRight;
    }

    public void setLShiftRight(NoEdgedButton lShiftRight)
    {
        this.lShiftRight = lShiftRight;
    }

    public void setActionListener( ActionListener actionListener)
    {
        this.actionListener = actionListener;
    }

    public ActionListener getActionListener()
    {
        return actionListener;
    }

    public String getExplanation()
    {
        return explanation;
    }

    public void setExplanation(String explanation)
    {
        this.explanation = explanation;
    }

    public boolean isForegroundChangesWhenSelected()
    {
        return foregroundChangesWhenSelected;
    }

    public void setForegroundChangesWhenSelected( boolean foregroundChangesWhenSelected)
    {
        this.foregroundChangesWhenSelected = foregroundChangesWhenSelected;
    }

    public boolean isSizeIncreasesWhenSelected()
    {
        return sizeIncreasesWhenSelected;
    }

    public void setSizeIncreasesWhenSelected(boolean sizeIncreasesWhenSelected)
    {
        this.sizeIncreasesWhenSelected = sizeIncreasesWhenSelected;
    }

    public void addChangeListener(ChangeListener l)
    {
        changeListeners.add( l);
    }

    public void removeChangeListener(ChangeListener l)
    {
        changeListeners.remove( l);
    }

    public int getId()
    {
        return id;
    }

    /**
     * Only tested for being called at RT
     */
    public void setBackground( int index, Color background)
    {
        lazyAddExtras( index);
        extras.get( index).setBackground( background);
        labels[index].setOpaque( true);
        labels[index].setBackground( background);
        labels[index].revalidate();
        labels[index].repaint();
    }

    /**
     * Only tested for being called at RT
     */
    public void setForeground( int index, Color foreground)
    {
        lazyAddExtras( index);
        extras.get( index).setForeground( foreground);
        labels[index].setOpaque( true);
        labels[index].setForeground( foreground);
        labels[index].revalidate();
        labels[index].repaint();
    }

    /**
     * Only tested for being called at RT
     */
    public void setToolTipText( int index, String toolTipText)
    {
        lazyAddExtras( index);
        extras.get( index).setToolTipText( toolTipText);
        labels[index].setToolTipText( toolTipText);
    }

    private void lazyAddExtras( int index)
    {
        if(extras == null)
        {
            extras = new ArrayList<ExpanderControlTransObj>();
        }
        if(extras.size() == index)
        {
            extras.add( new ExpanderControlTransObj());
        }
    }

    public List<ExpanderControlTransObj> getExtras()
    {
        return extras;
    }

    public void setExtras(List<ExpanderControlTransObj> extras)
    {
        this.extras = extras;
    }

    /**
     * The internal controls will want to tell about their mouse events.
     * No longer doing the outer casing (ie this component itself)
     * Also note that these mouse listeners are kept so they can be re-applied
     * later.
     */
    public void addMouseListener( MouseListener l)
    {
        //MouseListener mouseListeners[] = getMouseListeners();
        //Print.prArray( mouseListeners, "mouseListeners of <" + getName() + ">, ID: " + getId());
        //Err.pr( "Adding new mouse listener: " + l);
        //super.addMouseListener( l);
        for (int i = 0; i < labels.length; i++)
        {
            JLabel label = labels[i];
            label.addMouseListener( l);
            IconLabel iconLabel = getIconLabelAt( i, extras);
            if(iconLabel != null)
            {
                iconLabel.addMouseListener( l);
            }
        }
        mouseListeners.add( l);
    }

    public void removeMouseListener( MouseListener l)
    {
        Err.error( "Code now!");
        super.removeMouseListener( l);
    }

    private static void addMouseListeners( JComponent control, List<MouseListener> mouseListeners)
    {
        for (int i = 0; i < mouseListeners.size(); i++)
        {
            MouseListener mouseListener = mouseListeners.get(i);
            control.addMouseListener( mouseListener);
        }
    }
}