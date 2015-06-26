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

import mseries.ui.MChangeEvent;
import mseries.ui.MChangeListener;
import org.strandz.lgpl.util.ValidationException;
import org.strandz.data.supersix.objects.CompetitionSeason;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.MessageDlg;
import org.strandz.view.util.DTUtils;

import javax.swing.JComboBox;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;

public class MaintainSeasonMeetMatchEvents
{
    MaintainSeasonDT dt;
    SuperSixSdzManager superSixSdzManager;
    private TeamCopier teamCopier;
    private DivisionTeamLookup divisionTeamLookup;

    public MaintainSeasonMeetMatchEvents(
            MaintainSeasonDT dt, 
            SuperSixSdzManager superSixSdzManager,
            TeamCopier teamCopier,
            DivisionTeamLookup divisionTeamLookup)
    {
        this.superSixSdzManager = superSixSdzManager;
        this.dt = dt;
        this.teamCopier = teamCopier;
        this.divisionTeamLookup = divisionTeamLookup;
        DTUtils.chkNotNull( dt.ui0.getMdefStart());
        DTUtils.chkNotNull( dt.ui0.getMdefEnd());
        //DTUtils.chkNotNull( dt.ui0.getCbNight());
        dt.ui0.getMdefStart().addMChangeListener(
            new CalendarActionListener());
        dt.ui0.getMdefEnd().addMChangeListener(
            new CalendarActionListener());        
        //dt.ui0.getCbNight().addActionListener( new NightComboActionListener());    
        
        teamCopier.t1Combo.addActionListener( new TeamComboActionListener());
        teamCopier.t2Combo.addActionListener( new TeamComboActionListener());
        
        divisionTeamLookup.divisionCombo.addActionListener( new DivisionComboActionListener());
    }
    
    private void maybeCreateMeets()
    {
        try
        {
            if(dt.ui0.getMdefStart().getValue() != null && 
               dt.ui0.getMdefEnd().getValue() != null 
               //&& dt.ui0.getCbNight().getSelectedItem() != null
                    )
            {
                CompetitionSeason competitionSeason = (CompetitionSeason)dt.seasonCell.getItemValue();
                Err.pr( "Complete competitionSeason: " + competitionSeason);
                try
                {
                    superSixSdzManager.calculateMeets();
                }
                catch(ValidationException ex)
                {
                    new MessageDlg( (String)ex.getMsg());
                }
            }
        }
        catch(ParseException e)
        {
            //nufin
        }
    }

    class CalendarActionListener implements MChangeListener
    {
        public void valueChanged(MChangeEvent event)
        {
            if(event.getType() == MChangeEvent.CHANGE)
            {
                maybeCreateMeets();
            }
        }
    }

    class NightComboActionListener implements ActionListener
    {
        int times;
        //String lastValue;

        public void actionPerformed(ActionEvent e)
        {
            JComboBox cb = (JComboBox)e.getSource();
            /*
             * Anything non-sdz the user is making the change.
             * Tacky but do-able. Did try to use item validation which
             * is intended to veto changes from dialog boxes. See comments
             * there (MaintainTeamTriggers).
             */
            if(!dt.strand.sdzHasControl())
            {
                maybeCreateMeets();
            }
            //lastValue = (String)cb.getSelectedItem();
        }
    }
    
    class TeamComboActionListener implements ActionListener
    {
        int times;
        //String lastValue;

        public void actionPerformed(ActionEvent e)
        {
            JComboBox cb = (JComboBox)e.getSource();
            if(!dt.strand.sdzHasControl())
            {
                teamCopier.copyTeam( cb);
            }
            //lastValue = (String)cb.getSelectedItem();
        }
    }
    
    class DivisionComboActionListener implements ActionListener
    {
        int times;
        //String lastValue;

        public void actionPerformed(ActionEvent e)
        {
            //JComboBox cb = (JComboBox)e.getSource();
            if(!dt.strand.sdzHasControl())
            {
                divisionTeamLookup.performLookup( true, this.getClass().getName() + ".actionPerformed()");
            }
            //lastValue = (String)cb.getSelectedItem();
        }
    }
}
