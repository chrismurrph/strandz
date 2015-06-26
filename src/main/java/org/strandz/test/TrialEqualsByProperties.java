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
package org.strandz.test;

import org.strandz.core.domain.WidgetClassifier;
import org.strandz.core.interf.SdzBagI;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.ReasonNotEquals;
import org.strandz.lgpl.util.SpecialClassLoader;
import org.strandz.lgpl.util.ComponentUtils;
import org.strandz.lgpl.util.Utils;

import javax.swing.JComponent;

/**
 * This class
 *
 * @author Chris Murphy
 */
public class TrialEqualsByProperties
{
    private String baseDir = "C:\\dev\\classes";
    private String specialPackage = "org.strandz.view.wombatrescue";
    private SdzBagI sbI1;
    private SdzBagI sbI2;
    private SpecialClassLoader scl;
    private static String filename1 = "C:\\sdz-zone\\dt\\wombatrescue\\RosterVolunteers.xml";
    private static String filename2 = "C:\\sdz-zone\\dt\\wombatrescue\\RosterVolunteersDifferent.xml";
    public static final boolean useLoader = true;

    public static void main(String[] args)
    {
        new TrialEqualsByProperties();
    }

    private TrialEqualsByProperties()
    {
        createLoaderForThread(true);
        sbI1 = (SdzBagI) Utils.loadXMLFromResource(filename1, this, false);
        sbI2 = (SdzBagI) Utils.loadXMLFromResource(filename2, this, false);
        Err.pr("sbI1: " + sbI1);
        Err.pr("sbI2: " + sbI2);

        JComponent[] panels1 = sbI1.getPanes();
        JComponent[] panels2 = sbI2.getPanes();
        if(panels1.length != panels2.length)
        {
            Err.error("Expected the size of the panels to be the same");
        }

        WidgetClassifier wc = new WidgetClassifier();
        ReasonNotEquals.turnOn(true);
        for(int i = 0; i < panels1.length; i++)
        {
            JComponent component1 = panels1[i];
            JComponent component2 = panels2[i];
            if(ComponentUtils.equalsByProperties(component1, component2, wc))
            {
                Err.pr("Found to be equal");
            }
            else
            {
                Err.pr("Found to be NOT equal: " + ReasonNotEquals.formatReasons());
                break;
            }
        }
        ReasonNotEquals.turnOn(false);
    }

    private void createLoaderForThread(boolean force)
    {
        if(force || scl == null)
        {
            scl = new SpecialClassLoader(this.getClass().getClassLoader(), baseDir,
                specialPackage, false);
            if(useLoader)
            {
                Thread.currentThread().setContextClassLoader(scl);
            }
        }
    }
}
