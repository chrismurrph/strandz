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

import org.strandz.lgpl.tablelayout.ModernTableLayout;
import org.strandz.lgpl.widgets.Label;
import org.strandz.lgpl.widgets.LabelField;
import org.strandz.lgpl.widgets.PanelList;
import org.strandz.lgpl.widgets.TextField;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SdzBagCustomizerPanel extends JPanel
{
    private final static int ROW_HEIGHT = 100;
    private final static int TABLE_ROW_HEIGHT = 80;
    private final static int SHORT_ROW_HEIGHT = 25;
    private final static int BORDER = 12;
    private final org.strandz.lgpl.widgets.Label lType = new org.strandz.lgpl.widgets.Label("Type:  ");
    public LabelField tfType = null;
    private final JPanel jpTypeRow = new JPanel();
    private final org.strandz.lgpl.widgets.Label lName = new Label("Name:  ");
    public LabelField tfName = null;
    private final JPanel jpNameRow = new JPanel();
    // private final Label lView = new Label( "View:  ");
    // public ComponentLabelField tfView = null;
    // private final JPanel jpViewRow = new JPanel();

    public LHSTableControlPanel pLHS = new LHSTableControlPanel();
    public BelowTableControlPanel pBelow = new BelowTableControlPanel();
    public PanelList lpPanes = null;
    private final JPanel jpFindGUIRow = new JPanel();
    private final org.strandz.lgpl.widgets.Label lPhysicalController = new Label("Physical Controller: ");
    public TextField tfPhysicalController = null;
    private final JPanel jpPhysicalControllerRow = new JPanel();
    private final Label lStatusBar = new Label("Status Bar: ");
    public TextField tfStatusBar = null;
    private final JPanel jpStatusBarRow = new JPanel();
    private SdzBagCustomizerPanel outer;

    public SdzBagCustomizerPanel()
    {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(BORDER, BORDER, BORDER, BORDER));
        outer = this;
    }

    public void init()
    {
        Dimension dim = new Dimension((int) lType.getPreferredSize().getWidth(),
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
        dim = new Dimension((int) lName.getPreferredSize().getWidth(),
            SHORT_ROW_HEIGHT);
        tfName = new LabelField();
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
        // dim = new Dimension(
        // (int)lView.getPreferredSize().getWidth(), SHORT_ROW_HEIGHT);
        // tfView = new ComponentLabelField();
        // tfView.setHeight( SHORT_ROW_HEIGHT);
        // tfView.setName( "tfView");
        // lView.setPreferredSize( dim);
        // lView.setMaximumSize( dim);
        // lView.setVerticalAlignment( SwingConstants.CENTER);
        // jpViewRow.setLayout( new BoxLayout( jpViewRow, BoxLayout.X_AXIS));
        // jpViewRow.setAlignmentX( SwingConstants.LEFT);
        // jpViewRow.add( lView);
        // jpViewRow.add( tfView);
        // add( jpViewRow);

        /*
        * GUI classes. Which classes can be selected as panels to go inside the
        * Controller.
        */
        dim = new Dimension((int) pLHS.getPreferredSize().getWidth(),
            TABLE_ROW_HEIGHT + 15);
        lpPanes = new PanelList();
        lpPanes.init();
        lpPanes.setHeight(TABLE_ROW_HEIGHT);
        lpPanes.setTableHeight(TABLE_ROW_HEIGHT - SHORT_ROW_HEIGHT);
        lpPanes.addSouthComponent(pBelow);
        pLHS.setPreferredSize(dim);
        pLHS.setMaximumSize(dim);
        jpFindGUIRow.setLayout(new BoxLayout(jpFindGUIRow, BoxLayout.X_AXIS));
        jpFindGUIRow.setAlignmentX(SwingConstants.LEFT);
        jpFindGUIRow.add(pLHS);
        jpFindGUIRow.add(lpPanes);
        add(jpFindGUIRow);
        dim = new Dimension((int) lPhysicalController.getPreferredSize().getWidth(),
            SHORT_ROW_HEIGHT);
        tfPhysicalController = new TextField();
        tfPhysicalController.setHeight(SHORT_ROW_HEIGHT);
        tfPhysicalController.setName("tfPhysicalController");
        lPhysicalController.setPreferredSize(dim);
        lPhysicalController.setMaximumSize(dim);
        lPhysicalController.setVerticalAlignment(SwingConstants.CENTER);
        jpPhysicalControllerRow.setLayout(
            new BoxLayout(jpPhysicalControllerRow, BoxLayout.X_AXIS));
        jpPhysicalControllerRow.setAlignmentX(SwingConstants.LEFT);
        jpPhysicalControllerRow.add(lPhysicalController);
        jpPhysicalControllerRow.add(tfPhysicalController);
        add(jpPhysicalControllerRow);
        dim = new Dimension((int) lStatusBar.getPreferredSize().getWidth(),
            SHORT_ROW_HEIGHT);
        tfStatusBar = new TextField();
        tfStatusBar.setHeight(SHORT_ROW_HEIGHT);
        tfStatusBar.setName("tfStatusBar");
        lStatusBar.setPreferredSize(dim);
        lStatusBar.setMaximumSize(dim);
        lStatusBar.setVerticalAlignment(SwingConstants.CENTER);
        jpStatusBarRow.setLayout(new BoxLayout(jpStatusBarRow, BoxLayout.X_AXIS));
        jpStatusBarRow.setAlignmentX(SwingConstants.LEFT);
        jpStatusBarRow.add(lStatusBar);
        jpStatusBarRow.add(tfStatusBar);
        add(jpStatusBarRow);
    }

    public class LHSTableControlPanel extends JPanel
    {
        private final org.strandz.lgpl.widgets.Label lFindGUI = new org.strandz.lgpl.widgets.Label("View:  ");
        // Now lets control this from the Context
        // public JButton bDelete = new JButton( "Delete");

        LHSTableControlPanel()
        {
            // lFindGUI.setVerticalAlignment( SwingConstants.TOP);
            BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
            setLayout(layout);
             /**/
            add(lFindGUI);
            lFindGUI.setName( "lFindGUI");
            // add( Box.createVerticalStrut( 3));
            // ** add( Box.createVerticalGlue());
            // ** add( Box.createVerticalGlue());
            // add( Box.createVerticalStrut( 1));
            // bInsert.setToolTipText( "Insert");
            // bInsert.setMargin( new Insets(0,0,0,0));
            /*
            bDelete.setToolTipText( "Delete");
            bDelete.setMargin( new Insets(0,0,0,0));
            //bInsert.setMnemonic( KeyEvent.VK_I);
            bDelete.setMnemonic( KeyEvent.VK_D);
            add( Box.createVerticalGlue());
            //add( bInsert);
            //add( Box.createVerticalGlue());
            add( bDelete);
            add( Box.createVerticalGlue());
            */
        }
    } // end class

    public class BelowTableControlPanel extends JPanel
        implements ActionListener
    {
        public JButton bCurrentPane = new JButton("Current Pane");
        public JTextField lCurrentPane = new JTextField();

        public BelowTableControlPanel()
        {
            // Create a TableLayout for the panel
            double size[][] = {{25, 0.25, 7, 0.25, ModernTableLayout.FILL}, // Columns
                {ModernTableLayout.FILL}}; // Rows
            setLayout(new ModernTableLayout(size));
            add(bCurrentPane, "1, 0");
            add(lCurrentPane, "3, 0");
            bCurrentPane.addActionListener(this);
            bCurrentPane.setName( "bCurrentPane");
            lCurrentPane.setName( "lCurrentPane");
        }

        public void actionPerformed(ActionEvent evt)
        {
            String value = (String) outer.lpPanes.list.getSelectedValue();
            // Err.pr( "SENDING IN " + value);
            pBelow.lCurrentPane.setText(value);
        }
    } // end class
}
