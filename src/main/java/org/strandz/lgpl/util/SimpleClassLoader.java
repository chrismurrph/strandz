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

import org.strandz.lgpl.note.SdzNote;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Forerunner to SpecialClassLoader. Some tests still use this class, so keep
 * until have refactored them to use SpecialClassLoader.
 *
 * @author Chris Murphy
 */
public class SimpleClassLoader extends ClassLoader
{
    // basePath gives the path to which this class
    // loader appends "/.class" to get the
    // full path name of the class file to load
    private String basePath;
    private String toBeLoadedDynamically;
    private Class savedLoadedClass;
    private boolean force = false;

    public SimpleClassLoader(String basePath)
    {
        this.basePath = basePath;
    }

    public SimpleClassLoader(
        ClassLoader parent, String basePath)
    {
        this(parent, basePath, null);
    }

    /**
     * This strategy won't work as a class needs to have been originally
     * loaded with the same classloader as later.
     */
    public SimpleClassLoader(
        ClassLoader parent, String basePath, String toBeLoadedDynamically)
    {
        super(parent);
        this.basePath = basePath;
        this.toBeLoadedDynamically = toBeLoadedDynamically;
    }

    protected synchronized Class loadClass(String name,
                                           boolean resolve)
        throws ClassNotFoundException
    {
        Err.pr(SdzNote.GENERIC, "&&&&&&&&&  Calling loadClass with " + name);

        Class c = null;
        if(!name.equals(toBeLoadedDynamically))
        {
            c = super.loadClass(name, resolve);
        }
        else
        {
            if(force || savedLoadedClass == null)
            {
                c = findClass(name);
                savedLoadedClass = c;
            }
            else
            {
                c = savedLoadedClass;
            }
        }
        return c;
    }

    protected Class findClass(String className)
        throws ClassNotFoundException
    {
        // Err.pr( "&&&&&&&&&  Calling findClass with " + className);

        byte classData[];
        // Try to load it from the basePath directory.
        classData = getTypeFromBasePath(className);
        if(classData == null)
        {
            throw new ClassNotFoundException(className);
        }

        // Parse it
        Class clazz = null;
        try
        {
            clazz = defineClass(className, classData, 0, classData.length);
        }
        catch(NoClassDefFoundError err)
        {
            clazz = defineClass(null, classData, 0, classData.length);
            Err.pr(
                "Have already loaded " + clazz.getName()
                    + " using the system class loader");
            Err.error(err);
        }
        return clazz;
    }

    private byte[] getTypeFromBasePath(String typeName)
    {
        FileInputStream fis;
        String base = basePath + File.separatorChar;
        String tail = typeName.replace('.', File.separatorChar) + ".class";
        String fileName = null;
        if(basePath == null || "".equals(basePath))
        {
            fileName = tail;
        }
        else
        {
            fileName = base + tail;
        }
        try
        {
            fis = new FileInputStream(fileName);
        }
        catch(FileNotFoundException e)
        {
            Print.pr("Not found: " + fileName);
            return null;
        }

        BufferedInputStream bis = new BufferedInputStream(fis);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Err.pr("file name will try to use to load is " + fileName);
        try
        {
            int c = bis.read();
            while(c != -1)
            {
                out.write(c);
                c = bis.read();
            }
        }
        catch(IOException e)
        {
            return null;
        }
        Err.pr("file name have used to load is " + fileName);
        return out.toByteArray();
    }

    public boolean isForce()
    {
        return force;
    }

    public void setForce(boolean force)
    {
        this.force = force;
    }
}
