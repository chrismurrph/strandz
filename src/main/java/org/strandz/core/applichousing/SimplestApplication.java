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
package org.strandz.core.applichousing;

import org.strandz.core.interf.Application;
import org.strandz.core.interf.SdzBagI;
import org.strandz.core.interf.VisibleStrandAction;
import org.strandz.core.interf.VisibleStrandI;
import org.strandz.core.interf.VisibleStrandHelper;
import org.strandz.core.interf.ApplicationHelper;
import org.strandz.lgpl.util.DisplayUtils;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.MessageDlg;
import org.strandz.lgpl.util.Assert;
import org.strandz.view.util.AbstractStrandArea;

import javax.swing.JComponent;
import javax.swing.JFrame;
import java.util.List;

/**
 * An Application (the superclass) can be constructed with an
 * external JFrame. For this subclass of Application you cannot
 * construct it that way, and it has its own frame.
 */
public class SimplestApplication extends Application
{
    private JFrame frame;
    private boolean headless = false;
    
    private static final int HEIGHT = 300; // 600;
    private static final int WIDTH = 700;
    
    public SimplestApplication()
    {
        this(false);
    }

    public SimplestApplication(boolean headless)
    {
        if(!headless)
        {
            frame = new JFrame();
            if(MessageDlg.getFrame() == null)
            {
                MessageDlg.setFrame( frame);
            }
            else
            {
                //Assert.isTrue( MessageDlg.getFrame() == frame);
            }
        }
        else
        {
            this.headless = true;
        }
        applicationHelper = new ApplicationHelper( false);
    }

    public boolean isHeadless()
    {
        return headless;
    }

    public JComponent getEnclosure(VisibleStrandI actionableStrand)
    {
        return panel;
    }

    public JComponent getStrandArea(VisibleStrandI actionableStrand)
    {
        return panel;
    }

    public void addItem(VisibleStrandAction action)
    {
        super.addItem(action);
    }

    public void show()
    {
        DisplayUtils.display( panel, false, HEIGHT, WIDTH, frame);
    }

    public JFrame getFrame()
    {
        return frame;
    }
    
    public void removeDisplayedStrands( String cmd)
    {
        Err.error( "Can only remove current as only have current - have not yet implemented");
    }
    
    public void removeVisibleStrand( VisibleStrandI visibleStrand, String reason)
    {
        Err.error( "Not yet implemented removeVisibleStrand() in " + this.getClass().getName());
    }
    
    public boolean isDisplayed( VisibleStrandI visibleStrand)
    {
        return true;
    }

    /**
     * Only ever have one visible strand, so will always return 0  
     */
    public int getPosition( VisibleStrandI visibleStrand)
    {
        return 0;
    }

    public void removeAllItemsWithTitleContaining( String title)    
    {
        Err.error( "Not yet thought about for " + this.getClass().getName());
        List items = getItemsWithTitleContaining( title);
        removeAllItems( items);
    }
    
    public VisibleStrandI getCurrentVisibleStrand()
    {
        VisibleStrandI result = null;
        Err.error( "Not yet implemented");
        return result;
    }

    public VisibleStrandHelper createStrandHelper(
        SdzBagI sbI, VisibleStrandI as, AbstractStrandArea.EnclosurePanel surface)
    {
        VisibleStrandHelper result = new TabVisibleStrandHelper(
            ((SdzBag) sbI).getBarHelper(), sbI.getStrand(), this,
            surface
        );
        result.setNodeController(((SdzBag) sbI).getHelper().nodeController);
        return result;
    }
    }
