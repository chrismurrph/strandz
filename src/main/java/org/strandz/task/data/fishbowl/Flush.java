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
package org.strandz.task.data.fishbowl;

import org.strandz.data.startsimple.ContextData;
import org.strandz.data.startsimple.CustomizerData;
import org.strandz.lgpl.store.DataStore;
import org.strandz.lgpl.util.Err;
import org.strandz.store.fishbowl.FishbowlDataStoreFactory;

// import database.XMLData;

public class Flush
{
    public static void main(String s[])
    {
        if(s.length != 0)
        {
            processParams(s);
        }
        else
        {
            String str[] = {"FishBowlData"};
            processParams(str);
            // Err.pr( "Specify an AbstData subclass as param to purge it");
        }
    }

    /**
     * Like removing all the records of a table but
     * leaving the table there!
     */
    private static void processParams(String s[])
    {
        for(int i = 0; i <= s.length - 1; i++)
        {
            DataStore fbd = null;
            if(s[i].equals("ContextData"))
            {
                fbd = new ContextData();
                fbd.flush();
            }
            else if(s[i].equals("FishBowlData"))
            {
                //fbd = FishbowlApplicationData.getInstance().getData();
                fbd = new FishbowlDataStoreFactory( true).getDataStore();
                fbd.flush();
            }
            else if(s[i].equals("CustomizerData"))
            {
                fbd = new CustomizerData();
                fbd.flush();
            }
            else
            {
                Err.error("Unrecognised param " + s[i]);
            }
            // Err.pr( "Have flushed from " + fbd.getFileName());
        }
    }
}