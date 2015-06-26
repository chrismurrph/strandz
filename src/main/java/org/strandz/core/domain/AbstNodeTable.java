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

import org.strandz.core.domain.event.ControlActionListener;
import org.strandz.lgpl.util.IdentifierI;

import java.util.EventListener;

// import strandz.prod.move.MoveBlock;

/**
 * Even thou in domain, this 'view' of a Node is used by one client. That is
 * the plug-in implementation of the model that is associated with a 'table'
 * style control. For example JTextAreaModelImpl.
 *
 * @author Chris Murphy
 */
abstract public class AbstNodeTable implements IdentifierI
{
    // rec//NavExtent getInitialData();
    abstract public NodeTableModelI getTableModel();

    abstract public ControlActionListener getControlActionListener();

    abstract public boolean GOTO();

    // abstract public void goToEXCEPT(); //Now near same

    /**
     * Bit more weakly typed than would want. Could try to fix
     * later but not too big a deal.
     */
    abstract public void addNodeChangeListener(
        EventListener
            /* NodeChangeListener*/ l);
    abstract public void removeNodeChangeListener(EventListener l);

    abstract public String getName();
    
    abstract public Object getTableControl();

    abstract public void changeFromCurrentNode();

    abstract public Object getMoveBlock();

    abstract public boolean isColumnEnabled(int column);
    
    abstract public void REFRESH();
    
    abstract public CalculationPlace getCalculationPlace();
    
    abstract public Object getItemValue();
    
    abstract public boolean fieldsChanged();

    abstract public NodeI getCurrentNode();

    abstract public int getId();
}
