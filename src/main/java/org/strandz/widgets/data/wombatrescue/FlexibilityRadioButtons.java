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
package org.strandz.widgets.data.wombatrescue;

import org.strandz.lgpl.tablelayout.ModernTableLayout;
import org.strandz.data.wombatrescue.objects.Flexibility;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.BeansUtils;
import org.strandz.lgpl.widgets.OneOnlyGroup;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class FlexibilityRadioButtons extends JPanel
{
    private JRadioButton rbNoEvenings;
    private JRadioButton rbNoOvernights;
    private JRadioButton rbFlexible;
    private OneOnlyGroup flexibleGroup;
    
    private static boolean correctionNeeded = false;
    private static final boolean DEBUG = false;

    public void init()
    {
        rbNoEvenings = new JRadioButton();
        rbNoOvernights = new JRadioButton();
        rbFlexible = new JRadioButton();

        double size[][] =
            { // Columns
                {ModernTableLayout.FILL},
                // Rows
                {0.33, 0.33, 0.34}
            };
        setLayout(new ModernTableLayout(size));

        rbNoEvenings.setText(Flexibility.NO_EVENINGS.getName());
        rbNoEvenings.setName("rbNoEvenings");
        rbNoOvernights.setText(Flexibility.NO_OVERNIGHTS.getName());
        rbNoOvernights.setName("rbNoOvernights");
        rbFlexible.setText(Flexibility.FLEXIBLE.getName());
        rbFlexible.setName("rbFlexible");
        //Group the radio buttons.
        flexibleGroup = new OneOnlyGroup();
        flexibleGroup.add(rbNoEvenings);
        flexibleGroup.add(rbNoOvernights);
        flexibleGroup.add(rbFlexible);

        add(rbNoEvenings, "0, 0");
        add(rbNoOvernights, "0, 1");
        add(rbFlexible, "0, 2");
        setName("frbFlexibilityRadioButtons"); //silly name, but they all need prefixes
        setFlexibleGroup(flexibleGroup);
        setRbNoEvenings( rbNoEvenings);
        setRbNoOvernights( rbNoOvernights);
        setRbFlexible( rbFlexible);
    }

    public String getSelectedText()
    {
        String result = null;
        if(!BeansUtils.isDesignTime())
        {
            result = flexibleGroup.getSelectedText();
            pr("FlexibilityRadiobuttons.getSelectedText() to return " + result);
        }
        return result;
    }

    private void pr(String s)
    {
        if(DEBUG)
        {
            Err.pr(s);
        }
    }

    public void setSelectedText(String selectedText)
    {
        if(!BeansUtils.isDesignTime())
        {
            pr("FlexibilityRadiobuttons.setSelectedText() to " + selectedText);
            //Lost in transit, so we fix the problem here
            ButtonGroup bg = flexibleGroup.getRadioGroup();
            if(flexibleGroup.getRadioGroup().getButtonCount() == 0)
            {
                correctionNeeded = true;
                flexibleGroup.newButton(flexibleGroup.getRbNone());
                flexibleGroup.newButton(rbNoEvenings);
                flexibleGroup.newButton(rbNoOvernights);
                flexibleGroup.newButton(rbFlexible);
                //This will happen if the buttons didn't make it across (were not XMLable)
                if(bg.getButtonCount() != 4)
                {
                    Err.error("Have " + bg.getButtonCount() + " buttons when expected " + 4);
                }
            }
            else if(!correctionNeeded)
            {
                /*
                This will happen when NOT reading from XML, so don't want this error coming up
                Err.error( "ButtonGroup now transporting its buttons, so this is a reminder to get rid" +
                    " of the above superflous code, have " + bg.getButtonCount());
                */
                if(bg.getButtonCount() != 4)
                {
                    Err.error("Have " + bg.getButtonCount() + " buttons when expected " + 4);
                }
            }
            flexibleGroup.setSelectedText(selectedText);
        }
    }

    public boolean isEnabled()
    {
        boolean result = false;
        if(!BeansUtils.isDesignTime())
        {
            result = flexibleGroup.isEnabled();
        }
        return result;
    }

    public void setEnabled(boolean b)
    {
        if(!BeansUtils.isDesignTime())
        {
            flexibleGroup.setEnabled(b);
        }
    }

    public OneOnlyGroup getFlexibleGroup()
    {
        return flexibleGroup;
    }

    public void setFlexibleGroup(OneOnlyGroup flexibleGroup)
    {
        this.flexibleGroup = flexibleGroup;
    }

    public void setDebug(boolean b)
    {
    }

    /*
    * Without these properties, these buttons would not make it across the
    * XML transportation
    */

    public JRadioButton getRbNoEvenings()
    {
        return rbNoEvenings;
    }

    public void setRbNoEvenings(JRadioButton rbNoEvenings)
    {
        this.rbNoEvenings = rbNoEvenings;
    }

    public JRadioButton getRbNoOvernights()
    {
        return rbNoOvernights;
    }

    public void setRbNoOvernights(JRadioButton rbNoOvernights)
    {
        this.rbNoOvernights = rbNoOvernights;
    }

    public JRadioButton getRbFlexible()
    {
        return rbFlexible;
    }

    public void setRbFlexible(JRadioButton rbFlexible)
    {
        this.rbFlexible = rbFlexible;
    }
}
