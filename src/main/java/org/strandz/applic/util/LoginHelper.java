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
package org.strandz.applic.util;

import org.strandz.lgpl.store.ConnectionEnum;
import org.strandz.lgpl.store.DataStoreFactory;
import org.strandz.lgpl.store.SecureConnectionInfo;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.MessageDlg;
import org.strandz.lgpl.view.LoginAction;
import org.strandz.lgpl.view.LoginDialog;

import javax.swing.JFrame;

/**
 * Can be used even if will not need to login - in which case the simpler
 * factory methods can be used.
 */
public class LoginHelper
{
    private Params params;
    private LoginDialogCreator loginDialogCreator;
    private LoginAction logInAction;
    
    public class LocalLoginDialogCreator implements LoginDialogCreator
    {
        public LoginDialog create( JFrame frame, String title, LoginAction logInAction, String user, String pass, Object role)
        {
            LoginDialog result = new LoginDialog( frame);
            result.setTitle( title);
            result.setClientActionListener( logInAction);
            result.setUsername( user);
            result.setPassword( pass);
            result.setRole( role);
            result.init();            
            return result;
        }
    }

    /**
     * When there is no need for a username and password. Here no dialog will come up, the same as
     * using the factory method with username and password, but passing nulls. 
     */
    public static Params newParams( ConnectionEnum connectionEnum,
                                    DataStoreFactory dataStoreFactory,
                                    String dialogTitle)
    {
        Params result = new Params( connectionEnum, dataStoreFactory, dialogTitle, null, 
                                    null, null, false, null);
        return result;
    }
    
    public static Params newParams( ConnectionEnum connectionEnum,
                                    DataStoreFactory dataStoreFactory,
                                    String dialogTitle,
                                    OnceLoggedInAction onceLoggedInAction,
                                    String username, 
                                    String password)
    {
        Params result = new Params( connectionEnum, dataStoreFactory, dialogTitle, onceLoggedInAction, 
                                    username, password, false, null);
        return result;
    }
    
    public static Params newParams( ConnectionEnum connectionEnum,
                                    DataStoreFactory dataStoreFactory,
                                    String dialogTitle,
                                    OnceLoggedInAction onceLoggedInAction,
                                    String username, 
                                    String password,
                                    Object role)
    {
        Params result = new Params( connectionEnum, dataStoreFactory, dialogTitle, onceLoggedInAction, 
                                    username, password, false, role);
        return result;
    }

    /**
     * Will optionally bring up a Dialog depending on batchMode param. 
     * You don't need to assign an action but can instead see if login() is
     * ok then do your action inline. 
     */
    public static Params newParams( ConnectionEnum connectionEnum,
                                    DataStoreFactory dataStoreFactory,
                                    String dialogTitle,
                                    String username, 
                                    String password,
                                    boolean batchMode)
    {
        Params result = new Params( connectionEnum, dataStoreFactory, dialogTitle, null, 
                                    username, password, batchMode, null);
        return result;
    }

    /**
     * connectionEnum - for whether to use Dialog and title of Dialog
     * dataStoreFactory
     * springBeansFileName - a String to identify the authentication service
     * username - that will be defaulted into the login dialog
     * password - that will be defaulted into the login dialog
     * onceLoggedInAction - an action to perform if login succeeds
     */
    public static class Params
    {
        public ConnectionEnum connectionEnum; 
        public DataStoreFactory dataStoreFactory;
        public String dialogTitle;
        public OnceLoggedInAction onceLoggedInAction;
        public String username; 
        public String password;
        public boolean batchMode;
        public Object role;

        private Params(ConnectionEnum connectionEnum, 
                      DataStoreFactory dataStoreFactory, 
                      String dialogTitle, 
                      OnceLoggedInAction onceLoggedInAction, 
                      String username, 
                      String password,
                      boolean batchMode,
                      Object role)
        {
            this.connectionEnum = connectionEnum;
            this.dataStoreFactory = dataStoreFactory;
            this.dialogTitle = dialogTitle;
            this.onceLoggedInAction = onceLoggedInAction;
            this.username = username;
            this.password = password;
            this.batchMode = batchMode;
            this.role = role;
        }

        void init()
        {
            Assert.notNull( dataStoreFactory);
            Assert.notNull( connectionEnum);
            //Assert.notNull( onceLoggedInAction, "If no action required after logged in then " +
            //        "pass in a dummy OnceLoggedInAction");
            if(dialogTitle == null && connectionEnum != null)
            {
                dialogTitle = connectionEnum.toString();
            }
            if(connectionEnum.isRequiresLogin())
            {
                SecureConnectionInfo connectionInfo = (SecureConnectionInfo)dataStoreFactory.getConnectionInfo( 
                        connectionEnum);
                if(connectionInfo == null)
                {
                    Err.error( "No SecureConnectionInfo to securely connect to a server");
                }
            }
        }
    }

    /**
     * To login then to an action on the server
     */
    public LoginHelper( Params params)
    {
        params.init();
        this.params = params;
    }

    public void setLoginDialogCreator(LoginDialogCreator loginDialogCreator)
    {
        this.loginDialogCreator = loginDialogCreator;
    }

    public boolean login()
    {
        LoginResult result = loginWithAccess();
        result.loginDialog.dispose();
        return result.okLogin;
    }

    public static class LoginResult
    {
        public boolean okLogin;
        public LoginDialog loginDialog;
    }

    /**
     * With future access to the LoginDialog that may be created. Callers
     * will need to dispose() of the LoginDialog themselves
     */
    public LoginResult loginWithAccess()
    {
        LoginResult result = new LoginResult();
        if(params.connectionEnum.isRequiresLogin())
        {
            SecureConnectionInfo connectionInfo = (SecureConnectionInfo)params.dataStoreFactory.getConnectionInfo( 
                    params.connectionEnum);
            logInAction = connectionInfo.createLoginAction( params.connectionEnum, params.dataStoreFactory);
            if(params.batchMode)
            {
                logonToServer( logInAction);
                result.okLogin = logInAction.isOkLogin();
            }
            else
            {
                result.loginDialog = logonToServerByDialog( logInAction);
                result.okLogin = logInAction.isOkLogin();
            }
        }
        else //local and insecure (don't need to login)
        {
            Err.pr( params.connectionEnum + " does not require authentication");
            result.okLogin = true;
        }
        //
        if(result.okLogin && params.onceLoggedInAction != null)
        {
            params.onceLoggedInAction.doTransaction( params.dataStoreFactory);
        }
        return result;
    }
    
    public Object getLoggedInUserDetails()
    {
        Assert.notNull( logInAction, "Have not logged in");
        return logInAction.getLoggedInUserDetails();
    }

    private void logonToServer( LoginAction logInAction)
    {
        logInAction.logonToServer( params.username, params.password);
    }
    
    private LoginDialog logonToServerByDialog( LoginAction logInAction)
    {
        if(MessageDlg.getFrame( true) == null)
        {
            MessageDlg.setFrame( new JFrame());
        }
        LoginDialog loginDialog;
        if(loginDialogCreator != null)
        {
            loginDialog = loginDialogCreator.create( MessageDlg.getFrame(), params.dialogTitle,
                                                   logInAction, params.username, params.password, params.role);
        }
        else
        {
            LocalLoginDialogCreator localLoginDialogCreator = new LocalLoginDialogCreator();
            loginDialog = localLoginDialogCreator.create( MessageDlg.getFrame(), params.dialogTitle,
                                                   logInAction, params.username, params.password, params.role);
        }
        loginDialog.setVisible( true);
        //Too late:
        //loginDialog.securePanel.tfUsername.requestFocusInWindow();
        return loginDialog;
    }        
}
