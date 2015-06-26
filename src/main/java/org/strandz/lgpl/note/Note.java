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
package org.strandz.lgpl.note;

import java.awt.Toolkit;

public abstract class Note
{
    private String name;
    private String description;
    private String ongoingConcerns;
    //private String designConcerns;
    private String fix;
    private boolean visible = true;
    private boolean causeProblem = true;
    private boolean fixed = false;

    /**
     * To make it easier to search for the ones that are on - so
     * can ensure all debug messages are turned off before deploy.
     * Just search for 'SHOW'.
     */
    public static final boolean SHOW = true;
    public static final boolean HIDE = false;
    /**
     * For debugging how many times an event associated with this Note has occured 
     */
    private int times;
    
    private static int timesConstructed;
    public int id;

//    public Note(String name, String description)
//    {
//        this();
//        this.name = name;
//        this.description = description;
//    }
    
    public Note( boolean visible)
    {
        this( null, visible);
    }
    
    public Note()
    {
        this( null);
    }
    
    public Note( String name)
    {
        this( name, HIDE);
    }
    
    public Note( String name, boolean visible)
    {
        timesConstructed++;
        id = timesConstructed;
        /*
        Err.pr( "ItemStatus ### CREATED id: " + id);
        if(id == 0)
        {
        Err.stack();
        }
        */
        setName( name);
        setVisible( visible);
    }

    public String toString()
    {
        return name;
    }

    public int compareTo(Object obj)
    {
        Problem.error("compareTo() needs to work on severity");
        return -99;
    }

    public boolean equals(Object that)
    {
        return this.equals((Note) that);
    }

    public boolean equals(Note o)
    {
        boolean result = false;
        if(o == this)
        {
            result = true;
        }
        else
        {
            Note test = o;
            if((name == null ? test.name == null : name.equals(test.name)))
            {
                result = true;
            }
        }
        return result;
    }

    public int hashCode()
    {
        int result = 17;
        result = 37 * result + (name == null ? 0 : name.hashCode());
        return result;
    }

    public String getName()
    {
        return name;
    }

    //TODO make this private
    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public boolean isVisible()
    {
        return visible;
    }

    private void setVisible(boolean visible)
    {
        this.visible = visible;
    }

    public String getOngoingConcerns()
    {
        return ongoingConcerns;
    }

    public void setOngoingConcerns(String ongoingConcerns)
    {
        this.ongoingConcerns = ongoingConcerns;
    }

    public String getFix()
    {
        return fix;
    }

    public void setFix(String howFixed)
    {
        this.fix = howFixed;
    }

    /**
     * To avoid two-way dependencies we have a simple Err
     * class here, which is to be only used by classes in
     * the bug package.
     */
    private static class Problem
    {
        public static void error(String s)
        {
            Toolkit.getDefaultToolkit().beep();
            throw new Error(s);
        }
    }

    public boolean isCauseProblem()
    {
        return causeProblem;
    }

    public void setCauseProblem(boolean causeProblem)
    {
        this.causeProblem = causeProblem;
    }

    public void setFixed(boolean b)
    {
        this.fixed = b;
    }

    public void incTimes()
    {
        if(isVisible())
        {
            times++;
        }
    }

    public void debug()
    {
        if(isVisible())
        {
            System.err.println( "DEBUG HERE");
        }
    }

    public int getTimes()
    {
        int result = Integer.MIN_VALUE; //values that will never match with anything incremented from 0
        if(isVisible())
        {
            result = times;
        }
        return result;
    }
}
