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
package org.strandz.lgpl.store;

import org.strandz.lgpl.util.Err;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.server.RMISocketFactory;

/**
 * Used by JDOGenie so that the second port is not so random.
 * TODO portnumber to come from a property file
 */
public class FixedPortRMISocketFactory extends RMISocketFactory
{
    private int portnumber = NO_SUCH_PORT;
    private static FixedPortRMISocketFactory instance;
    //this is the port that is briefly used right at the beginning
    public static final int PORT_NUMBER = 1099;
    public static final int NO_SUCH_PORT = -99;

    public static synchronized void createInstance()
    {
        if(instance == null)
        {
            try
            {
                instance = new FixedPortRMISocketFactory(PORT_NUMBER);
                RMISocketFactory.setSocketFactory(instance);
            }
            catch(IOException e)
            {
                Err.error(e);
            }
        }
    }

    public static synchronized FixedPortRMISocketFactory getInstance()
    {
        createInstance();
        return instance;
    }

    /**
     * @return Returns the portnumber.
     */
    public synchronized int getPortnumber()
    {
        return portnumber;
    }

    /**
     * @param portnumber The portnumber to set.
     */
    public synchronized void setPortnumber(int portnumber)
    {
        this.portnumber = portnumber;
    }

    private FixedPortRMISocketFactory(int portnumber)
    {
        this.portnumber = portnumber;
    }

    /**
     * Creates a client socket connected to the specified host and port and writes out debugging info
     *
     * @param host the host name
     * @param port the port number
     * @return a socket connected to the specified host and port.
     * @throws IOException if an I/O error occurs during socket creation
     */
    public synchronized Socket createSocket(String host, int port) throws IOException
    {
        try
        {
            Socket s = new Socket(host, port);
            return s;
        }
        catch(Exception e)
        {
            Err.error(e, "connection failed in socketfactory");
        }

        return null;
    }

    /**
     * Create a server socket on the specified port (port 0 indicates
     * an anonymous port) and writes out some debugging info
     *
     * @param port the port number
     * @return the server socket on the specified port
     * @throws IOException if an I/O error occurs during server socket
     *                     creation
     */
    public synchronized ServerSocket createServerSocket(int port)
        throws IOException
    {
        if(port == 0)
        {
             /**/
            if(portnumber == NO_SUCH_PORT)
            {
                Err.error("FixedPortRMISocketFactory has not had portnumber property set");
            }
             /**/
            port = portnumber;
        }
        Err.pr("creating ServerSocket on port " + port);
        return new ServerSocket(port);

    }
}
