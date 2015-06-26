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
package org.strandz.core.info.convert;

/**
 * Subclasses of this class can be 'plugged in'. They allow any DO field Class to
 * be mapped to any control. Thus for example say you have a DO with a field of
 * a type that was not supported by a JTextField. (JTextField only supports
 * Strings). Using a subclass of AbstractObjectControlConvert you would still be able to allow
 * the user to update the DO field. In this case possibly your special DO field
 * Class would have methods that get and set itself as a String, which are wrapped
 * by the methods of this abstract class.
 *
 * @author Chris Murphy
 */
abstract public class AbstractObjectControlConvert
{
    public Class typeRequiredByControlAccessors;
    public Class typeOfObject;

    abstract public Object pushOntoScreen(Object object);

    abstract public Object pullOffScreen(Object control);
}
