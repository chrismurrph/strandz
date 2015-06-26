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
package org.strandz.core.interf;

import org.strandz.core.domain.constants.OperationEnum;
import org.strandz.core.domain.event.ControlActionListener;
import org.strandz.core.domain.event.DynamicAllowedEvent;

import java.util.EventListener;
import java.util.List;

/**
 * For a PhysicalNodeController to be part of the Strandz system it must
 * implement this interface. Thus for example a voice activated/voice announcing
 * could be implemented, and that would then be how the NodeController interacted
 * with the outside world.
 * <p/>
 * If it was say a voice activated ActualNodeControllerI, then some of the methods
 * might need a little re-interpretation, for instance setVisible( true) would actually
 * mean 'start listening'.
 *
 * @author Chris Murphy
 */
public interface ActualNodeControllerI extends EventListener
{
    public void removeAllControlActionListeners();

    public void addControlActionListener(ControlActionListener l);

    public void allowedPerformed(DynamicAllowedEvent e);

    public void setVisible(boolean b);

    public void strandChanged(StrandI strand);

    public void setAbilities(List abilities);

    public void setHowDisplayNotAllowed(int how);

    public void deleteTool(OperationEnum capability);

    public void disableTool(OperationEnum capability);

    public int getId();
    // public String getName();
}
