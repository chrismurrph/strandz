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
 * Subclasses of this class can be 'plugged in'. They allow the peculiarities of any
 * DO field Class to be provided to Strandz.
 *
 * @author Chris Murphy
 */

abstract public class AbstractDOInterrogate
{
    // blankingPolicy (applies to fields and tables):
    // May be able to get rid of some of this stuff now that JTable has been
    // improved (I think only tables use these - or FieldObj always
    // uses NEED_CONSTRUCT_BLANK_OBJ_READ and have NEED_NOTHING!?)
    // Basically I think this stuff was over-engineering for case
    // where Booleans in tables were causing problems - maybe inspired
    // by a Protoview/JClass problem.
    /**
     * References to reading and writing refer to the DO. Thus if read a
     * DO field and get a null back, then if NEED_CONSTRUCT_BLANK_OBJ_READ,
     * the nullary constructor will be used.
     */
    public static int NEED_CONSTRUCT_BLANK_OBJ_READ = 1;
    public static int NEED_PUT_NULL_OBJ_WRITE = 2;
    public static int NEED_NOTHING = 3;

    abstract public int getBlankingPolicy();
    abstract public boolean isBlank( Object obj);
    abstract public Class getClassFor();
}

