/*
    Strandz - an API that matches the user to the data.
    Copyright (C) 2007 Chris Murphy

    Strandz is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    Strandz is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Strandz; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

    In addition, as a special exception, Chris Murphy gives
    permission to link the code of this program with any Java software for
    which complete source code is available at no cost (even if distribution
    or modification of that source code is restricted), and distribute the
    results of that linking. You must obey the GNU General Public
    License in all respects for all of the code used other than this Java code.
    If you modify this file, you may extend this exception to your version of
    the file, but you are not obligated to do so. If you do not wish to do so,
    delete this exception statement from your version.

    The authors can be contacted via www.strandz.org
*/
package org.strandz.lgpl.util;

import java.util.List;
import java.util.Map;
import java.util.Collection;

public class Assert
{
    private static void outcome( String txt)
    {
        Err.error( txt);        
    }
    
    public static void isNull( Object obj)
    {
        isNull( obj, null);
    }
    
    public static void isNull( Object obj, String msg)
    {
        if(obj != null)
        {
            if(msg != null)
            {
                outcome( msg);
            }
            else
            {
                outcome( "Object of type " + obj.getClass().getName() + " should be null");
            }
        }
    }

    public static void notNull( Object obj)
    {
        notNull( obj, null);
    }
    
    public static void notNull( Object obj, String msg)
    {
        if(obj == null)
        {
            if(msg != null)
            {
                outcome( msg);
            }
            else
            {
                outcome( "Object should NOT be null");
            }
        }
    }
    
    public static void isBlank(String s)
    {
        isBlank( s, null);
    }
    
    public static void isBlank(String s, String msg)
    {
        if(!(s == null || s.equals("")))
        {
            if(msg != null)
            {
                outcome( msg);
            }
            else
            {
                outcome( "String should be blank");
            }
        }
    }
    
    public static void notBlank(String s)
    {
        notBlank( s, null);
    }
    
    public static void notBlank(String s, String msg)
    {
        if(s == null || s.equals(""))
        {
            if(msg != null)
            {
                outcome( msg);
            }
            else
            {
                outcome( "String should NOT be blank");
            }
        }
    }
    
    public static boolean isTrue( boolean b)
    {
        return isTrue( b, null);
    }
    
    public static boolean isTrue( boolean b, String msg)
    {
        if(!b)
        {
            if(msg != null)
            {
                outcome( msg);
            }
            else
            {
                outcome( "Should be true");
            }
        }
        return b;
    }
    
    public static void isFalse( boolean b)
    {
        isFalse( b, null);
    }
    
    public static void isFalse( boolean b, String msg)
    {
        if(b)
        {
            if(msg != null)
            {
                outcome( msg);
            }
            else
            {
                outcome( "Should be false");
            }
        }
    }

    public static void isPositive(int i)
    {
        if(i <= 0)
        {
            outcome( "Should be greater than zero");
        }
    }

    public static void oneOnly(Object obj1, Object obj2)
    {
        oneOnly( obj1, obj2, null);
    }
    
    /**
     * If one is not null, then the other must be null 
     */
    public static void oneOnly( Object obj1, Object obj2, String msg)
    {
        boolean err = false;
        if(obj1 == null && obj2 == null)
        {
            err = true;
        }
        else if(obj1 != null && obj2 != null)
        {
            err = true;
        }
        if(err)
        {
            String genericMsg = obj1 + " and " + obj2 + " are supposed to be mutually exclusive";
            if(msg != null)
            {
                outcome( msg + ": " + genericMsg);
            }
            else
            {
                outcome( genericMsg);
            }
        }
    }
    
    public static void notEmpty( Collection list)
    {
        notEmpty( list, null);
    }    

    public static void notEmpty( Object list, String msg)
    {
        notEmpty( (Collection)list, msg);
    }

    public static void notEmpty( Collection list, String msg)
    {
        if(list == null)
        {
            outcome( "list == null");
        }
        else if(list.isEmpty())
        {
            if(msg != null)
            {
                outcome( msg);
            }
            else
            {
                outcome( "list.isEmpty()");
            }
        }
    }

    public static void notEmpty( Map map)
    {
        notEmpty( map, null);
    }    

    public static void notEmpty( Map map, String msg)
    {
        if(map == null)
        {
            outcome( "map == null");
        }
        else if(map.isEmpty())
        {
            if(msg != null)
            {
                outcome( msg);
            }
            else
            {
                outcome( "map.isEmpty()");
            }
        }
    }
    
    public static void isEmpty( List list)
    {
        isEmpty( list, null);
    }    

    public static void isEmpty( List list, String msg)
    {
        if(list == null)
        {
            outcome( "list == null");
        }
        else if(!list.isEmpty())
        {
            if(msg != null)
            {
                outcome( msg);
            }
            else
            {
                outcome( "!list.isEmpty()");
            }
        }
    }
}
