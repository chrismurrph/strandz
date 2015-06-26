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
package org.strandz.applic.wombatrescue;

import org.strandz.applic.wombatrescue.reports.HelpAboutStrand;
import org.strandz.applic.wombatrescue.reports.HelpLicenseStrand;
import org.strandz.applic.wombatrescue.reports.HelpManualStrand;
import org.strandz.applic.wombatrescue.reports.ReportAddressesStrand;
import org.strandz.applic.wombatrescue.reports.ReportRosterableStrand;
import org.strandz.applic.wombatrescue.reports.ReportAllStrand;
import org.strandz.applic.wombatrescue.reports.ReportBuddyManagersEmailsStrand;
import org.strandz.applic.wombatrescue.reports.ReportBuddyManagersStrand;
import org.strandz.applic.wombatrescue.reports.ReportGroupVolunteersStrand;
import org.strandz.applic.wombatrescue.reports.ReportMalesStrand;
import org.strandz.applic.wombatrescue.reports.ReportNewVolunteersStrand;
import org.strandz.applic.wombatrescue.reports.ReportNotOnYahooEmailsOnlyStrand;
import org.strandz.applic.wombatrescue.reports.ReportNotOnYahooStrand;
import org.strandz.applic.wombatrescue.reports.ReportPhoneNumbersStrand;
import org.strandz.applic.wombatrescue.reports.ReportUnknownFlexibilityStrand;
import org.strandz.applic.wombatrescue.reports.SendEmailsMonthStrand;
import org.strandz.applic.wombatrescue.reports.SendNoOvernightsRequestStrand;
import org.strandz.applic.wombatrescue.reports.SendPhoneNumberRequestStrand;
import org.strandz.applic.wombatrescue.reports.ViewEmailsMonthStrand;
import org.strandz.applic.wombatrescue.reports.ViewNoOvernightsRequestStrand;
import org.strandz.applic.wombatrescue.reports.ViewPhoneNumberRequestStrand;
import org.strandz.applic.wombatrescue.reports.ViewUnrosteredVolsStrand;
import org.strandz.applic.wombatrescue.reports.ReportUnknownFlexibilityEmailsOnlyStrand;
import org.strandz.applic.wombatrescue.reports.ReportOvernightCapableStrand;
import org.strandz.applic.wombatrescue.reports.ReportOvernightCapableEmailsOnlyStrand;
import org.strandz.applic.wombatrescue.reports.ViewUnrosteredVolsEmailsOnlyStrand;
import org.strandz.util.applic.wombatrescue.WombatConstants;
import org.strandz.core.interf.VisibleStrandAction;
import org.strandz.core.interf.VisibleStrandI;
import org.strandz.core.interf.Node;
import org.strandz.core.applichousing.MenuTabApplication;
import org.strandz.core.applichousing.VisibleStrand;
import org.strandz.data.wombatrescue.business.ParticularRosterFactory;
import org.strandz.data.wombatrescue.business.ParticularRosterI;
import org.strandz.data.wombatrescue.business.Reports;
import org.strandz.data.wombatrescue.business.RosterSessionUtils;
import org.strandz.data.wombatrescue.business.RostererSessionFactory;
import org.strandz.data.wombatrescue.business.RostererSessionI;
import org.strandz.data.wombatrescue.util.RosteringConstants;
import org.strandz.data.wombatrescue.domain.WombatDomainQueryEnum;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.note.WombatNote;
import org.strandz.lgpl.store.DataStore;
import org.strandz.lgpl.store.LookupsI;
import org.strandz.lgpl.store.LookupsProviderI;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.ErrorMsgContainer;
import org.strandz.lgpl.util.PropertyUtils;
import org.strandz.lgpl.util.TaskTimeBandMonitorI;
import org.strandz.lgpl.util.TaskUtils;
import org.strandz.lgpl.widgets.WidgetUtils;
import org.strandz.store.wombatrescue.WombatDataStoreFactory;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.text.DefaultEditorKit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Properties;

/**
 * Application Housing is greated and configured here
 */
public class RunWombatrescue
{
    VisibleStrandI actionableStrand;
    RosterWorkersStrand rosterWorkersStrand;
    RosterWorkersStrand unrosterableWorkersStrand;
    
    /**
     * For a particular 'main' type of UI eg. different Application if
     * a different Controller were to be used.
     */
    MenuTabApplication aRoster;
    private static final String MAINTAIN = "Maintain";
    private static final String ROSTER = "Roster";
    private static final String VIEW_EMAILS = "View Emails";
    private static final String SEND_EMAILS = "Send Emails";
    private static final String REPORTS = "Reports";
    private static final String HELP = "Help";
    //
    private static final boolean ROSTER_BY_TABLE = false;
    private static final int TABLE_MAX_TIME = 41188;
    VisibleStrandAction vsa;
    private static int times;
    
    
    public RunWombatrescue()
    {
        this(null, new WombatDataStoreFactory(true).getEntityManagedDataStore());
//Doesn't seem to do anything
//        ClientPersistenceManagerFactory cpmf =
//                ((ClientPersistenceManagerFactory)
//                        JDOHelper.getPersistenceManagerFactory(System.getProperties()));
//        cpmf.addTransferListener(new MyTransferListener());
    }
    
    private class LookupsProvider implements LookupsProviderI
    {
        //private ParticularRosterI currentBO;
        private RostererSessionI rostererSession;
        
        LookupsProvider( /*ParticularRosterI currentBO,*/ RostererSessionI rostererSession)
        {
            Assert.notNull( rostererSession);
            //this.currentBO = currentBO;
            this.rostererSession = rostererSession;
        }
        
        public LookupsI obtainLookups()
        {
            LookupsI result;
            int duration = rostererSession.getDataStore().getEstimatedLookupDataDuration();
            TaskTimeBandMonitorI lovMonitor = WidgetUtils.getTimeBandMonitor( duration);
            LookupQueryTask task = new LookupQueryTask( rostererSession, duration);
            ErrorMsgContainer err = new ErrorMsgContainer( "Calling setLOVs() for " + rostererSession.getDataStore().getName()); 
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
    
    void accumulatedMessage( WombatDomainQueryEnum queryEnum)
    {
        accumulatedMessage( queryEnum.getDescription());
    }

    void accumulatedMessage( String taskName)
    {
        Err.pr( SdzNote.ACCUMULATED, "Accumulated to <" + taskName + "> is: " + TaskUtils.getAccumulatedTime());
    }
    
    public RunWombatrescue(JFrame frame, DataStore dataStore)
    {
        times++;
        Err.pr(WombatNote.GENERIC, "RunWombatrescue being called " + times + " times");
        if(times == 2)
        {
            Err.error("Only RunWombatrescue once in same JVM");
        }

        //DataStoreFactory factory = new WombatDataStoreFactory( true);
        //DataStore dataStore = factory.getDataStore();
        aRoster = new MenuTabApplication(dataStore);
        // Err.pr( "Will set title to " + WombatrescueApplicationData.getSetInstance().getName());
        aRoster.setCurrentItemTitle( // WombatrescueApplicationData.databaseName
            dataStore.getName());
        String propFileName = WombatConstants.PROPERTY_FILE_NAME;
        Properties props = PropertyUtils.getPortableProperties(propFileName, this);
        String titleText = PropertyUtils.getProperty("titleText", props);
        frame.setTitle(
            titleText + " ["
                + dataStore.getName() + "]");
        aRoster.addTitle(MAINTAIN, 'm');
        aRoster.addTitle(ROSTER, 'r');
        aRoster.addTitle(VIEW_EMAILS, 'v');
        aRoster.addTitle(SEND_EMAILS, 's');
        aRoster.addTitle(REPORTS, 'r');
        aRoster.addTitle(HELP, 'h');
        //aRoster.addTitle(EDIT, 'e');

        ParticularRosterI currentRosterBO = ParticularRosterFactory.newParticularRoster( RosteringConstants.CURRENT_STR);
        RostererSessionI rostererSession = RostererSessionFactory.newRostererSession( 
                RostererSessionFactory.WITH_SERVICE);
        rostererSession.init( dataStore);
        RosterSessionUtils.setGlobalCurrentParticularRoster(currentRosterBO); 
        Reports reporter = new Reports();
        currentRosterBO.init(rostererSession, dataStore);
        LookupsProvider provider = new LookupsProvider( rostererSession); 
        dataStore.setLookupsProvider( provider);
  
        int rosterableWorkersTime = 7450; //59781; Old was Kodo, new is Cayenne
        BetweenRosterSelector betweenRosterSelector = new BetweenRosterSelector();
        String triggersTitle;
        {
            triggersTitle = WombatDomainQueryEnum.ROSTERABLE_WORKERS.getDescription();
            vsa = new VisibleStrandAction(triggersTitle, MAINTAIN, rosterableWorkersTime)            
            {
                public void actionPerformed(ActionEvent evt)
                {
                    super.actionPerformed( evt);
                    accumulatedMessage( WombatDomainQueryEnum.ROSTERABLE_WORKERS);
                }
            };

            rosterWorkersStrand = new RosterWorkersStrand( triggersTitle, aRoster, betweenRosterSelector);
            vsa.putValue(AbstractAction.MNEMONIC_KEY, new Integer(KeyEvent.VK_R));
            vsa.putValue(AbstractAction.SHORT_DESCRIPTION,
                WombatDomainQueryEnum.ROSTERABLE_WORKERS.getDescription());
            vsa.setVisibleStrand(rosterWorkersStrand);
            aRoster.addItem(vsa);
        }
        {
            triggersTitle = WombatDomainQueryEnum.UNROSTERABLE_WORKERS.getDescription();
            vsa = new VisibleStrandAction(triggersTitle, MAINTAIN, rosterableWorkersTime);

            unrosterableWorkersStrand = new RosterWorkersStrand(triggersTitle, aRoster, betweenRosterSelector);
            vsa.putValue(AbstractAction.MNEMONIC_KEY, new Integer(KeyEvent.VK_U));
            vsa.putValue(AbstractAction.SHORT_DESCRIPTION,
                WombatDomainQueryEnum.UNROSTERABLE_WORKERS.getDescription());
            vsa.setVisibleStrand(unrosterableWorkersStrand);
            aRoster.addItem(vsa);
        }
        {
            triggersTitle = WombatDomainQueryEnum.GROUP_WORKERS.getDescription();
            vsa = new VisibleStrandAction(triggersTitle, MAINTAIN, 3047);

            RosterWorkersStrand groupWorkersStrand = new RosterWorkersStrand( triggersTitle, aRoster, betweenRosterSelector);
            vsa.putValue(AbstractAction.MNEMONIC_KEY, new Integer(KeyEvent.VK_G));
            vsa.putValue(AbstractAction.SHORT_DESCRIPTION,
                WombatDomainQueryEnum.GROUP_WORKERS.getDescription());
            vsa.setVisibleStrand(groupWorkersStrand);
            aRoster.addItem(vsa);
        }
        {
            vsa = new VisibleStrandAction(WombatDomainQueryEnum.BUDDY_MANAGERS.getDescription(), MAINTAIN, 79093);
            vsa.setAdorned( false);

            BuddyManagerStrand buddyManagerStrand = new BuddyManagerStrand(aRoster, dataStore);
            vsa.putValue(AbstractAction.MNEMONIC_KEY, new Integer(KeyEvent.VK_A));
            vsa.putValue(AbstractAction.SHORT_DESCRIPTION, WombatDomainQueryEnum.BUDDY_MANAGERS.getDescription());
            vsa.setVisibleStrand(buddyManagerStrand);
            aRoster.addItem(vsa);
        }
        {
            final String currentRosterMsg = "THIS - " + currentRosterBO.getMonth(RosteringConstants.CURRENT);
            vsa = new VisibleStrandAction( currentRosterMsg, ROSTER, 5344)
            {
                public void actionPerformed(ActionEvent evt)
                {
                    super.actionPerformed( evt);
                    accumulatedMessage( currentRosterMsg);
                }
            };

            PrintRosterStrand prs = new PrintRosterStrand(aRoster);
            currentRosterBO.setAtMonth(RosteringConstants.CURRENT);
            prs.setBusinessObjects(currentRosterBO, rostererSession);
            vsa.putValue(AbstractAction.MNEMONIC_KEY, new Integer(KeyEvent.VK_T));
            vsa.setVisibleStrand(prs);
            aRoster.addItem(vsa);
        }
        if(ROSTER_BY_TABLE)
        {
            final String currentRosterMsg = "THIS - " + currentRosterBO.getMonth(RosteringConstants.CURRENT) + " (New)";
            vsa = new VisibleStrandAction( currentRosterMsg, ROSTER, TABLE_MAX_TIME)
            {
                public void actionPerformed(ActionEvent evt)
                {
                    super.actionPerformed( evt);
                    accumulatedMessage( currentRosterMsg);
                }
            };
            vsa.setAdorned( false);

            TheRosterStrand theRosterStrand = new TheRosterStrand(aRoster, rosterWorkersStrand, false);
            currentRosterBO.setAtMonth(RosteringConstants.CURRENT);
            theRosterStrand.setBusinessObjects(currentRosterBO, rostererSession);
            vsa.putValue(AbstractAction.MNEMONIC_KEY, new Integer(KeyEvent.VK_T));
            vsa.setVisibleStrand(theRosterStrand);
            aRoster.addItem(vsa);
        }
        {
            ParticularRosterI nextBO = ParticularRosterFactory.newParticularRoster( RosteringConstants.NEXT_STR);
            nextBO.init(rostererSession, dataStore);
            {
                final String nextRosterMsg = RosteringConstants.NEXT_STR + " - " + nextBO.getMonth(RosteringConstants.NEXT);
                vsa = new VisibleStrandAction( nextRosterMsg, ROSTER, 6672)
                {
                    public void actionPerformed(ActionEvent evt)
                    {
                        super.actionPerformed( evt);
                        accumulatedMessage( nextRosterMsg);
                    }
                };
    
                PrintRosterStrand prs = new PrintRosterStrand(aRoster);
                nextBO.setAtMonth(RosteringConstants.NEXT);
                prs.setBusinessObjects( nextBO, rostererSession);
                vsa.putValue(AbstractAction.MNEMONIC_KEY, new Integer(KeyEvent.VK_N));
                vsa.setVisibleStrand(prs);
                aRoster.addItem(vsa);
            }
            if(ROSTER_BY_TABLE)
            {
                final String currentRosterMsg = RosteringConstants.NEXT_STR + " - " + nextBO.getMonth(RosteringConstants.NEXT) + " (New)";
                vsa = new VisibleStrandAction( currentRosterMsg, ROSTER, TABLE_MAX_TIME)
                {
                    public void actionPerformed(ActionEvent evt)
                    {
                        super.actionPerformed( evt);
                        accumulatedMessage( currentRosterMsg);
                    }
                };
                vsa.setAdorned( false);
    
                TheRosterStrand theRosterStrand = new TheRosterStrand(aRoster, rosterWorkersStrand, false);
                nextBO.setAtMonth(RosteringConstants.NEXT);
                theRosterStrand.setBusinessObjects(nextBO, rostererSession);
                vsa.putValue(AbstractAction.MNEMONIC_KEY, new Integer(KeyEvent.VK_N));
                vsa.setVisibleStrand(theRosterStrand);
                aRoster.addItem(vsa);
            }
        }
        {
            ParticularRosterI oneAfterBO = ParticularRosterFactory.newParticularRoster( RosteringConstants.ONE_AFTER_STR);
            oneAfterBO.init(rostererSession, dataStore);
            {
                vsa = new VisibleStrandAction(
                    RosteringConstants.ONE_AFTER_STR + " - "
                        + oneAfterBO.getMonth(RosteringConstants.ONE_AFTER),
                    ROSTER, 6673);
    
                PrintRosterStrand prs = new PrintRosterStrand(aRoster);
                oneAfterBO.setAtMonth(RosteringConstants.ONE_AFTER);
                prs.setBusinessObjects( oneAfterBO, rostererSession);
                vsa.putValue(AbstractAction.MNEMONIC_KEY, new Integer(KeyEvent.VK_N));
                vsa.setVisibleStrand(prs);
                aRoster.addItem(vsa);
            }
            if(ROSTER_BY_TABLE)
            {
                vsa = new VisibleStrandAction(
                    RosteringConstants.ONE_AFTER_STR + " - "
                        + oneAfterBO.getMonth(RosteringConstants.ONE_AFTER) + " (New)",
                    ROSTER, TABLE_MAX_TIME);
                vsa.setAdorned( false);
    
                TheRosterStrand theRosterStrand = new TheRosterStrand(aRoster, rosterWorkersStrand, false);
                oneAfterBO.setAtMonth(RosteringConstants.ONE_AFTER);
                theRosterStrand.setBusinessObjects(oneAfterBO, rostererSession);
                vsa.putValue(AbstractAction.MNEMONIC_KEY, new Integer(KeyEvent.VK_N));
                vsa.setVisibleStrand(theRosterStrand);
                aRoster.addItem(vsa);
            }
        }
        {
            ParticularRosterI justGoneBO = ParticularRosterFactory.newParticularRoster( RosteringConstants.JUST_GONE_STR);
            justGoneBO.init(rostererSession, dataStore);
            {
                vsa = new VisibleStrandAction(
                    "" + justGoneBO.getMonth(RosteringConstants.JUST_GONE), ROSTER);
    
                PrintRosterStrand prs = new PrintRosterStrand(aRoster);
                justGoneBO.setAtMonth(RosteringConstants.JUST_GONE);
                prs.setBusinessObjects( justGoneBO, rostererSession);
                vsa.putValue(AbstractAction.MNEMONIC_KEY, new Integer(KeyEvent.VK_G));
                vsa.setVisibleStrand(prs);
                aRoster.addItem(vsa);
            }
            if(ROSTER_BY_TABLE)
            {
                vsa = new VisibleStrandAction(
                    "" + justGoneBO.getMonth(RosteringConstants.JUST_GONE) + " (New)", ROSTER);
                vsa.setAdorned( false);
    
                TheRosterStrand theRosterStrand = new TheRosterStrand(aRoster, rosterWorkersStrand, false);
                justGoneBO.setAtMonth(RosteringConstants.JUST_GONE);
                theRosterStrand.setBusinessObjects(justGoneBO, rostererSession);
                vsa.putValue(AbstractAction.MNEMONIC_KEY, new Integer(KeyEvent.VK_G));
                vsa.setVisibleStrand(theRosterStrand);
                aRoster.addItem(vsa);
            }
        }

        /* Not used
        {
            vsa = new VisibleStrandAction("View Rostered no email", ROSTER);

            ViewRosteredNoEmailStrand rne = new ViewRosteredNoEmailStrand(aRoster);
            //rne.setBusinessObject( RosterSessionUtils.getGlobalCurrentParticularRoster());
            vsa.putValue(AbstractAction.MNEMONIC_KEY, new Integer(KeyEvent.VK_P));
            vsa.putValue(AbstractAction.SHORT_DESCRIPTION, "No Email");
            vsa.setVisibleStrand(rne);
            aRoster.addItem(vsa);
        }
        */
        /* Not used
        {
          vsa = new VisibleStrandAction( "Week Ahead", viewEmails);

          ViewEmailsWeekStrand ves = new ViewEmailsWeekStrand( aRoster);
          ves.setBusinessObject( currentRosterBO);
          vsa.putValue( AbstractAction.MNEMONIC_KEY, new Integer( KeyEvent.VK_V));
          vsa.putValue( AbstractAction.SHORT_DESCRIPTION, "Week");
          vsa.setVisibleStrand( ves);
          aRoster.addItem( vsa);
        }
        */
        {
            vsa = new VisibleStrandAction("Rostering Month", VIEW_EMAILS, 13906);

            ViewEmailsMonthStrand vem = new ViewEmailsMonthStrand(aRoster);
            //vem.setBusinessObject( RosterSessionUtils.getGlobalCurrentParticularRoster());
            vem.setReports(reporter);
            vsa.putValue(AbstractAction.MNEMONIC_KEY, new Integer(KeyEvent.VK_V));
            vsa.putValue(AbstractAction.SHORT_DESCRIPTION, "Month");
            vsa.setVisibleStrand(vem);
            aRoster.addItem(vsa);
        }
        /* Not used
        {
          vsa = new VisibleStrandAction( "Raffle/Address", viewEmails);

          ViewEmailsRaffleAddressStrand vem = new ViewEmailsRaffleAddressStrand(
              aRoster);
          vem.setBusinessObject( currentRosterBO);
          vsa.putValue( AbstractAction.MNEMONIC_KEY, new Integer( KeyEvent.VK_V));
          vsa.putValue( AbstractAction.SHORT_DESCRIPTION, "Address");
          vsa.setVisibleStrand( vem);
          aRoster.addItem( vsa);
        }
        */
        {
            vsa = new VisibleStrandAction("Phone Numbers", VIEW_EMAILS);

            ViewPhoneNumberRequestStrand vpm = new ViewPhoneNumberRequestStrand(
                aRoster);
            //vpm.setBusinessObject( RosterSessionUtils.getGlobalCurrentParticularRoster());
            vsa.putValue(AbstractAction.MNEMONIC_KEY, new Integer(KeyEvent.VK_P));
            vsa.putValue(AbstractAction.SHORT_DESCRIPTION, "Email to use to request a phone number");
            vsa.setVisibleStrand(vpm);
            aRoster.addItem(vsa);
        }
        /*
         * Wasn't presenting a filled in roster, and besides not used
        {
            vsa = new VisibleStrandAction("Unrostered Status Request", VIEW_EMAILS);

            ViewStatusRequestStrand vpm = new ViewStatusRequestStrand(
                aRoster);
            vsa.putValue(AbstractAction.MNEMONIC_KEY, new Integer(KeyEvent.VK_U));
            vsa.putValue(AbstractAction.SHORT_DESCRIPTION, "Unrostered Status");
            vsa.setVisibleStrand(vpm);
            aRoster.addItem(vsa);
        }
        */
        {
            vsa = new VisibleStrandAction("No Overnights", VIEW_EMAILS);

            ViewNoOvernightsRequestStrand vpm = new ViewNoOvernightsRequestStrand(
                aRoster);
            //vpm.setBusinessObject( RosterSessionUtils.getGlobalCurrentParticularRoster());
            vsa.putValue(AbstractAction.MNEMONIC_KEY, new Integer(KeyEvent.VK_N));
            vsa.putValue(AbstractAction.SHORT_DESCRIPTION, "No Overnights");
            vsa.setVisibleStrand(vpm);
            aRoster.addItem(vsa);
        }
        {
            vsa = new VisibleStrandAction("About", HELP);

            HelpAboutStrand has = new HelpAboutStrand(
                aRoster);
            vsa.putValue(AbstractAction.MNEMONIC_KEY, new Integer(KeyEvent.VK_A));
            vsa.putValue(AbstractAction.SHORT_DESCRIPTION, "About");
            vsa.setVisibleStrand(has);
            aRoster.addItem(vsa);
        }
        {
            vsa = new VisibleStrandAction("License", HELP);

            HelpLicenseStrand hls = new HelpLicenseStrand(
                aRoster);
            vsa.putValue(AbstractAction.MNEMONIC_KEY, new Integer(KeyEvent.VK_L));
            vsa.putValue(AbstractAction.SHORT_DESCRIPTION, "License");
            vsa.setVisibleStrand(hls);
            aRoster.addItem(vsa);
        }
        {
            vsa = new VisibleStrandAction("Manual", HELP);

            HelpManualStrand hls = new HelpManualStrand(
                aRoster);
            vsa.putValue(AbstractAction.MNEMONIC_KEY, new Integer(KeyEvent.VK_M));
            vsa.putValue(AbstractAction.SHORT_DESCRIPTION, "Manual");
            vsa.setVisibleStrand(hls);
            aRoster.addItem(vsa);
        }
        /* Was more for experiment than use - user can use keys
        {
            DefaultEditorKit.CopyAction copyAction = new MyCopyAction();
            copyAction.putValue(VisibleStrandAction.MASTER_MENU_TITLE, EDIT);
            aRoster.addAction(copyAction);
            DefaultEditorKit.PasteAction pasteAction = new MyPasteAction();
            pasteAction.putValue(VisibleStrandAction.MASTER_MENU_TITLE, EDIT);
            aRoster.addAction(pasteAction);
        }
        */
        /* Not used
        {
          vsa = new VisibleStrandAction( "Week Ahead", sendEmails);

          SendEmailsWeekStrand sew = new SendEmailsWeekStrand( aRoster);
          sew.setBusinessObject( currentRosterBO);
          vsa.putValue( AbstractAction.MNEMONIC_KEY, new Integer( KeyEvent.VK_S));
          vsa.putValue( AbstractAction.SHORT_DESCRIPTION, "Week");
          vsa.setVisibleStrand( sew);
          aRoster.addItem( vsa);
        }
        */
        {
            vsa = new VisibleStrandAction("Rostering Month", SEND_EMAILS);

            SendEmailsMonthStrand sem = new SendEmailsMonthStrand(aRoster);
            //sem.setBusinessObject( RosterSessionUtils.getGlobalCurrentParticularRoster());
            sem.setReports(reporter);
            vsa.putValue(AbstractAction.MNEMONIC_KEY, new Integer(KeyEvent.VK_S));
            vsa.putValue(AbstractAction.SHORT_DESCRIPTION, "Month");
            vsa.setVisibleStrand(sem);
            aRoster.addItem(vsa);
        }
        /* Not used
        {
          vsa = new VisibleStrandAction( "Raffle/Address", sendEmails);

          SendEmailsRaffleAddressStrand sem = new SendEmailsRaffleAddressStrand(
              aRoster);
          sem.setBusinessObject( currentRosterBO);
          vsa.putValue( AbstractAction.MNEMONIC_KEY, new Integer( KeyEvent.VK_S));
          vsa.putValue( AbstractAction.SHORT_DESCRIPTION, "Address");
          vsa.setVisibleStrand( sem);
          aRoster.addItem( vsa);
        }
        */
        {
            vsa = new VisibleStrandAction("Phone Numbers", SEND_EMAILS);

            SendPhoneNumberRequestStrand spm = new SendPhoneNumberRequestStrand(
                aRoster);
            //spm.setBusinessObject( RosterSessionUtils.getGlobalCurrentParticularRoster());
            vsa.putValue(AbstractAction.MNEMONIC_KEY, new Integer(KeyEvent.VK_P));
            vsa.putValue(AbstractAction.SHORT_DESCRIPTION, "Phone");
            vsa.setVisibleStrand(spm);
            aRoster.addItem(vsa);
        }
        /*
         * Wasn't presenting a filled in roster, and besides not used
        {
            vsa = new VisibleStrandAction("Unrostered Status Request", SEND_EMAILS);

            SendStatusRequestStrand spm = new SendStatusRequestStrand(
                aRoster);
            vsa.putValue(AbstractAction.MNEMONIC_KEY, new Integer(KeyEvent.VK_U));
            vsa.putValue(AbstractAction.SHORT_DESCRIPTION, "Unrostered Status");
            vsa.setVisibleStrand(spm);
            aRoster.addItem(vsa);
        }
        */
        {
            vsa = new VisibleStrandAction("No Overnights", SEND_EMAILS);

            SendNoOvernightsRequestStrand spm = new SendNoOvernightsRequestStrand(
                aRoster);
            //spm.setBusinessObject( RosterSessionUtils.getGlobalCurrentParticularRoster());
            vsa.putValue(AbstractAction.MNEMONIC_KEY, new Integer(KeyEvent.VK_N));
            vsa.putValue(AbstractAction.SHORT_DESCRIPTION, "No Overnights");
            vsa.setVisibleStrand(spm);
            aRoster.addItem(vsa);
        }


        {
            vsa = new VisibleStrandAction("Active Workers Info (CSV)", REPORTS);

            ReportRosterableStrand rar = new ReportRosterableStrand(aRoster);
            rar.setReports(reporter);
            vsa.putValue(AbstractAction.MNEMONIC_KEY, new Integer(KeyEvent.VK_A));
            vsa.putValue(AbstractAction.SHORT_DESCRIPTION, "All Info");
            vsa.setVisibleStrand(rar);
            aRoster.addItem(vsa);
        }
        {
            vsa = new VisibleStrandAction("Addresses", REPORTS);

            ReportAddressesStrand rem = new ReportAddressesStrand(aRoster);
            rem.setReports(reporter);
            vsa.putValue(AbstractAction.MNEMONIC_KEY, new Integer(KeyEvent.VK_A));
            vsa.putValue(AbstractAction.SHORT_DESCRIPTION, "Address");
            vsa.setVisibleStrand(rem);
            aRoster.addItem(vsa);
        }
        {
            vsa = new VisibleStrandAction("All Workers Info (CSV)", REPORTS);

            ReportAllStrand ras = new ReportAllStrand(aRoster);
            ras.setReports(reporter);
            vsa.putValue(AbstractAction.MNEMONIC_KEY, new Integer(KeyEvent.VK_A));
            vsa.putValue(AbstractAction.SHORT_DESCRIPTION, "All Workers Info");
            vsa.setVisibleStrand(ras);
            aRoster.addItem(vsa);
        }
        {
            vsa = new VisibleStrandAction("Buddy Managers", REPORTS);

            ReportBuddyManagersStrand rbm = new ReportBuddyManagersStrand(aRoster);
            rbm.setReports(reporter);
            vsa.putValue(AbstractAction.MNEMONIC_KEY, new Integer(KeyEvent.VK_M));
            vsa.putValue(AbstractAction.SHORT_DESCRIPTION, "Buddy Managers");
            vsa.setVisibleStrand(rbm);
            aRoster.addItem(vsa);
        }
        {
            vsa = new VisibleStrandAction("Buddy Managers Emails", REPORTS);

            ReportBuddyManagersEmailsStrand rbm = new ReportBuddyManagersEmailsStrand(
                aRoster);
            rbm.setReports(reporter);
            vsa.putValue(AbstractAction.MNEMONIC_KEY, new Integer(KeyEvent.VK_E));
            vsa.putValue(AbstractAction.SHORT_DESCRIPTION, "Buddy Managers Emails");
            vsa.setVisibleStrand(rbm);
            aRoster.addItem(vsa);
        }
        {
            vsa = new VisibleStrandAction("Current Wkrs Phone Numbers", REPORTS);

            ReportPhoneNumbersStrand rpn = new ReportPhoneNumbersStrand(aRoster);
            rpn.setReports(reporter);
            vsa.putValue(AbstractAction.MNEMONIC_KEY, new Integer(KeyEvent.VK_P));
            vsa.putValue(AbstractAction.SHORT_DESCRIPTION, "Phone");
            vsa.setVisibleStrand(rpn);
            aRoster.addItem(vsa);
        }
        {
            vsa = new VisibleStrandAction("Group Workers", REPORTS);

            ReportGroupVolunteersStrand rgv = new ReportGroupVolunteersStrand(aRoster);
            rgv.setReports(reporter);
            vsa.putValue(AbstractAction.MNEMONIC_KEY, new Integer(KeyEvent.VK_G));
            vsa.putValue(AbstractAction.SHORT_DESCRIPTION, "Group Workers");
            vsa.setVisibleStrand(rgv);
            aRoster.addItem(vsa);
        }
        {
            vsa = new VisibleStrandAction("Males (email only)", REPORTS);

            ReportMalesStrand rms = new ReportMalesStrand(
                aRoster);
            rms.setReports(reporter);
            vsa.putValue(AbstractAction.MNEMONIC_KEY, new Integer(KeyEvent.VK_A));
            vsa.putValue(AbstractAction.SHORT_DESCRIPTION,
                "Males (email only)");
            vsa.setVisibleStrand(rms);
            aRoster.addItem(vsa);
        }
        {
            vsa = new VisibleStrandAction("New Workers", REPORTS);

            ReportNewVolunteersStrand rnv = new ReportNewVolunteersStrand(aRoster);
            rnv.setReports(reporter);
            vsa.putValue(AbstractAction.MNEMONIC_KEY, new Integer(KeyEvent.VK_P));
            vsa.putValue(AbstractAction.SHORT_DESCRIPTION, "New Workers");
            vsa.setVisibleStrand(rnv);
            aRoster.addItem(vsa);
        }
        {
            vsa = new VisibleStrandAction("Not on Yahoo!", REPORTS);

            ReportNotOnYahooStrand rnoy = new ReportNotOnYahooStrand(aRoster);
            rnoy.setReports(reporter);
            vsa.putValue(AbstractAction.MNEMONIC_KEY, new Integer(KeyEvent.VK_P));
            vsa.putValue(AbstractAction.SHORT_DESCRIPTION, "Non-Yahoo!ers");
            vsa.setVisibleStrand(rnoy);
            aRoster.addItem(vsa);
        }
        /*
         * Report just says 'has not been implemented' so not much point in having
        {
            vsa = new VisibleStrandAction("Not in Database", REPORTS);

            ReportNotInDBStrand rnid = new ReportNotInDBStrand( aRoster);
            rnid.setReports(reporter);
            vsa.putValue(AbstractAction.MNEMONIC_KEY, new Integer(KeyEvent.VK_P));
            vsa.putValue(AbstractAction.SHORT_DESCRIPTION, "In Yahoo! but not in the DB");
            vsa.setVisibleStrand(rnid);
            aRoster.addItem(vsa);
        }
        */
        {
            vsa = new VisibleStrandAction("Not on Yahoo! (emails only)", REPORTS);

            ReportNotOnYahooEmailsOnlyStrand rnoyeo = new ReportNotOnYahooEmailsOnlyStrand(
                aRoster);
            rnoyeo.setReports(reporter);
            vsa.putValue(AbstractAction.MNEMONIC_KEY, new Integer(KeyEvent.VK_P));
            vsa.putValue(AbstractAction.SHORT_DESCRIPTION,
                "Non-Yahoo!ers (email only)");
            vsa.setVisibleStrand(rnoyeo);
            aRoster.addItem(vsa);
        }

        {
            vsa = new VisibleStrandAction("Overnight Capable", REPORTS);

            ReportOvernightCapableStrand ruf = new ReportOvernightCapableStrand(
                aRoster);
            ruf.setReports(reporter);
            vsa.putValue(AbstractAction.MNEMONIC_KEY, new Integer(KeyEvent.VK_O));
            vsa.putValue(AbstractAction.SHORT_DESCRIPTION, "Overnight Capable");
            vsa.setVisibleStrand(ruf);
            aRoster.addItem(vsa);
        }
        {
            vsa = new VisibleStrandAction("Overnight Capable (emails only)", REPORTS);

            ReportOvernightCapableEmailsOnlyStrand ruf = new ReportOvernightCapableEmailsOnlyStrand(
                aRoster);
            ruf.setReports(reporter);
            vsa.putValue(AbstractAction.MNEMONIC_KEY, new Integer(KeyEvent.VK_O));
            vsa.putValue(AbstractAction.SHORT_DESCRIPTION, "Overnight Capable (emails only)");
            vsa.setVisibleStrand(ruf);
            aRoster.addItem(vsa);
        }
        {
            vsa = new VisibleStrandAction("Unknown Flexibility", REPORTS);

            ReportUnknownFlexibilityStrand ruf = new ReportUnknownFlexibilityStrand(
                aRoster);
            ruf.setReports(reporter);
            vsa.putValue(AbstractAction.MNEMONIC_KEY, new Integer(KeyEvent.VK_U));
            vsa.putValue(AbstractAction.SHORT_DESCRIPTION, "Unknown Flexibility");
            vsa.setVisibleStrand(ruf);
            aRoster.addItem(vsa);
        }
        {
            vsa = new VisibleStrandAction("Unknown Flexibility (emails only)", REPORTS);

            ReportUnknownFlexibilityEmailsOnlyStrand ruf = new ReportUnknownFlexibilityEmailsOnlyStrand(
                aRoster);
            ruf.setReports(reporter);
            vsa.putValue(AbstractAction.MNEMONIC_KEY, new Integer(KeyEvent.VK_F));
            vsa.putValue(AbstractAction.SHORT_DESCRIPTION, "Unknown Flexibility (emails only)");
            vsa.setVisibleStrand(ruf);
            aRoster.addItem(vsa);
        }
        {
            vsa = new VisibleStrandAction("Unrostered", REPORTS);

            ViewUnrosteredVolsStrand vu = new ViewUnrosteredVolsStrand(
                aRoster);
            vu.setReports( reporter);
            vsa.putValue(AbstractAction.MNEMONIC_KEY, new Integer(KeyEvent.VK_U));
            vsa.putValue(AbstractAction.SHORT_DESCRIPTION, "Unrostered");
            vsa.setVisibleStrand(vu);
            aRoster.addItem(vsa);
        }
        {
            vsa = new VisibleStrandAction("Unrostered (emails only)", REPORTS);

            ViewUnrosteredVolsEmailsOnlyStrand vu = new ViewUnrosteredVolsEmailsOnlyStrand(
                aRoster);
            vu.setReports( reporter);
            vsa.putValue(AbstractAction.MNEMONIC_KEY, new Integer(KeyEvent.VK_O));
            vsa.putValue(AbstractAction.SHORT_DESCRIPTION, "Unrostered (emails only)");
            vsa.setVisibleStrand(vu);
            aRoster.addItem(vsa);
        }
        /*
         * This query causes:
         * QUERY All Roster Slot ordered by Worker took 1157 when estimate was 1187, so took 97% of estimated
         * to happen many many times so fix this performance problem if ever put it back 
        {
            vsa = new VisibleStrandAction("Weekend Overnighters", REPORTS);

            ReportWeekendVolsStrand rwv = new ReportWeekendVolsStrand(aRoster);
            rwv.setReports(reporter);
            vsa.putValue(AbstractAction.MNEMONIC_KEY, new Integer(KeyEvent.VK_P));
            vsa.putValue(AbstractAction.SHORT_DESCRIPTION, "Weekenders");
            vsa.setVisibleStrand(rwv);
            aRoster.addItem(vsa);
        }
        */
        /*
         * This query causes:
         * QUERY Buddy Managers took 750 when estimate was 3094, so took 24% of estimated
         * to happen many many times so fix this performance problem if ever put it back 
        {
            vsa = new VisibleStrandAction("Workers with many Buddy Managers",
                                          REPORTS);

            ReportManyBuddiesStrand rmb = new ReportManyBuddiesStrand(aRoster);
            rmb.setReports(reporter);
            vsa.putValue(AbstractAction.MNEMONIC_KEY, new Integer(KeyEvent.VK_M));
            vsa.putValue(AbstractAction.SHORT_DESCRIPTION, "Many Buddy Managers");
            vsa.setVisibleStrand(rmb);
            aRoster.addItem(vsa);
        }
        */
        if(frame != null)
        {
            aRoster.display();
        }
    }
    
    private class MyCopyAction extends DefaultEditorKit.CopyAction
    {
        public void actionPerformed(ActionEvent e)
        {
            super.actionPerformed( e);
        }
    }
    
    private class BetweenRosterSelector implements SelectDifferentStrandI
    {
        public boolean unselectedAndChangedRosterability(String triggersTitle)
        {
            boolean result;
            Err.pr( SdzNote.ROSTERABILITY, "Change to rosterable made in " + triggersTitle + 
                    " so will query it and it's partner");
            VisibleStrand visibleStrands[] = new VisibleStrand[2];  
            if(triggersTitle.equals( WombatDomainQueryEnum.ROSTERABLE_WORKERS.getDescription()))
            {
                visibleStrands[0] = rosterWorkersStrand;
                visibleStrands[1] = unrosterableWorkersStrand;
            }
            else if(triggersTitle.equals( WombatDomainQueryEnum.UNROSTERABLE_WORKERS.getDescription()))
            {
                visibleStrands[0] = unrosterableWorkersStrand;
                visibleStrands[1] = rosterWorkersStrand;
            }
            result = visibleStrands[0].getSdzBagI().getStrand().COMMIT_RELOAD();
            if(result)
            {
                /*
                 * Commit and reload them both
                 */
                if(visibleStrands[1].getSdzBagI().getStrand().getCurrentNode() == null)
                {
                    Node firstNode = visibleStrands[1].getSdzBagI().getStrand().getNodes().get( 0);
                    firstNode.GOTO();
                }
                result = visibleStrands[1].getSdzBagI().getStrand().COMMIT_RELOAD();
            }
            else
            {
                Err.pr( visibleStrands[0].getSdzBagI().getStrand().getErrorMessage());
                Err.pr( "Failed to COMMIT_RELOAD on " + ((RosterWorkersStrand)visibleStrands[0]).getTitle());
            }
            return result;
        }
    }

    private class MyPasteAction extends DefaultEditorKit.PasteAction
    {
        public void actionPerformed(ActionEvent e)
        {
            super.actionPerformed( e);
        }
    }
    
    /**
     * @return true to indicate that the exit should be veto-ed
     */
    public boolean exitVetoNotification()
    {
        return aRoster.exitVetoNotification();
    }
}
