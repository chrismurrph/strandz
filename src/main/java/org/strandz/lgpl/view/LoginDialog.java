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
package org.strandz.lgpl.view;

import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.util.DisplayUtils;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Utils;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Iterator;

public class LoginDialog extends JDialog
{
    public SecurePanel securePanel;
    private LocalActionListener localActionListener;
    private DialogCredentialsCommandListener clientActionListener;
    private String title;
    private String username;
    private String password;
    private Object role;
    private boolean useComboForUser;

    private static int constructedTimes;
    private int id;
        
    public LoginDialog( JFrame frame)
    {
        super(frame, true);
        initialize();
    }
    
    public LoginDialog( JFrame frame,
                        boolean useComboForUser)
    {
        super(frame, true);
        this.useComboForUser = useComboForUser;
        initialize();
    }

    private void initialize()
    {
        constructedTimes++;
        id = constructedTimes;
        if(id > 1)
        {
            //User can login again as someone else
            //Err.error( "LoginDialog is singleton");
        }
    }

    public void setSecurePanel( SecurePanel securePanel)
    {
        this.securePanel = securePanel;
    }

    public void setClientActionListener( DialogCredentialsCommandListener clientActionListener)
    {
        this.clientActionListener = clientActionListener;
    }

    public void setTitle( String title)
    {
        this.title = title;
    }
    
    public void setUsername( String username)
    {
        this.username = username;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public void setRole(Object role)
    {
        this.role = role;
    }

    public void init()
    {
        init( null, null);
    }

    public void init( List roleSelectionItems)
    {
        init( roleSelectionItems, null);
    }
    
    public void init( List roleSelectionItems, SecurePanel.TranslatorI translator)
    {
        if(securePanel == null)
        {
            securePanel = new SecurePanel( translator);
        }
        else
        {
            //external API user has set by setter injection
        }
        securePanel.init( useComboForUser);
        setContentPane( securePanel);
        setTitle( title);

        Dimension dim = securePanel.getPreferredSize();
        Dimension preferredSize = DisplayUtils.setPreferredSize(securePanel, dim.height, dim.width);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(screenSize.width / 2 - preferredSize.width / 2,
                    screenSize.height / 2 - preferredSize.height / 2);
        setVisible(false);

        Assert.notNull( clientActionListener);
        localActionListener = new LocalActionListener();
        securePanel.bOk.addActionListener(localActionListener);
        securePanel.bCancel.addActionListener(localActionListener);
        if(!Utils.isBlank( username))
        {
            if(Utils.isBlank( password))
            {
                //Ensure the password field gets the first focus.
                addComponentListener(new ComponentAdapter() {
                    public void componentShown(ComponentEvent ce) {
                        securePanel.tfPassword.requestFocusInWindow();
                    }
                });
            }
            else
            {
                addComponentListener(new ComponentAdapter() {
                    public void componentShown(ComponentEvent ce) 
                    {
                        if(useComboForUser)
                        {
                            securePanel.cbUsername.requestFocusInWindow();
                        }
                        else
                        {
                            securePanel.tfUsername.setSelectionStart( 0);
                            securePanel.tfUsername.setSelectionEnd( securePanel.tfUsername.getText().length());
                            securePanel.tfUsername.requestFocusInWindow();
                        }
                    }
                });
            }
            if(useComboForUser)
            {
                securePanel.cbUsername.setSelectedItem( username);
            }
            else
            {
                securePanel.tfUsername.setText( username);
            }
        }
        else //username is blank
        {
            addComponentListener(new ComponentAdapter() {
                public void componentShown(ComponentEvent ce) {
                    securePanel.tfUsername.requestFocusInWindow();
                }
            });
        }
        securePanel.tfPassword.setText( password);
        if(securePanel.getRoleComponent() != null)
        {
            if(roleSelectionItems != null)
            {
                for(Iterator iterator = roleSelectionItems.iterator(); iterator.hasNext();)
                {
                    Object item = iterator.next();
                    securePanel.getRoleComponent().addItem( item);
                }            
            }
            if(securePanel.getRoleComponent().getItemCount() == 0)
            {
                Err.error( "Setting to " + role + " will not work as no items have been put into " + 
                        securePanel.getRoleComponent().getClass().getName());
            }
            securePanel.getRoleComponent().setSelectedItem( role);
        }
        //Handle window closing correctly.
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent we)
            {
                localActionListener.actionPerformed( securePanel.bCancel);
            }
        });
        getRootPane().setDefaultButton( securePanel.bOk);
        pack();
    }

    class LocalActionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent evt)
        {
            Object object = evt.getSource();
            actionPerformed( object);
        }

        public void actionPerformed(Object object)
        {
            if(object == securePanel.bOk)
            {
                //nufin
            }
            else if(object == securePanel.bCancel)
            {
                if(useComboForUser)
                {
                    //required yet for getUsername()
                    //securePanel.cbUsername.setSelectedItem( null);
                }
                else
                {
                    securePanel.tfUsername.setText( null);
                }
                securePanel.tfPassword.setText( null);
            }
            else
            {
                Err.error( "Unknown source object: " + object);
            }
            setVisible(false);
            String cmd = ((JButton)object).getActionCommand();
            String username = getUsername();
            String password = getPassword();
            clientActionListener.commandPerformedByDialog( cmd, username, password);
        }
    }

    public String getUsername()
    {
        String result;
        Assert.notNull( securePanel);
        //Assert.notNull( securePanel.tfUsername);
        //Assert.notNull( securePanel.cbUsername);
        if(useComboForUser)
        {
            Assert.notNull( securePanel.cbUsername.getSelectedItem());
            String translated = securePanel.translateForUserCombo( (String)securePanel.cbUsername.getSelectedItem());
            //securePanel.tfUsername.setText( translated);
            result = translated;
        }
        else
        {
            result = securePanel.tfUsername.getText();
        }
        return result;
    }

    public String getPassword()
    {
        return new String( securePanel.tfPassword.getPassword());
    }
}
