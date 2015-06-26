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

public class Tokenizer 
{
    private final String wholeString;
    private int last;
    
    public static final String BETWEEN_FIELD_MARKER = "::";
    public static final int BETWEEN_FIELD_MARKER_LENGTH = BETWEEN_FIELD_MARKER.length();

    public Tokenizer( String wholeString) 
    {
        this.wholeString = wholeString;
    }

    public String nextToken() 
    {
        String result;
        int next = wholeString.indexOf( BETWEEN_FIELD_MARKER, last);
        if(next == -1) 
        {
            result = wholeString.substring( last);
            last = wholeString.length();
        } 
        else 
        {
            result = wholeString.substring( last, next);
            last = next + BETWEEN_FIELD_MARKER_LENGTH;
        }
        return result;
    }
}
