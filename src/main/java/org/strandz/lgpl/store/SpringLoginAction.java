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
package org.strandz.lgpl.store;

import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.MessageDlg;
import org.strandz.lgpl.util.SpringUserDetailsClient;
import org.strandz.lgpl.util.UserDetailsI;
import org.strandz.lgpl.view.LoginAction;

import javax.swing.JComponent;

public class SpringLoginAction extends LoginAction
{
    private DataStoreFactory dataStoreFactory;
    private ConnectionEnum connectionEnum;
    private JComponent parentPanel;
    private String springBeansFile;
    private SpringUserDetailsClient springUserDetailsClient;
    
    public SpringLoginAction( String springBeansFile, ConnectionEnum connectionEnum, 
                        DataStoreFactory dataStoreFactory)
    {
        this( springBeansFile, connectionEnum, dataStoreFactory, null);
    }
    
    public SpringLoginAction( String springBeansFile, ConnectionEnum connectionEnum, 
                        DataStoreFactory dataStoreFactory, JComponent parentPanel)
    {
        this.springBeansFile = springBeansFile;
        this.connectionEnum = connectionEnum;
        Err.pr( "Will login to " + connectionEnum);
        this.dataStoreFactory = dataStoreFactory;
        this.parentPanel = parentPanel;
    }
    
    private void submit(String username,
                        String password) throws RemoteAccessException
    {
        ListableBeanFactory beanFactory = new ClassPathXmlApplicationContext(springBeansFile);
        if(beanFactory.getBeanDefinitionCount() <= 0)
        {
            Err.error( "Could not find any bean definitions from resource " + springBeansFile);
        }
        else
        {
//            String beanNames[] = beanFactory.getBeanDefinitionNames();
//            for(int i = 0; i < beanNames.length; i++)
//            {
//                Err.pr( "Bean: " + beanNames[i]);
//            }
            springUserDetailsClient = new SpringUserDetailsClient(beanFactory);
            Authentication authObject = new UsernamePasswordAuthenticationToken(username, password);
            springUserDetailsClient.invoke( authObject);
            dataStoreFactory.setAuthenticationObject( authObject);
        }
    }
    
    public void logonToServer( String username, String password)
    {
        //Err.pr( "To log in using " + loginDialog.getUsername() + "/" + loginDialog.getPassword());
        try
        {
            submit( username, password);
            oklogIn();
        }
        catch(RemoteAccessException ex)
        {
            if(parentPanel != null)
            {
                //Actually this is usually done at application startup 
                //MessageDlg.setDlgParent( parentPanel);
                Assert.notNull( MessageDlg.getDlgParent(), "MessageDlg.setDlgParent() has not been called");
                new MessageDlg( "Not able to authenticate user " + username);
            }
            else
            {
                Err.error( "Not able to authenticate user " + username, ex);
            }
        }
    }

    private void oklogIn()
    {
        userDetails = springUserDetailsClient.getUserDetails();
        //This bad - was showing username and password!
        Err.pr(SdzNote.USER_DETAILS.isVisible(), "DB user: " + "as if" /*userDetails.getDatabaseUsername()*/);
        Err.pr(SdzNote.USER_DETAILS.isVisible(), "DB pass: " + "as if" /*userDetails.getDatabasePassword()*/);
        dataStoreFactory.setCredentials( connectionEnum, ((UserDetailsI)userDetails).getDatabaseUsername(),
                                         ((UserDetailsI)userDetails).getDatabasePassword());
    }
}
