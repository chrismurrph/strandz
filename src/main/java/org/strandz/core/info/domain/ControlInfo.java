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
package org.strandz.core.info.domain;

import org.strandz.core.info.convert.AbstractObjectControlConvert;
import org.strandz.core.info.convert.AbstractDOInterrogate;

// avoid recursive dependencies: import strandz.ControllerInterface;

/**
 * Used so that theoretically we can use any third-party
 * components, rather than just swing that we have written
 * an implementation of this for.
 * <p/>
 * TODO - We now monitor which fields the user is entering.
 * This way we can go back to those fields if changing
 * focus onto another node gives the exception. Should really
 * be implemented with a signature/s for the addBlahListener/s,
 * and a signature for a class which implements this listener/s,
 * which also implements the interface that core.info will rely
 * upon - a method such as fieldEntered().
 */
public abstract class ControlInfo
{
    abstract public ItemDescriptor[] getItemDescriptors();

    abstract public TableControlDescriptor[] getTableControlDescriptors();

    // abstract public Method getStringToTypeConvertMethod();

    abstract public Class[] getLookThruControls();

    abstract public Class[] getLookThruButStructuralControls();

    abstract public String[] getTerminatingControls();

    abstract public String[] getNotTerminatingControls();
    
    abstract public Class[] getVisibleTerminatingControls();

    abstract public AbstractObjectControlConvert[] getObjectControlConverts();

    abstract public AbstractDOInterrogate[] getDOInterrogators();

    abstract public String[] getSystemWidgetsPackages();

    abstract public void doStartupCode();

    abstract public FocusMonitorI getFocusMonitor();
}
