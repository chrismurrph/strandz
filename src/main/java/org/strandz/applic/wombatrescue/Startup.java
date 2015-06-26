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

import org.strandz.applic.util.HelpLicenseHelper;
import org.strandz.applic.wombatrescue.reports.HelpAboutHelper;
import org.strandz.applic.wombatrescue.reports.HelpManualHelper;
import org.strandz.data.wombatrescue.domain.WombatConnectionEnum;
import org.strandz.lgpl.store.DataStore;
import org.strandz.lgpl.store.DataStoreFactory;
import org.strandz.lgpl.store.DataStoreOpsUtils;
import org.strandz.lgpl.store.EntityManagedDataStore;
import org.strandz.lgpl.util.DialogEmbellisherI;
import org.strandz.lgpl.util.DisplayUtils;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.MessageDlg;
import org.strandz.lgpl.widgets.CustomDialog;
import org.strandz.store.wombatrescue.WombatDataStoreFactory;
import org.strandz.view.wombatrescue.StartupGUI;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

abstract class Startup extends StartupGUI
{
    private boolean splashScreen = true;
    public JMenuBar jmenu;
    public JMenu ident0;
    public JMenu ident1;
    public JMenu ident2;
    public JMenuItem maintainVols;
    public JMenuItem about;
    public JMenuItem license;
    public JMenuItem helpTopics;
    private String dialogCmd;
    DataStoreFactory dataStoreFactory;
    private RunWombatrescue run;

    //Keep these numbers as being divisible by 4 - that way 
    public static final int WIDTH = 892; // 790;
    public static final int HEIGHT = 700; // 650;//520;
    /*
    public Startup()
    {
    Err.pr( "In Startup");
    dataFactoryFactory = WombatrescueApplicationData.getInstance();
    dataFactoryFactory.fillProperties();
    init( dataFactoryFactory);
    }
    */

    void init()
    {
        //1.6 does not work with Windows L&F, so let's simplify with initial move to 1.6
        //DisplayUtils.setUI();
        Err.setBatch(false);

        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        WindowListener wl = new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                boolean leaveApp = true;
                if(run != null)
                {
                    //don't leave app if get a veto
                    leaveApp = !run.exitVetoNotification();
                }
                // temp comment out for simplicity
                //data.storeProperties();
                if(leaveApp) System.exit(0);
            }
        };
        addWindowListener(wl);
        // has a menu
        addStartupMenu();

        // menu can be listened to
        ActionAdapter aa = new ActionAdapter();
        transferListening(null, aa);
        dataStoreFactory = new WombatDataStoreFactory(); //subclasses will need to specify the connection
    }

    private class Dialoger implements DialogEmbellisherI
    {
        public boolean validate(String txt)
        {
            return true;
        }

        public void actionPerformed(ActionEvent e)
        {
            String pressed = e.getActionCommand();
            Err.pr(pressed);
            if(pressed == "Browse")
            {
                Err.error("Not yet implemented Browse");
            }
            dialogCmd = pressed;
        }
    }

    private String getExportedFilename()
    {
        //String result = data.getProperty( _PropertiesStore.BACKUP_FILE_PROP);
        String result = "C:\\sdz-zone\\data\\wombatrescue\\BackupWombatData.xml";
        Dialoger dialoger = new Dialoger();
        CustomDialog dialog = new CustomDialog
            (MessageDlg.getFrame(), dialoger, "Export File Name",
                "Name of the file to eXport to or iMport from", null, 40,
                new String[]{"Browse"}, null, result);
        dialog.pack();
        dialog.setLocationRelativeTo(MessageDlg.getFrame());
        dialog.setVisible(true);

        String txt = dialog.getValidatedText();
        if(txt != null && !txt.equals(""))
        {
            result = txt;
        }
        if(result == null)
        {
            Err.error("Should not return null from getExportedFilename()");
        }
        else
        {// Err.pr( "getBackupFilename() returning <" + result + ">");
        }
        return result;
    }

    private void addStartupMenu()
    {
        jmenu = new JMenuBar();
        jmenu.setName("jmenu");
        this.setJMenuBar(jmenu);
        ident0 = new JMenu("Entry Point");
        ident0.setMnemonic('e');
        ident0.setName("ident0");
        jmenu.add(ident0);
        maintainVols = new JMenuItem("Rostering Activity");
        maintainVols.setMnemonic('r');
        maintainVols.setName("rosteringActivity");
        ident0.add(maintainVols);
        //
        ident1 = new JMenu("Help");
        ident1.setMnemonic('h');
        ident1.setName("ident1");
        jmenu.add(ident1);

        about = new JMenuItem("About");
        about.setMnemonic('a');
        about.setName("About");
        ident1.add(about);
        license = new JMenuItem("License");
        license.setMnemonic('l');
        license.setName("License");
        ident1.add(license);
        helpTopics = new JMenuItem("Help Topics");
        helpTopics.setMnemonic('h');
        helpTopics.setName("Help Topics");
        ident1.add(helpTopics);
    }

    private void removeCurrentMenu()
    {
        if(isVisible() == false)
        {
            Startup.this.setJMenuBar(null);
            jmenu = null;
        }
        else
        {
            Err.error("frame must be invisible" + " to remove it's menu");
        }
    }

    private void transferListening(ActionListener from,
                                   ActionListener to)
    {
        if(from != null)
        {
            maintainVols.removeActionListener(from);
            about.removeActionListener(from);
            license.removeActionListener(from);
            helpTopics.removeActionListener(from);
        }
        if(to != null)
        {
            maintainVols.addActionListener(to);
            about.addActionListener(to);
            license.addActionListener(to);
            helpTopics.addActionListener(to);
        }
    }

    /**
     * Inherit this method to bring up a login to set credentials
     */
    public boolean setCredentials()
    {
        return true;     
    }

    class ActionAdapter implements ActionListener
    {
        private HelpManualHelper helpManualHelper;

        private ActionAdapter()
        {
            helpManualHelper = new HelpManualHelper();
        }

        private void redisplayAs(JPanel newPanel)
        {
            if(splashScreen)
            {
                setVisible(false);
                removeCurrentMenu();

                Container cp = getContentPane();
                cp.removeAll();
                cp.setLayout(new BorderLayout());
                cp.add(newPanel, BorderLayout.CENTER);
                MessageDlg.setDlgParent( newPanel);

                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                setLocation(screenSize.width / 2 - WIDTH / 2,
                    screenSize.height / 2 - HEIGHT / 2);
                setSize(WIDTH, HEIGHT);
                transferListening(this, null);
                setVisible(true);
                splashScreen = false;
            }
            else
            {
                Err.error("Must be Splash Screen to redisplay differently");
            }
        }
                
        public void actionPerformed(ActionEvent e)
        {
            if(e.getSource() == maintainVols)
            {
                boolean ok = setCredentials();
                if(ok)
                {
                    run = new RunWombatrescue(Startup.this, dataStoreFactory.getDataStore());
                    redisplayAs(run.aRoster.getEnclosingPanel());
                }
            }
            else if(e.getSource() == about)
            {
                HelpAboutHelper helper = new HelpAboutHelper();
                DisplayUtils.displayInAcknowledgeDialog(helper.getAcknowledgementsPanel(), "About " + titleText);
            }
            else if(e.getSource() == license)
            {
                JPanel licensePanel = new HelpLicenseHelper().getPanel();
                DisplayUtils.displayInAcknowledgeDialog(licensePanel, "License");
            }
            else if(e.getSource() == helpTopics)
            {
                //JHLauncher.main( new String[] { "-helpset", "WombatRescueHelp.hs"});
                helpManualHelper.displayHelp();
            }
            else
            {
                Err.error("Not yet implemented");
            }
        }
    }

    /*
    * Rest of file made up of interesting commands, so don't delete them, even
    * thou none of them currently called
    */

    private static void displayData(DataStore from)
    {
        DataStoreOpsUtils.viewData(from);
    }

    private void displayBackupData()
    {
        //XMLFileData from = WombatDataFactory.getXMLInstance2(
        //    WombatConnectionEnum.getFromName( getExportedFilename()));
        dataStoreFactory.addConnection(WombatConnectionEnum.XML);
        DataStore from = dataStoreFactory.getDataStoreSafely(1);
        if(!dialogCmd.equals(CustomDialog.CANCEL_BUTTON)
            && !dialogCmd.equals(CustomDialog.CANCEL_WINDOW))
        {
            DataStoreOpsUtils.viewData(from);
        }
        dataStoreFactory.removeConnection(WombatConnectionEnum.XML);
    }

    private void backupData()
    {
        String fileName = getExportedFilename();
        //XMLFileData to = WombatDataFactory.getXMLInstance2( WombatConnectionEnum.getFromName( fileName));
        dataStoreFactory.addConnection(WombatConnectionEnum.getFromName(fileName));
        DataStore to = dataStoreFactory.getDataStoreSafely(1);
        if(!dialogCmd.equals(CustomDialog.CANCEL_BUTTON)
            && !dialogCmd.equals(CustomDialog.CANCEL_WINDOW))
        {
            DataStore from = dataStoreFactory.getDataStore();
            DataStoreOpsUtils.transferData((EntityManagedDataStore)from, (EntityManagedDataStore)to);
        }
        dataStoreFactory.removeConnection(1);
    }
    /*
     * Nothing particularly wrong with having a dependency on task,
     * package, just makes it simpler (for demo downloading) if we don't
    private void restoreData()
    {
      XMLFileData from = WombatDataFactory.getXMLInstance2(
          WombatConnectionEnum.getFromName( getExportedFilename()));
      if(!dialogCmd.equals( CustomDialog.CANCEL_BUTTON)
          && !dialogCmd.equals( CustomDialog.CANCEL_WINDOW))
      {
        if(!(from instanceof XMLWombatData))
        {
          Err.error( "Normally restore from the archived XML file, not " + from);
        }

        DataStore to = WombatDataFactory.getSetInstance().getData();
        if(!(to instanceof JDOWombatDataStore))
        {
          Err.error( "Normally restore made to a JDO database, not " + to);
        }
        pDataStoreOps.transferData( from, to);
        MakeNullVolunteer.processParams( new String[] {
          "DevData", WombatDataFactory.getSetInstance().getDescription()});
      }
    }
    */

    private void prodToDev()
    {
        dataStoreFactory.addConnection(WombatConnectionEnum.PROD); //at 1 (default is at 0)
        dataStoreFactory.addConnection(WombatConnectionEnum.DEV); //at 2
        DataStore from = dataStoreFactory.getDataStoreSafely(1);
        DataStore to = dataStoreFactory.getDataStoreSafely(2);
        DataStoreOpsUtils.transferData((EntityManagedDataStore)from, (EntityManagedDataStore)to);
        //Just easier to always used fresh ones
        dataStoreFactory.removeConnection(WombatConnectionEnum.PROD);
        dataStoreFactory.removeConnection(WombatConnectionEnum.DEV);
    }

    private void replayLog()
    {
        RosterWorkersStrand rv = new RosterWorkersStrand();
        rv.sdzInit();
        rv.preForm();
        rv.getSbI().getStrand().replayRecorded("ml.xml", null);
        rv.getSbI().getStrand().COMMIT_ONLY();
    }
}
