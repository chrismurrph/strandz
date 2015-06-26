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

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class reads and writes lists of graphs of objects
 *
 * @author Chris Murphy
 */
public class SerialObjectsInFileReadWriter
{
    private String fileName;
    private List objectList;

    /**
     * Code to help us open a local file for reading Objects from it.
     *
     * @param fileName the name of the file that contains objects
     * @return an ObjectInputStream with objects in it
     */
    private static ObjectInputStream getObjInputStreamFromFile(String fileName)
    {
        FileInputStream fin = null;
        try
        {
            fin = new FileInputStream(fileName);
        }
        catch(FileNotFoundException e)
        {
            Err.error(
                "File not found - copy from another object stream file and try again <"
                    + fileName + ">");
        }

        ObjectInputStream inStream = null;
        try
        {
            inStream = new ObjectInputStream(fin);
        }
        catch(StreamCorruptedException e)
        {
            Err.error(e,
                "Create " + fileName + " by copying from another object stream file");
        }
        catch(IOException e)
        {
            Err.error(e, fileName);
        }
        return inStream;
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

    private ObjectInputStream getInputStream()
    {
        ObjectInputStream result = null;
        if(fileName != null)
        {
            result = getObjInputStreamFromFile(fileName);
        }
        else
        {
            Err.error("filename must be filled in");
        }
        return result;
    }

    private Object getAnObject(ObjectInputStream ois)
        throws ClassNotFoundException,
        InvalidClassException,
        IOException
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
     * Used to read the one object that was stored. Have to know that this is the case.
     *
     * @return the stored object
     */
    public Object getStoredObject()
    {
        Object obj = null;
        ObjectInputStream ois = getInputStream();
        try
        {
            obj = getAnObject(ois);
        }
        catch(Exception e)
        {
            Err.error(e.toString());
        }
        try
        {
            ois.close();
        }
        catch(IOException e)
        {
            Err.error(e.toString());
        }
        objectList = null;
        return obj;
    }

    /**
     * Used to read the objects that were stored. Have to know exactly how many were stored
     * in the first place. Alternatively use
     * the object list's size
     *
     * @param numObjects the number of objects that originally put into the file
     * @return a List of the objects
     * @throws InvalidClassException
     * @throws ClassNotFoundException
     * @see #getObjectListSize()
     */
    public List getStoredObjects(int numObjects)
        throws InvalidClassException,
        ClassNotFoundException
    {
        List localObjList = new ArrayList();
        ObjectInputStream ois = getInputStream();
        // fill and return the local ArrayList, so can set objectList
        // to null
        for(int i = 0; i <= numObjects - 1; i++)
        {
            try
            {
                localObjList.add(getAnObject(ois));
            }
            catch(InvalidClassException e)
            {
                throw e;
            }
            catch(ClassNotFoundException e)
            {
                throw e;
            }
            catch(EOFException e)
            {/*
         * We were asking for too many objects
         */}
            catch(java.io.WriteAbortedException e)
            {
                String compStr = "java.io.WriteAbortedException: Writing aborted by exception; java.io.NotSerializableException:";
                if(e.toString().startsWith(compStr))
                {
                    int len = compStr.length();
                    String classCausingProblem = e.toString().substring(len + 1);
                    Print.pr(
                        "Not retrieving an object of class <" + classCausingProblem
                            + "> that was not serialized in first place!");
                }
                else
                {
                    Err.error(
                        "Problem reading object <" + i + ">, use <applic.Flush> : "
                            + e.toString());
                }
            }
            catch(Exception e)
            {
                Err.error(
                    "Problem reading object <" + i + ">, use <applic.Flush> : "
                        + e.toString());
            }
        }
        try
        {
            ois.close();
        }
        catch(IOException e)
        {
            Err.error(e.toString());
        }
        objectList = null;
        return localObjList;
    }

    //
    // rest of class to do with writing objects
    //

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

    private ObjectOutputStream getObjOutputStream()
    {
        ObjectOutputStream result = null;
        if(fileName != null)
        {
            FileOutputStream fout = null;
            try
            {
                fout = new FileOutputStream(fileName);
            }
            catch(IOException e)
            {
                Err.error(e.toString());
            }
            try
            {
                result = new ObjectOutputStream(fout);
            }
            catch(IOException e)
            {
                Err.error(e.toString());
            }
        }
        else
        {
            result = null;
        }
        return result;
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

    /**
     * After the user has added a load of objects, he must then write them away to
     * file using this method
     *
     * @see #addToObjectList(Object)
     */
    public void writeObjectList()
    {
        try
        {
            ObjectOutputStream os = getObjOutputStream();
            if(os == null)
            {
                Err.error("objectOutputStream is null as try to writeObjectList");
            }

            Object element;
            for(int i = 0; i <= objectList.size() - 1; i++)
            {
                element = objectList.get(i);
                if(element == null)
                {
                    Err.error(
                        "When trying to write out (to stream) an "
                            + "object list, null element found at " + i);
                }
                // Err.pr("@@ TO WRITE " + element);
                os.writeObject(element);
            }
            os.flush();
            os.close();
        }
        catch(IOException e)
        {
            Err.error(e.toString());
        }
        // so ArrayList can be re-created next time
        objectList = null;
    }

    /**
     * Code to help us open a file on the Web Server for reading
     */
    /*
     Works fine, commented out as not using
    private static ObjectInputStream getObjInputStreamFromURL( URL url, String fileName)
    {
      DataInputStream dis = null;
      try
      {
        url = new URL( url, fileName);
        Print.pr( "URL to bring objects off server is" + " " + url.toString());
      }
      catch(MalformedURLException e)
      {
        Err.error( "Malformed URL");
      }
      try
      {
        dis = new DataInputStream( url.openStream());
      }
      catch(IOException e)
      {
        Err.error( "Stream could not be opened");
      }

      ObjectInputStream inStream = null;
      try
      {
        inStream = new ObjectInputStream( dis);
      }
      catch(StreamCorruptedException e)
      {
        Err.error( e.toString());
      }
      catch(IOException e)
      {
        Err.error( e.toString());
      }
      return inStream;
    }
    */
}
