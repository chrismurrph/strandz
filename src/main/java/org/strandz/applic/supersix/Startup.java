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

import org.strandz.applic.supersix.reports.HelpAboutHelper;
import org.strandz.applic.supersix.reports.HelpManualHelper;
import org.strandz.applic.util.AbstractHelpManualHelper;
import org.strandz.applic.util.HelpLicenseHelper;
import org.strandz.lgpl.store.DataStoreFactory;
import org.strandz.lgpl.util.DisplayUtils;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.MessageDlg;
import org.strandz.store.supersix.SuperSixDataStoreFactory;
import org.strandz.view.supersix.StartupGUI;

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

class Startup extends StartupGUI
{
    private boolean splashScreen = true;
    public JMenuBar jmenu;
    public JMenu ident0;
    public JMenu ident1;
    public JMenuItem maintainEverything;
    public JMenuItem about;
    public JMenuItem license;
    public JMenuItem help;
    DataStoreFactory dataStoreFactory;
    private RunSuperSix run;
    Startup.ActionAdapter actionAdapter;

    public static final int WIDTH = 890; // 790;
    public static final int HEIGHT = 700; // 650;//520;
    
    void init()
    {
        DisplayUtils.setUI();
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
        actionAdapter = new Startup.ActionAdapter();
        transferListening(null, actionAdapter);
        dataStoreFactory = new SuperSixDataStoreFactory(); //subclasses will need to specify the connection
    }

//    private class Dialoger implements DialogEmbellisherI
//    {
//        public boolean validate(String txt)
//        {
//            return true;
//        }
//
//        public void actionPerformed(ActionEvent e)
//        {
//            String pressed = e.getActionCommand();
//            Err.pr(pressed);
//            if(pressed == "Browse")
//            {
//                Err.error("Not yet implemented Browse");
//            }
//            dialogCmd = pressed;
//        }
//    }

    private void addStartupMenu()
    {
        jmenu = new JMenuBar();
        jmenu.setName("jmenu");
        this.setJMenuBar(jmenu);
        ident0 = new JMenu("Entry Point");
        ident0.setMnemonic('e');
        ident0.setName("ident0");
        jmenu.add(ident0);
        maintainEverything = new JMenuItem("Start");
        maintainEverything.setMnemonic('r');
        maintainEverything.setName("start");
        ident0.add(maintainEverything);
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
        help = new JMenuItem("Help Topics");
        help.setMnemonic('l');
        help.setName("Help Topics");
        ident1.add(help);
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
            maintainEverything.removeActionListener(from);
            about.removeActionListener(from);
            license.removeActionListener(from);
            help.removeActionListener(from);
        }
        if(to != null)
        {
            maintainEverything.addActionListener(to);
            about.addActionListener(to);
            license.addActionListener(to);
            help.addActionListener(to);
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
        private AbstractHelpManualHelper helpManualHelper;

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
                setLocation(screenSize.width / 2 - Startup.WIDTH / 2,
                            screenSize.height / 2 - Startup.HEIGHT / 2);
                setSize(Startup.WIDTH, Startup.HEIGHT);
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
            if(e.getSource() == maintainEverything)
            {
                boolean ok = setCredentials();
                if(ok)
                {
                    run = new RunSuperSix(Startup.this, dataStoreFactory.getDataStore());
                    redisplayAs(run.aSupersix.getEnclosingPanel());
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
            else if(e.getSource() == help)
            {
                helpManualHelper.displayHelp();
            }
            else
            {
                Err.error("Not yet implemented");
            }
        }
    }
}
