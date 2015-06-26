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
package org.strandz.applic.supersix.reports;

import org.strandz.lgpl.widgets.AcknowledgementsPanel;
import org.strandz.lgpl.util.PropertyUtils;

import java.util.Properties;
import java.util.HashMap;

public class HelpAboutHelper
{
    private String version;
    private AcknowledgementsPanel acknowledge;

    public HelpAboutHelper()
    {
        String propFileName = "property-files/supersix";
        Properties props = PropertyUtils.getPortableProperties(propFileName, this);
        version = PropertyUtils.getProperty("strandzVersionNumber", props);

        AcknowledgementsPanel.Bundle[] bundles = createAcknowledgementBundles();
        HashMap specificHeights = new HashMap();
        specificHeights.put(new Integer(0), new Integer(370));
        acknowledge = new AcknowledgementsPanel(bundles, specificHeights);
        acknowledge.init();
    }

    public AcknowledgementsPanel getAcknowledgementsPanel()
    {
        return acknowledge;
    }

    private AcknowledgementsPanel.Bundle[] createAcknowledgementBundles()
    {
        AcknowledgementsPanel.Bundle[] result = new AcknowledgementsPanel.Bundle[8];

        result[0] = new AcknowledgementsPanel.Bundle();
        result[0].name = "Super Six Soccer";
        //result[0].link = "www.strandz.org";
        result[0].iconFile = "images/Splash.jpg";
        result[0].text = "Administering Super Six Soccer" +
            "<br>Copyright (C) 2006<br>Chris Murphy<br>" +
            "Super Six Soccer comes with ABSOLUTELY NO WARRANTY.";
            //"This is free software, and you are welcome to redistribute it " +
            //"under certain conditions.";
        //result[0].license = "GNU General Public License (+ special exception)";

        result[1] = new AcknowledgementsPanel.Bundle();
        result[1].name = "Strandz version " + version;
        result[1].link = "www.strandz.org";
        result[1].iconFile = "images/strandz_img.jpg";
        result[1].text = "The 'least code' framework" +
            "<br>Copyright (C) 2006<br>Chris Murphy";
        result[1].license = "GNU General Public License (+ special exception)";

        result[2] = new AcknowledgementsPanel.Bundle();
        result[2].name = "Cayenne";
        result[2].link = "http://cayenne.apache.org/";
        result[2].iconFile = "images/cayenne_logo.gif";
        result[2].text = "Persistence framework providing object-relational mapping (ORM) and remoting services";
        result[2].license = "Apache License V2.0";

        result[3] = new AcknowledgementsPanel.Bundle();
        result[3].name = "TableLayout";
        result[3].link = "www.clearthought.info/software/tablelayout";
        result[3].iconFile = "images/TableLayoutLogo.gif";
        result[3].text = "Provides an intuitive way to specify component placement";
        result[3].license = "You may not distribute modified versions of the source";

        result[4] = new AcknowledgementsPanel.Bundle();
        result[4].name = "Foxtrot";
        result[4].link = "http://foxtrot.sourceforge.net";
        result[4].iconFile = "images/foxtrot.gif";
        result[4].text = "An easy and powerful API to use threads with Swing";
        result[4].license = "BSD License";

        result[5] = new AcknowledgementsPanel.Bundle();
        result[5].name = "MySql";
        result[5].link = "http://www.mysql.com/";
        result[5].iconFile = "images/mysql.gif";
        result[5].text = "A Relational Database";
        result[5].license = "GNU General Public License";

        /* This one was never really supposed to be used in real applications anyway
        result[6] = new AcknowledgementsPanel.Bundle();
        result[6].name = "Date Selector";
        result[6].link = "www.holub.com/software";
        result[6].iconFile = "images/Date_selector_dialog.gif";
        result[6].text = "This program contains Allen Holub's" +
        " Date_Selector calendar component.<br>" +
        "(c) 2003 Allen I. Holub. All Rights Reserved";
        result[6].license = "You may not redistribute the source code";
        */

        result[6] = new AcknowledgementsPanel.Bundle();
        result[6].name = "MDateEntryField";
        result[6].link = "http://web.ukonline.co.uk/mseries/";
        result[6].iconFile = "images/M2.gif";
        result[6].text = "A combobox style drop down calendar";
        result[6].license = "Lesser GNU Public Licence";

        result[7] = new AcknowledgementsPanel.Bundle();
        result[7].name = "BrowserLauncher";
        //previously/really http://browserlauncher.sourceforge.net
        result[7].link = "http://browserlaunch2.sourceforge.net";
        result[7].iconFile = "images/no_logo.gif";
        result[7].text = "Opens the default web browser for the current user of the system to the given URL";
        result[7].license = "May be redistributed or modified in any form without restrictions as long as ...";

        return result;
    }
}
