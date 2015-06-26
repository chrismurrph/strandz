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
package org.strandz.lgpl.widgets;

import mseries.ui.MDateEntryField;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Assert;

import javax.swing.plaf.TextUI;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

public class DebugMDateEntryField extends MDateEntryField
{
    private static int times;
    
    public DebugMDateEntryField()
    {
        this(DateFormat.getDateInstance(DateFormat.SHORT));
    }

    public DebugMDateEntryField( DateFormat df)
    {
        super();
        display = new DebugMDateField(df)
        {
            public void setUI(TextUI ui)
            {
                super.setUI(ui);
                setBorder(null);
            }
        };
        setValue(new Date());
        display.addFocusListener(this);
        updateUI();
    }
    
    public Date getValue()
    {
        times++;
        if(times == 5)
        {
            Err.debug();
        }
        Date result = null;
        try
        {
            result = super.getValue();
        }
        catch(ParseException e)
        {
            Err.error( e);
        }
        Err.pr( "DebugMDateEntryField value getting is <" + result + "> times " + times);
        return result;
    }
    
    public void setValue( Date date) 
    {
        super.setValue( date);
        Assert.isTrue( date == display.getValue());
        Err.pr( "@@@ DebugMDateEntryField value set to " + date);
    }
}
