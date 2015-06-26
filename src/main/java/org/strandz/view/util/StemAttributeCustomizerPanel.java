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

import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.widgets.ComboBox;
import org.strandz.lgpl.widgets.Label;
import org.strandz.lgpl.widgets.LabelField;
import org.strandz.lgpl.widgets.TextField;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StemAttributeCustomizerPanel extends JPanel
{
    private final static int ROW_HEIGHT = 100;
    private final static int SHORT_ROW_HEIGHT = 25;
    private final static int BORDER = 12;
    private final org.strandz.lgpl.widgets.Label lName = new org.strandz.lgpl.widgets.Label("Name: ");
    public TextField tfName = null;
    private final JPanel jpNameRow = new JPanel();
    private final Label lType = new org.strandz.lgpl.widgets.Label("Type:  ");
    public LabelField tfType = null;
    private final JPanel jpTypeRow = new JPanel();
    private final org.strandz.lgpl.widgets.Label lDOField = new org.strandz.lgpl.widgets.Label("DO Field:  ");
    public ComboBox cbDOField = null;
    private final JPanel jpDOFieldRow = new JPanel();
    private LocalActionListener cbListener = new LocalActionListener();

    public StemAttributeCustomizerPanel()
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
        dim = new Dimension((int) lType.getPreferredSize().getWidth(),
            SHORT_ROW_HEIGHT);
        tfType = new LabelField();
        tfType.setHeight(SHORT_ROW_HEIGHT);
        tfType.setName("tfType");
        lType.setPreferredSize(dim);
        lType.setMaximumSize(dim);
        lType.setVerticalAlignment(SwingConstants.CENTER);
        jpTypeRow.setLayout(new BoxLayout(jpTypeRow, BoxLayout.X_AXIS));
        jpTypeRow.setAlignmentX(SwingConstants.LEFT);
        jpTypeRow.add(lType);
        jpTypeRow.add(tfType);
        add(jpTypeRow);
        dim = new Dimension((int) lDOField.getPreferredSize().getWidth(),
            SHORT_ROW_HEIGHT);
        cbDOField = new ComboBox();
        // cbDOField.setEditable( true); //rm when developed
        cbDOField.setHeight(SHORT_ROW_HEIGHT);
        cbDOField.setName("cbDOField");
        lDOField.setPreferredSize(dim);
        lDOField.setMaximumSize(dim);
        lDOField.setVerticalAlignment(SwingConstants.CENTER);
        jpDOFieldRow.setLayout(new BoxLayout(jpDOFieldRow, BoxLayout.X_AXIS));
        jpDOFieldRow.setAlignmentX(SwingConstants.LEFT);
        jpDOFieldRow.add(lDOField);
        jpDOFieldRow.add(cbDOField);
        add(jpDOFieldRow);
        cbDOField.addActionListener(cbListener);
    }
    /*
    * This did not work either (first try was in Customizer)
    public void setVisible( boolean b)
    {
    super.setVisible( b);
    if(pUtils.isBlank( tfName.getText()))
    {
    tfType.requestFocusInWindow();
    }
    }
    */

    /**
     * Weirdest thing here is that selection events keep coming when
     * UN-customize (go back to the tree). Reason could be Strandz or
     * the ComboBox itself. Instead of investigating we remove the
     * listener after the first time it is used.
     */
    private class LocalActionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent evt)
        {
            if(evt.getID() == ActionEvent.ACTION_PERFORMED && evt.getModifiers() != 0)
            {
                /*
                Err.pr( "actionCommand: " + evt.getActionCommand());
                Err.pr( "ID: " + evt.getID());
                Err.pr( "when: " + evt.getWhen());
                Err.pr( "modifiers: " + evt.getModifiers());
                Err.alarm( "SELECTED: " + cbDOField.getSelectedItem());
                */
                if(Utils.isBlank(tfName.getText()))
                {
                    tfName.setText((String) cbDOField.getSelectedItem());
                }
                cbDOField.removeActionListener(cbListener);
            }
        }
    }
}
