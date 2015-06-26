/*
    Strandz LGPL - an API that matches the user to the data.
    Copyright (C) 2007 Chris Murphy

    Strandz LGPL is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA


    The authors can be contacted via www.strandz.org
*/
package org.strandz.lgpl.persist;

import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.util.Assert;
import org.apache.cayenne.conf.Configuration;
import org.apache.cayenne.conf.DefaultConfiguration;
import org.apache.cayenne.remote.ClientConnection;
import org.apache.cayenne.remote.ClientChannel;
import org.apache.cayenne.remote.hessian.HessianConnection;
import org.apache.cayenne.CayenneContext;
import org.apache.cayenne.DataChannel;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.BaseContext;

import java.util.Properties;

public class CayenneEntityManagerFactory
{
    public static SdzEntityManagerI createSdzEMI(
        ORMTypeEnum enumId, String connectionFileNameOrURL, Properties properties)
    {
        SdzEntityManagerI result = null;
        Assert.oneOnly(connectionFileNameOrURL, properties);
        if(enumId == ORMTypeEnum.JDO)
        {
            Err.error();
        }
        else if(enumId == ORMTypeEnum.CAYENNE_SERVER)
        {
            DefaultConfiguration config = new DefaultConfiguration(connectionFileNameOrURL);
            Configuration.initializeSharedConfiguration( config);
            Configuration configuration = Configuration.getSharedConfiguration();
            ObjectContext sessionContext;
            ObjectContext filterProvidedContext = null;
            try
            {
                filterProvidedContext = BaseContext.getThreadObjectContext();
            }
            catch( IllegalStateException ex)
            {
                if(!ex.getMessage().equals( "Current thread has no bound ObjectContext."))
                {
                    Err.error( "<" + ex.getMessage() + ">");
                }
            }
            if(filterProvidedContext != null)
            {
                Err.pr( "Filter provided context: " + filterProvidedContext);
                sessionContext = filterProvidedContext;
            }
            else
            {
                sessionContext = configuration.getDomain().createDataContext();
                Err.pr( "No filter provided so created new context: " + sessionContext);
            }
            result = new CayenneServerContext( sessionContext);
        }
        else if(enumId == ORMTypeEnum.CAYENNE_CLIENT)
        {
            ClientConnection connection = new HessianConnection( connectionFileNameOrURL);
            DataChannel channel = new ClientChannel(connection);
            ObjectContext context = new CayenneContext(channel);
            result = new CayenneClientContext( context);
        }
        else
        {
            Err.pr( "connectionFileNameOrURL: " + connectionFileNameOrURL);
            Print.prProperties( properties);
            Err.error( "Do not support an ORM of type " + enumId);
        }
        Err.pr(SdzNote.EMP_ERRORS, "Created a SdzEntityManagerI: " + result);
        return result;
    }

}