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
package org.strandz.lgpl.util;

import java.beans.ExceptionListener;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * This class serves the same function as
 * <code>SerialObjectsInFileReadWriter</code>, but instead of writing
 * lists of graphs of objects in a serialised format, they are written
 * in an XMLEncoded format. The actual file will thus actually be able
 * to be read and altered by a human, if that should ever be necessary.
 *
 * @author Chris Murphy
 */
public class XMLObjectsInFileReadWriter
{
    private String fileName;
    private List objectList;

    /**
     * Code to help us open a local file for reading Objects from it.
     *
     * @param fileName the name of the file that contains objects
     * @return an ObjectInputStream with objects in it
     */
    private static XMLDecoder getDecoderFromFile(final String fileName)
    {
        FileInputStream fis = null;
        try
        {
            fis = new FileInputStream(fileName);
        }
        catch(FileNotFoundException e)
        {// Don't need to error or create it - will create when write
            // Err.error("File not found - <" + fileName + ">");
        }

        XMLDecoder decoder = null;
        if(fis != null)
        {
            decoder = new XMLDecoder(fis);
            decoder.setExceptionListener(new ExceptionListener()
            {
                public void exceptionThrown(Exception exception)
                {
                    Err.error(exception, "Problem reading <" + fileName + ">");
                }
            });
        }
        return decoder;
    }

    private XMLDecoder getDecoder()
    {
        XMLDecoder result = null;
        if(fileName != null)
        {
            result = getDecoderFromFile(fileName);
        }
        else
        {
            throw new Error("filename must be filled in");
        }
        return result;
    }

    private Object getAnObject(XMLDecoder ois)
    // throws ClassNotFoundException
    {
        Object obj = null;
        if(ois != null)
        {
            obj = ois.readObject();
            // Err.pr("@@ HAVE READ " + obj);
        }
        return obj;
    }

    /**
     * Used to read the objects that were stored. Have to know exactly how many were stored
     * in the first place. Alternatively use
     * the object list's size
     *
     * @param numObjects the number of objects that originally put into the file
     * @return a List of the objects
     * @see #getObjectListSize()
     */
    public List getStoredObjects(int numObjects)
    // throws ClassNotFoundException
    {
        List localObjList = new ArrayList();
        XMLDecoder ois = getDecoder();
        // fill and return the local ArrayList, so can set objectList
        // to null
        for(int i = 0; i <= numObjects - 1; i++)
        {
            try
            {
                localObjList.add(getAnObject(ois));
            }
            catch(ArrayIndexOutOfBoundsException e)
            {/*
              * We were asking for too many objects
              */
            }
        }
        if(ois != null)
        {
            ois.close();
        }
        objectList = null;
        return localObjList;
    }

    /**
     * Set the name of the file that we will wish to read objects from and to.
     *
     * @param fileName
     */
    public void setFile(String fileName)
    {
        this.fileName = fileName;
    }

    /**
     * Add an object to the internally kept list of objects.
     *
     * @param obj The object to be added
     */
    public void addToObjectList(Object obj)
    {
        if(objectList == null)
        {
            objectList = new ArrayList(10);
        }
        objectList.add(obj);
    }

    /**
     * After the user has added a load of objects, he must then write them away to
     * file using this method
     *
     * @see #addToObjectList(Object)
     */
    public void writeObjectList()
    {
        XMLEncoder os = getEncoder();
        if(os == null)
        {
            Err.error("objectOutputStream is null as try to writeObjectList");
        }

        Object element;
        for(int i = 0; i <= objectList.size() - 1; i++)
        {
            element = objectList.get(i);
// Writing a null object to disk s/be ok
//            if (element == null)
//            {
//                Err.error("When trying to write out (to stream) an "
//                    + "object list, null element found at " + i);
//            }
            /*
             //Print.pr( "@@ TO WRITE " + element + " of type " + element.getClass());
             if(element instanceof ArrayList)
             {
             //Err.pr( "@@ size is " + ((List)element).size());
             }
             */
            os.writeObject(element);
        }
        os.flush();
        os.close();
        objectList = null;
    }

    /**
     * Find out how many objects have been added so far
     *
     * @return size of the internal object list
     */
    public int getObjectListSize()
    {
        return objectList.size();
    }

    private XMLEncoder getEncoder()
    {
        XMLEncoder result = null;
        if(fileName != null)
        {
            BufferedOutputStream out = null;
            try
            {
                out = new BufferedOutputStream(new FileOutputStream(fileName));
            }
            catch(FileNotFoundException ex)
            {
                Err.error(ex, fileName);
            }
            result = new XMLEncoder(out);
        }
        else
        {
            result = null;
        }
        return result;
    }
}
