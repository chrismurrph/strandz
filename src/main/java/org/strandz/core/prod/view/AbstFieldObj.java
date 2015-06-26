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
package org.strandz.core.prod.view;

import org.strandz.core.domain.constants.OperationEnum;
import org.strandz.lgpl.util.CautiousArrayList;

/**
 *
 * Composite pattern
 *
 */

/**
 * An object from a core.info point of view (giving reasons for name) represents
 * a real object from the world of OO, and encompasses the objects attachment
 * to a set of UI fields. It is made up of a set of Adapters (where a UI field
 * meets a class's field). It is thru this class that we both find out about
 * and set the UI that the user sees. Setting and getting the UI refer,
 * respectively, to displaying an object we already have and mapping the
 * user's entries onto an object we already have.
 * <p/>
 * Subclasses will deal with the lists of data and UI elements required to
 * implement these methods.
 * <p/>
 * fillAdaptersFlatObjects() is only used for TableObjs. Is required because a
 * table has to display many rows. Better - a table has no concept of rows, just
 * cells. When we move off of a row we have to set the data for that whole row.
 */
interface AbstFieldObj extends AbstInterf
{
    void blankoutDisplay(OperationEnum currentOperation, String reason, boolean removeComboItems);
    
    void displayLovObjects();

    boolean displayIsBlank();

    void setDisplay(OperationEnum currentOperation, int idx, Object element, boolean changedValuesOnly, boolean set, String reason);

    void getDisplay(Object element, boolean changedValuesOnly);

    boolean haveFieldsChanged();

    void setDisplayEnabled(boolean enabled);

    CautiousArrayList getAdapters(CautiousArrayList v, int visualType, boolean tableOnly);

    void fillAdaptersFlatObjectsTable(Object leftElement);
    
    Object getDerivedAt( int idx, Object element);    
}
