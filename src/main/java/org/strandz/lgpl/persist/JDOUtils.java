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
package org.strandz.lgpl.persist;

import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.note.JDONote;

import javax.jdo.PersistenceManager;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.JDOFatalUserException;
import java.util.Collection;
import java.util.Properties;
import java.security.AccessControlException;


public class JDOUtils
{
    public static void makePersistentAll(PersistenceManager pm, Object obj)
    {
        if(pm != null)
        {
            if(obj instanceof Object[])
            {
                pm.makePersistentAll((Object[]) obj);
            }
            else if(obj instanceof Collection)
            {
                pm.makePersistentAll((Collection) obj);
                // Err.pr( "%% Done registerPersistentAll() to " + obj);
            }
            else
            {
                Err.error("Don't know how to registerPersistent a " + obj.getClass());
            }
        }
    }

    /**
     * See if two objects are equal from a DB pov.
     * <p/>
     * TODO What is diff b/ween getTransactionalObjectId() and getObjectId()
     * Also what happens with datastore identity?
     *
     * @param jdoDO1
     * @param jdoDO2
     * @return whether two DOs are equal in terms of their OID
     */
    public static boolean equals(Object jdoDO1, Object jdoDO2)
    {
        boolean result = false;
        Object id1 = JDOHelper.getTransactionalObjectId(jdoDO1);
        Object id2 = JDOHelper.getTransactionalObjectId(jdoDO2);
        if(id1 == null && id2 != null)
        {
            if(jdoDO1 != null)
            {
                Err.pr( jdoDO1 + " does not have JDO identity (is just a normal object) (jdoDO1)");
                //Err.stack();
            }
        }
        if(id1 != null && id2 == null)
        {
            if(jdoDO2 != null)
            {
                Err.pr( jdoDO2 + " does not have JDO identity (is just a normal object) (jdoDO2)");
                //Err.stack();
            }
        }
        if(Utils.equals(id1, id2))
        {
            result = true;
        }
        return result;
    }
    
    static SdzEntityManagerI getNewSdzEM( Properties properties)
    {
        SdzEntityManagerI result;
        PersistenceManagerFactory pmf = getNewPMF( properties);
        result = new JDOEntityManager(pmf);
        return result;
    }

    static PersistenceManagerFactory getNewPMF(Properties props)
    {
        PersistenceManagerFactory pmf = getConnection( props);
        if(JDONote.RELOAD_PM_KODO_BUG.isVisible())
        {
            boolean optimistic = pmf.getOptimistic();
            Err.pr( "Optimistic is " + optimistic + " for " + pmf);
            if(pmf.toString().indexOf( "org.jpox") == -1)
            {
                Assert.isTrue( optimistic, "Surely optimistic is true by default for most JDO vendors");
            }
            boolean multithreaded = pmf.getMultithreaded();
            Err.pr( "Multithreaded is " + multithreaded);
            boolean retainValues = pmf.getRetainValues();
            Err.pr( "RetainValues is " + retainValues);
            boolean restoreValues = pmf.getRestoreValues();
            Err.pr( "RestoreValues is " + restoreValues);
            //For Spring server Ming has asked:
            //javax.jdo.option.RetainValues: false
            //javax.jdo.option.NontransactionalRead: false
            boolean nonTransRead = pmf.getNontransactionalRead();
            Err.pr( "NontransactionalRead is " + nonTransRead);
        }
//        if(optimistic)
//        {
//            pmf.setOptimistic( false);
//        }
        //PersistenceManager pm = pmf.getPersistenceManager();
//        Err.pr( "pm created of type: " + pm.getClass().getName());
//        if(pm instanceof PersistenceManagerImpl)
//        {
//            PersistenceManagerImpl jpoxPM = (PersistenceManagerImpl)pm;
//            Err.pr( "pm: " + pm);
//            Err.pr( "pm, its pmf context shows props file is: " + jpoxPM.getPMFContext().getPmfConfiguration().getPropertiesFile());
//        }
        return pmf;
    }

    public static PersistenceManagerFactory getConnection(Properties props)
    {
        PersistenceManagerFactory result = null;
        if(props == null)
        {
            Err.error("Must specify properties to get a connection");
        }
        try
        {
            // problems were with IDE and jar files while moving JPOX JDO1->JDO2
            // SOLUTION - manually rebuild this class - then jarStrandz.xml
            result = JDOHelper.getPersistenceManagerFactory( props);
            if(JDONote.RELOAD_PM_KODO_BUG.isVisible())
            {
                Print.prMap(props);
                Err.pr( "Type of PMF got from above properties is " + result.getClass().getName());
            }
        }
        catch(JDOFatalUserException ex)
        {
            Print.prMap(props);
            Print.pr("CLASSPATH: " + System.getProperty("java.class.path"));
            //Not allowed in Web Start
            //Print.prMap(System.getProperties());
            Err.error(ex);
        }
        catch(AccessControlException ex)
        {
            // Print.prThrowable( ex);
            Err.error(ex);
        }
        return result;
    }
}
