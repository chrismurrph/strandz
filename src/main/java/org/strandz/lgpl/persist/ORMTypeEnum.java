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
package org.strandz.lgpl.persist;

public class ORMTypeEnum
{
    private String name = null;

    ORMTypeEnum()
    {
    }

    ORMTypeEnum(String name)
    {
        this();
        this.name = name;
    }

    public ORMTypeEnum(ORMTypeEnum enumId)
    {
        this(enumId.getName());
    }

    public String toString()
    {
        return name;
    }

    public boolean isCayenne()
    {
        boolean result = false;
        if(getName() != null)
        {
            result = getName().equals( CAYENNE_SERVER.getName()) ||
                getName().equals( CAYENNE_CLIENT.getName());
        }
        return result;
    }

    public boolean isORM()
    {
        boolean result = false;
        if(getName() != null)
        {
            result = getName().equals( CAYENNE_SERVER.getName()) ||
                getName().equals( CAYENNE_CLIENT.getName()) ||
                getName().equals( JDO.getName())
                ;
        }
        return result;
    }

    public static final ORMTypeEnum NULL = new ORMTypeEnum();
    public static final ORMTypeEnum JDO = new ORMTypeEnum("JDO");
    public static final ORMTypeEnum XML = new ORMTypeEnum("XML");
    public static final ORMTypeEnum MEMORY = new ORMTypeEnum("MEMORY");
    public static final ORMTypeEnum CAYENNE_SERVER = new ORMTypeEnum("CAYENNE_SERVER");
    public static final ORMTypeEnum CAYENNE_CLIENT = new ORMTypeEnum("CAYENNE_CLIENT");

    /**
     * Returns the name.
     *
     * @return String
     */
    public String getName()
    {
        return name;
    }
}
