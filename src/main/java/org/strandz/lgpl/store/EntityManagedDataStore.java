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
package org.strandz.lgpl.store;

import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.persist.SdzEntityManagerI;
import org.strandz.lgpl.persist.DOHelperUtils;
import org.strandz.lgpl.persist.ORMTypeEnum;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.ErrorMsgContainer;

import java.util.Properties;

abstract public class EntityManagedDataStore extends DataStore
{
    SdzEntityManagerI em;
    private ErrorThrowerI errorThrower; 
    private String supportPhone;

    private static int counter;
    private static int times;

    public SdzEntityManagerI getEM()
    {
        return em;
    }
    
    public void setEM(SdzEntityManagerI em, ErrorMsgContainer err)
    {
        this.em = em;
        if(SdzNote.EMP_ERRORS.isVisible())
        {
            times++;
            Err.pr(SdzNote.EMP_ERRORS, "For dataStore, setting em to " + em + " times " + times);
            if(times == 0)
            {
                Err.stack();
            }
        }
        DOHelperUtils.transaction(++counter, em);
        getDomainQueries().setEM(em, err);
    }
    
    abstract public void flush( Class classes[]);
    protected void postConnection(){}

    public ErrorThrowerI getErrorThrower()
    {
        return errorThrower;
    }

    public void setErrorThrower(ErrorThrowerI errorThrower)
    {
        this.errorThrower = errorThrower;
    }

    public String getSupportPhone()
    {
        return supportPhone;
    }

    public void setSupportPhone(String supportPhone)
    {
        this.supportPhone = supportPhone;
    }
}
