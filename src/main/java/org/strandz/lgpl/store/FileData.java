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
import org.strandz.lgpl.util.FileUtils;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.util.TimeUtils;

import java.io.File;
import java.io.InvalidClassException;
import java.util.ArrayList;
import java.util.List;

abstract public class FileData extends DataStore
{
    String filename;
    //private HashMap refinedLists = new HashMap();
    private boolean dynamicallyCreateFile;
    private boolean backup;

    public void dynamicallyCreateFile()
    {
        //Won't be called if doesn't need to be, so ok to do nothing here,
        //leaving subclasses that need this functionality to do it
    }

    public boolean isBackup()
    {
        return backup;
    }

    public void setBackup(boolean backup)
    {
        this.backup = backup;
    }

    public String toString()
    {
        String result = filename + " contains " + size() + " classes and " + extents.size() + " extents";
        return result;
    }

    public void setFilename(String filename)
    {
        if (filename == null)
        {
            Err.error("Cannot call setFilename() with a null param");
        }
        this.filename = filename;

        File file = new File(filename);
        if (!file.exists())
        {
            if (!dynamicallyCreateFile)
            {
                Err.error("Need to manually create a file called: " + filename);
            }
            else
            {
                dynamicallyCreateFile();
                // Err.pr( "%% IS " + filename);
                // Err.error( "How did we create log files dynamically?");
                /*
                 try
                 {
                 if(!file.createNewFile())
                 {
                 Err.error( "Could not create a file called <" + filename + ">");
                 }
                 }
                 catch(IOException ex)
                 {
                 Err.error( ex, "Could not create a file called <" + filename + ">");
                 }
                 */
            }
        }
    }

    public String getFileName()
    {
        return filename;
    }

    public List query(Class clazz)
    {
        return (List) get(clazz);
    }

    abstract void internalReadData()
        throws InvalidClassException, ClassNotFoundException;

    public void flush()
    {
        /*
         if(extents.size() == 0)
         {
         try
         {
         internalReadData();
         Print.pr( "Stored Objects have been retrieved");
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
         */
        /*
         * If stored has lists of data in it, then the data will now
         * be destroyed:
         */
        boolean everyExtentAlreadyEmpty = true;
        if (extents.size() != 0)
        {
            for (int i = 0; i <= classes.length - 1; i++)
            {
                Object obj = extents.get(i);
                if (obj instanceof List)
                {
                    List list = (List) obj;
                    if (!list.isEmpty())
                    {
                        everyExtentAlreadyEmpty = false;
                        break;
                    }
                }
                else
                {
                    everyExtentAlreadyEmpty = false; // do whole thing
                    break;
                }
            }
        }
        else
        {
            everyExtentAlreadyEmpty = false;
        }
        if (!everyExtentAlreadyEmpty)
        {
            rollbackTx();
            startTx( "To expunge all data from " + this.getClass().getName());
            if (extents.size() != 0)
            {
                Print.pr("All current data will now be destroyed");
                extents.clear();
            }
            for (int i = 0; i <= classes.length - 1; i++)
            {
                Print.pr("Adding an empty element");
                extents.add(createList(classes[i]));
            }
            commitTx();
        }
    }

    /**
     * Override if need a specific class to be stored using
     * a specific list.
     */
    protected List createList(Class clazz)
    {
        return new ArrayList();
    }

    /**
     * Must have a filename when call this. Even if fresh, will still want to use
     * the filename when writeData is called.
     */
    public void startTx( String reason)
    {
        super.startTx( reason);

        // Err.pr( "About to read " + filename);
        File file = new File(filename);
        if(backup)
        {
            String backupFileName = filename + TimeUtils.getACurrentFileName(".bak");
            File backup = new File(backupFileName);
            if (file.length() != 0)
            {
                FileUtils.copyFile(file, backup);
            }
        }
        try
        {
            internalReadData();
        }
        catch (InvalidClassException ex)
        {
            Err.error("Won't be able to read anything until you flush, a class has changed");
        }
        catch (ClassNotFoundException ex)
        {
            Err.error("Need to " + "flush, a class no longer exists in <" + filename + ">");
        }
    }

    /**
     * No need to do anything. The files are opened and closed
     * as read/write.
     */
    /*
     public void close( int howDealWithLastTxn)
     {
     }
     */
    /*
    public SdzPersistenceManagerI getPM()
    {
      return null;
    }
    */

    /**
     * list param is the one that gets updated as user does
     * things. After it has been updated it will need to be
     * merged in with what actually gets written to the DB.
     */
    /*
    public void setRefinedList( Class clazz, Object list )
    {
      if (refinedLists.containsKey( clazz ))
      {// Happens when reload
        // Err.error( "No point in calling setRefinedList() again for " + clazz);
      }
      refinedLists.put( clazz, list );
    }
    */

    protected boolean isDynamicallyCreateFile()
    {
        return dynamicallyCreateFile;
    }

    protected void setDynamicallyCreateFile(boolean dynamicallyCreateFile)
    {
        this.dynamicallyCreateFile = dynamicallyCreateFile;
    }    
}
