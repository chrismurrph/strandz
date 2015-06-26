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
package org.strandz.lgpl.store;

/**
 * Actually can't always have different vendors jar files around. For
 * now we are just going to use Exception
 * <p/>
 * In the future we may unify all the different vendors exception. For the
 * present the tack we will take is to add another
 * notifyException( VendorException) method here for each vendor. This way
 * it is highly visible, not too complex, and everything is kept together.
 * Sure, without the use of an Adapter legacy code will break - but that
 * is what we mean by being 'highly visible'.
 */
public interface ConcurrencyExceptionListener
{
    void notifyException(Exception ex);
}
