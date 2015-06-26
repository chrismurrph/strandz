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
package org.strandz.view.fishbowl;

import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.widgets.data.MyJTable;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.Dimension;

public class ContactUI extends JPanel
{
    public MyJTable tableView;
    private JScrollPane scrollpane;
    // private JButton bInsert = new JButton( "New Contact");

    public ContactUI()
    {
        setPreferredSize(new Dimension(200, 200));
    }

    public void init()
    {
        tableView = new MyJTable();
        tableView.setName("ContactUI tableView");

        Border thisBorder1 = new EtchedBorder();
        Border thisBorder0 = new TitledBorder(thisBorder1, "Contacts");
        this.setBorder(thisBorder0);
        scrollpane = new JScrollPane(tableView);
        scrollpane.setBorder(new BevelBorder(BevelBorder.LOWERED));
        scrollpane.setPreferredSize(new Dimension(373, /* 190*/180));
        // scrollpane.setPreferredSize(
        // new Dimension(forSize.getPreferredSize().width-2,
        // forSize.getPreferredSize().height-2)
        // );
        /*
        Must be a swing note. When try to add scrollpane to the centre,
        it seems to add it to the centre of ClientUI

        this.setLayout( new BorderLayout());
        this.add( scrollpane, BorderLayout.CENTER);
        */
        add(scrollpane);
        // add( bInsert);
    }

    public void setPreferredSize(Dimension forSize)
    {
        double height = forSize.getHeight();
        double width = forSize.getWidth();
        if(height <= 10 || width <= 10)
        {
            Err.error(
                "forSize param of " + "ContactUI is only " + height + "(height) by "
                    + width + "(width)");
        }
        else
        {/*
       Err.pr( "Width and height of ContactUI are "
       + width + ", " + height);
       */}
        super.setPreferredSize(forSize);
    }
    /*
    public JButton getBInsert() {
    return bInsert;
    }

    public void setBInsert(JButton bInsert) {
    this.bInsert = bInsert;
    }
    */
}
