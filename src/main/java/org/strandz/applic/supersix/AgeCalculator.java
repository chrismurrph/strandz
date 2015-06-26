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
package org.strandz.applic.supersix;

import org.strandz.core.interf.RuntimeAttribute;
import org.strandz.view.util.DTUtils;
import org.strandz.lgpl.util.TimeUtils;
import org.strandz.lgpl.util.Err;

import javax.swing.JLabel;
import java.util.Date;

public class AgeCalculator
{
    private RuntimeAttribute dobAttribute;
    private JLabel ageLabel;
    
    private static final boolean DEBUG = false;

    public AgeCalculator(RuntimeAttribute dobAttribute, JLabel ageLabel)
    {
        DTUtils.chkNotNull( dobAttribute.getItem());
        DTUtils.chkNotNull( ageLabel);
        this.dobAttribute = dobAttribute;
        this.ageLabel = ageLabel;
    }

    /**
     * All the triggers that call this no longer need to be done, unless for some reason you
     * want to manually implement a calculated field. To implement a calculated field by having
     * an attribute (and using the designer to do it), then instead of using a JLabel on the
     * panel use a ROJLabel. 
     */
    public void calculateAge( String reason)
    {
        Date dob = (Date)dobAttribute.getItemValue();
        if(dob != null)
        {
            int age = TimeUtils.yearsFromPastDate( dob);
            ageLabel.setText( "Age is " + age + " (born " + TimeUtils.getDisplayedFromDate( dob, TimeUtils.PROSE_DATE_PARSE_STRING + ")"));
        }
        else
        {
            ageLabel.setText( "No DOB so age can't be calculated");
        }
        if(DEBUG)
        {
            Err.pr( "Calculated age because <" + reason + ">");
        }
    }
}
