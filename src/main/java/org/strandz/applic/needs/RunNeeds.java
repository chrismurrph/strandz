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
package org.strandz.applic.needs;

import org.strandz.core.interf.VisibleStrandI;
import org.strandz.core.interf.VisibleStrandAction;
import org.strandz.core.applichousing.MenuTabApplication;
import org.strandz.lgpl.store.DataStore;
import org.strandz.lgpl.util.Err;
import org.strandz.store.needs.NeedsApplicationData;
import org.strandz.store.needs.XMLNeedsData;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import java.awt.event.KeyEvent;

/**
 * Application Housing
 */
public class RunNeeds
{
    VisibleStrandI actionableStrand;
    /**
     * For a particular 'main' type of UI eg. different Application if
     * a different Controller were to be used.
     */
    MenuTabApplication aRoster;
    private static final String maintain = "Maintain";
    VisibleStrandAction vsa;
    private static int times;

    public RunNeeds()
    {
        this(null);
    }

    public RunNeeds(JFrame frame)
    {
        times++;
        Err.pr("RunNeeds being called " + times + " times");
        if(times == 2)
        {
            Err.error("Only RunNeeds once in same JVM");
        }

        DataStore td = NeedsApplicationData.getInstance().getData();
        if(td.getClass() != XMLNeedsData.class)
        {
            Err.error("Not ready to run with a store of type " + td.getClass());
        }
        aRoster = new MenuTabApplication(td);
        aRoster.setCurrentItemTitle(NeedsApplicationData.databaseName);
        aRoster.addTitle(maintain, 'm');
        {
            vsa = new VisibleStrandAction("Maintain Contact", maintain);

            MaintainContactStrand rvs = new MaintainContactStrand(aRoster);
            vsa.putValue(AbstractAction.MNEMONIC_KEY, new Integer(KeyEvent.VK_R));
            vsa.putValue(AbstractAction.SHORT_DESCRIPTION, "Maintain Contact");
            vsa.setVisibleStrand(rvs);
            aRoster.addItem(vsa);
        }
        if(frame != null)
        {
            aRoster.display();
        }
    }

    public boolean exitVetoNotification()
    {
        return aRoster.exitVetoNotification();
    }
}
