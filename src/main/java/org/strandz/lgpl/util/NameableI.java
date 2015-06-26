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

/**
 * This interface allows the implementer to have a name, and to be
 * able to show itself
 *
 * @author Chris Murphy
 */
public interface NameableI
{
    /**
     * Where would a nameable interface be without a name?
     *
     * @return the name identify of this Nameable
     */
    String getName();

    /**
     * The character of toShow rather than toString is that toString is used
     * for debugging purposes, while toShow relates to the appearance of text
     * on the screen.
     *
     * @return String that the user might see
     */
    String toShow();
}
