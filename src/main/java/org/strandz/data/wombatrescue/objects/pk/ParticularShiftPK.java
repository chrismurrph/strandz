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
package org.strandz.data.wombatrescue.objects.pk;

import org.strandz.data.wombatrescue.objects.WhichShift;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.TimeUtils;
import org.strandz.lgpl.util.Utils;

import java.util.Date;

public class ParticularShiftPK
{
    private Date shiftDate;
    private WhichShift shift;
    private int volunteerOrdinal;

    public boolean equals(Object o)
    {
        Utils.chkType(o, this.getClass());

        boolean result = false;
        if(o == this)
        {
            result = true;
        }
        else if(!(o instanceof ParticularShiftPK))
        {// nufin
        }
        else
        {
            ParticularShiftPK test = (ParticularShiftPK) o;
            if((shiftDate == null
                ? test.shiftDate == null
                : shiftDate.equals(test.shiftDate))
                && (shift == null ? test.shift == null : shift.equals(test.shift))
                && (volunteerOrdinal == test.volunteerOrdinal))
            {
                result = true;
            }
        }
        return result;
    }

    public int hashCode()
    {
        int result = 17;
        result = 37 * result + (shiftDate == null ? 0 : shiftDate.hashCode());
        result = 37 * result + (shift == null ? 0 : shift.hashCode());
        result = 37 * result + volunteerOrdinal;
        return result;
    }

    public ParticularShiftPK(
        Date shiftDate,
        WhichShift shift,
        int volunteerOrdinal
    )
    {
        if(shiftDate == null)
        {
            Err.error("shiftDate == null");
        }
        if(shift == null)
        {
            Err.error("shift == null");
        }
        this.shiftDate = shiftDate;
        this.shift = shift;
        this.volunteerOrdinal = volunteerOrdinal;
    }

    public ParticularShiftPK()
    {
        // to try to fix a problem seems to happen for nulls:
        this.shiftDate = TimeUtils.getDateFromString("01/01/1987");
        this.shift = WhichShift.DINNER;
        this.volunteerOrdinal = 1;
    }

    public String toString()
    {
        return shiftDate + ", " + shift + ", " + volunteerOrdinal;
    }
}
