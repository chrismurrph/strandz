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
package org.strandz.lgpl.widgets;

import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.BeansUtils;
import org.strandz.lgpl.util.Utils;

import javax.swing.Icon;
import java.io.Serializable;

/**
 * More than an enumerated constant. The Designer uses it to actually
 * retrieve a hard coded icon from a jar file.
 */
public class IconEnum implements Serializable
{
    private final String name;
    private final IconEnum notAttached;
    private transient Icon icon = null;

    private IconEnum(String name, IconEnum notAttached)
    {
        this.name = name;
        this.notAttached = notAttached;
    }

    private IconEnum(String name)
    {
        this.name = name;
        this.notAttached = null;
    }

    public String toString()
    {
        return name;
    }

    public static final IconEnum BEAN_16 = new IconEnum("images/Bean16.gif");
    public static final IconEnum BLUE_CIRCLE = new IconEnum(
        "images/blueCircle.gif");
    public static final IconEnum YELLOW_CIRCLE = new IconEnum(
        "images/yellowCircle.gif");
    public static final IconEnum GREEN_CIRCLE_BAD = new IconEnum(
        "images/badGreenCircle.gif");
    public static final IconEnum GREEN_CIRCLE = new IconEnum(
        "images/greenCircle.gif", GREEN_CIRCLE_BAD);
    public static final IconEnum DATE_PICKER_BAD = new IconEnum(
        "images/badDatePicker.gif");
    public static final IconEnum RED_CIRCLE = new IconEnum("images/redCircle.gif");
    public static final IconEnum DATE_PICKER = new IconEnum(
        "images/DatePicker.gif", DATE_PICKER_BAD);
    public static final transient IconEnum BLACK_CIRCLE = new IconEnum(
        "images/blackCircle.gif");
    public static final IconEnum LOOKUP_LINK = new IconEnum("images/Reply.gif");
    public static final IconEnum MASTER_LINK = new IconEnum("images/Undo.gif");
    public static transient final IconEnum[] CLOSED_VALUES = {
        BEAN_16, BLUE_CIRCLE, YELLOW_CIRCLE, GREEN_CIRCLE, GREEN_CIRCLE_BAD,
        DATE_PICKER_BAD, RED_CIRCLE, DATE_PICKER, BLACK_CIRCLE, LOOKUP_LINK,
        MASTER_LINK,};
    /*
    public static IconEnum getFromIcon( Icon icon)
    {
    IconEnum result = null;

    return result;
    }
    */

    public String getName()
    {
        return name;
    }

    public Icon getIcon()
    {
        if(BeansUtils.isDesignTime())
        {
            if(icon == null)
            {
                Err.pr(SdzNote.GENERIC, "IS dt so will read in image " + name);
                icon = PortableImageIcon.createImageIcon(name, Utils.fromImageToReadable(name));
            }
        }
        else
        {
            Err.pr(SdzNote.GENERIC, "IS _NOT_ dt so have NOT read in image " + name);
        }
        return icon;
    }

    public IconEnum getControlNotAttachedIconEnum()
    {
        return notAttached;
    }
}
