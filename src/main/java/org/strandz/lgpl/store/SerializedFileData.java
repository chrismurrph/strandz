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
import org.strandz.lgpl.util.SerialObjectsInFileReadWriter;

import java.io.InvalidClassException;
import java.util.Iterator;

abstract public class SerializedFileData extends FileData
{
    // When make this non-static, will be able to fold almost
    // everything up into FileData.
    private SerialObjectsInFileReadWriter serialObjectsInFileReadWriter = new SerialObjectsInFileReadWriter();

    public void set(int whichExtent, Object obj)
    {
        if(extents == null)
        {
            Err.error("Can't call set(...) when stored is null");
        }
        // Err.pr( "Set called with " + whichExtent + " " + obj);
        if(obj == null)
        {
            Err.error("Trying to insert null into: " + whichExtent);
        }
        if(whichExtent >= extents.size())
        {
            // Err.error("Do not have index <" + whichExtent + "> - have <" + extents.size() + "> elements only - call readData first");
            extents.add(whichExtent, obj);
        }
        else
        {
            // changed from add to set
            extents.set(whichExtent, obj);
        }
    }

    /*
    public void flush( int startAnotherTxn)
    {
    if(extents == null)
    {
    try
    {
    internalReadData();
    Print.pr( "Stored Objects have been retrieved");
    *
    Err.pr( extents);
    for( int i=0; i<=numExtents-1; i++)
    {
    Err.pr( extents.get( i).getClass() + pUtils.separator);
    }
    *
    }
    catch(InvalidClassException ex)
    {
    *
    * To let the user know that flushing was the only way. If you can't
    * get them you never will, and we'll replace them
    *
    Print.pr( ex);
    extents = new ArrayList();
    Print.pr( "A new container for stored objects has been created (InvalidClassException)");
    }
    catch(ClassNotFoundException ex)
    {
    *
    * To let the user know that flushing was the only way. If you can't
    * get them you never will, and we'll replace them
    *
    Print.pr( ex);
    extents = new ArrayList();
    Print.pr( "A new container for stored objects has been created (ClassNotFoundException)");
    }
    }
    *
    * If stored has lists of data in it, then the data will now
    * be destroyed:
    *
    if(extents.size() != 0)
    {
    Print.pr("All current data will now be destroyed");
    extents.clear();
    }
    for( int i=0; i<=classes.length-1; i++)
    {
    //Print.pr("Adding an empty element");
    extents.add( new ArrayList());
    }
    writeData( DO_NOT_START_NEW_TXN);
    }
    */

    /**
     * Must have a filename when call this. Even if fresh, will still want to use
     * the filename when writeData is called.
     */
    void internalReadData() throws InvalidClassException, ClassNotFoundException
    {
        if(classes.length == 0)
        {
            Err.error("Number of Objects cannot be zero - call setNumObjects first");
        }
        if(filename == null)
        {
            Err.error("A filename must exist before readData called - call setFilename first");
        }
        serialObjectsInFileReadWriter.setFile(filename);
        extents = serialObjectsInFileReadWriter.getStoredObjects(classes.length);
    }

    public boolean commitTx()
    {
        super.commitTx();

        boolean result = false;
        if(filename == null)
        {
            Err.error("Must have a filename before can writeData - call setFilename first");
        }
        if(extents == null || extents.size() == 0)
        {
            Err.error("No stored objects - must readData first");
        }
        serialObjectsInFileReadWriter.setFile(filename);
        // Err.pr( "stored is of size " + stored.size());
        if(extents.size() != classes.length)
        {
            Err.error("Should always be writing " + classes.length + " not "
                + extents.size());
        }
        for(Iterator e = extents.iterator(); e.hasNext();)
        {
            serialObjectsInFileReadWriter.addToObjectList(e.next());
        }
        int size = serialObjectsInFileReadWriter.getObjectListSize();
        if(size <= 0)
        {
            result = false;
        }
        serialObjectsInFileReadWriter.writeObjectList();
        return result;
    }
} // end class
