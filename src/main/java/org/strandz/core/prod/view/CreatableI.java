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

import org.strandz.core.domain.LookupTiesManagerI;
import org.strandz.core.domain.NewInstanceEvent;
import org.strandz.core.prod.view.CellI;
import org.strandz.lgpl.extent.InsteadOfAddRemoveTrigger;
import org.strandz.lgpl.extent.Tie;
import org.strandz.lgpl.extent.NavExtent;
import org.strandz.lgpl.extent.IndependentExtent;
import org.strandz.lgpl.util.NamedI;
import org.strandz.lgpl.persist.EntityManagerProviderI;

import java.util.ArrayList;
import java.util.List;

/**
 * A Creatable as core.info.prod.view needs to know about it. Prevents a
 * dependency on core.info.prod.Creatable. (core.info.prod is dependent on
 * core.info.prod.view, not the other way round).
 */
public interface CreatableI extends NamedI
{
    ArrayList getLookups();

    void setRecordObj(SubRecordObj chiefObj);

    AdaptersListI getAdapters();

    Tie getTie();

    SubRecordObj getSubRecordObj();

    LookupTiesManagerI getTiesManager();

    boolean hasNewInstanceTrigger();

    Object firePrimaryNewInstanceTrigger();
    Object fireSecondaryNewInstanceTrigger();

    EntityManagerProviderI getEntityManagerProvider();
    
    InsteadOfAddRemoveTrigger getInsteadOfAddRemoveTrigger();

    CellI getCell();

    NavExtent getDataRecords();
    void setDefaultElement(Object defaultElement);
    void setInitialData(IndependentExtent object);
    void setLOV(List list);
    List getLOV();
    void displayLovObjects();

} // end interface
