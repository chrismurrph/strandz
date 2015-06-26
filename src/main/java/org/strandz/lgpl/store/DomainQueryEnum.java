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

public class DomainQueryEnum
{
    private String name = null;
    private String description = null;
    private boolean cacheable = false;

    public DomainQueryEnum(String name, String description, boolean cacheable)
    {
        this.name = name;
        this.description = description;
        this.cacheable = cacheable;
    }
    
    public DomainQueryEnum(String name, String description)
    {
        this.name = name;
        this.description = description;
    }

    public String toString()
    {
        return description;
    }

    public String getName()
    {
        return name;
    }

    /*
    public void setName( String name)
    {
      this.name = name;
    }
    */

    public String getDescription()
    {
        return description;
    }

    public boolean isCacheable()
    {
        return cacheable;
    }

    /*
     * S/not be needed
    public boolean equals( Object o)
    {
      pUtils.chkType( o, this.getClass());

      boolean result = false;
      if(o == this)
      {
        result = true;
      }
      else if(!(o instanceof WombatConnectionEnum))
      {// nufin
      }
      else
      {
        WombatConnectionEnum test = (WombatConnectionEnum)o;
        if((name == null ? test.name == null : name.equals( test.name)))
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
    */
}
