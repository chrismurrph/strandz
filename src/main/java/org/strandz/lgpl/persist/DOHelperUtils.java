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

/**
 * The main point of this over-engineering is to make sure that
 * classes from jdo jar files are not loaded unless necessary.
 * The actual JDO code is almost certainly rubbish and doesn't
 * need to be done that way! This structure may become important
 * for talking to other POJO style implementations that do actually
 * require their own equality test.
 * <p/>
 * IIF we really need to we will be able to make this work for multi-
 * transaction, multi-threaded environments. There would only ever be
 * one transaction per thread, and one doh object would need to be
 * kept for each thread. The thread/transaction context would be globally
 * held here.
 */
public class DOHelperUtils
{
    private static final DOHelperI SIMPLE = new SimpleDOHelper();
    private static DOHelperI doh = SIMPLE;
    private static DOHelperI jdoDOH = null;
    private static SdzEntityManagerI em;
    
    private static int lastOrdinal;

    public static final int START = 1;
    public static final int STOP = 2;

    public static final boolean debugging = false;
    
    public static SdzEntityManagerI getEM()
    {
        return em;
    }

    public static boolean equalityTest
        (Object master,
         Object ref)
    {
        return doh.equalityTest(master, ref);
    }

    public static boolean equalsOrError
        (Object obj1,
         Object obj2)
    {
        return doh.equalsOrError(obj1, obj2);
    }

    /**
     * Keeps this object informed of the current DataStore version, so that
     * it can always have the right delegate ready to do the methods of DOHelperI.
     * Do not expect this to work when have two transactions working together.
     *
     * @param ordinal Errors may well occur if we start having multi-threaded
     *                DataStores
     */
    public static void transaction(int ordinal, SdzEntityManagerI em)
    {
        ORMTypeEnum enumId = em.getORMType();
        if(!(lastOrdinal + 1 == ordinal))
        {
            Err.error("Unexpected sequence");
        }
        if(debugging) Err.pr("No: " + ordinal + ", Version: " + enumId);
        if(doh.getVersion() != enumId)
        {
            if(enumId == ORMTypeEnum.JDO)
            {
                //'pooling' with the extra jdoDOH var. Making a static
                //final out of it would defeat the purpose of this class
                if(jdoDOH == null)
                {
                    doh = new JDODOHelper();
                    jdoDOH = doh;
                }
                else
                {
                    doh = jdoDOH;
                }
            }
            else if(enumId == ORMTypeEnum.CAYENNE_SERVER)
            {
                if(jdoDOH == null)
                {
                    doh = new CayenneServerDOHelper();
                    jdoDOH = doh;
                }
                else
                {
                    doh = jdoDOH;
                }
            }
            else if(enumId == ORMTypeEnum.CAYENNE_CLIENT)
            {
                if(jdoDOH == null)
                {
                    doh = new CayenneClientDOHelper();
                    jdoDOH = doh;
                }
                else
                {
                    doh = jdoDOH;
                }
            }
            else if(enumId.isORM())
            {
                Err.error( "Not yet coded for " + enumId);
            }
            else
            {
                doh = SIMPLE;
            }
        }
        lastOrdinal = ordinal;
        DOHelperUtils.em = em;
    }
}
