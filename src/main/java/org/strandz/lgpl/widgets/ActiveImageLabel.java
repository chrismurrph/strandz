/*
    Strandz - an API that matches the user to the data.
    Copyright (C) 2007 Chris Murphy

    Strandz is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    Strandz is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Strandz; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

    In addition, as a special exception, Chris Murphy gives
    permission to link the code of this program with any Java software for
    which complete source code is available at no cost (even if distribution
    or modification of that source code is restricted), and distribute the
    results of that linking. You must obey the GNU General Public
    License in all respects for all of the code used other than this Java code.
    If you modify this file, you may extend this exception to your version of
    the file, but you are not obligated to do so. If you do not wish to do so,
    delete this exception statement from your version.

    The authors can be contacted via www.strandz.org
*/
package org.strandz.lgpl.widgets;

import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.ImageUtils;
import org.strandz.lgpl.note.SdzNote;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ActiveImageLabel extends JLabel
{
    private ImageIcon redIcon;
    private ImageIcon greenIcon;
    private boolean active = false;
    private boolean firstTime = true;
    
    public int id;
    private static int timesConstructed;
    private static int times;
        
    public ActiveImageLabel()
    {
        super();
        timesConstructed++;
        id = timesConstructed;
        Err.pr( SdzNote.RED_WHEN_ENTER_QUERY, "Created ActiveImageLabel " + id);
        setIcon( getRedIcon());
    }
    
    public void setActive( boolean b)
    {
        if(active != b || firstTime)
        {
            firstTime = false;
            if(b)
            {
                setIcon( getGreenIcon());
            }
            else
            {
                setIcon( getRedIcon());
                times++;
                Err.pr( SdzNote.RED_WHEN_ENTER_QUERY, "RED Icon times " + times);
                if(times == 0)
                {
                    Err.stack();
                }
            }
            active = b;
        }
        //Err.pr( SdzNote.SYNC, "Active on ActiveImageLabel been set to " + active + " for " + id);
        if(id == 0)
        {
            Err.stack();
        }
    }
    
    private ImageIcon getGreenIcon()
    {
        ImageIcon result = greenIcon;
        if(result == null)
        {
            Image greenImage = ImageUtils.createMemoryImage( 19, 19, Color.GREEN, this);
            greenIcon = new ImageIcon( greenImage);
            result = greenIcon;
        }
        return result;
    }

    private ImageIcon getRedIcon()
    {
        ImageIcon result = redIcon;
        if(result == null)
        {
            Image redImage = ImageUtils.createMemoryImage( 19, 19, Color.RED, this);
            redIcon = new ImageIcon( redImage);
            result = redIcon;
        }
        return result;
    }
    
    public boolean isActive()
    {
        return active;
    }
        
    public void setText( String txt)
    {
        if(id >= 1)
        {
            Err.error( "Not intended that setText() be called on " + this.getClass().getName());
        }
        else
        {
            //constructor is source of this thread, so let it go thru
            super.setText( txt);
        }
    }
    
    public static void main(String[] args)
    {
        JFrame myFrame = new JFrame();
        myFrame.addWindowListener(new WindowAdapter()
        {
            public void windowClosing( WindowEvent e)
            {
                System.exit(0);
            }
        });

        JPanel panel1 = new JPanel();
        panel1.add(new ActiveImageLabel());

        JPanel panel2 = new JPanel();
        ActiveImageLabel active = new ActiveImageLabel();
        active.setActive( true);
        panel2.add( active);
        myFrame.setSize(100, 100);
        myFrame.getContentPane().add(panel1, BorderLayout.NORTH);
        myFrame.getContentPane().add(panel2, BorderLayout.SOUTH);
        myFrame.setVisible(true);
    }
}
