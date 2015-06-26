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

import org.strandz.core.interf.SdzBagI;
import org.strandz.core.interf.VisibleStrandAction;
import org.strandz.core.interf.VisibleStrandHelper;
import org.strandz.core.interf.VisibleStrandI;
import org.strandz.view.util.AbstractStrandArea;
import org.strandz.view.util.StrandArea;
import org.strandz.view.util.UnadornedStrandArea;

import javax.swing.JComponent;

public class SimpleApplication extends SimplestApplication
{
    public AbstractStrandArea strandArea = new UnadornedStrandArea();

    public SimpleApplication()
    {
        super();
        strandArea.init();
    }

    public SimpleApplication(boolean headless)
    {
        super(headless);
        strandArea.init();
    }

    public void show()
    {
        panel.add(strandArea);
        super.show();
    }

    public void addItem(VisibleStrandAction action)
    {
        super.addItem(action);

        VisibleStrandI visibleStrand = action.getVisibleStrand();
        visibleStrand.setStrandArea( strandArea);
    }

    /**
     * This is when change to a different Strand, not when change to
     * a different tab.
     */
    public void changeActionableStrand(VisibleStrandI visibleStrand, String reason)
    {
        super.changeActionableStrand(visibleStrand, reason);

        //VisibleStrandAction source = (VisibleStrandAction) e.getSource();
        //VisibleStrandI visibleStrand = source.getVisibleStrand();
        //Assert.notNull( visibleStrand.getNodeController(), visibleStrand + " does not have a NodeController");
        JComponent component = null;
        if(visibleStrand.getNodeController() != null)
        {
            component = (JComponent) visibleStrand.getNodeController().getPhysicalController();
        }
        else
        {
            //Not all Strands have a NodeController and SimpleApplication s/be able to handle
            //them, as more complex Applications can
            //component = new PictToolBarController();
            //Assert.notNull( visibleStrand.getNodeController(), visibleStrand + " does not have a NodeController");
        }
        if(StrandArea.USE_TOOL_BAR_AREA)
        {
            strandArea.getToolBarArea().refitArea( component);
        }
        else
        {
            strandArea.getToolBarPane().refitControllerArea( component);
        }
    }

    public VisibleStrandHelper createStrandHelper(SdzBagI sbI, VisibleStrandI as, AbstractStrandArea.EnclosurePanel surface)
    {
        VisibleStrandHelper result = new TabVisibleStrandHelper(
            ((SdzBag) sbI).getBarHelper(), sbI.getStrand(), this,
            surface
            //getSdzSurface(as)
        );
        ((TabVisibleStrandHelper) result).setNodeController(
            ((SdzBag) sbI).getHelper().nodeController);
        return result;
    }

    // called within display on the Strand
    public JComponent getEnclosure(VisibleStrandI actionableStrand)
    {
        return strandArea.getEnclosure();
    }
}
