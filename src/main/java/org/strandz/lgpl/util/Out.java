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

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
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

/**
 * This class has methods for outputting as text many different types of
 * fairly standard classes as an alternative to the way the native toString()
 * method does it. All methods in here are for debugging purposes. Thus if
 * Out.prXXX() needs to be working in production, copy the method into a new
 * class whose name reflects what it is to be used for.
 * <p/>
 * Note that the above stated rule is for production time. During development
 * it is quite ok to use Out.pr().
 * <p/>
 * Although a static class, it does not have a p at the front of it, for brevity.
 *
 * @author Chris Murphy
 */
public class Out
{
    private static PrintStream outStream = null;
    private static PrintStream oldOutStream = null;
    private static final boolean typeInfoIncluded = false;
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
        Out.pr("NUMBER: " + i);
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
            s = System.out;
        }
        s.print(o);
    }

    private static void _println(Object o)
    {
        PrintStream s = outStream;
        if(s == null)
        {
            s = System.out;
        }
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
        if(objs == null)
        {
            Err.error("method prArray does not handle a null param");
        }

        List list = Arrays.asList(objs);
        prList(list, id);
    }

    public static void prCollection(Collection list)
    {
        String heading = "pr for Collection of type " + list.getClass().getName();
        Out.pr(heading);
        Out.pr("------------------------");
        for(Iterator iter = list.iterator(); iter.hasNext();)
        {
            Object element = iter.next();
            Out.pr("\tvalue: " + element + ", type: " + element.getClass().getName());
        }
        Out.pr("------------------------");
    }

    public static void prIterator(Iterator it)
    {
        // Err.stack();
        String heading = "pr for Iterator of type " + it.getClass().getName();
        Out.pr(heading);
        Out.pr("------------------------");
        for(; it.hasNext();)
        {
            Object element = it.next();
            Out.pr("\tvalue: " + element);
        }
        Out.pr("------------------------");
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
                if(typeInfoIncluded)
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
                if(typeInfoIncluded)
                {
                    result.add("\tvalue: " + str + ", class: " + type);
                }
                else
                {
                    result.add("\tvalue: " + str);
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
        if(errOnNull)
        {
            Utils.chkNoNulls(list, id);
        }
        Out.pr(
            Utils.getStringBufferFromList(getPrList(list, id, new ArrayList())).toString());
    }

    public static void prSet(Set list)
    {
        // Err.stack();
        String heading = "Set in prSet()";
        /*
        if(list != null)
        {
        heading = "[" + list.size() + "] pr for List of type " + list.getClass().getName();
        if(!list.isEmpty())
        {
        Object firstValue = list.get(0);
        if(firstValue != null)
        {
        heading += " containing " + list.get(0).getClass().getName();
        }
        else
        {
        heading += " containing " + list.get(0);
        }
        }
        }
        */
        Out.pr(heading);
        Out.pr("------------------------");
        if(list != null)
        {
            for(Iterator iter = list.iterator(); iter.hasNext();)
            {
                Object element = iter.next();
                String type = "";
                if(element != null)
                {
                    type = element.getClass().getName();
                }
                Out.pr("\tvalue: " + element + ", class: " + type);
            }
        }
        Out.pr("------------------------");
    }

    public static void prMap(Map map)
    {
        String heading = "null Map in prMab()";
        if(map != null)
        {
            heading = "pr for Map of type " + map.getClass().getName();
        }
        Out.pr(heading);
        Out.pr("------------------------");
        /*
        times++;
        if(times == 2)
        {
          Err.stack();
        }
        */
        if(map != null)
        {
            for(Iterator iter = map.keySet().iterator(); iter.hasNext();)
            {
                Object key = iter.next();
                Out.pr("\tkey: " + key + ", value: " + map.get(key));
            }
        }
        Out.pr("------------------------");
    }

    public static void prObject(Object obj)
    {
        Out.pr("\ttype: " + obj.getClass().getName());
        Out.pr("\tvalue: " + obj);
    }

    public static void prEnumeration(Enumeration toRemoveList)
    {
        Out.pr("------------------------");
        for(Enumeration iter = toRemoveList; iter.hasMoreElements();)
        {
            Object element = iter.nextElement();
            String type = "";
            if(element != null)
            {
                type = element.getClass().getName();
            }
            Out.pr("\tvalue: " + element + ", class: " + type);
        }
        Out.pr("------------------------");
    }

    public static void prContents(Container cont)
    {
        List list = ComponentUtils.getAllControls(cont);
        Out.pr("------------------------");
        for(Iterator iter = list.iterator(); iter.hasNext();)
        {
            Object element = iter.next();
            String type = "";
            if(element != null)
            {
                type = element.getClass().getName();
            }
            Out.pr("\tvalue: " + element + ", class: " + type);
        }
        Out.pr("------------------------");
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
        Out.pr(heading);
        Out.pr("------------------------");
        for(Iterator iter = list.iterator(); iter.hasNext();)
        {
            Object element = iter.next();
            if(Utils.instanceOf(element, JPanel.class))
            {
                Out.pr("\tname: " + ((JPanel) element).getName());
            }
        }
        Out.pr("------------------------");
    }

    public static void prTabs(JTabbedPane tabbedPane)
    {
        Out.pr("-TABS-------------------");

        int count = tabbedPane.getTabCount();
        for(int i = 0; i < count; i++)
        {
            Out.pr("\tTAB: " + i + ":" + tabbedPane.getTitleAt(i));
        }
        Out.pr("------------------------");
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
                    Out.pr(rs.getString(3));
                }
                /*
                Out.pr( rs.getString( 1) + " " + rs.getString( 2) + " " +
                rs.getString( 3) + " " + rs.getString( 4) + " " +
                rs.getString( 5) + " " + rs.getString( 6));
                */
                count++;
            }
            if(count == 0)
            {
                Out.pr(rs + " contains no results!");
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
                Out.pr(
                    rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3)
                        + " " + rs.getString(4) + " " + rs.getString(5) + " "
                        + rs.getString(6));
                count++;
            }
            if(count == 0)
            {
                Out.pr(rs + " contains no results!");
            }
        }
        catch(SQLException ex)
        {
            Err.error(ex);
        }
    }
}
