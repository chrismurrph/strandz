package org.strandz.lgpl.widgets;

import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.util.IdentifierI;

import javax.swing.*;
import java.awt.event.MouseListener;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.*;

/**
 * User: Chris
 * Date: 05/05/2009
 * Time: 11:29:08 PM
 */
public class NoEdgedButton extends JLabel implements MouseListener, IdentifierI
{
    private ActionListener actionListener;
    private static int constructedTimes;
    private int id;

    public NoEdgedButton()
    {
        init();
    }

    public NoEdgedButton(String text)
    {
        super(text);
    }

    private void init()
    {
        addMouseListener( this);
        constructedTimes++;
        id = constructedTimes;
    }

    public void doClick()
    {
        if(actionListener != null)
        {
            ActionEvent event = new ActionEvent( this, ActionEvent.ACTION_PERFORMED, getText());
            actionListener.actionPerformed( event);
        }
    }

    public void mouseClicked(MouseEvent e)
    {
        if(actionListener != null && e.getButton() == MouseEvent.BUTTON1)
        {
            ActionEvent event = new ActionEvent( this, ActionEvent.ACTION_PERFORMED, getText());
            actionListener.actionPerformed( event);
        }
    }

    public String toString()
    {
        String result = "NoEdgedButton ID: " + id + ", opaque: " + isOpaque() + ", bgcolor: " + getBackground();
        return result;
    }

    public void setToolTipText( String toolTip)
    {
        //Err.pr( "NoEdgedButton ID: " + id + ", toolTip now set to <" + toolTip + ">");
        super.setToolTipText( toolTip);
    }

    /* Only inherited for debugging
    public void setBackground( Color colour)
    {
        if(id != 0)
        {
            if(colour != null)
            {
                Assert.isTrue( isOpaque());
            }
            Err.pr( "title: <" + getText() + ">");
            Err.pr( "NoEdgedButton.setBackground() being called for <" + getText() + ">. ID: " + id + ", opaque: " +
                isOpaque() + ", current bgcolor: " + getBackground() + ", new bgcolor: " + colour);
            Err.pr( "----");
            if(id == 2)
            {
                //Err.debug();
            }
        }
        else
        {
             *
             * Seems odd, but will be during XMLEncoding and decoding
             *
        }
        super.setBackground( colour);
    }
    */

    public void mousePressed(MouseEvent e)
    {
    }

    public void mouseReleased(MouseEvent e)
    {
    }

    public void mouseEntered(MouseEvent e)
    {
    }

    public void mouseExited(MouseEvent e)
    {
    }

    public void setActionListener( ActionListener actionListener)
    {
        this.actionListener = actionListener;
    }

    public ActionListener getActionListener()
    {
        return actionListener;
    }

    public int getId()
    {
        return id;
    }
}
