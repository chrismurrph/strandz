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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.beans.XMLEncoder;
import java.beans.XMLDecoder;

/**
 * This static class provides an API to the Reflection API, enabling us to instantiate
 * objects whose classes were unknown at compile time.
 *
 * @author Chris Murphy
 */
public class ObjectFoundryUtils
{
    /**
     * Instantiate a class from the class name
     *
     * @param p The class name
     * @return The created object or null
     */
    public static Object factory(String p)
    {
        return factory( p, false);
    }
    
    /**
     * Instantiate a class from the class name
     *
     * @param p The class name
     * @return The created object or null
     */
    public static Object factory(String p, boolean nullOk)
    {
        Object result = null;
        Class c = null;
        boolean ok = true;
        try
        {
            c = Class.forName(p);
        }
        catch(ClassNotFoundException e)
        {
            ok = false;
            if(!nullOk) Err.error(e.toString());
        }
        if(ok)
        {
            try
            {
                result = c.newInstance();
            }
            catch(IllegalAccessException e)
            {
                if(!nullOk) Err.error(e.toString());
            }
            catch(InstantiationException e)
            {
                if(!nullOk) Err.error(e, "Cannot instantiate an interface or abstract class");
            }
        }
        return result;
    }

    /**
     * Instantiate a class from the class name and the constructor argument values
     *
     * @param className The name of the class
     * @param args      The argument values
     * @return The created object or null
     */
    public static Object factory(String className,
                                 Object[] args)
    {
        Object result = null;
        try
        {
            result = factory(Class.forName(className), args);
        }
        catch(ClassNotFoundException e)
        {
            Err.error(e, "Could not recognise class with name " + className);
        }
        return result;
    }

    /**
     * Instantiate a class from a Class object
     *
     * @param clazz The Class object representing the class
     * @param args  The argument values
     * @return The created object or null
     */
    public static Object factory(Class clazz,
                                 Object[] args)
    {
        Class paramTypes[] = new Class[args.length];
        for(int i = 0; i <= args.length - 1; i++)
        {
            paramTypes[i] = args[i].getClass();
        }
        return factory(clazz, paramTypes, args);
    }

    /**
     * Instantiate a class from a Class object
     *
     * @param clazz      The Class object representing the class
     * @param paramTypes The types of the parameters of the constructor
     * @param args       The argument values
     * @return The created object or null
     */
    public static Object factory(Class clazz,
                                 Class[] paramTypes,
                                 Object[] args)
    {
        Constructor con = null;
        try
        {
            con = clazz.getConstructor(paramTypes);
        }
        catch(NoSuchMethodException e)
        {
            String params = new String();
            for(int j = 0; j <= paramTypes.length - 1; j++)
            {
                params += "\tParam: " + paramTypes[j];
            }
            Err.error(e,
                "Can not find constructor in " + clazz + " with params: \n" + params);
        }
        return factory(con, args);
    }

    /**
     * Instantiate a class from a Constructor
     *
     * @param c    The Constructor
     * @param args The argument values
     * @return The created object or null
     */
    public static Object factory(Constructor c,
                                 Object[] args)
    {
        Object o = null;
        try
        {
            o = c.newInstance(args);
        }
        catch(IllegalAccessException e)
        {
            Err.error(e.toString());
        }
        catch(InstantiationException e)
        {
            Err.error(e, "Cannot instantiate an interface or abstract class");
        }
        catch(InvocationTargetException e)
        {
            // Err.error(e.toString());
            SelfReferenceUtils.iteError(e, null, null, args);
        }
        catch(IllegalArgumentException e)
        {
            Err.error(e, "IllegalArgumentException trying to instantiate using constructor " + c + 
                    " with arguement " + args[0].getClass());
        }
        return o;
    }

    /**
     * Instantiate a class from an example object
     * @param obj The example object, which we are going to getClass() on
     * @return The created object or null
     */
    public static Object factoryObj(Object obj)
    {
        Class clazz = obj.getClass();
        return factory( clazz);
    }
    
//    public static Object factory( Clazz clazz)
//    {
//        return factory( clazz.getClassObject());
//    }

    public static Object factoryClazz( Clazz clazz, String id)
    {
        Object result = null;
        if(!clazz.isCanFactory())
        {
            Err.pr( "clazz.getClassObject(): " + clazz.getClassObject().getName());
            Err.error( "Not allowed to create a <" + clazz.getClassObject().getName() + "> (" + id +")");
        }
        else
        {
            //Err.pr( id);
            result = ObjectFoundryUtils.factory(clazz.getClassObject(), id);
        }
        return result;
    }

    public static Object factory( Class c)
    {
        return factory( c, (String)null);
    }

    /**
     * Instantiate a class from a Class object
     *
     * @param c The Class object representing the class we wish to instantiate
     * @return The created object or null
     */
    public static Object factory(Class c, String id)
    {
        Object o = null;
        try
        {
            o = c.newInstance();
        }
        catch(IllegalAccessException e)
        {
            Err.error(e.toString());
        }
        catch(InstantiationException e)
        {
            if(id != null)
            {
                Err.pr( id);
            }
            Err.error(e,
                "Cannot instantiate an interface or abstract class - or it does not " +
                        "have a nullary constructor - " + c.getName());
        }
        catch(NoSuchMethodError e)
        {
            Err.error(e,
                "Can only dynamically construct a new object "
                    + "if the constructor has no arguments");
        }
        return o;
    }

    /**
     * Create a deep (for JavaBeans anyway) copy of any object
     */
    public static Object copyConstruct(Object obj)
    {
        Object result;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        XMLEncoder encoder = new XMLEncoder(out);
        encoder.writeObject(obj);
        encoder.close();

        InputStream in = new ByteArrayInputStream(out.toByteArray());
        XMLDecoder decoder = new XMLDecoder(in);
        result = decoder.readObject();
        return result;
    }
    
    // returns a deep copy of an object
    public static Object deepCopy(Object oldObj)
    {
        Object result = null;
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        try
        {
            ByteArrayOutputStream bos = new ByteArrayOutputStream(); // A
            oos = new ObjectOutputStream(bos); // B
            // serialize and pass the object
            oos.writeObject(oldObj);   // C
            oos.flush();               // D
            ByteArrayInputStream bin =
                new ByteArrayInputStream(bos.toByteArray()); // E
            ois = new ObjectInputStream(bin);                  // F
            // return the new object
            result = ois.readObject(); // G
        }
        catch(IOException e)
        {
            Err.error("ObjectFoundryUtils.deepCopy", e);
        }
        catch(ClassNotFoundException e)
        {
            Err.error("ObjectFoundryUtils.deepCopy", e);
        }
        finally
        {
            try
            {
                if(oos != null)
                {
                    oos.close();
                }
                if(ois != null)
                {
                    ois.close();
                }
            }
            catch(IOException e)
            {
                Err.error("ObjectCloner.deepCopy", e);
            }
        }
        return result;
    }
}
