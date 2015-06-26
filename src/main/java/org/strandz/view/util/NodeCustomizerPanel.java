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

import org.strandz.lgpl.widgets.ComponentLabelField;
import org.strandz.lgpl.widgets.Label;
import org.strandz.lgpl.widgets.TextField;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Dimension;

/**
 * This class
 *
 * @author Chris Murphy
 */
public class NodeCustomizerPanel extends JPanel
{
    private final static int ROW_HEIGHT = 100;
    private final static int SHORT_ROW_HEIGHT = 25;
    private final static int BORDER = 12;
    public JPanel scrolledPanel = new JPanel();
    private final Label lName = new org.strandz.lgpl.widgets.Label("Name: ");
    public TextField tfName = null;
    private final JPanel jpNameRow = new JPanel();
    private final Label lTitle = new org.strandz.lgpl.widgets.Label("Title: ");
    public TextField tfTitle = null;
    private final JPanel jpTitleRow = new JPanel();
    private final Label lTableControl = new Label("Table Control: ");
    public ComponentLabelField tfTableControl = null;
    public JButton btnRemoveTableControl = null;
    private final JPanel jpTableControlRow = new JPanel();
    public JCheckBox cbEnterQuery = new JCheckBox();
    private final JPanel jpEnterQueryRow = new JPanel();
    public JCheckBox cbExecuteSearch = new JCheckBox();
    private final JPanel jpExecuteSearchRow = new JPanel();
    public JCheckBox cbLoad = new JCheckBox();
    private final JPanel jpLoadRow = new JPanel();
    public JCheckBox cbUpdate = new JCheckBox();
    private final JPanel jpUpdateRow = new JPanel();
    public JCheckBox cbInsert = new JCheckBox();
    private final JPanel jpInsertRow = new JPanel();
    public JCheckBox cbDelete = new JCheckBox();
    private final JPanel jpDeleteRow = new JPanel();
    public JCheckBox cbPost = new JCheckBox();
    private final JPanel jpPostRow = new JPanel();
    public JCheckBox cbCommitReload = new JCheckBox();
    private final JPanel jpCommitReloadRow = new JPanel();
    public JCheckBox cbCommitOnly = new JCheckBox();
    private final JPanel jpCommitOnlyRow = new JPanel();
    public JCheckBox cbUp = new JCheckBox();
    private final JPanel jpUpRow = new JPanel();
    public JCheckBox cbDown = new JCheckBox();
    private final JPanel jpDownRow = new JPanel();
    public JCheckBox cbSetRow = new JCheckBox();
    private final JPanel jpSetRow = new JPanel();
    public JCheckBox cbEditInsertsBeforeCommit = new JCheckBox();
    private final JPanel jpEditInsertsBeforeCommitRow = new JPanel();
    public JCheckBox cbIgnoredChild = new JCheckBox();
    private final JPanel jpIgnoredChildRow = new JPanel();
    public JCheckBox cbCascadeDelete = new JCheckBox();
    private final JPanel jpCascadeDeleteRow = new JPanel();
    public JCheckBox cbFocusNode = new JCheckBox();
    private final JPanel jpFocusNodeRow = new JPanel();

    public NodeCustomizerPanel()
    {
        JScrollPane pane = new JScrollPane(scrolledPanel);
        setLayout(new BorderLayout());
        add(pane, BorderLayout.CENTER);
        scrolledPanel.setLayout(new BoxLayout(scrolledPanel, BoxLayout.Y_AXIS));
        scrolledPanel.setBorder(
            BorderFactory.createEmptyBorder(BORDER, BORDER, BORDER, BORDER));
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
        scrolledPanel.add(jpNameRow);
        dim = new Dimension((int) lTitle.getPreferredSize().getWidth(),
            SHORT_ROW_HEIGHT);
        tfTitle = new TextField();
        tfTitle.setHeight(SHORT_ROW_HEIGHT);
        tfTitle.setName("tfTitle");
        lTitle.setPreferredSize(dim);
        lTitle.setMaximumSize(dim);
        lTitle.setVerticalAlignment(SwingConstants.CENTER);
        jpTitleRow.setLayout(new BoxLayout(jpTitleRow, BoxLayout.X_AXIS));
        jpTitleRow.setAlignmentX(SwingConstants.LEFT);
        jpTitleRow.add(lTitle);
        jpTitleRow.add(tfTitle);
        scrolledPanel.add(jpTitleRow);
        dim = new Dimension((int) lTableControl.getPreferredSize().getWidth(),
            SHORT_ROW_HEIGHT);
        tfTableControl = new ComponentLabelField();
        tfTableControl.setHeight(SHORT_ROW_HEIGHT);
        tfTableControl.setName("tfTableControl");
        btnRemoveTableControl = new JButton("Remove");
        btnRemoveTableControl.setPreferredSize(dim);
        btnRemoveTableControl.setName("btnRemoveTableControl");
        lTableControl.setPreferredSize(dim);
        lTableControl.setMaximumSize(dim);
        lTableControl.setVerticalAlignment(SwingConstants.CENTER);
        jpTableControlRow.setLayout(
            new BoxLayout(jpTableControlRow, BoxLayout.X_AXIS));
        jpTableControlRow.setAlignmentX(SwingConstants.LEFT);
        jpTableControlRow.add(lTableControl);
        jpTableControlRow.add(tfTableControl);
        jpTableControlRow.add(btnRemoveTableControl);
        scrolledPanel.add(jpTableControlRow);
        cbEnterQuery.setText("EnterQuery");
        cbEnterQuery.setName("cbEnterQuery");
        jpEnterQueryRow.setLayout(new BoxLayout(jpEnterQueryRow, BoxLayout.X_AXIS));
        jpEnterQueryRow.setAlignmentX(SwingConstants.LEFT);
        jpEnterQueryRow.add(cbEnterQuery);
        jpEnterQueryRow.add(Box.createHorizontalGlue());
        scrolledPanel.add(jpEnterQueryRow);
        cbExecuteSearch.setText("ExecuteSearch");
        cbExecuteSearch.setName("cbExecuteSearch");
        jpExecuteSearchRow.setLayout(
            new BoxLayout(jpExecuteSearchRow, BoxLayout.X_AXIS));
        jpExecuteSearchRow.setAlignmentX(SwingConstants.LEFT);
        jpExecuteSearchRow.add(cbExecuteSearch);
        jpExecuteSearchRow.add(Box.createHorizontalGlue());
        scrolledPanel.add(jpExecuteSearchRow);
        cbLoad.setText("ExecuteLoad");
        cbLoad.setName("cbLoad");
        jpLoadRow.setLayout(new BoxLayout(jpLoadRow, BoxLayout.X_AXIS));
        jpLoadRow.setAlignmentX(SwingConstants.LEFT);
        jpLoadRow.add(cbLoad);
        jpLoadRow.add(Box.createHorizontalGlue());
        scrolledPanel.add(jpLoadRow);
        cbUpdate.setText("Update");
        cbUpdate.setName("cbUpdate");
        jpUpdateRow.setLayout(new BoxLayout(jpUpdateRow, BoxLayout.X_AXIS));
        jpUpdateRow.setAlignmentX(SwingConstants.LEFT);
        jpUpdateRow.add(cbUpdate);
        jpUpdateRow.add(Box.createHorizontalGlue());
        scrolledPanel.add(jpUpdateRow);
        cbInsert.setText("Insert");
        cbInsert.setName("cbInsert");
        jpInsertRow.setLayout(new BoxLayout(jpInsertRow, BoxLayout.X_AXIS));
        jpInsertRow.setAlignmentX(SwingConstants.LEFT);
        jpInsertRow.add(cbInsert);
        jpInsertRow.add(Box.createHorizontalGlue());
        scrolledPanel.add(jpInsertRow);
        cbDelete.setText("Delete");
        cbDelete.setName("cbDelete");
        jpDeleteRow.setLayout(new BoxLayout(jpDeleteRow, BoxLayout.X_AXIS));
        jpDeleteRow.setAlignmentX(SwingConstants.LEFT);
        jpDeleteRow.add(cbDelete);
        jpDeleteRow.add(Box.createHorizontalGlue());
        scrolledPanel.add(jpDeleteRow);
        cbPost.setText("Post");
        cbPost.setName("cbPost");
        jpPostRow.setLayout(new BoxLayout(jpPostRow, BoxLayout.X_AXIS));
        jpPostRow.setAlignmentX(SwingConstants.LEFT);
        jpPostRow.add(cbPost);
        jpPostRow.add(Box.createHorizontalGlue());
        scrolledPanel.add(jpPostRow);
        cbCommitReload.setText("CommitReload");
        cbCommitReload.setName("cbCommitReload");
        jpCommitReloadRow.setLayout(
            new BoxLayout(jpCommitReloadRow, BoxLayout.X_AXIS));
        jpCommitReloadRow.setAlignmentX(SwingConstants.LEFT);
        jpCommitReloadRow.add(cbCommitReload);
        jpCommitReloadRow.add(Box.createHorizontalGlue());
        scrolledPanel.add(jpCommitReloadRow);
        cbCommitOnly.setText("CommitOnly");
        cbCommitOnly.setName("cbCommitOnly");
        jpCommitOnlyRow.setLayout(new BoxLayout(jpCommitOnlyRow, BoxLayout.X_AXIS));
        jpCommitOnlyRow.setAlignmentX(SwingConstants.LEFT);
        jpCommitOnlyRow.add(cbCommitOnly);
        jpCommitOnlyRow.add(Box.createHorizontalGlue());
        scrolledPanel.add(jpCommitOnlyRow);
        cbUp.setText("Prev");
        cbUp.setName("cbUp");
        jpUpRow.setLayout(new BoxLayout(jpUpRow, BoxLayout.X_AXIS));
        jpUpRow.setAlignmentX(SwingConstants.LEFT);
        jpUpRow.add(cbUp);
        jpUpRow.add(Box.createHorizontalGlue());
        scrolledPanel.add(jpUpRow);
        cbDown.setText("Next");
        cbDown.setName("cbDown");
        jpDownRow.setLayout(new BoxLayout(jpDownRow, BoxLayout.X_AXIS));
        jpDownRow.setAlignmentX(SwingConstants.LEFT);
        jpDownRow.add(cbDown);
        jpDownRow.add(Box.createHorizontalGlue());
        scrolledPanel.add(jpDownRow);
        cbSetRow.setText("Set Row");
        cbSetRow.setName("cbSetRow");
        jpSetRow.setLayout(new BoxLayout(jpSetRow, BoxLayout.X_AXIS));
        jpSetRow.setAlignmentX(SwingConstants.LEFT);
        jpSetRow.add(cbSetRow);
        jpSetRow.add(Box.createHorizontalGlue());
        scrolledPanel.add(jpSetRow);
        cbEditInsertsBeforeCommit.setText("Edit Inserts before Commit");
        cbEditInsertsBeforeCommit.setName("cbEditInsertsBeforeCommit");
        jpEditInsertsBeforeCommitRow.setLayout(
            new BoxLayout(jpEditInsertsBeforeCommitRow, BoxLayout.X_AXIS));
        jpEditInsertsBeforeCommitRow.setAlignmentX(SwingConstants.LEFT);
        jpEditInsertsBeforeCommitRow.add(cbEditInsertsBeforeCommit);
        jpEditInsertsBeforeCommitRow.add(Box.createHorizontalGlue());
        scrolledPanel.add(jpEditInsertsBeforeCommitRow);
        cbIgnoredChild.setText("Ignored Child (by Controller)");
        cbIgnoredChild.setName("cbIgnoredChild");
        jpIgnoredChildRow.setLayout(
            new BoxLayout(jpIgnoredChildRow, BoxLayout.X_AXIS));
        jpIgnoredChildRow.setAlignmentX(SwingConstants.LEFT);
        jpIgnoredChildRow.add(cbIgnoredChild);
        jpIgnoredChildRow.add(Box.createHorizontalGlue());
        scrolledPanel.add(jpIgnoredChildRow);
        cbCascadeDelete.setText("Cascade Delete");
        cbCascadeDelete.setName("cbCascadeDelete");
        jpCascadeDeleteRow.setLayout(
            new BoxLayout(jpCascadeDeleteRow, BoxLayout.X_AXIS));
        jpCascadeDeleteRow.setAlignmentX(SwingConstants.LEFT);
        jpCascadeDeleteRow.add(cbCascadeDelete);
        jpCascadeDeleteRow.add(Box.createHorizontalGlue());
        scrolledPanel.add(jpCascadeDeleteRow);
        cbFocusNode.setText("Focus causes Node Change");
        cbFocusNode.setName("cbFocusNode");
        jpFocusNodeRow.setLayout(new BoxLayout(jpFocusNodeRow, BoxLayout.X_AXIS));
        jpFocusNodeRow.setAlignmentX(SwingConstants.LEFT);
        jpFocusNodeRow.add(cbFocusNode);
        jpFocusNodeRow.add(Box.createHorizontalGlue());
        scrolledPanel.add(jpFocusNodeRow);
        scrolledPanel.setName("NodeCustomizerPanel.scrolledPanel");
    }
}
