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

import org.strandz.core.interf.Application;
import org.strandz.core.applichousing.RClickTabPopupHelper;
import org.strandz.data.supersix.objects.Competition;
import org.strandz.data.supersix.objects.SeasonYear;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.util.Err;
import org.strandz.view.util.DTUtils;

import javax.swing.JComboBox;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MaintainGlobalEvents
{
    private MaintainGlobalDT dt;
    private SuperSixSdzManager superSixSdzManager;
    private Application application;

    public MaintainGlobalEvents(MaintainGlobalDT dt, SuperSixSdzManager superSixSdzManager, Application application)
    {
        this.dt = dt;
        DTUtils.chkNotNull( dt.ui0.getCbSeasonYear());
        dt.ui0.getCbSeasonYear().addActionListener(
            new SeasonYearComboActionListener());
        DTUtils.chkNotNull( dt.ui0.getCbCompetition());
        dt.ui0.getCbCompetition().addActionListener(
            new CompetitionComboActionListener());
        this.superSixSdzManager = superSixSdzManager;
        this.application = application;
    }

    public class SeasonYearComboActionListener implements ActionListener
    {
        int times;
        String lastValue;
        boolean settingBack = false;

        public void actionPerformed(ActionEvent e)
        {
            if(!settingBack)
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
                    Err.pr( SdzNote.SDZ_HAS_CONTROL, "sdz does not have control for node " +
                        dt.globalNode + ", ID: " + dt.globalNode.getId());
//                    String msgs[] = new String[]{
//                            "Changing the Current Year will set all the Players",
//                            "to 'unpaid'. " +
//                            
//                            //"You will also need to restart the application for changes to take effect. " +
//                            "Are you sure you want to change the current year?"};
//                    int ret = MessageDlg.showConfirmDialog( msgs, "New Year");
//                    if(ret == JOptionPane.YES_OPTION)
                    {
                        application.removeDisplayedStrands(RClickTabPopupHelper.CLOSE_ALL_BUT_SELECTED);
                        SeasonYear seasonYear = SeasonYear.getFromName( (String)cb.getSelectedItem());
                        superSixSdzManager.changeCurrentSeasonYear( seasonYear);
                    }
//                    else
//                    {
//                        settingBack = true;
//                        cb.setSelectedItem( lastValue);
//                        settingBack = false;
//                    }
                }
                lastValue = (String)cb.getSelectedItem();
            }
        }
    }
    
    public class CompetitionComboActionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            JComboBox cb = (JComboBox)e.getSource();
            if(!dt.strand.sdzHasControl())
            {
                //Err.pr( SdzNote.lovsChangeDataSet, "current strand b4 removeDisplayedStrands is " + 
                //        application.getCurrentVisibleStrand().getClass().getName());
                application.removeDisplayedStrands(RClickTabPopupHelper.CLOSE_ALL_BUT_SELECTED);
                //Err.pr( SdzNote.lovsChangeDataSet, "current strand after removeDisplayedStrands is " + 
                //        application.getCurrentVisibleStrand().getClass().getName());
                Competition competition = Competition.getFromName( (String)cb.getSelectedItem());
                superSixSdzManager.changeCurrentCompetition( competition);
            }
        }
    }
}
