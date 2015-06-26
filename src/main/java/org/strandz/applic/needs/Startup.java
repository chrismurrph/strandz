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

import org.strandz.lgpl.store.DataStoreFactory;
import org.strandz.lgpl.util.DialogEmbellisherI;
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

class Startup extends StartupGUI
{
    private StartupGUI outer;
    private boolean splashScreen = true;
    public JMenuBar jmenu;
    public JMenu ident0;
    public JMenuItem maintainContacts;
    //This var not being used because this code has not yet been brought up to date
    private DataStoreFactory dataStoreFactory;
    private RunNeeds run;
    public static final int WIDTH = 890; // 790;
    public static final int HEIGHT = 700; // 650;//520;

    public Startup()
    {
        outer = this;
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
        dataStoreFactory = new WombatDataStoreFactory(true);
        //data.fillProperties();
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
        }
    }

    private String getExportedFilename()
    {
        //String result = data.getProperty( _PropertiesStore.BACKUP_FILE_PROP);
        String result = "C:\\sdz-zone\\data\\needs\\BackupNeedsData.xml";
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

    protected void addStartupMenu()
    {
        jmenu = new JMenuBar();
        jmenu.setName("jmenu");
        this.setJMenuBar(jmenu);
        ident0 = new JMenu("Entry Point");
        ident0.setMnemonic('e');
        ident0.setName("ident0");
        jmenu.add(ident0);
        maintainContacts = new JMenuItem("Maintain Contacts");
        maintainContacts.setMnemonic('c');
        maintainContacts.setName("maintainContacts");
        ident0.add(maintainContacts);
    }

    private void removeCurrentMenu()
    {
        if(isVisible() == false)
        {
            outer.setJMenuBar(null);
            jmenu = null;
        }
        else
        {
            Err.error("frame must be invisible" + " to remove it's menu");
        }
    }

    public static void main(String[] args)
    {
        new Startup().setVisible(true);
    }

    private void transferListening(ActionListener from,
                                   ActionListener to)
    {
        if(from != null)
        {
            maintainContacts.removeActionListener(from);
        }
        if(to != null)
        {
            maintainContacts.addActionListener(to);
        }
    }

    class ActionAdapter implements ActionListener
    {
        //
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
            if(e.getSource() == maintainContacts)
            {
                run = new RunNeeds(outer);
                redisplayAs(run.aRoster.getEnclosingPanel());
            }
            else
            {
                Err.error("Not yet implemented");
            }
        }
    }
}
