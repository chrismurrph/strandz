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

import org.strandz.core.domain.ControlSignatures;
import org.strandz.core.domain.IdEnum;
import org.strandz.lgpl.widgets.ObjComp;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.util.Err;

/**
 * A marker class for a NonVisualAttribute. Required so that
 * when copying and pasting we know to not collect a list.
 */
public class NonVisualFieldAttribute extends NonVisualAttribute
{
    Object comp;
    //IdEnum visualCounterpartIdEnum;

    public NonVisualFieldAttribute()
    {
        super();
        comp = new ObjComp();
    }

    public NonVisualFieldAttribute(String dataFieldName)
    {
        super(dataFieldName);
        comp = new ObjComp();
    }

    public NonVisualFieldAttribute(Attribute attrib)
    {
        super(attrib);
        comp = new ObjComp();
    }
    
    /**
     * This method is used in cases where we want to test out only the Strandz
     * part (ie. not the Swing part as well) of an application by swapping
     * NonVisualAttributes in in place of their applichousing counterparts. The type of
     * passed in component is important because we want to simulate exactly what will
     * happen when getItemValue() is called. We will make changes here as problems
     * arise. First problem encountered is that getItemValue() does not always return
     * null - sometimes false for instance. Thus we need to use
     * ControlSignatures.getBlankText()
     * One alternative which would not work would be be to just use the actual control.
     * It would not work because when you set a control's value the EDT is used, and
     * we don't want to involve this thread. Another problem is that a headless
     * exception would be thrown when running on a server. The whole point of having
     * non-applichousing attributes is to test your program without these two effects.
     *
     * @param component The actual control used in non-applichousing situations
     */
    public void setItem(Object component)
    {
        Err.pr(SdzNote.TIGHTEN_RECORD_VALIDATION, "NonVisualFieldAttribute getting a " + component);
        IdEnum visualCounterpartIdEnum = IdEnum.newField(component);
        comp = ControlSignatures.createNonVisualAlternative(visualCounterpartIdEnum);
    }

    /**
     * Where this attribute is a applichousing replacement it needs to be able to have
     * a doAdapter with a blanking policy. For applichousing attributes the doAdapter
     * turns to its itemAdapter, which we can't do here - all we have to go on
     * is the component, from which it is easy to get the IdEnum.
     * This method is overridden so that as soon as we have a doAdapter (and of
     * course this is a non-applichousing replacement) we set its blanking policy.
     */
    /*
    void setDOAdapter( DOAdapter doAdapter)
    {
      super.setDOAdapter( doAdapter);
      //Err.pr( SdzNote.tightenRecordValidation, "doAdapter being set for " + this);
  //    if(doAdapter != null && visualCounterpartIdEnum != null)
  //    {
  //      doAdapter.setBlankingPolicy(
  //          ControlSignatures.getBlankingPolicy( visualCounterpartIdEnum));
  //    }
    }
    */
    public Object getItem()
    {
        return comp;
    }

    /*
    public void setItemValue( Object value)
    {
      super.setItemValue( value);
      //comp.setTxt( value);
    }
    */

    /*
    public Object getItemValue()
    {
      Object result = null;
      Object value = comp.getTxt();
      if(value == null && visualCounterpartIdEnum != null)
      {
        result = ControlSignatures.getBlankText( visualCounterpartIdEnum);
      }
      else
      {
        result = value;
      }
      return result;
    }
    */
}
