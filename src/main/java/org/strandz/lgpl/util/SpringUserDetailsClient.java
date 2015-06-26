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
package org.strandz.lgpl.util;

import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.strandz.lgpl.util.UserDetailsI;

/**
 * Accesses a service that is secured by HTTP BASIC Authentication 
 */
public class SpringUserDetailsClient
{
    private final ListableBeanFactory beanFactory;
    private UserDetailsTransferObj received;

    public SpringUserDetailsClient(ListableBeanFactory beanFactory)
    {
        this.beanFactory = beanFactory;
    }

    public void invoke(Authentication authentication) throws RemoteAccessException
    {
        UserDetailsServiceI service = (UserDetailsServiceI)beanFactory.getBean( "userDetailsService");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String loggedInAs = authentication.getName();
        received = service.getUserDetails( loggedInAs);
//            Err.pr( "Have logged in as " + loggedInAs);
//            Err.pr( "FROM userDetailsService, was ok: " + !Utils.isBlank( received.getUserDetails().toString()));
//            Err.pr( "---------------START---------------");
//            Err.pr( received.getUserDetails().toString());
//            Err.pr( "----------------END----------------");
//        catch(RemoteAccessException ex)
//        {
//            new MessageDlg( "Not able to authenticate user " + loggedInAs);
//            Err.pr( ex);
//        }
        //SecurityContextHolder.clearContext();
    }

    public UserDetailsI getUserDetails()
    {
        return received.getUserDetails();
    }
}
