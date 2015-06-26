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
package org.strandz.core.prod;

import org.strandz.lgpl.util.Clazz;
import org.strandz.core.domain.LookupTiesManagerI;
import org.strandz.core.domain.NewInstanceTrigger;
import org.strandz.core.domain.NewInstanceEvent;
import org.strandz.core.prod.view.AdaptersListI;
import org.strandz.core.prod.view.CreatableI;
import org.strandz.core.prod.view.SubRecordObj;
import org.strandz.core.prod.view.CellI;
import org.strandz.lgpl.extent.IndependentExtent;
import org.strandz.lgpl.extent.InsteadOfAddRemoveTrigger;
import org.strandz.lgpl.extent.NavExtent;
import org.strandz.lgpl.extent.Tie;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.persist.EntityManagerProviderI;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Print;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Creatable implements CreatableI
{
    private AdaptersListI adapters;
    private ArrayList lookedupCells = new ArrayList(); // refs to other Cells
    private SubRecordObj chiefObj;
    private Tie tie;
    private LookupTiesManagerI tiesManager;
    private NewInstanceTrigger newInstanceTrigger;
    private InsteadOfAddRemoveTrigger insteadOfTrigger;
    private EntityManagerProviderI entityManagerProvider;
    private CellI cell;
    private NewInstanceEvent primaryEvent;
    private NewInstanceEvent secondaryEvent;

    public Creatable(CellI cell,
                     List lookupCreatables,
                     List lookupRefFields,
                     AdaptersListI adapters,
                     LookupTiesManagerI tiesManager,
                     NewInstanceTrigger nit,
                     InsteadOfAddRemoveTrigger insteadOfTrigger,
                     EntityManagerProviderI entityManagerProvider)
    {
        this.cell = cell;
        List origLOVs = cell.getLOV();
        //WRONG Always seems to be null so let's assert and perhaps get rid of the code: 
        if(origLOVs != null)
        {
            Err.pr(SdzNote.LOVS_CHANGE_DATA_SET, "###CONSTRUCTING NEW Creatable for " + cell + " with NewInstanceTrigger " + nit);
            if(SdzNote.LOVS_CHANGE_DATA_SET.isVisible())
            {
                Print.prList( origLOVs, "origLOVs", false);
            }
        }
        if(origLOVs != null)
        {
            adapters.setLovObjects( origLOVs);
        }
        this.adapters = adapters;
        if(lookupCreatables.size() != lookupRefFields.size())
        {
            Err.error("Sizes do not match");
        }

        Iterator creatables_en = lookupCreatables.iterator();
        Clazz clazz = cell.getClazz();
        if(clazz == null)
        {
            Err.error(
                "Cell " + cell
                    + " has a null clazz, possibly due to a class name change");
        }
        Clazz clazz1;
        if(cell.getClazzToConstruct() == null)
        {
            clazz1 = cell.getClazz();
            Err.pr( SdzNote.CAYENNE_CONSTRUCTOR, "Using class cell made with: " + clazz1);
        }
        else
        {
            clazz1 = cell.getClazzToConstruct();
            Err.pr( SdzNote.CAYENNE_CONSTRUCTOR, "Using special class to construct with: " + clazz1);
        }
        Err.pr( SdzNote.CAYENNE_CONSTRUCTOR, "clazz1 in cell " + cell.getName() + 
            " will be using is " + clazz1);
        setFromCell(clazz, clazz1, cell.getSecondaryClazzToConstruct(), cell); // addCell needs

        List ties = new ArrayList();
        this.tiesManager = tiesManager; // b4 addCell
        for(Iterator refFields_en = lookupRefFields.iterator(); creatables_en.hasNext();)
        {
            Creatable creatable = (Creatable) creatables_en.next();
            String refField = (String) refFields_en.next();
            ties.add(addCell(creatable, refField));
        }
        tiesManager.addTies(ties);
        // S/be done b4 call this constructor
        // addAttributes( cell.getAttributes()); //gives AdaptersList Adapters
        adapters.setAdapters(clazz); // now can set the Adapters
        this.newInstanceTrigger = nit;
        this.insteadOfTrigger = insteadOfTrigger;
        // Cell can later relay calls to setInitialData:
        cell.setCreatable(this);
        this.entityManagerProvider = entityManagerProvider;
        primaryEvent = new NewInstanceEvent( cell, false);
        secondaryEvent = new NewInstanceEvent( cell, true);
    }

    public String getName()
    {
        return chiefObj.getName();
    }

    private void setFromCell( Clazz c, Clazz cToConstruct, Clazz cToSecondarilyConstruct, CellI cell)
    {
        PropertyDescriptor pds[];
        Err.pr( SdzNote.INVOKE_WRONG_FIELD, "BeanInfo for " + c + ", cell " + cell);
        BeanInfo bi = null;
        try
        {
            bi = Introspector.getBeanInfo(c.getClassObject());
        }
        catch(IntrospectionException ex)
        {
            Err.error(ex.toString());
        }
        pds = bi.getPropertyDescriptors();
        PropertyDescriptor pdsToConstruct[] = null;
        if(cToConstruct != null)
        {
            BeanInfo biToConstruct = null;
            try
            {
                biToConstruct = Introspector.getBeanInfo(cToConstruct.getClassObject());
            }
            catch(IntrospectionException ex)
            {
                Err.error(ex.toString());
            }
            pdsToConstruct = biToConstruct.getPropertyDescriptors();
        }
        PropertyDescriptor pdsToSecondarilyConstruct[] = null;
        if(cToSecondarilyConstruct != null)
        {
            BeanInfo biToSecondarilyConstruct = null;
            try
            {
                biToSecondarilyConstruct = Introspector.getBeanInfo(cToSecondarilyConstruct.getClassObject());
            }
            catch(IntrospectionException ex)
            {
                Err.error(ex.toString());
            }
            pdsToSecondarilyConstruct = biToSecondarilyConstruct.getPropertyDescriptors();
        }
        adapters.setElement(c, cToConstruct, pds, pdsToConstruct, pdsToSecondarilyConstruct);
    }

    public CellI getCell()
    {
        return cell;
    }

    void setTie(Tie tie)
    {
        this.tie = tie;
    }

    public Tie getTie()
    {
        return tie;
    }

    public String toString()
    {
        Class c = null;
        if(!adapters.allIsEmpty())
        {
            c = adapters.getInstantiable().getClassObject();
            if(c == null)
            {
                Err.error(
                    "If a node has been validated then it should have an instantiable");
            }
            return c.getName();
        }
        else
        {
            return super.toString();
        }
    }

    /**
     * First used by Tie in constructor. RUBBISH May return null.
     * RECENT - think it's only being used by itself, so made default
     * accessibility
     */
    Clazz getClazz()
    {
        return adapters.getClassType();
    }

    /*
    private void addAttributes( List attributes_en)
    {
    int i=0;
    for(Iterator it = attributes_en.iterator(); it.hasNext(); i++)
    {
    Attribute itemAdapter = (Attribute)it.next();
    adapters.addAttribute( itemAdapter, i);
    }
    *
    for(int i=0;i<=attributes_en.length-1;i++)
    {
    Attribute attribute = (Attribute)attributes_en[i];
    attributes.addAttribute( attribute);
    }
    *
    }
    */

    public void setLOV(List list)
    {
        if(SdzNote.LOVS_CHANGE_DATA_SET.isVisible())
        {
            //if(list.get( 0) instanceof Competition)
            {
                //Print.prList( list, "LOVs being set", false);
                //Err.stack();
            }
        }
        adapters.setLovObjects(list);
    }
    
    public List getLOV()
    {
        return adapters.getLovObjects();
    }
    
    public void displayLovObjects()
    {
        adapters.displayLovObjects();
    }

    /**
     * refField belongs to either this or the passed in instantiable. At
     * moment determine which side, and that only one. TODO check that
     * it is an IndependentExtent on the side that needs to be an
     * IndependentExtent. Do this with Tie too. ie./two types of Tie
     * ALSO TODO - make sure that same Tie is not added twice, in
     * accordance with the matching method (3 times used 6/3/98)
     */
    private Tie addCell(Creatable creatable, String refField)
    {
        lookedupCells.add(creatable);
        //Err.pr( "this is <" + this.getClass().getName() + ">");
        Tie tie = Tie.createTie(creatable, this, creatable.getClazz().getClassObject(), this.getClazz().getClassObject(),
            refField, entityManagerProvider, insteadOfTrigger);
        creatable.setTie(tie);
        return tie;
    }
    
    public EntityManagerProviderI getEntityManagerProvider()
    {
        return entityManagerProvider;
    }

    public InsteadOfAddRemoveTrigger getInsteadOfAddRemoveTrigger()
    {
        return insteadOfTrigger;
    }
    
    /**
     * Only topLevels have load ability - see block constructor. Block
     * already created when this is called now setInitialData done at
     * the OPEN event.
     */
    public void setInitialData(IndependentExtent object)
    {
        // Err.pr( "setInitialData called for " + this + " with " + object);
        if(object == null)
        {
            // Err.pr("..initialData being set NULL in Node");
            Err.error(
                "Cannot pass null to setInitialData - but can pass an empty collection");
        }
        else
        {
            /*
            * Will ALSO absolutely HAVE to error if the chiefObj is null!
            */
            if(chiefObj == null)
            {
                Err.error("Before setInitialData a Cell must have a SubRecordObj");
            }
            chiefObj.setInitialData(object);
        }
    }

    // Not working so why have?
    // public Object getActualList()
    // {
    // return chiefObj.getActualList();
    // }

    public NavExtent getDataRecords()
    {
        return chiefObj.getDataRecords();
    }

    public void setDefaultElement(Object defaultElement)
    {
        if(defaultElement == null)
        {
            Err.error("Cannot pass null to setDefaultElement");
        }
        else
        {
            chiefObj.setDefaultElement(defaultElement);
        }
    }

    public void setRecordObj(SubRecordObj chiefObj)
    {
        this.chiefObj = chiefObj;
    }

    public SubRecordObj getSubRecordObj()
    {
        return chiefObj;
    }

    /**
     * Filled in like any other property.
     */
    public AdaptersListI getAdapters()
    {
        return adapters;
    }

    public ArrayList getLookups()
    {
        return lookedupCells;
    }

    public LookupTiesManagerI getTiesManager()
    {
        return tiesManager;
    }

    /*
    public void setNewInstanceTrigger( NewInstanceTrigger t)
    {
      newInstanceTrigger = t;
    }
    public NewInstanceTrigger getNewInstanceTrigger()
    {
      return newInstanceTrigger;
    }
    */

    public boolean hasNewInstanceTrigger()
    {
        return (newInstanceTrigger != null);
    }

    public Object firePrimaryNewInstanceTrigger()
    {
        return newInstanceTrigger.getNewInstance( primaryEvent);
    }

    public Object fireSecondaryNewInstanceTrigger()
    {
        return newInstanceTrigger.getNewInstance( secondaryEvent);
    }
}
