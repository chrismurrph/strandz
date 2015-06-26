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
package org.strandz.data.util;

import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.ObjectFoundryUtils;
import org.strandz.lgpl.util.SelfReferenceUtils;

/**
 * Specialist way of providing user details not directly from the database. Used when
 * initially inserting users into the DB or when testing/developing and want to just
 * have the credentials already there. When want to get in, but don't want to 
 * manually type stuff.
 */
public class UserDetailsFactory
{
    public static final String LIVE_TERESA_USER_DETAILS = "Live Teresa User Details"; 
    public static final String LIVE_SUPERSIX_USER_DETAILS = "Live SuperSix User Details";
    public static final String TEST_FILE_USER_DETAILS = "Test File User Details";
        
    public static UserDetailsProviderI newUserDetails( String id)
    {
        UserDetailsProviderI result = null;
        if(id.equals(LIVE_TERESA_USER_DETAILS))
        {
            result = giveLifeTo( "com.seasoft.store.ActualTeresaUserDetails");
        }
        else if(id.equals(LIVE_SUPERSIX_USER_DETAILS))
        {
            result = giveLifeTo( "com.seasoft.store.ActualSuperSixUserDetails");
        }
        else if(id.equals(TEST_FILE_USER_DETAILS))
        {
            result = readFromTestFile();
        }
        else
        {
            Err.error( id + " not yet coded for");
        }
        return result;
    }
    
    private static UserDetailsProviderI giveLifeTo( String id)
    {
        UserDetailsProviderI result = null;
        Class clazz = SelfReferenceUtils.getClass( id);
        if(clazz != null)
        {
            //Assert.notNull( clazz, "Classpath does not contain a UserDetailsProviderI for " + id);
            result = (UserDetailsProviderI) ObjectFoundryUtils.factory( clazz);
        }
        return result;
    }
    
    private static UserDetailsProviderI readFromTestFile()
    {
        UserDetailsProviderI result = new FileUserDetailsProvider();
        return result;
    }
}
