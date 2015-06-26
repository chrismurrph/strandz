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
package org.strandz.data.needs.objects;

import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.widgets.PanelList;

import java.util.Date;

public class Sending
{
    private Date dateSent; // this is pk for now, but later will have an ordinal
    private PanelList toPanelList; // of Sendings (added as sent)
    private SendingMedium medium;
    private Contact stoppedAt; // if successfully completed will be Contact.NULL

    public Sending()
    {
        super();
        /*
        constructedTimes++;
        id = constructedTimes;
        Err.pr( "Constructed " + id);
        if(id == 10)
        {
        Err.stack();
        }
        */
    }

    public boolean equals(Object o)
    {
        Utils.chkType(o, this.getClass());

        boolean result = false;
        if(o == this)
        {
            result = true;
        }
        else if(!(o instanceof Sending))
        {// nufin
        }
        else
        {
            Sending test = (Sending) o;
            if((dateSent == null
                ? test.dateSent == null
                : dateSent.equals(test.dateSent)))
            {
                result = true;
            }
        }
        return result;
    }

    public int hashCode()
    {
        int result = 17;
        result = 37 * result + (dateSent == null ? 0 : dateSent.hashCode());
        return result;
    }

    public Date getDateSent()
    {
        return dateSent;
    }

    public SendingMedium getMedium()
    {
        return medium;
    }

    public Contact getStoppedAt()
    {
        return stoppedAt;
    }

    public PanelList getToList()
    {
        return toPanelList;
    }

    public void setDateSent(Date dateSent)
    {
        this.dateSent = dateSent;
    }

    public void setMedium(SendingMedium medium)
    {
        this.medium = medium;
    }

    public void setStoppedAt(Contact stoppedAt)
    {
        this.stoppedAt = stoppedAt;
    }

    public void setToList(PanelList toPanelList)
    {
        this.toPanelList = toPanelList;
    }
}
