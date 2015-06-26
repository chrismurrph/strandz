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

import org.strandz.applic.supersix.reports.ReportLeagueTablesStrand;
import org.strandz.applic.supersix.reports.ReportMatchResultsStrand;
import org.strandz.applic.supersix.reports.ReportUnpaidTeamsStrand;
import org.strandz.lgpl.util.ValidationException;
import org.strandz.core.interf.VisibleStrandAction;
import org.strandz.core.applichousing.MenuTabApplication;
import org.strandz.data.supersix.business.Reports;
import org.strandz.data.supersix.business.SuperSixManagerI;
import org.strandz.data.supersix.business.SuperSixManagerUtils;
import org.strandz.data.supersix.objects.Division;
import org.strandz.data.supersix.objects.CompetitionSeason;
import org.strandz.lgpl.note.SuperSixNote;
import org.strandz.lgpl.store.DataStore;
import org.strandz.lgpl.store.LookupsI;
import org.strandz.lgpl.store.LookupsProviderI;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.ErrorMsgContainer;
import org.strandz.lgpl.util.MessageDlg;
import org.strandz.lgpl.util.PropertyUtils;
import org.strandz.lgpl.util.TaskTimeBandMonitorI;
import org.strandz.lgpl.util.TaskUtils;
import org.strandz.lgpl.widgets.WidgetUtils;
import org.strandz.store.supersix.SuperSixDataStoreFactory;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Properties;
import java.util.List;
import java.util.Iterator;

/**
 * Application Housing
 */
public class RunSuperSix
{
    /**
     * For a particular 'main' type of UI eg. different Application if
     * a different Controller were to be used.
     */
    MenuTabApplication aSupersix;
    private static final String MAINTAIN = "Maintain";
    private static final String ACTION = "Action";
    private static final String VIEW_EMAILS = "View Emails";
    private static final String SEND_EMAILS = "Send Emails";
    private static final String VISUAL_REPORTS = "Visual Reports";
    private static final String PRINTED_REPORTS = "Printed Reports";
    private static final String HELP = "Help";
    VisibleStrandAction vsa;
    private static int timesRun;

    public RunSuperSix()
    {
        this(null, new SuperSixDataStoreFactory(true).getEntityManagedDataStore());
    }
    
    private class LookupsProvider implements LookupsProviderI
    {
        private SuperSixManagerI superSixManager;
        
        LookupsProvider( SuperSixManagerI superSixManager)
        {
            Assert.notNull( superSixManager);
            this.superSixManager = superSixManager;
        }
        
        public LookupsI obtainLookups()
        {
            LookupsI result;
            int duration = superSixManager.getDataStore().getEstimatedLookupDataDuration();
            TaskTimeBandMonitorI lovMonitor = WidgetUtils.getTimeBandMonitor( duration);
            LookupQueryTask task = new LookupQueryTask( superSixManager, duration);
            ErrorMsgContainer err = new ErrorMsgContainer( "Calling setLOVs() for " + superSixManager.getDataStore().getName()); 
            lovMonitor.start(task, err);
            if(err.isInError)
            {
                Err.error( err.message);
            }
            lovMonitor.stop();
            result = task.getResult();
            return result;
        }
    }
    
    public RunSuperSix(JFrame frame, DataStore dataStore)
    {
        timesRun++;
        Err.pr(SuperSixNote.GENERIC, "RunSupersix being called " + timesRun + " times");
        if(timesRun == 2)
        {
            Err.error("Only RunSupersix once in same JVM");
        }
        aSupersix = new MenuTabApplication(dataStore);
        aSupersix.setCurrentItemTitle( dataStore.getName());
        String propFileName = "property-files/supersix";
        Properties props = PropertyUtils.getPortableProperties(propFileName, this);
        String titleText = PropertyUtils.getProperty("titleText", props);
        frame.setTitle(
            titleText + " ["
                + dataStore.getName() + "]");
        aSupersix.addTitle(MAINTAIN, 'm');
        aSupersix.addTitle(ACTION, 'a');
        aSupersix.addTitle(VIEW_EMAILS, 'v');
        aSupersix.addTitle(SEND_EMAILS, 's');
        aSupersix.addTitle(VISUAL_REPORTS, 'r');
        aSupersix.addTitle(PRINTED_REPORTS, 'p');
        aSupersix.addTitle(HELP, 'h');
        aSupersix.addSubTitle( MAINTAIN, TeamsAndPlayers.TEAMS_AND_PLAYERS_SUBMENU, 't');

        Reports reporter = new Reports();
        SuperSixManagerI superSixManager = SuperSixManagerUtils.getGlobalCurrentSuperSixManager();
        dataStore.setLookupsProvider( new LookupsProvider( superSixManager));
        //Doing now will force to retrieve lookups at beginning of app, so getting lookups
        //and connecting will happen together
        dataStore.getLookups();
        CompetitionSeason currentCompetitionSeason = superSixManager.getCurrentSeason( superSixManager.getGlobal());
        SuperSixManagerUtils.setCurrentCompetitionSeason(currentCompetitionSeason);
        
        SuperSixSdzManager superSixSdzManager = new SuperSixSdzManager( dataStore, aSupersix);
        {
            vsa = new VisibleStrandAction("Globals",
                                          RunSuperSix.MAINTAIN);

            MaintainGlobalStrand mgs = new MaintainGlobalStrand(aSupersix, superSixSdzManager);
            vsa.putValue(AbstractAction.MNEMONIC_KEY, new Integer(KeyEvent.VK_G));
            vsa.putValue(AbstractAction.SHORT_DESCRIPTION,
                         "Maintain Globals"
            );
            vsa.setVisibleStrand(mgs);
            aSupersix.addItem(vsa);
        }
        {
            vsa = new VisibleStrandAction("CompetitionSeason -> Rounds -> Games",
                                          RunSuperSix.MAINTAIN, 20843)
            {
                public void actionPerformed(ActionEvent evt)
                {
                    super.actionPerformed( evt);
                    Err.pr( "Accumulated: " + TaskUtils.getAccumulatedTime());
                }
            };

            MaintainSeasonMeetMatchStrand mss = new MaintainSeasonMeetMatchStrand(aSupersix, superSixSdzManager);
            vsa.putValue(AbstractAction.MNEMONIC_KEY, new Integer(KeyEvent.VK_S));
            vsa.putValue(AbstractAction.SHORT_DESCRIPTION,
                         "Maintain CompetitionSeason"
            );
            vsa.setVisibleStrand(mss);
            aSupersix.addItem(vsa);
        }
        superSixSdzManager.removeTeamsAndPlayersSubMenus();
        List divisions = currentCompetitionSeason.getDivisions();
        for(Iterator iterator = divisions.iterator(); iterator.hasNext();)
        {
            Division division = (Division) iterator.next();
            superSixSdzManager.addTeamsAndPlayersSubMenu( division);
        }
        //addTeamsAndPlayersSubMenu( superSixSdzManager, Division.DIVISION_MALE, 'M');
        //addTeamsAndPlayersSubMenu( superSixSdzManager, Division.DIVISION_FEMALE, 'F');
        /* Will be manually entering Draws for the first season
        {
            ActionListenerAction ala = new ActionListenerAction("Calculate Draws", ACTION);
            
            ala.putValue(AbstractAction.MNEMONIC_KEY, new Integer(KeyEvent.VK_C));
            ala.putValue(AbstractAction.SHORT_DESCRIPTION, "Calculate Draws Action");
            CalculateDrawsAction action = new CalculateDrawsAction( superSixSdzManager);
            ala.setActionListener( action);
            aSupersix.addItem( ala);
        }
        */
        {
            vsa = new VisibleStrandAction("Unpaid Teams", VISUAL_REPORTS);

            ReportUnpaidTeamsStrand ruts = new ReportUnpaidTeamsStrand(aSupersix);
            ruts.setReports(reporter);
            vsa.putValue(AbstractAction.MNEMONIC_KEY, new Integer(KeyEvent.VK_U));
            vsa.putValue(AbstractAction.SHORT_DESCRIPTION, "Unpaid Teams Report");
            vsa.setVisibleStrand(ruts);
            aSupersix.addItem(vsa);
        }
        //http://www.balmainsoccerclub.com/sixaside/2005/DrawMon.shtml
        {
            vsa = new VisibleStrandAction("Match Results Report", VISUAL_REPORTS);

            ReportMatchResultsStrand rmrs = new ReportMatchResultsStrand(aSupersix);
            rmrs.setReports(reporter);
            vsa.putValue(AbstractAction.MNEMONIC_KEY, new Integer(KeyEvent.VK_M));
            vsa.putValue(AbstractAction.SHORT_DESCRIPTION, "Match Results Report");
            vsa.setVisibleStrand(rmrs);
            aSupersix.addItem(vsa);
        }
        //http://www.balmainsoccerclub.com/sixaside/2005/tables.shtml
        {
            vsa = new VisibleStrandAction("League Tables Report", VISUAL_REPORTS);

            ReportLeagueTablesStrand rlts = new ReportLeagueTablesStrand(aSupersix);
            rlts.setReports(reporter);
            vsa.putValue(AbstractAction.MNEMONIC_KEY, new Integer(KeyEvent.VK_L));
            vsa.putValue(AbstractAction.SHORT_DESCRIPTION, "League Tables Report");
            vsa.setVisibleStrand(rlts);
            aSupersix.addItem(vsa);
        }
        if(frame != null)
        {
            aSupersix.display();
        }
    }
    
    private static class CalculateDrawsAction extends AbstractAction
    {
        SuperSixSdzManager superSixSdzManager;
                
        private CalculateDrawsAction( SuperSixSdzManager superSixSdzManager)
        {
            this.superSixSdzManager = superSixSdzManager;
        }
        
        public void actionPerformed(ActionEvent e)
        {
            try
            {
                superSixSdzManager.calculateDraws();
            }
            catch(ValidationException ex)
            {
                new MessageDlg( (String)ex.getMsg());
            }
        }
    }

    /**
     * @return true to indicate that the exit should be veto-ed
     */
    public boolean exitVetoNotification()
    {
        return aSupersix.exitVetoNotification();
    }
}
