package org.strandz.lgpl.widgets;

import org.strandz.lgpl.util.Err;

import javax.swing.*;
import java.awt.event.MouseListener;

/**
 * User: Chris
 * Date: 13/05/2009
 * Time: 11:58:25 AM
 */
public class IconLabel extends JLabel
{
    private static int constructedTimes;
    private int id;

    public IconLabel()
    {
    }

    public IconLabel( Icon icon)
    {
        super( icon);
        constructedTimes++;
        id = constructedTimes;
    }

    public String toString()
    {
        return "IconLabel, ID: " + id;
    }

    public void addMouseListener( MouseListener l)
    {
        //Err.pr( "Adding " + l + " to " + this);
        super.addMouseListener( l);
    }
}
