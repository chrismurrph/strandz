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
package org.strandz.core.applichousing;

import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.MessageDlg;

public class UnadornedSdzBag extends AbstractSdzBag
{
    public static final String DEFAULT_NAME = "New_UnadornedSdzBag";
    private static int times;
    
    public UnadornedSdzBag()
    {
        setName( "UnadornedSdzBag"); //for when setDefaults() not called
        MessageDlg.setDlgParent(this);
    }

    /**
     * If we are moving a VisibleStrand then we may want to also change its type. An example
     * of when might need to do this is when already have a SdzBag that has been done with
     * the Designer yet we want to use it again in a JDialog 
     * @param sdzBag
     */
    public UnadornedSdzBag( SdzBag sdzBag)
    {
        Err.error( "Not yet implemented");
        setName( "UnadornedSdzBag"); //for when setDefaults() not called
        MessageDlg.setDlgParent(this);
    }
    
    public void setDefaults()
    {
        setName(DEFAULT_NAME);
        Err.pr( SdzNote.GENERIC, "setDefaults() being called for ID:" + getStrand().id);
    }

    public String getDefaultName()
    {
        return DEFAULT_NAME;
    }    
}
