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
package org.strandz.view.util;

import org.strandz.lgpl.widgets.LabelField;
import org.strandz.lgpl.widgets.TextField;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.Dimension;

/**
 * This panel is used for editing a Cell at DT.
 *
 * @author Chris Murphy
 */
public class CellCustomizerPanel extends JPanel
{
    private final static int ROW_HEIGHT = 100;
    private final static int SHORT_ROW_HEIGHT = 25;
    private final static int BORDER = 12;
    private final org.strandz.lgpl.widgets.Label lName = new org.strandz.lgpl.widgets.Label("Name: ");
    public TextField tfName = null;
    private final JPanel jpNameRow = new JPanel();
    private final org.strandz.lgpl.widgets.Label lClass = new org.strandz.lgpl.widgets.Label("Data Object: ");
    public LabelField tfClass = null;
    private final JPanel jpClassRow = new JPanel();
    private final org.strandz.lgpl.widgets.Label lRefField = new org.strandz.lgpl.widgets.Label("Lookup Reference Attribute: ");
    public TextField tfRefField = null;
    private final JPanel jpRefFieldRow = new JPanel();

    public CellCustomizerPanel()
    {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(BORDER, BORDER, BORDER, BORDER));
    }

    public void init()
    {
        Dimension dim = new Dimension((int) lName.getPreferredSize().getWidth(),
            SHORT_ROW_HEIGHT);
        tfName = new TextField();
        tfName.setHeight(SHORT_ROW_HEIGHT);
        tfName.setName("tfName");
        lName.setPreferredSize(dim);
        lName.setMaximumSize(dim);
        lName.setVerticalAlignment(SwingConstants.CENTER);
        jpNameRow.setLayout(new BoxLayout(jpNameRow, BoxLayout.X_AXIS));
        jpNameRow.setAlignmentX(SwingConstants.LEFT);
        jpNameRow.add(lName);
        jpNameRow.add(tfName);
        add(jpNameRow);
        dim = new Dimension((int) lClass.getPreferredSize().getWidth(),
            SHORT_ROW_HEIGHT);
        tfClass = new LabelField();
        tfClass.setHeight(SHORT_ROW_HEIGHT);
        tfClass.setName("tfClass");
        lClass.setPreferredSize(dim);
        lClass.setMaximumSize(dim);
        lClass.setVerticalAlignment(SwingConstants.CENTER);
        jpClassRow.setLayout(new BoxLayout(jpClassRow, BoxLayout.X_AXIS));
        jpClassRow.setAlignmentX(SwingConstants.LEFT);
        jpClassRow.add(lClass);
        jpClassRow.add(tfClass);
        add(jpClassRow);
        dim = new Dimension((int) lRefField.getPreferredSize().getWidth(),
            SHORT_ROW_HEIGHT);
        tfRefField = new TextField();
        tfRefField.setHeight(SHORT_ROW_HEIGHT);
        tfRefField.setName("tfRefField");
        lRefField.setPreferredSize(dim);
        lRefField.setMaximumSize(dim);
        lRefField.setVerticalAlignment(SwingConstants.CENTER);
        jpRefFieldRow.setLayout(new BoxLayout(jpRefFieldRow, BoxLayout.X_AXIS));
        jpRefFieldRow.setAlignmentX(SwingConstants.LEFT);
        jpRefFieldRow.add(lRefField);
        jpRefFieldRow.add(tfRefField);
        add(jpRefFieldRow);
    }
}
