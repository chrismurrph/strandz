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
import org.strandz.lgpl.util.XMLObjectsInFileReadWriter;

import java.util.Iterator;
import java.util.List;

abstract public class XMLFileData extends FileData
{
    // When make this non-static, will be able to fold almost
    // everything up into FileData.
    private XMLObjectsInFileReadWriter ioObjectsInFileReadWriter = new XMLObjectsInFileReadWriter();
    int id;
    private static int constuctedTimes = 0;

    public XMLFileData()
    {
        constuctedTimes++;
        id = constuctedTimes;
        //Err.pr( "XMLFileData constructor, ID: " + id);
        if(id == 0)
        {
            //Err.stack();
        }
    }

    public String toString()
    {
        String result = super.toString();
        result += " ID: " + id;
        return result;
    }

    public void set(int whichExtent, Object obj)
    {
        if (extents == null)
        {
            Err.error("Can't call set(...) when stored is null");
        }
        // Err.pr( "$$$ Set called with " + whichExtent + " " + obj);
        if (obj == null)
        {
            Err.error("Trying to insert null into: " + whichExtent);
        }
        if (whichExtent >= extents.size())
        {
            // Err.error("Do not have index <" + whichExtent + "> - have <" + extents.size() + "> elements only - call readData first");
            //Err.pr( "To " + this + " adding extent " + whichExtent + " obj: " + obj);
            extents.add(whichExtent, obj);
        }
        else
        {
            // changed from add to set
            //Err.pr( "To " + this + " setting extent " + whichExtent + " obj: " + obj);
            extents.set(whichExtent, obj);
        }
    }

    public void addExtent(List list)
    {
        Err.error( "This method not to be used with XMLFileData");
    }

    /**
     * Must have a filename when call this. Even if fresh, will still want to use
     * the filename when writeData is called.
     */
    void internalReadData()
        // throws
        // InvalidClassException,
        // ClassNotFoundException
    {
        if (classes.length == 0)
        {
            Err.error("Number of Objects cannot be zero - call setNumObjects first");
        }
        if (filename == null)
        {
            Err.error("A filename must exist before readData called - call setFilename first");
        }
        ioObjectsInFileReadWriter.setFile(filename);
        extents = ioObjectsInFileReadWriter.getStoredObjects(classes.length);
        //Err.pr( "Read from " + filename);
        //Err.pr( "As reading data extents has become " + extents + " in " + this);
    }

    public boolean commitTx()
    {
        super.commitTx();

        boolean result = false;
        if (filename == null)
        {
            Err.error("Must have a filename before can writeData - call setFilename first");
        }
        if (extents == null || extents.size() == 0)
        {
            Err.error("No stored objects - must readData first");
        }
        ioObjectsInFileReadWriter.setFile(filename);
        for (Iterator e = extents.iterator(); e.hasNext();)
        {
            Object extent = e.next();
            /*
             if(extent instanceof DebugList)
             {
             Err.pr( "$$$ Adding a DEBUG LIST " + ((DebugList)extent).id);
             }
             */
            ioObjectsInFileReadWriter.addToObjectList(extent);
        }
        int size = ioObjectsInFileReadWriter.getObjectListSize();
        if (size <= 0)
        {
            result = false;
        }
        ioObjectsInFileReadWriter.writeObjectList();
        return result;
    }

    public Object getDatabase()
    {
        return filename;
    }
} // end class
