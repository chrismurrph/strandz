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
package org.strandz.core.domain.event;

import org.strandz.core.domain.constants.CapabilityEnum;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.util.Err;

public class DynamicAllowedEvent extends AllowedEvent
{
    private boolean dynamicAllowedSet = false;
    private boolean dynamicAllowed;

    public DynamicAllowedEvent(CapabilityEnum id)
    {
        super(id);
    }

    public DynamicAllowedEvent(CapabilityEnum id, boolean allowed)
    {
        super(id, allowed);
    }

    public void setDynamicAllowed(boolean dynamicAllowed)
    {
        this.dynamicAllowed = dynamicAllowed;
        dynamicAllowedSet = true;
    }

    public boolean getDynamicAllowed()
    {
        boolean result = false;
        if(!dynamicAllowedSet)
        {
            Err.error(SdzNote.DYNAMIC_ALLOWED, "Allowed has not been set for " + this);
        }
        if(dynamicAllowed && isAllowed())
        {
            result = dynamicAllowed;
        }
        else if(!dynamicAllowed)
        {
            result = dynamicAllowed;
        }
        return result;
    }

    public String toString()
    {
        String allowed = " unset";
        if(dynamicAllowedSet)
        {
            allowed = " " + getDynamicAllowed();
        }

        String result = super.toString();
        result += allowed;
        return result;
    }
}
