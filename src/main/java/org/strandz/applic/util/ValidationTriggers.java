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
package org.strandz.applic.util;

import mseries.ui.MDateEntryField;
import org.strandz.core.domain.event.ItemValidationTrigger;
import org.strandz.core.interf.RuntimeAttribute;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.TimeUtils;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.ValidationException;

import javax.swing.event.ChangeEvent;
import java.text.ParseException;

public class ValidationTriggers
{
    public static class DateValidationTrigger implements ItemValidationTrigger
    {
        protected RuntimeAttribute attribute;

        public DateValidationTrigger(RuntimeAttribute attribute)
        {
            this.attribute = attribute;
        }

        public void validateItem(ChangeEvent validationEvent)
            throws ValidationException
        {
            String txt = null;
            if(attribute.getItem() instanceof MDateEntryField)
            {
                txt = ((MDateEntryField) attribute.getItem()).getText();
            }
            /*
            else if(attribute.getItem() instanceof DatePicker)
            {
                JFormattedTextField tf = (JFormattedTextField)DatePickerMoreComplexMethods.getDisplay( 
                        (DatePicker)attribute.getItem());
                txt = tf.getText();
            }
            */
            else
            {
                Err.error( "Cannot validate a " + attribute.getItem().getClass().getName());
            }
            if(!Utils.isBlank(txt))
            {
                try
                {
                    Err.pr(SdzNote.MDATE_ENTRY_FIELD, "validating <" + txt + ">");
                    TimeUtils.DATE_FORMAT.parse(txt);
                }
                catch(ParseException ex)
                {
                    attribute.setInError(true);
                    throw new ValidationException("Date fails validation");
                }
            }
            else
            {
                Err.pr(SdzNote.MDATE_ENTRY_FIELD, "txt is blank so no point validating MDEF " + attribute.getName());
            }
            attribute.setInError(false);
        }
    }
    
    public static class WholeNumbersOnlyValidationTrigger implements ItemValidationTrigger
    {
        private RuntimeAttribute attribute;
        private String msg;

        /**
         * @param attribute The attribute that will be set in/out of being in-error
         * @param msg eg./ "Scores must be entered as numbers"
         */
        public WholeNumbersOnlyValidationTrigger(RuntimeAttribute attribute, String msg)
        {
            this.attribute = attribute;
            this.msg = msg;
        }

        public void validateItem(ChangeEvent validationEvent)
            throws ValidationException
        {
            String txt = (String) attribute.getItemValue();
            if(!attribute.isBlank())
            {
                try
                {
                    Integer.parseInt(txt);
                }
                catch(NumberFormatException ex)
                {
                    attribute.setInError(true);
                    throw new ValidationException( msg + ", you tried <" + txt + ">");
                }
            }
            attribute.setInError(false);
        }
    }
}
