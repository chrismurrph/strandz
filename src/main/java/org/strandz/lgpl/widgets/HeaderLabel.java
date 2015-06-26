package org.strandz.lgpl.widgets;

import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.IdentifierI;

import javax.swing.*;
import javax.swing.border.Border;
import java.util.List;
import java.util.ArrayList;
import java.awt.*;

/**
 * User: Chris
 * Date: 08/01/2009
 * Time: 4:15:40 PM
 */
/**
 * Note that this HeaderLabel can also effectively become a ColoredLabel when it
 * is determined that it is associated with a calculated Attribute.
 */
public class HeaderLabel extends JLabel implements IdentifierI
{
    private SortOrder sortOrder = SortOrder.UNSORTED;
    private Icon descendingImageIcon = descendingBlackPortableImageIcon;
    private Icon ascendingImageIcon = ascendingBlackPortableImageIcon;
    private List<PortableImageIcon> pictureIcons;
    private int currentPictureIcon = Utils.UNSET_INT;
    /**
     * Unrelated to sortOrder. If not Utils.UNSET_INT then will display
     * a number in the lower left corner.
     */
    private int visualOrderNumber = Utils.UNSET_INT;
    private boolean removable;

    private static final Border BORDER = BorderFactory.createLineBorder( Color.BLUE);
    private static Icon descendingBlackPortableImageIcon;
    private static Icon ascendingBlackPortableImageIcon;
    private static final int SORT_ICON_LENGTH = 11;
    private static final int PICTURE_ICON_WIDTH = 18;
    private static final int PICTURE_ICON_HEIGHT = 15;

    private static int constructedTimes;
    private int id;
    private static int times;

    static
    {
        /*
         * Ok to be static as there is only ever one column in the program that is sorted at
         * any one time
         */
        descendingBlackPortableImageIcon = PortableImageIcon.createImageIcon(
                "images/DescendingBlack.gif", "Descending", PortableImageIcon.STDERR_MSG);
        /*
        Works fine but Sun doesn't give us a white one:
          Trouble is that it gives a black Icon, but the application uses both black and white
          icons in keeping with its own colour scheme - depending on how bright the background
          colour is. So I need to get the one Sun gives me and write it to a file so I can
          manipulate it using Paint/GIMP or change its foreground colour using code.
        //descendingBlackPortableImageIcon = (ImageIcon)UIManager.get( "Table.descendingSortIcon");
        */
        ascendingBlackPortableImageIcon = PortableImageIcon.createImageIcon(
                "images/AscendingBlack.gif", "Ascending", PortableImageIcon.STDERR_MSG);
    }

    /**
     *
     * @param name
     * @param removable
     * @param pictureIcons
     * Intention is that these pictureIcons are created once for the running
     * program and the same ones put in each of these objects.
     *
     */
    public HeaderLabel( String name, boolean removable,
                        List<PortableImageIcon> pictureIcons,
                        int currentPictureIcon,
                        int visualOrderNumber)
    {
        Assert.isTrue( name.startsWith( "l"), "Name does not start with \'l\' in: " + this.getClass().getName());
        setBorder(BORDER);
        setHorizontalAlignment(SwingConstants.CENTER);
        setName( name);
        this.removable = removable;
        this.pictureIcons = pictureIcons;
        this.currentPictureIcon = currentPictureIcon;
        Assert.isFalse( visualOrderNumber == 0);
        this.visualOrderNumber = visualOrderNumber;
        constructedTimes++;
        id = constructedTimes;
        /*
        if(toString().contains( "VODAFONE"))
        {
            Err.pr( "HeaderLabel constructor: <" + this + ">");
            times++;
            if(times == 2)
            {
                Err.stack();
            }
        }
        */
    }

    public String toString()
    {
        return "name: " + getName() + ", currentPictureIcon: " + currentPictureIcon +
            ", visualOrderNumber: " + visualOrderNumber;
    }

    private void paintIcons( Graphics g)
    {
        if(visualOrderNumber != Utils.UNSET_INT)
        {
            g.drawString( "" + visualOrderNumber, 5, getHeight()-5);
        }
        if(sortOrder != SortOrder.UNSORTED)
        {
            paintSortIcon( g, sortOrder);
        }
        paintCurrentPictureIcon( g, sortOrder != SortOrder.UNSORTED);
    }

    void paintSortIcon( Graphics g, SortOrder sortOrder)
    {
        if(sortOrder == SortOrder.DESCENDING)
        {
            descendingImageIcon.paintIcon( this, g, getSize().width-SORT_ICON_LENGTH, getSize().height-SORT_ICON_LENGTH);
        }
        else
        {
            ascendingImageIcon.paintIcon( this, g, getSize().width-SORT_ICON_LENGTH, getSize().height-SORT_ICON_LENGTH);
        }
    }

    /**
     * Assumption is that the width of the icon is 18 and the height 15
     */
    private void paintCurrentPictureIcon( Graphics g, boolean sortIconDone)
    {
        Icon icon = currentIcon();
        if(icon != null)
        {
            int across;
            int down;
            if(sortIconDone)
            {
                //Need to position to the left of the sort icon
                across = getSize().width - PICTURE_ICON_WIDTH - SORT_ICON_LENGTH;
                down = getSize().height - PICTURE_ICON_HEIGHT;
            }
            else
            {
                across = getSize().width - PICTURE_ICON_WIDTH;
                down = getSize().height - PICTURE_ICON_HEIGHT;
            }
            icon.paintIcon( this, g, across, down);
        }
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        paintIcons(g);
    }

    public SortOrder getSortOrder()
    {
        return sortOrder;
    }

    public void setSortOrder( SortOrder sortOrder)
    {
        this.sortOrder = sortOrder;
    }

    public void setDescendingImageIcon(Icon descendingImageIcon)
    {
        this.descendingImageIcon = descendingImageIcon;
    }

    public void setAscendingImageIcon(Icon ascendingImageIcon)
    {
        this.ascendingImageIcon = ascendingImageIcon;
    }

    public boolean isRemovable()
    {
        return removable;
    }

    public void setCurrentPictureIcon(int currentPictureIcon)
    {
        this.currentPictureIcon = currentPictureIcon;
        revalidate();
        repaint();
    }

    private Icon currentIcon()
    {
        Icon result = null;
        if(pictureIcons != null &&
            (currentPictureIcon >= 0 && pictureIcons.size() > currentPictureIcon)
            )
        {
            result = pictureIcons.get( currentPictureIcon);
            Assert.isTrue( result.getIconWidth() == PICTURE_ICON_WIDTH,
                "picture width is " + result.getIconWidth());
            Assert.isTrue( result.getIconHeight() == PICTURE_ICON_HEIGHT, 
                "picture height is " + result.getIconWidth());
        }
        return result;
    }

    public int getVisualOrderNumber()
    {
        return visualOrderNumber;
    }

    public void setVisualOrderNumber( int visualOrderNumber)
    {
        this.visualOrderNumber = visualOrderNumber;
        //Err.pr( "setVisualOrderNumber to " + visualOrderNumber + " in " + this);
        revalidate();
        repaint();
    }

    public int getId()
    {
        return id;
    }

/*
public void setText( String txt)
{
    //Err.pr( "txt: " + txt);
    super.setText( txt);
}
*/
}
