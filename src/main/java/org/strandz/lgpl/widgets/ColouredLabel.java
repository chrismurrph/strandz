package org.strandz.lgpl.widgets;

import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.note.SdzNote;

import java.awt.*;
import java.util.*;

import javax.swing.*;

/**
 * User: Chris
 * Date: 08/01/2009
 * Time: 4:16:01 PM
 */
/**
 * Currently the foreground colour given is always white. This will change if we use
 * a light bg colour. When we do we will have to ask the colour if it is light or
 * dark and on this basis choose to use a white or black icon. Same logic will need
 * to be used for HeaderLabel.
 */
public class ColouredLabel extends HeaderLabel
{
    public static Icon descendingWhitePortableImageIcon;
    public static Icon ascendingWhitePortableImageIcon;
    private static int times;

    static
    {
        descendingWhitePortableImageIcon = PortableImageIcon.createImageIcon(
                "images/DescendingWhite.gif", "Descending", PortableImageIcon.STDERR_MSG);
        ascendingWhitePortableImageIcon = PortableImageIcon.createImageIcon(
                "images/AscendingWhite.gif", "Ascending", PortableImageIcon.STDERR_MSG);
    }

    public ColouredLabel( Color bgCalculateColor, Color fgCalculateColor,
                          String name, java.util.List<PortableImageIcon> pictureIcons,
                          int currentPictureIcon, int visualOrderNumber, boolean removable)
    {
        /*
         * Expect the false to become true when all dynamic
         */
        super( name, removable, pictureIcons, currentPictureIcon, visualOrderNumber);
        setOpaque( true);

        setForeground( fgCalculateColor);
        setBackground( bgCalculateColor);
        times++;
        Err.pr( SdzNote.TOO_MANY_LABELS, "CREATE ColouredLabel times " + times);
        if(times == 0)
        {
            Err.stack();
        }
    }

    void paintSortIcon( Graphics g, SortOrder sortOrder)
    {
        if(sortOrder == SortOrder.DESCENDING)
        {
            descendingWhitePortableImageIcon.paintIcon( this, g, getSize().width-11, getSize().height-11);
        }
        else
        {
            ascendingWhitePortableImageIcon.paintIcon( this, g, getSize().width-11, getSize().height-11);
        }
    }
}
