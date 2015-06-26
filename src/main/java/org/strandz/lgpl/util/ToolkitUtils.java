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
package org.strandz.lgpl.util;

import java.awt.Cursor;

public class ToolkitUtils
{
    private static Cursor moveCursor;
    private static Cursor northCursor;
    private static Cursor southCursor;
    private static Cursor eastCursor;
    private static Cursor westCursor;
    private static Cursor northEastCursor;
    private static Cursor southEastCursor;
    private static Cursor southWestCursor;
    private static Cursor northWestCursor;

    public static Cursor getMoveCursor()
    {
        if(moveCursor == null)
        {
            moveCursor = new Cursor(Cursor.MOVE_CURSOR);
        }
        return moveCursor;
    }

    public static Cursor getNorthCursor()
    {
        if(northCursor == null)
        {
            northCursor = new Cursor(Cursor.N_RESIZE_CURSOR);
        }
        return northCursor;
    }

    public static Cursor getSouthCursor()
    {
        if(southCursor == null)
        {
            southCursor = new Cursor(Cursor.S_RESIZE_CURSOR);
        }
        return southCursor;
    }

    public static Cursor getEastCursor()
    {
        if(eastCursor == null)
        {
            eastCursor = new Cursor(Cursor.E_RESIZE_CURSOR);
        }
        return eastCursor;
    }

    public static Cursor getWestCursor()
    {
        if(westCursor == null)
        {
            westCursor = new Cursor(Cursor.W_RESIZE_CURSOR);
        }
        return westCursor;
    }

    public static Cursor getNorthEastCursor()
    {
        if(northEastCursor == null)
        {
            northEastCursor = new Cursor(Cursor.NE_RESIZE_CURSOR);
        }
        return northEastCursor;
    }

    public static Cursor getSouthEastCursor()
    {
        if(southEastCursor == null)
        {
            southEastCursor = new Cursor(Cursor.SE_RESIZE_CURSOR);
        }
        return southEastCursor;
    }

    public static Cursor getSouthWestCursor()
    {
        if(southWestCursor == null)
        {
            southWestCursor = new Cursor(Cursor.SW_RESIZE_CURSOR);
        }
        return southWestCursor;
    }

    public static Cursor getNorthWestCursor()
    {
        if(northWestCursor == null)
        {
            northWestCursor = new Cursor(Cursor.NW_RESIZE_CURSOR);
        }
        return northWestCursor;
    }
}
