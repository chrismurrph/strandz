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
package org.strandz.view.books;

import org.strandz.lgpl.tablelayout.ModernTableLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

public class ButtonPanel extends JPanel
{
    private JButton bQuery;
    private JButton bInsert;
    private JButton bDelete;
    private JButton bPost;
    private JButton bShowData;
    private JButton bDebugTable;
    static final int GAP = 15;
    static final int BORDER = 15;
    static final int TEXT_HEIGHT = 23;

    public void init()
    {
        bQuery = new JButton();
        bQuery.setText( "Query");
        bInsert = new JButton();
        bInsert.setText( "Insert");
        bDelete = new JButton();
        bDelete.setText( "Delete");
        bPost = new JButton();
        bPost.setText( "Post");
        bShowData = new JButton();
        bShowData.setText( "Show Data");
        bDebugTable = new JButton();
        bDebugTable.setText( "Action");

        // Create a TableLayout for the panel
        double size[][] =
            {
                // Columns
                {ModernTableLayout.FILL, BORDER, 0.15, GAP, 0.15, GAP, 0.15, GAP, 0.15, GAP, 0.15, GAP, 0.15,
                    BORDER, ModernTableLayout.FILL},
                // Rows
                {ModernTableLayout.FILL, BORDER, TEXT_HEIGHT, BORDER, ModernTableLayout.FILL}
            };
        setLayout(new ModernTableLayout(size));
        bQuery.setName("bQuery");
        bInsert.setName("bInsert");
        bDelete.setName("bDelete");
        bPost.setName("bPost");
        bShowData.setName("bShowData");
        add(bQuery, "2, 2");
        add(bInsert, "4, 2");
        add(bDelete, "6, 2");
        add(bPost, "8, 2");
        add(bShowData, "10, 2");
        add(bDebugTable, "12, 2");
        setName("ButtonPanel");
    }

    public JButton getBQuery()
    {
        return bQuery;
    }

    public void setBQuery(JButton bQuery)
    {
        this.bQuery = bQuery;
    }

    public JButton getBInsert()
    {
        return bInsert;
    }

    public void setBInsert(JButton bInsert)
    {
        this.bInsert = bInsert;
    }

    public JButton getBDelete()
    {
        return bDelete;
    }

    public void setBDelete(JButton bDelete)
    {
        this.bDelete = bDelete;
    }

    public JButton getBShowData()
    {
        return bShowData;
    }

    public JButton getBPost()
    {
        return bPost;
    }

    public void setBPost(JButton bPost)
    {
        this.bPost = bPost;
    }

    public void setBShowData(JButton bShowData)
    {
        this.bShowData = bShowData;
    }

    public JButton getBDebugTable()
    {
        return bDebugTable;
    }

    public void setBDebugTable(JButton bDebugTable)
    {
        this.bDebugTable = bDebugTable;
    }
}
