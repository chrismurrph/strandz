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
package org.strandz.lgpl.extent;

import org.strandz.lgpl.note.JDONote;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.persist.EntityManagerProviderI;
import org.strandz.lgpl.persist.ORMTypeEnum;
import org.strandz.lgpl.util.Err;

import java.util.List;

abstract public class IndependentExtent extends VisibleExtent
{
    private EntityManagerProviderI entityManagerProvider;
    InsteadOfAddRemoveTrigger addRemoveTrigger;

    /**
     * Not just used for top-level lists, but any list. For instance may end
     * up inside a ReferenceExtent
     */
    public static IndependentExtent createIndependent(
        Object actualList, EntityManagerProviderI entityManagerProvider, 
        InsteadOfAddRemoveTrigger addRemoveTrigger)
    {
        Class collectionClass = actualList.getClass();
        // new MessageDlg("List class type is " + collectionClass.getName());
        int type = ascertainType(collectionClass);
        if(type == -1)
        {
            Err.error("Currently NavExtent does not support " + collectionClass);
        }

        /*
        * Whichever subclass of NavExtent actually construct
        * depends on what collectionType is set to by ascertainType
        */
        IndependentExtent ie = null;
        if(type == JAVA_UTIL_VECTOR)
        {
            ie = new ListExtent((List) actualList, entityManagerProvider, addRemoveTrigger);
        }
        else if(type == JAVA_UTIL_SET)
        {
            ie = new SetExtent(actualList, entityManagerProvider, addRemoveTrigger);
        }
        else if(type == JAVA_UTIL_COLLECTION)
        {
            ie = new CollectionExtent(actualList, entityManagerProvider, addRemoveTrigger);
        }
        else if(type == JAVA_ARRAY)
        {
            ie = new ArrayExtent((Object[]) actualList, entityManagerProvider, addRemoveTrigger);
        }
        else
        {
            Err.error(
                "Incomplete programming for IndependentExtent " + "of type: " + type);
        }
        /* Don't need, done in subclass constructor above
        ie.entityManagerProvider = entityManagerProvider;
        ie.addRemoveTrigger = addRemoveTrigger;
        ie.actualList = actualList;
        */
        // ie.backingList = backingList;
        return ie;
    }
    
    void chkAddRemoveTrigger(InsteadOfAddRemoveTrigger addRemoveTrigger)
    {
        if(addRemoveTrigger != null)
        {
            Err.error( "InsteadOfAddRemoveTrigger has not been coded for in " + this.getClass().getName() + ", but to do so would be a simple matter");
        }
    }

//  IndependentExtent()
//  {}

    IndependentExtent(EntityManagerProviderI entityManagerProvider, InsteadOfAddRemoveTrigger addRemoveTrigger)
    {
        this.entityManagerProvider = entityManagerProvider; 
        this.addRemoveTrigger = addRemoveTrigger;
    }

    /**
     * Return value not used where this method is called. If
     * was not informing JDO then this method would be abstract.
     */
    public boolean remove(Object obj)
    {
        Err.pr(JDONote.TOUCH_DELETED, "JDO informed of delete of CAN'T REFER TO obj!"/* + obj*/);
        if(entityManagerProvider.getEntityManager() == null)
        {
            Err.error("No entityManager for " + getClass().getName() + " ID: " + id);
        }
        entityManagerProvider.getEntityManager().deletePersistent( obj);
        return true;
    }

    abstract public boolean add(Object obj);

    abstract public boolean contains(Object elem);

    public void insert(Object obj, int index)
    {
        Err.pr(SdzNote.BG_ADD, "JDO informed of insert of " + obj);
        if(entityManagerProvider.getEntityManager() == null)
        {
            Err.pr(SdzNote.EM_BECOMES_NULL, "PersistenceUtils.createNullSdzEMI() should have been called");
            Err.error(SdzNote.EM_BECOMES_NULL, "Doing insert where need an em in " + this);
        }
        if(!entityManagerProvider.getEntityManager().getORMType().isCayenne())
        {
            if(entityManagerProvider.getEntityManager().isEntityManaged( obj))
            {
                Err.pr( "WARNING: DO is already entity managed: <" + obj + ">");
            }
            else
            {
                entityManagerProvider.getEntityManager().registerPersistent( obj);
            }
        }
        else
        {
            //The registerPersistent method does not work with Cayenne - it has to be
            //registered at the time it is created. With others such as JDO it is
            //convenient not to have to write a NewInstanceTrigger, and if have done
            //so and have already registered then no harm in doing it again. Although
            //as can see above we are now checking and warning.
        }
    }

    abstract public void removeElementAt(int index);
}
