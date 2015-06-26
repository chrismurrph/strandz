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
package org.strandz.task.wombatrescue;

import org.strandz.lgpl.util.Clazz;
import org.strandz.core.interf.Cell;
import org.strandz.core.interf.FieldAttribute;
import org.strandz.core.interf.Node;
import org.strandz.core.interf.NodeController;
import org.strandz.core.interf.SdzBagI;
import org.strandz.core.interf.Strand;
import org.strandz.data.wombatrescue.objects.Flexibility;
import org.strandz.data.wombatrescue.objects.Worker;
import org.strandz.data.wombatrescue.objects.FlexibilityI;
import org.strandz.data.wombatrescue.objects.WombatLookups;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.util.DisplayUtils;
import org.strandz.lgpl.store.LookupsProviderI;
import org.strandz.lgpl.store.LookupsI;
import org.strandz.view.wombatrescue.WorkerPanel;

import java.util.List;

public class WorkerAppHelper //implements LookupsProviderI
{
    Strand strand;
    Node workerNode;
    Cell workerCell;
    Cell flexibilityCell;
    WorkerPanel panel;
    FieldAttribute christianNameAttribute;
    FieldAttribute surnameAttribute;
    FieldAttribute groupNameAttribute;
    SdzBagI sbI;
    //WombatLookups wombatLookups = new WombatLookups();

    private static final boolean VISUAL = true;

    public WorkerAppHelper()
    {
        strand = new Strand();
        workerNode = new Node();
        workerCell = new Cell();
        flexibilityCell = new Cell();
        if(VISUAL)
        {
            panel = new WorkerPanel();
        }
        //wombatLookups.initValues();
    }

    public void sdzSetup()
    {
        if(panel != null) panel.init();
        workerCell.setClazz(new Clazz(Worker.class));
        workerCell.setName("Worker Cell");
        flexibilityCell.setClazz(new Clazz(FlexibilityI.class));
        flexibilityCell.setName("Flexibility Cell (looked up by Worker)");
        NodeController nodeController = new NodeController();
        nodeController.setStrand(strand);
        detailSetup();
        workerNode.setStrand(strand);
        boolean ok = strand.validateBean();
        if(!ok)
        {
            List msg = strand.retrieveValidateBeanMsg();
            //List msg = flexibilityCell.retrieveValidateBeanMsg();
            Print.prList(msg, "How Strand not correctly setup");
            Err.error("Strand not setup correctly");
        }
    }

    private void detailSetup()
    {
        FieldAttribute attr = new FieldAttribute();
        attr.setDOField("christianName");
        if(panel != null) attr.setItem(panel.tfChristianName);
        attr.setName("Christian Name");
        christianNameAttribute = attr;
        workerCell.addAttribute(attr);

        attr = new FieldAttribute();
        attr.setDOField("surname");
        if(panel != null) attr.setItem(panel.tfSurname);
        attr.setName("Surname");
        surnameAttribute = attr;
        workerCell.addAttribute(attr);

        attr = new FieldAttribute();
        attr.setDOField("groupName");
        if(panel != null) attr.setItem(panel.tfGroupName);
        attr.setName("Group Name");
        groupNameAttribute = attr;
        workerCell.addAttribute(attr);
        /** Non essential code whipped from here, see below **/
        attr = new FieldAttribute();
        attr.setDOField("name");
        //either one of these two
        if(panel != null) attr.setItem(panel.frbFlexibilityRadioButtons);
        //if(panel != null) attr.setItem( panel.cbSeniority);
        attr.setName("Flexibility (looked up by Worker)");
        flexibilityCell.addAttribute(attr);
        //
        workerNode.setCell(workerCell);
        flexibilityCell.setRefField("flexibility");
        workerCell.setCell(workerCell.getCells().length, flexibilityCell);
    }

    /* Not essential - sometimes it is convenient to use this extra attribute
    ReferenceLookupAttribute ref = new ReferenceLookupAttribute();
    ref.setDOField( "flexibility" );
    ref.setName( "Flexibility Reference" );
    workerCell.addAttribute( ref );
    */

    void displayWorkerPanel()
    {
        DisplayUtils.displayInExitable(panel);
    }

    public Cell getFlexibilityCell()
    {
        Cell result = flexibilityCell;
        return result;
    }

    public Cell getWorkerCell()
    {
        Cell result = workerCell;
        return result;
    }

    public Strand getStrand()
    {
        Strand result = strand;
        return result;
    }

    public Node getWorkerNode()
    {
        return workerNode;
    }

    public WorkerPanel getPanel()
    {
        return panel;
    }

//    public LookupsI obtainLookups()
//    {
//        Err.error("Not implemented");
//        return null;
//    }
}
