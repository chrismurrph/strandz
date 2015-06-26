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

import org.strandz.view.util.DTUtils;
import org.strandz.lgpl.util.Err;

import javax.swing.JLabel;
import javax.swing.JComboBox;

public class TeamCopier
{
    private JLabel t1Label;
    private JLabel t2Label;
    JComboBox t1Combo;
    JComboBox t2Combo;

    public TeamCopier( MaintainSeasonDT dt)
    {
        DTUtils.chkNotNull( dt.ui2.getOtherMatchDetailsPanel());
        t1Combo = dt.ui2.getOtherMatchDetailsPanel().getCbTeamOne();
        t2Combo = dt.ui2.getOtherMatchDetailsPanel().getCbTeamTwo();
        DTUtils.chkNotNull( t1Combo);
        DTUtils.chkNotNull( t2Combo);
        //There are no attributes for these, so we directly use the items:
        DTUtils.chkNotNull( dt.ui2.getMatchResultPanel());
        t1Label = dt.ui2.getMatchResultPanel().getLTeamOne();
        t2Label = dt.ui2.getMatchResultPanel().getLTeamTwo();
        DTUtils.chkNotNull( t1Label);
        DTUtils.chkNotNull( t2Label);
        //
    }
    
    void copyTeam()
    {
        copyTeam( t1Combo);
        copyTeam( t2Combo);
    }

    void copyTeam( JComboBox cb)
    {
        if(cb == t1Combo)
        {
            t1Label.setText( (String)cb.getSelectedItem());
        }
        else if(cb == t2Combo)
        {
            t2Label.setText( (String)cb.getSelectedItem());
        }
        else
        {
            Err.error( "Combo unknown: " + cb);
        }
    }
}
