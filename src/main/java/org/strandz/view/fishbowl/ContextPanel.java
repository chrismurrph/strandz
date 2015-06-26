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

import org.strandz.lgpl.widgets.ListTextArea;
import org.strandz.lgpl.widgets.TextField;
import org.strandz.lgpl.widgets.data.Table;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.Dimension;

public class ContextPanel extends JPanel
{
    private final static int SEPERATOR_WIDTH = 10;
    private final static int ROW_HEIGHT = 100;
    private final static int SHORT_ROW_HEIGHT = 25;
    private final static int BORDER = 12;
    private final JLabel lFindGUI = new JLabel("GUI Classes: ");
    public Table tFindGUI = null;
    private final JPanel jpFindGUIRow = new JPanel();
    private final JLabel lFile = new JLabel("Beans File: ");
    public TextField tfFile = null;
    private final JPanel jpFileRow = new JPanel();
    private final JLabel lControllerUI = new JLabel("Controller User Interface: ");
    // public ListPanel taControllerUI = null;
    public ListTextArea taControllerUI = null;
    private final JPanel jpControllerUIRow = new JPanel();

    public ContextPanel()
    {
        // Err.pr( "ContextPanel being constructed with hashCode " + hashCode());
        /*
        * General GUI stuff
        */
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(BORDER, BORDER, BORDER, BORDER));
        // init();
    }

    public void init()
    {
        /*
        * First panel. GUI classes.
        */
        // Err.pr( "applic.startsimple.ui.ContextPanel INIT()");
        Dimension dim = new Dimension((int) lFindGUI.getPreferredSize().getWidth(),
            ROW_HEIGHT);
        tFindGUI = new Table();
        tFindGUI.setHeight(ROW_HEIGHT);
        lFindGUI.setPreferredSize(dim);
        lFindGUI.setMaximumSize(dim);
        lFindGUI.setVerticalAlignment(SwingConstants.TOP);
        jpFindGUIRow.setLayout(new BoxLayout(jpFindGUIRow, BoxLayout.X_AXIS));
        jpFindGUIRow.setAlignmentX(SwingConstants.LEFT);
        jpFindGUIRow.add(lFindGUI);
        // jpFindGUIRow.add( Box.createRigidArea( new Dimension( SEPERATOR_WIDTH, 0)));
        jpFindGUIRow.add(tFindGUI);
        add(jpFindGUIRow);
        /*
        * Second panel. File that will contain the manipulated beans.
        */
        dim = new Dimension((int) lFile.getPreferredSize().getWidth(),
            SHORT_ROW_HEIGHT);
        tfFile = new TextField();
        tfFile.setHeight(SHORT_ROW_HEIGHT);
        tfFile.setName("tfFile");
        lFile.setPreferredSize(dim);
        lFile.setMaximumSize(dim);
        lFile.setVerticalAlignment(SwingConstants.CENTER);
        jpFileRow.setLayout(new BoxLayout(jpFileRow, BoxLayout.X_AXIS));
        jpFileRow.setAlignmentX(SwingConstants.LEFT);
        jpFileRow.add(lFile);
        // jpFileRow.add( Box.createRigidArea( new Dimension( SEPERATOR_WIDTH, 0)));
        jpFileRow.add(tfFile);
        add(jpFileRow);
        /*
        * Third panel. Controller UI classes.
        */
        dim = new Dimension((int) lControllerUI.getPreferredSize().getWidth(),
            ROW_HEIGHT);
        taControllerUI = new ListTextArea(); /* new ListPanel();*/
        taControllerUI.setHeight(ROW_HEIGHT);
        lControllerUI.setPreferredSize(dim);
        lControllerUI.setMaximumSize(dim);
        lControllerUI.setVerticalAlignment(SwingConstants.TOP);
        jpControllerUIRow.setLayout(
            new BoxLayout(jpControllerUIRow, BoxLayout.X_AXIS));
        jpControllerUIRow.setAlignmentX(SwingConstants.LEFT);
        jpControllerUIRow.add(lControllerUI);
        // jpControllerUIRow.add( Box.createRigidArea( new Dimension( SEPERATOR_WIDTH, 0)));
        jpControllerUIRow.add(taControllerUI);
        add(jpControllerUIRow);
        // add( Box.createVerticalGlue());

    }
}
