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

import javax.swing.ActionMap;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.InputMap;
import javax.swing.KeyStroke;
import java.awt.Component;
import java.awt.Container;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.beans.PropertyDescriptor;

/**
 * This class has methods for outputting as text many different types of
 * fairly standard classes as an alternative to the way the native toString()
 * method does it. All methods in here are for debugging purposes. Thus if
 * Print.prXXX() needs to be working in production, copy the method into a new
 * class whose name reflects what it is to be used for.
 * <p/>
 * Note that the above stated rule is for production time. During development
 * it is quite ok to use Print.pr().
 * <p/>
 * Although a static class, it does not have a p at the front of it, for brevity.
 *
 * @author Chris Murphy
 */
public class Print
{
    private static PrintStream outStream = null;
    private static PrintStream oldOutStream = null;
    private static final boolean TYPE_INFO_INCLUDED = true;
    private static boolean debug = false;
    private static int times;

    public static void setOutStream(String fileName)
    {
        if(outStream == null)
        {
            PrintStream ps = null;
            if(fileName != null)
            {
                if(oldOutStream != null)
                {
                    ps = oldOutStream;
                }
                else
                {
                    oldOutStream = ps = FileUtils.createPrintStream(fileName);
                }
            }
            setOutStream(ps);
        }
        else
        {
            if(fileName == null)
            {
                setOutStream((PrintStream) null);
            }
        }
    }

    private static void setOutStream(PrintStream s)
    {
        outStream = s;
    }

    public static void setDebug(boolean b)
    {
        debug = b;
    }

    public static boolean isDebug()
    {
        return debug;
    }

    public static void pr(int i)
    {
        Print.pr("NUMBER: " + i);
    }

    public static void pr(Object s)
    {
        if(debug)
        {
            times++;
            if(s != null)
            {
                _println(s.toString() + ", " + times);
            }
            else
            {
                _println(s + ", " + times);
            }
            if(times == 0)
            {
                Err.stack();
            }
        }
        else
        {
            _println(s);
        }
        _flush();
    }

    public static void print(Object s)
    {
        if(debug)
        {
            times++;
            if(s != null)
            {
                _print(s.toString() + ", " + times);
            }
            else
            {
                _print(s + ", " + times);
            }
            if(times == 0)
            {
                Err.stack();
            }
        }
        else
        {
            _print(s);
        }
        _flush();
    }

    private static void _print(Object o)
    {
        PrintStream s = outStream;
        if(s == null)
        {
            s = System.err;
        }
        s.print(o);
    }

    private static void _println(Object o)
    {
        PrintStream s = outStream;
        if(s == null)
        {
            s = System.err;
        }
//        if("overnight".equals( o))
//        {
//            Err.stack();
//        }
        s.println(o);
    }

    private static void _flush()
    {
        PrintStream s = outStream;
        if(s == null)
        {
            s = System.err;
        }
        s.flush();
    }

    public static void prThrowable(Throwable th, String id)
    {
        if(th.getCause() != null)
        {
            prThrowable(th.getCause(), id);
        }
        prArray(th.getStackTrace(), id);
    }

    public static void prArray(Object objs[], String id)
    {
        prArray( objs, id, true);
    }
    
    public static void prArray(Object objs[], String id, boolean errOnNull)
    {
        if(objs == null)
        {
            Err.error("method prArray does not handle a null param");
        }

        List list = Arrays.asList(objs);
        prList(list, id, errOnNull);
    }

    public static void prArray(float objs[], String id)
    {
        if(objs == null)
        {
            Err.error("method prArray does not handle a null param");
        }

        List list = Arrays.asList(objs);
        prList(list, id);
    }

    public static void prPropertyDescriptorArray(PropertyDescriptor pds[], String id)
    {
        List list = Arrays.asList( pds);
        Utils.chkNoNulls(list, id);
        Print.pr( id);
        for(int i = 0; i < list.size(); i++)
        {
            PropertyDescriptor pd = (PropertyDescriptor) list.get(i);
            Print.pr( "NAME: <" + pd.getName() + ">, READ METHOD: <" + 
                pd.getReadMethod() + ">, WRITE METHOD: <" + pd.getWriteMethod() + ">");
        }
        Print.pr( "");
    }

    private static String osDep(String s)
    {
        String result;
        if(!Utils.OS_NAME.startsWith("Windows"))
        {
            result = s + "\r";
        }
        else
        {
            result = s;
        }
        return result;
    }

    public static void prCollection(Collection collection, String id)
    {
        prCollection( collection, id, true);
    }
    
    public static void prCollection(Collection collection, String id, boolean errOnNull)
    {
        Assert.notNull( collection, "Collection asking to pr() is null");
        if(errOnNull)
        {
            Utils.chkNoNulls(collection, id);
        }
        String heading = "%%" + id + "%% pr for Collection of type " + collection.getClass().getName();
        Print.pr(heading);
        Print.pr("------------------------");
        for(Iterator iter = collection.iterator(); iter.hasNext();)
        {
            Object element = iter.next();
            if(element == null)
            {
                Print.pr( osDep( "\tvalue: " + element));
            }
            else
            {
                Print.pr( osDep( "\tvalue: " + element + ", type: " + element.getClass().getName()));
            }
        }
        Print.pr("------------------------");
    }

    public static void prIterator(Iterator it)
    {
        // Err.stack();
        String heading = "pr for Iterator of type " + it.getClass().getName();
        Print.pr(heading);
        Print.pr("------------------------");
        for(; it.hasNext();)
        {
            Object element = it.next();
            Print.pr("\tvalue: " + element);
        }
        Print.pr("------------------------");
    }

    public static List getPrList(List list, String id, List result)
    {
        String heading = "%%" + id + ", null List in getPrList()";
        if(list != null)
        {
            heading = "%%" + id + ", size [" + list.size() + "] List of type "
                + list.getClass().getName();
            if(!list.isEmpty())
            {
                //Err.stack();
                Object firstValue = list.get(0);
                if(TYPE_INFO_INCLUDED)
                {
                    if(firstValue != null)
                    {
                        heading += " containing (first element anyway) "
                            + list.get(0).getClass().getName();
                    }
                    else
                    {
                        heading += " containing (first element anyway) " + list.get(0);
                    }
                }
            }
        }
        result.add(heading);
        result.add("------------------------");
        if(list != null)
        {
            for(Iterator iter = list.iterator(); iter.hasNext();)
            {
                Object element = iter.next();
                String str = null;
                if(element != null)
                {
                    str = element.toString();
                    if(!(element instanceof Class) && Utils.instanceOf(element, Component.class))
                    {
                        str = ((Component) element).getName();
                    }
                }

                String type = "";
                if(element != null)
                {
                    type = element.getClass().getName();
                }
                if(TYPE_INFO_INCLUDED)
                {
                    result.add(osDep("\tvalue: " + str + ", class: " + type));
                }
                else
                {
                    result.add(osDep("\tvalue: " + str));
                }
            }
        }
        result.add("------------------------");
        return result;
    }

    public static void prList(List list, String id)
    {
        prList(list, id, true);
    }

    public static void prList(List list, String id, boolean errOnNull)
    {
        //Err.stack();
        if(errOnNull)
        {
            Utils.chkNoNulls(list, id);
        }
        //This way doesn't format well on servers
        //Print.pr(Utils.getStringBufferFromList(getPrList(list, id, new ArrayList())).toString());
        List output = getPrList(list, id, new ArrayList());
        for(int i = 0; i < output.size(); i++)
        {
            String s = (String) output.get(i);
            Print.pr( s);
        }
    }

    public static void prSet(Set theSet)
    {
        // Err.stack();
        String heading = "";
        if(theSet != null)
        {
            heading = "[" + theSet.size() + "] pr for Set of type " + theSet.getClass().getName();
            if(!theSet.isEmpty())
            {
                Object theArray[] = theSet.toArray();
                /* Hmm -can't retrieve from a Set, but has toArray() method ... */
                Object firstValue = theArray[0];
                if(firstValue != null)
                {
                    heading += " containing " + theArray[0].getClass().getName();
                }
                else
                {
                    heading += " containing " + theArray[0];
                }
                /**/
            }
        }
        Print.pr(heading);
        Print.pr("------------------------");
        if(theSet != null)
        {
            for(Iterator iter = theSet.iterator(); iter.hasNext();)
            {
                Object element = iter.next();
                String type = "";
                if(element != null)
                {
                    type = element.getClass().getName();
                }
                Print.pr(osDep("\tvalue: " + element + ", class: " + type));
            }
        }
        Print.pr("------------------------");
    }
    
    public static void prPropertyDescriptorNames(PropertyDescriptor pds[])
    {
        String heading = "null PropertyDescriptor[] in prPropertyDescriptorNames()";
        if(pds != null)
        {
            heading = "pr for PropertyDescriptor[], size: " + pds.length;
        }
        Print.pr(heading);
        Print.pr("------------------------");
        if(pds != null)
        {
            for(int i = 0; i < pds.length; i++)
            {
                PropertyDescriptor pd = pds[i];
                Print.pr(osDep("\tname: " + pd.getName()));
            }
        }
        Print.pr("------------------------");
    }

    /**
     * Just a different way of calling prMap
     *
     * @param map
     */
    public static void prProperties(Map map)
    {
        prMap(map);
    }

    public static void prMap(Map map)
    {
        String heading = "null Map in prMab()";
        if(map != null)
        {
            heading = "pr for Map of type " + map.getClass().getName() + ", size: " + map.size();
        }
        Print.pr(heading);
        Print.pr("------------------------");
        /*
        times++;
        if(times == 1)
        {
          Err.stack();
        }
        */
        if(map != null)
        {
            for(Iterator iter = map.keySet().iterator(); iter.hasNext();)
            {
                Object key = iter.next();
                Print.pr(osDep("\tkey: " + key + ", key type: " + key.getClass().getName() + ", value: " + map.get(key)));
            }
        }
        Print.pr("------------------------");
    }

    public static void prMap(ActionMap map)
    {
        String heading = "null Map in prMab()";
        if(map != null)
        {
            heading = "pr for Map of type " + map.getClass().getName() + ", size: " + map.size();
        }
        Print.pr(heading);
        Print.pr("------------------------");
        /*
        times++;
        if(times == 2)
        {
          Err.stack();
        }
        */
        if(map != null)
        {
            Object keys[] = map.allKeys();
            for(int i = 0; i < keys.length; i++)
            {
                Object key = keys[i];
                Print.pr(osDep("\tkey: " + key + ", key type: " + key.getClass().getName() + ", value: " + map.get(key)));
            }
        }
        Print.pr("------------------------");
    }
    
    public static void prMap(InputMap map)
    {
        String heading = "null Map in prMap()";
        if(map != null)
        {
            heading = "pr for Map of type " + map.getClass().getName() + ", size: " + map.size();
        }
        Print.pr(heading);
        Print.pr("------------------------");
        /*
        times++;
        if(times == 2)
        {
          Err.stack();
        }
        */
        if(map != null)
        {
            KeyStroke keys[] = map.allKeys();
            for(int i = 0; i < keys.length; i++)
            {
                KeyStroke key = keys[i];
                Print.pr(osDep("\tkey: " + key + ", key type: " + key.getClass().getName() + ", value: " + map.get(key)));
            }
        }
        Print.pr("------------------------");
    }
    
    public static void prObject(Object obj)
    {
        Print.pr("\ttype: " + obj.getClass().getName());
        Print.pr("\tvalue: " + obj);
    }

    public static void prEnumeration(Enumeration toRemoveList)
    {
        Print.pr("------------------------");
        for(Enumeration iter = toRemoveList; iter.hasMoreElements();)
        {
            Object element = iter.nextElement();
            String type = "";
            if(element != null)
            {
                type = element.getClass().getName();
            }
            Print.pr(osDep("\tvalue: " + element + ", class: " + type));
        }
        Print.pr("------------------------");
    }

    public static void prContents(Container cont)
    {
        List list = ComponentUtils.getAllControls(cont);
        Print.pr("------------------------");
        for(Iterator iter = list.iterator(); iter.hasNext();)
        {
            Object element = iter.next();
            String type = "";
            if(element != null)
            {
                type = element.getClass().getName();
            }
            Print.pr(osDep("\tvalue: " + element + ", class: " + type));
        }
        Print.pr("------------------------");
    }

    /**
     * Just panels within panels. This is not how the Designer sees that
     * world, but that involves ControlSignatures as well. To see how
     * Designer sees the world look at code for ControlAttributeReassigner.
     *
     * @param panel
     */
    public static void prPanelHierarchy(Container panel)
    {
        List list = ComponentUtils.getAllControls(panel);
        String heading = "pr for null panel in prPanel()";
        if(panel != null)
        {
            heading = "pr for panel named " + panel.getName();
        }
        Print.pr(heading);
        Print.pr("------------------------");
        for(Iterator iter = list.iterator(); iter.hasNext();)
        {
            Object element = iter.next();
            if(Utils.instanceOf(element, JPanel.class))
            {
                Print.pr(osDep("\tname: " + ((JPanel) element).getName()));
            }
        }
        Print.pr("------------------------");
    }

    public static void prTabs(JTabbedPane tabbedPane)
    {
        Print.pr("-TABS-------------------");

        int count = tabbedPane.getTabCount();
        for(int i = 0; i < count; i++)
        {
            Print.pr(osDep("\tTAB: " + i + ":" + tabbedPane.getTitleAt(i)));
        }
        Print.pr("------------------------");
    }

    public static void prTableResultSet(ResultSet rs)
    {
        try
        {
            int count = 0;
            while(rs.next())
            {
                if(!rs.getString(4).equals("SYSTEM TABLE"))
                {
                    Print.pr(rs.getString(3));
                }
                /*
                 Print.pr( rs.getString( 1) + " " + rs.getString( 2) + " " +
                 rs.getString( 3) + " " + rs.getString( 4) + " " +
                 rs.getString( 5) + " " + rs.getString( 6));
                 */
                count++;
            }
            if(count == 0)
            {
                Print.pr("Returned ResultSet is empty");
            }
        }
        catch(SQLException ex)
        {
            Err.error(ex);
        }
    }

    public static void prResultSet(ResultSet rs)
    {
        try
        {
            int count = 0;
            while(rs.next())
            {
                Print.pr(rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3)
                    + " " + rs.getString(4) + " " + rs.getString(5) + " "
                    + rs.getString(6));
                count++;
            }
            if(count == 0)
            {
                Print.pr(rs + " contains no results!");
            }
        }
        catch(SQLException ex)
        {
            Err.error(ex);
        }
    }

//Can't just pop them all off when want to view them!    
//    public static void prStack(Stack<Object> stack)
//    {
//        Stack stackToView = new Stack( stack);
//        for(int i=0; !stack.empty(); i++)
//        {
//            Object entry = stack.pop();
//            String tab = "\t";
//            String tabs = null;
//            for(int j = 0; j <= i-1; j++)
//            {
//                tabs += tab;               
//            }
//            Print.pr( tabs + entry);
//        }
//    }

    public static void prMatrix(List listOfLists)
    {
        for(int i = 0; i < listOfLists.size(); i++)
        {
            StringBuffer row = new StringBuffer(); 
            List list = (List)listOfLists.get(i);
            if(list.isEmpty())
            {
                row.append( "Empty list in row " + i);                
            }
            else
            {
                for(int j = 0; j < list.size(); j++)
                {
                    Object o = list.get(j);
                    row.append( "[");       
                    if(o instanceof ShortDescI)
                    {
                        row.append( ((ShortDescI)o).getShortDesc());
                    }
                    else
                    {
                        row.append( o);
                    }
                    row.append( "]");        
                }
            }
            Err.pr( row);
        }
    }
}
