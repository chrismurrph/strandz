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

import org.strandz.lgpl.util.Err;

abstract public class LoginAction implements DialogCredentialsCommandListener
{
    protected Object userDetails;
    
    abstract public void logonToServer( String username, String password);
    
    public void commandPerformedByDialog(Object cmd, String username, String password)
    {
        if(cmd.equals( SecurePanel.OK))
        {
            logonToServer( username, password);
        }
        else if(cmd.equals( SecurePanel.CANCEL))
        {
            //Err.pr( "Will NOT log in using " + loginDialog.getUsername() + "/" + loginDialog.getPassword());
            //System.exit( 0);
        }
        else
        {
            Err.error( "Unknown command " + cmd);
        }
    }
    
    public boolean isOkLogin()
    {
        return userDetails != null;
    }
    
    public Object getLoggedInUserDetails()
    {
        return userDetails;
    }
}
