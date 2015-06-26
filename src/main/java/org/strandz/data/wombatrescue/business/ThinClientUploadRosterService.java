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
package org.strandz.data.wombatrescue.business;

import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.strandz.data.util.SpringClientI;
import org.strandz.lgpl.data.objects.MonthInYearI;
import org.strandz.data.wombatrescue.util.RosteringConstants;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.MessageDlg;
import org.strandz.lgpl.store.EntityManagedDataStore;

import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.List;

/**
 * Accesses a service that is secured by HTTP BASIC Authentication 
 */
public class ThinClientUploadRosterService extends AbstractClientUploadRosterService implements UploadRosterServiceI, SpringClientI
{
    private final ListableBeanFactory beanFactory;
    private Authentication authentication;
    private EntityManagedDataStore entityManagedDataStore;

    private static final String SPRING_BEANS_FILE  = "teresa/spring/clientContext.xml";

    public ThinClientUploadRosterService()
    {
        beanFactory = new ClassPathXmlApplicationContext(SPRING_BEANS_FILE);
    }

    public void setAuthentication(Authentication authentication)
    {
        this.authentication = authentication;
    }

    public void setDataStore( EntityManagedDataStore entityManagedDataStore)
    {
        this.entityManagedDataStore = entityManagedDataStore;
    }
    
    public boolean isImplemented()
    {
        return true;    
    }
    
    public String uploadRoster(String roster, int whichMonth, MonthInYearI monthInYear)
    {
        String result = null;
        UploadRosterServiceI service = (UploadRosterServiceI)beanFactory.getBean( "uploadRosterService");
        Assert.notNull( authentication, "No authentication object - user needs to log in and " +
                "init this RosterServiceI " + this.getClass().getName());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        try
        {
            //Err.pr( roster);
            result = service.uploadRoster( roster, whichMonth, monthInYear);
        }
        catch(RemoteAccessException ex)
        {
            String propVal = RosterSessionUtils.getProperty( "supportPhone");
            List<String> msg = new ArrayList<String>();
            msg.add( "Server failure. Try again or phone support on " + propVal);
            msg.add( RosteringConstants.CANNOT_COMMIT);
            new MessageDlg( msg, JOptionPane.ERROR_MESSAGE);
            Err.error( ex);
        }
        return result;
    }    
}
