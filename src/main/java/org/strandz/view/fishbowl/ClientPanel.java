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

import org.strandz.lgpl.widgets.TextField;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.Dimension;

public class ClientPanel extends JPanel
{
    private final JLabel lTitle = new JLabel("Client");
    private final JPanel jpTitleRow = new JPanel();
    private final JLabel lName = new JLabel("Name: ");
    public TextField tfName = null;
    private final JPanel jpNameRow = new JPanel();
    private final JLabel lPhone = new JLabel("Phone: ");
    public TextField tfPhone = null;
    private final JPanel jpPhoneRow = new JPanel();
    private final JLabel lDescription = new JLabel("Description: ");
    public TextField tfDescription = null;
    private final JPanel jpDescriptionRow = new JPanel();

    public ClientPanel()
    {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(
            BorderFactory.createEmptyBorder(DoubleHeaderPanel.BORDER,
                DoubleHeaderPanel.BORDER, DoubleHeaderPanel.BORDER,
                DoubleHeaderPanel.BORDER));
    }

    void init()
    {
        // JLabel label = new JLabel();
        // label.setText( "Client");
        // add( label);
        Dimension dim = new Dimension((int) lTitle.getPreferredSize().getWidth(),
            DoubleHeaderPanel.SHORT_ROW_HEIGHT);
        lTitle.setPreferredSize(dim);
        lTitle.setMaximumSize(dim);
        jpTitleRow.setLayout(new BoxLayout(jpTitleRow, BoxLayout.X_AXIS));
        jpTitleRow.setAlignmentX(SwingConstants.LEFT);
        jpTitleRow.add(lTitle);
        // jpTitleRow.add( Box.createRigidArea( new Dimension( SEPERATOR_WIDTH, 0)));
        add(jpTitleRow);
        dim = new Dimension((int) lName.getPreferredSize().getWidth(),
            DoubleHeaderPanel.SHORT_ROW_HEIGHT);
        tfName = new TextField();
        tfName.setHeight(DoubleHeaderPanel.SHORT_ROW_HEIGHT);
        tfName.setName("tfName");
        lName.setPreferredSize(dim);
        lName.setMaximumSize(dim);
        lName.setVerticalAlignment(SwingConstants.CENTER);
        jpNameRow.setLayout(new BoxLayout(jpNameRow, BoxLayout.X_AXIS));
        jpNameRow.setAlignmentX(SwingConstants.LEFT);
        jpNameRow.add(lName);
        // jpNameRow.add( Box.createRigidArea( new Dimension( SEPERATOR_WIDTH, 0)));
        jpNameRow.add(tfName);
        add(jpNameRow);
        dim = new Dimension((int) lPhone.getPreferredSize().getWidth(),
            DoubleHeaderPanel.SHORT_ROW_HEIGHT);
        tfPhone = new TextField();
        tfPhone.setHeight(DoubleHeaderPanel.SHORT_ROW_HEIGHT);
        tfPhone.setName("tfPhone");
        lPhone.setPreferredSize(dim);
        lPhone.setMaximumSize(dim);
        lPhone.setVerticalAlignment(SwingConstants.CENTER);
        jpPhoneRow.setLayout(new BoxLayout(jpPhoneRow, BoxLayout.X_AXIS));
        jpPhoneRow.setAlignmentX(SwingConstants.LEFT);
        jpPhoneRow.add(lPhone);
        // jpPhoneRow.add( Box.createRigidArea( new Dimension( SEPERATOR_WIDTH, 0)));
        jpPhoneRow.add(tfPhone);
        add(jpPhoneRow);
        dim = new Dimension((int) lDescription.getPreferredSize().getWidth(),
            DoubleHeaderPanel.SHORT_ROW_HEIGHT);
        tfDescription = new TextField();
        tfDescription.setHeight(DoubleHeaderPanel.SHORT_ROW_HEIGHT);
        tfDescription.setName("tfDescription");
        lDescription.setPreferredSize(dim);
        lDescription.setMaximumSize(dim);
        lDescription.setVerticalAlignment(SwingConstants.CENTER);
        jpDescriptionRow.setLayout(
            new BoxLayout(jpDescriptionRow, BoxLayout.X_AXIS));
        jpDescriptionRow.setAlignmentX(SwingConstants.LEFT);
        jpDescriptionRow.add(lDescription);
        // jpDescriptionRow.add( Box.createRigidArea( new Dimension( SEPERATOR_WIDTH, 0)));
        jpDescriptionRow.add(tfDescription);
        add(jpDescriptionRow);

        Dimension d = new Dimension();
        d.setSize(200, 160);
        setPreferredSize(d);
        setName("Client Panel");
    }
}
