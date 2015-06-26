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

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * This class loader will make calls to the ClassLoader it extends for all classes except
 * those .class files that are in the directory/package specified in the constructor. These
 * are the special files, or dynamicDirectoryFiles.
 * <p/>
 * This classloader will be used to load all the files for a few lines of code. The normal
 * classloader can then be put back again once the loading of 'unknown at initial load time'
 * classes is complete.
 * <p/>
 * Classes that have already been dynamically loaded are not usually loaded again. To override
 * this set force to true.
 *
 * @author Chris Murphy
 */
public class SpecialClassLoader extends ClassLoader
{
    // basePath gives the path to which this class
    // loader appends "/.class" to get the
    // full path name of the class file to load
    protected String basePath;
    // protected String fullFilePath;
    private List savedLoadedClasses = new ArrayList();
    //private boolean currentlyLoading = false;
    private boolean force = false;
    private Class savedLoadedClass;
    List dynamicDirectoryFiles;
    private boolean debugging = false; // see constructor to change
    public int id;
    private String msgId;

    public SpecialClassLoader(
        ClassLoader parent,
        String basePath,
        String packagePrefix,
        boolean debugging)
    {
        super(parent);
        this.basePath = basePath;
        Assert.isFalse( this.debugging);
        this.debugging = debugging;
        //Uncomment to quickly find the culprit:
        //Assert.isFalse( this.debugging, "Comment out this line if you have chosen to debug classloading");
        pr("basePath: " + basePath);
        if(basePath != null && basePath.indexOf("eclipse") != -1)
        {
            Err.error("Not using this DIR anymore: " + basePath);
        }
        pr("packagePrefix: " + packagePrefix);
        if(packagePrefix != null)
        {
            setSpecialFiles(basePath, packagePrefix);
        }
        else
        {
            dynamicDirectoryFiles = new ArrayList();
        }
    }
    
    public void setSpecialFiles( String packagePrefix)
    {
        setSpecialFiles( basePath, packagePrefix);
    }    

    private void setSpecialFiles(String basePath, String packagePrefix)
    {
        Assert.notBlank( basePath, "Perhaps DataClassLoader needs this method to be public");
        if(dynamicDirectoryFiles != null && !dynamicDirectoryFiles.isEmpty())
        {
            // Err.error( "Not intended to overwrite special files");
            // Got an unsupported operation on AbstractList, so create again
            // specialFiles.clear();
            dynamicDirectoryFiles = new ArrayList();
        }
        String full;
//        if(!FileUtils.directoryExists( basePath + File.separatorChar + "classes"))
//        {
            full = NameUtils.getFullFilePath( basePath, packagePrefix);
//        }
//        else
//        {
//            full = NameUtils.getFullFilePath( basePath + File.separatorChar + "classes", packagePrefix);
//        }
        // fullFilePath = full;
        File dir = new File(full);
        // Err.pr( "? Get a diry from " + full);
        if(!dir.exists())
        {
            // field validation s/prevent this from ever happening
            Err.error("The directory <" + full + "> does not exist");
        }
        // s/really recursively call dir.list() here (or employ JavaDirectory)
        // Got an NPE in here if somehow validation failed on the existence of dir
        dynamicDirectoryFiles = Arrays.asList(dir.list(new ClassFilesOnly()));
        NameUtils.trimEnding(dynamicDirectoryFiles, ".class");
        Utils.prefixBeginning(dynamicDirectoryFiles, packagePrefix + ".");
        if(debugging)
        {
            Print.prList( dynamicDirectoryFiles, "Names of class files might load");
        }
    }

    private static class ClassFilesOnly implements FilenameFilter
    {
        public boolean accept(File dir, String name)
        {
            boolean result = false;
            if(name.endsWith(".class"))
            {
                result = true;
            }
            return result;
        }
    }

    protected void pr(String txt)
    {
        if(debugging)
        {
            Err.pr(txt);
        }
    }

    public void loadSpecialFiles()
    {
        for(Iterator iter = dynamicDirectoryFiles.iterator(); iter.hasNext();)
        {
            String name = (String) iter.next();
            try
            {
                loadClass(name);
            }
            catch(ClassNotFoundException ex)
            {
                Err.error(ex);
            }
        }
    }

    protected synchronized Class loadClass(String name,
                                           boolean resolve)
        throws ClassNotFoundException
    {
        /*
        if(name.equals( "ml.applichousing.SdzBag"))
        {
        Err.pr( "&&&&&&&&&  Calling loadClass with " + name);
        }
        */
        Class c;
        if(!dynamicDirectoryFiles.contains(name) || !isCurrentlyLoading())
        {
            c = super.loadClass(name, resolve);
        }
        else
        {
            if(!savedLoadedClasses.contains(name))
            {
                pr("To load dynamically: " + name + " in ID: " + id);
                if(debugging)
                {
                    Print.prList(savedLoadedClasses, "SpecialClassLoader.loadClass()");
                }
                try
                {
                    c = findClass(name);
                    savedLoadedClasses.add(name);
                }
                catch(LinkageError err) // java.lang.LinkageError: duplicate class definition: view/petstore/AccountPanel
                {
                    c = Class.forName(name, true, this);
                }
            }
            else
            {
                if(!force)
                {
                    pr("Getting from a saved cache: " + name);

                    String className = (String) savedLoadedClasses.get(
                        savedLoadedClasses.indexOf(name));
                    c = Class.forName(className, true, this);
                }
                else
                {
                    if(force || savedLoadedClass == null)
                    {
                        pr("Force on, so reloading: " + name);
                        c = findClass(name);
                        savedLoadedClass = c;
                    }
                    else
                    {
                        c = savedLoadedClass;
                    }
                }
            }
        }
        return c;
    }

    protected Class findClass(String className)
        throws ClassNotFoundException
    {
        if(debugging)
        {
            Err.pr("&&&&&&&&&  Calling findClass with <" + className + "> for id " + id);
            if(className.endsWith(".null"))
            {
                Err.error("Could not find class called <" + className + ">");
            }
        }

        byte classData[];
        // Try to load it from the basePath directory.
        classData = getTypeFromBasePath(className);
        if(classData == null)
        {
            if(debugging)
            {
                Print.prList(savedLoadedClasses, "savedLoadedClasses, not found for " + className);
            }
            throw new ClassNotFoundException(formFileName(className) + " when loading " + msgId);
        }

        // Parse it
        Class clazz;
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
        catch(LinkageError err) // java.lang.LinkageError: duplicate class definition: org/strandz/view/supersix/OtherMatchDetailsPanel
        {
            clazz = Class.forName(className, true, this);
        }
        return clazz;
    }

    private String formFileName(String typeName)
    {
        String result = null;
        String base = basePath + File.separatorChar;
        String tail = typeName.replace('.', File.separatorChar) + ".class";
        if(basePath == null || "".equals(basePath))
        {
            result = tail;
        }
        else
        {
            result = base + tail;
        }
        return result;
    }

    private byte[] getTypeFromBasePath(String typeName)
    {
        FileInputStream fis;
        String fileName = formFileName(typeName);
        try
        {
            fis = new FileInputStream(fileName);
        }
        catch(FileNotFoundException e)
        {
            // Err.pr( "Not found: " + fileName);
            return null;
        }

        BufferedInputStream bis = new BufferedInputStream(fis);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        // Err.pr( "file name will try to use to load is " + fileName);

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
            Err.pr(
                "Problem reading file <" + fileName + "> into a ByteArrayOutputStream");
            return null;
        }
        // Err.pr( "file name have used to load is " + fileName);

        return out.toByteArray();
    }

    public boolean isCurrentlyLoading()
    {
        return msgId != null;
    }

    public void setCurrentlyLoading(String msgId)
    {
        if(msgId == null)
        {
            //null is supposed to come through when have finished
            //Err.pr( "With no msgId set we won't be able to properly instrument not being able to find a class");
        }
        this.msgId = msgId;
    }

    public boolean isForce()
    {
        return force;
    }

    /**
     * Used when are going to reload one particular class
     */
    public void setForce(boolean force, String className)
    {
        this.force = force;
        //this.currentlyLoading = force;
        msgId = "FORCE for " + className;
        if(force)
        {
            savedLoadedClass = null;
        }
    }
}
