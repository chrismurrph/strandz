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
package org.strandz.core.domain;

import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.util.Err;

/**
 * Before and after images are used here so that we can update the real
 * objects from what the user has entered in the controls on a selective
 * basis. We will not do updates if before and after image are the same.
 * Adapter is the object we use to get at the real data.
 * Currently only used on a temporary basis by TableObj and FieldObj, and is
 * not actually necessary. In the future it is assumed that a list of this
 * class will be given to a user trigger to decide whether the actual data
 * should be changed. (Except we don't want the Adapter, and surely the user
 * would want to know which object this data is going to - on a related matter,
 * where does the user get to change what might be commited? If on the user
 * trigger then should the update be applichousing? I guess the user could specify
 * all this in a returned object).
 * <p/>
 * Name happens to be filled with the name of the control class this field
 * represents. (So there could be many with the same name).
 */
public class ItemSnapshot
{
    public String name;
    private Object b4Image;
    private Object afterImage;
    public ItemAdapter itemAdapter;
    private static int times;

    Object getB4Image()
    {
        return b4Image;
    }

    public void setB4Image(Object b4Image)
    {
        this.b4Image = b4Image;
    }

    public String toString()
    {
        String result = null;
        /* name + "," + itemAdapter.getCell().getName() + ", " +*/
        if(afterImage != null)
        {
            result = afterImage.toString();
        }
        else
        {
            result = "afterImage null for <" + name + "> where b4Image was <" + b4Image + ">";
        }
        return result;
    }

    public Object getAfterImage()
    {
        return afterImage;
    }

    public void setAfterImage(Object afterImage)
    {
        times++;
        Err.pr(SdzNote.POLLUTING_DB_WITH_NULL_STRINGS, "Snapshot, setting " + name + " to <" + afterImage + "> times " + times);
        if(times == 13)
        {
            Err.stack(SdzNote.POLLUTING_DB_WITH_NULL_STRINGS);
        }
        //if(name.equals)
        if(this.afterImage != null)
        {
            Err.error(
                "Already have an afterImage <" + this.afterImage
                    + ">, so no point setting to <" + afterImage + ">");
        }
        if(afterImage != null)
        {
            times++;
            Err.pr( SdzNote.PROP_EDITOR_CONVERT, "AfterImage being set to <" + afterImage + "> of type " + afterImage.getClass().getName() + " times " + times);
            if(times == 0 /*44*/)
            {
                Err.stack();
            }
        }
        else
        {
            Err.pr( SdzNote.PROP_EDITOR_CONVERT, "AfterImage being set to <NULL>");
        }
        this.afterImage = afterImage;
    }
}
