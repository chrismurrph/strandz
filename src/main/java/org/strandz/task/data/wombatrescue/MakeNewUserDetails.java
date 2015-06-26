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
package org.strandz.task.data.wombatrescue;

import org.strandz.data.wombatrescue.domain.WombatConnectionEnum;
import org.strandz.data.util.UserDetailsFactory;
import org.strandz.data.util.UserDetailsProviderI;
import org.strandz.lgpl.data.objects.UserDetails;
import org.strandz.lgpl.store.EntityManagedDataStore;
import org.strandz.lgpl.util.Err;
import org.strandz.store.wombatrescue.POJOWombatData;
import org.strandz.store.wombatrescue.AbstractWRTask;

import javax.swing.SwingUtilities;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * For running on the client (from IDE) and changing user details in a remote server.
 * Usually just displays the users, but can also add new ones, delete them, change
 * passwords.
 * 
 * Actually usernames are kept in a secret subversion repository. 
 * 
 * Ideally would want a special role for the user that can create other users.
 * 
 * As well as adding users can delete them too, but you will have to alter the code
 * below to do this - see DELETE static.
 * 
 * FOLLOWING not true - delete when have seen don't need to restart Tomcat a few more times.
 * Because users are cached by Spring, you will need to restart Tomcat for your
 * changes to become effective. 
 * 
 * Enhancements: As usernames become required for different application domains can:
 * -> Make this a real object rather than a static class
 * -> Generify everything that is particular to WOMBAT/TERESA.
 * -> Have a dropdown list of application domains
 * -> Move generic class to org/strandz/data
 * -> UserDetailsFactory can be used as an object that implements UserDetailsProviderI and thus
 *    this class only needs to know about the interface. 
 * -> UserDetailsProviderI to be moved up one package.
 */
public class MakeNewUserDetails 
{
    private UserDetailsTask userTask = new UserDetailsTask(); 
    private static boolean NOT_JUST_VIEW_CREATE_TOO = false;
    private static boolean DELETE = false;

    public static void main(String s[])
    {
        SwingUtilities.invokeLater(new Runnable() 
        {
            public void run() 
            {
                MakeNewUserDetails obj = new MakeNewUserDetails();
                obj.userTask.processParams( WombatConnectionEnum.REMOTE_DEMO_WOMBAT_KODO, "Create New User Login");
                System.exit(0);
            }
        });
    }
    
    private class UserDetailsTask extends AbstractWRTask
    {
        public void update( EntityManagedDataStore dataStore)
        {
            List list = (List) dataStore.get(POJOWombatData.USER);
            if(list.isEmpty())
            {
                Err.pr( "No users found in " + dataStore);            
            }
            else
            {
                //Err.error( "Need to add a row to the DB before can do this");
                for(Iterator iter = list.iterator(); iter.hasNext();)
                {
                    UserDetails user = (UserDetails) iter.next();
                    Err.pr( "USER: " + user);
                }
                if(NOT_JUST_VIEW_CREATE_TOO)
                {
                    UserDetailsProviderI infos = UserDetailsFactory.newUserDetails( UserDetailsFactory.LIVE_TERESA_USER_DETAILS);            
                    for(int i = 0; i < infos.size(); i++)
                    {
                        Object info[] = infos.get(i);
                        /* If want to delete s/one uncomment this and make sure the RO attribute is in the constructor
                        if(info[0].equals( "Ben") && info[1].equals( "blah"))
                        {
                            deleteUser( dataStore, list, (String)info[0], (String)info[1], (String)info[2], 
                                       (String)info[3], (String)info[4], (Boolean)info[5]);
                        }
                        else
                        */
                        {
                            if(info.length == 6)
                            {
                                if(DELETE)
                                {
                                    deleteUser( dataStore, list, (String)info[0], (String)info[1], (String)info[2], 
                                               (String)info[3], (String)info[4], (Boolean)info[5]);
                                }
                                else
                                {
                                    createUser( dataStore, list, (String)info[0], (String)info[1], (String)info[2], 
                                               (String)info[3], (String)info[4], (Boolean)info[5]);
                                }
                            }
                            else if(info.length == 5) //hacky way of saying 'read only user'
                            {
                                if(DELETE)
                                {
                                    deleteUser( dataStore, list, (String)info[0], (String)info[1], (String)info[2], 
                                               (String)info[3], (String)info[4], true);
                                }
                                else
                                {
                                    createUser( dataStore, list, (String)info[0], (String)info[1], (String)info[2], 
                                               (String)info[3], (String)info[4], true);
                                }
                            }
                            else
                            {
                                Err.error( "Bad length");
                            }
                        }
                    }
                }
            }
        }    
    }
        
    private void createUser( EntityManagedDataStore dataStore,
                             Collection allUsers, String username, String password, 
                             String databaseUsername, String databasePassword, 
                             String emailAddress, boolean readOnly)
    {
        UserDetails newUser = UserDetails.createUser( 
                username, password, databaseUsername, databasePassword, emailAddress, readOnly);
        if(!allUsers.contains( newUser))
        {
            dataStore.getEM().registerPersistent(newUser);
            Err.pr( "Made a new user: " + newUser);
        }
        else
        {
            Err.pr( "User already existed: " + newUser);
        }
    }
    
    private void deleteUser( EntityManagedDataStore dataStore,
                             Collection allUsers, String username, String password, 
                             String databaseUsername, String databasePassword, 
                             String emailAddress, boolean readOnly)
    {
        UserDetails cmpUser = UserDetails.createUser( 
                username, password, databaseUsername, databasePassword, emailAddress, readOnly);
        for(Iterator iter = allUsers.iterator(); iter.hasNext();)
        {
            UserDetails user = (UserDetails) iter.next();
            if(cmpUser.equals( user))
            {
                Err.pr( "USER: " + user + " to be deleted");
                dataStore.getEM().deletePersistent( user);                
            }
        }
    }
}
