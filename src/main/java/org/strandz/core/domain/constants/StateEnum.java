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
package org.strandz.core.domain.constants;

import org.strandz.lgpl.util.Utils;

public class StateEnum
{
    private final String name;

    StateEnum(String name)
    {
        this.name = name;
    }

    StateEnum(StateEnum enumId)
    {
        this(enumId.getName());
    }

    public String toString()
    {
        return name;
    }

    public boolean isNavigating()
    {
        return false;
    }

    public boolean isNew()
    {
        return false;
    }

    public boolean isPrior()
    {
        return false;
    }

    public boolean isEditing()
    {
        return false;
    }

    public static final StateEnum UNKNOWN = new StateEnum("UNKNOWN");
    public static final StateEnum FROZEN = new StateEnum("FROZEN");
    public static final StateEnum ENTER_QUERY = new StateEnum("ENTER QUERY");
    // Not a state!
    // public static final StateEnum EXECUTE_QUERY = new StateEnum( "EXECUTE_QUERY");
    public static final NavigatingStateEnum VIEW = new NavigatingStateEnum("VIEW");
    public static final NavigatingStateEnum EDIT = new EditingStateEnum("EDIT");
    public static final NewStateEnum NEW = new NewStateEnum("NEW");
    public static final NewPriorStateEnum NEW_PRIOR = new NewPriorStateEnum(
        "NEW PRIOR");
    public static final NavigatingStateEnum NOMOVE_VIEW = new NavigatingStateEnum(
        "NOMOVE VIEW");
    public static final NavigatingStateEnum NOMOVE_EDIT = new EditingStateEnum(
        "NOMOVE EDIT");
    public static final NewStateEnum NOMOVE_NEW = new NewStateEnum("NOMOVE NEW");
    public static final NewPriorStateEnum NOMOVE_NEW_PRIOR = new NewPriorStateEnum(
        "NOMOVE NEW PRIOR");
    public static final StateEnum[] OPEN_VALUES = {UNKNOWN, FROZEN, ENTER_QUERY, 
            VIEW, EDIT, NEW, NEW_PRIOR, NOMOVE_VIEW, NOMOVE_EDIT, NOMOVE_NEW, NOMOVE_NEW_PRIOR};


    /**
     * Returns the name.
     *
     * @return String
     */
    public String getName()
    {
        return name;
    }
    
    public static StateEnum getFromName(String name)
    {
        return (StateEnum) Utils.getByStringFromArray(StateEnum.OPEN_VALUES, name);
    }
    
}
