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
package org.strandz.view.util;

import org.strandz.lgpl.util.Err;

public class DTUtils
{
    /**
     * This method exists to highlight a frequently occuring problem where the SdzBag XML file
     * you are trying to access does not fully 'know' about a property (item) on a panel. To
     * resolve this problem the panel's init() method needs to have used the setter. If you have
     * done this then you should see the property being set in the XML file. If the panel is ok 
     * but the XML file is not then you need to re-put the panel into the XML file. You do this
     * using the Designer. Just reloading the panel into the Designer is not enough. You also
     * need to make a change to the SdzBag itself (ie. reload the panel into the SdzBag). Re-mapping
     * a field will allow this to happen.
     * TODO Change the Designer so that reloading the panel is sufficient for the Save action to
     * generate a new XML file.
     * TODO Unfortunately need to bring the Designer up before do this stuff (so need to bring it
     * down if it is already up) 
     * @param item
     */
    public static void chkNotNull( Object item)
    {
        if(item == null)
        {
            Err.error( "Check out the xml file - then construct the panel properly and " +
                    "re-do the XML file - what is added to a panel also needs to be a property of a panel - " +
                    "interestingly you will need to re-map in the Designer as well (see explanation in" +
                    " source code for DTUtils)");
        }
    }
}
