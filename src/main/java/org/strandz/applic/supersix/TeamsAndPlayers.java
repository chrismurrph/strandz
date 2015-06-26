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
package org.strandz.applic.supersix;

import org.strandz.data.supersix.objects.Division;
import org.strandz.core.applichousing.MenuTabApplication;
import org.strandz.core.interf.VisibleStrandAction;
import org.strandz.lgpl.util.Assert;

import javax.swing.AbstractAction;
import java.util.List;
import java.util.ArrayList;

public class TeamsAndPlayers
{
    private MenuTabApplication aSupersix;
    private SuperSixSdzManager superSixSdzManager;
    private List addedActions = new ArrayList();
    
    static final String TEAMS_AND_PLAYERS_SUBMENU = "Teams -> Players";
    
    TeamsAndPlayers( MenuTabApplication aSupersix, SuperSixSdzManager superSixSdzManager)
    {
        this.aSupersix = aSupersix;
        this.superSixSdzManager = superSixSdzManager;
    }
    
    void removeAllSubMenus()
    {
        aSupersix.removeAllItemsWithTitleContaining( TEAMS_AND_PLAYERS_SUBMENU);
        addedActions.clear();
    }

    void refresh()
    {
        Assert.notEmpty( addedActions);
        aSupersix.display( addedActions);
    }
    
    void addSubMenu( Division division)
    {
        int key = division.getName().charAt( 0);
        addSubMenu( division, key);
    }
        
    private void addSubMenu( Division division, int key)
    {
        VisibleStrandAction vsa = new VisibleStrandAction( "(" + division.getName() + ") " + TEAMS_AND_PLAYERS_SUBMENU,
                                      TEAMS_AND_PLAYERS_SUBMENU);

        MaintainTeamStrand mts = new MaintainTeamStrand(aSupersix, superSixSdzManager, division);
        vsa.putValue(AbstractAction.MNEMONIC_KEY, new Integer(key));
        vsa.putValue(AbstractAction.SHORT_DESCRIPTION,
                     "Maintain " + division.getName() + " Teams and Players"
        );
        vsa.setVisibleStrand(mts);
        aSupersix.addItem(vsa);
        addedActions.add( vsa);
    }
}
