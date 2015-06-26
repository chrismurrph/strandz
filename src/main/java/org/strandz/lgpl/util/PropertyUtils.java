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

import java.io.*;
import java.util.Properties;

public class PropertyUtils
{
    private static final boolean SHOW_PROPS_FILE_READ = false;
    private static int times;
    
    public static Properties getPortableProperties(String name, Object classLoadedObject)
    {
        return getPortableProperties(name, classLoadedObject, false);
    }
    
    private static void readProps( String name, Properties result)
    {
        if(result != null && SHOW_PROPS_FILE_READ)
        {
            times++;
            Err.pr( "Have read properties from <" + name + "> times " + times);
            if(times == 0)
            {
                Err.stack();
            }
        }
    }
    
    /**
     * Wrapper around getProperties() so the properties can be got from inside one
     * of the available jar files, without us knowing any details of the jar
     * file. This is equivalent to the portable part of PortableImageIcon.
     * <p/>
     * An area for improvement would be to cache here. We should expect the user to
     * call this method from many different parts of the application. A method to
     * evacuate this cache should also be available.
     *
     * @param name The name of a file stored in a jar, accessible thru classpath
     * @return Connection properties extracted from the name
     */
    public static Properties getPortableProperties(String name, Object classLoadedObject, boolean nullRetOk)
    {
        Properties result = null;
        ClassLoader cl = classLoadedObject.getClass().getClassLoader();
        if(cl == null)
        {
            Err.error("Could not find a class loader from " + cl);
        }
        InputStream inputStream = cl.getResourceAsStream(name);
        if(inputStream == null && !nullRetOk)
        {
            //available jars: 
            Err.pr( "ClassPath: <" + System.getProperty( "java.class.path") + ">");
            Err.error("Cannot find <" + name + 
                    "> as a 'runtime available' resource - this means a required " +
                    "jar file is not in the classpath, or you have incorrectly specified the name of the resource");
        }
        else if(inputStream != null)
        {
            result = getProperties(inputStream, name);
        }
        readProps( name, result);
        return result;
    }
    
    public static String getProperty(String propName, Properties props)
    {
        return getProperty(propName, props, false);
    }

    public static String getProperty(String propName, Properties props, boolean nullRetOk)
    {
        String result = (String) props.get(propName);
        if(result == null && !nullRetOk)
        {
            Print.prMap(props);
            Err.error("Not found a property value for key <" + propName + ">");
        }
        return result;
    }

    private static Properties getProperties(InputStream propStream, String name)
    {
        Properties result = new Properties();
        if(propStream == null)
        {
            Err.error("Cannot get properties from a null InputStream");
        }
        try
        {
            result.load(propStream);
            // Print.prMap( props);
        }
        catch(IOException e)
        {
            Err.error(e);
        }
        readProps( name, result);
        return result;
    }

    public static Properties getProperties(String path)
    {
        return getProperties(path, false);
    }
    
    public static Properties getProperties(String path, boolean notFoundOk)
    {
        Properties result = null;
        InputStream propStream = null;
        try
        {
            propStream = new FileInputStream(path);
        }
        catch(FileNotFoundException e)
        {
            if(!notFoundOk)
            {
                Err.error(e);
            }
        }
        if(propStream != null)
        {
            result = getProperties(propStream, path);
        }
        return result;
    }

    public static void writeProperties( File file, Properties properties)
    {
        if(file != null)
        {
            FileOutputStream out = null;
            try
            {
                out = new FileOutputStream( file);
            }
            catch(FileNotFoundException e)
            {
                Err.error( e);
            }
            if(out != null)
            {
                try
                {
                    properties.store(out, "---No Comment---");
                    out.close();
                }
                catch(IOException e)
                {
                    Err.error( e);
                }
            }
            else
            {
                Err.error();
            }
        }
        else
        {
            Err.error( "Need to have a file to write properties to");
        }
    }
}
