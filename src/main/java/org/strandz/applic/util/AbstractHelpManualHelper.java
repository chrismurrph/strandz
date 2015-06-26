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

import org.strandz.lgpl.util.PropertyUtils;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.DisplayUtils;

import javax.help.JHelp;
import javax.help.HelpSet;
import javax.help.HelpSetException;
import java.util.Properties;
import java.net.URL;

abstract public class AbstractHelpManualHelper
{
    private String titleText;
    private String toolTipText;
    private JHelp jHelp;
    
    abstract protected String getPropertyFileName();
    abstract protected String getHelpResourceName();
    abstract protected boolean ignoreErrors();

    public AbstractHelpManualHelper()
    {
        String propFileName = getPropertyFileName();
        Properties props = PropertyUtils.getPortableProperties(propFileName, this);
        titleText = PropertyUtils.getProperty("titleText", props);
        toolTipText = PropertyUtils.getProperty("toolTipText", props);
        initHelp();
    }

    private void initHelp()
    {
        ClassLoader loader = this.getClass().getClassLoader();
        String helpResource = getHelpResourceName();
        URL helpSetURL = HelpSet.findHelpSet(loader, helpResource);
        try
        {
            HelpSet helpSet = new HelpSet(loader, helpSetURL);
            jHelp = new JHelp(helpSet);
            jHelp.setToolTipText(toolTipText + " Help"); //no evidence this working
        }
        catch(HelpSetException ex)
        {
            error("Cannot create help system with helpSetURL: " + helpSetURL + ", was trying with resource " + helpResource);
        }
    }

    public void displayHelp()
    {
        if(jHelp != null)
        {
            DisplayUtils.display(jHelp, false, titleText + " Help");
        }
        else
        {
            error( "Help System has not been configured, so nothing could be displayed");            
        }
    }

    public JHelp getPanel()
    {
        return jHelp;
    }
    
    private void error( String s)
    {
        if(ignoreErrors())
        {
            Err.pr( "Ignoring Help error: " + s);            
        }
        else
        {
            Err.error( s);            
        }
    }
}
